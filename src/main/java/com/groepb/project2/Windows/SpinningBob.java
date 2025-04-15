package com.groepb.project2.Windows;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpinningBob {
    public ImageView bobImage(){
        ImageView imageView = new javafx.scene.image.ImageView("/bob.png");
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        rotation(imageView);

        return imageView;
    }

    private void rotation(ImageView imageView){
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), imageView);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();
    }
}
