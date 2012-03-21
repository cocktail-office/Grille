package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOEcVerrous;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.ajax.AjaxModalDialog;

import org.cocktail.scolaritefwk.serveur.finder.FinderScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteResponsableEc;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseApDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseEcDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;
import org.cocktail.scolaritemodulesfwk.serveur.components.SearchMaquette;

public class CompoEC extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4673704487254134638L;
	public static final String BIND_SELECTED_EC = "selectedEc";
	public static final String BIND_EDITING_CONTEXT = "editingContext";
	public static final String BIND_ANNEE = "annee";

	public CompoEC(WOContext context) {
		super(context);
	}

	public EOScolMaquetteEc selectedEc() {
		return (EOScolMaquetteEc) valueForBinding(BIND_SELECTED_EC);
	}

	public void setSelectedEc(EOScolMaquetteEc selectedEc) {
		setValueForBinding(selectedEc, BIND_SELECTED_EC);
	}

	public EOEditingContext editingContext() {
		if (hasBinding(BIND_EDITING_CONTEXT)) {
			return (EOEditingContext) valueForBinding(BIND_EDITING_CONTEXT);
		}
		return session().defaultEditingContext();
	}

	public EOScolFormationAnnee annee() {
		if (hasBinding(BIND_ANNEE)) {
			return (EOScolFormationAnnee) valueForBinding(BIND_ANNEE);
		}
		return ((Session) session()).selectedYear();
	}

	public ChooseEcDelegateCtr getTreeDelegate() {
		if ((((Session) session()).currentChooseEcDelegateCtr() != null)
				&& ((((Session) session()).currentChooseEcDelegateCtr() instanceof ChooseEcVoeuxDelegateCtr))) {

		} else
			((Session) session())
					.setCurrentChooseEcDelegateCtr(new ChooseEcVoeuxDelegateCtr());
		return ((Session) session()).currentChooseEcDelegateCtr();
	}

	public void setTreeDelegate(ChooseApDelegateCtr treeDelegate) {
		((Session) session()).setCurrentChooseEcDelegateCtr(treeDelegate);
	}

	public WOActionResults closeSearchWin() {
		AjaxModalDialog.close(context());
		return null;
	}

	public String cawsearchzoneid() {
		return getComponentId() + "_cawsearchzoneid";
	}

	private EOScolFormationDiplome diplOccur;

	/**
	 * @return the diplOccur
	 */
	public EOScolFormationDiplome diplOccur() {
		return diplOccur;
	}

	/**
	 * @param diplOccur
	 *            the diplOccur to set
	 */
	public void setDiplOccur(EOScolFormationDiplome diplOccur) {
		this.diplOccur = diplOccur;
	}

	public NSArray<EOScolFormationDiplome> lstDiplomesEc() {

		if (selectedEc() != null) {
			return FinderScolFormationDiplome.getDiplomesForEcAndYear(
					editingContext(), selectedEc(), annee());
		}
		return null;
	}

	public boolean isMultiDiplomes() {
		return ((lstDiplomesEc() != null) && (lstDiplomesEc().size() > 1));
	}

	public String libUniqueDiplome() {
		return ((lstDiplomesEc() != null)
				&& (lstDiplomesEc().lastObject() != null) ? lstDiplomesEc()
				.lastObject().fdipLibelle() : "");
	}

	private NSArray<EOScolMaquetteResponsableEc> lstEquipe;

	/**
	 * @return the lstEquipe
	 */
	public NSArray<EOScolMaquetteResponsableEc> lstEquipe() {
		return lstEquipe;
	}

	/**
	 * @param lstEquipe
	 *            the lstEquipe to set
	 */
	public void setLstEquipe(NSArray<EOScolMaquetteResponsableEc> lstEquipe) {
		this.lstEquipe = lstEquipe;
	}

	public boolean isMembreEquipe() {
		return ((lstEquipe() != null) && (lstEquipe().count() > 0));
	}

	private EOScolMaquetteResponsableEc responsableOccur;

	/**
	 * @return the responsableOccur
	 */
	public EOScolMaquetteResponsableEc responsableOccur() {
		return responsableOccur;
	}

	/**
	 * @param responsableOccur
	 *            the responsableOccur to set
	 */
	public void setResponsableOccur(EOScolMaquetteResponsableEc responsableOccur) {
		this.responsableOccur = responsableOccur;
	}

	/**
	 * @return the diplomePickerDelegate
	 */
	public DiplomePickerDefaultDelegate diplomePickerDelegate() {
		if ((((Session) session()).currentDiplomePickerDelegate() != null)
				&& (((Session) session()).currentDiplomePickerDelegate() instanceof DiplomePickerDefaultDelegate)) {

		} else {
			((Session) session())
					.setCurrentDiplomePickerDelegate(new DiplomePickerDefaultDelegate(
							session().defaultEditingContext(),
							((Session) session()).selectedYear()));

		}
		return ((Session) session()).currentDiplomePickerDelegate();
	}

	/**
	 * @param diplomePickerDelegate
	 *            the diplomePickerDelegate to set
	 */
	public void setDiplomePickerDelegate(
			DiplomePickerDefaultDelegate diplomePickerDelegate) {
		((Session) session())
				.setCurrentDiplomePickerDelegate(diplomePickerDelegate);
	}

	public boolean isTreeItemEcLocked() {
		if (getTreeDelegate() != null) {
			if ((SearchMaquette.TREE_MODE_SEARCH.equals(getTreeDelegate()
					.getModeSearch()))
					&& (getTreeDelegate().treeItem() != null)
					&& (getTreeDelegate().treeItem().getEoObject() != null)
					&& (getTreeDelegate().treeItem().getEoObject() instanceof EOScolMaquetteEc)) {
				return EOEcVerrous
						.IsEcLocked((EOScolMaquetteEc) getTreeDelegate()
								.treeItem().getEoObject());
			}
			if ((SearchMaquette.LIB_MODE_SEARCH.equals(getTreeDelegate()
					.getModeSearch()))
					&& (getTreeDelegate().getLocalSelectedObj() != null)
					&& (getTreeDelegate().getLocalSelectedObj() instanceof EOScolMaquetteEc)) {
				return EOEcVerrous
						.IsEcLocked((EOScolMaquetteEc) getTreeDelegate()
								.getLocalSelectedObj());
			}

		}
		return false;
	}

}