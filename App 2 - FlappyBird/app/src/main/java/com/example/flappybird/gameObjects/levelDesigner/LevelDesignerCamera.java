package com.example.flappybird.gameObjects.levelDesigner;

import android.graphics.Canvas;
import android.util.Pair;
import android.view.MotionEvent;

public class LevelDesignerCamera {
    public Pair<Integer, Integer> screenResolution;
    int viewPortX = 1;
    int viewPortY = 0;
    double dragStartX = 0;
    double dragStartY = 0;
    double viewPortBeforeX = 0;
    double viewPortBeforeY = 0;
    int levelWidth;
    private Canvas canvas;


    // set the camera based off the level width
    public LevelDesignerCamera(int levelWidth) {

        this.levelWidth = levelWidth;
    }

    public void onDragStart(LevelDesigner levelDesigner, MotionEvent event) {

        // handle dragging using x, dragStartX, viewPortX and event.getX

        dragStartX = event.getX();
        dragStartY = event.getY();
        viewPortBeforeX = this.viewPortX;
        viewPortBeforeY = this.viewPortY;
    }

    public void onDrag(LevelDesigner levelDesigner, MotionEvent event) {

        // allow dragging of the screen faster than the game level.

        int deltaX = (int) ((event.getX() - dragStartX) * 1.1);
        int deltaY = (int) ((event.getY() - dragStartY) * 1.1);

        // bound the new x y position

        int newX = (int) -((deltaX + viewPortBeforeX));
        int newY = (int) -((deltaX + viewPortBeforeY));
        if (newX < 0) { newX = 0; }
        if (newX > this.levelWidth) { newX = this.levelWidth; }

        this.viewPortX = -newX;

        // this.viewPortY = -newY; // disable y scrolling as not required.

    }

    public void onDragEnd(LevelDesigner levelDesigner, MotionEvent event) {

    }

    /*

        Getters and setters

     */
    public void setResolution(Pair<Integer, Integer> screenResolution) {
        this.screenResolution = screenResolution;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
