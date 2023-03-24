package com.example.flappybird.gameObjects;

import android.graphics.Canvas;
import android.util.Log;
import android.util.Pair;

import com.example.flappybird.FlappyBird;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class InfiniteLevel extends Level {

    public double level_speed = 10;
    List<InfiniteEnemy> enemies;
    List<CollectibleItem> collectibles;
    private Pair<Integer, Integer> screenResolution = new Pair<>(0, 0);
    private Canvas canvas;

    public InfiniteLevel(FlappyBird game) {
        super(game);
        this.enemies = new ArrayList<InfiniteEnemy>();
        this.collectibles = new ArrayList<CollectibleItem>();
        this.pipes.add(new InfinitePipe(game, 0));
    }

    @Override
    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {

        super.draw(screenResolution, canvas);

        this.screenResolution = screenResolution;
        this.canvas = canvas;

        // draw all enemies

        for (InfiniteEnemy enemy : enemies) {
            enemy.draw(screenResolution, canvas);
        }

        // draw all collectibles

        for (CollectibleItem collectibleItem : collectibles) {
            collectibleItem.draw(screenResolution, canvas);
        }
    }

    // when the game is updated

    @Override
    public void update(FlappyBird flappyBird) {

        super.update(flappyBird);

        // synchronize enemies

        synchronized (enemies) {

            // setup an iterator for InfiniteEnemy
            Iterator<InfiniteEnemy> itr = enemies.iterator();

            while (itr.hasNext()) {

                InfiniteEnemy enemy = itr.next();

                // if off the screen, destroy via iterator.

                if (enemy.get_x() < -100) {

                    itr.remove();

                } else {
                    enemy.update(flappyBird);
                }

            }
        }

        // synchronise collectibles

        synchronized (collectibles) {

            // setup an iterator for collectibles

            Iterator<CollectibleItem> itr = collectibles.iterator();

            while (itr.hasNext()) {

                CollectibleItem collectibleItem = itr.next();

                // if off the screen, destroy

                if (collectibleItem.get_x() < -100) {

                    itr.remove();

                } else {

                    collectibleItem.update(flappyBird);

                }

                collectibleItem.add_x(-level_speed);

                if (collectibleItem.collidesWith(flappyBird.player)) {

                    itr.remove();

                    flappyBird.player.addEffect(PlayerEffects.PLAYEREFFECT.HIGH_JUMP);

                    if (collectibleItem instanceof CollectibleDeath) {
                        flappyBird.on_player_die();
                    }

                }

            }
        }

        // for each pipe.

        for (InfinitePipe pipe : pipes) {

            pipe.update(flappyBird);

            // if off the screen
            if (pipe.get_x() < -pipe.pipeTexture.getWidth()) {

                // set the pipe back to the right of the screen
                pipe.set_x(flappyBird.get_screen_width());

                //  pick a new random height

                int random_height = ThreadLocalRandom.current().nextInt(pipe.get_min_height(), pipe.get_max_height() + 1);
                pipe.setGapHeight(random_height);

                // increase the score by 1

                flappyBird.ingameUI.increase_score(1);


                if (pipe.jump_gap > 500) {
                    pipe.jump_gap -= 50;
                }

                this.onLevelIncrease(flappyBird);
            }
            pipe.add_x(-level_speed);
        }

    }

    private void onLevelIncrease(FlappyBird flappyBird) {


        /*

            Handles all levels of the InfiniteLevel mini game


         */




        if (flappyBird.ingameUI.get_score() >= 4 && flappyBird.ingameUI.get_score() < 6) {
            if (flappyBird.ingameUI.get_score() % 2 == 0) {
                for (int i = 0; i < 1; i++) {
                    this.spawn_enemy();
                }
            }
        }
        if (flappyBird.ingameUI.get_score() >= 6) {
            if (flappyBird.ingameUI.get_score() % 2 == 0) {
                for (int i = 0; i < 1; i++) {
                    this.spawn_enemy();
                    this.spawn_enemy();
                }
            }
        }

        if (flappyBird.ingameUI.get_score() >= 6) {
            this.spawn_collectible_chance();
        }

        if (flappyBird.ingameUI.get_score() > 6) {
            level_speed += 1.0;
        } else {
            level_speed += 0.2;
        }
    }


    // collision checking between enemies and player

    @Override
    public boolean detect_collision_with_enemy(Player player) {
        for (InfinitePipe pipe : pipes) {
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
        for (InfiniteEnemy enemy : enemies) {

            // hypot Returns: sqrt(x²+ y²) without intermediate overflow or underflow.

            double collision_distance = (player.playerTexture.getWidth() / 2) + (enemy.enemyTexture.getWidth() / 2);
            double distance = Math.hypot(player.get_x() - enemy.get_x(), player.get_y() - enemy.get_y());
            if (distance < collision_distance) {
                return true;
            }

        }


        return false;
    }

    @Override
    public void reset_pipes() {
        for (InfinitePipe pipe : pipes) {
            pipe.set_x(-100);
            pipe.jump_gap = 1500;
            level_speed = 10;
        }
        enemies.clear();
    }

    /*
        Functions to spawn different game items.
     */
    protected void spawn_enemy() {
        InfiniteEnemy enemy = new InfiniteEnemy(game);
        this.enemies.add(enemy);
    }

    protected void spawn_collectible_chance() {
        if(Math.random() > 0.3){
            CollectibleHighJump highJump = new CollectibleHighJump(game, 0.25, true);
            //CollectibleDeath deathCollectable = new CollectibleDeath(game, 0.25, true);
            highJump.set_x(this.canvas.getWidth());
            this.collectibles.add(highJump);
        }
    }
}
