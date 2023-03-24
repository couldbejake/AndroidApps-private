package com.example.flappybird;

import static com.example.flappybird.FlappyBird.GameState.LEVEL_CREATOR;
import static com.example.flappybird.FlappyBird.GameState.LEVEL_PLAYER;
import static com.example.flappybird.FlappyBird.GameState.MENU;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import com.example.flappybird.UI.Menu;
import com.example.flappybird.UI.ingameUI;
import com.example.flappybird.gameObjects.Background;
import com.example.flappybird.gameObjects.InfiniteLevel;
import com.example.flappybird.gameObjects.Level;
import com.example.flappybird.gameObjects.Player;
import com.example.flappybird.gameObjects.PlayerLevel;
import com.example.flappybird.gameObjects.levelDesigner.LevelDesigner;

// TODO: Manage width and height better throughout classes with one variable.

/*
    Game manages all objects in the game and is responsible for updating
    all states and rendering all objects to the screen.

*/

public class FlappyBird extends SurfaceView implements SurfaceHolder.Callback {


    public final Player player;
    public final ingameUI ingameUI;
    public final Background background;
    private final Menu menu;
    private final Level infiniteLevel;
    private final GameLoop gameLoop;
    private final Context context;
    public boolean nightMode = false;
    public boolean fancyGraphics = false;
    public boolean devMode = false;
    public Boolean is_paused = false;
    LevelDesigner designLevel;
    private PlayerLevel playerLevel;
    // turn screen resolution into object.
    private Pair<Integer, Integer> screenResolution = new Pair<Integer, Integer>(0, 0);
    private GameState game_state = MENU;
    public FlappyBird(Context context) {
        super(context);

        this.context = context;

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // set the game objects

        gameLoop = new GameLoop(this, surfaceHolder);

        player = new Player(this);

        background = new Background(this, true);
        menu = new Menu(this, context);
        ingameUI = new ingameUI(this, context);

        designLevel = new LevelDesigner(this, context);
        playerLevel = new PlayerLevel(this, context);
        infiniteLevel = new InfiniteLevel(this);

        /////////////////////////////////////

        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    /*
        Draw method draws everything to the canvas.

    */
    @Override
    public void draw(Canvas canvas) {

        // TODO: move this to on resize.

        this.screenResolution = new Pair<Integer, Integer>(canvas.getWidth(), canvas.getHeight());

        super.draw(canvas);

        // switch over the current game state

        switch (this.game_state) {

            case MENU: {

                // handle drawing MENU

                background.draw(canvas, this.fancyGraphics);
                menu.draw(screenResolution, canvas);
                player.draw(screenResolution, canvas);
                break;
            }

            case LEVEL_PLAYER:

                // handle drawing custom player level

                background.draw(canvas, this.fancyGraphics);
                player.draw(screenResolution, canvas);
                playerLevel.draw(screenResolution, canvas);
                ingameUI.draw(screenResolution, canvas);

                break;

            case INFINITE: {

                // handle drawing infinite world

                background.draw(canvas, this.fancyGraphics);
                player.draw(screenResolution, canvas);
                infiniteLevel.draw(screenResolution, canvas);
                ingameUI.draw(screenResolution, canvas);

                break;
            }
            case LEVEL_CREATOR: {

                // handle drawing level creator

                designLevel.draw(screenResolution, canvas);
                break;

            }
        }

        // if debug is on, show information

        if (devMode) {
            drawDebug(canvas);
        }

    }

    /*

        Update method updates all game objects including physics

     */

    public void update() {

        switch (this.game_state) {

            case MENU:

                // handle updating MENU items

                menu.update(this);
                player.update(this, false);
                background.update();

                if (player.get_y() > screenResolution.second / 4) {
                    player.on_jump();
                }

                if (player.get_y() < -player.playerTexture.getHeight() * 2) {
                    player.set_y(-player.playerTexture.getHeight() * 2);
                }

                break;

            case INFINITE:

                // handle updating infinite level

                if (!is_paused) {
                    player.update(this, true);
                    infiniteLevel.update(this);

                    if (infiniteLevel.detect_collision_with_enemy(player)) {
                        on_player_die();
                    }
                }

                background.update();

                break;

            case LEVEL_PLAYER:

                // handle updating custom level

                if (!is_paused) {
                    player.update(this, true);
                    playerLevel.update(this);

                    if (playerLevel.detect_collision_with_enemy(player)) {
                        on_player_die();
                    }
                }

                background.update();

                break;

            case LEVEL_CREATOR:

                // handle updating level creator

                designLevel.update(this);

                break;
        }


    }

    public void on_player_die() {

        this.set_game_state(GameState.MENU);
        this.nightMode = false;

        ingameUI.reset_score();
        player.resetEffect();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (game_state) {

            case MENU:

                // handle master menu click

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        menu.onTouch(event.getX(), event.getY());
                        return true;
                }

                break;

            case LEVEL_PLAYER:
            case INFINITE:

                // handle infinite world click

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        // jump when clicked

                        if (!this.is_paused()) {

                            player.on_jump();
                            ingameUI.on_tap(event);

                        } else {

                            this.set_paused(false);

                        }

                        return true;

                    case MotionEvent.ACTION_MOVE:

                        return true;

                }
                break;

            case LEVEL_CREATOR:

                // handle level creator

                switch (event.getAction()) {

                    // on scroll down and tap,
                    // call designer onDragStart
                    case MotionEvent.ACTION_SCROLL:
                    case MotionEvent.ACTION_DOWN:
                        designLevel.onDragStart(event);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        designLevel.onDrag(event);
                        return true;
                    case MotionEvent.ACTION_UP:
                        designLevel.onDragEnd(event);
                        return true;
                }
        }


        return super.onTouchEvent(event);
    }

    public void drawDebug(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(context, R.color.black));
        paint.setTextSize(50);

        Paint paintr = new Paint();

        int alpha = (this.game_state == MENU) ? 200 : 100;

        paintr.setColor(Color.argb(alpha, 255, 255, 255));
        canvas.drawRect(0, 200, 700, 950, paintr);

        canvas.drawText("DEBUG", 10, 300, paint);
        canvas.drawText("UPS " + Math.round(gameLoop.getAverageUPS()), 10, 400, paint);
        canvas.drawText("FPS " + Math.round(gameLoop.getAverageFPS()), 10, 450, paint);
        canvas.drawText("Effect Timer " + player.currentEffectTimer, 10, 550, paint);
        canvas.drawText("Effect " + player.getCurrentEffect().name(), 10, 600, paint);
        canvas.drawText("Player X " + player.get_x(), 10, 700, paint);
        canvas.drawText("Player Y " + player.get_y(), 10, 750, paint);
        canvas.drawText("Game State " + this.game_state.name(), 10, 800, paint);
        canvas.drawText("Load X " + this.playerLevel.loadX, 10, 850, paint);
    }

    public int get_screen_width() {
        return this.screenResolution.first;
    }

    public int get_screen_height() {
        return this.screenResolution.second;
    }

    public Menu get_menu() {
        return menu;
    }

    public void set_game_state(GameState gameState) {
        this.game_state = gameState;
        this.infiniteLevel.reset_pipes();

        // nice animation when returning to menu

        if (gameState != MENU) {
            this.player.reset_player();
        }

        if (gameState == LEVEL_CREATOR) {
            this.designLevel = new LevelDesigner(this, context);
        }

        if (gameState == LEVEL_PLAYER) {
            this.playerLevel = new PlayerLevel(this, context);
        }

        ingameUI.reset_score();
    }

    public boolean is_paused() {
        return this.is_paused;
    }

    public void set_paused(Boolean bool) {
        this.is_paused = bool;
    }

    public enum GameState {
        MENU,
        INFINITE,
        LEVEL_CREATOR,
        LEVEL_PLAYER
    }

}
