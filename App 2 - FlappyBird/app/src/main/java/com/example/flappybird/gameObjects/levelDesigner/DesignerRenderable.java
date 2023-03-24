package com.example.flappybird.gameObjects.levelDesigner;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Pair;

import com.example.flappybird.FlappyBird;

public class DesignerRenderable {

    // sets the draw distance to save memory
    protected final Integer DRAW_DISTANCE = 500;

    // defines an initial bounding box of 0

    private final Rect bounding_box = new Rect(0, 0, 0, 0);
    protected LevelDesignerCamera camera;
    private double x;
    private double y;

    public DesignerRenderable(LevelDesignerCamera camera) {
        this.camera = camera;
        this.x = 0;
        this.y = 0;
    }

    public double get_rendering_x() {
        return this.x + camera.viewPortX;
    }

    public double get_rendering_y() {
        return this.y + camera.viewPortY;
    }

    public boolean in_rendering_distance() {
        return get_rendering_x() > -DRAW_DISTANCE && get_rendering_x() < camera.screenResolution.first + DRAW_DISTANCE;
    }

    public void update(FlappyBird flappyBird) {
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
    }

    public void add_x(double x_diff) {
        this.x += x_diff;
    }

    public void add_y(double y_diff) {
        this.y += y_diff;
    }

    public int get_x() {
        return (int) this.x;
    }

    public void set_x(int x) {
        this.x = x;
    }

    public int get_y() {
        return (int) this.y;
    }

    public void set_y(int y) {
        this.y = y;
    }

    public boolean collides(float x, float y) {
        return false;
    }

}
