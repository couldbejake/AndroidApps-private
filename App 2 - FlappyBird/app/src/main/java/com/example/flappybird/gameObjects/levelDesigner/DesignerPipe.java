package com.example.flappybird.gameObjects.levelDesigner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.gameObjects.Pipe;

// A pipe object for the editor view.
public class DesignerPipe extends DesignerRenderable {
    private final Pipe pipe;
    private final FlappyBird flappyBird;
    private Canvas canvas;


    public DesignerPipe(LevelDesignerCamera camera, FlappyBird game, Integer initialXOffset) {
        super(camera);
        this.camera = camera;
        this.pipe = new Pipe(game, initialXOffset);
        this.flappyBird = game;
    }

    @Override
    public void update(FlappyBird flappyBird) {
        this.pipe.set_x((int) this.get_rendering_x());
        this.pipe.set_y((int) this.get_rendering_y());
    }

    @Override
    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        this.pipe.draw(screenResolution, canvas);
        this.canvas = canvas;

        if (flappyBird.devMode) {
            this.drawBoundingBox();
        }

    }

    // draws a bounding box around the object
    public void drawBoundingBox() {
        Rect rect = get_collision_rect();

        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
    }

    // defines a collision rectangle.

    public Rect get_collision_rect() {

        int collision_gutter = 50;

        float rectX = (float) this.get_rendering_x() - collision_gutter;
        float rectY = (float) 0;
        float rectWidth = (float) (this.get_rendering_x() + pipe.pipeTexture.getWidth()) + collision_gutter;
        float rectHeight = canvas.getMaximumBitmapHeight();

        Rect rect = new Rect((int) rectX, (int) rectY, (int) rectWidth, (int) rectHeight);

        return rect;
    }

    // collision detection with mouse
    @Override
    public boolean collides(float x, float y) {

        Rect rect = get_collision_rect();

        return rect.contains((int) x, (int) y);
    }

    public Pipe getPipe() {
        return this.pipe;
    }
}
