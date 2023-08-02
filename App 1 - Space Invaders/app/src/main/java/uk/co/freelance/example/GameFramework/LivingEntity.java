package uk.co.reading.example.GameFramework;

import android.graphics.Canvas;

// A living entity for storing objects with an X and Y position.

public class LivingEntity {
    protected float xPos = -1;
    protected float yPos = -1;

    public void draw(Canvas c) {
        // to be implemented
    }

    public LivingEntity(){

    }

    public LivingEntity(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }
}