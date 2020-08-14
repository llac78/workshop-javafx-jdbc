package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.MudancaDadosListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Vendedor;
import model.excpetions.ValidacaoException;
import model.service.VendedorService;

public class VendedorFormController implements Initializable {
	
	private Vendedor entidade;
	
	private VendedorService service;
	
	private List<MudancaDadosListener> mdListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpDataNascimento;

	@FXML
	private TextField txtSalario;

	@FXML
	private Label labelErroNome;
	
	@FXML
	private Label labelErroEmail;
	
	@FXML
	private Label labelErroDataNascimento;
	
	@FXML
	private Label labelErroSalario;
	
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
			notificarListeners();
			
			// para fechar a janela após salvar
			Utils.stageAtual(evento).close();
			
		} catch (DBException e) {
			
			Alerts.mostrarAlert("Erro ao salvar", null, e.getMessage(), AlertType.ERROR);
		
		} catch (ValidacaoException e) {
	
			setMsgErros(e.getErros());
		}
	}
	
	private void notificarListeners() {
		for (MudancaDadosListener mudancaDadosListener : mdListeners) {
			mudancaDadosListener.onDadosMudados();
		}
	}

	private Vendedor getDadosFormulario() {
		Vendedor dep = new Vendedor();

		ValidacaoException exception = new ValidacaoException("Erro de validação");
		
		dep.setId(Utils.tentarConverterParaInteiro(txtId.getText()));

		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErro("nome", "Este campo é obrigatório!");
		}
		dep.setNome(txtNome.getText());
		
		if(exception.getErros().size() > 0) {
			throw exception;
		}
		
		return dep;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent evento){
		Utils.stageAtual(evento).close();
	}
	
	//injeção de dependência
	public void setEntidade(Vendedor entidade) {
		this.entidade = entidade;
	}
	
	//injeção de dependência
	public void setService(VendedorService service) {
		this.service = service;
	}
	
	public void adicionarMudancaDadosListener(MudancaDadosListener listener) {
		mdListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();		
	}
	
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 20);
		Constraints.setTextFieldDouble(txtSalario);
		Constraints.setTextFieldMaxLength(txtEmail, 20);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
	}
	
	public void updateDadosFormulario() {
		if(entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		txtSalario.setText(String.format("%.2f", entidade.getSalario()));
		if(entidade.getDataNascimento() != null) {
			dpDataNascimento.setValue(LocalDate.ofInstant(entidade.getDataNascimento().toInstant(), ZoneId.systemDefault()));
		}
	}
	
	private void setMsgErros(Map<String,String> erros) {
		Set<String> campos = erros.keySet();
	
		if(campos.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}
	}
}
