/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
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

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.setEditable(false);
    	
    	Integer anno = null;
    	try {
    	anno = boxAnno.getValue();
    	}catch(NumberFormatException e) {
    		txtResult.setText("selezionere un anno dalla tendina");
    		return;
    	}
    	
    	if(anno != null) {
    		model.creaGrafo(anno);
    		txtResult.setText("Grafo creato\n# VERTICI: "+model.vertexSetSize()+"\n# ARCHI: "+model.edgeSetSize());
    		
    		boxRegista.getItems().clear();
    		boxRegista.getItems().addAll(model.getRegistiVertici());
    		
    		return;
    	}else {
    		
    		txtResult.setText("selezionere un anno dalla tendina");
    		return;
    	}
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	
    	
    	
    	Director regista = null;

    	try {
    		regista = boxRegista.getValue();
    	}catch(NullPointerException e){
    		txtResult.setText("Scegliere un regista");
    		return;
    	}
    	
    	List<Adiacenza> mappa = new LinkedList<Adiacenza>( model.getRegistiAdiacenti(regista));
    	
    	if(mappa != null) {
    		
    		for(int i = 0; i < mappa.size(); i++) {
    			
    			Director d1 = model.getRegista(mappa.get(i).getD1());
    			Director d2 = model.getRegista(mappa.get(i).getD2());
    			
    			txtResult.appendText("\n"+d2+" - # attori condivisi: "+ mappa.get(i).getPeso());
    		}
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	
    	double c = -1.0;
    	
    	try {
    		c= Double.parseDouble(txtAttoriCondivisi.getText());
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire solo caratteri numerici!");
    	}

    	List<Director> affini = new LinkedList<Director>( model.camminoMassimo(boxRegista.getValue(), c));
    	
    	txtResult.appendText("Cammino massimo di conoscenze tra registi: \n(come criterio attori comuni)");
    	for(Director a : affini)
    		txtResult.appendText("\n"+a);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	
    	List<Integer> anni = new LinkedList<Integer>();
    	anni.add(2004);
    	anni.add(2005);
    	anni.add(2006);
    	
    	boxAnno.getItems().addAll(anni);
    	
    }
    
}
