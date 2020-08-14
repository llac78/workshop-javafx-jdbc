package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegridadeException;
import gui.listeners.MudancaDadosListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.service.DepartamentoService;

public class DepartamentoListaController implements Initializable, MudancaDadosListener {
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> colunaId;

	@FXML
	private TableColumn<Departamento, String> colunaNome;
	
	@FXML
	private TableColumn<Departamento, Departamento> colunaEdit;
	
	@FXML
	private TableColumn<Departamento, Departamento> colunaRemover;

	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		Stage parent = Utils.stageAtual(evento);
		Departamento departamento = new Departamento();
		criarDialogForm(departamento, "/gui/DepartamentoForm.fxml", parent);
	}
	
	private ObservableList<Departamento> obsLista;
	
	private DepartamentoService service;
	
	public void setService(DepartamentoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		inicializarNodes();
	}

	private void inicializarNodes() {

		colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service null!");
		}
		List<Departamento> lista = service.listar();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsLista);
		initBotoesEdicao();
		initBotoesRemover();
	}
	
	private void criarDialogForm(Departamento departamento, String caminho, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			//injeção de departamentoendência
			DepartamentoFormController controller = loader.getController();
			controller.setEntidade(departamento); 
			controller.setService(new DepartamentoService());
			controller.adicionarMudancaDadosListener(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Informe os dados do Departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.mostrarAlert("IO Exception", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDadosMudados() {
		updateTableView();
	}
	
	public void initBotoesEdicao() {
		
		colunaEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaEdit.setCellFactory(param -> new TableCell<Departamento, Departamento>(){
			
			private final Button botao = new Button("Editar");
			
			@Override
			protected void updateItem(Departamento departamento, boolean vazio) {
				super.updateItem(departamento, vazio);
				if(departamento == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(evento -> criarDialogForm(departamento, "/gui/DepartamentoForm.fxml", Utils.stageAtual(evento)));
			}
		});
	}
	
	public void initBotoesRemover() {
		
		colunaRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaRemover.setCellFactory(param -> new TableCell<Departamento, Departamento>(){
			
			private final Button botao = new Button("Remover");
			
			@Override
			protected void updateItem(Departamento departamento, boolean vazio) {
				super.updateItem(departamento, vazio);
				if(departamento == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(evento -> removerEntidade(departamento));
			}
		});
	}

	private void removerEntidade(Departamento departamento) {

		Optional<ButtonType> resultado = Alerts.mostrarConfirmacao("Confirmar", "Deseja realmente deletar este registro?");
		
		if(resultado.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service nulo!");
			}
			try {
				service.remover(departamento);
				updateTableView();
			} catch (DBIntegridadeException e) {
				Alerts.mostrarAlert("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}


}
