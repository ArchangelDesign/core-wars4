package com.archangel_design.core_wars.utils;

import javafx.scene.image.Image;

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
}
