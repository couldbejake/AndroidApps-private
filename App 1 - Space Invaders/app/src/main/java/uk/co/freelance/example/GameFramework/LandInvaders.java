package uk.co.reading.example.GameFramework;

//Other parts of the android libraries that we use
import static android.support.v4.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Iterator;
import java.util.Vector;


public class LandInvaders extends GameThread{

    public int Level;
    private Paint backgroundPaint;
    private Player player;

    Vector<PlayerBullet> player_bullets;
    public BAnimationGenerator bAnim;

    // hold the touched x and y positions.
    float touchedX;
    float touchedY;

    int lastTouchMode = 0;

    Bitmap playerResource;
    Bitmap enemyResource;

    InvaderCommander invaderCommander;

    GameView gm_view;

    int enemyPerLineCount = 1;
    public LandInvaders(GameView gameView) {

        //House keeping
         super(gameView);

         gm_view = gameView;

         this.Level = 0;

         // create the background animation object

         bAnim = new BAnimationGenerator();

        // get the player icon.

         playerResource = BitmapFactory.decodeResource
                 (gameView.getContext().getResources(),
                         R.drawable.spaceship);

         player = new Player(playerResource);

         // create a vector for the player's bullets

         player_bullets = new Vector<PlayerBullet>();

         enemyResource = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.enemy);

        invaderCommander = new InvaderCommander(enemyResource);

        invaderCommander.setup(1, 1);

    }

    @Override
    public void setupBeginning() {

    }

    @Override
    protected void doDraw(Canvas canvas) {
        if(canvas == null) return;


        super.doDraw(canvas);

        // update the background animation.
        bAnim.do_tick(mCanvasWidth, mCanvasHeight);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.rgb(100, 100, 100));
        backgroundPaint.setStrokeJoin(Paint.Join.ROUND);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setStrokeWidth(1);

        // update each element in the background

        for (BAnimationElement background_element : bAnim.get_background_elements()) {
            canvas.drawRoundRect(background_element.get_position(), 2, 2, backgroundPaint);
        }

        // for each bullet

        for (Iterator<PlayerBullet> it = player_bullets.iterator(); it.hasNext(); ) {
            PlayerBullet player_bullet = it.next();

            // delete the bullet if off the screen

            if (player_bullet.getY() < -10) {
                player_bullet = null;
                it.remove();
                break;
            } else {

                // otherwise, draw the bullet

                player_bullet.render(canvas);

                // for each invader

                for (Iterator<Invader> it2 = invaderCommander.get_invaders().iterator(); it2.hasNext(); ) {

                    Invader invader = it2.next();

                    // use pythagoras theorem

                    double x1 = player_bullet.getX();
                    double y1 = player_bullet.getY();
                    double x2 = invader.getX();
                    double y2 = invader.getY();

                    // check the collision between an invader and a bullet

                    double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

                    // if the bullet hits the invader, destroy the invader

                    if(distance < 50){
                        invader = null;
                        it2.remove();
                    }

                    // if the invader goes off the screen.

                    if(y2 > mCanvasHeight) {
                        invader = null;
                        it2.remove();
                        this.Level = 0;
                        invaderCommander.setup(1, 1);
                        invaderCommander.setXDifficulty(1);
                        invaderCommander.setYDifficulty(1);
                    }
                }
            }



        }

        // render the player

        player.render(canvas, mCanvasWidth, mCanvasHeight, touchedX, touchedY);

        // only fire if the user moves their finger on the screen.

        if(lastTouchMode == MotionEvent.ACTION_MOVE && player_bullets.size() < 10){
            player_bullets.add(new PlayerBullet(mCanvasWidth, mCanvasHeight, touchedX));
        }



        if(invaderCommander.get_invaders().size() == 0){

            // temporary allocation of each level.

            switch(this.Level) {
                case 1:
                    invaderCommander.setup(2, 2);
                case 2:
                    invaderCommander.setup(3, 2);
                    invaderCommander.setXDifficulty(1.2);
                    break;
                case 3:
                    invaderCommander.setup(2, 2);
                    invaderCommander.setXDifficulty(1.2);
                    break;
                case 4:
                    invaderCommander.setup(1, 1);
                    invaderCommander.setXDifficulty(1.6);
                    break;
                case 5:
                    invaderCommander.setup(4, 2);
                    invaderCommander.setXDifficulty(1);
                    break;
                case 6:
                    invaderCommander.setup(4, 3);
                    invaderCommander.setXDifficulty(1.5);
                    break;
                case 7:
                    invaderCommander.setup(3, 3);
                    invaderCommander.setXDifficulty(2);
                    invaderCommander.setYDifficulty(2);
                    break;
                case 8:
                    invaderCommander.setup(3, 3);
                    invaderCommander.setXDifficulty(2.5);
                    invaderCommander.setYDifficulty(2.5);
                    break;
                case 9:
                    invaderCommander.setup(1, 4);
                    invaderCommander.setXDifficulty(4);
                    invaderCommander.setYDifficulty(4);
                    break;
                default:
                    // code block
            }
            //Log.e("LEVEL", "Currently on level " + this.Level);
            this.Level += 1;

        }

        invaderCommander.render(canvas, mCanvasWidth, mCanvasHeight);

    }

    //This is run whenever the phone is touched by the user
    @Override
    protected void actionOnTouch(float x, float y, int mMode) {
        touchedX = x;
        touchedY = y;
        lastTouchMode = mMode;
    }

    @Override
    protected void updateGame(float secondsElapsed) {

    }



}