package org.cocktail.grille.serveur.components;

import java.math.BigDecimal;

import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grillefwk.serveur.metier.eof.EOReservation;

import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

public class OccupantResaForTbV implements NSKeyValueCoding {
	
	public static final String TO_INDIVIDU_KEY = "individu";
	public static final String TO_RESERVATIONS_KEY = "reservations";
	public static final String NB_HEURES_KEY = "nbHeures";	
	public static final String DEBUT_KEY = "debut";
	public static final String FIN_KEY = "fin";
	

	private EOIndividu individu;
	private NSMutableArray<EOReservation> reservations;
	private NSTimestamp debut;
	private NSTimestamp fin;
	private BigDecimal nbHeures;
	private String hrValide;
	private ApResaForTbV ApResa;
	

	public OccupantResaForTbV(EOIndividu individu,
			NSMutableArray<EOReservation> reservations, BigDecimal nbHeures) {
		super();
		this.individu = individu;
		this.reservations = reservations;
		this.nbHeures = nbHeures;
	}

	public NSMutableArray<EOReservation> getReservations() {
		return reservations;
	}

	public void setReservations(NSMutableArray<EOReservation> reservations) {
		this.reservations = reservations;
	}

	public EOIndividu getIndividu() {
		return individu;
	}

	public void setIndividu(EOIndividu individu) {
		this.individu = individu;
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

	public BigDecimal getNbHeures() {
		return nbHeures;
	}

	public void setNbHeures(BigDecimal nbHeures) {
		this.nbHeures = nbHeures;
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

	public ApResaForTbV getApResa() {
		return ApResa;
	}

	public void setApResa(ApResaForTbV apResa) {
		ApResa = apResa;
	}

}
