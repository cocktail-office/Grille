package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.metier.eof.EOPeriodicite;

import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;

public class PeriodesResaForTbV implements NSKeyValueCoding {
	public static final String PERIODICITE_KEY = "periodicite";
	public static final String OCCUPANT_RESA_KEY = "occupantResa";
	public static final String DEBUT_KEY = "debut";
	public static final String FIN_KEY = "fin";
	

	private EOPeriodicite periodicite;
	private OccupantResaForTbV occupantResa;
	private NSTimestamp debut;
	private NSTimestamp fin;
	
	public PeriodesResaForTbV(EOPeriodicite periodicite,
			OccupantResaForTbV occupantResa) {
		super();
		this.periodicite = periodicite;
		this.occupantResa = occupantResa;
	}

	public EOPeriodicite getPeriodicite() {
		return periodicite;
	}

	public void setPeriodicite(EOPeriodicite periodicite) {
		this.periodicite = periodicite;
	}

	public OccupantResaForTbV getOccupantResa() {
		return occupantResa;
	}

	public void setOccupantResa(OccupantResaForTbV occupantResa) {
		this.occupantResa = occupantResa;
	}

	public NSTimestamp getDebut() {
		return debut;
	}

	public void setDebut(NSTimestamp debut) {
		this.debut = debut;
	}

	public NSTimestamp getFin() {
		return fin;
	}

	public void setFin(NSTimestamp fin) {
		this.fin = fin;
	}


	public void takeValueForKey(Object arg0, String arg1) {
		NSKeyValueCoding.DefaultImplementation
				.takeValueForKey(this, arg0, arg1);

	}

	public Object valueForKey(String arg0) {
		return NSKeyValueCoding.DefaultImplementation.valueForKey(this, arg0);
	}

}
