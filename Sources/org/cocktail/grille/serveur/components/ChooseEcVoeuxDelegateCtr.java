package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseEcDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnsTreeItem;

import com.webobjects.foundation.NSArray;

public class ChooseEcVoeuxDelegateCtr extends ChooseEcDelegateCtr {

	@Override
	public NSArray<EnsTreeItem> childrenTreeNodes(Object node) {
		if (((EnsTreeItem) node).getEoObject() instanceof EOScolMaquetteAp) {
			if (((EnsTreeItem) node).isNotChilds()) {
				((EnsTreeItem) node)
						.setChilds(EnsTreeItem.buildEnsTreeItemArray(
								FinderVoeux.getVoeuxForEcOrAPAndYear(
										((EnsTreeItem) node).getEoObject()
												.editingContext(), null,
										(EOScolMaquetteAp) ((EnsTreeItem) node)
												.getEoObject(),
										((EnsTreeItem) node).getSelectedYear()),
								((EnsTreeItem) node)));
			}
			return ((EnsTreeItem) node).getChilds();
		}
		return ((EnsTreeItem) node).getChilds();
	}

	@Override
	public String getLibelle(Object eoObject) {
		if (eoObject instanceof EOVoeux) {
			EOVoeux voeux = ((EOVoeux) eoObject);
			return voeux.toFwkpers_IndividuEnseignant().getNomPrenomAffichage()
					+ " : " + voeux.nbHeuresVoeux();
		}
		String retour = super.getLibelle(eoObject);

		return retour;
	}
}
