package silhouetteFolding;

public class Face {
	
	private Half_edge _outer_component;
	private Half_edge _inner_component; // here must be List if I want holes
	
	Face(Face f){
		
		// for cloning
		
		Half_edge beg = new Half_edge(f._inner_component);
		
		Half_edge it = f._inner_component;
		Half_edge it2 = beg;
		
		while (it.get_next() != f._inner_component){
			it = it.get_next();
			it2.set_next(new Vertex(it.get_origin()));
			it2 = it2.get_next();
		} 
		
		it2.set_next(beg);
		
		_outer_component = null;
		_inner_component = beg;
	}
	
	Face(){}
	
	void set_outer_component(Half_edge e) {
		_outer_component = e;
	}
	
	void set_inner_components(Half_edge e) {
		_inner_component = e;
		
	}
	
	Half_edge get_outer_component() {
		return _outer_component;
	}
	
	Half_edge get_inner_component(){
		return _inner_component;
	}
	
	void print_face() {
		Half_edge it = _inner_component;
		
		do {
			it.print_edge(false);
			it = it.get_next();
		} while (it != _inner_component);

	}
}
