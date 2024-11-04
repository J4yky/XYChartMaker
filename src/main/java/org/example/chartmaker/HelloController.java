package org.example.chartmaker;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
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

    @FXML
    private ListView<String> listView;

    @FXML
    private LineChart<Number, Number> lineChart;
    ArrayList<String> loadedFiles = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\"));

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateChartWithFileData(newValue);
            }
        });
    }
    @FXML
    void openFile(ActionEvent event) {
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

    private void updateChartWithFileData(String filePath){
        lineChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                if(values.length == 2){
                    try{
                        double x = Double.parseDouble(values[0]);
                        double y = Double.parseDouble(values[1]);
                        series.getData().add(new XYChart.Data<>(x, y));

                        if(x < minX) minX = x;
                        if(x > maxX) maxX = x;
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing line: " + line);
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);

        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(minX);
        xAxis.setUpperBound(maxX);
        xAxis.setTickUnit((maxX - minX) / 10);
    }
}
