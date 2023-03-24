package com.example.flappybird.gameObjects;

public class GameObject {
    private double x;
    private double y;

    public GameObject() {
        this.x = 0;
        this.y = -500;
    }

    /*

        Getters and setters for GameObjects

     */
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

}
