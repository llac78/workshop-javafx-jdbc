package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage stageAtual(ActionEvent evento) {
		return (Stage) ( (Node)evento.getSource() ).getScene().getWindow();
	}
	
	public static Integer tentarConverterParaInteiro(String valor) {
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
