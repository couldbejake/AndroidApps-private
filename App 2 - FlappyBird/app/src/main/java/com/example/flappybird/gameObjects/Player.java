package com.example.flappybird.gameObjects;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;

public class Player extends GameObject {

    private final FlappyBird flappyBird;

    public final Bitmap playerTexture;

    private final double gravity_night = 0.2;
    private final double x_velocity = 0;
    public int currentEffectTimer = 0;

    // set the physics values of the player
    private double gravity = 0.8;
    private double jump_impulse = 10;
    private double _effect_jump_impulse;
    private double y_velocity = 0;
    private double rotation = 0;
    private double rotationVel = 0;
    public final int rotationRenderOffset = 10;
    private long last_jump_millis;
    private Pair<Integer, Integer> screenDimensions = new Pair<Integer, Integer>(0, 0);
    private PlayerEffects.PLAYEREFFECT currentEffect = PlayerEffects.PLAYEREFFECT.NONE;


    public Player(FlappyBird game) {

        this.flappyBird = game;

        Bitmap unscaledTexture = BitmapFactory.decodeResource
                (game.getContext().getResources(),
                        R.drawable.flappybird_normal);

        this.playerTexture = Bitmap.createScaledBitmap(
                unscaledTexture,
                unscaledTexture.getWidth() / 15,
                unscaledTexture.getHeight() / 15,
                false);
    }

    public void draw(Pair<Integer, Integer> screenDimensions, Canvas canvas) {

        this.screenDimensions = screenDimensions;

        Matrix matrix = new Matrix();

        // TODO (Optimize): move all matrcies to global variable

        matrix.reset();
        Paint paint = new Paint();

        float px = this.get_x();
        float py = this.get_y();

        // set the x and y positions offset by half the player texture

        matrix.postTranslate(-this.playerTexture.getWidth() / 2, -this.playerTexture.getHeight() / 2);
        matrix.postRotate((float) this.rotation - rotationRenderOffset);
        matrix.postTranslate(px, py);
        canvas.drawBitmap(this.playerTexture, matrix, paint);

    }

    public void update(FlappyBird flappyBird, Boolean canDie) {

        // add gravity to y velocity

        y_velocity += gravity;

        // add velocity to the y position

        this.add_y(y_velocity);

        // add the rotation velocity to the rotation

        rotation += rotationVel;

        // if the last jump time was more than 50 milliseconds, allow another jump

        if (System.currentTimeMillis() > last_jump_millis + 50) {
            rotationVel += 0.9;
        }


        // bound the rotation values

        if (rotation > 65 + rotationRenderOffset) { rotation = 65 + rotationRenderOffset; }
        if (rotation < 0) { rotation = 0; }
        if (rotationVel > 2) {rotationVel = 2; }
        if (rotationVel < -5) { rotationVel = -5; }

        // if the player hits the edge of the screen

        if ((this.get_y() > flappyBird.get_screen_height() || this.get_y() < 0) && canDie) {

            // call the die function

            flappyBird.on_player_die();

        } else {

            // set the x position to half of the screen width

            this.set_x(flappyBird.get_screen_width() / 2);

        }

        // if night mode, make the gravity higher

        if (this.flappyBird.nightMode) {

            this.gravity = 1;
            jump_impulse = 25;

        } else {

            this.gravity = 0.8;
            jump_impulse = 20;

        }

        // handle the player effects

        this.handleEffect();
    }


    // when jumping
    public void on_jump() {

        // set the velocity to the jump impulse + the jump impulse from any effects

        y_velocity = -jump_impulse - _effect_jump_impulse;


        double jumpRotVelocity = rotation * (1.2);

        if (rotation <= 0) {
            jumpRotVelocity = 0;
        }

        rotationVel -= jumpRotVelocity;

        last_jump_millis = System.currentTimeMillis();

    }

    // reset the player, legacy.
    public void reset_player() {
        // game over.
        this.set_y(screenDimensions.second / 2);
        y_velocity = 0;
        rotation = 0;
        rotationVel = 0;
        // move somewhere else;
        this.resetEffect();
    }

    // sets the y velocity of the player

    public void set_y_velocity(int y_velocity) {
        this.y_velocity = y_velocity;
    }

    // resets all player effects
    public void resetEffect() {
        this.currentEffect = PlayerEffects.PLAYEREFFECT.NONE;
        this.currentEffectTimer = 0;
    }

    // adds an effect to the player
    public void addEffect(PlayerEffects.PLAYEREFFECT effect) {
        this.currentEffect = effect;
        this.currentEffectTimer = 200;
    }

    // handles effects
    public void handleEffect() {


        switch (this.currentEffect) {
            case HIGH_JUMP:

                // if high jump effect, increase jump height

                _effect_jump_impulse = 10;
                flappyBird.nightMode = true;
                break;

            case NONE:

                // if no effect, reset jump impulse.

                _effect_jump_impulse = 0;
                flappyBird.nightMode = false;
                break;
        }

        // if effect timer == 0, rese.

        if (this.currentEffectTimer <= 0) {
            // reset all effects
            this.currentEffect = PlayerEffects.PLAYEREFFECT.NONE;
        } else {
            this.currentEffectTimer--;
        }
    }

    public PlayerEffects.PLAYEREFFECT getCurrentEffect() {
        return this.currentEffect;
    }
}
