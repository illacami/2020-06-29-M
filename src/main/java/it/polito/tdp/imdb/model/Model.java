package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	List<Director> best = null;
	
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
	
	public List<Adiacenza> getRegistiAdiacenti(Director regista){
		
		List<Adiacenza> mappa = new LinkedList <Adiacenza>();
		
		if(regista!= null && grafo.vertexSet().contains(regista)) {
			for(Adiacenza a : archi) {
				
				Director d1 = registi.get(a.getD1());
				Director d2 = registi.get(a.getD2());
				
				if(d1 != null && d2 != null ) {
					if(d1.equals(regista)){
						mappa.add(a);
					}
				}
			}
			
		}
		//			List<Director> vicini = new LinkedList<Director>(Graphs.neighborListOf(grafo, regista));
		Collections.sort(mappa);
		return mappa;
	}
	
	
	public List<Director> camminoMassimo(Director regista, double c){
		
		List<Director> parziale = new ArrayList<Director>();
		parziale .add(regista);
		
		this.best = new ArrayList<Director>();
		best.add(regista);
		
		cerca(parziale, 1, c);
		
		return best;
	}
	
	
	public void cerca(List<Director> parziale, int livello, double c) {
		
		//caso terminale
		//Numero di attori == c
		//sottraggo a c ogni volta che aggiungo attori in comune
		if(c < 0)
			return;
		if(c >= 0) {
			if(parziale.size() > best.size()) {
				best = parziale;
			}
//			return;
		}
		
		Director ultimo = parziale.get(parziale.size()-1);
		
		List<Director> adiacenti = new ArrayList<Director>(Graphs.neighborListOf(this.grafo, ultimo));
		
		for(Director prova : adiacenti) {
			
			if(!parziale.contains(prova)) {
				
				double restanti = c - grafo.getEdgeWeight(grafo.getEdge(ultimo, prova));
				parziale.add(prova);
				cerca(parziale, livello+1, restanti);
				
				parziale.remove(parziale.size()-1);
				
			}
			
		}
	}
	
	
	public List<Director> getRegistiVertici(){
		
		List<Director> r = new LinkedList<Director>(grafo.vertexSet());
		Collections.sort(r);
		
		return r;
	}
	
	
	public int vertexSetSize() {
		return grafo.vertexSet().size();
	}
	
	public int edgeSetSize() {
		return grafo.edgeSet().size();
	}

	public Director getRegista(int id) {
		if(registi.get(id)!=null)
			return registi.get(id);
		return null;
	}
}
