package MainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

public class Controller{
	//attributs
	private Computer computer;
	private Fenetre fenetre;
	public static String collectFolderPath = "../../../"; //avant modif //! pour linux : collectFolderPath = "/etc/consommation/";
	private static String path = collectFolderPath+"collect/data/config.csv";
	
	//-------------------------------------------------------------------------------------------------------
	//constructeurs
	
	//par défauts
	public Controller() throws NumberFormatException, ParseException, IOException{
		computer = new Computer(); 
		PanneauTempsReel.setInfosToDisplay(computer.getInfosToDisplay());
		Graphique.setAllTheData(computer.getTotalAllConsoArray());
		if (computer.getTotalConsoArray("gpu")==null) 
			fenetre = new Fenetre(computer.getTotalConsoArray("total"),null);
		else 
			fenetre = new Fenetre(computer.getTotalConsoArray("cpu"),computer.getTotalConsoArray("gpu"));
	}
	//parametré
	public Controller(boolean linux) throws NumberFormatException, ParseException, IOException{
		if (linux) {
			collectFolderPath="/etc/consommation/";
		}
		else 
		{
			collectFolderPath = "../../";
		}
		computer = new Computer(); 
		PanneauTempsReel.setInfosToDisplay(computer.getInfosToDisplay());
		Graphique.setAllTheData(computer.getTotalAllConsoArray());
		if (computer.getTotalConsoArray("gpu")==null) 
			fenetre = new Fenetre(computer.getTotalConsoArray("total"),null);
		else 
			fenetre = new Fenetre(computer.getTotalConsoArray("cpu"),computer.getTotalConsoArray("gpu"));
	}
	
	//-------------------------------------------------------------------------------------------------------
	//methodes
	
	//tetourne la fenetre complete
	public Fenetre getFenetre() {
		return fenetre;
	}
	
	//retourne Computeur
	public Computer getComputer() {
		return computer;
	}
	
	//actualise toute les informations
	public void updateVisualInfo() throws IOException {
		this.computer.updateData();
		this.computer.updateInfosToDisplay();
		 
		//actualisations
		this.fenetre.getPanneauActuelle().updateUI();
		this.fenetre.getPanneauConsoMoyJournee().updateUI();
		this.fenetre.getPanneauConsoJournee().updateUI();
		this.fenetre.getPanneauConsoJourPrecedent().updateUI();
		
		this.fenetre.updateGraphiques();
		
		this.fenetre.getPanneauAjoutComposant().updateUI();
		this.fenetre.getPanneauSuppression().updateUI();
		this.fenetre.getPanneauListeComposants().updateUI();
	}
	
	//a partir d'un double renvoye un string avec un seul chiffre apres la virgule
	public String format(double d) {
		return String.valueOf(Math.round(d * 100) / 100.0);
	}
	
	//recupere les informations du fichier de config
	public static Hashtable<String, ArrayList<String>> readConfigFile() throws NumberFormatException, IOException {
		Hashtable<String, ArrayList<String>> res = new Hashtable<>();
		BufferedReader file;
		String ligne="";
		
		try
		{
			file = new BufferedReader(new FileReader(path));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture "+path);
			return new Hashtable<>();
		}
		
		int size = 5;
		while ((ligne = file.readLine()) != null) 
		{
			String[] donne = ligne.split(",");
			if (!ligne.equals("")) {
				ArrayList<String> list = new ArrayList<String>();
				for(int i = 1; i < size; i++) {
					list.add(donne[i]);
				}
				res.put(donne[0], list);
			}
		}
		
		file.close();
		
		return res;
	}
	//ecrit un nouveau fichier de config
	public static void writeConfigFile(Hashtable<String, ArrayList<String>> data) throws IOException {
		new File(path).createNewFile();
		PrintWriter writer = new PrintWriter(path);
		
		for (Entry<String,ArrayList<String>> mapentry : data.entrySet()) {
			writer.print(mapentry.getKey());
			for(int i = 0; i < mapentry.getValue().size(); i++) {
				writer.print("," + mapentry.getValue().get(i));
			}
			writer.println("");
		}
		
		writer.close();
	}
	
	//setFolderPath
	public static void setFolderPath(String path) {
		Controller.collectFolderPath=path;
	}
}


