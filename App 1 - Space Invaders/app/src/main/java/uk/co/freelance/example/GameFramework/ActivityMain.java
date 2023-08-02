package uk.co.reading.example.GameFramework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

// comments
// display score
// random patterns
// (end)

public class ActivityMain extends Activity {

    // private variables for the buttons on the ActivityMain page.
    private Button startGame;
    private Button settings;
    private Button highscores;
    private Button exit;


    // Creates the main activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // sets the flags for the game to run.
        // ; keep the screen on, full screen and without a title.

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        super.onCreate(savedInstanceState);

        // set the content view to the activity

        setContentView(R.layout.activity_main);

        // when the start button is clicked

        startGame = (Button) findViewById(R.id.start_game);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start the main game activity.

                Intent game = new Intent(ActivityMain.this, ActivityGame.class);

                startActivity(game);
            }
        });

        // when the high score button is clicked

        highscores = (Button) findViewById(R.id.highscores);
        highscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start the high scores activity, with an animation.

                startActivity(new Intent(ActivityMain.this, ActivityHighScores.class));

                overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);

            }
        });

        // when the exit button is clicked

        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // exit the game

                finish();

                System.exit(0);
            }
        });
    }
}
