package com.example.flappybird.gameObjects.levelDesigner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.gameObjects.Background;

import java.util.ArrayList;

public class LevelDesigner {

    // creates a parallax effect between the foreground and background.
    private static final double PARALLAX_MULTIPLIER = 1.3;
    public final ArrayList<DesignerCollectibleItem> UICollectibles;
    private final DesignerUI designerUI;
    private final ArrayList<DesignerCollectibleItem> collectibleItems;
    private final LevelDesignerCamera camera;
    private final FlappyBird game;
    private final Context context;
    private final ArrayList<DesignerPipe> pipeList;
    public DesignerSaver designerSaver;
    Background background;

    // sets a default level width
    int levelWidth = 15000;
    Boolean first_load = true;
    Boolean loading = true;
    private ArrayList<DesignerRenderable> objectsInDrawingDistance;
    private DragContext currentDragContext = DragContext.NONE;
    private DesignerPipe currentDragPipe;
    private DesignerCollectibleItem currentDragCollectible;
    private Canvas canvas;

    public LevelDesigner(FlappyBird game, Context context) {
        camera = new LevelDesignerCamera(levelWidth);

        background = new Background(game, false);
        background.set_x_scroll(camera.viewPortX);

        // a class that can save and load games
        this.designerSaver = new DesignerSaver(context);

        this.game = game;
        this.context = context;
        this.pipeList = new ArrayList<DesignerPipe>();

        this.objectsInDrawingDistance = new ArrayList<DesignerRenderable>();
        this.UICollectibles = new ArrayList<DesignerCollectibleItem>();
        this.designerUI = new DesignerUI(game, context, this, camera);

        this.collectibleItems = new ArrayList<DesignerCollectibleItem>();
    }

    private void onBegin() {

        // decide the pipe count based on the game width

        int pipeCount = (int) Math.floor((levelWidth - game.getWidth()) / game.getWidth());

        Log.e("Pipe Count", pipeCount + "");

        // generates the pipes.

        // in the future, this could be multi-threaded.

        for (int i = 0; i < pipeCount; i++) {
            DesignerPipe pipe = new DesignerPipe(camera, this.game, 0);
            pipe.set_x((canvas.getWidth() * i));
            pipe.getPipe().jump_gap = 600;

            pipeList.add(pipe);
        }

        designerUI.onBegin();
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {

        this.canvas = canvas;

        // call onBegin on the first load

        if (first_load) {
            this.onBegin();
            first_load = false;
        }

        camera.setCanvas(canvas);
        camera.setResolution(screenResolution);

        // draw the background

        background.draw(canvas, true);

        // create a parallax effect for the background, and use modulo to infinitely draw the texture

        background.set_x_scroll((int) (camera.viewPortX * PARALLAX_MULTIPLIER % background.tex1.getWidth()));

        drawRelativeTest(canvas);

        // holds objects in drawing distance to save time

        this.objectsInDrawingDistance = new ArrayList<DesignerRenderable>();

        // for each pipe in the list

        for (DesignerPipe pipe : pipeList) {

            // if within drawing distance
            if (pipe.in_rendering_distance()) {

                pipe.draw(screenResolution, canvas);
                this.objectsInDrawingDistance.add(pipe);

            }
        }

        // for each collectible, check whether in rendering distance.

        for (DesignerCollectibleItem collectible : this.collectibleItems) {
            if (collectible.in_rendering_distance() || collectible instanceof DesignerCollectibleItem) {
                collectible.draw(screenResolution, canvas);
                this.objectsInDrawingDistance.add(collectible);
            }
        }

        // loop through objects within the viewport.
        // TODO:
        designerUI.draw(screenResolution, canvas);
    }

    public void update(FlappyBird flappyBird) {
        background.update();

        // update pipes and collectibles

        for (DesignerPipe pipe : pipeList) {
            pipe.update(flappyBird);
        }

        for (DesignerCollectibleItem collectible : this.collectibleItems) {
            collectible.update(flappyBird);
        }

        designerUI.update();
    }

    // when a drag starts
    public void onDragStart(MotionEvent event) {

        // if the UI is clicked, do not affect anything else

        if (designerUI.onTouch(event.getX(), event.getY())) {
            return;
        }

        // for each collectible item

        for (DesignerCollectibleItem collectible : this.designerUI.UICollectibles) {

            // if the collectible collides with the player mouse

            if (collectible.collides(event.getX(), event.getY())) {

                // set the context to dragging a collectible

                currentDragContext = DragContext.CREATE_COLLECTIBLE;

                // create a copy of the collectible and set it to the context

                DesignerCollectibleItem draggableItem = new DesignerCollectibleItem(camera, game, true, collectible.collectible_type);

                this.collectibleItems.add(draggableItem);
                this.currentDragCollectible = draggableItem;

                return;
            }
        }

        // render all objects in render distance

        for (DesignerRenderable renderable : objectsInDrawingDistance) {

            // if instanceOf DesignerPipe, render.

            if (renderable instanceof DesignerPipe) {
                if (renderable.collides(event.getX(), event.getY())) {
                    currentDragContext = DragContext.ADJUST_PIPE;
                    currentDragPipe = (DesignerPipe) renderable;
                    currentDragPipe.getPipe().darken_image = true;
                    currentDragPipe.getPipe().setGapHeight((int) event.getY() - currentDragPipe.getPipe().jump_gap / 2);
                    return;
                } else {
                    ((DesignerPipe) renderable).getPipe().darken_image = false;
                }
            }

        }

        // if no other item is clicked, set context to dragging the view.

        camera.onDragStart(this, event);
        currentDragContext = DragContext.SCROLL;

        return;
    }


    /*

        Handle UI interactions.

     */

    // when the user drags the screen
    public void onDrag(MotionEvent event) {

        // get the drag context

        switch (currentDragContext) {

            case SCROLL:

                // if dragging, call the camera drag event

                camera.onDrag(this, event);
                break;

            case ADJUST_PIPE:

                // if adjusting pipe, set the pipe height to the Y position minus half the gap

                int yPos = (int) event.getY() - currentDragPipe.getPipe().jump_gap / 2;
                currentDragPipe.getPipe().setGapHeight(yPos);
                break;

            case CREATE_COLLECTIBLE:

                // if creating a collectible
                // set realItem to false, so that the position is relative

                this.currentDragCollectible.realItem = false;
                this.currentDragCollectible.set_x((int) event.getX());
                this.currentDragCollectible.set_y((int) event.getY());

                break;
            case NONE:
                Log.e("Oops!", "Drag without anything selected?!");
                break;
        }


    }

    public void onDragEnd(MotionEvent event) {


        // when the drag ends

        switch (currentDragContext) {

            case SCROLL:

                // call the drag end event on the camera when the drag ends.

                camera.onDragEnd(this, event);

                break;

            case ADJUST_PIPE:

                // set the pipe to darkened if not dragging.

                currentDragPipe.getPipe().darken_image = false;

                break;
            case CREATE_COLLECTIBLE:

                // reset the collectible if drag ends

                // modify the x, y position by the absolute game level position

                this.currentDragCollectible.realItem = true;
                this.currentDragCollectible.add_x(-camera.viewPortX);
                this.currentDragCollectible.add_y(-camera.viewPortY);
                currentDragContext = DragContext.NONE;
                break;

        }

        currentDragContext = DragContext.NONE;
    }

    public void drawRelativeTest(Canvas canvas) {

        // if devMode turn on bounding box.

        if (game.devMode) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setAlpha(150);
            paint.setTextSize(50);
            for (int i = 0; i < 40; i++) {
                canvas.drawText(i + "", camera.viewPortX + (canvas.getWidth() / 2 * i) + 30, 100, paint);
            }
        }
    }

    /*
        Setters and Getters

     */

    public ArrayList<DesignerPipe> get_pipe_list() {
        return this.pipeList;
    }

    public ArrayList<DesignerCollectibleItem> get_collectibles() {
        return this.collectibleItems;
    }

    public enum DragContext {
        NONE,
        SCROLL,
        ADJUST_PIPE,
        CREATE_COLLECTIBLE
    }
}
