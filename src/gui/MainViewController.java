package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.service.DepartamentoService;
import model.service.VendedorService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	private void onMenuItemVendedorAction() {
		carregarView("/gui/VendedorLista.fxml",
				(VendedorListaController controller) -> {
					controller.setService(new VendedorService());
					controller.updateTableView();
				});
	}
	
	@FXML
	private void onMenuItemDepartamentoAction() {
		carregarView("/gui/DepartamentoLista.fxml",
				(DepartamentoListaController controller) -> {
					controller.setService(new DepartamentoService());
					controller.updateTableView();
				});
	}
	
	@FXML
	private void onMenuItemSobreAction() {
		carregarView("/gui/Sobre.fxml", x -> {});
	}
	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {

			
		
		
	}

	private synchronized <T> void carregarView(String caminho,  Consumer<T> inicializacaoAction) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			inicializacaoAction.accept(controller);
			
		} catch (IOException e) {

			Alerts.mostrarAlert("IO Exception", "Erro ao carregar view", e.getMessage(), AlertType.ERROR);
		}
	}
	

}
