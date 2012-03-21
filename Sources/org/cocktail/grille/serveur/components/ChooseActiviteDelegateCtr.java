package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.grillefwk.serveur.finder.FinderActivite;
import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnsTreeItem;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnseignementsTreeDelegate;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.appserver.ERXWOContext;
import er.extensions.eof.ERXQ;

public class ChooseActiviteDelegateCtr extends EnseignementsTreeDelegate {

	public ChooseActiviteDelegateCtr() {
		// TODO Auto-generated constructor stub
	}

	public ChooseActiviteDelegateCtr(EOEditingContext ec,
			EOScolFormationAnnee selectedYear) {
		super(ec, selectedYear);
	}

	public boolean isLink() {
		return (treeItem().getEoObject() instanceof EOActivite);
	}

	@Override
	public boolean isLeaf(Object node) {
		return false;

	}

	@Override
	public NSArray<EnsTreeItem> childrenTreeNodes(Object node) {

		if ((((EnsTreeItem) node).getItemObject() != null)
				&& (((EnsTreeItem) node).getItemObject() instanceof ActiviteTreeItem)) {
			if (((EnsTreeItem) node).isNotChilds()) {
				((EnsTreeItem) node).setChilds(EnsTreeItem
						.buildEnsTreeItemArray(
								((ActiviteTreeItem) ((EnsTreeItem) node)
										.getItemObject()).getChilds(),
								((EnsTreeItem) node)));
			}
			return ((EnsTreeItem) node).getChilds();
		}

		if (((EnsTreeItem) node).getEoObject() instanceof EOActivite) {
			if (((EnsTreeItem) node).isNotChilds()) {
				((EnsTreeItem) node)
						.setChilds(EnsTreeItem.buildEnsTreeItemArray(
								FinderVoeux.getVoeuxForActiviteOrEcAndYear(
										((EnsTreeItem) node).getEoObject()
												.editingContext(),
										(EOActivite) ((EnsTreeItem) node)
												.getEoObject(), null,
										((EnsTreeItem) node).getSelectedYear()),
								((EnsTreeItem) node)));
			}
			return ((EnsTreeItem) node).getChilds();

		}

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

		if (((EnsTreeItem) node).isNotChilds()) {

			NSMutableArray<EnsTreeItem> retour = null;

			if (((EnsTreeItem) node).getEoObject() != null) {
				//recherche si activité sur l'object
				NSArray<EOActivite> activites = FinderActivite
						.getActivitesForObjectAndYear(((EnsTreeItem) node)
								.getEoObject().editingContext(),
								((EnsTreeItem) node).getEoObject(),
								((EnsTreeItem) node).getSelectedYear());

				if ((activites != null) && (activites.size() > 0)) {
					//il y a des activités, on fait un noeud activité
					EnsTreeItem ensItemActiv = new EnsTreeItem(
							((EnsTreeItem) node).treeDelegate(),
							((EnsTreeItem) node).getEoObject(),
							new ActiviteTreeItem("Activités", activites),
							((EnsTreeItem) node),
							((EnsTreeItem) node).getSelectedYear());
					retour = new NSMutableArray<EnsTreeItem>();
					retour.addObject(ensItemActiv);
					//retour.addObjectsFromArray(EnsTreeItem.buildEnsTreeItemArray(activites,((EnsTreeItem) node)));
				}

				NSArray<EnsTreeItem> superArray = super.childrenTreeNodes(node);
				if (superArray != null) {
					if (retour != null) {
						retour.addObjectsFromArray(superArray);
					} else {
						retour = new NSMutableArray<EnsTreeItem>(superArray);
					}
				}
			}
			((EnsTreeItem) node).setChilds(retour);
			return ((EnsTreeItem) node).getChilds();
		}
		return super.childrenTreeNodes(node);
	}

	@SuppressWarnings("unchecked")
	@Override
	public NSArray<EOGenericRecord> getLstObjFromSearch() {

		EOQualifier qualifier = EOQualifier.qualifierWithQualifierFormat(
				"(("
						+ EOActivite.COMMENTAIRE_KEY
						+ " caseInsensitiveLike %s) OR ("
						+ ERXQ.keyPath(EOActivite.TO_TYPE_ACTIVITE_KEY,
								EOTypeActivite.LIB_COURT_KEY)
						+ " caseInsensitiveLike %s) OR ("
						+ ERXQ.keyPath(EOActivite.TO_TYPE_ACTIVITE_KEY,
								EOTypeActivite.LIB_LONG_KEY)
						+ " caseInsensitiveLike %s)) AND "
						+ EOActivite.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY
						+ " = %@ ", new NSArray<Object>(new Object[] {
						"*" + getLibelleSearch() + "*",
						"*" + getLibelleSearch() + "*",
						"*" + getLibelleSearch() + "*", getSelectedYear() }));

		return (NSArray<EOGenericRecord>) EOActivite.fetchActivites(
				getEc(),
				qualifier,
				new NSArray<EOSortOrdering>(new EOSortOrdering(ERXQ.keyPath(
						EOActivite.TO_TYPE_ACTIVITE_KEY,
						EOTypeActivite.LIB_COURT_KEY),
						EOSortOrdering.CompareCaseInsensitiveAscending)))
				.clone();

	}

	@Override
	public NSArray<CktlAjaxTableViewColumn> getColonnes() {
		ActiviteTbV tbv = new ActiviteTbV(
				ERXWOContext.currentContext());

		return tbv.getColonnes();

	}

	@Override
	public String getLibSearchField() {
		return "Activité ou type activité contient";
	}

	@Override
	public String getLibelle(Object eoObject) {
		if (eoObject instanceof EOActivite) {
			return ((EOActivite) eoObject).getLibelle() + " : "
					+ ((EOActivite) eoObject).nbHeuresActivite() + "h";
		}
		if (eoObject instanceof ActiviteTreeItem) {
			return ((ActiviteTreeItem) eoObject).getLibelle();
		}
		if (eoObject instanceof EOVoeux) {
			EOVoeux voeux = ((EOVoeux) eoObject);
			return voeux.toFwkpers_IndividuEnseignant().getNomPrenomAffichage()
					+ " : " + voeux.nbHeuresVoeux();
		}
		String retour = super.getLibelle(eoObject);

		return retour;
	}

}
