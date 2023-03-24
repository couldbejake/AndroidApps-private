package uk.ac.reading.sis05kol.GameFramework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Vector;

/*

    The invader class controls a large number of 'invaders'

 */

public class InvaderCommander extends LivingEntity {

    // get the resource bitmap
    private Bitmap invaderResource;

    int mCanvasWidth;
    int mCanvasHeight;

    int maxEnemiesInRow = 0;

    // set the commander x and y offsets.
    int commanderXOffset = 50;
    int commanderYOffset = 0;

    // allows the group of invaders to change direction.
    double commanderDirection = 10;

    // xDifficulty and yDifficulty changes the speed of movement.
    double xDiff;
    double yDiff;

    // a Vector of invaders.
    Vector<Invader> invaders;

    // initialise the InvaderCommander and invaders vector.
    public InvaderCommander(Bitmap invaderResource){
        super();
        this.invaderResource = invaderResource;
        invaders = new Vector<Invader>();

        // default difficulty is 1.

        xDiff = 1;
        yDiff = 1;
    }


    public void setup(int width, int height){

        this.commanderXOffset = 50;
        this.commanderYOffset = 0;

        this.maxEnemiesInRow = 0;
        // could do with improvement

        // loop through the width and height and create new invaders

        for (int j = 0; j < height; j++){
            for (int i = 0; i < width; i++){
                int xOffset = 90 * i;
                int yOffset = 50 + (90 * j);
                Invader invader = new Invader(invaderResource, xOffset, yOffset);
                invaders.add(invader);
            }
        }

        // set the initial position of the commander group to off the screen.
        // 90 * height = 90       (the height of the image) * (the number of rows/height)
        this.commanderYOffset = -( 50 + (90 * height) );

        this.maxEnemiesInRow = width;

    }

    public void render(Canvas canvas, int mCanvasWidth, int mCanvasHeight) {

        this.mCanvasWidth = mCanvasWidth;
        this.mCanvasHeight = mCanvasHeight;

        // invader size = 0.05

        int invaderWidth = (int) (this.invaderResource.getWidth() * 0.05);

        // if there are no invaders
        if(this.invaders.size() == 0){
            // set the default values.
            this.maxEnemiesInRow = 0;
            this.commanderXOffset = 50;
            this.commanderYOffset = -50;
            this.commanderDirection = 10;
        } else {

            // if the invader group is about to go off the screen, reverse the direction.

            if(this.commanderXOffset > mCanvasWidth - 90 * this.maxEnemiesInRow){
                this.commanderDirection = -15 * this.xDiff;
                this.commanderYOffset += 40 * this.yDiff;
            }

            if(this.commanderXOffset < 50){
                this.commanderDirection = 15 * this.xDiff;
                this.commanderYOffset += 40 * this.yDiff;
            }

            this.commanderXOffset += this.commanderDirection;

            // for each invader, set it's Commander origin, so that
            // all invaders move at the same time.

            for (Invader invader : invaders) {
                invader.setOrigin(commanderXOffset, commanderYOffset );
                invader.render(canvas, this.mCanvasWidth, this.mCanvasHeight);
            }
        }


    }

    // getters and settings for difficulty values.
    public void setXDifficulty(double xDiff) {
        this.xDiff = xDiff;
    }

    public void setYDifficulty(double yDiff) {
        this.yDiff = yDiff;
    }

    public Vector<Invader> get_invaders(){
        return this.invaders;
    }


    public void tick() {
    }
}
