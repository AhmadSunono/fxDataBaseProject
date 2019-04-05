package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.StoredItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StoreInventoryController implements Initializable {
	
	@FXML
	private TableView<StoredItem> table;

    @FXML
    private TableColumn<StoredItem, String> barcodeColumn;

    @FXML
    private TableColumn<StoredItem, String> itemColumn;

    @FXML
    private TableColumn<StoredItem, String> quantityColumn;

    @FXML
    private TableColumn<StoredItem, Double> buyPriceColumn;

    @FXML
    private TableColumn<StoredItem, Double> sellPriceColumn;

    @FXML
    private TableColumn<StoredItem, Double> totalItemProfitColumn;

    @FXML
    private TableColumn<StoredItem, String> expiryDateColumn;

    @FXML
    private Label totalBuyPrice;

    @FXML
    private Label totalSellPrice;

    @FXML
    private Label totalProfit;
    
    @FXML
    private Button updateExpiryDate;

    @FXML
    void buttonHandler(ActionEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		int count=0;
		
		  ObservableList<StoredItem> data = FXCollections.observableArrayList();
		  
		  try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","ahmad","112233");
			Statement statement=connection.createStatement();
			String q="select * from stored";
			ResultSet rs=statement.executeQuery(q);
			
			while(rs.next()) {
				data.add(new StoredItem(rs.getString("barcode"),rs.getString("name"),rs.getDouble("buy_price"),rs.getDouble("sell_price"),rs.getDate("exp_date").toLocalDate(),rs.getInt("quantity")));			
				count++;
			}
			
			table.setItems(data);
			barcodeColumn.setCellValueFactory( new PropertyValueFactory<>("barcode") );
			itemColumn.setCellValueFactory( new PropertyValueFactory<>("name") );
			quantityColumn.setCellValueFactory( new PropertyValueFactory<>("quant") );
			buyPriceColumn.setCellValueFactory( new PropertyValueFactory<>("buyPrice") );
			sellPriceColumn.setCellValueFactory( new PropertyValueFactory<>("sellPrice") );
			totalItemProfitColumn.setCellValueFactory( new PropertyValueFactory<>("totalProfit") );
			expiryDateColumn.setCellValueFactory( new PropertyValueFactory<>("expDate") );
			
			double totalBuyPrice=0; double totalSellPrice=0; double totalProfit=0;
			
			for(int i=0;i<count;i++) {
				totalBuyPrice+=buyPriceColumn.getCellObservableValue(i).getValue();		
				totalSellPrice+=sellPriceColumn.getCellObservableValue(i).getValue();	
				totalProfit+=totalItemProfitColumn.getCellObservableValue(i).getValue();	
			}
			
			this.totalBuyPrice.setText(totalBuyPrice+"");
			this.totalSellPrice.setText(totalSellPrice+"");
			this.totalProfit.setText(totalProfit+"");

		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
