package uk.co.reading.example.GameFramework;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlayerBullet {
    private float xPos = 0;
    private float yPos = 0;

    // velocity values for the bullets, initially, move upwards.
    private float yVel = 50;
    private float xVel = 0;

    public PlayerBullet(int mCanvasWidth, int mCanvasHeight, float xPos) {
        this.xPos = xPos;
        this.yPos = mCanvasHeight - 400;
    }
    public void render(Canvas c) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(235, 65, 50));
        paint.setStyle(Paint.Style.FILL);
        c.drawCircle(this.xPos, this.yPos, (float) (10), paint);
        this.yPos -= this.yVel;
        this.xPos -= this.xVel;

        // add friction to the Y direction so the bullet slows down.

        this.xVel = 0;
        this.yVel -= 0.6;

        // limit the minimum speed of a bullet.

        if(this.yVel < 10){
            this.yVel = 10;
        }

    }

    public float getX() { return xPos; }
    public float getY(){
        return yPos;
    }
}
