package org.cocktail.grille.serveur.components;

import java.math.BigDecimal;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOEcVerrous;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxModalDialog;
import er.ajax.AjaxUpdateContainer;
import er.extensions.eof.ERXEC;

public class PageGestVoeuxActiv extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8584661259266065972L;

	public PageGestVoeuxActiv(WOContext context) {
		super(context);
	}

	public String aucactivid() {
		return getComponentId() + "_aucactivid";
	}

	public EOActivite selectedActiv() {
		return ((Session) session()).getSelectedActiv();
	}

	public void setSelectedActiv(EOActivite selectedActiv) {
		totalHeuresVoeux = null;
		((Session) session()).setSelectedActiv(selectedActiv);
	}

	public ChooseActiviteDelegateCtr getActivTreeDelegate() {
		return ((Session) session()).currentChooseActiviteDelegateCtr();
	}

	public void setActivTreeDelegate(ChooseActiviteDelegateCtr activTreeDelegate) {
		((Session) session())
				.setCurrentChooseActiviteDelegateCtr(activTreeDelegate);
	}

	public WOActionResults closeSearchWinActiv() {
		AjaxModalDialog.close(context());

		return null;
	}

	private WODisplayGroup dgVoeuxActiv;

	public WODisplayGroup dgVoeuxActiv() {
		if (dgVoeuxActiv == null) {
			dgVoeuxActiv = new WODisplayGroup();
			// displayGroup.setDelegate(new DgDelegate());
		}
		if (selectedActiv() != null) {
			dgVoeuxActiv.setObjectArray(selectedActiv().toVoeuxs());
		} else {
			dgVoeuxActiv.setObjectArray(null);
		}
		dgVoeuxActiv.clearSelection();
		return dgVoeuxActiv;
	}

	public void setDgVoeuxActiv(WODisplayGroup dgVoeuxActiv) {
		this.dgVoeuxActiv = dgVoeuxActiv;
	}

	public String colonnesVoeuxActivKeys() {
		return VoeuxTbV.COL_INDIV_KEY + "," + VoeuxTbV.COL_NB_HEURES_VOEUX_KEY
				+ "," + VoeuxTbV.COL_NB_HEURES_REALISE_KEY + ","
				+ VoeuxTbV.COL_DAT_CREATION_KEY + "," + VoeuxTbV.COL_VALIDE_KEY
				+ "," + VoeuxTbV.OBJ_KEY + ".action";
	}

	public WOActionResults addVoeuxActiv() {
		Session sess = ((Session) session());
		EOEditingContext ecVoeux = ERXEC.newEditingContext(session()
				.defaultEditingContext());
		setEditedVoeux(EOVoeux.createVoeux(ecVoeux, currentYear()
				.localInstanceIn(ecVoeux)));

		editedVoeux
				.setToFwkpers_IndividuCreateurRelationship(((Session) session())
						.getGUser().individu().localInstanceIn(ecVoeux));

		if (selectedActiv() != null) {
			editedVoeux.setToActiviteRelationship(selectedActiv()
					.localInstanceIn(ecVoeux));
		}

		if (sess.getSelectedEns() != null) {
			if (sess.getGUser().haveRightForObjMaquette(selectedActiv(),
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
		setModeEditVoeux(EditVoeux.MODE_CREATE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un voeux");
		return null;
	}
	
	
	public WOActionResults addRealiseActiv() {
		Session sess = ((Session) session());
		EOEditingContext ecVoeux = ERXEC.newEditingContext(session()
				.defaultEditingContext());
		setEditedVoeux(EOVoeux.createVoeux(ecVoeux, currentYear()
				.localInstanceIn(ecVoeux)));

		editedVoeux
				.setToFwkpers_IndividuCreateurRelationship(((Session) session())
						.getGUser().individu().localInstanceIn(ecVoeux));

		if (selectedActiv() != null) {
			editedVoeux.setToActiviteRelationship(selectedActiv()
					.localInstanceIn(ecVoeux));
		}

		if (sess.getSelectedEns() != null) {
			if (sess.getGUser().haveRightForObjMaquette(selectedActiv(),
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
		editedVoeux.setValide("O");
		
		setModeEditVoeux(EditVoeux.MODE_CREAT_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un réalisé");
		return null;
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

	public boolean canEditVoeux() {
		return ((!isVerrouEC()) && (selectedActiv() != null)
				&& (voeuxOccur() != null) && ((Session) session()).getGUser()
				.canEditVoeux(voeuxOccur()));
	}

	public WOComponent editMethodeObject() {
		return this;
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

	public WOActionResults editVoeuxActiv(Object voeux) {
		setEditedVoeux((EOVoeux) voeux);
		dgVoeuxActiv().setSelectedObject(this.editedVoeux);
		setModeEditVoeux(EditVoeux.MODE_EDITION);
		CktlAjaxWindow.open(context(), caweditvoeuxid(),
				"Modification d'un voeux sur activité");

		return null;
	}

	public WOActionResults editRealiseActiv(Object voeux) {
		setEditedVoeux((EOVoeux) voeux);
		dgVoeuxActiv().setSelectedObject(this.editedVoeux);
		setModeEditVoeux(EditVoeux.MODE_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(),
				"Modification d'un réalisé sur activité");

		return null;
	}

	public WOActionResults refreshVoeuxActiv() {
		if (dgVoeuxActiv != null) {
			dgVoeuxActiv.setObjectArray(selectedActiv().toVoeuxs());
			dgVoeuxActiv.clearSelection();
		}
		totalHeuresVoeux = null;
		AjaxUpdateContainer.updateContainerWithID("aucvoeuxactiv", context());
		AjaxUpdateContainer.updateContainerWithID(auctotalhrvoeuxid(),
				context());

		return null;
	}

	public boolean canAddVoeux() {
		return ((!isVerrouEC())
				&& getgUser().canAddVoeux(((Session) session()).selectedYear()) && (selectedActiv() != null));
	}

	public NSArray<EOActivite> lstActivs() {
		if (selectedActiv() != null) {
			return new NSArray<EOActivite>(selectedActiv());
		}
		return null;
	}

	/**
	 * @return the diplomePickerDelegate
	 */
	public DiplomePickerDefaultDelegate diplomePickerDelegate() {
		return ((Session) session()).currentDiplomePickerDelegate();
	}

	/**
	 * @param diplomePickerDelegate
	 *            the diplomePickerDelegate to set
	 */
	public void setDiplomePickerDelegate(
			DiplomePickerDefaultDelegate diplomePickerDelegate) {
		((Session) session())
				.setCurrentDiplomePickerDelegate(diplomePickerDelegate);
	}

	public boolean canChangeEnseignant() {
		// TODO
		return ((Session) session()).getGUser()
				.canChangeEnseignantForObjMaquette(
						((Session) session()).selectedYear(), selectedActiv());
	}

	private java.math.BigDecimal totalHeuresVoeux;

	/**
	 * @return the totalHeuresVoeux
	 */
	public java.math.BigDecimal totalHeuresVoeux() {
		if (totalHeuresVoeux == null) {
			totalHeuresVoeux = new BigDecimal(0);
			if ((selectedActiv() != null)
					&& (selectedActiv().toVoeuxs() != null)) {
				for (EOVoeux voeuxOccur : selectedActiv().toVoeuxs()) {
					totalHeuresVoeux = totalHeuresVoeux
							.add(BigDecimal
									.valueOf(voeuxOccur.nbHeuresVoeux() != null ? voeuxOccur
											.nbHeuresVoeux() : 0));
				}
			}
		}
		return totalHeuresVoeux;
	}

	/**
	 * @param totalHeuresVoeux
	 *            the totalHeuresVoeux to set
	 */
	public void setTotalHeuresVoeux(java.math.BigDecimal totalHeuresVoeux) {
		this.totalHeuresVoeux = totalHeuresVoeux;
	}
	
	private java.math.BigDecimal totalHeuresRealise;

	/**
	 * @return the totalHeuresRealise
	 */
	public java.math.BigDecimal totalHeuresRealise() {
		if (totalHeuresRealise == null) {
			totalHeuresRealise = new BigDecimal(0);
			if ((selectedActiv() != null)
					&& (selectedActiv().toVoeuxs() != null)) {
				for (EOVoeux voeuxOccur : selectedActiv().toVoeuxs()) {
					totalHeuresRealise = totalHeuresRealise
							.add(BigDecimal
									.valueOf(voeuxOccur.nbHeureRealise() != null ? voeuxOccur
											.nbHeureRealise() : 0));
				}
			}
		}
		return totalHeuresRealise;
	}

	public String auctotalhrvoeuxid() {
		return getComponentId() + "_auctotalhrvoeuxid";
	}

	public String deficitExcedentActiv() {
		if (selectedActiv() != null) {
			BigDecimal balance = totalHeuresVoeux().subtract(
					selectedActiv().nbHeuresActivite()).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			return ((balance.signum() == 1) ? "+" + balance : "" + balance)
					+ "h";
		}
		return "-";
	}
	
	public String bilanRealise() {
		if (selectedActiv() != null) {
			BigDecimal balance = totalHeuresRealise().subtract(
					selectedActiv().nbHeuresActivite()).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			return ((balance.signum() == 1) ? "+" + balance : "" + balance)
					+ "h";
		}
		return "-";
	}

	public boolean isVerrouEC() {
		if ((selectedActiv() != null)
				&& (selectedActiv().lien() instanceof EOScolMaquetteEc)) {
			return EOEcVerrous.IsEcLocked(selectedActiv()
					.toFwkScolarite_ScolMaquetteEc());
		}
		return false;

	}

	public boolean canAddRealiseActiv() {
		return ((selectedActiv() != null) && (getgUser().canRealiseVoeuxFor(
				selectedActiv().lien(), ((Session) session()).selectedYear())));
	}

	public Boolean canRealiseVoeuxActiv() {
		return ((selectedActiv() != null) && (voeuxOccur() != null) && getgUser()
				.canRealiseVoeux(voeuxOccur()));
	}

}