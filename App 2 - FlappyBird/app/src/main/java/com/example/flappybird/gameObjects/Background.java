package com.example.flappybird.gameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import androidx.core.content.ContextCompat;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;

public class Background {

    // a bitmap for the background
    public final Bitmap tex1;

    // defined how fast the background should move
    private final int SCROLL_SPEED = 12;

    // defines whether the background SHOULD move
    private final Boolean do_autoscroll;

    // the main game object

    public FlappyBird game;

    // how far the background has scrolled
    private int backgroundScroll = 0;

    // handles night time
    private final double lastNightModeAmount = 0;
    private boolean hasColorFilter = false;
    private ColorMatrixColorFilter backgroundColorMatrix;
    private double nightModeAmount = 0;

    public Background(FlappyBird game, Boolean do_autoscroll) {

        this.game = game;
        this.tex1 = BitmapFactory.decodeResource
                (game.getContext().getResources(),
                        R.drawable.mars_landscape);
        this.do_autoscroll = do_autoscroll;
    }

    public void draw(Canvas canvas, Boolean fancyGraphics) {

        int bg1_from = ContextCompat.getColor(game.getContext(), R.color.bg1_start);
        int bg1_to = ContextCompat.getColor(game.getContext(), R.color.bg1_end);

        this.drawBackground(canvas, tex1, bg1_from, bg1_to, fancyGraphics);
    }


    public void update() {

        /*

            Gradually reduce or increase night amount

        */

        if (this.game.nightMode) {

            // if night time
            // set the hue of the sky more towards night

            if (this.nightModeAmount < 1) {
                this.nightModeAmount += 0.04;
            }

        } else {

            // if night time
            // set the hue of the sky more towards day

            if (this.nightModeAmount > 0) {
                this.nightModeAmount -= 0.02;
            }
        }
        this.setNightMode(this.nightModeAmount);
    }

    private void drawBackground(Canvas canvas, Bitmap bitmapTexture, int grad_from, int grad_to, boolean fancyGraphics) {

        // defines how far down the sky image should be drawn

        int top = canvas.getHeight() - bitmapTexture.getHeight();

        // fancy graphics causes an FPS dip, caused by the shaders being called.
        // TODO: Move shader creation to constructor.

        if (fancyGraphics) {

            this.drawGradient(canvas, top, grad_from, grad_to);

        } else {

            this.drawFill(canvas, grad_to);

        }

        // defines a paint color

        Paint paint = new Paint();

        // if we have a color filter

        if (this.hasColorFilter) {

            // set the color filter to the background color matrix

            paint.setColorFilter(this.backgroundColorMatrix);

        }

        // draw two bitmaps for the background side-by-side.

        canvas.drawBitmap(tex1, backgroundScroll, top, paint);
        canvas.drawBitmap(tex1, backgroundScroll + bitmapTexture.getWidth(), top, paint);

        // if we should auto scroll

        if (this.do_autoscroll) {

            // move the background to the left by SCROLL_POSITION

            backgroundScroll -= SCROLL_SPEED;

        }

        // if the background scroll position is less than the image width

        if (backgroundScroll < -tex1.getWidth()) {

            // set the scroll position to 0

            backgroundScroll = 0;

        }
    }

    /*
        Draws a gradient on the canvas behind the background image.
     */
    private void drawGradient(Canvas canvas, int yStop, int startColor, int endColor) {

        // move the paint to the constructor to save on resources.

        Paint paint = new Paint();

        // set paint sytyle

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        paint.setFilterBitmap(false);
        paint.setDither(false);

        if (this.hasColorFilter) {
            paint.setColorFilter(this.backgroundColorMatrix);
        }

        // define the filter and set the start and end colours of the gradient
        // (Can we viewed with Fancy Graphics turned on.)

        Shader shader = new LinearGradient(
                0,
                0,
                0,
                yStop,
                new int[]{startColor, endColor},
                null,
                Shader.TileMode.CLAMP
        );

        // set the paint shader

        paint.setShader(shader);

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);


    }

    private void drawFill(Canvas canvas, int color) {

        // if using simple graphics, do a simple FILL to the canvas.

        Paint paint = new Paint();

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        if (this.hasColorFilter) {
            paint.setColorFilter(this.backgroundColorMatrix);
        }

        canvas.drawPaint(paint);
    }


    public void set_x_scroll(int scroll) {
        backgroundScroll = scroll;
    }

    private ColorMatrixColorFilter generateNightFilter(double nightAmount) {

        /*
                Generates a night time filter by affecting raw R G B A values
                in 4x5 matrices.
         */

        final ColorMatrix matrixA = new ColorMatrix();

        float r = (float) scaleBetween(nightAmount, 0.25, 1, 1, 0);//0.25f;
        float g = (float) scaleBetween(nightAmount, 0.3, 1, 1, 0);//0.3f;
        float b = (float) scaleBetween(nightAmount, 0.6, 1, 1, 0);//0.6f;

        float[] mx = {
                r, 0, 0, 0, 0, // R RGBA
                0, g, 0, 0, 0, // G RGBA
                0, 0, b, 0, 0, // B RGBA
                0, 0, 0, 1, 0,  // A RGBA
        };

        matrixA.set(mx);


        return new ColorMatrixColorFilter(matrixA);
    }




    public void setNightMode(double amount) {
        if (this.lastNightModeAmount != amount) {
            this.backgroundColorMatrix = generateNightFilter(amount);
        }
        this.hasColorFilter = amount != 0;
    }

    /*
        Utility function to scale a value between two numbers.
    */

    double scaleBetween(double num, double out_min, double out_max, double in_min, double in_max) {
        return (out_max - out_min) * (num - in_min) / (in_max - in_min) + out_min;
    }
}
