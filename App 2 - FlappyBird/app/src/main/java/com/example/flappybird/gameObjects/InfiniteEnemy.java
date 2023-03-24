package com.example.flappybird.gameObjects;

import android.graphics.Canvas;
import android.util.Pair;

import com.example.flappybird.FlappyBird;

import java.util.concurrent.ThreadLocalRandom;

public class InfiniteEnemy extends Enemy {

    private boolean hasDrawn = false;

    private int enemyJumpY;

    public InfiniteEnemy(FlappyBird game) {
        super(game);
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        super.draw(screenResolution, canvas);
        if (!hasDrawn) {
            this.onInit(screenResolution, canvas);
            this.hasDrawn = true;
        }
    }

    public void onInit(Pair<Integer, Integer> screenResolution, Canvas canvas) {

        // set yMargin to add some space around the collision box

        int yMargin = (this.enemyTexture.getHeight() * 2) + 100;

        int randomYBounded = ThreadLocalRandom.current().nextInt(yMargin, canvas.getHeight() - yMargin);

        // set the position before jumping to a random number

        this.enemyJumpY = randomYBounded;

        this.set_x(canvas.getWidth() + this.enemyTexture.getWidth() * 4);
    }

    public void update(FlappyBird flappyBird) {

        // if the y position lower than the enemyJumpPosition

        if (this.get_y() > this.enemyJumpY) {

            // trigger a jump
            this.on_jump();

        }

        // move the enemy to the left

        this.add_x(-10);

        super.update(flappyBird);
    }
}
