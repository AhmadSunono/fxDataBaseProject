package application.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    @FXML
    private TableView<?> table;

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
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ahmad", "112233");

    	
    	JasperDesign jd=JRXmlLoader.load("JasperReport.jrxml");
    	String q="select * from buying_bill,bought where buying_bill.bar=bought.barcode";
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
		
	}

}
