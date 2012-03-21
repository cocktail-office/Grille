package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOEcVerrous;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;

public class EcForTbView extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EcForTbView(WOContext context) {
		super(context);
	}

	public String libEc() {
		if ((hasBinding("value")) && (getValue() != null)) {
			return ((EOScolMaquetteEc) getValue()).codeEtLibelle();
		}
		return null;
	}

	public boolean isVerrouEC() {
		if ((hasBinding("value")) && (getValue() != null)) {
			return EOEcVerrous.IsEcLocked((EOScolMaquetteEc) getValue());
		}
		return false;
	}

	public Object getValue() {
		return valueForBinding("value");
	}

	public WOActionResults goToGestEc() {
		/*
		GestionEC next = (GestionEC) pageWithName(GestionEC.class.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser().haveAdminRight());
		//*/
		VoeuxElp next = (VoeuxElp) pageWithName(VoeuxElp.class.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());
		((Session) session()).setSelectedEc((EOScolMaquetteEc) getValue());
		return next;
	}

}