package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseSpecialisationDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnsTreeItem;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

public class ChooseActivSpecialisationDelegateCtr extends
		ChooseSpecialisationDelegateCtr {
	public ChooseActivSpecialisationDelegateCtr(EOEditingContext ec,
			EOScolFormationAnnee selectedYear) {
		super(ec, selectedYear);
	}

	private GrilleApplicationUser gUser;

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
				&& (tmp.lastObject().getEoObject() instanceof EOScolFormationSpecialisation)) {
			edithRight = true;
		}

		((EnsTreeItem) node).setChilds(ChooseActivUeDelegateCtr.filteredChilds(
				tmp, gUser(), getSelectedYear(),
				edithRight));
		return super.childrenTreeNodes(node);
	}

	@Override
	public NSArray<EOGenericRecord> getLstObjFromSearch() {
		return ChooseActivUeDelegateCtr.filteredObjFromSearch(
				super.getLstObjFromSearch(), gUser(), getSelectedYear(), true);
	}
}
