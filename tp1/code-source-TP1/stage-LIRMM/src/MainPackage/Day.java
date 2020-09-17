package MainPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import MainPackage.Component;

public class Day {
	//attributs
	private Hashtable<String, Component> components; //Hashtable dans la clés est l'intitulé du composant et la valeur le Composant en lui-meme
	private Date date; //Date qui correspond au jour. exemple : 26/06/2020
	private double consoCourante=-1, consoTotal=-1, consoMoyenne=-1; //la consommation courante total et moyenne de cette journée
	private long lastTimeStamp=-1; //Unix timestamp de la dernière mesure prise
	//! pour linux !
	private static boolean linux=false; //vrai si linux faut sinon
	
	//-------------------------------------------------------------------------------------------------------
	//constructeurs
	public Day(Date date) throws NumberFormatException, IOException {
		components = new Hashtable<String, Component>();
		this.date = date;
		this.importData();
	}
	
	//-------------------------------------------------------------------------------------------------------
	//methodes 
	
	//getter acces rapide au info sans surcout a favoriser
	//retourne la consommation la plus recente
	public double getConsoCourante() {
		return consoCourante;
	}

	//retourne la consommation total de la journee
	public double getConsoTotal() {
		return consoTotal;
	}

	//retourne la consommation moyenne de la journee
	public double getConsoMoyenne() {
		return consoMoyenne;
	}
	
	//getter identique mais pour chaque composant
	public double getConsoCourantCompo(String keyComp) {
		if (this.components.containsKey(keyComp))
			return this.components.get(keyComp).getCourant();
		else 
			return  0;
	}
	
	public double getConsoTotalComp(String keyComp) {
		if (this.components.containsKey(keyComp))
			return this.components.get(keyComp).getTotal();
		else 
			return  0;
	}
	
	public double getConsoMoyenneComp(String keyComp) {
		int maxNbMesure = 0;
		if (this.components.containsKey(keyComp)) {
			for (Entry<String, Component> mapentry : this.components.entrySet())
				maxNbMesure = (mapentry.getValue().getNbConso() > maxNbMesure ? mapentry.getValue().getNbConso() : maxNbMesure);
		
			return this.components.get(keyComp).getTotal()/maxNbMesure;
		}
		else 
			return  0;
	}
	
	//autres getter et setter
	//retourne components
	public Hashtable<String, Component> getCompontents() {
		return components;
	}

	//actualise components à partir d'une autre Hashtable
	public void setCompontents(Hashtable<String, Component> compontents) {
		this.components = compontents;
	}

	//retourne la date
	public Date getDate() {
		return date;
	}

	//actualise la date
	public void setDate(Date date) {
		this.date = date;
	}
	
	//getter mais calculer cette fois à ne pas utiliser de preference sauf pour vérifier
	//calcul la consommation la plus récente
	private double getLast() {
		if (this.components.containsKey("total"))
			return this.components.get("total").getCourant();
		else 
		{
			double last=0;
			for (Entry<String, Component> mapentry : this.components.entrySet())
				last+=mapentry.getValue().getCourant();
			return last;
		}
	}

	//calcul la consommation moyenne
	private double getAverage() {
		double avg = 0;
		int maxNbMesure = 0;
		if (this.components.containsKey("total"))
			return this.components.get("total").getAvg();
		else 
		{
			for (Entry<String, Component> mapentry : this.components.entrySet())
				maxNbMesure = (mapentry.getValue().getNbConso() > maxNbMesure ? mapentry.getValue().getNbConso() : maxNbMesure);
			
			for (Entry<String, Component> mapentry : this.components.entrySet())
				avg+=mapentry.getValue().getTotal()/maxNbMesure;
		}
		
		return avg;
	}
	//calcul conosmmation total
	public double getTotal() {
		double total = 0;
		if (this.components.containsKey("total"))
			total = this.components.get("total").getTotal();
		else 
		{
			for (Entry<String, Component> mapentry : this.components.entrySet())
				total+=mapentry.getValue().getTotal();
		}
		
		return total/360;
	}
	
	//retourne le temps de la machine éveiller : a implémenter
	public int getUpTime() {
		int time = 0;
		
		return time;
	}

	//ajoute une conso manuellement : inutile normalement 
	public void addConso(Component compo, int timestamp, double conso) {
		return;
	}
	
	//importe les données : a n'utiliser que à la constructions
	@SuppressWarnings("deprecation")
	private void importData() throws NumberFormatException, IOException {
		String day = String.valueOf(date.getDate());
		String month = String.valueOf(date.getMonth()+1);
		String year = String.valueOf(date.getYear()+1900);
		String path = "../../collect/log/" + day + "_" + (month.length() <= 1 ? "0"+month : month) + "_" + year + ".log";
		if (linux) {
			path = "/etc/consommation/collect/log/"+ day + "_" + (month.length() <= 1 ? "0"+month : month) + "_" + year + ".log";
		}
		BufferedReader lecteurAvecBuffer = null;
		String ligne="";
		try
		{
			lecteurAvecBuffer = new BufferedReader(new FileReader(path));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture "+path);
		}
		
		while ((ligne = lecteurAvecBuffer.readLine()) != null) 
		{
			String[] donne = ligne.split(",");
			this.lastTimeStamp = Long.parseLong(donne[0]);
			//java.util.Date time=new java.util.Date((long)timestamp*1000);
			for (int i=1;i<donne.length;i+=2) {
				//System.out.print(donne[i]+" "+donne[i+1]+" W ");
				if (!this.components.containsKey(donne[i]))
					this.components.put(donne[i], new Component());
				this.components.get(donne[i]).addConso(lastTimeStamp, Double.parseDouble(donne[i+1]));	
			}
		}
		lecteurAvecBuffer.close();
		this.updateAttributCalculer();
	}

	
	//actualise les données
	@SuppressWarnings("deprecation")
	public void updateData() throws IOException {
		String day = String.valueOf(date.getDate());
		String month = String.valueOf(date.getMonth()+1);
		String year = String.valueOf(date.getYear()+1900);
		String path = "../../collect/log/" + (day.length() <= 1 ? "0"+day : day) + "_" + (month.length() <= 1 ? "0"+month : month) + "_" + year + ".log";
		if (linux) {
			path = "/etc/consommation/collect/log/"+ day + "_" + (month.length() <= 1 ? "0"+month : month) + "_" + year + ".log";
		}
		BufferedReader lecteurAvecBuffer = null;
		String ligne="";
		try
		{
			lecteurAvecBuffer = new BufferedReader(new FileReader(path));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture "+path);
		}
		
		while ((ligne = lecteurAvecBuffer.readLine()) != null) 
		{
			String[] donne = ligne.split(",");
			long timestamp = Long.parseLong(donne[0]);
			if (this.lastTimeStamp<timestamp)
			{
				this.lastTimeStamp = timestamp;
				for (int i=1;i<donne.length;i+=2) {
					//System.out.print(donne[i]+" "+donne[i+1]+" W ");
					if (!this.components.containsKey(donne[i]))
						this.components.put(donne[i], new Component());
					this.components.get(donne[i]).addConso(timestamp, Double.parseDouble(donne[i+1]));	
				}
			}
		}
		lecteurAvecBuffer.close();
		this.updateAttributCalculer();
	}
	
	//actualise les attributs à la fin de updateData et importData
	private void updateAttributCalculer() {
		this.consoCourante=this.getLast() ;
		this.consoMoyenne=this.getAverage() ;
		this.consoTotal=this.getTotal() ;
	}
	
	//retourne le nombre de composants 
	public int getNbCompents() {
		return this.components.size();
	}
	
	//retourne l'arrrayList de Conso du total
	public ArrayList<Conso> getConsoArray() {
		return components.get("total").getConsoArray();
	}
	
	//retourne l'arrrayList de Conso en fonction de la keyComp
	public ArrayList<Conso> getConsoArray(String keyComp) {
		if (this.components.containsKey(keyComp))
			return this.components.get(keyComp).getConsoArray();
		else 
			return null;

	}
	
	//retourne un Set de tous les composants
	public Set<String> getComponentSet(){
		Set<String> keys = components.keySet();
		return(keys);
	}
	
	//toString
	public String toString() {
		return "last : "+this.getLast()+"\navg : "+this.getAverage()+"\ntotal : "+this.getTotal()+"\nbCompo : "+this.getNbCompents();
	}
	
	//similaire a toString mais plus develloper
	public String getRecord() {
		String res=this.toString()+"\n";
		for (Entry<String, Component> mapentry : this.components.entrySet())
			res+=mapentry.getKey()+" :\n"+mapentry.getValue();
		return res;
	}
	
	//setOS linux 
	//project factory
	public static void setOS(boolean linux) {
		Day.linux = linux;
	}
	
}
