package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteRepartitionAp;
import org.cocktail.scolaritemodulesfwk.serveur.components.APTableView;
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

public class ChooseAPEditVoeuxDelegateCtr extends EnseignementsTreeDelegate {

	

	public ChooseAPEditVoeuxDelegateCtr() {
		// TODO Auto-generated constructor stub
	}

	public ChooseAPEditVoeuxDelegateCtr(EOEditingContext ec,
			EOScolFormationAnnee selectedYear) {
		super(ec, selectedYear);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isLink() {
		return (treeItem().getEoObject() instanceof EOScolMaquetteAp);
	}

	@Override
	public NSArray<EnsTreeItem> childrenTreeNodes(Object node) {
		
		/*
		if ((gUser()!=null)&&(!gUser().canSelectAllMaquette())){
			NSArray<EnsTreeItem> tmp = super.childrenTreeNodes(node);
			if (tmp!=null) {
				NSMutableArray<EnsTreeItem> retour = new NSMutableArray<EnsTreeItem>();
				for (EnsTreeItem item : tmp){
					if ((item.getEoObject()!=null)&&(gUser().haveRightForObjMaquette(item.getEoObject()))){
						retour.addObject(item);
					}
				}
				((EnsTreeItem) node).setChilds(retour);
			} else {
				((EnsTreeItem) node).setChilds(tmp);
			}
		}
		
		if ((goodDiplomes != null) && (goodDiplomes.size() > 0)) {
			if (((EnsTreeItem) node).getEoObject() instanceof EOScolFormationDiscipline) {
				if (((EnsTreeItem) node).isNotChilds()) {
					((EnsTreeItem) node).setChilds(getGoodObject(null,
							super.childrenTreeNodes(node)));
				}
				return ((EnsTreeItem) node).getChilds();
			}

			if (((EnsTreeItem) node).getEoObject() instanceof EOScolFormationDiplome) {
				if (((EnsTreeItem) node).isNotChilds()) {
					((EnsTreeItem) node)
							.setChilds(getGoodObject(
									EOScolFormationSpecialisation.TO_FWK_SCOLARITE__SCOL_FORMATION_DIPLOME_KEY,
									super.childrenTreeNodes(node)));
				}
				return ((EnsTreeItem) node).getChilds();
			}
			if (((EnsTreeItem) node).getEoObject() instanceof EOScolFormationSpecialisation) {
				if (((EnsTreeItem) node).isNotChilds()) {
					NSArray<EnsTreeItem> tmpLst = super.childrenTreeNodes(node);
					if (tmpLst != null) {
						if (tmpLst.size() > 0) {
							if (tmpLst.lastObject().getEoObject() instanceof EOScolMaquetteParcours) {
								((EnsTreeItem) node)
										.setChilds(getGoodObject(
												ERXQ.keyPath(
														EOScolMaquetteParcours.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,
														EOScolFormationSpecialisation.TO_FWK_SCOLARITE__SCOL_FORMATION_DIPLOME_KEY),
												tmpLst));
							}
							if (tmpLst.lastObject().getEoObject() instanceof EOScolMaquetteSemestre) {
								((EnsTreeItem) node)
										.setChilds(getGoodObject(
												ERXQ.keyPath(
														EOScolMaquetteSemestre.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_SEMS_KEY,
														EOScolMaquetteRepartitionSem.TO_FWK_SCOLARITE__SCOL_MAQUETTE_PARCOURS_KEY,
														EOScolMaquetteParcours.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,
														EOScolFormationSpecialisation.TO_FWK_SCOLARITE__SCOL_FORMATION_DIPLOME_KEY),
												tmpLst));
							}
						}

					}
				}
				return ((EnsTreeItem) node).getChilds();
			}

			if (((EnsTreeItem) node).getEoObject() instanceof EOScolMaquetteParcours) {
				if (((EnsTreeItem) node).isNotChilds()) {
					((EnsTreeItem) node)
							.setChilds(getGoodObject(
									ERXQ.keyPath(
											EOScolMaquetteSemestre.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_SEMS_KEY,
											EOScolMaquetteRepartitionSem.TO_FWK_SCOLARITE__SCOL_MAQUETTE_PARCOURS_KEY,
											EOScolMaquetteParcours.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,
											EOScolFormationSpecialisation.TO_FWK_SCOLARITE__SCOL_FORMATION_DIPLOME_KEY),
									super.childrenTreeNodes(node)));
				}
				return ((EnsTreeItem) node).getChilds();
			}

			if (((EnsTreeItem) node).getEoObject() instanceof EOScolMaquetteSemestre) {
				if (((EnsTreeItem) node).isNotChilds()) {
					((EnsTreeItem) node)
							.setChilds(getGoodObject(
									ERXQ.keyPath(
											EOScolMaquetteUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_UES_KEY,
											EOScolMaquetteRepartitionUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_SEMESTRE_KEY,
											EOScolMaquetteSemestre.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_SEMS_KEY,
											EOScolMaquetteRepartitionSem.TO_FWK_SCOLARITE__SCOL_MAQUETTE_PARCOURS_KEY,
											EOScolMaquetteParcours.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,
											EOScolFormationSpecialisation.TO_FWK_SCOLARITE__SCOL_FORMATION_DIPLOME_KEY),
									super.childrenTreeNodes(node)));
				}
				return ((EnsTreeItem) node).getChilds();
			}

			if (((EnsTreeItem) node).getEoObject() instanceof EOScolMaquetteUe) {
				if (((EnsTreeItem) node).isNotChilds()) {
					((EnsTreeItem) node)
							.setChilds(getGoodObject(
									ERXQ.keyPath(
											EOScolMaquetteEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_ECS_KEY,
											EOScolMaquetteRepartitionEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_UE_KEY,
											EOScolMaquetteUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_UES_KEY,
											EOScolMaquetteRepartitionUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_SEMESTRE_KEY,
											EOScolMaquetteSemestre.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_SEMS_KEY,
											EOScolMaquetteRepartitionSem.TO_FWK_SCOLARITE__SCOL_MAQUETTE_PARCOURS_KEY,
											EOScolMaquetteParcours.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,
											EOScolFormationSpecialisation.TO_FWK_SCOLARITE__SCOL_FORMATION_DIPLOME_KEY),
									super.childrenTreeNodes(node)));
				}
				return ((EnsTreeItem) node).getChilds();
			}

			if (((EnsTreeItem) node).getEoObject() instanceof EOScolMaquetteEc) {
				if (((EnsTreeItem) node).isNotChilds()) {
					((EnsTreeItem) node)
							.setChilds(getGoodObject(
									ERXQ.keyPath(
											EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_APS_KEY,
											EOScolMaquetteRepartitionAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY,
											EOScolMaquetteEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_ECS_KEY,
											EOScolMaquetteRepartitionEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_UE_KEY,
											EOScolMaquetteUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_UES_KEY,
											EOScolMaquetteRepartitionUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_SEMESTRE_KEY,
											EOScolMaquetteSemestre.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_SEMS_KEY,
											EOScolMaquetteRepartitionSem.TO_FWK_SCOLARITE__SCOL_MAQUETTE_PARCOURS_KEY,
											EOScolMaquetteParcours.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,
											EOScolFormationSpecialisation.TO_FWK_SCOLARITE__SCOL_FORMATION_DIPLOME_KEY),
									super.childrenTreeNodes(node)));
				}
				return ((EnsTreeItem) node).getChilds();
			}

		}
		//*/
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

		return super.childrenTreeNodes(node);
	}

	/**
	 * filtre les objets retournes selon la liste des diplomes "goodDiplomes" si
	 * renseignée. La liste goodDiplomes doit contenir les diplomes pour les
	 * quels l'utilisateur connecté à les droits.
	 * 
	 * @param pathToDiplome
	 * @param tmp
	 * @return
	 */
	/*
	private NSArray<EnsTreeItem> getGoodObject(String pathToDiplome,
			NSArray<EnsTreeItem> tmp) {
		if (tmp == null) {
			return tmp;
		}
		if ((goodDiplomes != null) && (goodDiplomes.size() > 0)) {
			EOEditingContext ec = goodDiplomes.lastObject().editingContext();
			NSMutableArray<EnsTreeItem> retour = new NSMutableArray<EnsTreeItem>();
			for (EnsTreeItem itemOccur : tmp) {
				Object wObj = ((pathToDiplome != null) ? itemOccur
						.getEoObject().valueForKeyPath(pathToDiplome)
						: itemOccur.getEoObject());
				if (wObj instanceof EOGenericRecord) {
					if (goodDiplomes.contains(EOUtilities
							.localInstanceOfObject(ec, (EOGenericRecord) wObj))) {
						if (!retour.contains(itemOccur)) {
							retour.add(itemOccur);
						}
					}
				}

				if (wObj instanceof NSArray) {
					@SuppressWarnings("unchecked")
					NSArray<EOScolFormationDiplome> dipls = getDiplomesInArray(
							(NSArray<Object>) wObj,
							new NSMutableArray<EOScolFormationDiplome>());
					for (EOScolFormationDiplome dipOccur : dipls) {

						if (goodDiplomes.contains(EOUtilities
								.localInstanceOfObject(ec, dipOccur))) {
							if (!retour.contains(itemOccur)) {
								retour.add(itemOccur);
								break;
							}
						}
					}
				}
			}
			return retour.immutableClone();
		} else {
			return tmp;
		}
	}
	//*/

	/**
	 * retourne un nsarray de diplomes a partir d'un tableau contenant soit des
	 * diplomes soit des nsarray
	 **/
	/*
	@SuppressWarnings("unchecked")
	private NSArray<EOScolFormationDiplome> getDiplomesInArray(
			NSArray<Object> lst, NSMutableArray<EOScolFormationDiplome> retour) {
		for (Object item : lst) {
			while ((item instanceof NSArray)) {
				return getDiplomesInArray((NSArray<Object>) item, retour);
			}
			if (item instanceof EOScolFormationDiplome) {
				retour.addObject((EOScolFormationDiplome) item);
			}
			return retour;
		}
		return null;
	}
	//*/
	@SuppressWarnings("unchecked")
	@Override
	public NSArray<EOGenericRecord> getLstObjFromSearch() {

		EOQualifier qualifier = EOQualifier
				.qualifierWithQualifierFormat(
						"(("
								+ EOScolMaquetteAp.MAP_LIBELLE_KEY
								+ " caseInsensitiveLike %s) OR ("
								+ ERXQ.keyPath(
										EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_APS_KEY,
										EOScolMaquetteRepartitionAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY,
										EOScolMaquetteEc.MEC_CODE_KEY)
								+ " caseInsensitiveLike %s) OR ("
								+ ERXQ.keyPath(
										EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_APS_KEY,
										EOScolMaquetteRepartitionAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY,
										EOScolMaquetteEc.MEC_LIBELLE_KEY)
								+ " caseInsensitiveLike %s) OR ("
								+ ERXQ.keyPath(
										EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_APS_KEY,
										EOScolMaquetteRepartitionAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY,
										EOScolMaquetteEc.MEC_LIBELLE_COURT_KEY)
								+ " caseInsensitiveLike %s)) AND "
								+ EOScolMaquetteEc.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY
								+ " = %@ ", new NSArray<Object>(new Object[] {
								"*" + getLibelleSearch() + "*",
								"*" + getLibelleSearch() + "*",
								"*" + getLibelleSearch() + "*",
								"*" + getLibelleSearch() + "*",
								getSelectedYear() }));
		
			return EOScolMaquetteAp.fetchAll(getEc(), qualifier,
					new NSArray<EOSortOrdering>(new EOSortOrdering(
							EOScolMaquetteAp.MAP_LIBELLE_KEY,
							EOSortOrdering.CompareCaseInsensitiveAscending)),
					true);

	}

	@Override
	public NSArray<CktlAjaxTableViewColumn> getColonnes() {
		APTableView tbv = new APTableView(ERXWOContext.currentContext());
		return tbv.getColonnes();

	}

	@Override
	public String getLibSearchField() {
		return "AP ou EC contient";
	}

	@Override
	public String getLibelle(Object eoObject) {
		if (eoObject instanceof EOVoeux) {
			EOVoeux voeux = ((EOVoeux) eoObject);
			return voeux.toFwkpers_IndividuEnseignant().getNomPrenomAffichage()
					+ " : " + voeux.nbHeuresVoeux();
		}
		return super.getLibelle(eoObject);
	}

}
