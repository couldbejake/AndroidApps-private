package com.example.flappybird.gameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;

/*

    Base class for inherited collectible items

 */
public class CollectibleItem extends GameObject {
    private final FlappyBird game;
    public Bitmap collectibleTexture;
    public Pair<Integer, Integer> screenResolution;
    public Canvas canvas;
    double imageScale = 0.50;

    public CollectibleItem(FlappyBird game, double imageScale) {
        this.game = game;
        this.imageScale = imageScale;

        Bitmap unscaledTexture = BitmapFactory.decodeResource
                (game.getContext().getResources(),
                        R.drawable.ui_unpause);

        this.collectibleTexture = Bitmap.createScaledBitmap(
                unscaledTexture,
                (int) (unscaledTexture.getWidth() * imageScale),
                (int) (unscaledTexture.getHeight() * imageScale),
                false);
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        this.screenResolution = screenResolution;
        this.canvas = canvas;

        canvas.drawBitmap(this.collectibleTexture, this.get_x(), this.get_y(), null);

    }

    public void update(FlappyBird flappyBird) {

    }

    @Override
    public int get_x() {
        return super.get_x();
    }

    @Override
    public int get_y() {
        return super.get_y();
    }

    public boolean collidesWith(Player player) {
        double collision_distance = (player.playerTexture.getWidth() / 2) + (this.collectibleTexture.getWidth() / 2);
        double distance = Math.hypot(player.get_x() - this.get_x(), player.get_y() - this.get_y());
        return distance < collision_distance;
    }

    public enum Collectible_type {
        HIGH_JUMP,
        DEATH
    }
}
