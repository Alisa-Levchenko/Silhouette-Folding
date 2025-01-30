package Polygons;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class Graph<T> {
	
	protected List<T> _vertices = new ArrayList<T>();
	protected HashMap<T, List<T>> _edges = new HashMap<T, List<T>>();
	protected boolean _is_directed;
	
	protected HashMap<T, Boolean> _mask = new HashMap<T, Boolean>();
	
	Graph(){}
	
	Graph(HashMap<T, List<T>> e){
		_edges = e;
	}
	
	void add_vertex(T v) {
		if(!_edges.containsKey(v)) {
			_vertices.add(v);
			_edges.put(v, new LinkedList<T>());
		}
	}
	
	void add_edge(T start, T end) {
		List<T> _new_st = _edges.get(start);
		_new_st.add(end);
		_edges.replace(start, _new_st);
		
		if (!_is_directed) {
			List<T> _new_end = _edges.get(end);
			_new_end.add(start);
			_edges.replace(end, _new_end);
		}
	}
	
	Graph<T> depth_search(T v) {
		_mask.clear();
		Graph<T> G = new Graph<T>();
		depth_search_recursion(v, _mask, G);
		return G;
	} 
	
	void depth_search_recursion(T v, HashMap<T, Boolean> m, Graph<T> G) {
		m.put(v, true);
		G.add_vertex(v);
		List<T> _neigh = _edges.get(v);
		for (T w : _neigh) {
			if (!m.containsKey(w)) {
				G.add_vertex(w);
				G.add_edge(v, w);
				depth_search_recursion(w, m, G);
			}
		}
	}
	
	ArrayList<T> sequence_of_t() {
		_mask.clear();
		Graph<T> G = new Graph<T>();
		T v = _vertices.getFirst();
		ArrayList<T> L = new ArrayList<T>();
		sequence_of_t_recursion(v, _mask, G, L);
		return L;
	}
	
	void sequence_of_t_recursion(T v, HashMap<T, Boolean> m, Graph<T> G, ArrayList<T> L) {
		m.put(v, true);
		G.add_vertex(v);
		L.add(v);
		List<T> _neigh = _edges.get(v);
		for (T w : _neigh) {
			if (!m.containsKey(w)) {
				G.add_vertex(w);
				G.add_edge(v, w);
				sequence_of_t_recursion(w, m, G, L);
				L.add(v);
			}
		}
	}
}
