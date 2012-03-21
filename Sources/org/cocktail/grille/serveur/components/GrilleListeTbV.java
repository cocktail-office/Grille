package org.cocktail.grille.serveur.components;

import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

public class GrilleListeTbV extends BaseTbView {
	
    public GrilleListeTbV(WOContext context) {
        super(context);
    }

	@Override
	public NSArray<String> DEFAULT_COLONNES_KEYS() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WOActionResults commitSave() {
		// TODO Auto-generated method stub
		return null;
	}
}