package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOEcVerrous;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;

public class ActiviteForTbView extends CktlAjaxWOComponent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActiviteForTbView(WOContext context) {
        super(context);
    }
	
	public EOActivite getValue() {
		return (EOActivite) valueForBinding("value");
	}

	public String libActivite() {
		if ((hasBinding("value")) && (getValue() != null)) {
			return ((EOActivite) getValue()).getLibelle();
		}
		return null;
	}

	public WOActionResults goToGestActivite() {
		/*
		GestionActivite next = (GestionActivite) pageWithName(GestionActivite.class
				.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser().haveAdminRight());
		next.searchDico().setObjectForKey(getValue().commentaire(), "libelle");
		next.searchDico().setObjectForKey(getValue().toTypeActivite(), "typeActivite");
		next.searchActivite();
		//*/
		PageGestVoeuxActiv next = (PageGestVoeuxActiv) pageWithName(PageGestVoeuxActiv.class
				.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());
		((Session) session()).setSelectedActiv(getValue());
		return next;
	}

	public boolean isVerrouEC() {
		if ((hasBinding("value")) && (getValue() != null) && (getValue().lien() instanceof EOScolMaquetteEc)) {
			return EOEcVerrous.IsEcLocked(getValue().toFwkScolarite_ScolMaquetteEc());
		}
		return false;
		
	}
}