package com.archangel_design.core_wars.utils;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public static Image getImage(String name, double rotation) {
        ImageView iv = new ImageView(getImage(name));

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return iv.snapshot(params, null);
    }
}
