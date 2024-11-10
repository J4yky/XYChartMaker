package org.example.chartmaker;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class ShowToolTIp {
    public static void showTooltip(MouseEvent event, Tooltip tooltip) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            Tooltip.install((Node) event.getSource(), tooltip);
            tooltip.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
