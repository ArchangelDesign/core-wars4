package com.archangel_design.core_wars.utils;

import com.archangel_design.core_wars.utils.bugs.Direction;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Assets {

    private static HashMap<String, Image> buffer = new HashMap<>();
    private static HashMap<String, AudioClip> soundBuffer = new HashMap<>();

    public static Image getImage(String name) {
        try {
            URL url = new File("assets/" + name).toURI().toURL();
            if (buffer.containsKey(url.toString()))
                return buffer.get(url.toString());

            buffer.put(url.toString(), new Image(url.toString()));
            return buffer.get(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image getImage(String name, Direction rotation) {
        ImageView iv = new ImageView(getImage(name));

        switch (rotation) {
            case DOWN: iv.setRotate(180);break;
            case LEFT: iv.setRotate(270);break;
            case RIGHT: iv.setRotate(90);break;
        }
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return iv.snapshot(params, null);
    }

    public static AudioClip getAudio(String fileName) {
        try {
            URL url = new File("assets/sounds/" + fileName).toURI().toURL();
            if (soundBuffer.containsKey(url.toString()))
                return soundBuffer.get(url.toString());
            soundBuffer.put(url.toString(), new AudioClip(url.toString()));
            return soundBuffer.get(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File f = new File("assets/sounds/" + fileName);
        return new AudioClip(f.toURI().toString());
    }

    public static Media getMedia(String fileName) {
        URL url = null;
        try {
            url = new File("assets/sounds/" + fileName).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new Media(url.toString());
    }
}
