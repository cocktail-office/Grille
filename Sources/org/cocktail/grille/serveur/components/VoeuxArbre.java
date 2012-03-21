package org.cocktail.grille.serveur.components;

import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseSemestreDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnseignementsTreeDelegate;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOGenericRecord;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;

public class VoeuxArbre extends WOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object treeOccur;
	private EOGenericRecord selectedObj;

	public VoeuxArbre(WOContext context) {
		super(context);
	}

	public Object getTreeOccur() {
		return treeOccur;
	}

	public void setTreeOccur(Object treeOccur) {
		this.treeOccur = treeOccur;
	}

	/**
	 * @return the treeDelegate
	 */
	public ConsultArbreDelegateCtr treeDelegate() {
		return ((Session)session()).currentConsArbreDelegate();
	}

	/**
	 * @param treeDelegate
	 *            the treeDelegate to set
	 */
	public void setTreeDelegate(ConsultArbreDelegateCtr treeDelegate) {
		((Session)session()).setCurrentConsArbreDelegate(treeDelegate);
	}

	/**
	 * @return the selectedYear
	 */
	public EOScolFormationAnnee selectedYear() {
		return ((Session) session()).selectedYear();
	}

	/**
	 * @return the selectedObj
	 */
	public EOGenericRecord getSelectedObj() {
		return selectedObj;
	}

	/**
	 * @param selectedObj
	 *            the selectedObj to set
	 */
	public void setSelectedObj(EOGenericRecord selectedObj) {
		this.selectedObj = selectedObj;
	}

	public WOActionResults onSelectObj() {
		if (selectedObj != null) {
			if (selectedObj instanceof EOScolMaquetteEc) {
				GestionEC next = (GestionEC) pageWithName(GestionEC.class
						.getName());
				next.setSelectedEc((EOScolMaquetteEc) selectedObj);
				return next;
			}
			if (selectedObj instanceof EOActivite) {
				PageGestVoeuxActiv next = (PageGestVoeuxActiv) pageWithName(PageGestVoeuxActiv.class
						.getName());
				next.setCurrentYear(((Session) session()).currentYear());
				next.setEcEdit(session().defaultEditingContext());
				next.setHaveAdminRight(((Session) session()).getGUser()
						.haveAdminRight());
				next.setSelectedActiv((EOActivite) selectedObj);
				return next;
			}
		}
		return null;
	}

	public DiplomePickerDefaultDelegate diplomePickerDelegate() {
		if (((Session)session()).currentConsArbreDelegate() == null) {			
			((Session)session()).setCurrentDiplomePickerDelegate(new DiplomePickerDefaultDelegate(session().defaultEditingContext(), ((Session) session()).currentYear()));
		}
		return ((Session)session()).currentDiplomePickerDelegate();
	}

	public void setDiplomePickerDelegate(
			DiplomePickerDefaultDelegate diplomePikerDelegate) {
		((Session)session()).setCurrentDiplomePickerDelegate(diplomePikerDelegate);
	}

}