import java.util.ArrayList;

public class Vertex {
	private String name;
	private ArrayList<Edge> edges;
	private Vertex parent;
	private boolean isVisited;//to prevent cycle
	private int packages;//to keep how many packs this node has

	public Vertex(String name) {
		this.name = name;
		edges = new ArrayList<Edge>();
		parent = null;
		packages = 0;
		isVisited = false;
	}

	public void addEdge(Edge e) {
		edges.add(e);
	}

	public ArrayList<Edge> getEdges() {
		return this.edges;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vertex getParent() {
		return parent;
	}

	public void setParent(Vertex parent) {
		this.parent = parent;
	}

	public int getPackages() {
		return packages;
	}

	public void setPackages(int packages, String operator) {
		if (operator.equals("+"))
			this.packages += packages;
		else
			this.packages = packages;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

}
