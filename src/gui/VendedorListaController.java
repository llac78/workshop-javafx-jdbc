package gui;

import java.net.URL;
import java.util.Date;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.service.VendedorService;

public class VendedorListaController implements Initializable, MudancaDadosListener {
	
	@FXML
	private TableView<Vendedor> tableViewVendedor;
	
	@FXML
	private TableColumn<Vendedor, Integer> colunaId;

	@FXML
	private TableColumn<Vendedor, String> colunaNome;
	
	@FXML
	private TableColumn<Vendedor, String> colunaEmail;

	@FXML
	private TableColumn<Vendedor, Date> colunaDataNascimento;

	@FXML
	private TableColumn<Vendedor, Double> colunaSalario;

	
	@FXML
	private TableColumn<Vendedor, Vendedor> colunaEdit;
	
	@FXML
	private TableColumn<Vendedor, Vendedor> colunaRemover;

	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		Stage parent = Utils.stageAtual(evento);
		Vendedor vendedor = new Vendedor();
		criarDialogForm(vendedor, "/gui/VendedorForm.fxml", parent);
	}
	
	private ObservableList<Vendedor> obsLista;
	
	private VendedorService service;
	
	public void setService(VendedorService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
	}

	private void inicializarNodes() {

		colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
		Utils.formatarColunaDate(colunaDataNascimento, "dd/MM/yyyy");
		colunaSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
		Utils.formatarColunaDouble(colunaSalario, 2);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service null!");
		}
		List<Vendedor> lista = service.listar();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewVendedor.setItems(obsLista);
		initBotoesEdicao();
		initBotoesRemover();
	}
	
	private void criarDialogForm(Vendedor vendedor, String caminho, Stage parentStage) {
		
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
//			Pane pane = loader.load();
//			
//			//injeção de vendedorendência
//			VendedorFormController controller = loader.getController();
//			controller.setEntidade(vendedor); 
//			controller.setService(new VendedorService());
//			controller.adicionarMudancaDadosListener(this);
//			controller.updateDadosFormulario();
//			
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Informe os dados do Vendedor");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//			
//		} catch (IOException e) {
//			Alerts.mostrarAlert("IO Exception", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDadosMudados() {
		updateTableView();
	}
	
	public void initBotoesEdicao() {
		
		colunaEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaEdit.setCellFactory(param -> new TableCell<Vendedor, Vendedor>(){
			
			private final Button botao = new Button("Editar");
			
			@Override
			protected void updateItem(Vendedor vendedor, boolean vazio) {
				super.updateItem(vendedor, vazio);
				if(vendedor == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(evento -> criarDialogForm(vendedor, "/gui/VendedorForm.fxml", Utils.stageAtual(evento)));
			}
		});
	}
	
	public void initBotoesRemover() {
		
		colunaRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaRemover.setCellFactory(param -> new TableCell<Vendedor, Vendedor>(){
			
			private final Button botao = new Button("Remover");
			
			@Override
			protected void updateItem(Vendedor vendedor, boolean vazio) {
				super.updateItem(vendedor, vazio);
				if(vendedor == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(evento -> removerEntidade(vendedor));
			}
		});
	}

	private void removerEntidade(Vendedor vendedor) {

		Optional<ButtonType> resultado = Alerts.mostrarConfirmacao("Confirmar", "Deseja realmente deletar este registro?");
		
		if(resultado.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service nulo!");
			}
			try {
				service.remover(vendedor);
				updateTableView();
			} catch (DBIntegridadeException e) {
				Alerts.mostrarAlert("Erro ao remover", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}


}
