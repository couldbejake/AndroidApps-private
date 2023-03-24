package com.example.flappybird.UI;

public class ButtonContext {
    private int color;
    private String text;

    public ButtonContext(int color, String text) {
        this.color = color;
        this.text = text;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
