/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<java.time.Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	Integer year = boxAnno.getValue();
    	txtResult.clear();
    	
    	if (year != null) {
    		model.creaGrafo(year);
    		txtResult.appendText(model.getDatiGrafo());
    		
    		HashMap<Integer, List<Adiacenza>> adiacenze = model.getAdiacenti();
    		for (Integer i : adiacenze.keySet() ) {
    			txtResult.appendText("District_ID: " + i + "\n");
    			txtResult.appendText( adiacenze.get(i).toString() );
    		}
    		
    		boxMese.getItems().addAll( java.time.Month.values() );
    		
    		for (int i  = 1; i <= 31; i++ ) {
    			boxGiorno.getItems().add(i);
    		}
    	} else {
    		txtResult.appendText("Selezionare un anno!\n");
    		return;
    	}
    	   	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	Integer giorno = boxGiorno.getValue();
    	Month mese = boxMese.getValue();
    	int N;
    	try {
    		N = Integer.parseInt(txtN.getText());
    		if ( N < 1 || N > 10) {
    			txtResult.appendText("Inserire un N valido!\n");
    			return;
    		}
    	} catch (NumberFormatException nfe) {
    		txtResult.appendText("Inserire un N valido!\n");
    		return;
    	}
    	
    	txtResult.clear();
    	
    	try {
    		MonthDay.of(mese, giorno);
    	} catch (DateTimeException ex) {
    		txtResult.appendText("Selezionare una combinazione mese-giorno valida!\n");
    		return;
    	}
    	
    	txtResult.appendText("Eventi mal gestiti: " + model.simulate(giorno, mese.getValue(), N));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxAnno.getItems().clear();
    	boxAnno.getItems().addAll( model.listYears() );
    	
    }
}
