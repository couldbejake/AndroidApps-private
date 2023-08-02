package uk.co.reading.example.GameFramework;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Invader extends LivingEntity {

    private Bitmap invaderResource;

    // values to hold canvas size.
    int mCanvasWidth;
    int mCanvasHeight;

    // the postion of the invader in the grid.
    int gridX;
    int gridY;

    // sets the origin position of the invadercommander.
    int originX;
    int originY;

    public Invader(Bitmap invaderResource, int gridX, int gridY){
        super();
        this.invaderResource = invaderResource;
        // constructor for an Invader.
        this.gridX = gridX;
        this.gridY = gridY;

        this.originX = 0;
        this.originY = 0;
    }

    public void render(Canvas c, int mCanvasWidth, int mCanvasHeight) {

        this.mCanvasWidth = mCanvasWidth;
        this.mCanvasHeight = mCanvasHeight;

        this.xPos = gridX + this.originX;
        this.yPos = gridY + this.originY;

        double bitMapScale = 0.05;

        // scale the image to the correct size.

        Bitmap invadeResourceScaled = Bitmap.createScaledBitmap(
                this.invaderResource,
                (int) (this.invaderResource.getWidth() * bitMapScale),
                (int) (this.invaderResource.getHeight() * bitMapScale),
                true);

        c.drawBitmap(invadeResourceScaled, xPos, yPos, null);

    }

    public void setOrigin(int originX, int originY) {
        this.originX = originX;
        this.originY = originY;
    }

    public double getX(){
        return (int) this.xPos;
    }
    public double getY(){
        return this.yPos;
    }

}
