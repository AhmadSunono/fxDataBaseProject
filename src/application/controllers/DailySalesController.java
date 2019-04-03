package application.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class DailySalesController implements Initializable {

    @FXML
    private TableColumn<?, ?> itemColumn;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private TableColumn<?, ?> TotalSellPriceColumn;

    @FXML
    private TableColumn<?, ?> profitColumn;

    @FXML
    private DatePicker date;

    @FXML
    private Label totalSellPrice;

    @FXML
    private Label totalProfit;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		date.setValue(LocalDate.now());
	}

}
