package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOReport;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.appserver.WOActionResults;

import er.ajax.AjaxUpdateContainer;

public class ModGestionReport extends GrilleComponent {

	private static final long serialVersionUID = 4863741374987137769L;
	public static final String B_editedReport = "editedReport";
	
	public EOScolFormationAnnee itemAnnee;
	
	public ModGestionReport(final WOContext context) {
        super(context);
    }
	
	@SuppressWarnings("unchecked")
	public NSArray<EOScolFormationAnnee> listAnnees() {
		return EOScolFormationAnnee.fetchAll(activeEC(), null,null);
	}
	
	public String anneeDescription() {
		if(itemAnnee!=null) {
			return String.valueOf(itemAnnee.fannDebut())+"/"+String.valueOf(itemAnnee.fannFin());
		}
		return "";
	}
	
	public WOActionResults saveChanges() {
		try {
			editedReport().validateObjetMetier();
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
				
				CktlAjaxWindow.close(context());
				
			}
		}
		
		return null;

	}

	public WOActionResults cancel() {
		
		if ((editedReport() != null) && (editedReport().editingContext() != null))
			editedReport().editingContext().revert();
		setEditedReport(null);
		CktlAjaxWindow.close(context());
		return null;
	}

	
	public EOReport editedReport() {
		return (EOReport)valueForBinding(B_editedReport);
	}
	
	public void setEditedReport(EOReport report) {
		setValueForBinding(report, B_editedReport);
	}
	
	public EOEditingContext activeEC() {
		return editedReport().editingContext();
	}
	
	public String formgestreportid() {
		return getComponentId()+"_formgestreport";
	}
	
	public String pubanneeid() {
		return getComponentId()+"_pubannee";
	}

	public WOActionResults onValidMethod() {
		return (WOActionResults) valueForBinding("onValidMethod");
	}

}