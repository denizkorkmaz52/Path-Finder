import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException {
		FileReader fileReader = new FileReader("graph.txt");
		String line;
		BufferedReader br = new BufferedReader(fileReader);
		Graph graph = new Graph();
		while ((line = br.readLine()) != null) {// reading story file
			String[] split = line.split("	");
			graph.addEdge(split[0], split[1], Integer.parseInt(split[2]));
		}
		
		graph.MaxPackage("HG", "KA");
		
		//graph.print();
        System.out.println("-----------------------------------------------");
		br.close();
	}

}
