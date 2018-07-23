package com.archangel_design.core_wars.utils;

import javafx.scene.media.AudioClip;

public class SoundPlayer {

    private static AudioClip ambientSound;

    public static void playSound(Sound sound) {
        switch (sound) {
            case SND_OPENING:
                Assets.getAudio("opening.wav").play();
                break;
            case SND_BUZZER:
                Assets.getAudio("buzzer.wav").play();
                break;
            case SND_MOVE:
                Assets.getAudio("move.wav").play();
                break;
            case SND_SCAN:
                Assets.getAudio("scan.wav").play();
                break;
            case SND_SHOOT:
                Assets.getAudio("shot.wav").play();
                break;
            case SND_EXPLOSION:
                Assets.getAudio("explosion.wav").play();
                break;
        }
    }

    public static void playAmbience(Sound snd) {
        switch (snd) {
            case AMB_NATURE:
                ambientSound = Assets.getAudio("nature-ambience.mp3");
                // https://bugs.openjdk.java.net/browse/JDK-8088116
                //ambientSound.setCycleCount(10);
                ambientSound.play(0.6);
        }
    }

    public static void stopAmbience() {
        if (ambientSound == null)
            return;

        ambientSound.stop();
    }
}
