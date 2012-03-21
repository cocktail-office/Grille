package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDomaine;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationGrade;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class FilteredDiplomePickerDelegate extends DiplomePickerDefaultDelegate {

	protected boolean editRight;
	
	public FilteredDiplomePickerDelegate(EOEditingContext ec,
			EOScolFormationAnnee year,boolean editRight) {
		super(ec, year);
		this.editRight = editRight;
	}

	private <T extends EOGenericRecord> NSArray<T> getUserAuthorisedObjs(
			NSArray<T> tmp) {
		if ((gUser() != null) && (!gUser().canSelectAllMaquette())) {
			NSMutableArray<T> retour = new NSMutableArray<T>();
			for (T grd : tmp) {
				if (gUser().haveRightForObjMaquette(grd, getYear(), editRight)) {
					retour.addObject(grd);
				}
			}
			return retour;
		} else {
			return tmp;
		}
	}

	@Override
	public NSArray<EOScolFormationDomaine> lstDomaines() {
		if (gUser().haveAdminRight()) {
			return super.lstDomaines();
		} else {
			if ((editRight)&&(gUser().isGrilleurSpec(getYear()))) {
				NSMutableArray<EOScolFormationDomaine> retour = new NSMutableArray<EOScolFormationDomaine>();
				if (gUser().isGrilleurSpec(getYear())) {
					for (EOScolFormationResponsabilite resp : gUser()
							.scolFormationResponsabilitesGrilleurForYear(
									getYear())) {
						if (!retour.contains(resp
								.toFwkScolarite_ScolFormationSpecialisation()
								.toFwkScolarite_ScolFormationDiplome()
								.toFwkScolarite_ScolFormationDomaine())) {
							retour.add(resp
									.toFwkScolarite_ScolFormationSpecialisation()
									.toFwkScolarite_ScolFormationDiplome()
									.toFwkScolarite_ScolFormationDomaine());
						}
					}
				}
				return retour;
			} else {
				return getUserAuthorisedObjs(super.lstDomaines());
			}
		}
	}

	@Override
	public NSArray<EOScolFormationGrade> lstGrades(
			EOScolFormationDomaine selectedDomaine) {
		if (gUser().haveAdminRight()) {
			return super.lstGrades(selectedDomaine);
		} else {
			if (gUser().isGrilleurSpec(getYear())) {
				NSMutableArray<EOScolFormationGrade> retour = new NSMutableArray<EOScolFormationGrade>();
				if (gUser().isGrilleurSpec(getYear())) {
					for (EOScolFormationResponsabilite resp : gUser()
							.scolFormationResponsabilitesGrilleurForYear(
									getYear())) {
						if (!retour.contains(resp
								.toFwkScolarite_ScolFormationSpecialisation()
								.toFwkScolarite_ScolFormationDiplome()
								.toFwkScolarite_ScolFormationGrade())) {
							retour.add(resp
									.toFwkScolarite_ScolFormationSpecialisation()
									.toFwkScolarite_ScolFormationDiplome()
									.toFwkScolarite_ScolFormationGrade());
						}
					}
				}
				return retour;
			} else {
				return getUserAuthorisedObjs(super.lstGrades(selectedDomaine));
			}
		}
	}

	@Override
	public NSArray<EOScolFormationDiplome> lstDiplomes(
			EOScolFormationDomaine selectedDomaine) {		
		return getUserAuthorisedObjs(super.lstDiplomes(selectedDomaine));
	}

	@Override
	public NSArray<EOScolFormationDiplome> lstDiplomes(
			EOScolFormationDomaine selectedDomaine,
			EOScolFormationGrade selectedGrade) {
		return getUserAuthorisedObjs(super.lstDiplomes(selectedDomaine,
				selectedGrade));
	}

	@Override
	public NSArray<EOScolFormationDiplome> lstDiplomes() {
		return getUserAuthorisedObjs(super.lstDiplomes());
	}

	@Override
	public NSArray<EOScolFormationSpecialisation> lstSpecForDipl(
			EOScolFormationDiplome diplome) {

		return getUserAuthorisedObjs(super.lstSpecForDipl(diplome));

	}

	private GrilleApplicationUser gUser;

	public GrilleApplicationUser gUser() {
		return gUser;
	}

	public void setgUser(GrilleApplicationUser gUser) {
		this.gUser = gUser;
	}

}
