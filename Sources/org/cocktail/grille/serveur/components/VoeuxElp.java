package org.cocktail.grille.serveur.components;

import java.math.BigDecimal;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.finder.FinderActivite;
import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSComparator.ComparisonException;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSMutableArray;

import er.ajax.AjaxUpdateContainer;

public class VoeuxElp extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5707540000418234389L;

	public VoeuxElp(WOContext context) {
		super(context);
	}

	/**
	 * @return the selectedEc
	 */
	public EOScolMaquetteEc selectedEc() {
		return ((Session) session()).getSelectedEc();
	}

	/**
	 * @param selectedEc
	 *            the selectedEc to set
	 */
	public void setSelectedEc(EOScolMaquetteEc selectedEc) {
		refreshAll();
		((Session) session()).setSelectedEc(selectedEc);
	}

	private void refreshAll() {
		lstVoeux = null;
		lstVoeuxActiv = null;
		lstAps = null;
		lstTotalVoeux = null;
	}

	private NSArray<EOVoeux> lstVoeux;

	/**
	 * @return the lstVoeux
	 */
	@SuppressWarnings("unchecked")
	public NSArray<EOVoeux> lstVoeux() {
		if ((lstVoeux == null) && (selectedEc() != null))
			lstVoeux = EOSortOrdering.sortedArrayUsingKeyOrderArray(FinderVoeux
					.getVoeuxAPForEcAndYear(selectedEc().editingContext(),
							selectedEc(), selectedEc()
									.toFwkScolarite_ScolFormationAnnee()),
					EOVoeux.SORT_INDIV_EC_AP_DATE_CREATION);
		return lstVoeux;
	}

	/**
	 * @param lstVoeux
	 *            the lstVoeux to set
	 */
	public void setLstVoeux(NSArray<EOVoeux> lstVoeux) {
		this.lstVoeux = lstVoeux;
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

	private EOVoeux voeuxOccurActiv;

	/**
	 * @return the voeuxOccurActiv
	 */
	public EOVoeux voeuxOccurActiv() {
		return voeuxOccurActiv;
	}

	/**
	 * @param voeuxOccurActiv
	 *            the voeuxOccurActiv to set
	 */
	public void setVoeuxOccurActiv(EOVoeux voeuxOccurActiv) {
		this.voeuxOccurActiv = voeuxOccurActiv;
	}

	private WODisplayGroup dgVoeux;

	public WODisplayGroup dgVoeux() {
		if (dgVoeux == null) {
			dgVoeux = new WODisplayGroup();
			// displayGroup.setDelegate(new DgDelegate());
		}
		if (lstVoeux() != null) {
			dgVoeux.setObjectArray(lstVoeux());
		}
		dgVoeux.clearSelection();
		return dgVoeux;
	}

	public void setDgVoeux(WODisplayGroup dgVoeux) {
		this.dgVoeux = dgVoeux;
	}

	public WOComponent editMethodeObject() {
		return this;
	}

	public String colonnesVoeuxKeys() {
		return VoeuxTbV.COL_AP_KEY + "," + VoeuxTbV.COL_TYPE_AP_KEY + ","
				+ VoeuxTbV.COL_INDIV_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_VOEUX_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_TD_VOEUX_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_REALISE_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_TD_KEY
				+ "," + VoeuxTbV.COL_DAT_CREATION_KEY + ","
				+ VoeuxTbV.COL_VALIDE_KEY + "," + VoeuxTbV.OBJ_KEY + ".action";
	}

	public WOActionResults refreshVoeux() {
		lstVoeux = null;
		if (dgVoeux != null) {
			dgVoeux.setObjectArray(lstVoeux());
			dgVoeux.clearSelection();
		}
		AjaxUpdateContainer.updateContainerWithID("aucvoeux", context());
		lstTotalVoeux = null;
		AjaxUpdateContainer
				.updateContainerWithID(auctotauxvoeuxid(), context());
		return null;
	}

	public boolean canAddVoeux() {
		return (((lstAps != null) && (lstAps.size() > 0)) && getgUser()
				.canAddVoeuxFor(selectedEc(),
						selectedEc().toFwkScolarite_ScolFormationAnnee()));
	}

	public boolean canAddVoeuxActiv() {
		return (((lstActivs() != null) && (lstActivs().size() > 0)) && getgUser()
				.canAddVoeuxFor(selectedEc(),
						selectedEc().toFwkScolarite_ScolFormationAnnee()));
	}

	public WOActionResults addVoeux() {
		creatVoeux();
		lstVoeux = null;
		setApSelection(true);
		setActivSelection(false);
		setModeEditVoeux(EditVoeux.MODE_CREATE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un voeux");
		return null;
	}

	public WOActionResults addRealise() {
		creatVoeux();
		editedVoeux.setValide("O");
		lstVoeux = null;
		setApSelection(true);
		setActivSelection(false);
		setModeEditVoeux(EditVoeux.MODE_CREAT_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un réalisé");
		return null;
	}
	
	public WOActionResults addVoeuxActiv() {
		creatVoeux();
		lstVoeuxActiv = null;
		setApSelection(false);
		setActivSelection(true);
		setModeEditVoeux(EditVoeux.MODE_CREATE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un voeux");
		return null;
	}
	
	public WOActionResults addRealiseActiv() {
		creatVoeux();
		editedVoeux.setValide("O");
		lstVoeuxActiv = null;
		setApSelection(false);
		setActivSelection(true);
		setModeEditVoeux(EditVoeux.MODE_CREAT_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un réalisé");
		return null;
	}

	private void creatVoeux() {
		Session sess = ((Session) session());
		EOEditingContext ecVoeux = new EOEditingContext(
				sess.defaultEditingContext());
		setEditedVoeux(EOVoeux.createVoeux(ecVoeux, currentYear()
				.localInstanceIn(ecVoeux)));

		editedVoeux.setToFwkpers_IndividuCreateurRelationship(sess.getGUser()
				.individu().localInstanceIn(ecVoeux));

		if (sess.getSelectedEns() != null) {
			if (sess.getGUser().haveRightForObjMaquette(selectedEc(),
					sess.selectedYear(), true)) {
				editedVoeux
						.setToFwkpers_IndividuEnseignantRelationship(((Session) session())
								.getSelectedEns().getIndividu()
								.localInstanceIn(ecVoeux));
			}
		}

		if ((editedVoeux.toFwkpers_IndividuEnseignant() == null)
				&& (sess.getGUser().isEnseignant(sess.selectedYear()))) {
			editedVoeux.setToFwkpers_IndividuEnseignantRelationship(sess
					.getGUser().individu().localInstanceIn(ecVoeux));

		}

		editedVoeux.setDCreation(new NSTimestamp());
		// editedVoeux.setValide("O");
	}

	private EOVoeux editedVoeux;

	public EOVoeux getEditedVoeux() {
		return editedVoeux;
	}

	public void setEditedVoeux(EOVoeux editedVoeux) {
		this.editedVoeux = editedVoeux;
	}

	public boolean isVoeuxEdited() {
		return (editedVoeux != null);
	}

	public String caweditvoeuxid() {
		return getComponentId() + "_caweditvoeuxid";
	}

	public boolean canEditVoeux() {
		return ((selectedEc() != null) && (lstVoeux() != null)
				&& (lstVoeux().size() > 0) && ((Session) session()).getGUser()
				.canEditVoeux(voeuxOccur()));
	}

	public boolean canEditVoeuxActiv() {
		return ((selectedEc() != null) && (lstVoeuxActiv() != null)
				&& (lstVoeuxActiv().size() > 0) && ((Session) session())
				.getGUser().canEditVoeux(voeuxOccurActiv()));
	}

	private String modeEditVoeux;

	/**
	 * @return the modeEditVoeux
	 */
	public String modeEditVoeux() {
		return modeEditVoeux;
	}

	/**
	 * @param modeEditVoeux
	 *            the modeEditVoeux to set
	 */
	public void setModeEditVoeux(String modeEditVoeux) {
		this.modeEditVoeux = modeEditVoeux;
	}

	public WOActionResults editVoeux(Object voeux) {
		setEditedVoeux((EOVoeux) voeux);
		dgVoeux().setSelectedObject(this.editedVoeux);
		setApSelection(true);
		setActivSelection(false);
		setModeEditVoeux(EditVoeux.MODE_EDITION);
		CktlAjaxWindow.open(context(), caweditvoeuxid(),
				"Modification d'un voeux sur AP");

		return null;
	}

	public WOActionResults editRealiseVoeux(Object voeux) {
		setEditedVoeux((EOVoeux) voeux);
		dgVoeux().setSelectedObject(this.editedVoeux);
		setApSelection(true);
		setActivSelection(false);
		setModeEditVoeux(EditVoeux.MODE_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(),
				"Réalisé d'un voeux sur AP");

		return null;
	}

	public WOActionResults editVoeuxActiv(Object voeux) {
		setEditedVoeux((EOVoeux) voeux);
		dgVoeuxActiv().setSelectedObject(this.editedVoeux);
		setApSelection(false);
		setActivSelection(true);
		setModeEditVoeux(EditVoeux.MODE_EDITION);
		CktlAjaxWindow.open(context(), caweditvoeuxid(),
				"Modification d'un voeux sur activité");

		return null;
	}
	
	public WOActionResults editRealiseActiv(Object voeux) {
		setEditedVoeux((EOVoeux) voeux);
		dgVoeuxActiv().setSelectedObject(this.editedVoeux);
		setApSelection(false);
		setActivSelection(true);
		setModeEditVoeux(EditVoeux.MODE_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(),
				"Modification du réalisé d'un voeux sur activité");
		return null;
	}

	private NSArray<EOScolMaquetteAp> lstAps;

	/**
	 * @return the lstAps
	 */
	public NSArray<EOScolMaquetteAp> lstAps() {
		if ((lstAps == null) && (selectedEc() != null)) {
			lstAps = FinderScolMaquetteEc.getApForEcAndYear(selectedEc()
					.editingContext(), selectedEc(), selectedEc()
					.toFwkScolarite_ScolFormationAnnee());
		}
		return lstAps;
	}

	/**
	 * @param lstAps
	 *            the lstAps to set
	 */
	public void setLstAps(NSArray<EOScolMaquetteAp> lstAps) {
		this.lstAps = lstAps;
	}

	private EOScolMaquetteAp apOccur;

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

	public Integer grpsAp() {
		return groupeForAp(apOccur);
	}

	private Integer groupeForAp(EOScolMaquetteAp ap) {
		return (ap.mapGroupeReel() != null ? ap.mapGroupeReel() : ap
				.mapGroupePrevu());
	}

	public int coutAp() {
		return coutForAp(apOccur);
	}

	private int coutForAp(EOScolMaquetteAp ap) {
		return (ap.mapValeur().intValue() * Math.max(1, groupeForAp(ap)));
	}

	private NSMutableDictionary<String, NSMutableDictionary<String, BigDecimal>> lstTotalVoeux;
	private final static String TOTAL_VOEUX = "TV";
	private final static String TOTAL_FAIT = "TF";
	private final static String BILAN_VOEUX = "BV";
	private final static String BILAN_FAIT = "BF";
	private String totalVoeuxOccur;

	/**
	 * @return the lstTotalVoeux
	 */
	public NSMutableDictionary<String, NSMutableDictionary<String, BigDecimal>> lstTotalVoeux() {
		if ((lstTotalVoeux == null) && (selectedEc() != null)) {
			lstTotalVoeux = new NSMutableDictionary<String, NSMutableDictionary<String, BigDecimal>>();
			for (EOVoeux vap : lstVoeux()) {
				NSMutableDictionary<String, BigDecimal> dicType = null;
				String type = vap.toFwkScolarite_ScolariteFwkScolMaquetteAp()
						.toFwkScolarite_ScolMaquetteHoraireCode()
						.mhcoAbreviation();
				if (lstTotalVoeux.containsKey(type)) {
					dicType = lstTotalVoeux.objectForKey(type);
				} else {
					dicType = new NSMutableDictionary<String, BigDecimal>();
					dicType.setObjectForKey(BigDecimal.valueOf(0), TOTAL_VOEUX);
					dicType.setObjectForKey(BigDecimal.valueOf(0),
							TOTAL_FAIT);
					dicType.setObjectForKey(BigDecimal.valueOf(0),
							BILAN_VOEUX);
					dicType.setObjectForKey(BigDecimal.valueOf(0),
							BILAN_FAIT);
					lstTotalVoeux.setObjectForKey(dicType, type);
				}
				dicType.setObjectForKey(
						dicType.objectForKey(TOTAL_VOEUX).add(
								BigDecimal.valueOf(vap.nbHeuresVoeux()!=null?vap.nbHeuresVoeux():0)),
						TOTAL_VOEUX);
				dicType.setObjectForKey(
						dicType.objectForKey(TOTAL_FAIT).add(
								BigDecimal.valueOf(vap.nbHeureRealise()!=null?vap.nbHeureRealise():0)),
						TOTAL_FAIT);
				// lstTotalVoeux.setObjectForKey(dicType, type);
			}
			for (String type : lstTotalVoeux.allKeys()) {
				EOScolMaquetteAp ap = getApForType(type);
				int besoin = (ap != null ? coutForAp(ap) : 0);
				BigDecimal voeux = lstTotalVoeux.objectForKey(type)
						.objectForKey(TOTAL_VOEUX);
				BigDecimal fait = lstTotalVoeux.objectForKey(type)
				.objectForKey(TOTAL_FAIT);
				/*int ratio = voeux.compareTo(BigDecimal.valueOf(besoin));
				if (ratio > 0) {*/
					lstTotalVoeux.objectForKey(type).setObjectForKey(
							voeux.subtract(BigDecimal.valueOf(besoin)),
							BILAN_VOEUX);
				/*}
				if (ratio < 0) {
					lstTotalVoeux.objectForKey(type).setObjectForKey(
							"-"+voeux.subtract(BigDecimal.valueOf(besoin)),
							BILAN_VOEUX);
				}
				*/
					lstTotalVoeux.objectForKey(type).setObjectForKey(
							fait.subtract(BigDecimal.valueOf(besoin)),
							BILAN_FAIT);
			}

		}
		return lstTotalVoeux;
	}

	private EOScolMaquetteAp getApForType(String type) {
		if (type == null)
			return null;
		for (EOScolMaquetteAp ap : lstAps()) {
			if (type.equals(ap.toFwkScolarite_ScolMaquetteHoraireCode()
					.mhcoAbreviation())) {
				return ap;
			}
		}
		return null;
	}

	/**
	 * @param lstTotalVoeux
	 *            the lstTotalVoeux to set
	 */
	public void setLstTotalVoeux(
			NSMutableDictionary<String, NSMutableDictionary<String, BigDecimal>> lstTotalVoeux) {
		this.lstTotalVoeux = lstTotalVoeux;
	}

	/**
	 * @return the totalVoeuxOccur
	 */
	public String totalVoeuxOccur() {
		return totalVoeuxOccur;
	}

	/**
	 * @param totalVoeuxOccur
	 *            the totalVoeuxOccur to set
	 */
	public void setTotalVoeuxOccur(String totalVoeuxOccur) {
		this.totalVoeuxOccur = totalVoeuxOccur;
	}

	public NSArray<String> lstTotalVoeuxKeys() throws ComparisonException {
		return (lstTotalVoeux() != null ? lstTotalVoeux().allKeys()
				.sortedArrayUsingComparator(
						NSComparator.AscendingCaseInsensitiveStringComparator)
				: null);
	}

	public String bilanVoeux() {
		return lstTotalVoeux().objectForKey(totalVoeuxOccur).objectForKey(
				BILAN_VOEUX)
				+ "";
	}

	public String bilanFait() {
		return lstTotalVoeux().objectForKey(totalVoeuxOccur).objectForKey(
				BILAN_FAIT)
				+ "";
	}

	public String ttVoeux() {
		return lstTotalVoeux().objectForKey(totalVoeuxOccur).objectForKey(
				TOTAL_VOEUX)
				+ "";
	}
	public String ttFait() {
		return lstTotalVoeux().objectForKey(totalVoeuxOccur).objectForKey(
				TOTAL_FAIT)
				+ "";
	}

	private WODisplayGroup dgVoeuxActiv;

	public WODisplayGroup dgVoeuxActiv() {
		if (dgVoeuxActiv == null) {
			dgVoeuxActiv = new WODisplayGroup();
			// displayGroup.setDelegate(new DgDelegate());
		}
		if (lstVoeuxActiv() != null) {
			dgVoeuxActiv.setObjectArray(lstVoeuxActiv());
		}
		dgVoeuxActiv.clearSelection();
		return dgVoeuxActiv;
	}

	public void setDgVoeuxActiv(WODisplayGroup dgVoeuxActiv) {
		this.dgVoeuxActiv = dgVoeuxActiv;
	}

	public String colonnesVoeuxActivKeys() {
		return VoeuxTbV.COL_ACTIVITE_KEY + "," + VoeuxTbV.COL_INDIV_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_VOEUX_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_REALISE_KEY + ","
				+ VoeuxTbV.COL_DAT_CREATION_KEY + "," + VoeuxTbV.COL_VALIDE_KEY
				+ "," + VoeuxTbV.OBJ_KEY + ".action";
	}

	private NSArray<EOVoeux> lstVoeuxActiv;

	/**
	 * @return the lstVoeuxActiv
	 */
	public NSArray<EOVoeux> lstVoeuxActiv() {
		if ((lstVoeuxActiv == null) && (selectedEc() != null))
			lstVoeuxActiv = FinderVoeux.getVoeuxActiviteForEcAndYear(
					getEcEdit(), selectedEc(), selectedEc()
							.toFwkScolarite_ScolFormationAnnee());
		return lstVoeuxActiv;
	}

	/**
	 * @param lstVoeuxActiv
	 *            the lstVoeuxActiv to set
	 */
	public void setLstVoeuxActiv(NSArray<EOVoeux> lstVoeuxActiv) {
		this.lstVoeuxActiv = lstVoeuxActiv;
	}

	private NSArray<EOActivite> lstActivs;
	private boolean isActivSelection;
	private boolean isApSelection;

	public NSArray<EOActivite> lstActivs() {
		if ((lstActivs == null) && (selectedEc() != null)) {
			lstActivs = FinderActivite.getActivitesForEcAndYear(getEcEdit(),
					selectedEc(), selectedEc()
							.toFwkScolarite_ScolFormationAnnee());
		}
		return lstActivs;
	}

	public void setLstActivs(NSArray<EOActivite> lstActivs) {
		this.lstActivs = lstActivs;
	}

	/**
	 * @return the isActivSelection
	 */
	public boolean isActivSelection() {
		return isActivSelection;
	}

	/**
	 * @param isActivSelection
	 *            the isActivSelection to set
	 */
	public void setActivSelection(boolean isActivSelection) {
		this.isActivSelection = isActivSelection;
	}

	/**
	 * @return the isApSelection
	 */
	public boolean isApSelection() {
		return isApSelection;
	}

	/**
	 * @param isApSelection
	 *            the isApSelection to set
	 */
	public void setApSelection(boolean isApSelection) {
		this.isApSelection = isApSelection;
	}

	public String refreshVoeuxFonctionName() {
		return (isApSelection() ? "refreshVoeux()" : "refreshvoeuxactiv()");
	}

	public WOActionResults refreshVoeuxActiv() {
		lstVoeuxActiv = null;
		if (dgVoeuxActiv != null) {
			dgVoeuxActiv.setObjectArray(lstVoeuxActiv());
			dgVoeuxActiv.clearSelection();
		}
		AjaxUpdateContainer.updateContainerWithID("aucvoeuxactiv", context());
		AjaxUpdateContainer.updateContainerWithID(aucbilanvoeuxid(), context());

		return null;
	}

	public WOActionResults onSuccessMethode() {
		return (isApSelection() ? refreshVoeux() : refreshVoeuxActiv());
	}

	private EOScolFormationAnnee lastFetchedYear;

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		// refresh des liste si changement d'année
		if (lastFetchedYear == null) {
			lastFetchedYear = ((Session) session()).selectedYear();
		}
		if (!lastFetchedYear.equals(((Session) session()).selectedYear())) {
			refreshAll();
			lastFetchedYear = ((Session) session()).selectedYear();
		}
		super.appendToResponse(response, context);
	}

	public boolean canChangeEnseignant() {
		return ((Session) session()).getGUser()
				.canChangeEnseignantForObjMaquette(
						((Session) session()).selectedYear(), selectedEc());
	}

	public String auctotauxvoeuxid() {
		return getComponentId() + "_auctotauxvoeuxid";
	}

	public String aucbilanvoeuxid() {
		return getComponentId() + "_aucbilanvoeuxid";
	}

	public BigDecimal nbHeuresActivite() {
		if (selectedEc() == null) {
			return null;
		}
		BigDecimal tt = new BigDecimal(0);
		NSArray<EOActivite> activForEc = FinderActivite
				.getActivitesForEcAndYear(selectedEc().editingContext(),
						selectedEc(), selectedEc()
								.toFwkScolarite_ScolFormationAnnee());
		for (EOActivite activ : activForEc) {
			tt = tt.add(activ.nbHeuresActivite());
		}
		return tt;
	}

	public BigDecimal totalHeuresVoeux() {
		if (selectedEc() == null) {
			return null;
		}
		BigDecimal tt = new BigDecimal(0);

		for (EOVoeux voeux : lstVoeuxActiv()) {
			tt = tt.add(voeux.getNbHeuresTdVoeux());
		}
		return tt;

	}
	public BigDecimal totalHeuresFait() {
		if (selectedEc() == null) {
			return null;
		}
		BigDecimal tt = new BigDecimal(0);

		for (EOVoeux voeux : lstVoeuxActiv()) {
			tt = tt.add(voeux.getNbHeuresTd());
		}
		return tt;

	}

	public BigDecimal totalHeures() {
		if (selectedEc() == null) {
			return null;
		}
		BigDecimal tt = new BigDecimal(0);

		for (EOVoeux voeux : lstVoeuxActiv()) {
			tt = tt.add(voeux.getNbHeuresTdVoeux());
		}
		return tt;

	}

	public String deficitExcedentActiv() {
		if (selectedEc() != null) {
			BigDecimal balance = totalHeures().subtract(nbHeuresActivite())
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			return ((balance.signum() == 1) ? "+" + balance : "" + balance)
					+ "h";
		}
		return "-";
	}
	
	public String deficitExcedentActivFait() {
		if (selectedEc() != null) {
			BigDecimal balance = totalHeuresFait().subtract(nbHeuresActivite())
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			return ((balance.signum() == 1) ? "+" + balance : "" + balance)
					+ "h";
		}
		return "-";
	}

	public String auczonevoeuxid() {
		return getComponentId() + "_auczonevoeuxid";
	}

	public Boolean canRealiseVoeux() {
		return ((selectedEc() != null) && (voeuxOccur() != null) && getgUser()
				.canRealiseVoeux(voeuxOccur()));
	}

	public boolean canAddRealise() {
		return ((lstAps != null) && (lstAps.size() > 0)) &&((selectedEc() != null) && (getgUser().canRealiseVoeuxFor(
				selectedEc(), ((Session)session()).selectedYear())));
	}
	
	public boolean canAddRealiseActiv() {
		return ((lstActivs != null) && (lstActivs.size() > 0)) &&((selectedEc() != null) && (getgUser().canRealiseVoeuxFor(
				selectedEc(), ((Session)session()).selectedYear())));
	}
	
	public Boolean canRealiseVoeuxActiv() {
		return ((selectedEc() != null) && (voeuxOccurActiv() != null) && getgUser()
				.canRealiseVoeux(voeuxOccurActiv()));
	}
}