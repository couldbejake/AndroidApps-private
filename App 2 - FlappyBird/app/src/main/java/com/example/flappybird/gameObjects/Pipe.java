package com.example.flappybird.gameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;

/*
    Defines a default game pipe

 */
public class Pipe extends GameObject {
    public final Bitmap pipeTexture;

    // defines the the pipe bounding
    private final int jumpHeightTopMin = 150;
    public int jump_gap = 1000; //500
    private int jumpHeightTop = 150;
    private final FlappyBird game;

    // initial x position
    public int pipeOffsetX = 0;

    // a flag for whether to darken the pipe sprite
    public boolean darken_image = false;
    private Pair<Integer, Integer> screenResolution = new Pair<Integer, Integer>(0, 0);

    public Pipe(FlappyBird game, Integer initialXOffset) {
        this.game = game;

        Bitmap unscaledTexture = BitmapFactory.decodeResource
                (game.getContext().getResources(),
                        R.drawable.pipe_texture);

        this.pipeTexture = Bitmap.createScaledBitmap(
                unscaledTexture,
                (int) (unscaledTexture.getWidth() * 0.20),
                (int) (unscaledTexture.getHeight() * 0.20),
                false);
        pipeOffsetX = initialXOffset;
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {

        this.screenResolution = screenResolution;

        Paint paint = new Paint();

        // if we should darken the image

        if (darken_image) {

            // darken the image using a ColorFilter & LightningColorFilter

            ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);
            paint.setColorFilter(filter);
            paint.setAlpha(210);
        }


        // draw the first and second pipes

        canvas.drawBitmap(this.pipeTexture, this.get_x(), -this.pipeTexture.getHeight() + jumpHeightTop, paint);

        // rotate the second pipe 180 degrees.

        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postTranslate(-this.pipeTexture.getWidth(), -this.pipeTexture.getHeight() * 2);
        matrix.postRotate(180);
        matrix.postTranslate(this.get_x(), this.jump_gap - this.pipeTexture.getHeight() + jumpHeightTop);

        canvas.drawBitmap(this.pipeTexture, matrix, paint);

    }

    public void update(FlappyBird flappyBird) {

    }

    public int get_min_height() {
        return jumpHeightTopMin;
    }

    public int get_max_height() {
        return this.screenResolution.second - this.jump_gap - 200;
    }

    @Override
    public int get_x() {
        return super.get_x();
    }

    @Override
    public int get_y() {
        return super.get_y();
    }

    public int getGapHeight() {
        return this.jumpHeightTop;
    }

    public void setGapHeight(int height) {
        if (height < get_min_height()) {
            height = get_min_height();
        }
        if (height > get_max_height()) {
            height = get_max_height();
        }
        this.jumpHeightTop = height;
    }

    public void setJumpGap(int gap){
        jump_gap = gap;
    }

    public int getJumpGap(){
        return this.jump_gap;
    }

}
