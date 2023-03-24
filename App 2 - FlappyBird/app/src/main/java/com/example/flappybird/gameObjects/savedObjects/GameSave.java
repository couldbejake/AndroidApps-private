package com.example.flappybird.gameObjects.savedObjects;

import java.util.ArrayList;

/*
    A small class to handle GameSave states
*/
public class GameSave {
    private final ArrayList<SavedPipe> savedPipes;
    private final ArrayList<SavedCollectible> savedCollectibles;
    private String savedName;

    public GameSave() {
        this.savedName = "Unknown game name";
        this.savedPipes = new ArrayList<SavedPipe>();
        this.savedCollectibles = new ArrayList<SavedCollectible>();
    }

    /*
        Getters and setters
     */
    public ArrayList<SavedPipe> getSavedPipes() {
        return savedPipes;
    }

    public ArrayList<SavedCollectible> getSavedCollectibles() {
        return savedCollectibles;
    }

    public void addSavedPipe(SavedPipe savedPipe) {
        this.savedPipes.add(savedPipe);
    }

    public void addSavedCollectible(SavedCollectible savedCollectible) {
        this.savedCollectibles.add(savedCollectible);
    }

    public void setSavedname(String name) {
        this.savedName = name;
    }

}
