package com.example.flappybird.gameObjects;

import com.example.flappybird.FlappyBird;

public class InfinitePipe extends Pipe {


    public int fixed_gap_height;

    public InfinitePipe(FlappyBird game, Integer initialXOffset) {
        super(game, initialXOffset);
    }
}