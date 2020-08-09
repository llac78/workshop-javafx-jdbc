package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("Sobre");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

			
		
		
	}

}
