package com.example.flappybird.gameObjects.savedObjects;

public class SavedPipe {
    private final int height;
    private final int x;
    private final int y;
    private boolean loaded;

    // Holds the saved pipe state
    public SavedPipe(int height, int x, int y) {
        this.height = height;
        this.x = x;
        this.y = y;
        this.loaded = false;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLoaded(Boolean bool) {
        this.loaded = bool;
    }

    public boolean hasLoaded() {
        return this.loaded;
    }
}
