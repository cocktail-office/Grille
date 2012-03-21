package org.cocktail.grille.serveur.components;

import java.math.BigDecimal;
import java.util.Iterator;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.finder.FinderEnseignant;
import org.cocktail.grillefwk.serveur.metier.eof.EOOccupant;
import org.cocktail.grillefwk.serveur.metier.eof.EOPeriodicite;
import org.cocktail.grillefwk.serveur.metier.eof.EOReservation;
import org.cocktail.grillefwk.serveur.metier.eof.EOReservationAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteHoraireCode;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteRepartitionAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteRepartitionEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteRepartitionUe;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteSemestre;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteUe;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxModalDialog;
import er.extensions.eof.ERXQ;

public class ValidResaSuperPlan extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidResaSuperPlan(WOContext context) {
		super(context);
	}

	public String aucindividuid() {
		return getComponentId() + "_aucindividuid";
	}

	private EOIndividu selIndiv;

	public EOIndividu getSelIndiv() {
		return selIndiv;
	}

	public void setSelIndiv(EOIndividu selIndiv) {
		this.selIndiv = selIndiv;
	}

	public EOIndividu searchedIndiv() {
		return ((Session) session()).getIndividuForEnseigant();
	}

	public void setSearchedIndiv(EOIndividu searchedIndiv) {
		((Session) session()).setIndividuForEnseigant(searchedIndiv);
	}

	public String textButtonChoisirPersonne() {
		if (getSelIndiv() != null) {
			return "Choisir " + getSelIndiv().getNomAndPrenom();
		}
		return null;
	}

	public WOActionResults setSearchOn() {
		setSearchMode(true);
		AjaxModalDialog.open(context(), searchZoneId(), "Chercher un individu");

		return null;
	}

	private boolean searchMode = false;

	/**
	 * @return the searchMode
	 */
	public boolean searchMode() {
		return searchMode;
	}

	/**
	 * @param searchMode
	 *            the searchMode to set
	 */
	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}

	public String searchZoneId() {
		return getComponentId() + "_searchZoneId";
	}

	public String aucSearch() {
		return getComponentId() + "_aucSearch";
	}

	public WOActionResults clearSearchedInd() {
		setSearchedIndiv(null);
		return null;
	}

	public WOActionResults searchResa() {
		haveSearched = true;
		displayGroup().clearSelection();

		String qualStr = ERXQ.keyPath(
				EOReservationAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_AP_KEY,
				EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY)
				+ "=%@";
		NSMutableArray<Object> args = new NSMutableArray<Object>(
				new Object[] { ((Session) session()).selectedYear() });

		if (!"T".equals(searchValidHr)) {
			qualStr += " AND "
					+ ERXQ.keyPath(EOReservation.TO_PERIODICITES_KEY,
							EOPeriodicite.HCOMP_KEY) + " = %@";
			args.add(Integer.parseInt(searchValidHr));
		}

		if (debutSearch() != null) {
			qualStr += " AND "
					+ ERXQ.keyPath(EOReservation.TO_PERIODICITES_KEY,
							EOPeriodicite.DATE_DEB_KEY) + " => %@";
			args.add(debutSearch());
		}

		if (finSearch() != null) {
			qualStr += " AND "
					+ ERXQ.keyPath(EOReservation.TO_PERIODICITES_KEY,
							EOPeriodicite.DATE_FIN_KEY) + " <= %@";
			args.add(finSearch());
		}

		if (selSemestre() != null) {
			qualStr += " AND "
					+ ERXQ.keyPath(
							EOReservationAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_AP_KEY,
							EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_APS_KEY,
							EOScolMaquetteRepartitionAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY,
							EOScolMaquetteEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_ECS_KEY,
							EOScolMaquetteRepartitionEc.TO_FWK_SCOLARITE__SCOL_MAQUETTE_UE_KEY,
							EOScolMaquetteUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_UES_KEY,
							EOScolMaquetteRepartitionUe.TO_FWK_SCOLARITE__SCOL_MAQUETTE_SEMESTRE_KEY)
					+ "=%@";
			args.add(selSemestre());
		}
		if (searchedIndiv() != null) {
			qualStr += " AND "
					+ ERXQ.keyPath(EOReservationAp.TO_OCCUPANTS_KEY,
							EOOccupant.TO_FWKPERS__INDIVIDU_KEY) + "=%@";
			args.add(searchedIndiv());
		}

		if (searchedTypeAp != null) {
			qualStr += " AND "
					+ ERXQ.keyPath(
							EOReservationAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_AP_KEY,
							EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_HORAIRE_CODE_KEY)
					+ "=%@";
			args.add(searchedTypeAp);
		}

		NSArray<EOReservationAp> tmp = EOReservationAp
				.fetchDistinctReservationAps(
						session().defaultEditingContext(),
						EOQualifier.qualifierWithQualifierFormat(qualStr, args),
						null);

		displayGroup().setDelegate(null);
		dgResaOccup().setDelegate(null);
		dgPeriodes().setDelegate(null);

		// construction d'un array d'ApResaForTbV
		NSMutableArray<ApResaForTbV> tmpAp = new NSMutableArray<ApResaForTbV>();
		Iterator<EOReservationAp> it = EOSortOrdering
				.sortedArrayUsingKeyOrderArray(
						tmp,
						new NSArray<EOSortOrdering>(
								new EOSortOrdering(
										EOReservationAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_AP_KEY,
										EOSortOrdering.CompareAscending)))
				.iterator();
		EOReservationAp tmpResaAp = null;
		while (it.hasNext()) {
			if (tmpResaAp == null) {
				// c'est la 1ere occurance
				tmpResaAp = (EOReservationAp) it.next();
			}

			EOScolMaquetteAp ap = tmpResaAp.toFwkScolarite_ScolMaquetteAp();
			NSMutableArray<EOReservation> lstResa = new NSMutableArray<EOReservation>();
			while ((ap.equals(tmpResaAp.toFwkScolarite_ScolMaquetteAp()))
					&& (it.hasNext())) {
				if (!lstResa.contains(tmpResaAp.toReservation())) {
					lstResa.add(tmpResaAp.toReservation());
				}
				tmpResaAp = it.next();
			}
			if (lstResa.size() <= 0) {
				// pas de passage dans le while
				lstResa.add(tmpResaAp.toReservation());
			}
			tmpAp.add(new ApResaForTbV(ap, lstResa, debutSearch(), finSearch(),
					searchValidHr()));

		}
		displayGroup()
				.setObjectArray(
						EOSortOrdering
								.sortedArrayUsingKeyOrderArray(
										tmpAp,
										new NSArray<EOSortOrdering>(
												new EOSortOrdering[] {
														new EOSortOrdering(
																ERXQ.keyPath(
																		ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
																		EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_REPARTITION_APS_KEY,
																		EOScolMaquetteRepartitionAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY,
																		EOScolMaquetteEc.MEC_CODE_KEY),
																EOSortOrdering.CompareAscending),
														new EOSortOrdering(
																ERXQ.keyPath(
																		ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
																		EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_HORAIRE_CODE_KEY,
																		EOScolMaquetteHoraireCode.MHCO_ABREVIATION_KEY),
																EOSortOrdering.CompareAscending) })));

		// AjaxUpdateContainer.updateContainerWithID(erreurid(), context());
		displayGroup().setDelegate(dgDelegate());
		dgResaOccup().setObjectArray(null);
		dgPeriodes().setObjectArray(null);
		dgResaOccup().setDelegate(dgResaDelegate());
		dgPeriodes().setDelegate(dgPeriodesDelegate());
		displayGroup().clearSelection();
		return null;
	}

	public String auclstresaid() {
		return getComponentId() + "_auclstresaid";
	}

	public String aucselectlinkid() {
		return getComponentId() + "_aucselectlinkid";
	}

	public WOActionResults setSearchOff() {
		setSearchMode(false);
		setSearchedIndiv(getSelIndiv());
		AjaxModalDialog.close(context());
		return null;
	}

	public boolean haveSearched = false;

	public String aucselectionlinkFenetreid() {
		return getComponentId() + "_aucselectionlinkFenetreid";
	}

	/**
	 * @return the selSemestre
	 */
	public EOScolMaquetteSemestre selSemestre() {
		return ((Session) session()).getSelectedSemestre();
	}

	/**
	 * @param selSemestre
	 *            the selSemestre to set
	 */
	public void setSelSemestre(EOScolMaquetteSemestre selSemestre) {
		((Session) session()).setSelectedSemestre(selSemestre);
	}

	public ChooseActivSemestreDelegateCtr treeSemDelegate() {
		return ((Session) session()).currentChooseActivSemestreDelegateCtr();
	}

	public String cawsearchzoneid() {
		return getComponentId() + "_cawsearchzoneid";
	}

	public String aucsemestreid() {
		return getComponentId() + "_aucsemestreid";
	}

	public WOActionResults closeSearchWin() {

		AjaxModalDialog.close(context());
		return null;
	}

	public FilteredDiplomePickerDelegate diplomePickerDelegate() {
		return ((Session) session()).currentFilteredDiplomePickerDelegate();
	}

	private WODisplayGroup displayGroup;

	public WODisplayGroup displayGroup() {
		if (displayGroup == null) {
			displayGroup = new WODisplayGroup();
			displayGroup.setNumberOfObjectsPerBatch(10);
		}
		return displayGroup;
	}

	public void setDisplayGroup(WODisplayGroup displayGroup) {
		this.displayGroup = displayGroup;
	}

	private DgDelegate dgDelegate;

	public DgDelegate dgDelegate() {
		if (dgDelegate == null) {
			dgDelegate = new DgDelegate();
		}
		return dgDelegate;
	}

	public class DgDelegate {
		public void displayGroupDidChangeSelection(WODisplayGroup group) {
			setSelectedApResa((ApResaForTbV) group.selectedObject());
			dgResaOccup().clearSelection();
			dgPeriodes().clearSelection();
		}
	}

	private EOReservationAp selectedResa;

	public EOReservationAp selectedResa() {
		return selectedResa;
	}

	public void setSelectedResa(EOReservationAp selectedResa) {
		this.selectedResa = selectedResa;
		if (selectedResa == null) {
			dgResaOccup().setObjectArray(null);
			dgPeriodes().setObjectArray(null);
		} else {
			dgResaOccup().setDelegate(null);
			dgResaOccup().clearSelection();
			dgResaOccup().setObjectArray(selectedResa.toOccupants());
			dgResaOccup().setDelegate(dgResaDelegate());
			dgPeriodes().setObjectArray(null);
		}
	}

	private ApResaForTbV selectedApResa;

	public ApResaForTbV selectedApResa() {
		return selectedApResa;
	}

	public void setSelectedApResa(ApResaForTbV selectedAp) {
		this.selectedApResa = selectedAp;
		if (selectedAp == null) {
			dgResaOccup().setObjectArray(null);
			dgPeriodes().setObjectArray(null);
		} else {
			dgResaOccup().clearSelection();
			dgResaOccup().setDelegate(null);
			dgPeriodes().clearSelection();
			dgPeriodes().setObjectArray(null);
			dgPeriodes().setDelegate(null);

			@SuppressWarnings("unchecked")
			NSArray<EOReservation> tmp = selectedAp.getReservations();

			NSMutableArray<OccupantResaForTbV> tmpOccups = new NSMutableArray<OccupantResaForTbV>();

			for (EOReservation resa : tmp) {
				// parse des occupants
				for (EOOccupant occup : resa.toOccupants()) {
					// recherche si on a deja un OccupantResaForTbV pour cet
					// occupant
					OccupantResaForTbV occupResa = getOccupResaIn(tmpOccups,
							occup);
					if (occupResa == null) {
						occupResa = new OccupantResaForTbV(
								occup.toFwkpers_Individu(),
								new NSMutableArray<EOReservation>(),
								new BigDecimal(0));
						occupResa.setDebut(selectedApResa.getDebut());
						occupResa.setFin(selectedApResa.getFin());
						occupResa.setHrValide(selectedAp.getHrValide());
						occupResa.setApResa(selectedAp);
						tmpOccups.add(occupResa);
					}
					if (!occupResa.getReservations().contains(resa)) {
						occupResa.getReservations().add(resa);
						occupResa.setNbHeures(occupResa.getNbHeures().add(
								resa.totalHrPeriodicite(occupResa.getDebut(),
										occupResa.getFin())));
					}
				}
			}
			dgResaOccup()
					.setObjectArray(
							EOSortOrdering
									.sortedArrayUsingKeyOrderArray(
											tmpOccups,
											new NSArray<EOSortOrdering>(
													new EOSortOrdering(
															ERXQ.keyPath(
																	OccupantResaForTbV.TO_INDIVIDU_KEY,
																	EOIndividu.NOM_AFFICHAGE_KEY),
															EOSortOrdering.CompareCaseInsensitiveAscending))));
			dgResaOccup().setDelegate(dgResaDelegate());
		}
	}

	private OccupantResaForTbV getOccupResaIn(NSArray<OccupantResaForTbV> lst,
			EOOccupant occup) {

		if (occup == null) {
			return null;
		}
		if (lst == null) {
			return null;
		}
		for (OccupantResaForTbV occ : lst) {
			if (occup.toFwkpers_Individu().equals(occ.getIndividu())) {
				return occ;
			}
		}
		return null;
	}

	private ApResaForTbV resaOccur;
	private OccupantResaForTbV resaOccupOccur;

	/**
	 * @return the resaOccur
	 */
	public ApResaForTbV resaOccur() {
		return resaOccur;
	}

	/**
	 * @param resaOccur
	 *            the resaOccur to set
	 */
	public void setResaOccur(ApResaForTbV resaOccur) {
		this.resaOccur = resaOccur;
	}

	public WOActionResults openSearchMaquette() {
		// CktlAjaxWindow.open(context(), cawsearchzoneid());
		AjaxModalDialog.open(context(), cawsearchzoneid());
		return null;
	}

	public WOActionResults clearSearchedSemestre() {
		setSelSemestre(null);
		return null;
	}

	public String libSemestre() {

		return treeSemDelegate().getLibelle(selSemestre());
	}

	public String auclstintervenantsid() {

		return getComponentId() + "_auclstintervenantsid";
	}

	/**
	 * @return the resaOccupOccur
	 */
	public OccupantResaForTbV resaOccupOccur() {
		return resaOccupOccur;
	}

	/**
	 * @param resaOccupOccur
	 *            the resaOccupOccur to set
	 */
	public void setResaOccupOccur(OccupantResaForTbV resaOccupOccur) {
		this.resaOccupOccur = resaOccupOccur;
	}

	private WODisplayGroup dgResaOccup;

	public WODisplayGroup dgResaOccup() {
		if (dgResaOccup == null) {
			dgResaOccup = new WODisplayGroup();
			dgResaOccup.setNumberOfObjectsPerBatch(10);
		}
		return dgResaOccup;
	}

	public void setDgResaOccup(WODisplayGroup dgResaOccup) {
		this.dgResaOccup = dgResaOccup;
	}

	private DgOccupDelegate dgResaDelegate;

	public DgOccupDelegate dgResaDelegate() {
		if (dgResaDelegate == null) {
			dgResaDelegate = new DgOccupDelegate();
		}
		return dgResaDelegate;
	}

	public class DgOccupDelegate {
		public void displayGroupDidChangeSelection(WODisplayGroup group) {
			setSelectedOccupant((OccupantResaForTbV) group.selectedObject());
			dgPeriodes().clearSelection();
		}
	}

	private OccupantResaForTbV selectedOccupant;
	private NSArray<EOScolMaquetteHoraireCode> lstMhcode;
	private EOScolMaquetteHoraireCode mhocodeOccur;
	private EOScolMaquetteHoraireCode searchedTypeAp;

	public OccupantResaForTbV selectedOccupant() {
		return selectedOccupant;

	}

	public void setSelectedOccupant(OccupantResaForTbV selectedOccupant) {
		this.selectedOccupant = selectedOccupant;
		dgPeriodes().setDelegate(null);
		dgPeriodes().clearSelection();
		if (selectedOccupant == null) {
			dgPeriodes().setObjectArray(null);
		} else {

			@SuppressWarnings("unchecked")
			NSArray<EOReservation> tmp = selectedOccupant.getReservations();

			String qualStr = "";
			NSMutableArray<Object> args = new NSMutableArray<Object>();
			if (selectedOccupant.getDebut() != null) {
				qualStr += ERXQ.keyPath(EOPeriodicite.DATE_DEB_KEY) + " => %@";
				args.add(selectedOccupant.getDebut());
			}

			if (selectedOccupant.getFin() != null) {
				if (!"".equals(qualStr)) {
					qualStr += " AND ";
				}
				qualStr += ERXQ.keyPath(EOPeriodicite.DATE_FIN_KEY) + " <= %@";
				args.add(selectedOccupant.getFin());
			}

			if (!"T".equals(selectedOccupant.getHrValide())) {
				if (!"".equals(qualStr)) {
					qualStr += " AND ";
				}
				qualStr += ERXQ.keyPath(EOPeriodicite.HCOMP_KEY) + " = %@";
				args.add(Integer.parseInt(selectedOccupant.getHrValide()));
			}

			EOQualifier qualPeriode = EOQualifier.qualifierWithQualifierFormat(
					qualStr, args);

			NSMutableArray<PeriodesResaForTbV> tmpPeriodes = new NSMutableArray<PeriodesResaForTbV>();

			for (EOReservation resa : tmp) {
				// parse des occupants
				for (EOPeriodicite periode : resa.toPeriodicites(qualPeriode)) {
					// recherche si on a deja un PeriodesResaForTbV pour cet
					// occupant
					PeriodesResaForTbV periodeResa = getPeriodeResaIn(
							tmpPeriodes, periode);
					if (periodeResa == null) {
						periodeResa = new PeriodesResaForTbV(periode,
								selectedOccupant);

						periodeResa.setDebut(selectedOccupant.getDebut());
						periodeResa.setFin(selectedOccupant.getFin());
						tmpPeriodes.add(periodeResa);
					}
				}
			}

			dgPeriodes().setObjectArray(
					EOSortOrdering.sortedArrayUsingKeyOrderArray(
							tmpPeriodes,
							new NSArray<EOSortOrdering>(new EOSortOrdering(ERXQ
									.keyPath(
											PeriodesResaForTbV.PERIODICITE_KEY,
											EOPeriodicite.DATE_DEB_KEY),
									EOSortOrdering.CompareAscending))));

		}
		dgPeriodes().setDelegate(dgPeriodesDelegate());
	}

	private PeriodesResaForTbV getPeriodeResaIn(
			NSArray<PeriodesResaForTbV> lst, EOPeriodicite periode) {

		if (periode == null) {
			return null;
		}
		if (lst == null) {
			return null;
		}
		for (PeriodesResaForTbV occ : lst) {
			if (periode.equals(occ.getPeriodicite())) {
				return occ;
			}
		}
		return null;
	}

	/**
	 * @return the lstMhcode
	 */
	public NSArray<EOScolMaquetteHoraireCode> lstMhcode() {
		if (lstMhcode == null) {
			lstMhcode = EOScolMaquetteHoraireCode.fetchAll(session()
					.defaultEditingContext());
		}
		return lstMhcode;
	}

	/**
	 * @param lstMhcode
	 *            the lstMhcode to set
	 */
	public void setLstMhcode(NSArray<EOScolMaquetteHoraireCode> lstMhcode) {
		this.lstMhcode = lstMhcode;
	}

	/**
	 * @return the mhocodeOccur
	 */
	public EOScolMaquetteHoraireCode mhocodeOccur() {
		return mhocodeOccur;
	}

	/**
	 * @param mhocodeOccur
	 *            the mhocodeOccur to set
	 */
	public void setMhocodeOccur(EOScolMaquetteHoraireCode mhocodeOccur) {
		this.mhocodeOccur = mhocodeOccur;
	}

	/**
	 * @return the searchedTypeAp
	 */
	public EOScolMaquetteHoraireCode searchedTypeAp() {
		return searchedTypeAp;
	}

	/**
	 * @param searchedTypeAp
	 *            the searchedTypeAp to set
	 */
	public void setSearchedTypeAp(EOScolMaquetteHoraireCode searchedTypeAp) {
		this.searchedTypeAp = searchedTypeAp;
	}

	public EOQualifier enseignantsQualifier() {
		return FinderEnseignant.qualEnseignant(((Session) session())
				.selectedYear());
	}

	public String auclstperiodesid() {
		return getComponentId() + "_auclstperiodesid";
	}

	private WODisplayGroup dgPeriodes;

	public WODisplayGroup dgPeriodes() {
		if (dgPeriodes == null) {
			dgPeriodes = new WODisplayGroup();
			dgPeriodes.setNumberOfObjectsPerBatch(10);
		}
		return dgPeriodes;
	}

	public void setDgPeriodesOccup(WODisplayGroup dgPeriodes) {
		this.dgPeriodes = dgPeriodes;
	}

	private DgPeriodeDelegate dgPeriodesDelegate;

	public DgPeriodeDelegate dgPeriodesDelegate() {
		if (dgPeriodesDelegate == null) {
			dgPeriodesDelegate = new DgPeriodeDelegate();
		}
		return dgPeriodesDelegate;
	}

	public class DgPeriodeDelegate {
		@SuppressWarnings("unchecked")
		public void displayGroupDidChangeSelection(WODisplayGroup group) {
			setSelectedPeriode(group.selectedObjects());
		}
	}

	private NSArray<PeriodesResaForTbV> selectedPeriodes;
	private PeriodesResaForTbV periodeOccur;
	private EOScolMaquetteAp apOccur;
	private Boolean hrValides;
	private NSTimestamp debutSearch;
	private NSTimestamp finSearch;
	private String searchValidHr = "T";
	private Boolean isValidMode;
	private String modeValidPeriode;

	public NSArray<PeriodesResaForTbV> selectedPeriode() {
		return selectedPeriodes;
	}

	public void setSelectedPeriode(NSArray<PeriodesResaForTbV> selectedPeriode) {
		this.selectedPeriodes = selectedPeriode;
	}

	/**
	 * @return the periodeOccur
	 */
	public PeriodesResaForTbV periodeOccur() {
		return periodeOccur;
	}

	/**
	 * @param periodeOccur
	 *            the periodeOccur to set
	 */
	public void setPeriodeOccur(PeriodesResaForTbV periodeOccur) {
		this.periodeOccur = periodeOccur;
	}

	/**
	 * @return the apOccur
	 */
	public EOScolMaquetteAp apOccur() {
		return apOccur;
	}

	/**
	 * @param apOccur
	 *            the apOccur to set
	 */
	public void setApOccur(EOScolMaquetteAp apOccur) {
		this.apOccur = apOccur;
	}

	public boolean canShowOccupants() {
		return ((displayGroup().allObjects() != null)
				&& (displayGroup().allObjects().size() > 0) && (selectedApResa() != null));
	}

	public boolean canShowPeriodes() {
		return canShowOccupants() && (dgResaOccup().allObjects() != null)
				&& (dgResaOccup().allObjects().size() > 0)
				&& (selectedOccupant() != null);
	}

	/**
	 * @return the hrValides
	 */
	public boolean hrValides() {
		if (hrValides == null) {
			hrValides = Boolean.FALSE;
		}
		return hrValides;
	}

	/**
	 * @param hrValides
	 *            the hrValides to set
	 */
	public void setHrValides(Boolean hrValides) {
		if (hrValides == null) {
			this.hrValides = Boolean.FALSE;
		} else {
			this.hrValides = hrValides;
		}
	}

	/**
	 * @return the debutSearch
	 */
	public NSTimestamp debutSearch() {
		return debutSearch;
	}

	/**
	 * @param debutSearch
	 *            the debutSearch to set
	 */
	public void setDebutSearch(NSTimestamp debutSearch) {
		this.debutSearch = debutSearch;
	}

	/**
	 * @return the finSearch
	 */
	public NSTimestamp finSearch() {
		return finSearch;
	}

	/**
	 * @param finSearch
	 *            the finSearch to set
	 */
	public void setFinSearch(NSTimestamp finSearch) {
		this.finSearch = finSearch;
	}

	public boolean searchNotNull() {
		return ((displayGroup().displayedObjects() != null) && (displayGroup()
				.displayedObjects().size() > 0));
	}

	public boolean canValidePeriode() {
		return canValidDevaliPeriode(new Integer(0));
	}

	public boolean canDeValidePeriode() {
		return canValidDevaliPeriode(new Integer(1));
	}

	private boolean canValidDevaliPeriode(Integer hcomp) {
		GrilleApplicationUser user = ((Session) session()).getGUser();
		EOScolFormationAnnee year = ((Session) session()).selectedYear();
		NSArray<PeriodesResaForTbV> tmp = EOQualifier
				.filteredArrayWithQualifier(selectedPeriode(), EOQualifier
						.qualifierWithQualifierFormat(
								PeriodesResaForTbV.PERIODICITE_KEY + "."
										+ EOPeriodicite.HCOMP_KEY + "=%@",
								new NSArray<Integer>(hcomp)));
		if ((tmp != null) && (tmp.size() > 0)) {

			return (user.canRealiseVoeux(selectedOccupant.getIndividu(), year) && user
					.canRealiseVoeuxFor(selectedApResa.getScolMaquetteAp(),
							year));
		}
		return false;
	}

	public WOActionResults openValidPeriodes() {
		setModeValidPeriode(EditValidResa.VALID_MODE);
		CktlAjaxWindow.open(context(), caweditvalidid(),
				"Validation de périodes");
		isValidMode = Boolean.TRUE;
		return null;
	}

	public WOActionResults openDeValidPeriodes() {
		setModeValidPeriode(EditValidResa.DEVALID_MODE);
		CktlAjaxWindow.open(context(), caweditvalidid(),
				"Dé-validation de périodes");
		isValidMode = Boolean.TRUE;
		return null;
	}

	public String aucValidPeriodesId() {
		return getComponentId() + "_aucValidPeriodesId";
	}

	/**
	 * @return the searchValidHr
	 */
	public String searchValidHr() {
		return searchValidHr;
	}

	/**
	 * @param searchValidHr
	 *            the searchValidHr to set
	 */
	public void setSearchValidHr(String searchValidHr) {
		this.searchValidHr = searchValidHr;
	}

	/**
	 * @return the isValidMode
	 */
	public Boolean isValidMode() {
		return isValidMode;
	}

	/**
	 * @param isValidMode
	 *            the isValidMode to set
	 */
	public void setValidMode(Boolean isValidMode) {
		this.isValidMode = isValidMode;
	}

	public String caweditvalidid() {
		return getComponentId() + "_caweditvalidid";
	}

	public String auceditvalidid() {
		return getComponentId() + "_auceditvalidid";
	}

	/**
	 * @return the modeValidPeriode
	 */
	public String modeValidPeriode() {
		return modeValidPeriode;
	}

	/**
	 * @param modeValidPeriode
	 *            the modeValidPeriode to set
	 */
	public void setModeValidPeriode(String modeValidPeriode) {
		this.modeValidPeriode = modeValidPeriode;
	}
	
	public WOActionResults onValidDevalidAction(){
		dgPeriodes().clearSelection();
		setSelectedOccupant(selectedOccupant);
		dgPeriodes().clearSelection();
		return null;
	}

}