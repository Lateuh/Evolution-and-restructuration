package analyse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Analyse {

		public static void printMe(String nomC1, String nomC2) {
			System.out.println("APPEL : "+ nomC1 + " -> " + nomC2);
			try {
	              File myObj = new File("sortie.txt");
	              if (myObj.createNewFile()) {
	                System.out.println("File created: " + myObj.getName());
	              }

	              FileWriter myWriter = new FileWriter("sortie.txt",true);
	              myWriter.write(nomC1+"->"+nomC2+"\n");
	              myWriter.close();
	            } catch (IOException e) {
	              System.out.println("An error occurred.");
	              e.printStackTrace();
	            }
		}
}
