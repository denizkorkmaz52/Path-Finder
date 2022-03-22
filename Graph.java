import java.util.HashMap;

public class Graph {
	// Bu class'ý ve edge ve vertex classlarýný lab derslerinin slaytlarýndan aldým.
	// Üzerinde kendimce deðiþiklikler yaparak
	// düzenledim ve o þekilde kullandým.
	private HashMap<String, Vertex> vertices;
	private HashMap<String, Edge> edges;

	Graph() {
		this.vertices = new HashMap<>();
		this.edges = new HashMap<>();
	}

	public void addEdge(String source, String destination, int weight) {// A function to add edges to
																		// the graph

		if (edges.get(source + "-" + destination) == null) {
			Vertex source_v, destination_v;

			if (vertices.get(source) == null) {
				source_v = new Vertex(source);
				vertices.put(source, source_v);
			} else
				source_v = vertices.get(source);

			if (vertices.get(destination) == null) {

				destination_v = new Vertex(destination);
				vertices.put(destination, destination_v);
			} else
				destination_v = vertices.get(destination);

			Edge edge = new Edge(source_v, destination_v, weight);
			source_v.addEdge(edge);
			destination_v.addEdge(edge);
			edges.put(source + "-" + destination, edge);
		} else {
			edges.get(source + "-" + destination).setWeight(edges.get(source + "-" + destination).getWeight() + weight);
		}
	}

	public void MaxPackage(String source, String destination) {//A function that takes source and destination
														//and find their values in the graph then send these values to sub functions
		if (vertices.get(source) == null || vertices.get(destination) == null) { //Controls if the graph have these nodes.
			System.out.println("There is no vertex such that" + source + " or " + destination);
		} else {
			int sourcePacks = 0;
			Vertex sourceVert = vertices.get(source);//Find source nodes value in graph
			Vertex destVert = vertices.get(destination);//find destination nodes value on graph
			for (int i = 0; i < sourceVert.getEdges().size(); i++) { //Giving the starting packs to the source
				if (sourceVert.getEdges().get(i).getDestination() != sourceVert)
					sourcePacks += sourceVert.getEdges().get(i).getWeight();
			}
			vertices.get(source).setPackages(sourcePacks, "+");
			for (int i = 0; i < sourceVert.getEdges().size(); i++) {//Making all edges of source node to
				sourceVert.getEdges().get(i).setEffective(true); // use in a function later.
			}
			boolean isFound = false;
			searchVertex(destVert, sourceVert);//find the paths if there any
			for (int i = 0; i < sourceVert.getEdges().size(); i++) {
				if (sourceVert.getEdges().get(i).isCanGo())
					isFound = true;
			}
			if (!isFound) {//Controls if there exist path(s) that goes from source to destination.
				System.out.println("There is no connection between these two vertices");
			} else {
				carryPackages(sourceVert, destVert, sourceVert);//if there exist path this function called to carry packs
				System.out.println("In this form of graph the biggest package count on node " + destination + " is "
						+ vertices.get(destination).getPackages());
				System.out.println("-----------------------------------------------");
				makeEfficient(sourceVert.getName(), destVert.getName(), sourceVert.getName());//After carrying packs then
																							//find the edge that have to be expanded
				// sourceVert.getName());
			}

		}

	}

	public void carryPackages(Vertex source, Vertex destination, Vertex mainSource) {//this function works as recursive
		Vertex next;
		for (int i = 0; i < source.getEdges().size(); i++) {
			int sourcePacks = source.getPackages();
			int carriedPacks = 0;
			source.setVisited(true);
			if (sourcePacks != 0) {
				if (source.getEdges().get(i).isCanGo() && source.getEdges().get(i).getDestination() != source) {//Checks if this edge goes to destination
																												//and prevent cycle
					findBetter(source.getEdges().get(i), mainSource, destination);//Finds the most effective edge to give priority
					if (source.getEdges().get(i).isEffective()) {//checks if the current edge is effective
						if (source.getEdges().get(i).getWeight() < sourcePacks) {//checks if the packs in the node can pass 
							carriedPacks = (source.getEdges().get(i).getWeight()	// this edge if not then reduce the packs
									- source.getEdges().get(i).getCarriedPackage());
						} else
							carriedPacks = sourcePacks;
						next = source.getEdges().get(i).getDestination();//taking the next node
						next.setPackages(carriedPacks, "+");//adding the packs from last step to this node
						next.setVisited(true);//marking as visited
						source.getEdges().get(i).setCarriedPackage(carriedPacks);//adding the packs number that
						if (next == destination) {								//go through on this edge to edge
							next.setVisited(false);

						} else {
							carryPackages(next, destination, mainSource);//call the function again for next node

						}
						source.setPackages(sourcePacks - carriedPacks, "-");

					} else if (!source.getEdges().get(i).isEffective()) {// if the node is not effective
						int index = 0;
						for (int j = 0; j < source.getEdges().size(); j++) {
							if (source.getEdges().get(j).isEffective() && source.getEdges().get(j).getSource() == source
									&& source.getEdges().get(j).isCanGo()) {//find the efective edge's index
								index = j;
								break;
							}
						}
						//Check if the effective edge can be used
						if (source.getEdges().get(index).getCarriedPackage() == source.getEdges().get(index)
								.getWeight()) {//if not

							if (source.getEdges().get(i).getWeight() < sourcePacks) {//then use the current edge to carry packs
								carriedPacks = (source.getEdges().get(i).getWeight()
										- source.getEdges().get(i).getCarriedPackage());
							} else
								carriedPacks = sourcePacks;
							next = source.getEdges().get(i).getDestination();
							next.setPackages(carriedPacks, "+");
							next.setVisited(true);
							source.getEdges().get(i).setCarriedPackage(carriedPacks);
							if (next == destination) {
								next.setVisited(false);

							} else {
								carryPackages(next, destination, mainSource);

							}
							source.setPackages(sourcePacks - carriedPacks, "-");

						} else {//if it is usable
							if ((source.getEdges().get(index).getWeight()
									- source.getEdges().get(index).getCarriedPackage()) <= sourcePacks) {
								carriedPacks = (source.getEdges().get(index).getWeight() //set packs to pass by this edge if it is not fit in edge
										- source.getEdges().get(index).getCarriedPackage());//by subtracting carried packs from edges weight
							} else
								carriedPacks = sourcePacks;
							//same processes to carry packs
							next = source.getEdges().get(index).getDestination();
							next.setPackages(carriedPacks, "+");
							next.setVisited(true);
							source.getEdges().get(index).setCarriedPackage(carriedPacks);
							if (next == destination) {
								next.setVisited(false);

							} else {
								carryPackages(next, destination, mainSource);

							}
							source.setPackages(sourcePacks - carriedPacks, "-");//subtracting carried packs from source nodes packs 
																				// because we carried them
							if (source.getPackages() != 0) {					//check if there is still some packs 
								//if there is carry them with current edge.
								carriedPacks = source.getEdges().get(i).getWeight()
										- source.getEdges().get(i).getCarriedPackage();
								next = source.getEdges().get(i).getDestination();
								next.setPackages(carriedPacks, "+");
								next.setVisited(true);
								source.getEdges().get(i).setCarriedPackage(carriedPacks);
								if (next == destination) {
									next.setVisited(false);

								} else {
									carryPackages(next, destination, mainSource);

								}
								source.setPackages(sourcePacks - carriedPacks, "-");
							}
						}
					}

				}
			}
		}
		source.setVisited(false);//marking source node as not visited because when the function ends
		//this node must be usable to find another path.
	}

	public boolean searchVertex(Vertex search, Vertex source) {//a function to find paths and have same
		Vertex next;											//mentality as carrying packs			
		boolean isFound = false;
		source.setVisited(true);//to prevent cycle
		for (int i = 0; i < source.getEdges().size(); i++) {
			if (source.getEdges().get(i).getDestination() != source) {//if the destination of source node's edge
				next = source.getEdges().get(i).getDestination();    //is not the source node
				if (!next.isVisited()) {//to prevent cycle
					next.setVisited(true);
					if (next == search) {//if the node is the destination
						source.getEdges().get(i).setCanGo(true);//mark edge as it can let you go to destination
						next.setVisited(false);//marking destination as not visited so you can find it again
						isFound = true;
					} else {// if it is not destination then search the destination again starting with next
						isFound = searchVertex(search, next);
						if (isFound) {//if the next node has connection by destination
							source.getEdges().get(i).setCanGo(true);//then mark the edge as is can go destination
						}
						next.setVisited(false);
					}
				}
			}
		}

		return isFound;
	}

	public void makeEfficient(String source, String dest, String mainSource) {// a function to find bottlenecks 
		Vertex sourceVert = vertices.get(source);								// and expand them
		Vertex mainS = vertices.get(mainSource);
		Vertex destV = vertices.get(dest);
		for (int i = 0; i < sourceVert.getEdges().size(); i++) {
			boolean isChanged = false;
			Edge current = sourceVert.getEdges().get(i);//takes an edge from the source
			if (current.isCanGo() && current.getDestination() != sourceVert && current.getCarriedPackage() != 0
					&& !current.isChanged()) {//checks if its is used to carry packs and if it has changed before
				Vertex next = current.getDestination();//takes the destination of the edge
				for (int j = 0; j < next.getEdges().size(); j++) {
					Edge nextEdge = next.getEdges().get(j);//takes the destination nodes one edge
					if (nextEdge.getWeight() > current.getWeight()//checks if the edge is bigger than the current 
																	//edge and if it is used before
							&& (nextEdge.getWeight() != nextEdge.getCarriedPackage()) && nextEdge.isCanGo()
							&& nextEdge.getCarriedPackage() != 0 && nextEdge.getSource() == next && next != destV) {
						//if it provides all of the conditions then it means we can pass the next step
						if (sourceVert != mainS) {
							int possiblePacks = 0;
							for (int k = 0; k < sourceVert.getEdges().size(); k++) {//finds the packs that can be inserted to
																					//the source node
								if (sourceVert != mainS && sourceVert.getEdges().get(k).getDestination() == sourceVert
										&& sourceVert.getEdges().get(k).isCanGo())
									possiblePacks += sourceVert.getEdges().get(k).getCarriedPackage();
							}
							if (possiblePacks > current.getWeight()) {//if these packs count is bigger than the weight of 
																		// the edge then expand the weight of the current edge
								int currentW = current.getWeight();//keep current edges value to use while printing
								if (possiblePacks > nextEdge.getWeight() - nextEdge.getCarriedPackage()) {//comparing possible packs
																						//and the next edges weight to decide current edges new weight
									sourceVert.getEdges().get(i).setWeight(sourceVert.getEdges().get(i).getWeight()
											+ nextEdge.getWeight() - nextEdge.getCarriedPackage());
								} else
									sourceVert.getEdges().get(i).setWeight(possiblePacks);
								System.out.println("Edge " + source + "-" + next.getName() + " is expanded by "
										+ (sourceVert.getEdges().get(i).getWeight() - currentW));
								isChanged = true;
								sourceVert.getEdges().get(i).setChanged(true);
								//the code end here in this conditions because there must be one expanded value in one path
							}
						}
					} else if (nextEdge != current && nextEdge.isCanGo() && nextEdge.getCarriedPackage() != 0
							&& nextEdge.getSource() == next && next != destV) {//if it is not bigger than the current nodes weight then 
																				//do the same process for the next edge in this conditions
						makeEfficient(next.getName(), dest, mainSource);
					} else if (next == destV && current.getCarriedPackage() != 0) {//current edge is lead packs to the destination it is a new condition 
																					//then we need another algorithm like this 
																					
						int possiblePacks = 0;
						int currentW = current.getWeight();
						for (int k = 0; k < sourceVert.getEdges().size(); k++) {
							if (sourceVert.getEdges().get(k).getDestination() == sourceVert
									&& sourceVert.getEdges().get(k).isCanGo()
									&& sourceVert.getEdges().get(k).getCarriedPackage() != 0) {
								possiblePacks += sourceVert.getEdges().get(k).getCarriedPackage();
							}
						}
						if (possiblePacks > current.getWeight()) {
							current.setWeight(possiblePacks);
							System.out.println("Edge " + source + "-" + next.getName() + " is expanded by "
									+ (sourceVert.getEdges().get(i).getWeight() - currentW));
						}
					}
					if (isChanged)
						break;

				}

			}
		}
	}

	public boolean findBetter(Edge edge, Vertex mainSource, Vertex destination) {//to decide which path is more effective than others
		Vertex source = edge.getSource();//taking edges source
		Vertex dest1 = edge.getDestination();//taking edges destination
		if (source != mainSource && dest1 != destination) {
			for (int i = 0; i < source.getEdges().size(); i++) {
				if (source.getEdges().get(i).isCanGo() && source.getEdges().get(i).getDestination() != source) {//taking another edge from the sources edges
					Vertex dest2 = source.getEdges().get(i).getDestination();//and take this edges destination
					if (dest2 != dest1) {
						int in1 = 0, in2 = 0, out1 = 0, out2 = 0;
						for (int j = 0; j < dest1.getEdges().size(); j++) {//find the packs that goes in and out to the first destination 
							if (dest1.getEdges().get(j).getDestination() == dest1
									&& dest1.getEdges().get(j).isCanGo()) {
								in1 += dest1.getEdges().get(j).getWeight();
							} else if (dest1.getEdges().get(j).getDestination() != dest1
									&& dest1.getEdges().get(j).isCanGo()) {
								out1 += dest1.getEdges().get(j).getWeight();
							}
						}
						for (int j = 0; j < dest2.getEdges().size(); j++) {//find the packs that goes in and out to the second destination 
							if (dest2.getEdges().get(j).getDestination() == dest2
									&& dest2.getEdges().get(j).isCanGo()) {
								in2 += dest2.getEdges().get(j).getWeight();
							} else if (dest2.getEdges().get(j).getDestination() != dest2
									&& dest2.getEdges().get(j).isCanGo()) {
								out2 += dest2.getEdges().get(j).getWeight();
							}
						}

						if (dest2 == destination) {//if the second destination equals the main destination
							source.getEdges().get(i).setEffective(true);
						} else if ((out2 - in2) > (out1 - in1)) {//compare the competence of two nodes. if the second node can pass more packs
							for (int j = 0; j < source.getEdges().size(); j++) {
								if (source.getEdges().get(j).getSource() == source
										&& source.getEdges().get(j).getDestination() == dest1) {
									source.getEdges().get(j).setEffective(false);//mark the edge as not effective

								} else if (source.getEdges().get(j).getSource() == source
										&& source.getEdges().get(j).getDestination() == dest2) {
									source.getEdges().get(j).setEffective(true);//mark the edge as effective
								}
							}
							dest1 = dest2;

						} else {
							for (int j = 0; j < source.getEdges().size(); j++) {
								if (source.getEdges().get(j).getSource() == source
										&& source.getEdges().get(j).getDestination() == dest1) {
									source.getEdges().get(j).setEffective(true);

								} else if (source.getEdges().get(j).getSource() == source
										&& source.getEdges().get(j).getDestination() == dest2) {
									source.getEdges().get(j).setEffective(false);
								}
							}
						}
					}
				}

			}
		} else if (dest1 == destination) {//if the first destination equals the main destination
			for (int j = 0; j < source.getEdges().size(); j++) {
				if (source.getEdges().get(j).getSource() == source
						&& source.getEdges().get(j).getDestination() == dest1) {
					source.getEdges().get(j).setEffective(true);
					break;

				}
			}
		}
		return true;
	}

	public void print() {

		System.out.println("Source\tDestination\tWeight");
		for (Edge e : edges.values()) {
			System.out.println(
					"" + e.getSource().getName() + "\t" + e.getDestination().getName() + "\t\t" + e.getWeight() + " ");
		}
	}

	public Iterable<Vertex> vertices() {
		return vertices.values();
	}

	public Iterable<Edge> edges() {
		return edges.values();
	}

	public int size() {
		return vertices.size();
	}
}
