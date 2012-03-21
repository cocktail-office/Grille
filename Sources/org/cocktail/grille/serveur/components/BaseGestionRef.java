package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grille.serveur.Session;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

public abstract class BaseGestionRef extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8233403648130016571L;

	protected EOGenericRecord selObj;
	private EOGenericRecord editedObj;
	private WODisplayGroup displayGroup;
	private NSArray<String> lstEditZones;
	
	
	private EOIndividu selectedEnseignant;

	public BaseGestionRef(WOContext context) {
		super(context);
	}

	public Session session() {
		return (Session) super.session();
	}

	public WODisplayGroup displayGroup() {

		if (displayGroup == null) {
			displayGroup = new WODisplayGroup();
			displayGroup.setDelegate(new DgDelegate());
			displayGroup.setNumberOfObjectsPerBatch(10);
			searchObj();
		}

		return displayGroup;
	}

	public class DgDelegate {
		public void displayGroupDidChangeSelection(WODisplayGroup group) {
			setSelObj((EOGenericRecord) group.selectedObject());
		}
	}

	public abstract NSArray<EOGenericRecord> getLstObj();

	public WOActionResults searchObj() {
		displayGroup.setObjectArray(getLstObj());
		displayGroup.clearSelection();
		displayGroup.updateDisplayedObjects();
		displayGroup.setCurrentBatchIndex(1);
		return null;
	}

	public void setDisplayGroup(WODisplayGroup displayGroup) {
		this.displayGroup = displayGroup;
	}

	public EOGenericRecord getSelObj() {
		return selObj;
	}

	public void setSelObj(EOGenericRecord selDisc) {
		this.selObj = selDisc;
	}

	private Boolean canEditObj = ((Session) session()).getGUser()
			.canEditRefTable(((Session)session()).selectedYear());

	/**
	 * @return the canEditDisc
	 */
	public Boolean canEditObj() {
		return canEditObj;
	}

	public String deleteTrigId() {
		return getComponentId() + "_deleteTrigId";
	}

	public NSArray<String> getLstEditZones() {
		if (lstEditZones == null) {
			lstEditZones = new NSArray<String>(new String[] { auclisteid(),
					auceditformid(), aucerreurid() });
		}
		return lstEditZones;
	}

	public CktlAjaxWOComponent editMethodeObject() {
		return this;
	}

	public abstract WOActionResults editObj(Object disc);

	public EOGenericRecord getEditedObj() {
		return editedObj;
	}

	public void setEditedObj(EOGenericRecord editedDisc) {
		this.editedObj = editedDisc;
	}

	private Boolean isEdited;

	/**
	 * @return the isEdited
	 */
	public Boolean isEdited() {
		return isEdited;
	}

	/**
	 * @param isEdited
	 *            the isEdited to set
	 */
	public void setEdited(Boolean isEdited) {
		this.isEdited = isEdited;
	}

	public String caweditobjid() {
		return getComponentId() + "_caweditobjcid";
	}

	public String auclisteid() {
		return getComponentId() + "_auclisteid";
	}

	public WOActionResults valid() {
		editedObj.editingContext().saveChanges();
		if (editedObj.editingContext().parentObjectStore()
				.equals(session().defaultEditingContext()))
			session().defaultEditingContext().saveChanges();

		return null;
	}

	public String auceditobjid() {
		return getComponentId() + "_auceditobjid";
	}

	public WOActionResults cancel() {
		editedObj.editingContext().revert();
		setEdited(Boolean.FALSE);
		CktlAjaxWindow.close(context(), caweditobjid());
		return null;
	}

	public String aucerreurid() {
		return getComponentId() + "_aucerreurid";
	}

	public String idmessageutil() {
		return getComponentId() + "_idmessageutil";
	}

	public String auceditformid() {
		return getComponentId() + "_auceditformid";
	}
	
	public String searchKeyPress() {
		return "if (event.keyCode == 13) $('" + asbsearchid() + "').click();";
	}
	
	public String asbsearchid() {
		return getComponentId() + "_asbsearchid";
	}

	public EOIndividu getSelectedEnseignant() {
		return selectedEnseignant;
	}

	public void setSelectedEnseignant(EOIndividu selectedEnseignant) {
		this.selectedEnseignant = selectedEnseignant;
	}

}
