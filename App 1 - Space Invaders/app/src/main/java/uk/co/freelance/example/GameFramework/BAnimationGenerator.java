package uk.co.reading.example.GameFramework;

import android.util.Log;

import java.util.Iterator;
import java.util.Vector;

public class BAnimationGenerator {

    // a vector of background light beams
    private Vector<BAnimationElement> background_elements = new Vector<BAnimationElement>();

    // variables for the canvas width and height
    private int mCanvasWidth;
    private int mCanvasHeight;

    public BAnimationGenerator() {
        // empty constructor!
    }

    // generates a random number (integer) between min and max.
    public int rand_range(int Min, int  Max){
        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }

    // runs every time the game ticks
    public void do_tick(int mCanvasWidth, int mCanvasHeight){

        // update the local width and height of the canvas.

        this.mCanvasWidth = mCanvasWidth;
        this.mCanvasHeight = mCanvasHeight;

        // limit the UI to 20 light trails in the background

        if(background_elements.size() < 20){

            // generate a random "size" value, which determines it's depth
                // or how far back in 3D it looks.

            int typeSize = this.rand_range(5, 20);

            // set width and height based off the typeSize
            int width = (int)(typeSize * 0.5);
            int height = typeSize * 10;

            // set the speed, so that larger light trails move slower.

            int speed = 800 / (typeSize);

            // set the x position to a random position
            int xPos = this.rand_range(0, this.mCanvasWidth);

            // set the y position so it's above the screen, and give it a random offset.
            // this allows there to be a delay when the light beam falls.
            int yPos = -height - (this.rand_range(0, 15) * height);

            // adds the background elements to a vector

            background_elements.add(new BAnimationElement(
                xPos, yPos, width, height, speed
            ));

        }

        // creates an iterator for background elements

        Iterator<BAnimationElement> i = background_elements.iterator();

        // loops through the iterator

        while (i.hasNext()) {
            BAnimationElement background_element = i.next();

            // if the beam is off the screen delete it.
            // clear the memory to prevent a crash.
            if (background_element.get_yPos() > this.mCanvasHeight) {
                background_element = null;
                i.remove();
            } else {
                background_element.tick();
            }
        }
    }

    public Vector<BAnimationElement> get_background_elements() {
        return background_elements;
    }

}