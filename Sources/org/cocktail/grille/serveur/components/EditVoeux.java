package org.cocktail.grille.serveur.components;

import java.math.BigDecimal;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.IPersonne;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.finder.FinderEnseignant;
import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOEcVerrous;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;

import sun.nio.cs.ext.ISCII91;

import com.ibm.icu.util.IslamicCalendar;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNumberFormatter;

import er.ajax.AjaxModalDialog;
import er.ajax.AjaxUpdateContainer;
import com.webobjects.foundation.NSMutableArray;

public class EditVoeux extends CktlAjaxWOComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean isSaveNoProbleme = false;
	public static final String BIND_CAN_CHANGE_ENS = "canChangeEnseignant";
	public static final String BIND_IS_ACTIV_SELECTION = "isActivSelection";
	public static final String BIND_IS_AP_SELECTION = "isApSelection";
	public static final String BIND_MODE = "mode";
	public static final String MODE_EDITION = "E";
	public static final String MODE_CREATE = "C";
	public static final String MODE_REALISE = "R";
	public static final String MODE_CREAT_REALISE = "CR";

	public EditVoeux(WOContext context) {
		super(context);
	}

	public WOActionResults refreshLink() {
		return null;
	}

	/**
	 * @return the editedVoeux
	 */
	public EOVoeux editedVoeux() {
		return (EOVoeux) valueForBinding("editedVoeux");
	}

	/**
	 * @param editedVoeux
	 *            the editedVoeux to set
	 */
	public void setEditedVoeux(EOVoeux editedVoeux) {
		setValueForBinding(editedVoeux, "editedVoeux");
	}

	public WOActionResults cancel() {
		setSaveNoProbleme(new Boolean(true));
		if ((editedVoeux() != null) && (editedVoeux().editingContext() != null))
			editedVoeux().editingContext().revert();
		setEditedVoeux(null);
		CktlAjaxWindow.close(context(), idWindow());
		return null;
	}

	public WOActionResults valide() {
		setSaveNoProbleme(new Boolean(true));
		boolean setActivite = false;

		if ((hasBinding(BIND_IS_ACTIV_SELECTION))
				&& ((Boolean) valueForBinding(BIND_IS_ACTIV_SELECTION))) {
			if ((getLstActivDispos() != null) && (!haveMultiActiv())
					&& (getSelectedActivite() != null)) {
				editedVoeux().setToActiviteRelationship(getSelectedActivite());
				setActivite = true;
			}
		}
		editedVoeux().setToFwkpers_IndividuCreateurRelationship(
				((Session) session()).getGUser().individu());

		if (!(editedVoeux().valide() != null ? editedVoeux().valide() : "N")
				.equals((editedVoeuxValide != null ? editedVoeuxValide : "N"))) {
			// on change la validation du voeux que si on a les droits
			if (canValidVoeux()) {
				editedVoeux().setValide(editedVoeuxValide);
			}
		}

		try {
			editedVoeux().validateForSave();

		} catch (ValidationException e) {
			UtilMessages.creatMessageUtil(session(), UtilMessages.INFO_MESSAGE,
					e.getMessage());
			setSaveNoProbleme(new Boolean(false));
			if (setActivite) {
				editedVoeux().setToActiviteRelationship(null);
			}
			return null;
		}

		if (editedVoeux().toFwkScolarite_ScolariteFwkScolMaquetteAp() != null) {
			editedVoeux()
					.setToFwkScolarite_ScolariteFwkScolMaquetteApRelationship(
							editedVoeux()
									.toFwkScolarite_ScolariteFwkScolMaquetteAp()
									.localInstanceIn(
											editedVoeux().editingContext()));
		}

		if (editedVoeux().toActivite() != null) {
			editedVoeux().setToActiviteRelationship(
					editedVoeux().toActivite().localInstanceIn(
							editedVoeux().editingContext()));
		}

		if (editedVoeux().toFwkpers_IndividuCreateur() == null)
			editedVoeux().setToFwkpers_IndividuCreateurRelationship(
					((Session) session()).getGUser().individu()
							.localInstanceIn(editedVoeux().editingContext()));

		if (nbHeuresVoeux() != null) {
			editedVoeux()
					.setNbHeuresVoeux(
							nbHeuresVoeux()
									+ Double.valueOf((etDemi() != null ? etDemi()
											: 0)));
		}

		if (nbHeuresRealise() != null) {
			editedVoeux()
					.setNbHeureRealise(
							nbHeuresRealise()
									+ Double.valueOf((etDemiRealise() != null ? etDemiRealise()
											: 0)));
		}

		if (editedVoeux().editingContext() == null)
			getEc().insertObject(editedVoeux());

		editedVoeux().editingContext().saveChanges();
		if (editedVoeux().editingContext().parentObjectStore()
				.equals(session().defaultEditingContext()))
			session().defaultEditingContext().saveChanges();
		setEditedVoeux(null);
		CktlAjaxWindow.close(context(), idWindow());

		return onSuccessMethod();
	}

	public EOEditingContext getEc() {
		if (hasBinding("ec"))
			return (EOEditingContext) valueForBinding("ec");

		return session().defaultEditingContext();

	}

	public void setEc(EOEditingContext ec) {
		if (hasBinding("ec"))
			setValueForBinding(ec, "ec");
	}

	/**
	 * @return the saveNoProbleme
	 */
	public Boolean saveNoProbleme() {
		if (isSaveNoProbleme == null)
			isSaveNoProbleme = new Boolean(true);

		if (isSaveNoProbleme) {
			isSaveNoProbleme = new Boolean(false);
			return new Boolean(true);
		}
		return isSaveNoProbleme;
	}

	/**
	 * @param saveNoProbleme
	 *            the saveNoProbleme to set
	 */
	public void setSaveNoProbleme(Boolean saveNoProbleme) {
		this.isSaveNoProbleme = saveNoProbleme;
	}

	public WOActionResults onSuccessMethod() {
		return (WOActionResults) valueForBinding("onSuccessMethod");
	}

	public boolean canValidVoeux() {
		if ((isRealiseMode() || (isCreatRealiseMode()))) {
			return false;
		}
		if ((editedVoeux().toFwkScolarite_ScolariteFwkScolMaquetteAp() == null)
				&& (editedVoeux().toActivite() == null)) {
			return false;
		}

		return ((Session) session()).getGUser().canValidVoeux(editedVoeux());
	}

	public boolean canNotValidVoeux() {
		return !canValidVoeux();
	}

	private boolean isFilteredMaquette() {
		GrilleApplicationUser user = ((Session) session()).getGUser();
		EOScolFormationAnnee ann = ((Session) session()).selectedYear();

		if (user.haveAdminRight()) {
			// admin liste non filtree
			return false;
		}
		if (!user.isEnseignant(ann)) {
			// c'est pas un enseignant, liste filtrée
			return true;
		}

		if ((editedVoeux() != null)
				&& (editedVoeux().toFwkpers_IndividuEnseignant() != null)) {
			if ((user.isGrilleur(ann)) && (user.isEnseignant(ann))) {
				// grilleur et enseignant, si c'est son voeux liste non filtree,
				// sinon liste filtree
				return !editedVoeux().toFwkpers_IndividuEnseignant().persId()
						.equals(user.getPersId());
			} else {
				if (user.isGrilleur(ann)) {
					return true;
				}
				// c'est un enseignant, liste non filtrée
				return false;

			}
		} else {
			return false;
		}
	}

	public ChooseAPEditVoeuxDelegateCtr getTreeDelegate() {
		if (isFilteredMaquette()) {
			return ((Session) session())
					.currentFilteredChooseAPEditVoeuxDelegateCtr();
		}
		return ((Session) session()).currentChooseAPEditVoeuxDelegateCtr();
	}

	public void setTreeDelegate(ChooseAPEditVoeuxDelegateCtr treeDelegate) {
		if (treeDelegate instanceof FilteredChooseAPEditVoeuxDelegateCtr) {
			((Session) session())
					.setCurrentFilteredChooseActivUeDelegateCtr((FilteredChooseAPEditVoeuxDelegateCtr) treeDelegate);
		}
		((Session) session())
				.setCurrentChooseAPEditVoeuxDelegateCtr(treeDelegate);
	}

	/**
	 * @return the diplomePickerDelegate
	 */
	public DiplomePickerDefaultDelegate diplomePickerDelegate() {
		if (isFilteredMaquette()) {
			return ((Session) session()).currentFilteredDiplomePickerDelegate();
		}
		return ((Session) session()).currentDiplomePickerDelegate();
	}

	/**
	 * @param diplomePickerDelegate
	 *            the diplomePickerDelegate to set
	 */
	public void setDiplomePickerDelegate(
			DiplomePickerDefaultDelegate diplomePickerDelegate) {
		if (diplomePickerDelegate instanceof FilteredDiplomePickerDelegate) {
			((Session) session())
					.setCurrentDiplomePickerDelegate(diplomePickerDelegate);
		} else {
			((Session) session())
					.setCurrentFilteredDiplomePickerDelegate((FilteredDiplomePickerDelegate) diplomePickerDelegate);
		}
	}

	@Override
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public NSArray<EOScolMaquetteAp> getLstApDispos() {
		if (hasBinding("lstApDispos"))
			return (NSArray<EOScolMaquetteAp>) valueForBinding("lstApDispos");
		return null;
	}

	public void setLstApDispos(NSArray<EOScolMaquetteAp> lstApDispos) {
		if (canSetValueForBinding("lstApDispos"))
			setValueForBinding(lstApDispos, "lstApDispos");
	}

	@SuppressWarnings("unchecked")
	public NSArray<EOActivite> getLstActivDispos() {
		if (hasBinding("lstActivDispos"))
			return (NSArray<EOActivite>) valueForBinding("lstActivDispos");
		return null;
	}

	public void setLstActivDispos(NSArray<EOActivite> lstActivDispos) {
		if (canSetValueForBinding("lstActivDispos"))
			setValueForBinding(lstActivDispos, "lstActivDispos");
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

	public String apOccurLibelle() {
		return apOccur.mapLibelle()
				+ " ("
				+ apOccur.toFwkScolarite_ScolMaquetteHoraireCode()
						.mhcoAbreviation()
				+ " de "
				+ apOccur.mapValeur()
				+ "hr * "
				+ Math.max(1, (apOccur().mapGroupeReel() != null ? apOccur()
						.mapGroupeReel() : apOccur().mapGroupePrevu()))
				+ "grp)";
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

	public WOActionResults setSearchOn() {
		setSearchMode(true);
		if (dgSearch() != null)
			dgSearch().clearSelection();
		AjaxModalDialog.open(context(), searchZoneId(),
				"Chercher un enseignant");
		return null;
	}

	public WOActionResults selectEnsFromSearch() {
		editedVoeux().setToFwkpers_IndividuEnseignantRelationship(
				(EOIndividu) searchedEnseigant());
		if ((editedVoeux().toFwkpers_IndividuEnseignant() != null)
				&& (((Session) session()).getSelectedEns() == null)) {
			((Session) session()).setIndividuForEnseigant(editedVoeux()
					.toFwkpers_IndividuEnseignant());
		}
		if ((editedVoeux().toFwkScolarite_ScolariteFwkScolMaquetteAp() != null)
				&& (((Session) session()).getSelectedEc() == null)) {
			((Session) session()).setSelectedEc(editedVoeux()
					.toFwkScolarite_ScolariteFwkScolMaquetteAp()
					.toOneFwkScolarite_ScolMaquetteEC());
		}
		if ((editedVoeux().toActivite() != null)
				&& (((Session) session()).getSelectedActiv() == null)) {
			((Session) session()).setSelectedActiv(editedVoeux().toActivite());
		}
		AjaxUpdateContainer.updateContainerWithID("erreureditvoeux", context());
		AjaxUpdateContainer
				.updateContainerWithID(aucbuttonszoneid(), context());
		return setSearchOff();
	}

	public WOActionResults setSearchOff() {
		setSearchMode(false);
		AjaxModalDialog.close(context());
		AjaxUpdateContainer.updateContainerWithID(aucSearch(), context());

		return null;
	}

	private WODisplayGroup dgSearch;

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

	public String idWindow() {
		return (String) valueForBinding("idWindow");
	}

	public EOQualifier enseignantsQualifier() {
		return FinderEnseignant.qualEnseignant(((Session) session())
				.selectedYear());
	}

	private Double etDemi;

	/**
	 * @return the etDemi
	 */
	public Double etDemi() {
		if ((editedVoeux() != null) && (editedVoeux().nbHeuresVoeux() != null)) {
			if (Double.valueOf(nbHeuresVoeux() + "").equals(
					editedVoeux().nbHeuresVoeux()))
				etDemi = Double.valueOf(0);
			else
				etDemi = Double.valueOf(0.5);
		} else
			etDemi = null;
		return etDemi;
	}

	/**
	 * @param etDemi
	 *            the etDemi to set
	 */
	public void setEtDemi(Object etDemi) {

		if (etDemi != null) {
			if (etDemi instanceof String) {
				this.etDemi = Double.valueOf((String) etDemi);
			} else {
				this.etDemi = (Double) etDemi;
			}
		} else {
			this.etDemi = null;
		}

		if ((editedVoeux() != null) && (nbHeuresVoeux() != null)) {
			editedVoeux()
					.setNbHeuresVoeux(
							nbHeuresVoeux()
									+ ((this.etDemi != null ? this.etDemi : 0)));
		}
	}

	private Integer nbHeuresVoeux;
	private EOActivite activOccur;

	/**
	 * @return the nbHeuresVoeux
	 */
	public Integer nbHeuresVoeux() {
		if ((editedVoeux() != null) && (editedVoeux().nbHeuresVoeux() != null))
			nbHeuresVoeux = editedVoeux().nbHeuresVoeux().intValue();
		else
			nbHeuresVoeux = null;
		return nbHeuresVoeux;
	}

	/**
	 * @param nbHeuresVoeux
	 *            the nbHeuresVoeux to set
	 */
	public void setNbHeuresVoeux(Integer nbHeuresVoeux) {
		this.nbHeuresVoeux = (nbHeuresVoeux!=null?nbHeuresVoeux:0);
		editedVoeux().setNbHeuresVoeux(
				nbHeuresVoeux + Double.valueOf((etDemi != null ? etDemi : 0)));
	}

	public String searchZoneId() {
		return getComponentId() + "_searchZoneId";
	}

	public String aucSearch() {
		return getComponentId() + "_aucSearchid";
	}

	/**
	 * @return the activOccur
	 */
	public EOActivite activOccur() {
		return activOccur;
	}

	/**
	 * @param activOccur
	 *            the activOccur to set
	 */
	public void setActivOccur(EOActivite activOccur) {
		this.activOccur = activOccur;
	}

	public String libActiviteOccur() {
		return activOccur().commentaire() + " - "
				+ activOccur().toTypeActivite().libCourt() + " ("
				+ activOccur().nbHeuresActivite() + "hr)";
	}

	public String aucinfosapid() {
		return getComponentId() + "_aucinfosapid";
	}

	private NSMutableDictionary<EOScolMaquetteAp, BigDecimal> totalVoeuxAp;

	public BigDecimal totalVoeuxAp() {
		if (totalVoeuxAp == null) {
			totalVoeuxAp = new NSMutableDictionary<EOScolMaquetteAp, BigDecimal>();
		}
		if (editedVoeux().toFwkScolarite_ScolariteFwkScolMaquetteAp() == null) {
			return null;
		}
		if (!totalVoeuxAp.containsKey(editedVoeux()
				.toFwkScolarite_ScolariteFwkScolMaquetteAp())) {
			calculTotalAp();
		}
		return totalVoeuxAp.objectForKey(editedVoeux()
				.toFwkScolarite_ScolariteFwkScolMaquetteAp());
	}

	private NSMutableDictionary<EOScolMaquetteAp, BigDecimal> totalDispoAp;

	public BigDecimal totalDispoAp() {
		if (totalDispoAp == null) {
			totalDispoAp = new NSMutableDictionary<EOScolMaquetteAp, BigDecimal>();
		}
		if (editedVoeux().toFwkScolarite_ScolariteFwkScolMaquetteAp() == null) {
			return null;
		}
		if (!totalDispoAp.containsKey(editedVoeux()
				.toFwkScolarite_ScolariteFwkScolMaquetteAp())) {
			calculTotalAp();
		}
		return totalDispoAp.objectForKey(editedVoeux()
				.toFwkScolarite_ScolariteFwkScolMaquetteAp());
	}

	public WOActionResults calculTotalAp() {
		EOScolMaquetteAp ap = editedVoeux()
				.toFwkScolarite_ScolariteFwkScolMaquetteAp();
		if (ap != null) {
			if (totalDispoAp == null) {
				totalDispoAp = new NSMutableDictionary<EOScolMaquetteAp, BigDecimal>();
			}
			if (totalVoeuxAp == null) {
				totalVoeuxAp = new NSMutableDictionary<EOScolMaquetteAp, BigDecimal>();
			}
			totalVoeuxAp.setObjectForKey(FinderVoeux.getTotalVoeuxForAp(
					editedVoeux().editingContext(), ap), ap);
			totalDispoAp.setObjectForKey(
					ap.mapValeur().multiply(
							BigDecimal.valueOf(Math.max(
									1,
									(ap.mapGroupeReel() != null ? ap
											.mapGroupeReel() : ap
											.mapGroupePrevu())))), ap);
		}

		return null;
	}

	public String aucinfosactivid() {
		return getComponentId() + "_aucinfosactivid";
	}

	private BigDecimal totalVoeuxActiv;

	public BigDecimal totalVoeuxActiv() {
		if (totalVoeuxActiv == null) {
			calculTotalActiv();
		}
		return totalVoeuxActiv;
	}

	public WOActionResults calculTotalActiv() {
		EOActivite activ = editedVoeux().toActivite();
		totalVoeuxActiv = FinderVoeux.getTotalVoeuxForActivite(editedVoeux()
				.editingContext(), activ);

		return null;
	}

	public WOActionResults closeSearchWin() {
		AjaxModalDialog.close(context());
		calculTotalAp();
		AjaxUpdateContainer.updateContainerWithID(aucinfosapid(), context());
		AjaxUpdateContainer
				.updateContainerWithID(aucbuttonszoneid(), context());
		AjaxUpdateContainer.updateContainerWithID(aucValidVoeuxId(), context());
		return null;
	}

	public WOActionResults closeSearchWinActiv() {
		AjaxModalDialog.close(context());
		calculTotalActiv();
		AjaxUpdateContainer.updateContainerWithID(aucinfosactivid(), context());
		AjaxUpdateContainer
				.updateContainerWithID(aucbuttonszoneid(), context());
		AjaxUpdateContainer.updateContainerWithID(aucValidVoeuxId(), context());
		return null;
	}

	private boolean isRealiseMode() {
		if (hasBinding(BIND_MODE)) {
			if (MODE_REALISE.equals(valueForBinding(BIND_MODE))) {
				return true;
			}
		}
		return false;
	}

	private boolean isCreatRealiseMode() {
		if (hasBinding(BIND_MODE)) {
			if (MODE_CREAT_REALISE.equals(valueForBinding(BIND_MODE))) {
				return true;
			}
		}
		return false;
	}

	private boolean isEditionMode() {
		if (hasBinding(BIND_MODE)) {
			if (MODE_EDITION.equals(valueForBinding(BIND_MODE))) {
				return true;
			}
		}
		return false;
	}

	public boolean canChangeEnseignant() {
		if (isRealiseMode()) {
			return false;
		}
		if (isEditionMode()) {
			return false;
		}
		if (hasBinding(BIND_CAN_CHANGE_ENS)) {
			return (Boolean) valueForBinding(BIND_CAN_CHANGE_ENS);
		}
		return false;
	}

	public String aucactionid() {
		return getComponentId() + "_aucactionid";
	}

	public String aucapid() {
		return getComponentId() + "_aucapid";
	}

	public String aucactivid() {
		return getComponentId() + "_aucactivid";
	}

	public ChooseActiviteDelegateCtr getActivTreeDelegate() {
		if (isFilteredMaquette()) {
			return ((Session) session())
					.currentFilteredChooseActiviteDelegateCtr();
		}

		return ((Session) session()).currentChooseActiviteDelegateCtr();
	}

	public void setActivTreeDelegate(ChooseActiviteDelegateCtr activTreeDelegate) {
		if (activTreeDelegate instanceof FilteredChooseActiviteDelegateCtr) {
			((Session) session())
					.setCurrentFilteredChooseActiviteDelegateCtr((FilteredChooseActiviteDelegateCtr) activTreeDelegate);
		}
		((Session) session())
				.setCurrentChooseActiviteDelegateCtr(activTreeDelegate);
	}

	public boolean haveMultiActiv() {
		return ((getLstActivDispos() != null) && (getLstActivDispos().size() > 1));
	}

	private EOActivite getSelectedActivite() {
		if ((!haveMultiActiv()) && (getLstActivDispos() != null)) {
			return ((EOActivite) getLstActivDispos().lastObject());
		}
		return null;
	}

	public String activiteLib() {
		return (getSelectedActivite() != null ? getSelectedActivite()
				.libelleActivite() : null);
	}

	private IPersonne searchedEnseigant;
	private NSMutableArray<Double> lstEtDemi;
	private Double etDemiOccur;

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

	public boolean isServiceValided() {
		if (editedVoeux().toFwkpers_IndividuEnseignant() != null) {
			return Enseignant.isServiceValidForEnsAndYear(session()
					.defaultEditingContext(), editedVoeux()
					.toFwkpers_IndividuEnseignant(), editedVoeux()
					.toFwkScolarite_ScolFormationAnnee());
		}
		return false;
	}

	public String aucbuttonszoneid() {
		return getComponentId() + "_aucbuttonszoneid";
	}

	public boolean isEcLocked() {
		if (editedVoeux().toFwkScolarite_ScolariteFwkScolMaquetteAp() != null) {
			return EOEcVerrous.IsEcLocked(editedVoeux()
					.toFwkScolarite_ScolariteFwkScolMaquetteAp()
					.toOneFwkScolarite_ScolMaquetteEC());
		}
		return false;
	}

	public boolean isVoeuxLocked() {
		if ("O".equals(editedVoeux().valide())) {
			// voeux validé : locked si possede pas les droits
			return (!(((Session) session()).getGUser()
					.haveEditRightOnObjMaquetteForVoeux(editedVoeux(),
							editedVoeux().toFwkScolarite_ScolFormationAnnee())));
		}
		return false;
	}

	public boolean canValid() {

		if (isRealiseMode() || isCreatRealiseMode()) {
			return ((Session) session()).getGUser().canRealiseVoeux(
					editedVoeux());
		} else {
			return (!isServiceValided()) && (!isEcLocked())
					&& (!isEcForActivLocked()) && (!isVoeuxLocked());
		}

	}

	/**
	 * @return the msgCannotValid
	 */
	public String msgCannotValid() {
		String msg = "";
		if (isRealiseMode() || isCreatRealiseMode()) {
			if (!"O".equals(editedVoeux().valide())) {
				msg = "Le voeux n'est pas validé";
			} else if ((editedVoeux().toFwkpers_IndividuEnseignant() == null)) {
				msg = "Choisissez un enseignant";
			} else if ((editedVoeux().toFwkpers_IndividuEnseignant() != null)
					&& (!Enseignant.isServiceValidForEnsAndYear(editedVoeux()
							.editingContext(), editedVoeux()
							.toFwkpers_IndividuEnseignant(), editedVoeux()
							.toFwkScolarite_ScolFormationAnnee()))) {
				msg = "Le service de l'enseignant n'est pas validé";

			} else if (!((Session) session()).getGUser()
					.haveEditRightOnObjMaquetteForVoeux(editedVoeux(),
							editedVoeux().toFwkScolarite_ScolFormationAnnee())) {
				msg = "Vous n'avez pas les droits nécessaires ";
			}

		} else {
			if (isServiceValided()) {
				msg = "Le service de l'enseignant est valid&eacute;, l'ajout ou la modification de voeux est impossible.";
			}
			if (isEcLocked()) {
				if (!"".equals(msg)) {
					msg += "<br />\n";
				}
				msg += "L'E.C est verrouillé, l'ajout ou la modification de voeux est impossible.";
			}
			if (isEcForActivLocked()) {
				if (!"".equals(msg)) {
					msg += "<br />\n";
				}
				msg += "L'activité est liée à un E.C verrouillé, l'ajout ou la modification de voeux est impossible.";
			}
			if (isVoeuxLocked()) {
				if (!"".equals(msg)) {
					msg += "<br />\n";
				}
				msg += "Le voeux est validé, la modification du voeux est impossible.";
			}
		}
		return msg;
	}

	public boolean isTreeItemEcLocked() {
		if ((getTreeDelegate() != null)
				&& (getTreeDelegate().treeItem() != null)
				&& (getTreeDelegate().treeItem().getEoObject() != null)
				&& (getTreeDelegate().treeItem().getEoObject() instanceof EOScolMaquetteEc)) {
			return EOEcVerrous.IsEcLocked((EOScolMaquetteEc) getTreeDelegate()
					.treeItem().getEoObject());
		}
		return false;

	}

	public boolean isTreeItemActivEcLocked() {
		if ((getActivTreeDelegate() != null)
				&& (getActivTreeDelegate().treeItem() != null)
				&& (getActivTreeDelegate().treeItem().getEoObject() != null)
				&& (getActivTreeDelegate().treeItem().getEoObject() instanceof EOScolMaquetteEc)) {
			return EOEcVerrous
					.IsEcLocked(((EOScolMaquetteEc) getActivTreeDelegate()
							.treeItem().getEoObject()));
		}
		if ((getActivTreeDelegate() != null)
				&& (getActivTreeDelegate().treeItem() != null)
				&& (getActivTreeDelegate().treeItem().getEoObject() != null)
				&& (getActivTreeDelegate().treeItem().getEoObject() instanceof EOActivite)
				&& (((EOActivite) getActivTreeDelegate().treeItem()
						.getEoObject()).toFwkScolarite_ScolMaquetteEc() != null)) {
			return EOEcVerrous.IsEcLocked(((EOActivite) getActivTreeDelegate()
					.treeItem().getEoObject()).toFwkScolarite_ScolMaquetteEc());
		}
		return false;

	}

	/**
	 * @return the lstEtDemi
	 */
	public NSMutableArray<Double> lstEtDemi() {
		if (lstEtDemi == null) {
			lstEtDemi = new NSMutableArray<Double>();
			lstEtDemi.addObject(Double.valueOf(0));
			lstEtDemi.addObject(Double.valueOf(0.5));
		}
		return lstEtDemi;
	}

	/**
	 * @param lstEtDemi
	 *            the lstEtDemi to set
	 */
	public void setLstEtDemi(NSMutableArray<Double> lstEtDemi) {
		this.lstEtDemi = lstEtDemi;
	}

	/**
	 * @return the etDemiOccur
	 */
	public Double etDemiOccur() {
		return etDemiOccur;
	}

	/**
	 * @param etDemiOccur
	 *            the etDemiOccur to set
	 */
	public void setEtDemiOccur(Double etDemiOccur) {
		this.etDemiOccur = etDemiOccur;
	}

	protected NSNumberFormatter hrFormat = new NSNumberFormatter(".00");
	private NSArray<String> changeApIds;
	private NSArray<String> changeActivIds;
	private String editedVoeuxValide;

	public String displayEtDemi() {

		return hrFormat.format(etDemiOccur);
	}

	public boolean isEcForActivLocked() {
		if ((editedVoeux().toActivite() != null)
				&& (editedVoeux().toActivite().toFwkScolarite_ScolMaquetteEc() != null)) {
			return EOEcVerrous.IsEcLocked(editedVoeux().toActivite()
					.toFwkScolarite_ScolMaquetteEc());
		}
		return false;

	}

	public String onSuccessValid() {
		return "function (ok) {" + functionRefresh() + ";}";
	}

	public String functionRefresh() {
		return (String) valueForBinding("functionRefresh");
	}

	public void setFunctionRefresh(String functionRefresh) {
		setValueForBinding(functionRefresh, "functionRefresh");
	}

	public boolean canChangeAp() {
		if (isRealiseMode()) {
			return false;
		}
		if (isEditionMode()) {
			return false;
		}
		return true;
	}

	public boolean canNotChangeAp() {
		return !canChangeAp();
	}

	public String aucValidVoeuxId() {
		return getComponentId() + "_aucValidVoeuxId";
	}

	public String aucTriggerChangeApid() {
		return getComponentId() + "_AucTriggerChangeApid";
	}

	public String aucTriggerChangeActivid() {
		return getComponentId() + "_AucTriggerChangeActivid";
	}

	/**
	 * @return the changeApIds
	 */
	public NSArray<String> changeApIds() {
		if (changeApIds == null) {
			changeApIds = new NSArray<String>(new String[] { aucinfosapid(),
					aucValidVoeuxId(), aucrealiseid() });
		}
		return changeApIds;
	}

	/**
	 * @param changeApIds
	 *            the changeApIds to set
	 */
	public void setChangeApIds(NSArray<String> changeApIds) {
		this.changeApIds = changeApIds;
	}

	/**
	 * @return the changeActivIds
	 */
	public NSArray<String> changeActivIds() {
		if (changeActivIds == null) {
			changeActivIds = new NSArray<String>(new String[] {
					aucinfosactivid(), aucValidVoeuxId(), aucrealiseid() });
		}
		return changeActivIds;
	}

	/**
	 * @param changeActivIds
	 *            the changeActivIds to set
	 */
	public void setChangeActivIds(NSArray<String> changeActivIds) {
		this.changeActivIds = changeActivIds;
	}

	/**
	 * @return the editedVoeuxValide
	 */
	public String editedVoeuxValide() {
		if (editedVoeuxValide == null) {
			if (editedVoeux() != null) {
				if ("O".equals(editedVoeux().valide())) {
					editedVoeuxValide = editedVoeux().valide();
				} else {
					editedVoeuxValide = "N";
				}
			}
		}
		return editedVoeuxValide;
	}

	/**
	 * @param editedVoeuxValide
	 *            the editedVoeuxValide to set
	 */
	public void setEditedVoeuxValide(String editedVoeuxValide) {
		this.editedVoeuxValide = editedVoeuxValide;
	}

	public boolean canChangeHrVoeux() {
		if ((isRealiseMode()) || (isCreatRealiseMode())) {
			return false;
		}
		return true;
	}

	public boolean CanShowRealise() {
		if (isRealiseMode() || isCreatRealiseMode()) {
			return true;
		}
		if ("O".equals(editedVoeux().valide())) {
			if ((editedVoeux().toFwkpers_IndividuEnseignant() != null)
					&& (Enseignant.isServiceValidForEnsAndYear(session()
							.defaultEditingContext(), editedVoeux()
							.toFwkpers_IndividuEnseignant(),
							((Session) session()).selectedYear()))) {
				return true;
			}
		}
		return false;
	}

	public boolean canChangeRealise() {

		return (isRealiseMode() || isCreatRealiseMode())
				&& ((Session) session()).getGUser().canRealiseVoeux(
						editedVoeux());
	}

	private Double etDemiRealise;

	/**
	 * @return the etDemiRealise
	 */
	public Double etDemiRealise() {
		if ((editedVoeux() != null) && (editedVoeux().nbHeureRealise() != null)) {
			if (Double.valueOf(nbHeuresRealise() + "").equals(
					editedVoeux().nbHeureRealise()))
				etDemiRealise = Double.valueOf(0);
			else
				etDemiRealise = Double.valueOf(0.5);
		} else
			etDemiRealise = null;
		return etDemiRealise;
	}

	/**
	 * @param etDemiRealise
	 *            the etDemi to set
	 */
	public void setEtDemiRealise(Object etDemiRealise) {

		if (etDemiRealise != null) {
			if (etDemiRealise instanceof String) {
				this.etDemiRealise = Double.valueOf((String) etDemiRealise);
			} else {
				this.etDemiRealise = (Double) etDemiRealise;
			}
		} else {
			this.etDemiRealise = null;
		}

		if ((editedVoeux() != null) && (nbHeuresRealise() != null)) {
			editedVoeux().setNbHeureRealise(
					nbHeuresRealise()
							+ ((this.etDemiRealise != null ? this.etDemiRealise
									: 0)));
		}
	}

	private Integer nbHeuresRealise;

	/**
	 * @return the nbHeuresRealise
	 */
	public Integer nbHeuresRealise() {
		if ((editedVoeux() != null) && (editedVoeux().nbHeureRealise() != null))
			nbHeuresRealise = (editedVoeux().nbHeureRealise() != null ? editedVoeux()
					.nbHeureRealise().intValue() : nbHeuresVoeux());
		else
			nbHeuresRealise = null;
		return nbHeuresRealise;
	}

	public void setNbHeuresRealise(Integer nbHeuresVoeux) {
		this.nbHeuresRealise = (nbHeuresVoeux!=null?nbHeuresVoeux:0);
		editedVoeux()
				.setNbHeureRealise(
						nbHeuresRealise
								+ Double.valueOf((etDemi != null ? etDemi : 0)));
	}

	public String aucrealiseid() {
		return getComponentId() + "_aucrealiseid";
	}

}