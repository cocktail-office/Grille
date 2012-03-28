package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.finder.FinderActivite;
import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteResponsableEc;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseApDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseEcDelegateCtr;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxModalDialog;
import er.extensions.eof.ERXEC;

public class GestionEC extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 585687264500642631L;

	private NSArray<String> lstMajValidZones;

	private EOActivite editedActivite;

	private String classLigne;
	private WODisplayGroup displayGroup;

	public GestionEC(WOContext context) {
		super(context);
		lstMajValidZones = new NSArray<String>(new String[] { "aucgrps",
				"ErreurContainer" });
		setLstEditZones(new NSArray<String>(new String[] { "editactivite",
		"aucactivites" }));

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
		lstAps = null;
		//lstDiplomesEc = null;
		lstActivites = null;
		((Session) session()).setSelectedEc(selectedEc);
	}

	private NSArray<EOScolMaquetteAp> lstAps;

	/**
	 * @return the lstAps
	 */
	public NSArray<EOScolMaquetteAp> lstAps() {
		if ((lstAps == null) && (selectedEc() != null))
			lstAps = FinderScolMaquetteEc.getApForEcAndYear(getEcEdit(),
					selectedEc(), selectedEc()
							.toFwkScolarite_ScolFormationAnnee());
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

	/*
	 * private boolean isChanges = false;
	 * 
	 * 
	 * public boolean isChanges() { return isChanges; }
	 * 
	 * 
	 * public void setChanges(boolean isChanges) { this.isChanges = isChanges; }
	 * //
	 */

	public WOActionResults editGrp() {
		setEdited(true);
		return null;
	}

	public NSArray<String> getLstMajValidZones() {
		return lstMajValidZones;
	}

	public boolean canEditGrp() {
		return (!isEdited() && (selectedEc() != null)
				&& ((Session) session()).getGUser().canEditEC(selectedEc())
				&& (lstAps() != null) && (lstAps().size() > 0));
	}

	public WOActionResults test() {
		return null;
	}

	public int coutApOccurPrevu() {
		return (apOccur.mapValeur().intValue() * Math.max(1, apOccur
				.mapGroupePrevu().intValue()));
	}

	public int coutApOccurReel() {
		return (apOccur.mapValeur().intValue() * Math.max(1, apOccur
				.mapGroupeReel().intValue()));
	}

	private NSArray<EOActivite> lstActivites;

	/**
	 * @return the lstActivites
	 */
	public NSArray<EOActivite> lstActivites() {
		if ((lstActivites == null) && (selectedEc() != null))
			lstActivites = FinderActivite.getActivitesForEcAndYear(getEcEdit(),
					selectedEc(), selectedEc()
							.toFwkScolarite_ScolFormationAnnee());
		return lstActivites;
	}

	/**
	 * @param lstActivites
	 *            the lstActivites to set
	 */
	public void setLstActivites(NSArray<EOActivite> lstActivites) {
		this.lstActivites = lstActivites;
	}

	private EOActivite activiteOccur;

	/**
	 * @return the activiteOccur
	 */
	public EOActivite activiteOccur() {
		return activiteOccur;
	}

	/**
	 * @param activiteOccur
	 *            the activiteOccur to set
	 */
	public void setActiviteOccur(EOActivite activiteOccur) {
		this.activiteOccur = activiteOccur;
	}

	public boolean canEditActivite() {
		return (selectedEc() != null)
				&& getgUser().canEditActivite(activiteOccur);
	}

	public WOActionResults editActivite(Object act) {
		this.editedActivite = (EOActivite) act;
		displayGroup().setSelectedObject(this.editedActivite);
		CktlAjaxWindow.open(context(), caweditobjid(),
				"Modification d'une activité");

		return null;
	}

	public boolean canAddActivite() {
		return getgUser().canAddActivite(((Session) session()).selectedYear())
				&& (selectedEc() != null)
				&& (getgUser().canEditEC(selectedEc()));
	}

	public WOActionResults AddActivite() {
		EOEditingContext newActivEc = ERXEC.newEditingContext(getEcEdit());
		setEditedActivite(EOActivite.createActivite(newActivEc, null, null,
				null));
		editedActivite
				.setToFwkScolarite_ScolMaquetteEcRelationship(selectedEc()
						.localInstanceIn(editedActivite.editingContext()));
		editedActivite.setTypeLien(EOActivite.TYPE_LIEN_EC);
		editedActivite
				.setToFwkScolarite_ScolFormationAnneeRelationship(((Session) session())
						.selectedYear().localInstanceIn(
								editedActivite.editingContext()));
		lstActivites = null;
		CktlAjaxWindow.open(context(), caweditobjid(), "Ajout d'une activité");

		return null;
	}

	/**
	 * @return the editedActivite
	 */
	public EOActivite editedActivite() {
		return editedActivite;
	}

	/**
	 * @param editedActivite
	 *            the editedActivite to set
	 */
	public void setEditedActivite(EOActivite editedActivite) {
		this.editedActivite = editedActivite;
	}

	public boolean isActiviteEdited() {
		return (editedActivite != null);
	}

	public String classLigne() {
		classLigne = ("row1".equals(classLigne) ? "row2" : "row1");
		/*
		 * if (isActiviteDeleted()) return "deleted"; //
		 */
		return classLigne;

	}

	/*
	 * public WOActionResults deleteActivite() {
	 * deletedActivites.addObject(activiteOccur); return null; }
	 * 
	 * public boolean isActiviteDeleted() { return
	 * deletedActivites.containsObject(activiteOccur); }
	 * 
	 * public WOActionResults restoreActivite() {
	 * deletedActivites.removeObject(activiteOccur); return null; }
	 * 
	 * public NSMutableArray<EOActivite> getDeletedActivites() { return
	 * deletedActivites; }
	 * 
	 * @Override public WOActionResults cancelSave() {
	 * deletedActivites.removeAllObjects(); return super.cancelSave(); }
	 * 
	 * @Override public WOActionResults commitSave() { // del des objets
	 * supprimés for (EOActivite delAct : deletedActivites) {
	 * delAct.deleteLien(null); delAct.editingContext().deleteObject(delAct); }
	 * deletedActivites.removeAllObjects();
	 * 
	 * return super.commitSave(); }
	 * 
	 * //
	 */
	public WODisplayGroup displayGroup() {
		if (displayGroup == null) {
			displayGroup = new WODisplayGroup();
			// displayGroup.setDelegate(new DgDelegate());
		}
		if (lstActivites() != null)

			displayGroup.setObjectArray(lstActivites());
		displayGroup.clearSelection();
		return displayGroup;
	}

	public void setDisplayGroup(WODisplayGroup disp) {
		displayGroup = disp;
	}

	/*
	 * public class DgDelegate { public void
	 * displayGroupDidChangeSelection(WODisplayGroup group) {
	 * setSelectedActivite((EOActivite) group.selectedObject()); } } //
	 */

	public WOComponent editMethodeObject() {
		return this;
	}

	public WOActionResults refreshActivites() {
		lstActivites = null;
		displayGroup.setObjectArray(lstActivites());
		displayGroup.clearSelection();
		return null;
	}

	public String caweditobjid() {
		return getComponentId() + "_caweditobjcid";
	}

	public String aucverrouid() {
		return getComponentId() + "_aucverrouid";
	}

}