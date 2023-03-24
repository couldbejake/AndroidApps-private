package com.example.flappybird.UI.menus;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;
import com.example.flappybird.UI.Button;
import com.example.flappybird.UI.ButtonContext;
import com.example.flappybird.UI.Menu;
import com.example.flappybird.UI.SubMenu;

public class MainMenu extends SubMenu {

    public MainMenu(FlappyBird flappyBird, Context context) {
        super(flappyBird, context);

        int buttonCount = 6;
        int buttonGap = 150;
        int buttonY = buttonCount * buttonGap + 100;

        int defaultButtonColor = ContextCompat.getColor(context, R.color.button_translucent);

        ButtonContext bcontext0 = new ButtonContext(defaultButtonColor, "INFINITE ∞");
        createButton(buttonY -= buttonGap, bcontext0, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                Log.e("Clicked", "∞");
                flappyBird.set_game_state(FlappyBird.GameState.INFINITE);
            }
        });


        ButtonContext bcontext4 = new ButtonContext(defaultButtonColor, "PLAYER LEVEL");
        createButton(buttonY -= buttonGap, bcontext4, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                Log.e("Clicked", "Player Level");
                flappyBird.set_game_state(FlappyBird.GameState.LEVEL_PLAYER);
            }
        });

        ButtonContext bcontextEditor = new ButtonContext(defaultButtonColor, "LEVEL EDITOR");
        createButton(buttonY -= buttonGap, bcontextEditor, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                Log.e("Clicked", "LEVEL EDITOR");
                flappyBird.set_game_state(FlappyBird.GameState.LEVEL_CREATOR);
            }
        });

        ButtonContext bcontext1 = new ButtonContext(defaultButtonColor, "SETTINGS");
        createButton(buttonY -= buttonGap, bcontext1, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                flappyBird.get_menu().set_menu(Menu.MenuType.SETTINGS_MENU);
            }
        });

        /*
        createButton(buttonY -= buttonGap, "HIGH SCORES", new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {

            }
        }); */

        ButtonContext bcontext2 = new ButtonContext(defaultButtonColor, "QUIT");
        createButton(buttonY -= buttonGap, bcontext2, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                System.exit(0);
            }
        });
    }
}
