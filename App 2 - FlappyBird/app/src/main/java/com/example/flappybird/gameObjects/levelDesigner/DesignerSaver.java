package com.example.flappybird.gameObjects.levelDesigner;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.example.flappybird.gameObjects.CollectibleItem;
import com.example.flappybird.gameObjects.savedObjects.GameSave;
import com.example.flappybird.gameObjects.savedObjects.SavedCollectible;
import com.example.flappybird.gameObjects.savedObjects.SavedPipe;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DesignerSaver {

    private final Context context;

    public DesignerSaver(Context context) {
        this.context = context;
    }

    /*
        Saves the current game to an XML file
     */
    public void saveGame(LevelDesigner designer) {

        Log.e("Saving game..", "...");

        ArrayList<DesignerPipe> pipes = designer.get_pipe_list();
        ArrayList<DesignerCollectibleItem> collectibles = designer.get_collectibles();

        Log.e("Pipe Count", pipes.size() + "");
        Log.e("Collectibles Count", collectibles.size() + "");

        try {
            File dir = context.getFilesDir();
            File file = new File(dir, "file.xml");
            FileOutputStream fos = new FileOutputStream(file);

            // Create a new instance of the XML serializer.
            XmlSerializer serializer = Xml.newSerializer();

            // Set the output stream for the serializer to the file output stream.
            serializer.setOutput(fos, "UTF-8");

            // Start the document.
            serializer.startDocument(null, Boolean.valueOf(true));

            // Start a new element with the specified tag name.
            serializer.startTag(null, "GameSave");
            serializer.attribute(null, "GameName", "MyCoolGame");

            serializer.startTag(null, "Pipes");

            for (DesignerPipe pipe : pipes) {
                serializer.startTag(null, "Pipe");
                int x = pipe.get_x();
                int y = pipe.get_y();
                int pipeHeight = pipe.getPipe().getGapHeight();
                serializer.attribute(null, "PipeHeight", String.valueOf(pipeHeight));
                serializer.attribute(null, "x", String.valueOf(x));
                serializer.attribute(null, "y", String.valueOf(x));
                serializer.endTag(null, "Pipe");
            }

            serializer.endTag(null, "Pipes");

            serializer.startTag(null, "Collectibles");

            for (DesignerCollectibleItem collectible : collectibles) {
                serializer.startTag(null, "Collectible");
                int x = collectible.get_x();
                int y = collectible.get_y();
                String type = collectible.get_collectible_type_string();
                serializer.attribute(null, "x", String.valueOf(x));
                serializer.attribute(null, "y", String.valueOf(y));
                serializer.attribute(null, "type", type);
                serializer.endTag(null, "Collectible");
            }

            serializer.endTag(null, "Collectibles");

            // End the element.
            serializer.endTag(null, "GameSave");

            // End the document.
            serializer.endDocument();

            // Close the file output stream.
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Loads the game from an XML file
     */
    public GameSave loadGame() {

        File dir = context.getFilesDir();
        File file = new File(dir, "file.xml");

        GameSave gameSave = new GameSave();

        try {

            FileInputStream inputStream = new FileInputStream(file);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(inputStream, null);

            // Initialize variables to store the attribute values
            String gameName = null;
            int pipeHeight, x, y;
            String collectibleTypeName;
            CollectibleItem.Collectible_type collectibleType;

            // Loop over the XML elements
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("GameSave")) {
                            // Get the GameName attribute value
                            gameName = parser.getAttributeValue(null, "GameName");
                        } else if (name.equals("Pipe")) {
                            // Get the attributes of the Pipe element
                            pipeHeight = Integer.parseInt(parser.getAttributeValue(null, "PipeHeight"));
                            x = Integer.parseInt(parser.getAttributeValue(null, "x"));
                            y = Integer.parseInt(parser.getAttributeValue(null, "y"));
                            gameSave.addSavedPipe(new SavedPipe(pipeHeight, x, y));
                            Log.d("Pipe", "PipeHeight: " + pipeHeight + ", x: " + x + ", y: " + y);
                        } else if (name.equals("Collectible")) {
                            // Get the attributes of the Collectible element
                            collectibleTypeName = parser.getAttributeValue(null, "type");
                            collectibleType = CollectibleItem.Collectible_type.valueOf(collectibleTypeName);

                            x = Integer.parseInt(parser.getAttributeValue(null, "x"));
                            y = Integer.parseInt(parser.getAttributeValue(null, "y"));
                            gameSave.addSavedCollectible(new SavedCollectible(collectibleType, x, y));
                            Log.d("Collectible", "Type: " + collectibleType + ", Location: " + x + ", " + y);
                        }
                        break;
                }
                eventType = parser.next();
            }

            // Log the GameName attribute value
            gameSave.setSavedname(gameName);

            return gameSave;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
