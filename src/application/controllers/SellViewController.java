package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import application.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SellViewController implements Initializable {

	@FXML
	private TextField itemNameTF;

	@FXML
	private TextField barcodeTF;

	@FXML
	private TableView<Item> tableView;

	@FXML
	private TableColumn<Item, String> barcodeColumn;

	@FXML
	private TableColumn<Item, String> itemColumn;

	@FXML
	private TableColumn<Item, Double> itemPriceColumn;

	@FXML
	private TableColumn<Item, Integer> quantityColumn;

	@FXML
	private TableColumn<Item, Double> totalPriceColumn;

	@FXML
	private Button cancelButton;

	@FXML
	private Button saveAndNewButton;

	@FXML
	private TextField totalPriceTF;

	ArrayList<String> data = new ArrayList<String>();

	ObservableList<Item> itemsData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ahmad",
					"112233");
			Statement statement = connection.createStatement();
			String q = "select name from stored";
			ResultSet rs = statement.executeQuery(q);

			while (rs.next()) {
				data.add(rs.getString("name"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextFields.bindAutoCompletion(itemNameTF, data);
		Platform.runLater(() -> barcodeTF.requestFocus());

		barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
		itemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quant"));
		totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalSP"));
		tableView.setItems(itemsData);

	}

	@FXML
	void buttonHandler(ActionEvent event) {
		if (event.getSource().toString().contains("saveAndNewButton")) {
			
			/// After the query clear the table
			itemsData.clear();
			tableView.refresh();
			totalPriceTF.clear();
		}
		
		if (event.getSource().toString().contains("cancelButton")) {
			itemsData.clear();
			tableView.refresh();
			totalPriceTF.clear();
		}
	}

	@FXML
	void itemHandler(KeyEvent event) throws SQLException {

		if (event.getSource().toString().contains("barcodeTF"))
			if (event.getCode().equals(KeyCode.ENTER))
				showInTable(barcodeTF.getText());

		if (event.getSource().toString().contains("itemNameTF")) {
			if (event.getCode().equals(KeyCode.ENTER)) {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ahmad",
						"112233");
				Statement statement = connection.createStatement();
				String q = "";

				q = "select barcode from stored where name='" + itemNameTF.getText() + "'";
				ResultSet rs = statement.executeQuery(q);
				rs.next();
				showInTable(rs.getString("barcode"));
			}

		}
	}

	public void showInTable(String barcode) throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ahmad", "112233");
		Statement statement = connection.createStatement();
		String q = "";

		q = "select * from stored where barcode='" + barcode + "'";
		ResultSet rs = statement.executeQuery(q);
		rs.next();
		boolean exist = false;
		int i = 0;
		for (i = 0; i < itemsData.size(); i++) {

			if (itemsData.get(i).getBarcode().trim().equals(barcode.trim())) {
				exist = true;
				break;
			}
		}
		if (!exist)
			itemsData.add(new Item(rs.getString("barcode"), rs.getString("name"), 1, 0, rs.getDouble("sell_price"),
					null, 0, rs.getDouble("sell_price")));
		else
			itemsData.get(i).update();

		tableView.refresh();
		barcodeTF.clear();
		itemNameTF.clear();
		
		double sum=0;
		for(i=0;i<itemsData.size();i++) {
			sum+=itemsData.get(i).getTotalSP();
		}
		totalPriceTF.setText(sum+"");
		

	}

}
