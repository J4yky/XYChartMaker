package org.example.chartmaker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    FileChooser fileChooser = new FileChooser();

    private Tooltip maxYTooltip;

    @FXML
    private ListView<String> listView;
    @FXML
    private LineChart<Number, Number> lineChart;
    ArrayList<String> loadedFiles = new ArrayList<>();
    @FXML
    void openFile() {
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                if (file.getName().endsWith(".xy")) {
                    loadedFiles.add(file.getPath());
                    listView.getItems().add(file.getPath());
                } else {
                    System.out.println("Warning: Ignoring non-xy file: " + file.getName());
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\"));

        maxYTooltip = new Tooltip();
        maxYTooltip.setAutoHide(true);

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateChartWithFileData(newValue);
            }
        });
    }
    private void updateChartWithFileData(String filePath){
        lineChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        XYChart.Series<Number, Number> maxYSeries = new XYChart.Series<>();

        File file = new File(filePath);
        String fileName = file.getName();
        series.setName(fileName);

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double maxY_X = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                if(values.length == 2){
                    try{
                        double x = Double.parseDouble(values[0]);
                        double y = Double.parseDouble(values[1]);
                        series.getData().add(new XYChart.Data<>(x,y));

                        if(x < minX) minX = x;
                        if(x > maxX) maxX = x;
                        if(y > maxY){
                            maxY = y;
                            maxY_X = x;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing line: " + line);
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        XYChart.Data<Number, Number> maxYData = new XYChart.Data<>(maxY_X, maxY);

        maxYSeries.getData().add(maxYData);
        maxYSeries.setName("Max Y for " + fileName);

        lineChart.getData().add(series);
        lineChart.getData().add(maxYSeries);

        maxYTooltip.setText("(" + maxY_X + ", " + maxY + ")");

        Node maxYNode = maxYData.getNode();
        if (maxYNode != null) {
            maxYNode.setOnMouseEntered(event -> ShowToolTIp.showTooltip(event, maxYTooltip));
        }

        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(minX);
        xAxis.setUpperBound(maxX);
        xAxis.setTickUnit((maxX - minX) / 10);
    }
    @FXML
    void calculateFWHMButton() {
        if(lineChart.getData().isEmpty()){
            showErrorDialog("Line Chart with no data.");
        } else {
            LRPointsSeries fwhmPoints = CalculateAdditionalPoints.calculateFWHMFunction(lineChart.getData().getFirst());

            lineChart.getData().add(fwhmPoints.getLeftPoints());
            lineChart.getData().add(fwhmPoints.getRightPoints());
            lineChart.getData().add(fwhmPoints.getFWHM());
        }
    }
    private void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}