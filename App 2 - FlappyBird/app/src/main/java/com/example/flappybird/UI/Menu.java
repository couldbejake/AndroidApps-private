package com.example.flappybird.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Pair;

import com.example.flappybird.FlappyBird;
import com.example.flappybird.UI.menus.MainMenu;
import com.example.flappybird.UI.menus.SettingsMenu;

public class Menu {

    private final FlappyBird flappyBird;
    private final Typeface menuTypeFace;
    SubMenu currentMenu;
    private Context context;
    private Pair<Integer, Integer> screenResolution = new Pair<Integer, Integer>(0, 0);
    public Menu(FlappyBird flappyBird, Context context) {

        this.context = context;

        this.flappyBird = flappyBird;
        this.context = context;

        this.set_menu(MenuType.MAIN_MENU);

        this.menuTypeFace = Typeface.createFromAsset(this.context.getAssets(), "inter_variablefont.ttf");
    }

    public void draw(Pair<Integer, Integer> screenResolution, Canvas canvas) {
        this.screenResolution = screenResolution;

        // put border around text

        this.drawText(canvas, "[ FLAPPYBIRD ∞ ] ", 0, 230 + (screenResolution.second / 4), 100, true);
        this.currentMenu.draw(screenResolution, canvas, screenResolution.second / 4);

    }

    public void update(FlappyBird flappyBird) {
        this.currentMenu.update(flappyBird);
    }

    public void drawText(Canvas canvas, String text, int x, int y, int size, Boolean center) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(size);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);

        textPaint.setTypeface(this.menuTypeFace);

        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        if (center) {
            x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        }

        canvas.drawText(text, x, y, textPaint);

    }

    public boolean onTouch(float x, float y) {
        return currentMenu.onTouch(x, y);
    }

    public void set_menu(MenuType menuType) {
        switch (menuType) {
            case MAIN_MENU:
                this.currentMenu = new MainMenu(flappyBird, this.context);
                break;
            case SETTINGS_MENU:
                this.currentMenu = new SettingsMenu(flappyBird, this.context);
                break;
        }
    }

    public enum MenuType {
        MAIN_MENU,
        SETTINGS_MENU,
    }
}
