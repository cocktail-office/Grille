package org.cocktail.grille.serveur.components;

import org.cocktail.grille.serveur.Session;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseApDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

public class GestionAP extends BaseModule {
	
	private EOScolFormationDiplome diplOccur;
	private ChooseApDelegateCtr treeDelegate;
	
    public GestionAP(WOContext context) {
        super(context);
    }

	private EOScolMaquetteAp selectedAp;

	/**
	 * @return the selectedAp
	 */
	public EOScolMaquetteAp selectedAp() {
		return selectedAp;
	}

	/**
	 * @param selectedAp the selectedAp to set
	 */
	public void setSelectedAp(EOScolMaquetteAp selectedAp) {
		this.selectedAp = selectedAp;
	}

	public NSArray<EOScolFormationDiplome> lstDiplomesAp() {
		// TODO
		return null;
	}

	

	/**
	 * @return the diplOccur
	 */
	public EOScolFormationDiplome diplOccur() {
		return diplOccur;
	}

	/**
	 * @param diplOccur the diplOccur to set
	 */
	public void setDiplOccur(EOScolFormationDiplome diplOccur) {
		this.diplOccur = diplOccur;
	}
	
	public ChooseApDelegateCtr getTreeDelegate() {
		if ((treeDelegate != null)
				&& (treeDelegate instanceof ChooseApDelegateCtr)) {

		} else
			treeDelegate = new ChooseApDelegateCtr();
		return treeDelegate;
	}

	public void setTreeDelegate(ChooseApDelegateCtr treeDelegate) {
		this.treeDelegate = treeDelegate;
	}
	/**
	 * @return the diplomePickerDelegate
	 */
	public DiplomePickerDefaultDelegate diplomePickerDelegate() {
		if ((((Session)session()).currentDiplomePickerDelegate() != null)
				&& (((Session)session()).currentDiplomePickerDelegate() instanceof DiplomePickerDefaultDelegate)) {

		} else {
			((Session)session()).setCurrentDiplomePickerDelegate(new DiplomePickerDefaultDelegate(session().defaultEditingContext(),((Session)session()).selectedYear()));
			
		}
		return ((Session)session()).currentDiplomePickerDelegate();
	}

	/**
	 * @param diplomePickerDelegate the diplomePickerDelegate to set
	 */
	public void setDiplomePickerDelegate(
			DiplomePickerDefaultDelegate diplomePickerDelegate) {
		((Session)session()).setCurrentDiplomePickerDelegate(diplomePickerDelegate);
	}
}