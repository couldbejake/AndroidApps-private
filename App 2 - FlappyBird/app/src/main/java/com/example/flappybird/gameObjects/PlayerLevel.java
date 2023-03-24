package com.example.flappybird.gameObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.gameObjects.levelDesigner.DesignerSaver;
import com.example.flappybird.gameObjects.savedObjects.GameSave;
import com.example.flappybird.gameObjects.savedObjects.SavedCollectible;
import com.example.flappybird.gameObjects.savedObjects.SavedPipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerLevel extends Level {

    private final Context context;
    private final DesignerSaver designerSaver;
    private final GameSave level;
    private final FlappyBird flappyBird;
    public double level_speed = 10;
    public int loadX = 0;
    List<InfiniteEnemy> enemies;
    List<CollectibleItem> collectibles;
    private Pair<Integer, Integer> screenResolution;
    private Canvas canvas;


    public PlayerLevel(FlappyBird game, Context context) {
        super(game);
        this.context = context;
        this.flappyBird = game;
        this.enemies = new ArrayList<InfiniteEnemy>();
        this.collectibles = new ArrayList<CollectibleItem>();
        this.designerSaver = new DesignerSaver(context);
        this.level = this.designerSaver.loadGame();
        this.loadX = 0;
    }

    private void generate_entities() {
        //Log.e("Load X", this.loadX +"");
        for (SavedPipe savedPipe : this.level.getSavedPipes()) {
            if (this.loadX > savedPipe.getX() && !savedPipe.hasLoaded()) {
                Log.e(">>>", "Loaded pipe" + Math.random());
                InfinitePipe pipe = new InfinitePipe(game, 0);

                int rel_x = 0;

                if (savedPipe.getX() != 0) {
                    rel_x = canvas.getWidth() + (canvas.getWidth() % savedPipe.getX());
                }

                pipe.fixed_gap_height = savedPipe.getHeight();
                pipe.set_x(rel_x);
                pipe.setJumpGap(800);

                this.pipes.add(pipe);
                savedPipe.setLoaded(true);
            }
        }

        for (SavedCollectible savedCollectible : this.level.getSavedCollectibles()) {
            if (this.loadX > savedCollectible.getX() && !savedCollectible.hasLoaded()) {


                CollectibleItem collectable = null;


                if (savedCollectible.getType() == CollectibleItem.Collectible_type.HIGH_JUMP) {
                    Log.e("test", "High jump loaded");
                    collectable = new CollectibleHighJump(game, 0.25, false);
                }

                if (savedCollectible.getType() == CollectibleItem.Collectible_type.DEATH) {
                    Log.e("test", "Death loaded");
                    collectable = new CollectibleDeath(game, 0.25, false);
                }

                if (savedCollectible.getType() != null) {
                    int rel_x = 0;

                    if (savedCollectible.getX() != 0) {
                        rel_x = canvas.getWidth() + (canvas.getWidth() % savedCollectible.getX());
                    }

                    collectable.set_x(rel_x);
                    collectable.set_y(savedCollectible.getY());

                    this.collectibles.add(collectable);

                    Log.e(">>>", "Loaded Collectable" + Math.random());

                    savedCollectible.setLoaded(true);
                }


            }
        }
    }

    @Override
    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        super.draw(screenResolution, canvas);
        this.screenResolution = screenResolution;
        this.canvas = canvas;
        for (InfiniteEnemy enemy : enemies) {
            enemy.draw(screenResolution, canvas);
        }
        for (CollectibleItem collectibleItem : collectibles) {
            collectibleItem.draw(screenResolution, canvas);
        }
        for (InfinitePipe pipe : pipes) {
            pipe.draw(screenResolution, canvas);
            pipe.setGapHeight(pipe.fixed_gap_height);
        }
        if (!flappyBird.is_paused) {
            generate_entities();
            loadX += 20;
        }

    }

    @Override
    public void update(FlappyBird flappyBird) {

        super.update(flappyBird);

        synchronized (enemies) {

            Iterator<InfiniteEnemy> itr = enemies.iterator();
            while (itr.hasNext()) {
                InfiniteEnemy enemy = itr.next();
                if (enemy.get_x() < -100) {
                    itr.remove();
                    Log.e("Destroyed", "Destroyed enemy");
                } else {
                    enemy.update(flappyBird);
                }
            }
        }

        synchronized (collectibles) {

            Iterator<CollectibleItem> itr = collectibles.iterator();
            while (itr.hasNext()) {
                CollectibleItem collectibleItem = itr.next();
                if (collectibleItem.get_x() < -100) {
                    itr.remove();
                    Log.e("Destroyed", "Destroyed collectible item.");
                } else {
                    collectibleItem.update(flappyBird);
                }
                (collectibleItem).add_x(-level_speed);
                if (collectibleItem.collidesWith(flappyBird.player)) {
                    itr.remove();
                    flappyBird.player.addEffect(PlayerEffects.PLAYEREFFECT.HIGH_JUMP);
                    if (collectibleItem instanceof CollectibleDeath) {
                        flappyBird.on_player_die();
                    }
                }

            }
        }


        Iterator<InfinitePipe> itr = pipes.iterator();
        while (itr.hasNext()) {
            InfinitePipe pipe = itr.next();
            pipe.update(flappyBird);

            // if the pipe hits the left side of the screen

            if (pipe.get_x() < -pipe.pipeTexture.getWidth()) {



                flappyBird.ingameUI.increase_score(1);

                this.onLevelIncrease(flappyBird);

                // destroy

                itr.remove();
            }

            pipe.add_x(-level_speed);
        }
    }

    private void onLevelIncrease(FlappyBird flappyBird) {


    }


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

}
