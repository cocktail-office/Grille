package org.cocktail.grille.serveur.components;

import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteHoraireCode;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.foundation.NSArray;

@SuppressWarnings("serial")
public class RepEquivalentTD extends GrilleComponent {
    
	
	private WODisplayGroup dgMaquetteHoraireCodes = null;
	
	public RepEquivalentTD(WOContext context) {
        super(context);
    }
    
    public WODisplayGroup dgMaquetteHoraireCodes() {
    	if(dgMaquetteHoraireCodes==null) {
    		dgMaquetteHoraireCodes = new WODisplayGroup();
    	}
    	fetchEquivalentTD();
    	return dgMaquetteHoraireCodes;
    }
    
    private void fetchEquivalentTD() {
    	@SuppressWarnings("unchecked")
		NSArray<EOScolMaquetteHoraireCode> data = EOScolMaquetteHoraireCode.fetchAll(edc());
    	dgMaquetteHoraireCodes.setObjectArray(data);
    }
    
}