package org.cocktail.grille.serveur.components;

import java.math.BigDecimal;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOHcompRecup;
import org.cocktail.grillefwk.serveur.metier.eof.EOPeriodicite;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXQ;

public class EditValidResa extends CktlAjaxWOComponent {
	public final static String BIND_LST_PERIODES = "lstPeriodes";
	public final static String VALID_MODE = "V";
	public final static String DEVALID_MODE = "D";
	public final static String BIND_MODE = "mode";
	public final static String BIND_VALID_ACTION = "onValidDevalidAction";

	public EditValidResa(WOContext context) {
		super(context);
	}

	public int nbPeriode = 0;

	public int getHcompValid() {
		return (isValidMode() ? 0 : 1);
	}

	public BigDecimal ttHrPeriodes() {
		BigDecimal tt = new BigDecimal(0);

		nbPeriode = 0;
		nbHeuresFaites = null;
		for (PeriodesResaForTbV per : getLstPeriodes()) {
			if (per.getPeriodicite().hcomp() == getHcompValid()) {
				tt = tt.add(per.getPeriodicite().totalHrPeriodicite());
				nbPeriode++;
			}
		}
		return tt;
	}

	@SuppressWarnings("unchecked")
	public NSMutableArray<PeriodesResaForTbV> getLstPeriodes() {
		return (NSMutableArray<PeriodesResaForTbV>) valueForBinding(BIND_LST_PERIODES);
	}

	public void setLstPeriodes(NSMutableArray<PeriodesResaForTbV> lstPeriodes) {
		setValueForBinding(lstPeriodes, BIND_LST_PERIODES);
	}

	private EOVoeux voeuxOccur;

	/**
	 * @return the voeuxOccur
	 */
	public EOVoeux voeuxOccur() {
		return voeuxOccur;
	}

	/**
	 * @param voeuxOccur
	 *            the voeuxOccur to set
	 */
	public void setVoeuxOccur(EOVoeux voeuxOccur) {
		this.voeuxOccur = voeuxOccur;
	}

	public String colonnesVoeuxKeys() {
		return VoeuxTbV.COL_AP_KEY + "," + VoeuxTbV.COL_TYPE_AP_KEY + ","
				+ VoeuxTbV.COL_INDIV_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_VOEUX_KEY + ","
				// + VoeuxTbV.COL_NB_HEURES_TD_VOEUX_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_REALISE_KEY + ","
				// + VoeuxTbV.COL_NB_HEURES_TD_KEY+ ","
				+ VoeuxTbV.COL_DAT_CREATION_KEY
		// + "," + VoeuxTbV.COL_VALIDE_KEY
		;
	}

	private WODisplayGroup dgVoeux;

	public WODisplayGroup dgVoeux() {
		if (dgVoeux == null) {
			dgVoeux = new WODisplayGroup();
		}

		if (lstVoeux() != null) {
			dgVoeux.setObjectArray(lstVoeux());
		}
		dgVoeux.clearSelection();
		dgVoeux.setDelegate(getDgVoeuxDelegate());
		return dgVoeux;
	}

	private DgVoeuxDelegate dgVoeuxDelegate;

	public DgVoeuxDelegate getDgVoeuxDelegate() {
		if (dgVoeuxDelegate == null) {
			dgVoeuxDelegate = new DgVoeuxDelegate();
		}
		return dgVoeuxDelegate;
	}

	public class DgVoeuxDelegate {
		@SuppressWarnings("unchecked")
		public void displayGroupDidChangeSelection(WODisplayGroup group) {
			setSelectedVoeux(group.selectedObjects());
		}
	}

	private NSArray<EOVoeux> lstVoeux() {
		String qualStr = "(";
		NSMutableArray<Object> args = new NSMutableArray<Object>();
		for (PeriodesResaForTbV per : getLstPeriodes()) {
			if (per.getPeriodicite().hcomp() == getHcompValid()) {
				if (!"(".equals(qualStr)) {
					qualStr += " OR ";
				}
				qualStr += "("
						+ EOVoeux.TO_FWK_SCOLARITE__SCOLARITE_FWK_SCOL_MAQUETTE_AP_KEY
						+ "=%@)";
				args.add(per.getOccupantResa().getApResa().getScolMaquetteAp());
			}

		}
		qualStr += ")";
		String qualStrInd = "(";
		for (PeriodesResaForTbV per : getLstPeriodes()) {
			if (per.getPeriodicite().hcomp() == getHcompValid()) {
				if (!"(".equals(qualStrInd)) {
					qualStrInd += " OR ";
				}
				qualStrInd += "(" + EOVoeux.TO_FWKPERS__INDIVIDU_ENSEIGNANT_KEY
						+ "=%@)";
				args.add(per.getOccupantResa().getIndividu());
			}

		}
		qualStrInd += ")";
		String qual = "";
		if (!"()".equals(qualStr)) {
			qual += qualStr;
		}
		if ((!"()".equals(qualStrInd))) {
			if (!"".equals(qual)) {
				qual += " AND ";
			}
			qual += qualStrInd;
		}

		return EOVoeux.fetchVoeuxes(session().defaultEditingContext(),
				EOQualifier.qualifierWithQualifierFormat(qual, args), null);
	}

	public void setDgVoeux(WODisplayGroup dgVoeux) {
		this.dgVoeux = dgVoeux;
	}

	public String aucheuresfaitesid() {
		return getComponentId() + "_aucheuresfaitesid";
	}

	private NSArray<EOVoeux> selectedVoeux;
	private BigDecimal nbHeuresFaites;
	private NSDictionary<String, Object> creatOccur;
	private NSArray<NSDictionary<String, Object>> selectedToCreate;

	public NSArray<EOVoeux> getSelectedVoeux() {
		return selectedVoeux;
	}

	public void setSelectedVoeux(NSArray<EOVoeux> selectedVoeux) {

		this.selectedVoeux = selectedVoeux;
	}

	/**
	 * @return the nbHeuresFaites
	 */
	public BigDecimal nbHeuresFaites() {
		if (nbHeuresFaites == null) {
			nbHeuresFaites = ttHrPeriodes();
		}
		return nbHeuresFaites;
	}

	/**
	 * @param nbHeuresFaites
	 *            the nbHeuresFaites to set
	 */
	public void setNbHeuresFaites(BigDecimal nbHeuresFaites) {
		this.nbHeuresFaites = nbHeuresFaites;
	}

	public WOActionResults validPeriodes() {
		EOEditingContext ec = new EOEditingContext(session()
				.defaultEditingContext());
		// traitement voeux
		for (Object obVoeu : dgVoeux().allObjects()) {
			EOVoeux voeu = ((EOVoeux) obVoeu).localInstanceIn(ec);
			voeu.setNbHeureRealise(nbHeuresFaites().add(
					BigDecimal.valueOf(voeu.nbHeureRealise() != null ? voeu
							.nbHeureRealise() : 0)).doubleValue());
		}

		// creations voeux
		for (NSDictionary<String, Object> dic : selectedToCreate()) {
			EOVoeux voeu = EOVoeux.createVoeux(ec,
					(EOScolFormationAnnee) dic.objectForKey("an"));
			voeu.setDCreation(new NSTimestamp());
			voeu.setNbHeureRealise(nbHeuresFaites().doubleValue());
			voeu.setToFwkpers_IndividuCreateurRelationship(((Session) session())
					.getGUser().individu().localInstanceIn(ec));
			voeu.setToFwkpers_IndividuEnseignantRelationship((EOIndividu) dic
					.objectForKey("individu"));
			voeu.setToFwkScolarite_ScolariteFwkScolMaquetteApRelationship((EOScolMaquetteAp) dic
					.objectForKey("ap"));
			voeu.setValide("O");
		}

		// validation période
		for (PeriodesResaForTbV per : getLstPeriodes()) {
			EOPeriodicite EoPer = per.getPeriodicite().localInstanceIn(ec);
			EoPer.setHcomp(new Integer(1));
			EOHcompRecup currentHcomp = EOHcompRecup.createHcompRecup(ec,
					new Double(0));

			currentHcomp.setToReservationRelationship(EoPer.toReservation());
			currentHcomp.setToFwkpers_IndividuRelationship(per
					.getOccupantResa().getIndividu());
			currentHcomp.setToFwkScolarite_ScolMaquetteApRelationship(per
					.getOccupantResa().getApResa().getScolMaquetteAp());
			currentHcomp.setToPeriodiciteRelationship(EoPer);
		}

		ec.saveChanges();
		session().defaultEditingContext().saveChanges();
		return onValidAction();
	}

	public String getMode() {
		if ((hasBinding(BIND_MODE)) && (valueForBinding(BIND_MODE) != null)) {
			return (String) valueForBinding(BIND_MODE);
		}
		return VALID_MODE;
	}

	public void setMode(String mode) {
		setValueForBinding(mode, BIND_MODE);
	}

	public boolean isValidMode() {
		return VALID_MODE.equals(getMode());
	}

	public boolean isDeValidMode() {
		return DEVALID_MODE.equals(getMode());
	}

	public WOActionResults devalidPeriodes() {
		EOEditingContext ec = new EOEditingContext(session()
				.defaultEditingContext());
		// traitement voeux
		for (Object obVoeu : dgVoeux().allObjects()) {
			EOVoeux voeu = ((EOVoeux) obVoeu).localInstanceIn(ec);
			BigDecimal nb = BigDecimal.valueOf(
					voeu.nbHeureRealise() != null ? voeu.nbHeureRealise() : 0)
					.subtract(nbHeuresFaites());
			voeu.setNbHeureRealise(nb.compareTo(BigDecimal.valueOf(0)) != -1 ? nb
					.doubleValue() : 0);
		}
		// validation période
		for (PeriodesResaForTbV per : getLstPeriodes()) {
			EOPeriodicite EoPer = per.getPeriodicite().localInstanceIn(ec);
			EoPer.setHcomp(new Integer(0));
			for (EOHcompRecup hcomp : EoPer.toHcompRecups()) {
				ec.deleteObject(hcomp);
			}
		}

		ec.saveChanges();
		session().defaultEditingContext().saveChanges();
		return onValidAction();
	}

	public WOActionResults onValidAction() {
		if (hasBinding(BIND_VALID_ACTION)) {
			return (WOActionResults) valueForBinding(BIND_VALID_ACTION);
		}
		return null;
	}

	public NSArray<NSDictionary<String, Object>> lstToCreate() {
		NSMutableArray<NSDictionary<String, Object>> retour = new NSMutableArray<NSDictionary<String, Object>>();
		for (PeriodesResaForTbV per : getLstPeriodes()) {
			NSArray<EOVoeux> exist = EOQualifier
					.filteredArrayWithQualifier(
							dgVoeux.allObjects(),
							EOQualifier.qualifierWithQualifierFormat(
									ERXQ.keyPath(EOVoeux.TO_FWKPERS__INDIVIDU_ENSEIGNANT_KEY)
											+ "=%@ AND "
											+ EOVoeux.TO_FWK_SCOLARITE__SCOLARITE_FWK_SCOL_MAQUETTE_AP_KEY
											+ "=%@ ",
									new NSArray<EOGenericRecord>(
											new EOGenericRecord[] {
													per.getOccupantResa()
															.getIndividu(),
													per.getOccupantResa()
															.getApResa()
															.getScolMaquetteAp() })));
			if (!((exist != null) && (exist.size() > 0))) {
				NSMutableDictionary<String, Object> dico = new NSMutableDictionary<String, Object>();
				dico.setObjectForKey(per.getOccupantResa().getIndividu(),
						"individu");
				dico.setObjectForKey(per.getOccupantResa().getApResa()
						.getScolMaquetteAp(), "ap");
				dico.setObjectForKey(per.getOccupantResa().getApResa()
						.getScolMaquetteAp()
						.toFwkScolarite_ScolFormationAnnee(), "an");
				retour.add(dico);
			}
		}

		return retour;
	}

	/**
	 * @return the creatOccur
	 */
	public NSDictionary<String, Object> creatOccur() {
		return creatOccur;
	}

	/**
	 * @param creatOccur
	 *            the creatOccur to set
	 */
	public void setCreatOccur(NSDictionary<String, Object> creatOccur) {
		this.creatOccur = creatOccur;
	}

	/**
	 * @return the selectedToCreate
	 */
	public NSArray<NSDictionary<String, Object>> selectedToCreate() {
		return selectedToCreate;
	}

	/**
	 * @param selectedToCreate
	 *            the selectedToCreate to set
	 */
	public void setSelectedToCreate(
			NSArray<NSDictionary<String, Object>> selectedToCreate) {
		this.selectedToCreate = selectedToCreate;
	}

	public String libToCreate() {
		return ((EOIndividu) creatOccur().objectForKey("individu"))
				.getNomPrenomAffichage()
				+ " sur "
				+ ((EOScolMaquetteAp) creatOccur().objectForKey("ap"))
						.mapLibelle();
	}
}