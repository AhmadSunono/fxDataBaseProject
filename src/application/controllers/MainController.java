package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainController implements Initializable {

	public int offTime = 0;

	@FXML
	private Button close;

	@FXML
	private Button sellViewButton;

	@FXML
	private Button dailySalesButton;

	@FXML
	private Button storeInventoryButton;

	@FXML
	private Button importBillButton;

	@FXML
	private Button importBillsButton;

	@FXML
	private Button backupButton;

	@FXML
	private StackPane stack;

	@FXML
	private AnchorPane loginForm;

	@FXML
	private TextField username;

	@FXML
	private Button login;

	@FXML
	private PasswordField password;

	@FXML
	private VBox menu;

	@FXML
	private Button signOutButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		menu.translateXProperty().set(200);

		Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				offTime++;
				if (offTime == 900) {
					Platform.runLater(() -> {
						signOut();
						offTime = 0;
					});

				}
			}
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();

	}

	@FXML
	void buttonHandler(ActionEvent event) throws IOException {

		if (event.getSource().toString().contains("sellViewButton")) {
			stack.getChildren().clear();
			stack.getChildren()
					.add((AnchorPane) FXMLLoader.load(getClass().getResource("/application/fxml/SellView.fxml")));
		} else if (event.getSource().toString().contains("dailySalesButton")) {
			stack.getChildren().clear();
			stack.getChildren()
					.add((AnchorPane) FXMLLoader.load(getClass().getResource("/application/fxml/DailySales.fxml")));
		} else if (event.getSource().toString().contains("storeInventoryButton")) {
			stack.getChildren().clear();
			stack.getChildren()
					.add((AnchorPane) FXMLLoader.load(getClass().getResource("/application/fxml/StoreInventory.fxml")));
		} else if (event.getSource().toString().contains("importBillButton")) {
			stack.getChildren().clear();
			stack.getChildren()
					.add((AnchorPane) FXMLLoader.load(getClass().getResource("/application/fxml/ImportBill.fxml")));
		} else if (event.getSource().toString().contains("importBillsButton")) {

		} else if (event.getSource().toString().contains("backupButton")) {

		} else if (event.getSource().toString().contains("login")) {
//			   if(username.getText().toString().equals("Apache") && password.getText().toString().equals("22023202")) {
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5),
					new KeyValue(menu.translateXProperty(), 0, Interpolator.EASE_IN)));
			stack.getChildren().remove(loginForm);
			timeline.play();
//			   } else {
//				   Alert alert = new Alert(AlertType.WARNING);
//				   alert.setTitle("»Ì«‰«  œŒÊ· Œ«ÿ∆…");
//				   alert.setHeaderText(null);
//				   alert.setContentText("«·—Ã«¡ «· √ﬂœ „‰ «”„ «·„” Œœ„ √Ê ﬂ·„… «·„—Ê—");
//				   Stage stage =(Stage) alert.getDialogPane().getScene().getWindow();
//				   stage.getIcons().add(new Image("/application/images/icon.png"));
//
//				   alert.showAndWait();
//			   }

		} else if (event.getSource().toString().contains("signOutButton")) {
			signOut();
		}

		else if (event.getSource().toString().contains("close")) {
			System.exit(0);
		}
	}

	@FXML
	void onMuseMoved(MouseEvent event) {
		offTime=0;
	}

	public void signOut() {
		this.stack.getChildren().clear();
		this.menu.translateXProperty().set(200);
		this.password.clear();
		this.stack.getChildren().add(loginForm);
	}

	public void closeHandler(javafx.scene.input.MouseEvent event) {
		System.exit(0);
	}

}
