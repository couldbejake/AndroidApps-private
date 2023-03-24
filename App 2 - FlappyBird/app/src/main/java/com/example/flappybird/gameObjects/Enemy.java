package com.example.flappybird.gameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;

public class Enemy extends GameObject {
    public final int ROTATION_RENDER_OFFSET = 10;
    private final double GRAVITY = 0.8;
    public final Bitmap enemyTexture;
    private final FlappyBird flappyBird;
    public Pair<Integer, Integer> screenDimensions = new Pair<Integer, Integer>(0, 0);

    // handle object position
    private double y_velocity = 0;
    private double rotation = 0;
    private double rotationVel = 0;

    private long last_jump_millis;


    public Enemy(FlappyBird game) {

        this.flappyBird = game;

        Bitmap unscaledTexture = BitmapFactory.decodeResource
                (game.getContext().getResources(),
                        R.drawable.enemy_v1);

        this.enemyTexture = Bitmap.createScaledBitmap(
                unscaledTexture,
                unscaledTexture.getWidth() / 18,
                unscaledTexture.getHeight() / 18,
                false);
    }

    public void draw(Pair<Integer, Integer> screenDimensions, Canvas canvas) {

        this.screenDimensions = screenDimensions;

        // Create a matrix for rotation and translation

        Matrix matrix = new Matrix();

        // TODO (Optimize): move matrix to global variable

        matrix.reset();
        Paint paint = new Paint();

        float px = this.get_x();
        float py = this.get_y();

        // use postTranslate, postRotate and postTranslate to move the enemy to the current position.

        matrix.postTranslate(-this.enemyTexture.getWidth() / 2, -this.enemyTexture.getHeight() / 2);
        matrix.postRotate((float) -this.rotation - ROTATION_RENDER_OFFSET);
        matrix.postTranslate(px, py);

        // draw the bitmap

        canvas.drawBitmap(this.enemyTexture, matrix, paint);

    }

    // on update to the enemy
    public void update(FlappyBird flappyBird) {

        // add gravity to the y velocity of the enemy

        y_velocity += GRAVITY;

        // add the y_velocity to the y position

        this.add_y(y_velocity);

        // add the rotation velocity to the rotation

        rotation += rotationVel;

        // only do animation rotation if haven't jumped recently

        if (System.currentTimeMillis() > last_jump_millis + 50) {
            rotationVel += 0.9;
        }

        // clamp the values

        if (rotation > 65 + ROTATION_RENDER_OFFSET) { rotation = 65 + ROTATION_RENDER_OFFSET; }
        if (rotation < 0) { rotation = 0; }
        if (rotationVel > 2) { rotationVel = 2; }
        if (rotationVel < -5) { rotationVel = -5; }
    }


    public void on_jump() {

        // set the jump velocity to -20 on jump

        y_velocity = -20;

        // set the rotation velocity to the rotation * 1.2

        double jumpRotImpulse = rotation * (1.2);

        // don't allow a negative rotation velociity

        if (rotation <= 0) {
            jumpRotImpulse = 0;
        }

        // take the jump rotation impulse away from the rotation velocity

        rotationVel -= jumpRotImpulse;

        last_jump_millis = System.currentTimeMillis();
    }

    public void set_y_velocity(int y_velocity) {
        this.y_velocity = y_velocity;
    }
}
