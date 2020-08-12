package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;

public class DepartamentoFormController implements Initializable {
	
	private Departamento entidade;
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErroNome;
	
	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;
	
	@FXML
	public void onBtSalvarAction(){
		System.out.println("Salvooou");
	}

	@FXML
	public void onBtCancelarAction(){
		System.out.println("Cancelouuu");
	}
	
	public void setEntidade(Departamento entidade) {
		this.entidade = entidade;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();		
	}
	
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 10);
	}
	
	public void updateDadosFormulario() {
		if(entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
	}

}
