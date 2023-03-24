package com.example.flappybird.gameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;

import java.util.concurrent.ThreadLocalRandom;

public class CollectibleDeath extends CollectibleItem {
    boolean hasInitialised = false;
    boolean randomPosition = false;
    public CollectibleDeath(FlappyBird game, double v, boolean randomPosition) {
        super(game, v);
        this.randomPosition = randomPosition;
        Bitmap unscaledTexture = BitmapFactory.decodeResource
                (game.getContext().getResources(),
                        R.drawable.powerup_invincible);

        this.collectibleTexture = Bitmap.createScaledBitmap(
                unscaledTexture,
                (int) (unscaledTexture.getWidth() * imageScale),
                (int) (unscaledTexture.getHeight() * imageScale),
                false);
    }
    private void initialise() {
        if (this.randomPosition) {
            int yMargin = this.collectibleTexture.getHeight() * 2 + 100;

            int randomX = (int) ((Math.random() * canvas.getWidth()) + canvas.getWidth());
            int randomY = ThreadLocalRandom.current().nextInt(0, canvas.getHeight() - yMargin);

            this.set_x(randomX);
            this.set_y(randomY);
        }
    }

    @Override
    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        super.draw(screenResolution, canvas);
        if (!this.hasInitialised) {
            this.hasInitialised = true;
            this.initialise();
        }
        this.screenResolution = screenResolution;
        this.canvas = canvas;
    }


    @Override
    public void update(FlappyBird flappyBird) {
        super.update(flappyBird);
    }
}
