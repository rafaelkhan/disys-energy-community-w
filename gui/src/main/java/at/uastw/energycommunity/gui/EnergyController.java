package at.uastw.energycommunity.gui;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EnergyController {

    @FXML private Label lbCommunityPool;
    @FXML private Label lbGridPortion;
    @FXML private Label lbStatus;

    @FXML private DatePicker dpStart;
    @FXML private TextField tfStartTime;
    @FXML private DatePicker dpEnd;
    @FXML private TextField tfEndTime;

    @FXML private Label lbTotalProduced;
    @FXML private Label lbTotalUsed;
    @FXML private Label lbTotalGrid;

    @FXML private TableView<HistoricalUsage> tableHistorical;
    @FXML private TableColumn<HistoricalUsage, String> colHour;
    @FXML private TableColumn<HistoricalUsage, Number> colProduced;
    @FXML private TableColumn<HistoricalUsage, Number> colUsed;
    @FXML private TableColumn<HistoricalUsage, Number> colGrid;

    private final EnergyApiService api = new EnergyApiService();
    private final DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        dpStart.setValue(LocalDate.now().minusDays(1));
        tfStartTime.setText("00:00");
        dpEnd.setValue(LocalDate.now());
        tfEndTime.setText("23:00");

        colHour.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getHour().format(labelFormatter)));
        colProduced.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCommunityProduced()));
        colUsed.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCommunityUsed()));
        colGrid.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getGridUsed()));

        onRefresh();
    }

    @FXML
    protected void onRefresh() {
        try {
            CurrentPercentage current = api.getCurrent();
            lbCommunityPool.setText(String.format("%.2f%% used", current.getCommunityDepleted()));
            lbGridPortion.setText(String.format("%.2f%%", current.getGridPortion()));
            lbStatus.setText("Current data loaded for " + current.getHour().format(labelFormatter));
        } catch (Exception e) {
            lbStatus.setText("Error loading current data: " + e.getMessage());
        }
    }

    @FXML
    protected void onShowData() {
        try {
            LocalDateTime start = LocalDateTime.of(dpStart.getValue(), parseTime(tfStartTime.getText()));
            LocalDateTime end = LocalDateTime.of(dpEnd.getValue(), parseTime(tfEndTime.getText()));
            List<HistoricalUsage> data = api.getHistorical(start.toString(), end.toString());
            tableHistorical.setItems(FXCollections.observableArrayList(data));

            double totalProduced = data.stream().mapToDouble(HistoricalUsage::getCommunityProduced).sum();
            double totalUsed = data.stream().mapToDouble(HistoricalUsage::getCommunityUsed).sum();
            double totalGrid = data.stream().mapToDouble(HistoricalUsage::getGridUsed).sum();
            lbTotalProduced.setText(String.format("%.3f kWh", totalProduced));
            lbTotalUsed.setText(String.format("%.3f kWh", totalUsed));
            lbTotalGrid.setText(String.format("%.3f kWh", totalGrid));

            lbStatus.setText("Loaded " + data.size() + " historical entries.");
        } catch (Exception e) {
            lbStatus.setText("Error loading historical data: " + e.getMessage());
        }
    }

    private LocalTime parseTime(String text) {
        if (text == null || text.isBlank()) {
            return LocalTime.MIDNIGHT;
        }
        return LocalTime.parse(text.trim());
    }
}
