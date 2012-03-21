package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EORne;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOActePrestation;
import org.cocktail.grillefwk.serveur.metier.eof.EOPrestationEnseignant;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.ajax.AjaxUpdateContainer;

@SuppressWarnings("serial")
public class ModGestionPrestation extends GrilleComponent {
	
	public static final EOSortOrdering SORT_RNE_ASC = new EOSortOrdering(EORne.LL_RNE_KEY,EOSortOrdering.CompareCaseInsensitiveAscending);
	
	public static final String B_prestationEnseignant = "prestationEnseignant";
	public static final String B_closeWindowId = "closeWindowId";
	
	public EOActePrestation itemActePrestation;
	
	private String rneInput;
	public EORne itemRne;
	
    public ModGestionPrestation(WOContext context) {
        super(context);
    }
    
    
    public NSArray<EOActePrestation> listActesPrestations() {
    	return EOActePrestation.fetchActePrestations(	activeEC(), 
    													EOQualifier.qualifierWithQualifierFormat(EOActePrestation.DATE_ANNULATION_KEY+" = nil", null), 
    													new NSArray<EOSortOrdering>(EOActePrestation.SORT_LIB_LONG_ASC)	);
    }


    public NSArray<EORne> getFilteredRne() {
    	String fieldValue = "*"+rneInput+"*";
    	NSMutableArray<EOQualifier> array = new NSMutableArray<EOQualifier>();
    	array.add( 
    			new EOKeyValueQualifier(EORne.LL_RNE_KEY, EOQualifier.QualifierOperatorCaseInsensitiveLike, fieldValue)
    	);
    	array.add( 
    			new EOKeyValueQualifier(EORne.LC_RNE_KEY, EOQualifier.QualifierOperatorCaseInsensitiveLike, fieldValue)
    	);
    	return EORne.fetchAll(activeEC(), new EOOrQualifier(array), new NSArray<EOSortOrdering>(SORT_RNE_ASC));
    }
    
	public String getRneInput() {
		if(rneInput==null || rneInput.trim().equals("")) {
			if(prestationEnseignant().toFwkpers_Rne() !=null) {
				return prestationEnseignant().toFwkpers_Rne().llRne();
			}
			else {
				return null;
			}
		}
		else {
			return rneInput;
		}
	}
	
	public void setRneInput(String value) {
		this.rneInput = value;
	}
    
	public EOPrestationEnseignant prestationEnseignant() {
		return (EOPrestationEnseignant)valueForBinding(B_prestationEnseignant);
	}
	
	public void setPrestationEnseignant(EOPrestationEnseignant presta) {
		setValueForBinding(presta, B_prestationEnseignant);
	}
	
	public EOEditingContext activeEC() {
		return prestationEnseignant().editingContext();
	}

	public WOActionResults saveChanges() {
		
		try {
			prestationEnseignant().validateObjetMetier();
		}
		catch(Exception e) {
			e.printStackTrace();
			UtilMessages.creatMessageUtil(session(), UtilMessages.ERROR_MESSAGE, e.getMessage());
			AjaxUpdateContainer.updateContainerWithID(erreurId(), context());
			return null;
		}
				
		if(activeEC().hasChanges()) {
			try {

				activeEC().saveChanges();
				if(activeEC().parentObjectStore()==edc()) {
					edc().saveChanges();
				}
				if (hasBinding(CktlAjaxWOComponent.BINDING_updateContainerID)){
					AjaxUpdateContainer.updateContainerWithID((String) valueForBinding(CktlAjaxWOComponent.BINDING_updateContainerID), context());
				}
				if (hasBinding("onValidMethod")){
					return onValidMethod();
				}
			}
			catch(Exception e) {
				
				e.printStackTrace();
			}
			finally {
				CktlAjaxWindow.close(context(), closeWindowId());
			}
		}
		
		return null;
	}
	
	public String formgestprestid() {
		return getComponentId() + "_formgestprest";
	}
	
	public String pubactprestationid() {
		return getComponentId()+"_pubactprestation";
	}
	
	public String erreurId() {
		return getComponentId()+"_erreur";
	}
	
	public String messageId() {
		return getComponentId()+"_message";
	}

	public String closeWindowId() {
		return (String)valueForBinding(B_closeWindowId);
	}
	
	public WOActionResults fermer() {
		activeEC().revert();
		if(closeWindowId()!=null) {
			CktlAjaxWindow.close(context(), closeWindowId());
		}
		return null;
	}
	public WOActionResults onValidMethod() {
		return (WOActionResults) valueForBinding("onValidMethod");
	}
	
	public WOActionResults cancel() {
		
		if ((prestationEnseignant() != null) && (prestationEnseignant().editingContext() != null))
			prestationEnseignant().editingContext().revert();
		setPrestationEnseignant(null);
		CktlAjaxWindow.close(context());
		return null;
	}

	

}