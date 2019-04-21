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

import application.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ImportBillController implements Initializable {

	String[] storedItems = { "Hi", "Hello", "Hakuna", "Matata" };

	ObservableList<Item> data = FXCollections.observableArrayList();

	@FXML
	private DatePicker billDate;

	@FXML
	private TextField distPhone;

	@FXML
	private TextField distName;

	@FXML
	private TableView<Item> table;

	@FXML
	private TableColumn<Item, String> barcodeColumn;

	@FXML
	private TableColumn<Item, String> itemColumn;

	@FXML
	private TableColumn<Item, Integer> quantityColumn;

	@FXML
	private TableColumn<Item, Double> buyPriceColumn;

	@FXML
	private TableColumn<Item, Double> sellPriceColumn;

	@FXML
	private TableColumn<Item, Item> TotalBuyPriceColumn;

	@FXML
	private TableColumn<Item, Double> TotalSellPriceColumn;

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
	void buttonHandler(ActionEvent event) throws SQLException {
		if (event.getSource().toString().contains("saveButton")) {

			// هون بنشوف اول اشي في اسم موزع ولا لأ، اذا
			// لا خلص بنضيف عالستور مباشرة بدون بوت ايتم
			// اذا اه بنعمل الكويري هاي الي تحت
			// بعدين بنحدث الستور تحديث ،اذا الايتمز
			// موجودات بنزيد عليهم واذا لا بنضيفهم

			// Data Base Query
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ahmad",
					"112233");
			Statement statement = connection.createStatement();
			String q = "";

			// if name of supplier doesn't exists
			if (distName.getText().equals("")) {

				for (int i = 0; i < data.size(); i++) {
					/// we should make it here
					if (!data.get(i).getBarcode().equals("")) {
						q = "select * from stored where barcode ='" + data.get(i).getBarcode() + "'";
						ResultSet rs = statement.executeQuery(q);
						int count = 0;
						while (rs.next()) {
							count++;
						}
						if (count == 0) {

							q = "insert into stored values (" + data.get(i).getSellPrice() + ",'"
									+ data.get(i).getBarcode() + "','" + data.get(i).getName() + "',"
									+ data.get(i).getBuyPrice() + "," + data.get(i).getQuant() + ",TO_DATE('"
									+ data.get(i).getExpDate() + "','YYYY-MM-DD'))";
							statement.executeUpdate(q);
						}

						else {
							q = "update stored set quantity=quantity+" + data.get(i).getQuant() + "where barcode ='"
									+ data.get(i).getBarcode() + "'";
							statement.executeUpdate(q);
							q = "update stored set exp_date=TO_DATE('" + data.get(i).getExpDate()
									+ "','YYYY-MM-DD') where barcode = '" + data.get(i).getBarcode() + "'";
							statement.executeUpdate(q);

						}

					}

					else {

						int count = 0;
						int count2 = 0;
						int count3 = 0;
						q = "select * from stored where barcode between '000000000'and '000001000'";
						ResultSet rs = statement.executeQuery(q);
						while (rs.next()) {
							count3++;
						}
						String bar = "";
						if (count3 >= 0 && count3 < 10)
							bar = "00000000" + count3;
						else if (count3 >= 10 && count3 <= 100)
							bar = "0000000" + count3;
						else
							bar = "000000" + count3;

						q = "select * from stored where name ='" + data.get(i).getName() + "'";
						rs = statement.executeQuery(q);
						while (rs.next()) {
							count++;
						}

						if (count != 0) {
							q = "update stored set quantity=quantity+" + data.get(i).getQuant() + " where name='"
									+ data.get(i).getName() + "'";
							statement.executeUpdate(q);
							q = "update stored set exp_date=TO_DATE('" + data.get(i).getExpDate()
									+ "','YYYY-MM-DD') where name = '" + data.get(i).getName() + "'";
							statement.executeUpdate(q);
						} else {
							q = "insert into stored values (" + data.get(i).getSellPrice() + ",'" + bar + "','"
									+ data.get(i).getName() + "'," + data.get(i).getBuyPrice() + ","
									+ data.get(i).getQuant() + ",TO_DATE('" + data.get(i).getExpDate()
									+ "','YYYY-MM-DD'))";
							statement.executeUpdate(q);

						}

					}

				}

			}
			// if name of supplier exists
			else {
				q = "select * from supplier where supplier_name ='" + distName.getText() + "'";
				ResultSet rs = statement.executeQuery(q);
				int count = 0;
				while (rs.next()) {
					count++;
				}
				if (count == 0) {
					q = "insert into supplier values('" + distName.getText() + "','" + distPhone.getText() + "')";
					statement.executeUpdate(q);
				}
				// till here i had insert the name of supplier if it doesnt Exist

				for (int i = 0; i < data.size(); i++) {

					if (!data.get(i).getBarcode().equals("")) {

						q = "select * from bought where barcode ='" + data.get(i).getBarcode() + "'";
						rs = statement.executeQuery(q);
						count = 0;
						while (rs.next()) {
							count++;
						}
						if (count == 0) {
							q = "insert into bought values(" + data.get(i).getSellPrice() + ",'"
									+ data.get(i).getBarcode() + "','" + data.get(i).getName() + "',"
									+ data.get(i).getBuyPrice() + ")";
							statement.executeUpdate(q);
							q = "insert into buying_bill values('" + data.get(i).getBarcode() + "',TO_DATE('"
									+ LocalDate.now() + "','YYYY-MM-DD')," + data.get(i).getQuant() + ",'"
									+ distName.getText() + "')";
							statement.executeUpdate(q);

							count = 0;
							q = "select * from stored where barcode='" + data.get(i).getBarcode() + "'";
							rs = statement.executeQuery(q);
							while (rs.next()) {
								count++;
							}

							if (count != 0) {
								q = "update stored set quantity=quantity+" + data.get(i).getQuant() + "where barcode='"
										+ data.get(i).getBarcode() + "'";
								statement.executeUpdate(q);
								q = "update stored set exp_date=TO_DATE('" + data.get(i).getExpDate()
										+ "','YYYY-MM-DD') where name = '" + data.get(i).getName() + "'";
								statement.executeUpdate(q);
							}

							else {
								q = "insert into stored values (" + data.get(i).getSellPrice() + ",'"
										+ data.get(i).getBarcode() + "','" + data.get(i).getName() + "',"
										+ data.get(i).getBuyPrice() + "," + data.get(i).getQuant() + ",TO_DATE('"
										+ data.get(i).getExpDate() + "','YYYY-MM-DD'))";
								statement.executeUpdate(q);

							}

						}

						else {
							q = "insert into buying_bill values('" + data.get(i).getBarcode() + "',TO_DATE('"
									+ LocalDate.now() + "','YYYY-MM-DD')," + data.get(i).getQuant() + ",'"
									+ distName.getText() + "')";
							statement.executeUpdate(q);

							if (count != 0) {
								q = "update stored set quantity=quantity+" + data.get(i).getQuant() + "where barcode='"
										+ data.get(i).getBarcode() + "'";
								statement.executeUpdate(q);
								q = "update stored set exp_date=TO_DATE('" + data.get(i).getExpDate()
										+ "','YYYY-MM-DD') where name = '" + data.get(i).getName() + "'";
								statement.executeUpdate(q);
							} else {
								q = "insert into stored values (" + data.get(i).getSellPrice() + ",'"
										+ data.get(i).getName() + "','" + data.get(i).getName() + "',"
										+ data.get(i).getBuyPrice() + "," + data.get(i).getQuant() + ",TO_DATE('"
										+ data.get(i).getExpDate() + "','YYYY-MM-DD'))";
								statement.executeUpdate(q);

							}

						}

					}

					else {
						// here if supplier exists but barcode doesn't
						int countb = 0;
						int count2 = 0;
						int count3 = 0;
						q = "select * from stored where barcode between '000000000'and '000001000'";
						ResultSet rss = statement.executeQuery(q);
						while (rss.next()) {
							count3++;
						}
						String bar = "";
						if (count3 >= 0 && count3 < 10)
							bar = "00000000" + count3;
						else if (count3 >= 10 && count3 <= 100)
							bar = "0000000" + count3;
						else
							bar = "000000" + count3;

						q = "select * from stored where name ='" + data.get(i).getName() + "'";
						rss = statement.executeQuery(q);
						while (rs.next()) {
							countb++;
						}

						if (countb != 0) {
							q = "update stored set quantity=quantity+" + data.get(i).getQuant() + " where name='"
									+ data.get(i).getName() + "'";
							statement.executeUpdate(q);
							q = "update stored set exp_date=TO_DATE('" + data.get(i).getExpDate()
									+ "','YYYY-MM-DD') where name = '" + data.get(i).getName() + "'";
							statement.executeUpdate(q);
							q = "insert into buying_bill values('" + bar + "',TO_DATE('" + LocalDate.now()
									+ "','YYYY-MM-DD')," + data.get(i).getQuant() + ",'" + distName.getText() + "')";
							statement.executeUpdate(q);
						} else {
							q = "insert into stored values (" + data.get(i).getSellPrice() + ",'" + bar + "','"
									+ data.get(i).getName() + "'," + data.get(i).getBuyPrice() + ","
									+ data.get(i).getQuant() + ",TO_DATE('" + data.get(i).getExpDate()
									+ "','YYYY-MM-DD'))";
							statement.executeUpdate(q);
							q = "insert into buying_bill values('" + bar + "',TO_DATE('" + LocalDate.now()
									+ "','YYYY-MM-DD')," + data.get(i).getQuant() + ",'" + distName.getText() + "')";
							statement.executeUpdate(q);
						}

						q = "select * from bought where name='" + data.get(i).getName() + "'";
						ResultSet rs3 = statement.executeQuery(q);
						int count4 = 0;
						while (rs3.next()) {
							count4++;
						}
						if (count4 == 0) {
							q = "insert into bought values(" + data.get(i).getSellPrice() + ",'" + bar + "','"
									+ data.get(i).getName() + "'," + data.get(i).getBuyPrice() + ")";
							statement.executeUpdate(q);
						}

					}

				}

			}

			data.clear();
			table.refresh();

		}

	}

	@FXML
	void barcodeHandler(KeyEvent event) throws SQLException {
		String barcode = "", itemName = "";
		int quant = 0;
		double buyPrice = 0, sellPrice = 0, totalBP = 0, totalSP = 0;
		LocalDate expDate;

		if (event.getSource().toString().contains("barcodeReader"))
			if (event.getCode().equals(KeyCode.ENTER)) {
				barcode = barcodeReader.getText();
				
				// Data Base Query
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ahmad",
						"112233");
				Statement statement = connection.createStatement();
				String q = "";


				// barcode written
				if (!barcodeReader.getText().equals("")) {
					int count=0;
					q="select * from stored where barcode='"+barcodeReader.getText()+"'";
					ResultSet rs=statement.executeQuery(q);
					while(rs.next()) {count++;}
					
					if (count==0) {
						// barcode is new
						Dialog<String> nameDialog = new Dialog<>();
						nameDialog.setTitle("اسم السلعة");
						nameDialog.setHeaderText("الرجاء ادخال اسم السلعة");

						Stage stage2 = (Stage) nameDialog.getDialogPane().getScene().getWindow();
						stage2.getIcons().add(new Image("/application/images/icon.png"));

						TextField nameTF = new TextField("");
						TextFields.bindAutoCompletion(nameTF, storedItems);
						nameDialog.getDialogPane().setContent(nameTF);

						ButtonType buttonTypeOk2 = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

						itemName = nameDialog.showAndWait().get();
						TextInputDialog dialog = new TextInputDialog();

//			             
							dialog = new TextInputDialog();
							dialog.setTitle("الكمية");
							dialog.setHeaderText("الرجاء ادخل الكمية");
							quant = Integer.parseInt(dialog.showAndWait().get());
//			             
							dialog = new TextInputDialog();
							dialog.setTitle("سعر شراء الوحدة");
							dialog.setHeaderText("الرجاء ادخال سعر شراء الوحدة");
							buyPrice = Double.parseDouble(dialog.showAndWait().get());
//			             
							dialog = new TextInputDialog();
							dialog.setTitle("سعر بيع الوحدة");
							dialog.setHeaderText("الرجاء ادخال سعر بيع الوحدة");
							sellPrice = Double.parseDouble(dialog.showAndWait().get());

							// show Calender dialog
							Dialog<LocalDate> dateDialog = new Dialog<>();
							dateDialog.setTitle("تاريخ انتهاء السلعة");
							dateDialog.setHeaderText("الرجاء ادخال تاريخ انتهاء السلعة");

							Stage stage = (Stage) dateDialog.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("/application/images/icon.png"));

							DatePicker datePicker = new DatePicker();
							dateDialog.getDialogPane().setContent(datePicker);

							ButtonType buttonTypeOk = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

							expDate = dateDialog.showAndWait().get();
						
						
						
						
					} else {
						rs=statement.executeQuery(q);
						
						TextInputDialog dialog = new TextInputDialog();

						dialog = new TextInputDialog();
						dialog.setTitle("الكمية");
						dialog.setHeaderText("الرجاء ادخل الكمية");
						quant = Integer.parseInt(dialog.showAndWait().get());
						Dialog<LocalDate> dateDialog = new Dialog<>();
						dateDialog.setTitle("تاريخ انتهاء السلعة");
						dateDialog.setHeaderText("الرجاء ادخال تاريخ انتهاء السلعة");

						Stage stage = (Stage) dateDialog.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("/application/images/icon.png"));

						DatePicker datePicker = new DatePicker();
						dateDialog.getDialogPane().setContent(datePicker);

						ButtonType buttonTypeOk = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

						expDate = dateDialog.showAndWait().get();
					sellPrice=rs.getDouble("sell_price");
 					buyPrice=rs.getDouble("buy_price");
                    itemName=rs.getString("Name");
 					
					}
				}
				//barcode not written
				else {
					// ask for the name
					////////
					Dialog<String> nameDialog = new Dialog<>();
					nameDialog.setTitle("اسم السلعة");
					nameDialog.setHeaderText("الرجاء ادخال اسم السلعة");

					Stage stage2 = (Stage) nameDialog.getDialogPane().getScene().getWindow();
					stage2.getIcons().add(new Image("/application/images/icon.png"));

					TextField nameTF = new TextField("");
					TextFields.bindAutoCompletion(nameTF, storedItems);
					nameDialog.getDialogPane().setContent(nameTF);

					ButtonType buttonTypeOk2 = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

					itemName = nameDialog.showAndWait().get();
					
					q="select * from stored where name='"+itemName+"'";
					int count=0;
					ResultSet rs=statement.executeQuery(q);
					while(rs.next()) {count++;}
					
					if (count!=0) {
						// ask for date & quant
						rs=statement.executeQuery(q);
						rs.next();
						sellPrice=rs.getDouble("sell_price");
						buyPrice=rs.getDouble("buy_price");
						TextInputDialog dialog = new TextInputDialog();

						dialog = new TextInputDialog();
						dialog.setTitle("الكمية");
						dialog.setHeaderText("الرجاء ادخل الكمية");
						quant = Integer.parseInt(dialog.showAndWait().get());
						Dialog<LocalDate> dateDialog = new Dialog<>();
						dateDialog.setTitle("تاريخ انتهاء السلعة");
						dateDialog.setHeaderText("الرجاء ادخال تاريخ انتهاء السلعة");

						Stage stage = (Stage) dateDialog.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("/application/images/icon.png"));

						DatePicker datePicker = new DatePicker();
						dateDialog.getDialogPane().setContent(datePicker);

						ButtonType buttonTypeOk = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

						expDate = dateDialog.showAndWait().get();
						
					}

					else {
						// ask for everything
						
						
						
						
						TextInputDialog dialog = new TextInputDialog();

//		             
						dialog = new TextInputDialog();
						dialog.setTitle("الكمية");
						dialog.setHeaderText("الرجاء ادخل الكمية");
						quant = Integer.parseInt(dialog.showAndWait().get());
//		             
						dialog = new TextInputDialog();
						dialog.setTitle("سعر شراء الوحدة");
						dialog.setHeaderText("الرجاء ادخال سعر شراء الوحدة");
						buyPrice = Double.parseDouble(dialog.showAndWait().get());
//		             
						dialog = new TextInputDialog();
						dialog.setTitle("سعر بيع الوحدة");
						dialog.setHeaderText("الرجاء ادخال سعر بيع الوحدة");
						sellPrice = Double.parseDouble(dialog.showAndWait().get());

						// show Calender dialog
						Dialog<LocalDate> dateDialog = new Dialog<>();
						dateDialog.setTitle("تاريخ انتهاء السلعة");
						dateDialog.setHeaderText("الرجاء ادخال تاريخ انتهاء السلعة");

						Stage stage = (Stage) dateDialog.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("/application/images/icon.png"));

						DatePicker datePicker = new DatePicker();
						dateDialog.getDialogPane().setContent(datePicker);

						ButtonType buttonTypeOk = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

						expDate = dateDialog.showAndWait().get();

						
						
					}
				}

				/*
				 * // اذا الباركود هاد موجود بالداتا بيس
				 * معناته معلوماته معنا بس بنطلب الكمية //
				 * وتاريخ الانتهاء بدون اسعار if (true) ; // اذا لأ
				 * بنشوف الاسم اذا موجود بالداتا بيس ، بنعطي
				 * الباركود تبع هاد الاسم // للمتغير باركود
				 * وبناخد كمية وتاريخ انتهاء else if (true) ; //
				 * بالحالتين هدول السلعة موجودة بالمخزن ف ما
				 * بنضيفها بس بنعدل ع كميتها وتاريخها
				 * 
				 * // اذا الباركود والاسم جداد ، بناخد كل
				 * المعلومات وبنضيفها للمخزن كسلعة جديدة else ;
				 * 
				 * 
				 */

				// show ItemName Dialog
			/*	Dialog<String> nameDialog = new Dialog<>();
				nameDialog.setTitle("اسم السلعة");
				nameDialog.setHeaderText("الرجاء ادخال اسم السلعة");

				Stage stage2 = (Stage) nameDialog.getDialogPane().getScene().getWindow();
				stage2.getIcons().add(new Image("/application/images/icon.png"));

				TextField nameTF = new TextField("");
				TextFields.bindAutoCompletion(nameTF, storedItems);
				nameDialog.getDialogPane().setContent(nameTF);

				ButtonType buttonTypeOk2 = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

				itemName = nameDialog.showAndWait().get();
// to here
				
				
				TextInputDialog dialog = new TextInputDialog();

//             
				dialog = new TextInputDialog();
				dialog.setTitle("الكمية");
				dialog.setHeaderText("الرجاء ادخل الكمية");
				quant = Integer.parseInt(dialog.showAndWait().get());
//             
				dialog = new TextInputDialog();
				dialog.setTitle("سعر شراء الوحدة");
				dialog.setHeaderText("الرجاء ادخال سعر شراء الوحدة");
				buyPrice = Double.parseDouble(dialog.showAndWait().get());
//             
				dialog = new TextInputDialog();
				dialog.setTitle("سعر بيع الوحدة");
				dialog.setHeaderText("الرجاء ادخال سعر بيع الوحدة");
				sellPrice = Double.parseDouble(dialog.showAndWait().get());

				// show Calender dialog
				Dialog<LocalDate> dateDialog = new Dialog<>();
				dateDialog.setTitle("تاريخ انتهاء السلعة");
				dateDialog.setHeaderText("الرجاء ادخال تاريخ انتهاء السلعة");

				Stage stage = (Stage) dateDialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/application/images/icon.png"));

				DatePicker datePicker = new DatePicker();
				dateDialog.getDialogPane().setContent(datePicker);

				ButtonType buttonTypeOk = new ButtonType("تأكيد", ButtonData.OK_DONE);
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

				expDate = dateDialog.showAndWait().get();
*/
				//to here
//
//              System.out.println(barcode+"    "+itemName+"    "+quant+"    "+buyPrice+"    "+sellPrice+"  "+expDate);

				data.add(new Item(barcode, itemName, quant, buyPrice, sellPrice, expDate, buyPrice * quant,
						sellPrice * quant));
				table.refresh();

				barcodeReader.clear();

			}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> barcodeReader.requestFocus());
		billDate.setValue(LocalDate.now());

		table.setItems(data);

		barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
		itemColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quant"));
		buyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
		sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
		TotalBuyPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalBP"));
		TotalSellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalSP"));

	}

}