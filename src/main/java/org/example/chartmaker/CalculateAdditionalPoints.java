package org.example.chartmaker;

import javafx.scene.chart.XYChart;

public class CalculateAdditionalPoints {

    public static LRPointsSeries calculateFWHMFunction(XYChart.Series<Number, Number> series){
        XYChart.Series<Number, Number> additionalPointsLeft = new XYChart.Series<>();
        XYChart.Series<Number, Number> additionalPointsRight = new XYChart.Series<>();
        double maxY = Double.MIN_VALUE;
        int maxIndex = -1;

        for (int i = 0; i < series.getData().size(); i++) {
            double y = series.getData().get(i).getYValue().doubleValue();
            if (y > maxY) {
                maxY = y;
                maxIndex = i;
            }
        }

        double halfMaxY = maxY / 2.0;

        double leftXT;
        double leftXB;
        double leftYT;
        double leftYB;
        for (int i = maxIndex; i >= 0; i--) {
            double y = series.getData().get(i).getYValue().doubleValue();
            if (y == halfMaxY) {
                leftXT = series.getData().get(i).getXValue().doubleValue();
                leftYT = series.getData().get(i).getYValue().doubleValue();

                XYChart.Data<Number, Number> leftT = new XYChart.Data<>(leftXT, leftYT);
                additionalPointsLeft.getData().add(leftT);
                break;
            }
            else if(y < halfMaxY){
                leftXT = series.getData().get(i + 1).getXValue().doubleValue();
                leftYT = series.getData().get(i + 1).getYValue().doubleValue();
                leftXB = series.getData().get(i).getXValue().doubleValue();
                leftYB = series.getData().get(i).getYValue().doubleValue();

                XYChart.Data<Number, Number> leftT = new XYChart.Data<>(leftXT, leftYT);
                XYChart.Data<Number, Number> leftB = new XYChart.Data<>(leftXB, leftYB);
                additionalPointsLeft.getData().add(leftT);
                additionalPointsLeft.getData().add(leftB);
                break;
            }
        }

        double rightXT;
        double rightXB;
        double rightYT;
        double rightYB ;
        for (int i = maxIndex; i < series.getData().size(); i++) {
            double y = series.getData().get(i).getYValue().doubleValue();
            if (y == halfMaxY) {
                rightXT = series.getData().get(i).getXValue().doubleValue();
                rightYT = series.getData().get(i).getYValue().doubleValue();

                XYChart.Data<Number, Number> rightT = new XYChart.Data<>(rightXT, rightYT);
                additionalPointsRight.getData().add(rightT);
                break;
            }
            else if(y < halfMaxY){
                rightXT = series.getData().get(i - 1).getXValue().doubleValue();
                rightYT = series.getData().get(i - 1).getYValue().doubleValue();
                rightXB = series.getData().get(i).getXValue().doubleValue();
                rightYB = series.getData().get(i).getYValue().doubleValue();

                XYChart.Data<Number, Number> rightT = new XYChart.Data<>(rightXT, rightYT);
                XYChart.Data<Number, Number> rightB = new XYChart.Data<>(rightXB, rightYB);
                additionalPointsRight.getData().add(rightT);
                additionalPointsRight.getData().add(rightB);
                break;
            }
        }
        return new LRPointsSeries(additionalPointsLeft, additionalPointsRight);
    }
}