package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Departamento;
import model.service.DepartamentoService;

public class DepartamentoFormController implements Initializable {
	
	private Departamento entidade;
	
	private DepartamentoService service;
	
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
	public void onBtSalvarAction(ActionEvent evento){
		if(entidade == null) {
			throw new IllegalStateException("Entidade nula!");
		}
		if(service == null) {
			throw new IllegalStateException("Service nulo!");
		}
		
		
		try {
			entidade = getDadosFormulario();
			service.salvarOuAtualizar(entidade);
			
			// para fechar a janela após salvar
			Utils.stageAtual(evento).close();
			
		} catch (DBException e) {
			Alerts.mostrarAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Departamento getDadosFormulario() {
		Departamento dep = new Departamento();
		dep.setId(Utils.tentarConverterParaInteiro(txtId.getText()));
		dep.setNome(txtNome.getText());
		
		return dep;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent evento){
		Utils.stageAtual(evento).close();
	}
	
	//injeção de dependência
	public void setEntidade(Departamento entidade) {
		this.entidade = entidade;
	}
	
	//injeção de dependência
		public void setService(DepartamentoService service) {
			this.service = service;
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
