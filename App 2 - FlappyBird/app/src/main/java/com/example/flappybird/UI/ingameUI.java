package com.example.flappybird.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;

public class ingameUI {
    private final Typeface UITypeFace;
    private final int UIColor;
    private final FlappyBird flappyBird;
    private final Context context;
    private final Bitmap unpauseTexture;
    private final Bitmap pauseTexture;
    private Pair<Integer, Integer> screenResolution;
    private Canvas canvas;
    private int score = 0;

    public ingameUI(FlappyBird flappyBird, Context context) {
        this.flappyBird = flappyBird;
        this.context = context;
        this.UITypeFace = Typeface.createFromAsset(flappyBird.getContext().getAssets(), "inter_variablefont.ttf");
        this.UIColor = ContextCompat.getColor(context, R.color.button_translucent);

        Bitmap unscaledTexturePause = BitmapFactory.decodeResource
                (flappyBird.getContext().getResources(),
                        R.drawable.ui_pause);

        this.pauseTexture = Bitmap.createScaledBitmap(
                unscaledTexturePause,
                unscaledTexturePause.getWidth() / 10,
                unscaledTexturePause.getHeight() / 10,
                false);

        Bitmap unscaledTextureUnpause = BitmapFactory.decodeResource
                (flappyBird.getContext().getResources(),
                        R.drawable.ui_unpause);

        this.unpauseTexture = Bitmap.createScaledBitmap(
                unscaledTextureUnpause,
                unscaledTextureUnpause.getWidth() / 10,
                unscaledTextureUnpause.getHeight() / 10,
                false);

    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        this.screenResolution = screenResolution;
        this.canvas = canvas;
        drawTopbar();
        drawScore(this.score);
        drawPauseButton();
    }

    public void drawTopbar() {
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);

        int x1 = 0;
        int y1 = 0;
        int x2 = screenResolution.first;
        int y2 = 150;

        canvas.drawRect(x1, y1, x2, y2, paint);
        paint.setStrokeWidth(10);
        paint.setColor(this.UIColor);
        canvas.drawRect(x1, y1, x2, y2, paint);

        canvas.drawRect(x1, y1, x2, y2, paint);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x1, y1, x2, y2, paint);
    }

    public void drawScore(int score) {
        String text = "Score | " + score;

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(70);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(this.UITypeFace);
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);


        canvas.drawText(text, 20, bounds.height() + 40, textPaint);
    }

    public void drawPauseButton() {

        int xPos = canvas.getWidth() - this.pauseTexture.getWidth() - 20;
        if (flappyBird.is_paused()) {
            canvas.drawBitmap(this.pauseTexture, xPos, 20, null);
        } else {
            canvas.drawBitmap(this.unpauseTexture, xPos, 20, null);
        }
    }

    public void drawQuitButton() {

    }

    public void increase_score(int amount) {
        this.score += amount;
    }

    public void reset_score() {
        this.score = 0;
    }

    public int get_score() {
        return this.score;
    }

    public void on_tap(MotionEvent event) {
        if (this.backButtonPressed(event)) {

            flappyBird.is_paused = !flappyBird.is_paused;
            Log.e("Paused button = ", flappyBird.is_paused() + "-" + Math.random());
        }
    }

    private boolean backButtonPressed(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        //Log.e("x", x + "");
        //Log.e("y", y +"");
        if (x > canvas.getWidth() - 150) {
            return y < 150;
        }
        return false;
    }
}
