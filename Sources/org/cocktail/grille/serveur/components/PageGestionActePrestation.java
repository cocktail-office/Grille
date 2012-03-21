package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grillefwk.serveur.metier.eof.EOActePrestation;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

import er.ajax.AjaxUpdateContainer;
import er.extensions.eof.ERXEC;

@SuppressWarnings("serial")
public class PageGestionActePrestation extends BaseGestionRef {
	
	private static final EOQualifier QUAL_ACTE_VALIDE = new EOKeyValueQualifier(EOActePrestation.DATE_ANNULATION_KEY,
																				EOQualifier.QualifierOperatorEqual,
																				NSKeyValueCoding.NullValue);
	private EOActePrestation editingActePrestation;
	
	private EOGenericRecord selObject;
	
    public PageGestionActePrestation(WOContext context) {
        super(context);
        displayGroup().setDelegate(new DgDelegate());
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public NSArray<EOGenericRecord> getLstObj() {
		NSArray objects = EOActePrestation.fetchActePrestations(edc(), QUAL_ACTE_VALIDE, new NSArray<EOSortOrdering>(EOActePrestation.SORT_LIB_LONG_ASC));
		return objects;
	}

	@Override
	public WOActionResults editObj(Object disc) {
		editingActePrestation = (EOActePrestation)disc;
		CktlAjaxWindow.open( context(), caweditobjid(), "Modifier l'acte de prestation");
		return null;
	}

	public WOActionResults addObj() {
		ERXEC ec = new ERXEC(edc());
		editingActePrestation = (EOActePrestation)EOUtilities.createAndInsertInstance(ec, EOActePrestation.ENTITY_NAME);
		CktlAjaxWindow.open( context(), caweditobjid(), "Ajouter un acte de prestation");
		return null;
	}

	public EOActePrestation getEditingActePrestation() {
		return editingActePrestation;
	}

	public void setEditingActePrestation(EOActePrestation object) {
		this.editingActePrestation = object;
	}
	
	public EOGenericRecord getSelObject() {
		return selObject;
	}

	public void setSelObject(EOGenericRecord object) {
		this.selObject = object;
	}

	public class DgDelegate {
		public void displayGroupDidChangeSelection(WODisplayGroup group) {
			setSelObject((EOGenericRecord) group.selectedObject());
		}
	}
	
	public  void search(){
		displayGroup().setObjectArray(getLstObj());
		AjaxUpdateContainer.updateContainerWithID(auclisteid(), context());
	}
}