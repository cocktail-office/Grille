package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grillefwk.serveur.metier.eof.EOGrilleListe;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteParcours;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.ajax.AjaxUpdateContainer;
import er.extensions.eof.ERXEC;

@SuppressWarnings("serial")
public class PageGestionListes extends GrilleComponent {

	private EOGrilleListe currentListe;
	private EOGrilleListe selectedListe;
	public EOGrilleListe itemGrilleListe;

	public boolean showExistingLists = false;
	private boolean showGestionListe = false;
	private String titreEditListe;
	private boolean canEditCode;

	public PageGestionListes(WOContext context) {
		super(context);
	}

	public EOGrilleListe currentListe() {
		if (currentListe == null) {
			ERXEC ec = new ERXEC(edc());
			initList(ec);
		}
		return currentListe;
	}

	public void setCurrentListe(EOGrilleListe object) {
		this.currentListe = object;
	}

	public WOActionResults modifyAList() {
		showExistingLists = true;
		setTitreEditListe("Modification d'une liste");
		setShowGestionListe(false);
		return null;
	}

	private void initList(ERXEC ec) {

		currentListe = (EOGrilleListe) EOUtilities.createAndInsertInstance(ec,
				EOGrilleListe.ENTITY_NAME);
		currentListe
				.setToFwkScolarite_ScolFormationAnneeRelationship(getSession()
						.selectedYear().localInstanceIn(ec));
	}

	public WOActionResults initAList() {
		setShowGestionListe(true);
		if (showExistingLists) {
			showExistingLists = false;
			//AjaxUpdateContainer.updateContainerWithID(aucpubgrillelistesid(),context());
		}
		//AjaxUpdateContainer.updateContainerWithID(auccopielst(),context());
		setTitreEditListe("Création d'un liste");
		ERXEC ec = new ERXEC(edc());
		initList(ec);
		canEditCode = true;
		return null;
	}

	public WOActionResults initAListSameParcours() {

		setShowGestionListe(true);

		ERXEC ec = new ERXEC(edc());
		if (currentListe != null) {
			EOScolMaquetteParcours parcours = currentListe
					.toFwkScolarite_ScolMaquetteParcours();
			if (parcours != null) {
				initList(ec);
				currentListe
						.setToFwkScolarite_ScolMaquetteParcoursRelationship(parcours
								.localInstanceIn(ec));
				currentListe
						.setToFwkScolarite_ScolFormationSpecialisationRelationship(currentListe
								.toFwkScolarite_ScolMaquetteParcours()
								.toFwkScolarite_ScolFormationSpecialisation());
			}
			canEditCode = true;
		}
		showExistingLists = false;
		return null;
	}

	public NSArray<EOGrilleListe> listOfGrilleListes() {
		NSMutableArray<EOGrilleListe> retour = new NSMutableArray<EOGrilleListe>();
		for (EOGrilleListe lst : EOGrilleListe.fetchAllGrilleListes(edc(),
				new NSArray<EOSortOrdering>(EOGrilleListe.SORT_LIBELLE_ASC))) {
			if (lst.toFwkpers_Individu().equals(
					getSession().getGUser().individu())) {
				retour.add(lst);
			} else {
				boolean right = getSession().getGUser()
						.haveRightForObjMaquette(lst.getParcours(),
								getSession().selectedYear(),true);
				if (right) {
					retour.add(lst);
				}
			}
		}
		
		return retour.immutableClone();
	}

	public WOActionResults grilleListeSelectionChanged() {
		System.out.println("grilleListeSelectionChanged");

		return null;
	}

	public WOActionResults initModifyListe() {
		if (selectedListe != null) {
			setShowGestionListe(true);
			ERXEC ec = new ERXEC(edc());
			currentListe = selectedListe.localInstanceIn(ec);
			//AjaxUpdateContainer.updateContainerWithID(auccreationlisteid(),	context());
			//AjaxUpdateContainer.updateContainerWithID(auccopielst(), context());
			canEditCode = false;
		}

		return null;
	}

	public EOGrilleListe selectedListe() {
		return selectedListe;
	}

	public void setSelectedListe(EOGrilleListe selectedListe) {
		this.selectedListe = selectedListe;
	}

	public boolean isShowGestionListe() {
		return showGestionListe;
	}

	public void setShowGestionListe(boolean showGestionListe) {
		this.showGestionListe = showGestionListe;
	}

	public String pubgrillelistesid() {
		return getComponentId() + "_pubgrillelistes";
	}

	public String auccreationlisteid() {
		return getComponentId() + "_auccreationliste";
	}

	public String aucpubgrillelistesid() {
		return getComponentId() + "_aucpubgrillelistes";
	}

	public String formchangelisteid() {
		return getComponentId() + "_formchangeliste";
	}

	/**
	 * @return the titreEditListe
	 */
	public String titreEditListe() {
		return titreEditListe;
	}

	/**
	 * @param titreEditListe
	 *            the titreEditListe to set
	 */
	public void setTitreEditListe(String titreEditListe) {
		this.titreEditListe = titreEditListe;
	}

	public String libListeOccur() {
		return "(" + itemGrilleListe.glCode() + ")-"
				+ itemGrilleListe.glLibelle();
	}

	public Boolean canCreateListeSameFromation() {

		return ((currentListe != null) && (currentListe
				.toFwkScolarite_ScolMaquetteParcours() != null));
	}

	public String auccopielst() {
		return getComponentId() + "_auccopielst";
	}

	/**
	 * @return the canEditCode
	 */
	public boolean canEditCode() {
		return canEditCode;
	}

	/**
	 * @param canEditCode
	 *            the canEditCode to set
	 */
	public void setCanEditCode(boolean canEditCode) {
		this.canEditCode = canEditCode;
	}

}