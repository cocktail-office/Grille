package org.cocktail.grille.serveur.components;

import com.webobjects.appserver.WOContext;

public class PageGestionImpressions extends GrilleComponent {
	
	private String currentSubcomponentDisplay;
	
    public PageGestionImpressions(WOContext context) {
        super(context);
    }

	public String getCurrentSubcomponentDisplay() {
		return currentSubcomponentDisplay;
	}

	public void setCurrentSubcomponentDisplay(String value) {
		this.currentSubcomponentDisplay = value;
	}
}