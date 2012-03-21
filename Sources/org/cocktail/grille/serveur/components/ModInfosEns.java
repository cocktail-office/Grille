package org.cocktail.grille.serveur.components;

import java.math.BigDecimal;

import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.IPersonne;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.GrilleFwkException;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.finder.FinderEnseignant;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOQualifier;

import er.ajax.AjaxModalDialog;
import er.ajax.AjaxUpdateContainer;

public class ModInfosEns extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6235789422824666257L;
	public static final String BIND_ENSEIGNANT = "enseignant";
	public static final String BIND_ACTION_SEL_ENSEIGNANT = "onSelectEnseignant";
	public static final String BIND_SHOW_DETAILS = "showDetails";
	public static final String BIND_TOTAL_VOEUX_ID = "totalVoeuxZoneId";

	public ModInfosEns(WOContext context) {
		super(context);
	}

	public String aucSearch() {
		return getComponentId() + "_aucSearchid";
	}

	public WOActionResults setSearchOn() {
		setSearchMode(true);

		if (dgSearch() != null)
			dgSearch().clearSelection();
		AjaxModalDialog.open(context(), searchZoneId(),
				"Chercher un enseignant");
		return null;
	}

	private boolean searchMode = false;

	public Enseignant selectedEnseignant() {
		return (Enseignant) valueForBinding(BIND_ENSEIGNANT);
	}

	public void setselectedEnseignant(Enseignant ens) {
		setValueForBinding(ens, BIND_ENSEIGNANT);
	}

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

	private WODisplayGroup dgSearch;
	private IPersonne searchedEnseigant;

	/**
	 * @return the dgSearch
	 */
	public WODisplayGroup dgSearch() {
		return dgSearch;
	}

	/**
	 * @param dgSearch
	 *            the dgSearch to set
	 */
	public void setDgSearch(WODisplayGroup dgSearch) {
		this.dgSearch = dgSearch;
	}

	public String searchZoneId() {
		return getComponentId() + "_searchZoneId";
	}

	/**
	 * @return the serchedEnseigant
	 */
	public IPersonne searchedEnseigant() {
		return searchedEnseigant;
	}

	/**
	 * @param serchedEnseigant
	 *            the serchedEnseigant to set
	 */
	public void setSearchedEnseigant(IPersonne searchedEnseigant) {
		this.searchedEnseigant = searchedEnseigant;
	}

	public EOQualifier enseignantsQualifier() {
		return FinderEnseignant.qualEnseignant(((Session) session())
				.selectedYear());
	}

	public WOActionResults selectAction() {
		setselectedEnseignant(new Enseignant(
				((Session) session()).selectedYear(),
				(EOIndividu) searchedEnseigant));
		setSearchOff();
		if (hasBinding(BIND_ACTION_SEL_ENSEIGNANT)) {
			return (WOActionResults) valueForBinding(BIND_ACTION_SEL_ENSEIGNANT);
		}

		return null;
	}

	public WOActionResults setSearchOff() {
		setSearchMode(false);
		AjaxModalDialog.close(context());
		AjaxUpdateContainer.updateContainerWithID(aucSearch(), context());
		return null;
	}

	public WOActionResults cancelSearch() {
		return setSearchOff();
	}

	public String aucSelectLinkid() {
		return getComponentId() + "_aucSelectLinkid";
	}

	public String aucinfosensid() {
		return getComponentId() + "_aucinfosensid";
	}

	private WODisplayGroup dgAffectations;

	public WODisplayGroup dgAffectations() {
		if (dgAffectations == null) {
			dgAffectations = new WODisplayGroup();
		}

		if (selectedEnseignant() != null) {
			dgAffectations.setObjectArray(selectedEnseignant().getQuotites());
			dgAffectations.clearSelection();
		}
		return dgAffectations;
	}

	public BigDecimal quotiteEns() {
		if (selectedEnseignant() != null) {
			return selectedEnseignant().getQuotiteForYear();
		}
		return null;
	}

	public BigDecimal serviceEns() {
		if (selectedEnseignant() != null) {
			return selectedEnseignant().getPotentielBrut();
		}
		return null;
	}

	public String lastPosition() {
		if ((selectedEnseignant() != null)
				&& (selectedEnseignant().getLastPosition() != null)) {
			return selectedEnseignant().getLastPosition().lcPosition();
		}
		return null;
	}

	public BigDecimal dechargeEns() {
		if (selectedEnseignant() != null) {
			return selectedEnseignant().getNbHeuresDecharges();
		}
		return null;
	}

	public BigDecimal prestationsEns() throws GrilleFwkException {
		if (selectedEnseignant() != null) {
			return selectedEnseignant().getNbHeuresPrestations();
		}
		return null;
	}

	public BigDecimal reportEns() {
		if (selectedEnseignant() != null) {
			return selectedEnseignant().getNbHeuresReport();
		}
		return null;
	}

	public BigDecimal serviceDue() {
		if (selectedEnseignant() != null) {
			BigDecimal retour;
			try {
				retour = selectedEnseignant().getTempsServiceForEnsAndYear();
			} catch (GrilleFwkException e) {				
				System.out.println(selectedEnseignant()+":"+e.getMessage());
				retour = new BigDecimal(0);				
			}
			return retour;
		}
		return null;
	}

	public BigDecimal totalVoeuxEns() {
		if (selectedEnseignant() != null) {
			return selectedEnseignant().getTotalVoeux();
		}
		return null;
	}
	
	public BigDecimal totalFaitEns() {
		if (selectedEnseignant() != null) {
			return selectedEnseignant().getTotalFait();
		}
		return null;
	}

	public String deficitExcedentEns() throws GrilleFwkException {
		if ((selectedEnseignant() != null)&&(serviceDue()!=null)) {
			BigDecimal balance = totalVoeuxEns().subtract(serviceDue())
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			return (balance.signum() == 1) ? "+" + balance : ""+balance;
		}
		return "-";
	}
	
	public String deficitExcedentFaitEns() throws GrilleFwkException {
		if ((selectedEnseignant() != null)&&(serviceDue()!=null)) {
			BigDecimal balance = totalFaitEns().subtract(serviceDue())
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			return (balance.signum() == 1) ? "+" + balance : ""+balance;
		}
		return "-";
	}

	public boolean canChangeEns() {
		return ((Session) session()).getGUser().canChangeEnseignant(
				((Session) session()).selectedYear());
	}

	private WODisplayGroup dgCorps;

	public WODisplayGroup dgCorps() {
		if (dgCorps == null) {
			dgCorps = new WODisplayGroup();
		}

		if (selectedEnseignant() != null) {
			dgCorps.setObjectArray(selectedEnseignant().getCorpsEnseignant());
			dgCorps.clearSelection();
		}
		return dgCorps;
	}

	private WODisplayGroup dgPosition;

	public String colonnesPositions = PositionTbV.COL_LIBELLE_KEY + ","
			+ PositionTbV.COL_DEBUT_KEY + "," + PositionTbV.COL_FIN_KEY;

	//+ "," + PositionTbV.COL_CREATION_KEY;

	public WODisplayGroup dgPosition() {
		if (dgPosition == null) {
			dgPosition = new WODisplayGroup();
		}

		if (selectedEnseignant() != null) {
			dgPosition.setObjectArray(selectedEnseignant().getPositions());
			dgPosition.clearSelection();
		}
		return dgPosition;
	}

	public boolean showDetails() {
		if (hasBinding(BIND_SHOW_DETAILS)) {
			return (Boolean) valueForBinding(BIND_SHOW_DETAILS);
		}
		return false;
	}

	public String aucbilanvoeuxid() {
		if (hasBinding(BIND_TOTAL_VOEUX_ID)) {
			return (String) valueForBinding(BIND_TOTAL_VOEUX_ID);
		}
		return getComponentId()+"_aucbilanvoeuxid";
	}
	
	private WODisplayGroup dgDecharges;
	/**
	 * @return the dgDecharges
	 */
	public WODisplayGroup dgDecharges() {
		if (dgDecharges == null) {
			dgDecharges = new WODisplayGroup();
		}

		if (selectedEnseignant() != null) {
			dgDecharges.setObjectArray(selectedEnseignant().getDechargesForYear());
			dgDecharges.clearSelection();
		}
		return dgDecharges;
	}

	public String textButtonChoisirPersonne() {
		return "Choisir "+searchedEnseigant().getNomPrenomAffichage();
	}

	public String erreurserviceid() {
		return getComponentId()+"_erreurserviceid";
	}

}