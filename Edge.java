
public class Edge {
	private Vertex source;
	private Vertex destination;
	private int weight;
	private int carriedPackage;
	private boolean canGo;//to keep if this edge can go to destination
	private boolean isEffective;//if it is an effective way
	private boolean isChanged;//if it is expanded
	
	public Edge(Vertex source, Vertex destination, int weight) {
		super();
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		this.carriedPackage = 0;
		this.canGo = false;
		this.isEffective = false;
		this.isChanged = false;
	}


	public boolean isChanged() {
		return isChanged;
	}


	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}


	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}

	public Vertex getDestination() {
		return destination;
	}

	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCarriedPackage() {
		return carriedPackage;
	}

	public void setCarriedPackage(int carriedPackage) {
		this.carriedPackage += carriedPackage;
	}

	public boolean isCanGo() {
		return canGo;
	}

	public void setCanGo(boolean canGo) {
		this.canGo = canGo;
	}

	public boolean isEffective() {
		return isEffective;
	}

	public void setEffective(boolean isEffective) {
		this.isEffective = isEffective;
	}	
	
	
	
}
