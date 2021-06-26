package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge> grafo;
	
	private Map<Integer,Director> registi;
	private List<Movie> film;
	private Map<Integer, Actor> attori;
	
	private List<Director> vertici;
	private List<Adiacenza> archi;
	
	public Model() {
		dao = new ImdbDAO();
		
//		attori = new HashMap<Integer, Actor>();
//		dao.listAllActors(attori);
		
		registi = new HashMap<Integer, Director>();
		dao.listAllDirectors(registi);
	}
	
	public List<Director> getVertici(int anno) {
		
		vertici = new LinkedList<Director>();
		
		if(anno > 2003 && anno < 2007) {
			vertici = dao.getVertici(anno);
			return vertici;
		}
		return null;
	}
	
	public List<Adiacenza> getArchi(int anno){
		
		archi = new LinkedList<Adiacenza>();
		archi = dao.getArchi(anno);
		return archi;
		
	}
	
	public void creaGrafo(int anno) {
		
		grafo = new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		
		Graphs.addAllVertices(grafo, this.getVertici(anno));
		
		//aggiungo archi
		
		this.getArchi(anno);
		for(Adiacenza a : archi) {
			
			Director d1 = registi.get(a.getD1());
			Director d2 = registi.get(a.getD2());
			
			if(d1 != null && d2 != null)
				Graphs.addEdgeWithVertices(grafo, d1, d2, a.getPeso());
			
		}
		
		System.out.println("creato grafo con\n#Verici: "+grafo.vertexSet().size()+"\n#Archi: "+grafo.edgeSet().size());
		
	}
	
}
