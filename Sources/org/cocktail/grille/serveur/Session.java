/*
 * Copyright Cocktail, 2001-2008
 * 
 * This software is governed by the CeCILL license under French law and abiding
 * by the rules of distribution of free software. You can use, modify and/or
 * redistribute the software under the terms of the CeCILL license as circulated
 * by CEA, CNRS and INRIA at the following URL "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package org.cocktail.grille.serveur;

import org.cocktail.fwkcktlajaxwebext.serveur.CocktailAjaxSession;
import org.cocktail.fwkcktlpersonne.common.PersonneApplicationUser;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grille.serveur.components.ChooseAPEditVoeuxDelegateCtr;
import org.cocktail.grille.serveur.components.ChooseActivEcDelegateCtr;
import org.cocktail.grille.serveur.components.ChooseActivParcoursDelegateCtr;
import org.cocktail.grille.serveur.components.ChooseActivSemestreDelegateCtr;
import org.cocktail.grille.serveur.components.ChooseActivSpecialisationDelegateCtr;
import org.cocktail.grille.serveur.components.ChooseActivUeDelegateCtr;
import org.cocktail.grille.serveur.components.ChooseActiviteDelegateCtr;
import org.cocktail.grille.serveur.components.ConsultArbreDelegateCtr;
import org.cocktail.grille.serveur.components.FilteredChooseAPEditVoeuxDelegateCtr;
import org.cocktail.grille.serveur.components.FilteredChooseActiviteDelegateCtr;
import org.cocktail.grille.serveur.components.FilteredDiplomePickerDelegate;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.finder.FinderMaquetteHoraireCode;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteHoraireCode;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteSemestre;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseApDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseEcDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXThreadStorage;

public class Session extends CocktailAjaxSession {
	private static final long serialVersionUID = 1L;
	protected EOScolFormationAnnee selectedYear;

	protected EOScolFormationAnnee currentYear;

	protected Enseignant selectedEns;

	protected EOScolMaquetteEc selectedEc;

	protected EOActivite selectedActiv;

	private GrilleApplicationUser gUser;

	public Session() {

	}

	public EOScolFormationAnnee selectedYear() {
		if (selectedYear == null)
			setSelectedYear(currentYear());
		return selectedYear;
	}

	public void setSelectedYear(EOScolFormationAnnee selectedYear) {
		this.selectedYear = selectedYear;
		if ((getSelectedEns() != null)
				&& (!getSelectedEns().getYear().equals(selectedYear))) {
			if (Enseignant.isEnseignant(defaultEditingContext(), selectedYear,
					getSelectedEns().getPersId())) {
				getSelectedEns().setYear(selectedYear);
			} else {
				setSelectedEns(null);
			}
		}

		if ((getSelectedSemestre() != null)
				&& (!getSelectedSemestre().toFwkScolarite_ScolFormationAnnee()
						.equals(selectedYear))) {
			setSelectedSemestre(null);
		}

		if ((currentChooseActivEcDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseActivEcDelegateCtr
						.getSelectedYear()))) {
			currentChooseActivEcDelegateCtr = null;
		}
		if ((currentChooseActiviteDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseActiviteDelegateCtr
						.getSelectedYear()))) {
			currentChooseActiviteDelegateCtr = null;
		}
		if ((currentChooseActivParcoursDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseActivParcoursDelegateCtr
						.getSelectedYear()))) {
			currentChooseActivParcoursDelegateCtr = null;
		}
		if ((currentChooseActivSemestreDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseActivSemestreDelegateCtr
						.getSelectedYear()))) {
			currentChooseActivSemestreDelegateCtr = null;
		}
		if ((currentChooseActivSpecialisationDelegateCtr != null)
				&& (!selectedYear
						.equals(currentChooseActivSpecialisationDelegateCtr
								.getSelectedYear()))) {
			currentChooseActivSpecialisationDelegateCtr = null;
		}
		if ((currentChooseActivUeDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseActivUeDelegateCtr
						.getSelectedYear()))) {
			currentChooseActivUeDelegateCtr = null;
		}
		if ((currentChooseApDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseApDelegateCtr
						.getSelectedYear()))) {
			currentChooseApDelegateCtr = null;
		}
		if ((currentChooseAPEditVoeuxDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseAPEditVoeuxDelegateCtr
						.getSelectedYear()))) {
			currentChooseAPEditVoeuxDelegateCtr = null;
		}
		if ((currentChooseEcDelegateCtr != null)
				&& (!selectedYear.equals(currentChooseEcDelegateCtr
						.getSelectedYear()))) {
			currentChooseEcDelegateCtr = null;
		}
		if ((currentConsArbreDelegate != null)
				&& (!selectedYear.equals(currentConsArbreDelegate
						.getSelectedYear()))) {
			currentConsArbreDelegate = null;
		}
		if ((currentDiplomePickerDelegate != null)
				&& (!selectedYear
						.equals(currentDiplomePickerDelegate.getYear()))) {
			currentDiplomePickerDelegate = null;
		}
		if ((currentFiletedChooseActivUeDelegateCtr != null)
				&& (!selectedYear.equals(currentFiletedChooseActivUeDelegateCtr
						.getSelectedYear()))) {
			currentFiletedChooseActivUeDelegateCtr = null;
		}
		if ((currentFilteredChooseActiviteDelegateCtr != null)
				&& (!selectedYear
						.equals(currentFilteredChooseActiviteDelegateCtr
								.getSelectedYear()))) {
			currentFilteredChooseActiviteDelegateCtr = null;
		}
		if ((currentFilteredDiplomePickerDelegate != null)
				&& (!selectedYear.equals(currentFilteredDiplomePickerDelegate
						.getYear()))) {
			currentFilteredDiplomePickerDelegate = null;
		}
	}

	private EOScolMaquetteSemestre selectedSemestre;

	public EOScolMaquetteSemestre getSelectedSemestre() {
		return selectedSemestre;
	}

	public void setSelectedSemestre(EOScolMaquetteSemestre selSemestre) {
		this.selectedSemestre = selSemestre;
	}

	public EOScolFormationAnnee currentYear() {
		if (currentYear == null)
			currentYear = FinderScolFormationAnnee
					.getScolFormationAnneeCourante(defaultEditingContext());
		return currentYear;
	}

	public void setCurrentYear(EOScolFormationAnnee currentYear) {
		this.currentYear = currentYear;
	}

	public Enseignant getSelectedEns() {
		if ((selectedEns == null) && (getGUser() != null)
				&& (getGUser().isEnseignant(selectedYear()))) {
			setIndividuForEnseigant(getGUser().individu());
		}
		return selectedEns;
	}

	public void setSelectedEns(Enseignant selectedEns) {
		this.selectedEns = selectedEns;
	}

	public EOIndividu getIndividuForEnseigant() {
		if (selectedEns != null) {
			return selectedEns.getIndividu();
		}
		return null;
	}

	public void resetVoeuxEnseignant() {
		if (getSelectedEns() != null) {
			getSelectedEns().resetTotalVoeux();
		}
	}

	public void setIndividuForEnseigant(EOIndividu indiv) {
		if ((indiv != null)
				&& (Enseignant.isEnseignant(defaultEditingContext(),
						selectedYear(), indiv.persId()))) {
			if ((selectedEns == null)
					|| ((selectedEns != null) && (!selectedEns.getIndividu()
							.persId().equals(indiv.persId())))) {
				selectedEns = new Enseignant(selectedYear(), indiv);
			}
			if ((selectedEns != null)
					&& (selectedEns.getIndividu().persId().equals(indiv
							.persId()))) {
				selectedEns.setYear(selectedYear());
			}
		}
		if (indiv==null) {
			selectedEns=null;
		}
	}

	public EOScolMaquetteEc getSelectedEc() {
		if ((selectedEc != null)
				&& (selectedYear() != null)
				&& (!selectedEc.toFwkScolarite_ScolFormationAnnee().equals(
						selectedYear()))) {
			//recherche de l'EC pour la nouvelle année en cours
			@SuppressWarnings("unchecked")
			NSArray<EOScolMaquetteEc> tmpEc = EOScolMaquetteEc
					.fetchAll(
							selectedEc.editingContext(),
							EOQualifier
									.qualifierWithQualifierFormat(
											EOScolMaquetteEc.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY
													+ "=%@ AND "
													+ EOScolMaquetteEc.MEC_CODE_KEY
													+ "=%@",
											new NSArray<Object>(new Object[] {
													selectedYear(),
													selectedEc.mecCode() })));
			if ((tmpEc != null) && (tmpEc.size() > 0)) {
				setSelectedEc(tmpEc.lastObject());
			} else {
				setSelectedEc(null);
			}
		}
		return selectedEc;
	}

	public void setSelectedEc(EOScolMaquetteEc selectedEc) {
		this.selectedEc = selectedEc;
	}

	public GrilleApplicationUser getGUser() {
		return gUser;
	}

	public void setGUser(GrilleApplicationUser user) {
		gUser = user;
	}

	@Override
	public void awake() {
		super.awake();
		if (getGUser() != null)
			ERXThreadStorage.takeValueForKey(getGUser().getPersId(),
					PersonneApplicationUser.PERS_ID_CURRENT_USER_STORAGE_KEY);
	}

	public EOActivite getSelectedActiv() {
		if ((selectedActiv != null)
				&& (selectedYear() != null)
				&& (!selectedActiv.toFwkScolarite_ScolFormationAnnee().equals(
						selectedYear()))) {
			selectedActiv = null;
		}
		return selectedActiv;
	}

	public void setSelectedActiv(EOActivite selectedActiv) {
		this.selectedActiv = selectedActiv;
	}

	protected ConsultArbreDelegateCtr currentConsArbreDelegate;

	public ConsultArbreDelegateCtr currentConsArbreDelegate() {
		if (currentConsArbreDelegate == null) {
			currentConsArbreDelegate = new ConsultArbreDelegateCtr(
					defaultEditingContext(), selectedYear());
		}
		return currentConsArbreDelegate;
	}

	public void setCurrentConsArbreDelegate(
			ConsultArbreDelegateCtr consArbreDelegate) {
		this.currentConsArbreDelegate = consArbreDelegate;
	}

	protected DiplomePickerDefaultDelegate currentDiplomePickerDelegate;

	public DiplomePickerDefaultDelegate currentDiplomePickerDelegate() {
		if (currentDiplomePickerDelegate == null) {
			currentDiplomePickerDelegate = new DiplomePickerDefaultDelegate(
					defaultEditingContext(), selectedYear());
		}
		return currentDiplomePickerDelegate;
	}

	public void setCurrentDiplomePickerDelegate(
			DiplomePickerDefaultDelegate currentDiplomePickerDelegate) {
		this.currentDiplomePickerDelegate = currentDiplomePickerDelegate;
	}

	protected FilteredDiplomePickerDelegate currentFilteredDiplomePickerDelegate;

	public FilteredDiplomePickerDelegate currentFilteredDiplomePickerDelegate() {
		if (currentFilteredDiplomePickerDelegate == null) {
			currentFilteredDiplomePickerDelegate = new FilteredDiplomePickerDelegate(
					defaultEditingContext(), selectedYear(),false);
			currentFilteredDiplomePickerDelegate.setgUser(getGUser());
		}
		return currentFilteredDiplomePickerDelegate;
	}

	public void setCurrentFilteredDiplomePickerDelegate(
			FilteredDiplomePickerDelegate currentFilteredDiplomePickerDelegate) {
		this.currentFilteredDiplomePickerDelegate = currentFilteredDiplomePickerDelegate;
	}
	
	protected FilteredDiplomePickerDelegate currentFilteredDiplomePickerEditRightDelegate;

	public FilteredDiplomePickerDelegate currentFilteredDiplomePickerEditRightDelegate() {
		if (currentFilteredDiplomePickerEditRightDelegate == null) {
			currentFilteredDiplomePickerEditRightDelegate = new FilteredDiplomePickerDelegate(
					defaultEditingContext(), selectedYear(),true);
			currentFilteredDiplomePickerEditRightDelegate.setgUser(getGUser());
		}
		return currentFilteredDiplomePickerEditRightDelegate;
	}

	public void setCurrentFilteredDiplomePickerEditRightDelegate(
			FilteredDiplomePickerDelegate currentFilteredDiplomePickerDelegate) {
		this.currentFilteredDiplomePickerEditRightDelegate = currentFilteredDiplomePickerDelegate;
	}

	protected ChooseEcDelegateCtr currentChooseEcDelegateCtr;

	public ChooseEcDelegateCtr currentChooseEcDelegateCtr() {
		return currentChooseEcDelegateCtr;
	}

	public void setCurrentChooseEcDelegateCtr(
			ChooseEcDelegateCtr currentChooseEcDelegateCtr) {
		this.currentChooseEcDelegateCtr = currentChooseEcDelegateCtr;
	}

	protected ChooseApDelegateCtr currentChooseApDelegateCtr;

	public ChooseApDelegateCtr currentChooseApDelegateCtr() {
		return currentChooseApDelegateCtr;
	}

	public void setCurrentChooseApDelegateCtr(
			ChooseApDelegateCtr currentChooseApDelegateCtr) {
		this.currentChooseApDelegateCtr = currentChooseApDelegateCtr;
	}

	protected ChooseActivParcoursDelegateCtr currentChooseActivParcoursDelegateCtr;

	public ChooseActivParcoursDelegateCtr currentChooseActivParcoursDelegateCtr() {
		if (currentChooseActivParcoursDelegateCtr == null) {
			currentChooseActivParcoursDelegateCtr = new ChooseActivParcoursDelegateCtr(
					defaultEditingContext(), selectedYear());
			currentChooseActivParcoursDelegateCtr.setgUser(getGUser());
		}
		return currentChooseActivParcoursDelegateCtr;
	}

	public void setCurrentChooseActivParcoursDelegateCtr(
			ChooseActivParcoursDelegateCtr currentChooseActivParcoursDelegateCtr) {
		this.currentChooseActivParcoursDelegateCtr = currentChooseActivParcoursDelegateCtr;
	}

	protected ChooseActivSemestreDelegateCtr currentChooseActivSemestreDelegateCtr;

	public ChooseActivSemestreDelegateCtr currentChooseActivSemestreDelegateCtr() {
		if (currentChooseActivSemestreDelegateCtr == null) {
			currentChooseActivSemestreDelegateCtr = new ChooseActivSemestreDelegateCtr(
					defaultEditingContext(), selectedYear());
			currentChooseActivSemestreDelegateCtr.setgUser(getGUser());
		}
		return currentChooseActivSemestreDelegateCtr;
	}

	public void setCurrentChooseActivSemestreDelegateCtr(
			ChooseActivSemestreDelegateCtr currentChooseActivSemestreDelegateCtr) {
		this.currentChooseActivSemestreDelegateCtr = currentChooseActivSemestreDelegateCtr;
	}

	protected ChooseActivUeDelegateCtr currentChooseActivUeDelegateCtr;

	public ChooseActivUeDelegateCtr currentChooseActivUeDelegateCtr() {
		if (currentChooseActivUeDelegateCtr == null) {
			currentChooseActivUeDelegateCtr = new ChooseActivUeDelegateCtr(
					defaultEditingContext(), selectedYear());
			currentChooseActivUeDelegateCtr.setgUser(getGUser());
		}
		return currentChooseActivUeDelegateCtr;
	}

	public void setCurrentChooseActivUeDelegateCtr(
			ChooseActivUeDelegateCtr currentChooseActivUeDelegateCtr) {
		this.currentChooseActivUeDelegateCtr = currentChooseActivUeDelegateCtr;
	}

	protected ChooseActivEcDelegateCtr currentChooseActivEcDelegateCtr;

	public ChooseActivEcDelegateCtr currentChooseActivEcDelegateCtr() {
		if (currentChooseActivEcDelegateCtr == null) {
			currentChooseActivEcDelegateCtr = new ChooseActivEcDelegateCtr(
					defaultEditingContext(), selectedYear());
			currentChooseActivEcDelegateCtr.setgUser(getGUser());
		}
		return currentChooseActivEcDelegateCtr;
	}

	public void setCurrentChooseActivEcDelegateCtr(
			ChooseActivEcDelegateCtr currentChooseActivEcDelegateCtr) {
		this.currentChooseActivEcDelegateCtr = currentChooseActivEcDelegateCtr;
	}

	protected ChooseActivSpecialisationDelegateCtr currentChooseActivSpecialisationDelegateCtr;

	public ChooseActivSpecialisationDelegateCtr currentChooseActivSpecialisationDelegateCtr() {
		if (currentChooseActivSpecialisationDelegateCtr == null) {
			currentChooseActivSpecialisationDelegateCtr = new ChooseActivSpecialisationDelegateCtr(
					defaultEditingContext(), selectedYear());
			currentChooseActivSpecialisationDelegateCtr.setgUser(getGUser());
		}
		return currentChooseActivSpecialisationDelegateCtr;
	}

	public void setCurrentChooseActivSpecialisationDelegateCtr(
			ChooseActivSpecialisationDelegateCtr currentChooseActivSpecialisationDelegateCtr) {
		this.currentChooseActivSpecialisationDelegateCtr = currentChooseActivSpecialisationDelegateCtr;
	}

	protected ChooseAPEditVoeuxDelegateCtr currentChooseAPEditVoeuxDelegateCtr;

	public ChooseAPEditVoeuxDelegateCtr currentChooseAPEditVoeuxDelegateCtr() {
		if (currentChooseAPEditVoeuxDelegateCtr == null) {
			currentChooseAPEditVoeuxDelegateCtr = new ChooseAPEditVoeuxDelegateCtr(
					defaultEditingContext(), selectedYear());
		}
		return currentChooseAPEditVoeuxDelegateCtr;
	}

	public void setCurrentChooseAPEditVoeuxDelegateCtr(
			ChooseAPEditVoeuxDelegateCtr currentChooseAPEditVoeuxDelegateCtr) {
		this.currentChooseAPEditVoeuxDelegateCtr = currentChooseAPEditVoeuxDelegateCtr;
	}

	protected FilteredChooseAPEditVoeuxDelegateCtr currentFiletedChooseActivUeDelegateCtr;

	public FilteredChooseAPEditVoeuxDelegateCtr currentFilteredChooseAPEditVoeuxDelegateCtr() {
		if (currentFiletedChooseActivUeDelegateCtr == null) {
			currentFiletedChooseActivUeDelegateCtr = new FilteredChooseAPEditVoeuxDelegateCtr(
					defaultEditingContext(), selectedYear());
			currentFiletedChooseActivUeDelegateCtr.setgUser(getGUser());
		}
		return currentFiletedChooseActivUeDelegateCtr;
	}

	public void setCurrentFilteredChooseActivUeDelegateCtr(
			FilteredChooseAPEditVoeuxDelegateCtr currentFileterdChooseActivUeDelegateCtr) {
		this.currentFiletedChooseActivUeDelegateCtr = currentFileterdChooseActivUeDelegateCtr;
	}

	protected ChooseActiviteDelegateCtr currentChooseActiviteDelegateCtr;

	public ChooseActiviteDelegateCtr currentChooseActiviteDelegateCtr() {
		if (currentChooseActiviteDelegateCtr == null) {
			currentChooseActiviteDelegateCtr = new ChooseActiviteDelegateCtr(
					defaultEditingContext(), selectedYear());
		}
		return currentChooseActiviteDelegateCtr;
	}

	public void setCurrentChooseActiviteDelegateCtr(
			ChooseActiviteDelegateCtr currentChooseActiviteDelegateCtr) {
		this.currentChooseActiviteDelegateCtr = currentChooseActiviteDelegateCtr;
	}

	protected FilteredChooseActiviteDelegateCtr currentFilteredChooseActiviteDelegateCtr;

	public FilteredChooseActiviteDelegateCtr currentFilteredChooseActiviteDelegateCtr() {
		if (currentFilteredChooseActiviteDelegateCtr == null) {
			currentFilteredChooseActiviteDelegateCtr = new FilteredChooseActiviteDelegateCtr(
					defaultEditingContext(), selectedYear());
			currentFilteredChooseActiviteDelegateCtr.setgUser(getGUser());
		}
		return currentFilteredChooseActiviteDelegateCtr;
	}

	public void setCurrentFilteredChooseActiviteDelegateCtr(
			FilteredChooseActiviteDelegateCtr currentFilteredChooseActiviteDelegateCtr) {
		this.currentFilteredChooseActiviteDelegateCtr = currentFilteredChooseActiviteDelegateCtr;
	}

	protected EOScolMaquetteHoraireCode mhcoCM;
	protected EOScolMaquetteHoraireCode mhcoTD;
	protected EOScolMaquetteHoraireCode mhcoTP;

	public EOScolMaquetteHoraireCode getMhcoCM() {
		if (mhcoCM == null) {
			mhcoCM = FinderMaquetteHoraireCode
					.getHoraireCodeCM(defaultEditingContext());
		}
		return mhcoCM;
	}

	public EOScolMaquetteHoraireCode getMhcoTD() {
		if (mhcoTD == null) {
			mhcoTD = FinderMaquetteHoraireCode
					.getHoraireCodeTD(defaultEditingContext());
		}
		return mhcoTD;
	}

	public EOScolMaquetteHoraireCode getMhcoTP() {
		if (mhcoTP == null) {
			mhcoTP = FinderMaquetteHoraireCode
					.getHoraireCodeTP(defaultEditingContext());
		}
		return mhcoTP;
	}

	public boolean isEnsWithServiceValided() {
		if (getGUser() == null) {
			return false;
		}
		if (selectedYear() == null) {
			return false;
		}
		return ((getGUser().isEnseignantWithServiceValidated(selectedYear())) && (getGUser()
				.isOnlyEnseignant(selectedYear())));
	}

}
