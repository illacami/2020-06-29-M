package it.polito.tdp.imdb.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();

		model.creaGrafo(2005);
		
		System.out.println(model.getRegistiAdiacenti(new Director(498,"Keith", "Adler")));
		
		System.out.println(model.camminoMassimo(new Director(498,"Keith", "Adler"), 13));
	}

}
