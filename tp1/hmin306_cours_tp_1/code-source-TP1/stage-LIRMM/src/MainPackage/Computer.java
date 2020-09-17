package MainPackage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

import MainPackage.Day;

public class Computer {
	//attributs
	private ArrayList<Day> days; 						//Collections contenant tous les jours avec des données
	private Day currentDay;								//le jour actuel
	private Hashtable<String, String> infosToDisplay;	//Hashtable dont la clés est le composants et la valeur le string à afficher dans l'interface graphique
	
	//-------------------------------------------------------------------------------------------------------
	//constructeurs
	
	//Construit une instance de computeur en allant directement cherche tous les Day dans le bon répertoire
	public Computer() throws ParseException, NumberFormatException, IOException {
		infosToDisplay = new Hashtable<String, String>();
		days = new ArrayList<Day>();
		File repertoire = new File(Controller.collectFolderPath+"collect/log");
        String liste[] = repertoire.list();      
        if (liste != null) {         
            for (int i = 0; i < liste.length; i++) {
            	if (parseDate(liste[i])!=null)
            	{
            		Day d = new Day(parseDate(liste[i]));
            		days.add(d);
            	}
            }   
        } else {
            System.err.println("Nom de repertoire invalide");
        }
        this.currentDay=this.getLastDay();
        updateInfosToDisplay();
	}

	//-------------------------------------------------------------------------------------------------------
	//methodes
	//retourne la consommation actuelle de l'ordinateur
	public double getConsoRealTime() {
		return getLastDay().getConsoCourante();
		
	}
	
	//retourne la consommation moyenne aujourd'hui
	public double getConsoAvgDay() {
	
		return getLastDay().getConsoMoyenne();
	}

	//retourne la consommation total aujourd'hui
	public double getConsoTotalDay() {
	
		return getLastDay().getConsoTotal();
	}
	
	//retourne le Day d'aujourd'hui
	public Day getCurrentDay() {
		return this.currentDay;
	}
	
	//actualise computeur : verifie qu'on est toujours le même jour. Si non on va chercher le jour d'aujourd'hui. Dans tous les cas on actualise le jour actuel
	@SuppressWarnings("deprecation")
	public void updateData() throws IOException {
		Date actu = this.currentDay.getDate(), auj = new Date();
		if (actu.getDay()!=auj.getDay()) {
			File repertoire = new File(Controller.collectFolderPath+"collect/log");
			String liste[] = repertoire.list();
			if (liste!=null)
			{
				int i=0;
				while (i<liste.length && (parseDate(liste[i])==null || !(parseDate(liste[i]).getYear()==auj.getYear() && parseDate(liste[i]).getMonth()==auj.getMonth() && parseDate(liste[i]).getDay()==auj.getDay())))
					i++;
				if (i<liste.length)
				{
					days.add(new Day(parseDate(liste[i])));
				}
			}
		}
		
		this.currentDay=this.getLastDay();
		this.currentDay.updateData();
	}
	
	//retourne le jour le plus récent dans l'ArrayList days
	public Day getLastDay() {
		Day res = days.get(0);
		for(Day d : days) {
			if (res.getDate().compareTo(d.getDate())<0) //res apres d
				res = d;
		}	
		return res;
	}
	
	//retourne le deuxième jour le plus récent  dans l'ArrayList days
	public Day getPrecDay() {
		if (this.days.size()<2)
			return null;
		else 
		{
			Day auj=this.currentDay, res = (days.get(0)!=auj?days.get(0):days.get(1));
			for (Day d : days) {
				if (res.getDate().compareTo(d.getDate())<0 && d!=auj)
					res = d;
			}
			return res;
		}
	}
	
	//permet de comparer deux jours entre eux
	@SuppressWarnings("deprecation")
	public boolean compareDay(Day d1,Day d2) { //is d1 later than d2
		boolean res = false;
		if(d1.getDate().getYear()>d2.getDate().getYear()) {
			res = true;
		} else if (d1.getDate().getYear() == d2.getDate().getYear() && d1.getDate().getMonth()>d2.getDate().getMonth()) {
			res = true;
		} else if (d1.getDate().getYear() == d2.getDate().getYear() && d1.getDate().getMonth()==d2.getDate().getMonth() && d1.getDate().getDate()==d2.getDate().getDate()) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}
	
	//retourne une Date formater
	public static Date parseDate(String date) {
	    try {
	        return new SimpleDateFormat("dd_MM_yyyy").parse(date);
	    } catch (ParseException e) {
	        return null;
	    }
	 }
	
	//retourne l'ArrayList de conso du jour actuel
	public ArrayList<Conso> getTotalConsoArray(String keycomp){
		return this.currentDay.getConsoArray(keycomp);
	}
	
	//retourne une HashTable dont la clé est le nom des composants et la valeurs leurs ArrayList
	//retourne l'HashTable des composants du jour actuelle ainsi que de leurs consommations
	public Hashtable<String, ArrayList<Conso>> getTotalAllConsoArray(){	
		Hashtable<String, ArrayList<Conso>> res = new Hashtable<String, ArrayList<Conso>>();
		Set<String> composName = getLastDay().getComponentSet();
		for(String key: composName)
			res.put(key,getTotalConsoArray(key));
		
		return res;
	}
	 
	//actualise les infos que l'on peut afficher à l'écran en fonction des composants présents
	public void updateInfosToDisplay() {
		Set<String> composName = getLastDay().getComponentSet();
		for(String key: composName){
			if(key.equals("total")) {
				infosToDisplay.put("Consommation en temps reel",String.valueOf(format(getLastDay().getConsoCourantCompo(key)))+" W");
				infosToDisplay.put("Consommation moyenne de la journee",String.valueOf(format(getLastDay().getConsoMoyenneComp(key)))+" W");
				infosToDisplay.put("Consommation totale de la journee",String.valueOf(format(getLastDay().getConsoTotalComp(key)/360))+" Wh");		
			} else {
				infosToDisplay.put("Consommation du "+ key +" en temps reel",String.valueOf(format(getLastDay().getConsoCourantCompo(key)))+" W");
				infosToDisplay.put("Consommation moyenne du " + key + " de la journee",String.valueOf(format(getLastDay().getConsoMoyenneComp(key)))+" W");
				infosToDisplay.put("Consommation totale du "+ key + " de la journee",String.valueOf(format(getLastDay().getConsoTotalComp(key)/360))+" Wh");
			}
        }
		if(getPrecDay() != null) {
		composName = getPrecDay().getComponentSet();
			for(String key: composName){
				if(key.equals("total")) {
					infosToDisplay.put("Consommation moyenne de la veille", String.valueOf(format(getPrecDay().getConsoMoyenneComp(key)))+" W");
					infosToDisplay.put("Consommation totale de la veille",String.valueOf(format(getPrecDay().getConsoTotalComp(key)/360))+" Wh");
				} else {
					infosToDisplay.put("Consommation moyenne du " + key + " de la veille", String.valueOf(format(getPrecDay().getConsoMoyenneComp(key)))+" W");
					infosToDisplay.put("Consommation totale du "+ key + " de la veille",String.valueOf(format(getPrecDay().getConsoTotalComp(key)/360))+" Wh");
				}
	        }
		}
		if(!(getLastDay().getCompontents().containsKey("total"))) {
			
			infosToDisplay.put("Consommation en temps reel",String.valueOf(format(getLastDay().getConsoCourante()))+" W");
			infosToDisplay.put("Consommation moyenne de la journee",String.valueOf(format(getLastDay().getConsoMoyenne()))+" W");
			infosToDisplay.put("Consommation totale de la journee",String.valueOf(format(getLastDay().getConsoTotal()))+" Wh");
			if(getPrecDay() != null) {
				infosToDisplay.put("Consommation moyenne de la veille",String.valueOf(format(getPrecDay().getConsoMoyenne()))+" W");
				infosToDisplay.put("Consommation totale de la veille",String.valueOf(format(getPrecDay().getConsoTotal()))+" Wh");
			}
		}
		
		if(getLastDay().getConsoTotal()/1000.0 * 15 < 100)
			infosToDisplay.put("Prix de la journee a 15c le kilowattheure",String.valueOf((format(getLastDay().getConsoTotal()/1000.0 * 15)))+" cent");
		else
			infosToDisplay.put("Prix de la journee a 15c le kilowattheure",String.valueOf((format(getLastDay().getConsoTotal()/1000.0 * 0.15)))+" euro");
		
		if(getPrecDay() != null) {
			if(getPrecDay().getConsoTotal()/1000.0 * 15 < 100) {
				infosToDisplay.put("Prix de la veille a 15c le kilowattheure",String.valueOf((format(getPrecDay().getConsoTotal()/1000.0 * 15)))+" cent");
			}
			else {
				infosToDisplay.put("Prix de la veille a 15c le kilowattheure",String.valueOf((format(getPrecDay().getConsoTotal()/1000.0 * 0.15)))+" euro");
			}
		}
		
		//pour 
		
		
	}
	
	//retourne infosToDisplay
	public Hashtable<String, String> getInfosToDisplay() {
		return infosToDisplay;
	}
	
	//le double n'ont qu'un seul chiffre après la virgule
	public String format(double d) {
		return String.valueOf(Math.round(d * 100) / 100.0);
	}
}