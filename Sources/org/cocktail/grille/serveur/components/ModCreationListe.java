package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOGrilleListe;
import org.cocktail.grillefwk.serveur.metier.eof.EOGrilleListeEc;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseParcourDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnseignementsTreeDelegate;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxModalDialog;
import er.ajax.AjaxUpdateContainer;

public class ModCreationListe extends GrilleComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String B_editedListe = "editedListe";

	public String inputFiltreEC;
	private NSMutableArray<EOScolMaquetteEc> listEc = new NSMutableArray<EOScolMaquetteEc>();
	private NSMutableArray<EOScolMaquetteEc> listRetainedEc = new NSMutableArray<EOScolMaquetteEc>();

	public EOScolMaquetteEc itemEc;
	private EOScolMaquetteEc selectedEc;

	public EOGrilleListeEc itemGrilleListeEc, selectedGrilleListeEc;

	public ModCreationListe(WOContext context) {
		super(context);
	}

	public WOActionResults openTreeFormation() {
		//AjaxUpdateContainer.updateContainerWithID("selectionlinkFenetre", context());
		AjaxModalDialog.open(context(), cawsearchzoneid(),
				"Choisir une formation");
		return null;
	}

	private ChooseParcourDelegateCtr treeParDelegate;

	public ChooseParcourDelegateCtr treeParDelegate() {
		if (treeParDelegate == null) {
			treeParDelegate = new ChooseParcourDelegateCtr(session()
					.defaultEditingContext(),
					((Session) session()).selectedYear());
			initTreeDelagate(treeParDelegate);
		}
		return treeParDelegate;
	}

	public void setTreeParDelegate(ChooseParcourDelegateCtr treeParDelegate) {
		this.treeParDelegate = treeParDelegate;
	}

	private void initTreeDelagate(EnseignementsTreeDelegate delegate) {
		delegate.setSelectedYear(editedListe()
				.toFwkScolarite_ScolFormationAnnee());
	}

	public WOActionResults closeSearchWin() {
		if (hasBinding("updateContainerID")) {
			AjaxUpdateContainer.updateContainerWithID(
					(String) valueForBinding("updateContainerID"), context());
		}
		AjaxModalDialog.close(context());
		return null;
	}

	public WOActionResults afficherEC() {
		String input = null;
		if (inputFiltreEC != null) {
			input = inputFiltreEC.replaceAll("%", "*").toUpperCase();
		}
		if ((input != null) && (!input.contains("*"))) {
			input = "*" + input + "*";
		}
		listEc.clear();
		if ((editedListe() != null)
				&& (editedListe().toFwkScolarite_ScolMaquetteParcours() != null)) {
			listEc.addObjectsFromArray(FinderScolMaquetteEc
					.getEcForParcoursAndLibelle(
							editedListe().editingContext(),
							editedListe().toFwkScolarite_ScolMaquetteParcours(),
							input, ((Session) session()).selectedYear()));
		}
		AjaxUpdateContainer.updateContainerWithID(auclisteecid(), context());
		return null;
	}

	private void addEc(EOScolMaquetteEc anEc) {
		if (anEc == null) {
			return;
		}
		boolean ok = true;
		if ((editedListe() != null)
				&& (editedListe().toGrilleListeEcs() != null)) {
			for (EOGrilleListeEc ec : editedListe().toGrilleListeEcs()) {
				if (ec.toFwkScolarite_ScolMaquetteEc().equals(anEc)) {
					ok = false;
					break;
				}
			}
		}
		if (ok) {
			EOGrilleListeEc gle = (EOGrilleListeEc) EOUtilities
					.createAndInsertInstance(editedListe().editingContext(),
							EOGrilleListeEc.ENTITY_NAME);
			gle.addObjectToBothSidesOfRelationshipWithKey(anEc,
					EOGrilleListeEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY);
			gle.addObjectToBothSidesOfRelationshipWithKey(editedListe(),
					EOGrilleListeEc.TO_GRILLE_LISTE_KEY);
			gle.setDCreation(new NSTimestamp());
		}
	}

	private void removeGrilleListeEc(EOGrilleListeEc gle) {
		editedListe().removeFromToGrilleListeEcsRelationship(gle);
		editedListe().editingContext().deleteObject(gle);
	}

	public WOActionResults addAll() {
		//listRetainedEc.addObjectsFromArray(listEc);
		for (EOScolMaquetteEc anEc : listEc) {
			addEc(anEc);
		}
		listEc.clear();
		updateBothListsContainers();
		return null;
	}

	public WOActionResults addSelected() {
		if (selectedEc != null) {
			//listRetainedEc.add(selectedEc);
			addEc(selectedEc);
			listEc.remove(selectedEc);
			updateBothListsContainers();
		}
		return null;
	}

	public WOActionResults removeAll() {
		for (EOGrilleListeEc gle : editedListe().toGrilleListeEcs()) {
			removeGrilleListeEc(gle);
			listEc.add(gle.toFwkScolarite_ScolMaquetteEc());
		}
		updateBothListsContainers();
		return null;

		/*
		if(listRetainedEc!=null) {
			listEc.addObjectsFromArray(listRetainedEc);
			listRetainedEc.clear();
			updateBothListsContainers();
		}
		return null;
		*/
	}

	public WOActionResults removeSelected() {
		if (selectedGrilleListeEc != null) {
			listEc.add(selectedGrilleListeEc.toFwkScolarite_ScolMaquetteEc());
			//listRetainedEc.remove(selectedRetainedEc);
			removeGrilleListeEc(selectedGrilleListeEc);
			updateBothListsContainers();
		}
		return null;
	}

	public WOActionResults viderListeEc() {
		listEc.clear();
		updateBothListsContainers();
		return null;
	}

	private void updateBothListsContainers() {
		AjaxUpdateContainer.updateContainerWithID(auclisteecid(), context());
		AjaxUpdateContainer.updateContainerWithID(auclistretainedecid(),
				context());
	}

	/** Enregistrer les modifications */
	public WOActionResults save() {

		if (editedListe().myGlobalID().isTemporary()) {
			saveNewList();
		} else {
			saveExistingList();
		}
		return null;
	}

	private void saveNewList() {
		EOGrilleListe el = editedListe();
		EOEditingContext ec = el.editingContext();

		EOIndividu ind = getSession().getGUser().individu().localInstanceIn(ec);

		el.setToFwkpers_IndividuRelationship(ind);
		el.setToFwkScolarite_ScolFormationAnneeRelationship(getSession()
				.selectedYear().localInstanceIn(ec));
		/*
		for(EOScolMaquetteEc aEc : listRetainedEc) {
			EOGrilleListeEc gle = (EOGrilleListeEc)EOUtilities.createAndInsertInstance(ec, EOGrilleListeEc.ENTITY_NAME);
			gle.addObjectToBothSidesOfRelationshipWithKey(aEc, EOGrilleListeEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY);
			gle.addObjectToBothSidesOfRelationshipWithKey(el, EOGrilleListeEc.TO_GRILLE_LISTE_KEY);
			gle.setDCreation(new NSTimestamp());
		}
		*/

		try {
			ec.saveChanges();
			edc().saveChanges();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveExistingList() {
		System.out.println("saveExistingList");
		try {
			editedListe().editingContext().saveChanges();
			edc().saveChanges();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EOGrilleListe editedListe() {
		return (EOGrilleListe) valueForBinding(B_editedListe);
	}

	public void setEditedListe(EOGrilleListe editedListe) {
		if ((editedListe() != null) && (!editedListe().equals(editedListe))) {
			viderListeEc();
		}
		setValueForBinding(editedListe, B_editedListe);
	}

	public NSArray<EOScolMaquetteEc> listEc() {
		return listEc;
	}

	public void setListEc(NSArray<EOScolMaquetteEc> array) {
		this.listEc.clear();
		this.listEc.addObjectsFromArray(array);
	}

	@SuppressWarnings("unchecked")
	public NSArray<EOScolMaquetteEc> listRetainedEc() {
		if (!editedListe().myGlobalID().isTemporary()) {
			if (listRetainedEc.count() == 0) {
				listRetainedEc = new NSMutableArray<EOScolMaquetteEc>(
						(NSArray<EOScolMaquetteEc>) editedListe()
								.valueForKeyPath(
										EOGrilleListe.TO_GRILLE_LISTE_ECS_KEY
												+ "."
												+ EOGrilleListeEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY));
			}
		}
		return listRetainedEc;
	}

	public void setListRetainedEc(NSArray<EOScolMaquetteEc> array) {
		this.listRetainedEc.clear();
		this.listRetainedEc.addObjectsFromArray(array);
	}

	public EOScolMaquetteEc selectedEc() {
		return selectedEc;
	}

	public void setSelectedEc(EOScolMaquetteEc selectedEc) {
		this.selectedEc = selectedEc;
	}

	/*
	public EOScolMaquetteEc selectedRetainedEc() {
		return selectedRetainedEc;
	}

	public void setSelectedRetainedEc(EOScolMaquetteEc selectedRetainedEc) {
		this.selectedRetainedEc = selectedRetainedEc;
	}
	*/

	public String cawsearchzoneid() {
		return getComponentId() + "_cawsearchzone";
	}

	public String formlisteid() {
		return getComponentId() + "_formliste";
	}

	public String aucformationid() {
		return getComponentId() + "_aucformation";
	}

	public String auclisteecid() {
		return getComponentId() + "_auclisteec";
	}

	public String auclistretainedecid() {
		return getComponentId() + "_auclistretainedec";
	}

	public String cawchooseparcoursid() {
		return getComponentId() + "_cawchooseparcoursid";
	}

}