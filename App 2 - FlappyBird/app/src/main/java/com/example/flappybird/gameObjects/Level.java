package com.example.flappybird.gameObjects;

import android.graphics.Canvas;
import android.util.Log;
import android.util.Pair;

import com.example.flappybird.FlappyBird;

import java.util.ArrayList;
import java.util.List;

/*
    Base Level class to be inherited

 */
public class Level {

    protected List<InfinitePipe> pipes;
    FlappyBird game;


    public Level(FlappyBird game) {
        this.game = game;
        this.pipes = new ArrayList<InfinitePipe>();

    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        for (InfinitePipe pipe : pipes) {
            pipe.draw(screenResolution, canvas);
        }
    }

    public void update(FlappyBird flappyBird) {

    }

    // detect a collision with the player, can be overrided.
    public boolean detect_collision_with_enemy(Player player) {
        for (Pipe pipe : pipes) {
            // detect collision with top box
            if (player.get_y() - 50 < pipe.getGapHeight()) {
                if (player.get_x() + player.playerTexture.getWidth() / 2 > pipe.get_x() && player.get_x() < pipe.get_x() + pipe.pipeTexture.getWidth()) {
                    return true;
                }
            }

            // detect collision with bottom box
            if (player.get_y() + 50 > pipe.getGapHeight() + pipe.jump_gap) {
                if (player.get_x() + player.playerTexture.getWidth() / 2 > pipe.get_x() && player.get_x() < pipe.get_x() + pipe.pipeTexture.getWidth()) {
                    return true;
                }
            }

        }
        return false;
    }

    public void reset_pipes() {

    }

}
