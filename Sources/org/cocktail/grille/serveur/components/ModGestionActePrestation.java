package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOActePrestation;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

import er.ajax.AjaxUpdateContainer;

@SuppressWarnings("serial")
public class ModGestionActePrestation extends GrilleComponent {
	
	public static final String B_actePrestation = "actePrestation";
	public static final String B_closeWindowId = "closeWindowId";
	public static final String B_aftercloseaction = "afterCloseAction";
	
    public ModGestionActePrestation(WOContext context) {
        super(context);
    }
    
    public EOEditingContext activeEC() {
    	return actePrestation().editingContext();
    }
    
    public EOActePrestation actePrestation() {
    	return (EOActePrestation) valueForBinding(B_actePrestation);
    }

	public WOActionResults saveChanges() {
		
		try {
			actePrestation().validateObjetMetier();
		}
		catch(Exception e) {
			UtilMessages.creatMessageUtil(session(), UtilMessages.ERROR_MESSAGE, e.getMessage());
			AjaxUpdateContainer.updateContainerWithID(erreurId(), context());
			return null;
		}
		
		try {
			activeEC().saveChanges();
			if(activeEC().parentObjectStore()==edc()) {
				edc().saveChanges();
			}
			if(closeWindowId()!=null) {
				CktlAjaxWindow.close(context(), closeWindowId());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			UtilMessages.creatMessageUtil(session(), UtilMessages.ERROR_MESSAGE, e.getMessage());
			AjaxUpdateContainer.updateContainerWithID(erreurId(), context());
		}
		if (hasBinding(B_aftercloseaction)){
			return (WOActionResults) valueForBinding(B_aftercloseaction);
		}
		return null;
	}
    
	public WOActionResults fermer() {
		activeEC().revert();
		if(closeWindowId()!=null) {
			CktlAjaxWindow.close(context(), closeWindowId());
		}
		if (hasBinding(B_aftercloseaction)){
			return (WOActionResults) valueForBinding(B_aftercloseaction);
		}
		return null;
	}
	
	public String closeWindowId() {
		return (String)valueForBinding(B_closeWindowId);
	}
	
	
	
	public String formGestActPrestId() {
		return getComponentId()+"_formgestactprest";
	}
	
	public String erreurId() {
		return getComponentId()+"_erreur";
	}
	
	public String messageId() {
		return getComponentId()+"_message";
	}




}