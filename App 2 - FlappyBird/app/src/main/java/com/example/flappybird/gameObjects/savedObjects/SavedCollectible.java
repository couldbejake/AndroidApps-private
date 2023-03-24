package com.example.flappybird.gameObjects.savedObjects;


// this is duplicated


import com.example.flappybird.gameObjects.CollectibleItem;

public class SavedCollectible {

    // holds the saved collectible type
    private final CollectibleItem.Collectible_type type;

    private final int x;
    private final int y;

    private boolean loaded;

    public SavedCollectible(CollectibleItem.Collectible_type type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.loaded = false;
    }

    public CollectibleItem.Collectible_type getType() {
        return type;
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
