package com.example.flappybird.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Pair;

import com.example.flappybird.FlappyBird;

public class Button {
    private final boolean full_width;
    private final boolean align_top;
    private final int fontSize;
    private final int buttonColor;
    private final float strokeWidth;
    private final float buttonMargin = 100;
    private final int buttonHeight = 120;


    // TODO: Fix typos of Colour.
    private final Typeface menuTypeFace;
    private final FlappyBird flappyBird;
    private final ButtonContext bcontext;
    public ButtonCallbackInterface callback;
    float x1;
    float x2;
    float y1;
    float y2;
    private Canvas canvas;
    private boolean firstDrawDone = false;
    public Button(FlappyBird flappyBird, Context context, int y_position, ButtonContext bcontext, ButtonCallbackInterface buttonCallbackInterface) {
        this.menuTypeFace = Typeface.createFromAsset(context.getAssets(), "inter_variablefont.ttf");
        this.flappyBird = flappyBird;
        this.bcontext = bcontext;
        this.callback = buttonCallbackInterface;

        x1 = this.buttonMargin;
        y1 = y_position;
        x2 = this.buttonMargin + 100;
        y2 = y_position + buttonHeight;

        this.full_width = true;
        this.align_top = false;
        this.fontSize = 70;
        this.buttonColor = Color.TRANSPARENT;
        this.strokeWidth = 5;

    }


    public Button(FlappyBird flappyBird, Context context, int x, int y, int width, int height, ButtonContext bcontext, ButtonCallbackInterface buttonCallbackInterface) {
        this.menuTypeFace = Typeface.createFromAsset(context.getAssets(), "inter_variablefont.ttf");
        this.flappyBird = flappyBird;
        this.bcontext = bcontext;
        this.callback = buttonCallbackInterface;
        x1 = x;
        y1 = y;
        x2 = x + width;
        y2 = y + height;
        this.full_width = false;
        this.align_top = true;
        this.fontSize = 40;
        this.strokeWidth = 3;
        this.buttonColor = Color.argb(230, 255, 255, 255);
    }

    private void drawButton(Canvas canvas) {

        this.canvas = canvas;

        Paint paint = new Paint();
        paint.setColor(buttonColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(x1, y1, x2, y2, paint);
        paint.setStrokeWidth(0);
        paint.setColor(bcontext.getColor());
        canvas.drawRect(x1, y1, x2, y2, paint);

        canvas.drawRect(x1, y1, x2, y2, paint);
        paint.setStrokeWidth(this.strokeWidth);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x1, y1, x2, y2, paint);

        int buttonHeight = (int) (y2 - y1);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(this.fontSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(this.menuTypeFace);
        Rect bounds = new Rect();
        textPaint.getTextBounds(this.bcontext.getText(), 0, this.bcontext.getText().length(), bounds);

        int textX;

        if (this.full_width) {
            textX = (canvas.getWidth() / 2) - (bounds.width() / 2);
        } else {
            textX = (int) (x1 + ((x2 - x1) / 2) - (bounds.width() / 2));
        }

        int textY = (int) (y1 + ((y2 - y1) / 2) + (bounds.height() / 2));

        canvas.drawText(this.bcontext.getText(), textX, textY, textPaint);
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        if (!this.firstDrawDone) {
            this.firstDraw(canvas);
            this.firstDrawDone = true;
        }
        this.drawButton(canvas);
    }

    public void firstDraw(Canvas canvas) {
        if (this.align_top == false) {
            y1 = canvas.getHeight() - y1;
            y2 = canvas.getHeight() - y2;
        }
        if (this.full_width) {
            x2 = canvas.getWidth() - this.buttonMargin;
        }
    }

    public boolean isInBounds(float x, float y) {

        if (x > x1 && x < x2 || x < x1 && x > x2) {
            return y > y1 && y < y2 || y < y1 && y > y2;
        }
        return false;
    }


    public interface ButtonCallbackInterface {
        void onClick();
    }
}
