package org.example.chartmaker;

import javafx.scene.chart.XYChart;

public class LRPointsSeries {
    private final XYChart.Series<Number, Number> leftPoints;
    private final XYChart.Series<Number, Number> rightPoints;

    public LRPointsSeries(XYChart.Series<Number, Number> leftPoints, XYChart.Series<Number, Number> rightPoints) {
        this.leftPoints = leftPoints;
        leftPoints.setName("Neighbouring points (L)");
        this.rightPoints = rightPoints;
        rightPoints.setName("Neighbouring points (R)");
    }

    public XYChart.Series<Number, Number> getLeftPoints() {
        return leftPoints;
    }

    public XYChart.Series<Number, Number> getRightPoints() {
        return rightPoints;
    }
}
