package MainPackage;

import java.util.ArrayList;
import MainPackage.Conso;

//Dans les commentaire attention de de pas confondre Conso qui correspond à un objet de la classe Conso et consommation qui est la consommation (en W/Wh).

public class Component {
	//-------------------------------------------------------------------------------------------------------
	private ArrayList<Conso> consos;
	private double courant, total;
	
	//-------------------------------------------------------------------------------------------------------
	//constructuers 
	
	public Component() {
		consos = new ArrayList<Conso>();
	}
	//-------------------------------------------------------------------------------------------------------
	//méthodes
	public void addConso(long timestamp, double valeur) {
		consos.add(new Conso(timestamp,valeur));
		this.courant=valeur;
		this.total+=valeur;
	}
	//retourne la dernière Conso enregistré
	public Conso getLast() {
		return consos.get(consos.size()-1);
	}
	
	//retourne la moyenne des consommations de ce composant
	public double getAvg() {
		return (consos.size()!=0?this.getTotal()/consos.size():-1);
	}
	
	//retourne le dernière consommation enregisté si elle existe et qu'elle est plus récente que 15 secondes, sinon 0
	public double getCourant() {
		if (consos.get(consos.size()-1).getTimeStamp() + 15 < System.currentTimeMillis()/1000)
			return 0;
		else
			return this.courant;
	}
	
	//retourne le total des consommations
	public double getTotal() {
		return this.total;
	}

	//toString convertit Composant en String (pour meilleur lisibilité des erreurs)
	public String toString() {
		String res="";
		for (Conso c : this.consos) {
			res+=c+"\n";
		}
		return res;
	}
	
	//retourne l'arrayList de toute les Conso du composants
	public ArrayList<Conso> getConsoArray() {
		return consos;
	}
	
	//retourne le nombre de Conso, où également le nombre de mesure prise.
	public int getNbConso() {
		return consos.size();
	}
}
