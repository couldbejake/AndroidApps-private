package com.example.flappybird.UI.menus;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.R;
import com.example.flappybird.UI.Button;
import com.example.flappybird.UI.ButtonContext;
import com.example.flappybird.UI.Menu;
import com.example.flappybird.UI.SubMenu;

public class SettingsMenu extends SubMenu {

    /*
        TODO: Add clouds under fancy graphics
     */

    public SettingsMenu(FlappyBird flappyBird, Context context) {
        super(flappyBird, context);

        int buttonCount = 4;
        int buttonGap = 150;
        int buttonY = buttonCount * buttonGap + 100;

        int defaultButtonColor = ContextCompat.getColor(context, R.color.button_translucent);
        int buttonSuccessColor = ContextCompat.getColor(context, R.color.button_success);
        int buttonFailureColor = ContextCompat.getColor(context, R.color.button_failure);


        // handle the dev button.

        ButtonContext bcontext = new ButtonContext(buttonFailureColor, "DEBUG");

        if (flappyBird.devMode) {
            bcontext.setColor(buttonSuccessColor);
        } else {
            bcontext.setColor(buttonFailureColor);
        }

        createButton(buttonY -= buttonGap, bcontext, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                flappyBird.devMode = !flappyBird.devMode;

                if (flappyBird.devMode) {
                    bcontext.setColor(buttonSuccessColor);
                } else {
                    bcontext.setColor(buttonFailureColor);
                }
            }
        });

        // handle the dev button.

        ButtonContext bcontext0 = new ButtonContext(buttonFailureColor, "FANCY GRAPHICS");

        if (flappyBird.fancyGraphics) {
            bcontext0.setColor(buttonSuccessColor);
        } else {
            bcontext0.setColor(buttonFailureColor);
        }

        createButton(buttonY -= buttonGap, bcontext0, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                flappyBird.fancyGraphics = !flappyBird.fancyGraphics;

                if (flappyBird.fancyGraphics) {
                    bcontext0.setColor(buttonSuccessColor);
                } else {
                    bcontext0.setColor(buttonFailureColor);
                }
            }
        });



        /*
        Button button = createButton(buttonY -= buttonGap, "DEV MODE BUTTON", new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                button.setColor(Color.RED);
            }
        });


         */

        ButtonContext bcontext1 = new ButtonContext(defaultButtonColor, "BACK");
        createButton(buttonY -= buttonGap, bcontext1, new Button.ButtonCallbackInterface() {
            @Override
            public void onClick() {
                flappyBird.get_menu().set_menu(Menu.MenuType.MAIN_MENU);
            }
        });
    }
}
