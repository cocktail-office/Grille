package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.grille.serveur.Session;

import com.webobjects.appserver.WOContext;

@SuppressWarnings("serial")
public abstract class GrilleComponent extends CktlAjaxWOComponent {

	public GrilleComponent(WOContext context) {
		super(context);
	}
	
	
	public Session getSession() {
		return (Session)session();
	}
	
	
	
	
	public String erreurId() {
		return getComponentId() + "_erreur";
	}
	
	public String messageId() {
		return getComponentId() + "_message";
	}

}
