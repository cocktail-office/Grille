package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.ajax.CktlAjaxUtils;
import er.extensions.eof.ERXEC;

public class GestionActivite extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WODisplayGroup displayGroup;
	private EOActivite selectedActivite;
	private NSMutableDictionary<String, Object> searchDico;
	private EOActivite editedActivite;	
	private NSArray<String> lstEditZones;
	private EOTypeActivite typeActiviteOccur;
	

	public GestionActivite(WOContext context) {
		super(context);
		
		searchDico = new NSMutableDictionary<String, Object>();
		
		setLstEditZones(new NSArray<String>(new String[] { "editactivite",
				"resultsearch" }));
	}

	public WODisplayGroup displayGroup() {
		if (displayGroup == null) {
			displayGroup = new WODisplayGroup();
			displayGroup.setDelegate(new DgDelegate());
		}
		return displayGroup;
	}

	
	public void setDisplayGroup(WODisplayGroup displayGroup) {
		this.displayGroup = displayGroup;
	}


	public class DgDelegate {
		public void displayGroupDidChangeSelection(WODisplayGroup group) {
			setSelectedActivite((EOActivite) group.selectedObject());
		}
	}

	public EOActivite getSelectedActivite() {
		return selectedActivite;
	}

	public void setSelectedActivite(EOActivite selectedActivite) {
		this.selectedActivite = selectedActivite;
	}

	/**
	 * @return the searchDico
	 */
	public NSMutableDictionary<String, Object> searchDico() {
		return searchDico;
	}

	/**
	 * @param searchDico
	 *            the searchDico to set
	 */
	public void setSearchDico(NSMutableDictionary<String, Object> searchDico) {
		this.searchDico = searchDico;
	}

	private NSArray<EOTypeActivite> lstTypeActivite;
	
	/**
	 * @return the lstTypeActivite
	 */
	public NSArray<EOTypeActivite> lstTypeActivite() {
		if (lstTypeActivite == null)
			lstTypeActivite = EOTypeActivite.fetchAllTypeActivites(session()
					.defaultEditingContext(), new NSArray<EOSortOrdering>(
					new EOSortOrdering(EOTypeActivite.LIB_COURT_KEY,
							EOSortOrdering.CompareCaseInsensitiveDescending)));
		return lstTypeActivite;
	}

	

	/**
	 * @return the typeActiviteOccur
	 */
	public EOTypeActivite typeActiviteOccur() {
		return typeActiviteOccur;
	}

	/**
	 * @param typeActiviteOccur
	 *            the typeActiviteOccur to set
	 */
	public void setTypeActiviteOccur(EOTypeActivite typeActiviteOccur) {
		this.typeActiviteOccur = typeActiviteOccur;
	}

	public WOActionResults searchActivite() {
		EOEditingContext ec = session().defaultEditingContext();
		NSMutableArray<Object> tbKeys = new NSMutableArray<Object>();
		String conditionStr = "%@ = %@";
		tbKeys.add(EOActivite.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY);
		tbKeys.add(((Session)session()).selectedYear());

		if (searchDico.valueForKey("libelle") != null) {
			conditionStr += " AND %s caseInsensitiveLike %s ";
			tbKeys.add(EOActivite.COMMENTAIRE_KEY);
			tbKeys.add(searchDico.objectForKey("libelle") + "*");
		}

		if (searchDico.valueForKey("typeActivite") != null) {
			conditionStr += " AND %@ = %@ ";
			tbKeys.add(EOActivite.TO_TYPE_ACTIVITE_KEY);
			tbKeys.add(searchDico.objectForKey("typeActivite"));
		}

		NSArray<EOSortOrdering> sort = new NSArray<EOSortOrdering>(
				new EOSortOrdering[] {
						new EOSortOrdering(EOActivite.TO_TYPE_ACTIVITE_KEY
								+ "." + EOTypeActivite.LIB_COURT_KEY,
								EOSortOrdering.CompareCaseInsensitiveAscending),
						new EOSortOrdering(EOActivite.COMMENTAIRE_KEY,
								EOSortOrdering.CompareCaseInsensitiveAscending) });

		NSArray<EOActivite> tmp = EOActivite.fetchActivites(ec, EOQualifier
				.qualifierWithQualifierFormat(conditionStr, tbKeys), sort);

		displayGroup().setObjectArray(tmp);
		displayGroup().clearSelection();
		return null;
	}

	public boolean canAddActivite() {		
		return getgUser().canAddActivite(((Session)session()).selectedYear());
	}
	
	public boolean canEditActivite() {		
		return getgUser().canEditActivite(activOccur());
	}

	public WOActionResults AddActivite() {
		EOEditingContext newActivEc = ERXEC.newEditingContext(session().defaultEditingContext());
		editedActivite=EOActivite.createActivite(newActivEc,null, null, null);
		editedActivite.setToFwkScolarite_ScolFormationAnneeRelationship(((Session)session()).selectedYear().localInstanceIn(editedActivite.editingContext()));
		editedActivite.setToFwkScolarite_ScolMaquetteEcRelationship(null);
		editedActivite.setTypeLien(EOActivite.TYPE_LIEN_EC);
		displayGroup().clearSelection();
		CktlAjaxWindow.open(context(), caweditobjid(),
		"Ajout d'une activité");
		return null;
	}



	public EOActivite getEditedActivite() {
		//editedActivite = (EOActivite) valueForBinding("editedActivite");
		
		return editedActivite;
	}

	public void setEditedActivite(EOActivite editedActivite) {

	}

	/*
	public void setEditedActivite(EOActivite editedActivite) {
		setValueForBinding(editedActivite, "editedActivite");
		this.editedActivite = editedActivite;
	}
	//*/

	public boolean isActiviteEdited() {
		return (editedActivite != null);
	}

	

	public WOComponent editMethodeObject() {
		return this;
	}

	public WOActionResults editActivite(Object act) {
		this.editedActivite = (EOActivite) act;
		displayGroup().setSelectedObject(this.editedActivite);
		CktlAjaxWindow.open(context(), caweditobjid(),	"Modification d'une activité");
		return null;
	}

	public NSArray<String> getLstEditZones() {
		return lstEditZones;
	}

	public void setLstEditZones(NSArray<String> lstEditZones) {
		this.lstEditZones = lstEditZones;
	}

	public String caweditobjid() {
		return getComponentId() + "_caweditobjcid";
	}

	private EOScolFormationAnnee lastFetchedYear;
	private EOActivite activOccur;	

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		//refresh des liste si changement d'année
		if (lastFetchedYear==null) {
			lastFetchedYear = ((Session)session()).selectedYear();
		}
		if (!lastFetchedYear.equals(((Session)session()).selectedYear())){
			searchActivite();			
			lastFetchedYear = ((Session)session()).selectedYear();
		}
		// Chargement à la main des js et css pour la modal box pour éviter des bug sur ouverture d'une modal dans un CktlAjaxWindow
		super.appendToResponse(response, context);
		//CktlAjaxUtils.addScriptResourceInHead(context, response, "Ajax.framework", "modalbox.js");
		CktlAjaxUtils.addStylesheetResourceInHead(context, response, "Ajax.framework", "modalbox.css");
	}

	/**
	 * @return the activOccur
	 */
	public EOActivite activOccur() {
		return activOccur;
	}

	/**
	 * @param activOccur the activOccur to set
	 */
	public void setActivOccur(EOActivite activOccur) {
		this.activOccur = activOccur;
	}
	
	
	
}