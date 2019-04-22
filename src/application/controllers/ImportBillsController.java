package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.BoughtItem;
import application.Item;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ImportBillsController implements Initializable {

	ObservableList<BoughtItem> data = FXCollections.observableArrayList();

	
    @FXML
    private TableView<BoughtItem> table;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField phoneTF;

    @FXML
    private TextField dateTF;

    @FXML
    private Button viewBillButton;

    @FXML
    void buttonHandler(ActionEvent event) throws SQLException, JRException {
    	
    	
    	
    	
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "imad", "11");

		BoughtItem m=table.getSelectionModel().getSelectedItem();
    	
    	JasperDesign jd=JRXmlLoader.load("JasperReport.jrxml");
    	String q="select * from buying_bill,bought where buying_bill.bar=bought.barcode and supplier_name='"+m.getSupplierName()+"' and buying_date=TO_DATE('"+m.getBuyingDate()+"','YYYY-MM-DD')";
    	JRDesignQuery newQuery=new JRDesignQuery();
    	newQuery.setText(q);
    	jd.setQuery(newQuery);
    	
    	JasperReport jr=JasperCompileManager.compileReport(jd);
    	JasperPrint jp=JasperFillManager.fillReport(jr, null,connection);
    	JasperViewer.viewReport(jp,false);
    	
//        SwingNode swingNode = new SwingNode();
//        swingNode.setContent(new JRViewer(jp));
//
//        AnchorPane anchorPane = new AnchorPane();
//
//        AnchorPane.setTopAnchor(swingNode,0.0);
//        AnchorPane.setBottomAnchor(swingNode,0.0);
//        AnchorPane.setLeftAnchor(swingNode,0.0);
//        AnchorPane.setRightAnchor(swingNode,0.0);
//
//        anchorPane.getChildren().add(swingNode);
//        Scene scene = new Scene(anchorPane);
//        Stage stage = new Stage();
//                stage.setHeight(550);
//                stage.setWidth(600);
//                stage.setAlwaysOnTop(true);
//                stage.setScene(scene);
//                stage.showAndWait();
    	
    	
    	
    	

    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "imad", "11");

			Statement statement=connection.createStatement();
			String q="select distinct supplier_name,Buying_date  from buying_bill ";
			ResultSet rs=statement.executeQuery(q);
			while(rs.next()) {
				data.add(new BoughtItem("","",0,0,0,rs.getDate("Buying_date").toLocalDate(),rs.getString("Supplier_name")));
			}
			
			
			table.setItems(data);
			nameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
			dateColumn.setCellValueFactory(new PropertyValueFactory<>("buyingDate"));
			
			table.getSelectionModel().selectedItemProperty().addListener(
		            (observable, oldValue, newValue) -> {//your code here

		            	nameTF.setText(newValue.getSupplierName());
		            	dateTF.setText(newValue.getBuyingDate()+"");
		            	String s="select * from supplier where supplier_name='"+newValue.getSupplierName()+"'";
		            	try {
							ResultSet rss=statement.executeQuery(s);							
               
//							while(
							rss.next();
							//) {
	                phoneTF.setText(rss.getString("phone_number"));
//          }			
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		            	
		               }
		            
					);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
