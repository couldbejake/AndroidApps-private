package com.example.flappybird.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Pair;

import com.example.flappybird.FlappyBird;

import java.util.ArrayList;

public class SubMenu {

    private final ArrayList<Button> buttons;
    private final FlappyBird flappyBird;
    private final Context context;
    private Pair<Integer, Integer> screenResolution = new Pair<Integer, Integer>(0, 0);

    public SubMenu(FlappyBird flappyBird, Context context) {

        this.context = context;
        this.flappyBird = flappyBird;
        this.buttons = new ArrayList<Button>();
    }


    public Button createButton(int y_position, ButtonContext bcontext, Button.ButtonCallbackInterface buttonCallbackInterface) {
        Button button = new Button(flappyBird, context, y_position, bcontext, buttonCallbackInterface);
        this.buttons.add(button);
        return button;
    }


    public void update(FlappyBird flappyBird) {

    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas, int startPosition) {
        this.screenResolution = screenResolution;
        for (Button button : buttons) {
            button.draw(screenResolution, canvas);
        }
    }

    public boolean onTouch(float x, float y) {
        for (Button button : buttons) {
            if (button.isInBounds(x, y)) {
                button.callback.onClick();
                return true;
            }
        }
        return false;
    }


}

