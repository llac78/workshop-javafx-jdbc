package gui.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
	
	public static Double tentarConverterParaDouble(String valor) {
		try {
			return Double.parseDouble(valor);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	
	public static <T> void formatarColunaDate(TableColumn<T, Date> colunaTabela, String format) {
		colunaTabela.setCellFactory(coluna -> {
			TableCell<T, Date> cell = new TableCell<T, Date>(){
				
				private SimpleDateFormat sdf = new SimpleDateFormat(format);
				
				@Override
				protected void updateItem(Date item, boolean vazio) {
					super.updateItem(item, vazio);
					if(vazio) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}
	
	public static <T> void formatarColunaDouble(TableColumn<T, Double> colunaTabela, int decimal) {
		colunaTabela.setCellFactory(coluna -> {
			TableCell<T, Double> cell = new TableCell<T, Double>(){
				
				@Override
				protected void updateItem(Double item, boolean vazio) {
					super.updateItem(item, vazio);
					if(vazio) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimal + "f", item));
					}
				}
			};
			return cell;
		});
	}
	
	public static void formatDatePicker(DatePicker datePicker, String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			{
				datePicker.setPromptText(format.toLowerCase());
			}

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}
}
