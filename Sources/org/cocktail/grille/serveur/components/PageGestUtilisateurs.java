package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolConstanteResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;

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

import er.ajax.AjaxModalDialog;
import er.ajax.AjaxUpdateContainer;
import er.ajax.CktlAjaxUtils;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;

public class PageGestUtilisateurs extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EOScolFormationSpecialisation selectedSpec;
	private EOScolFormationResponsabilite respOccur;

	public PageGestUtilisateurs(WOContext context) {
		super(context);
	}

	/**
	 * @return the selectedSpec
	 */
	public EOScolFormationSpecialisation selectedSpec() {
		return selectedSpec;
	}

	/**
	 * @param selectedSpec
	 *            the selectedSpec to set
	 */
	public void setSelectedSpec(EOScolFormationSpecialisation selectedSpec) {
		this.selectedSpec = selectedSpec;
	}

	/**
	 * @return the respOccur
	 */
	public EOScolFormationResponsabilite respOccur() {
		return respOccur;
	}

	/**
	 * @param respOccur
	 *            the respOccur to set
	 */
	public void setRespOccur(EOScolFormationResponsabilite respOccur) {
		this.respOccur = respOccur;
	}

	private WODisplayGroup displayGroup;

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
			setSelectedResp((EOScolFormationResponsabilite) group
					.selectedObject());
		}
	}

	protected EOScolFormationResponsabilite selectedResp;

	public EOScolFormationResponsabilite getSelectedResp() {
		return selectedResp;
	}

	public void setSelectedResp(EOScolFormationResponsabilite selectedResp) {
		this.selectedResp = selectedResp;
	}

	public WOComponent editMethodeObject() {
		return this;
	}

	public String auclstrespid() {
		return getComponentId() + "_auclstrespid";
	}

	public WOActionResults searchResp() {
		haveSearched=true;
		displayGroup().clearSelection();
		String qualStr = EOScolFormationResponsabilite.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY+"=%@";
		NSMutableArray<Object> args = new NSMutableArray<Object>(((Session)session()).selectedYear());
		if (selectedSpec() != null) {
			qualStr+=" AND "+EOScolFormationResponsabilite.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY
			+ "=%@";
			args.add(selectedSpec());
		}
		if (searchedIndiv != null) {
			qualStr+=" AND "+EOScolFormationResponsabilite.TO_FWKPERS__INDIVIDU_KEY
			+ "=%@";
			args.add(searchedIndiv);
		}
		
			NSArray<EOScolFormationResponsabilite> tmp = EOScolFormationResponsabilite
					.fetchAll(
							session().defaultEditingContext(),
							EOQualifier.qualifierWithQualifierFormat(qualStr, args), null,
							true);
			displayGroup()
					.setObjectArray(
							EOSortOrdering
									.sortedArrayUsingKeyOrderArray(
											tmp,
											new NSArray<EOSortOrdering>(
													new EOSortOrdering[] { new EOSortOrdering(
															ERXQ.keyPath(
																	EOScolFormationResponsabilite.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,
																	EOScolFormationSpecialisation.FSPN_LIBELLE_KEY),
															EOSortOrdering.CompareAscending) ,new EOSortOrdering(
															ERXQ.keyPath(
																	EOScolFormationResponsabilite.TO_FWK_SCOLARITE__SCOL_CONSTANTE_RESPONSABILITE_KEY,
																	EOScolConstanteResponsabilite.CRES_PRIORITE_KEY),
															EOSortOrdering.CompareAscending) })));
		/*	
		} else {
			UtilMessages.creatMessageUtil(session(), UtilMessages.INFO_MESSAGE,
					"Choisissez une spécialisation !");
			displayGroup().setObjectArray(null);
			
		}
		//*/
		AjaxUpdateContainer.updateContainerWithID(erreurid(), context());

		return null;
	}
	public boolean haveSearched=false;
	
	public WOActionResults editResp(Object act) {
		setModeEdition("M");
		this.editedResp = (EOScolFormationResponsabilite) act;
		displayGroup().setSelectedObject(this.editedResp);
		CktlAjaxWindow.open(context(), caweditobjid(),	"Modification d'une responsabilité");
		return null;
	}
	
	
	private EOScolFormationResponsabilite editedResp;
	/**
	 * @return the editedResp
	 */
	public EOScolFormationResponsabilite editedResp() {
		return editedResp;
	}

	/**
	 * @param editedResp the editedResp to set
	 */
	public void setEditedResp(EOScolFormationResponsabilite editedResp) {
		this.editedResp = editedResp;
	}

	public String auceditrespid() {
		return getComponentId()+"_auceditrespid";
	}
	
	public String caweditobjid() {
		return getComponentId() + "_caweditobjcid";
	}

	public WOActionResults AddResp() {
		setModeEdition("A");
		EOEditingContext newRespEc = ERXEC.newEditingContext(session().defaultEditingContext());
		editedResp=EOScolFormationResponsabilite.create(newRespEc);
		editedResp.setToFwkScolarite_ScolFormationAnneeRelationship(((Session)session()).selectedYear());
		editedResp.setToFwkScolarite_ScolFormationSpecialisationRelationship(selectedSpec);
		displayGroup().clearSelection();
		CktlAjaxWindow.open(context(), caweditobjid(),
		"Ajout d'une responsabilité");
		return null;
		
	}

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		super.appendToResponse(response, context);
		// Chargement à la main des js et css pour la modal box pour éviter des bug sur ouverture d'une modal dans un CktlAjaxWindow		
		CktlAjaxUtils.addStylesheetResourceInHead(context, response, "Ajax.framework", "modalbox.css");
	}

	public String editRespWindowId() {
		return getComponentId()+"_editRespWindowId";
	}

	public String erreurid() {
		return getComponentId()+"_erreurid"; 
	}

	public String aucSearch() {
		return getComponentId()+"_aucSearch";
	}

	public String searchZoneId() {
		return getComponentId()+"_searchZoneId";
	}

	public String aucselectlinkid() {
		return getComponentId()+"_aucselectlinkid";
	}
	private EOIndividu selIndiv;
	public EOIndividu getSelIndiv() {
		return selIndiv;
	}

	public void setSelIndiv(EOIndividu selIndiv) {
		this.selIndiv = selIndiv;
	}

	public String aucindividuid() {
		return getComponentId()+"_aucindividuid";
	}
	
	public WOActionResults setSearchOff() {
		setSearchMode(false);
		setSearchedIndiv(getSelIndiv());
		AjaxModalDialog.close(context());
		return null;
	}
	private boolean searchMode = false;

	/**
	 * @return the searchMode
	 */
	public boolean searchMode() {
		return searchMode;
	}

	/**
	 * @param searchMode
	 *            the searchMode to set
	 */
	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}
	private EOIndividu searchedIndiv;
	private String modeEdition;

	public EOIndividu getSearchedIndiv() {
		return searchedIndiv;
	}

	public void setSearchedIndiv(EOIndividu searchedIndiv) {
		this.searchedIndiv = searchedIndiv;
	}
	public String textButtonChoisirPersonne() {
		if (getSelIndiv() != null) {
			return "Choisir " + getSelIndiv().getNomAndPrenom();
		}
		return null;
	}
	
	public WOActionResults setSearchOn() {
		setSearchMode(true);
		AjaxModalDialog.open(context(), searchZoneId(), "Chercher un individu");
		
		return null;
	}

	/**
	 * @return the modeEdition
	 */
	public String modeEdition() {
		return modeEdition;
	}

	/**
	 * @param modeEdition the modeEdition to set
	 */
	public void setModeEdition(String modeEdition) {
		this.modeEdition = modeEdition;
	}

	public WOActionResults clearSearchedInd() {
		setSearchedIndiv(null);
		return null;
	}
}