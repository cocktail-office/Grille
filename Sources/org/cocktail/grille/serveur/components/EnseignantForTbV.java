package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.Enseignant;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;

public class EnseignantForTbV extends CktlAjaxWOComponent {
	public EnseignantForTbV(WOContext context) {
		super(context);
	}

	public String libEns() {
		if ((hasBinding("value")) && (getValue() != null)) {
			return ((EOIndividu) getValue()).getNomPrenomAffichage();
		}
		return null;
	}

	public boolean isVerrouEC() {
		if ((hasBinding("value")) && (getValue() != null)) {
			return Enseignant.isServiceValidForEnsAndYear(session()
					.defaultEditingContext(), (EOIndividu) getValue(),
					((Session) session()).selectedYear());
		}
		return false;
	}

	public Object getValue() {
		return valueForBinding("value");
	}

	public WOActionResults goToVoeuxEns() {
		VoeuxEnseignant next = (VoeuxEnseignant) pageWithName(VoeuxEnseignant.class
				.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());
		((Session) session()).setIndividuForEnseigant((EOIndividu) getValue());
		return next;
	}

	public boolean canChangeEnseignant() {		
		return ((Session) session()).getGUser().canChangeEnseignant(
				((Session) session()).selectedYear());
	}
}