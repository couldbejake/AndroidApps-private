package uk.ac.reading.sis05kol.GameFramework;

import android.graphics.RectF;

public class BAnimationElement {

    // set the x and y positions in the background.
    // the following are for the light trails
    private float xPos = 0;
    private float yPos = 0;

    // sets the width and height of each trail.
    private float width = 100;
    private float height = 100;

    // sets the speed of the trail.
    private float speed = 10;

    // a constructor for the background animation element.
    public BAnimationElement(int xPos, int yPos, int width, int height, int speed) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    // get's a RectF for the position, in order to display it on a canvas.
    public RectF get_position() {
        return new RectF(
                xPos, yPos, xPos + width, yPos + height
        );
    }

    // when the game ticks, move the bar down.
    public void tick() {
        this.yPos += this.speed;
    }

    // get the bar's Y position.
    public float get_yPos() {
        return this.yPos;
    }
}
