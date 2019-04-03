package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ImportBillController implements Initializable {
	
	String []data= {"Hi","Hello","Hakuna","Matata"};

	@FXML
	private DatePicker billDate;

	@FXML
	private TextField distPhone;

	@FXML
	private TextField distName;

	@FXML
	private TableColumn<?, ?> barcodeColumn;

	@FXML
	private TableColumn<?, ?> itemColumn;

	@FXML
	private TableColumn<?, ?> quantityColumn;

	@FXML
	private TableColumn<?, ?> buyPriceColumn;

	@FXML
	private TableColumn<?, ?> sellPriceColumn;

	@FXML
	private TableColumn<?, ?> TotalBuyPriceColumn;

	@FXML
	private TableColumn<?, ?> TotalSellPriceColumn;

	@FXML
	private Label collectTotalBuyPriceLabel;

	@FXML
	private Label collectTotalSellPriceLabel;

	@FXML
	private Button saveButton;

	@FXML
	private Button cancelButton;

	@FXML
	private TextField barcodeReader;

	@FXML
	void buttonHandler(ActionEvent event) {

	}

	@FXML
	void barcodeHandler(KeyEvent event) throws SQLException {
		String barcode = "", itemName = "";
		int quant = 0;
		double buyPrice = 0, sellPrice = 0;
		LocalDate expDate;

		if (event.getSource().toString().contains("barcodeReader"))
			if (event.getCode().equals(KeyCode.ENTER)) {
				barcode = barcodeReader.getText();
/*
				// «–« «·»«—ﬂÊœ Â«œ „ÊÃÊœ »«·œ« « »Ì” „⁄‰« Â „⁄·Ê„« Â „⁄‰« »” »‰ÿ·» «·ﬂ„Ì…
				// Ê «—ÌŒ «·«‰ Â«¡ »œÊ‰ «”⁄«—
				if (true)
					;
				// «–« ·√ »‰‘Ê› «·«”„ «–« „ÊÃÊœ »«·œ« « »Ì” ° »‰⁄ÿÌ «·»«—ﬂÊœ  »⁄ Â«œ «·«”„
				// ··„ €Ì— »«—ﬂÊœ Ê»‰«Œœ ﬂ„Ì… Ê «—ÌŒ «‰ Â«¡
				else if (true)
					;
				// »«·Õ«· Ì‰ ÂœÊ· «·”·⁄… „ÊÃÊœ… »«·„Œ“‰ › „« »‰÷Ì›Â« »” »‰⁄œ· ⁄ ﬂ„Ì Â« Ê «—ÌŒÂ«

				// «–« «·»«—ﬂÊœ Ê«·«”„ Ãœ«œ ° »‰«Œœ ﬂ· «·„⁄·Ê„«  Ê»‰÷Ì›Â« ··„Œ“‰ ﬂ”·⁄… ÃœÌœ…
				else
					;
					
					
					*/
				
				

				// show Calender dialog

				Dialog<LocalDate> dateDialog = new Dialog<>();
				dateDialog.setTitle(" «—ÌŒ «‰ Â«¡ «·”·⁄…");
				dateDialog.setHeaderText("«·—Ã«¡ «œŒ«·  «—ÌŒ «‰ Â«¡ «·”·⁄…");

				Stage stage = (Stage) dateDialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/application/images/icon.png"));

				DatePicker datePicker = new DatePicker();
				dateDialog.getDialogPane().setContent(datePicker);

				ButtonType buttonTypeOk = new ButtonType(" √ﬂÌœ", ButtonData.OK_DONE);
				dateDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

				dateDialog.setResultConverter(new Callback<ButtonType, LocalDate>() {
					@Override
					public LocalDate call(ButtonType b) {

						if (b == buttonTypeOk) {

							return datePicker.getValue();
						}

						return null;
					}
				});

//				System.out.println(dateDialog.showAndWait().get());
				expDate=dateDialog.showAndWait().get();
				// show ItemName Dialog

				Dialog<String> nameDialog = new Dialog<>();
				nameDialog.setTitle("«”„ «·”·⁄…");
				nameDialog.setHeaderText("«·—Ã«¡ «œŒ«· «”„ «·”·⁄…");

				Stage stage2 = (Stage) nameDialog.getDialogPane().getScene().getWindow();
				stage2.getIcons().add(new Image("/application/images/icon.png"));

				TextField nameTF = new TextField("");
				TextFields.bindAutoCompletion(nameTF, data);
				nameDialog.getDialogPane().setContent(nameTF);

				ButtonType buttonTypeOk2 = new ButtonType(" √ﬂÌœ", ButtonData.OK_DONE);
				nameDialog.getDialogPane().getButtonTypes().add(buttonTypeOk2);

				nameDialog.setResultConverter(new Callback<ButtonType, String>() {
					@Override
					public String call(ButtonType b) {

						if (b == buttonTypeOk2) {

							return nameTF.getText();
						}

						return null;
					}
				});

//				System.out.println(nameDialog.showAndWait().get());
				itemName=nameDialog.showAndWait().get();
				
				
    			TextInputDialog dialog=new TextInputDialog();
    			
    			
//    			dialog.setTitle("«”„ «·”·⁄…"); dialog.setHeaderText("«·—Ã«¡ «œŒ«· «”„ «·”·⁄…");
//    			itemName=dialog.showAndWait().get();
//    			
    			dialog=new TextInputDialog();
    			dialog.setTitle("«·ﬂ„Ì…"); dialog.setHeaderText("«·—Ã«¡ «œŒ· «·ﬂ„Ì…");
    			quant=Integer.parseInt(dialog.showAndWait().get());
//    			
    			dialog=new TextInputDialog();
    			dialog.setTitle("”⁄— ‘—«¡ «·ÊÕœ…"); dialog.setHeaderText("«·—Ã«¡ «œŒ«· ”⁄— ‘—«¡ «·ÊÕœ…");
    			buyPrice=Double.parseDouble(dialog.showAndWait().get());
//    			
    			dialog=new TextInputDialog();
    			dialog.setTitle("”⁄— »Ì⁄ «·ÊÕœ…"); dialog.setHeaderText("«·—Ã«¡ «œŒ«· ”⁄— »Ì⁄ «·ÊÕœ…");
    			sellPrice=Double.parseDouble(dialog.showAndWait().get());
//
    			System.out.println(barcode+"    "+itemName+"    "+quant+"    "+buyPrice+"    "+sellPrice+"	"+expDate);
    			
    			// Data Base Query 
    			
    			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    			Connection connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","ahmad","112233");
    			Statement statement=connection.createStatement();
    			
    			
    			String q1="insert into STORED (name,barcode, quantity, buy_price, sell_price, exp_date) values('"+itemName+"','"+barcode+"',"+quant+","+buyPrice+","+sellPrice+",TO_DATE('"+expDate+"','YYYY-MM-DD'))";
    			//String q1="delete from stored";
    			//statement.executeQuery(q1);
    			statement.executeUpdate(q1);
    			
    			
    			barcodeReader.clear();
    			
			}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> barcodeReader.requestFocus());
		billDate.setValue(LocalDate.now());
	
		
	}

}
