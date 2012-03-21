package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlpersonneguiajax.serveur.components.AComponent;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDomaine;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteParcours;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteRepartitionSem;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteSemestre;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteUe;

import com.webobjects.appserver.WOContext;

public class ActiviteLieeA extends AComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4506888472994303391L;

	public ActiviteLieeA(WOContext context) {
		super(context);
	}

	public Object getValue() {
		return valueForBinding("value");
	}

	public void setValue(Object value) {
		setValueForBinding(value, "value");
	}

	public String mueLibelle() {
		return ((EOScolMaquetteUe) getValue()).mueLibelle();
	}

	public String mecLibelle() {
		return ((EOScolMaquetteEc) getValue()).mecLibelleCourt();
	}

	public String ParcFspnLibelle() {
		return ((EOScolMaquetteParcours) getValue())
				.toFwkScolarite_ScolFormationSpecialisation().fspnAbreviation();
	}

	public String mparLibelle() {
		return ((EOScolMaquetteParcours) getValue()).mparAbreviation();
	}

	public String semFspnLibelle() {
		return ((EOScolMaquetteRepartitionSem) ((EOScolMaquetteSemestre) getValue())
				.toFwkScolarite_ScolMaquetteRepartitionSems().lastObject())
				.toFwkScolarite_ScolMaquetteParcours()
				.toFwkScolarite_ScolFormationSpecialisation().fspnAbreviation();
	}

	public String msemOrdre() {		
		return ""+((EOScolMaquetteSemestre) getValue()).msemOrdre();
	}

	public String fspnLibelle() {
		return ((EOScolFormationSpecialisation) getValue()).fspnAbreviation();
	}
	
	public String domaineLibelle(){
		return ((EOScolFormationDomaine)getValue()).fdomLibelle();
	}

	public String diplomeLibelle() {
		
		return ((EOScolFormationDiplome)getValue()).fdipLibelle();
	}

}