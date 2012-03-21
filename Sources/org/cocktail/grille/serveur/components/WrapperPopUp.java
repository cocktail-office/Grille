package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlwebapp.server.components.CktlWebPage;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;

import er.extensions.appserver.ERXResponseRewriter;

public class WrapperPopUp extends CktlWebPage {
	private String titre;

	public WrapperPopUp(WOContext context) {
		super(context);
		
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {		
		super.appendToResponse(response, context);
		ERXResponseRewriter.addScriptResourceInHead(response, context, "app", "scripts/GrilleModules.js");
	}
	
}