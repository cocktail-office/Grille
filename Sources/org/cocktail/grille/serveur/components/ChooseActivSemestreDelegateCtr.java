package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteSemestre;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseSemestreDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnsTreeItem;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

public class ChooseActivSemestreDelegateCtr extends ChooseSemestreDelegateCtr {
	private GrilleApplicationUser gUser;

	public ChooseActivSemestreDelegateCtr(
			EOEditingContext defaultEditingContext,
			EOScolFormationAnnee selectedYear) {
		super(defaultEditingContext, selectedYear);
	}

	public GrilleApplicationUser gUser() {
		return gUser;
	}

	public void setgUser(GrilleApplicationUser gUser) {
		this.gUser = gUser;
	}

	@Override
	public NSArray<EnsTreeItem> childrenTreeNodes(Object node) {
		boolean edithRight = false;
		NSArray<EnsTreeItem> tmp = ((EnsTreeItem) node).getChilds();
		if ((tmp != null)
				&& (tmp.size() > 0)
				&& (tmp.lastObject().getEoObject() instanceof EOScolMaquetteSemestre)) {
			edithRight = true;
		}
		((EnsTreeItem) node).setChilds(ChooseActivUeDelegateCtr.filteredChilds(
				tmp, gUser(), getSelectedYear(), edithRight));
		return super.childrenTreeNodes(node);
	}

	@Override
	public NSArray<EOGenericRecord> getLstObjFromSearch() {
		return ChooseActivUeDelegateCtr.filteredObjFromSearch(
				super.getLstObjFromSearch(), gUser(), getSelectedYear(), true);
	}
}
