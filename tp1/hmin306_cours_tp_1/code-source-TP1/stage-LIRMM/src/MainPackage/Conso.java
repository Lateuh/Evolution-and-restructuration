package MainPackage;

import java.util.Date;

public class Conso {
	
	//attributs
	private long timeStamp;	//Unix timeStamp de la mesure
	private double conso; //consommation enregistré
	
	//-------------------------------------------------------------------------------------------------------
	//constructeurs
	//par defaut
	public Conso() {
		timeStamp = 0;
		conso = 0;
	}
	
	//parametré
	public Conso(long timeStamp, double conso) {
		this.timeStamp = timeStamp;
		this.conso = conso;
	}

	//-------------------------------------------------------------------------------------------------------
	//methodes 
	
	//getter et setter 
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public double getConso() {
		return conso;
	}

	public void setConso(double conso) {
		this.conso = conso;
	}
	
	//autres methodes
	//retourne la Date au format String a partir du timeStamp
	public String getDate() {
		Date date = new java.util.Date((long)timeStamp*1000);
		return date.toString();
	}
	
	//toString de Conso
	public String toString() {
		return this.getDate()+" "+this.conso;
	}
}
