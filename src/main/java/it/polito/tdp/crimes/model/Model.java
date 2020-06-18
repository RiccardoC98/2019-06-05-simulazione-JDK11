package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao = new EventsDao();
	Graph<Integer, DefaultWeightedEdge> grafo; // Int rappresenta district_id
	int year;
	
	public List<Integer> listYears() {
		// TODO Auto-generated method stub
		return dao.listYears();
	}
	
	public void creaGrafo(int year) {
		grafo = new SimpleWeightedGraph(DefaultWeightedEdge.class);
		this.year = year;
		
		Map<Integer, LatLng> avgLL = new HashMap<>(); // avg Lat Long
		
		for (int i = 1; i <= 7; i++) {
			grafo.addVertex(i); // 7 IDs come vertici
			LatLng value = dao.getAvgLL(i, year); // associo ad ogni ID il suo centro geografico
			avgLL.put(i, value);
		}
		
		for (Integer i : avgLL.keySet()) {
			for (Integer j : avgLL.keySet()) {
				if (i != j && i < j ) { // una volta sola e evito self-loops
					Double distance = LatLngTool.distance(avgLL.get(i), avgLL.get(j), LengthUnit.KILOMETER);
					Graphs.addEdge(grafo, i, j, distance);
				}
			}
		}
		
		// grafo is now ready
	}
	
	public String getDatiGrafo() {
		return "Grafo creato!\n" +
				"#Vertici: " + grafo.vertexSet().size() +
				"\n#Archi: " + grafo.edgeSet().size() ;
	}
	
	public HashMap<Integer, List<Adiacenza>> getAdiacenti() {
		HashMap<Integer, List<Adiacenza>> result = new HashMap<>();
		
		for (int i = 1; i <= 7; i++ ) {
			
			List<Adiacenza> temp = new ArrayList<>();
			for (int j = 1; j <= 7; j++) {
				if ( i != j ) {
					Double lengthKM = grafo.getEdgeWeight( grafo.getEdge(i, j) );
					temp.add( new Adiacenza ( j, lengthKM ) );
				}
				Collections.sort(temp);
				result.put(i, temp);
			}
		}
		
		return result;
		
	}
	
	public Integer getCentralID( int year ) { 
		return dao.getCentralID(year);
	}
	
	public List<Event> eventsOfDay(int day, int month, int year) {
		return dao.eventsOfDay(day, month, year);
	}
	
	public int simulate( int day, int month,  int N) { // N agenti
		Simulator sim = new Simulator(grafo, this);
		sim.setN(N);
		sim.init(day, month, year);
		sim.run();
		
		return sim.getMalGestiti();
	}
}






