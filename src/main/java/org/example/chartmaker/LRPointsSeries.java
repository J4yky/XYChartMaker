package org.example.chartmaker;

import javafx.scene.chart.XYChart;

public class LRPointsSeries {
    private final XYChart.Series<Number, Number> leftPoints;
    private final XYChart.Series<Number, Number> rightPoints;
    private final XYChart.Series<Number, Number> halfPoints;

    public LRPointsSeries(XYChart.Series<Number, Number> leftPoints, XYChart.Series<Number, Number> rightPoints,
                          XYChart.Series<Number, Number> halfPoints, double deltaXValue) {
        this.leftPoints = leftPoints;
        leftPoints.setName("Neighbouring points (L)");
        this.rightPoints = rightPoints;
        rightPoints.setName("Neighbouring points (R)");
        this.halfPoints = halfPoints;
        halfPoints.setName("FWHM(Δx) = " + deltaXValue);
    }

    public XYChart.Series<Number, Number> getLeftPoints() {
        return leftPoints;
    }

    public XYChart.Series<Number, Number> getRightPoints() {
        return rightPoints;
    }

    public XYChart.Series<Number, Number> getFWHM() {
        return halfPoints;
    }
}