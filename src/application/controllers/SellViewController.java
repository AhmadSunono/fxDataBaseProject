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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SellViewController implements Initializable  {


    @FXML
    private TextField itemNameTF;

    @FXML
    private TextField barcodeTF;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<?, ?> barcodeColumn;

    @FXML
    private TableColumn<?, ?> itemColumn;

    @FXML
    private TableColumn<?, ?> itemPriceColumn;

    @FXML
    private TableColumn<?, ?> quantityColumn;

    @FXML
    private TableColumn<?, ?> totalPriceColumn;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveAndNewButton;

    @FXML
    private TextField totalPriceTF;
    
    ArrayList <String>data=new ArrayList<String>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","ahmad","112233");
			Statement statement=connection.createStatement();
			String q="select name from stored";
			ResultSet rs=statement.executeQuery(q);
			
			while(rs.next()) {
				data.add(rs.getString("name"));
			}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		TextFields.bindAutoCompletion(itemNameTF, data);
		Platform.runLater(()->barcodeTF.requestFocus());
		
		
		
	}
	
    @FXML
    void buttonHandler(ActionEvent event) {

    }

    @FXML
    void itemHandler(KeyEvent event) {
    	
    	if(event.getSource().toString().contains("barcodeTF")) {
    		if(event.getCode().equals(KeyCode.ENTER)) {
    			System.out.println(barcodeTF.getText());
    		}
    	}
    	else if(event.getSource().toString().contains("itemNameTF")) {
    		if(event.getCode().equals(KeyCode.ENTER)) {
    			System.out.println(itemNameTF.getText());
    		}
    	}
    	
    }


}
