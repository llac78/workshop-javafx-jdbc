package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	private void onMenuItemVendedorAction() {
		System.out.println("vendedor");
	}
	
	@FXML
	private void onMenuItemDepartamentoAction() {
		System.out.println("Departamento");
	}
	
	@FXML
	private void onMenuItemSobreAction() {
		carregarView("/gui/Sobre.fxml");
	}
	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {

			
		
		
	}

	private synchronized void carregarView(String caminho) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			
		} catch (IOException e) {

			Alerts.mostrarAlert("IO Exception", "Erro ao carregar view", e.getMessage(), AlertType.ERROR);
		}
	}
}
