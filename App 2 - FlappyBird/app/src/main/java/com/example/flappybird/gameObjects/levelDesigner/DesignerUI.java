package com.example.flappybird.gameObjects.levelDesigner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;

import androidx.core.content.ContextCompat;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;
import com.example.flappybird.UI.Button;
import com.example.flappybird.UI.ButtonContext;
import com.example.flappybird.gameObjects.CollectibleItem;

import java.util.ArrayList;

public class DesignerUI {

    // holds an array of collectible objects
    public final ArrayList<DesignerCollectibleItem> UICollectibles;
    private final ArrayList<Button> buttons;
    private final int defaultButtonColor;
    private final int UIColor;
    private final LevelDesignerCamera camera;
    private final LevelDesigner designer;
    private final FlappyBird flappyBird;
    private final Context context;
    private Pair<Integer, Integer> screenResolution;
    private Canvas canvas;

    /*
        Defines the UI for the editor view
     */
    public DesignerUI(FlappyBird flappyBird, Context context, LevelDesigner designer, LevelDesignerCamera camera) {
        this.UIColor = ContextCompat.getColor(context, R.color.button_translucent);

        this.flappyBird = flappyBird;
        this.buttons = new ArrayList<Button>();
        this.context = context;
        this.camera = camera;
        this.designer = designer;
        this.defaultButtonColor = ContextCompat.getColor(context, R.color.button_translucent);

        this.UICollectibles = new ArrayList<DesignerCollectibleItem>();

        // Draw a JUMP UI draggable object in the editor view.

        DesignerCollectibleItem test = new DesignerCollectibleItem(camera, flappyBird, false, CollectibleItem.Collectible_type.HIGH_JUMP);
        test.set_x(450);
        test.set_y(25);
        this.UICollectibles.add(test);

        // Draw a DEATH UI draggable object in the editor view

        DesignerCollectibleItem test2 = new DesignerCollectibleItem(camera, flappyBird, false, CollectibleItem.Collectible_type.DEATH);
        test2.set_x(630);
        test2.set_y(25);
        this.UICollectibles.add(test2);
    }


    public void onBegin() {

        // create Menu buttons through abstract classes

        ButtonContext bcontext0 = new ButtonContext(defaultButtonColor, "Menu");
        createButton(20, 20, 180, 100, bcontext0, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                Log.e("Clicked", "∞");
                flappyBird.set_game_state(FlappyBird.GameState.MENU);
            }
        });

        ButtonContext bcontext1 = new ButtonContext(defaultButtonColor, "Save");
        createButton(220, 20, 180, 100, bcontext1, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                Log.e("Clicked", "∞");
                designer.designerSaver.saveGame(designer);
            }
        });
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {

        this.screenResolution = screenResolution;
        this.canvas = canvas;

        // draw the background for the top bar

        this.drawTopbar();

        // draw the buttons
        for (Button button : buttons) {
            button.draw(screenResolution, canvas);
        }
        for (DesignerCollectibleItem item : UICollectibles) {
            item.draw(screenResolution, canvas);
        }
    }

    // update all collectible items

    public void update() {
        for (DesignerCollectibleItem item : UICollectibles) {
            item.update(flappyBird);
        }
    }

    // on touch, check if any button is clicked
    public boolean onTouch(float x, float y) {
        for (Button button : buttons) {
            if (button.isInBounds(x, y)) {
                button.callback.onClick();
                return true;
            }
        }
        return false;
    }

    // a function to create a button
    public Button createButton(int x, int y, int width, int height, ButtonContext bcontext, Button.ButtonCallbackInterface buttonCallbackInterface) {
        Button button = new Button(flappyBird, context, x, y, width, height, bcontext, buttonCallbackInterface);
        this.buttons.add(button);
        return button;
    }

    // draws the top bar
    public void drawTopbar() {
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);

        int x1 = 0;
        int y1 = 0;
        int x2 = screenResolution.first;
        int y2 = 150;

        canvas.drawRect(x1, y1, x2, y2, paint);
        paint.setStrokeWidth(10);
        paint.setColor(this.UIColor);
        canvas.drawRect(x1, y1, x2, y2, paint);

        canvas.drawRect(x1, y1, x2, y2, paint);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x1, y1, x2, y2, paint);
    }


}
