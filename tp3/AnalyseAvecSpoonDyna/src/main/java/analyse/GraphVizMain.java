package analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;
import static guru.nidi.graphviz.model.Factory.*;

public class GraphVizMain {

	public static void main(String[] args) {

		
		MutableGraph graphDeDependances = mutGraph("graphe_dépendances").setDirected(true);
		//graphDeDependances.add(mutNode(name))
		
		try {
		      File myObj = new File("sortie.txt");
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        String dataTable[] = data.split("->");
		        //System.out.println(dataTable[0] + "==="+ dataTable[1]);
		        
		        graphDeDependances.add(mutNode(dataTable[0]).addLink(dataTable[1]));
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		
		try {
			Graphviz.fromGraph(graphDeDependances).width(7500)/*.height(5000)*/.render(Format.PNG).toFile(new File("./graphe_dependances.png"));
			Graphviz.fromGraph(graphDeDependances).width(7500)/*.height(5000)*/.render(Format.DOT).toFile(new File("./graphe_dependances.dot"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
