package com.archangel_design.core_wars.utils;

import com.archangel_design.core_wars.utils.bugs.Direction;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Assets {
    public static Image getImage(String name) {
        try {
            URL url = new File("assets/" + name).toURI().toURL();
            return new Image(url.toString());
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
        File f = new File("assets/sounds/" + fileName);
        return new AudioClip(f.toURI().toString());
    }
}
