package uk.ac.reading.sis05kol.GameFramework;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Vector;

public class Player extends LivingEntity {

    private Bitmap playerResource;

    int mCanvasWidth;
    int mCanvasHeight;

    Vector<PlayerBullet> bullets;

    public Player(Bitmap playerResource) {
        super();

        this.playerResource = playerResource;

    }

    public void render(Canvas c, int mCanvasWidth, int mCanvasHeight, float touchX, float touchY) {

        this.mCanvasWidth = mCanvasWidth;
        this.mCanvasHeight = mCanvasHeight;

        // scale for the player icon.
        double bitMapScale = 0.2;

        xPos = touchX -  (int)((this.playerResource.getWidth() * bitMapScale) / 2);
        yPos = (int)(this.mCanvasHeight - (this.playerResource.getHeight() * bitMapScale));

        // limit the xPosition between 0 and the edge of the scren

        xPos = (xPos < 0) ? (0) : (xPos > this.mCanvasWidth - 430 ? this.mCanvasWidth - 430 : xPos);

        Bitmap playerResourceResized = Bitmap.createScaledBitmap(
            this.playerResource,
                (int) (this.playerResource.getWidth() * bitMapScale),
                (int) (this.playerResource.getHeight() * bitMapScale),
            true);
        c.drawBitmap(playerResourceResized, xPos, yPos, null);


    }
}
