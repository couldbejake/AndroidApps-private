package com.example.flappybird.gameObjects.levelDesigner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.gameObjects.CollectibleDeath;
import com.example.flappybird.gameObjects.CollectibleHighJump;
import com.example.flappybird.gameObjects.CollectibleItem;

public class DesignerCollectibleItem extends DesignerRenderable {

    private final CollectibleItem collectibleItem;
    private final FlappyBird flappyBird;
    // a class to hold collectible designer objects.
    public CollectibleItem.Collectible_type collectible_type;
    public boolean realItem;
    private Canvas canvas;

    public DesignerCollectibleItem(LevelDesignerCamera camera, FlappyBird game, boolean realItem, CollectibleItem.Collectible_type ct) {


        super(camera);

        // set the camera, flappyBird and collectible_type

        this.camera = camera;
        this.flappyBird = game;
        this.collectible_type = ct;

        // check which type of collectible

        switch (ct) {
            case HIGH_JUMP:
                this.collectibleItem = new CollectibleHighJump(game, 0.25, false);
                break;
            case DEATH:
                this.collectibleItem = new CollectibleDeath(game, 0.25, false);
                break;
            default:
                this.collectibleItem = null;
                break;
        }

        // changes depending on whether in the context of the
        // level editor

        this.realItem = realItem;
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        super.draw(screenResolution, canvas);
        this.canvas = canvas;
        this.collectibleItem.draw(screenResolution, canvas);

        // if in dev mode, draw a bounding box
        if (flappyBird.devMode) {
            drawBoundingBox();
        }
    }

    public void update(FlappyBird flappyBird) {

        // update the collectible item
        this.collectibleItem.update(flappyBird);

        // if a real item
        if (realItem) {

            // draw position within context of the draggable level editor
            this.collectibleItem.set_x((int) this.get_rendering_x());
            this.collectibleItem.set_y((int) this.get_rendering_y());

        } else {

            // draw relative to the screen's position as a UI element

            this.collectibleItem.set_x(this.get_x());
            this.collectibleItem.set_y(this.get_y());
        }
    }

    // draws a bounding box in red around the item
    public void drawBoundingBox() {
        Rect rect = get_collision_rect();

        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
    }

    // generates a collision Rect.
    public Rect get_collision_rect() {
        int collision_gutter = 20;

        double rectX;
        double rectY;
        double rectWidth;
        double rectHeight;

        // if a real item

        if (realItem) {

            // draw the item with respect to the editor view.

            rectX = (float) this.get_rendering_x() - collision_gutter;
            rectY = (float) this.get_rendering_y() - collision_gutter;
            rectWidth = (float) (this.get_rendering_x() + this.collectibleItem.collectibleTexture.getWidth()) + collision_gutter;
            rectHeight = (float) (this.get_rendering_y() + this.collectibleItem.collectibleTexture.getHeight()) + collision_gutter;
        } else {

            // draw in respect to the screen view.

            rectX = (float) this.get_x() - collision_gutter;
            rectY = (float) this.get_y() - collision_gutter;
            rectWidth = (float) (this.get_x() + this.collectibleItem.collectibleTexture.getWidth()) + collision_gutter;
            rectHeight = (float) (this.get_y() + this.collectibleItem.collectibleTexture.getHeight()) + collision_gutter;
        }
        Rect rect = new Rect((int) rectX, (int) rectY, (int) rectWidth, (int) rectHeight);

        return rect;
    }

    // checks if the Collectible item was clicked.
    public boolean collides(float x, float y) {

        Rect rect = get_collision_rect();

        return rect.contains((int) x, (int) y);
    }

    // getter for collectible item.

    public CollectibleItem get_collectible() {
        return this.collectibleItem;
    }

    // gets collectible item types.

    public CollectibleItem.Collectible_type get_collectible_type() {
        return this.collectible_type;
    }

    public String get_collectible_type_string() {
        return this.collectible_type.name();
    }
}
