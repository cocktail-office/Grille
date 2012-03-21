package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.metier.eof.EOReservation;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;

public class ApResaForTbV  implements NSKeyValueCoding {
	public static final String TO_SCOL_MAQUETTE_AP_KEY = "scolMaquetteAp";
	public static final String TO_RESERVATIONS_KEY = "reservations";
	public static final String DEBUT_KEY = "debut";
	public static final String FIN_KEY = "fin";

	private EOScolMaquetteAp scolMaquetteAp;
	private NSArray<EOReservation> reservations;
	private NSTimestamp debut;
	private NSTimestamp fin;
	private String hrValide;

	public EOScolMaquetteAp getScolMaquetteAp() {
		return scolMaquetteAp;
	}

	public void setScolMaquetteAp(EOScolMaquetteAp scolMaquetteAp) {
		this.scolMaquetteAp = scolMaquetteAp;
	}

	public NSArray<EOReservation> getReservations() {
		return reservations;
	}

	public void setReservations(NSArray<EOReservation> reservations) {
		this.reservations = reservations;
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

	public ApResaForTbV(EOScolMaquetteAp scolMaquetteAp,
			NSArray<EOReservation> reservations, NSTimestamp debut,
			NSTimestamp fin,String hrValide) {
		super();
		this.scolMaquetteAp = scolMaquetteAp;
		this.reservations = reservations;
		this.debut = debut;
		this.fin = fin;
		this.hrValide=hrValide;
	}

	public ApResaForTbV(EOScolMaquetteAp scolMaquetteAp,
			NSArray<EOReservation> reservations) {
		super();
		this.scolMaquetteAp = scolMaquetteAp;
		this.reservations = reservations;
	}

	public void takeValueForKey(Object arg0, String arg1) {
		NSKeyValueCoding.DefaultImplementation
				.takeValueForKey(this, arg0, arg1);

	}

	public Object valueForKey(String arg0) {
		return NSKeyValueCoding.DefaultImplementation.valueForKey(this, arg0);
	}

	public String getHrValide() {
		return hrValide;
	}

	public void setHrValide(String hrValide) {
		this.hrValide = hrValide;
	}

}
