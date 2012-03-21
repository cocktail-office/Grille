package org.cocktail.grille.serveur.components;

import java.sql.Connection;
import java.util.Hashtable;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlwebapp.server.CktlResourceManager;
import org.cocktail.grille.serveur.MailMessageFactory;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOServiceEnseignant;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxUpdateContainer;

public class VoeuxEnseignant extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8292196997026350542L;

	private NSArray<String> lstMajValidZones;

	private EOScolFormationAnnee lastFetchedYear;

	public VoeuxEnseignant(WOContext context) {
		super(context);
		lstMajValidZones = new NSArray<String>(new String[] { "aucgrps",
				"ErreurContainer" });
	}

	public WOActionResults goToSearch() {
		SearchEnseignant next = (SearchEnseignant) pageWithName(SearchEnseignant.class
				.getName());
		next.setEcEdit(getEcEdit());
		next.setHaveAdminRight(haveAdminRight());
		next.setCurrentYear(getSelectedYear());
		return next;
	}

	public WOActionResults goToSearchAp() {
		ChooseAP next = (ChooseAP) pageWithName(ChooseAP.class.getName());
		next.setEcEdit(getEcEdit());
		next.setCurrentYear(currentYear());
		// next.setSelectedEc(((Session)session()).getSelectedEc());
		try {
			next.setActionLink(VoeuxEnseignant.class.getMethod("chooseAP",
					EOScolMaquetteAp.class));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return next;
	}

	public NSArray<String> getLstMajValidZones() {
		return lstMajValidZones;
	}

	public void setLstMajValidZones(NSArray<String> lstMajValidZones) {
		this.lstMajValidZones = lstMajValidZones;
	}

	public WOActionResults test() {
		return null;
	}

	private WODisplayGroup dgVoeux;

	public WODisplayGroup dgVoeux() {
		if (dgVoeux == null) {
			dgVoeux = new WODisplayGroup();
			// displayGroup.setDelegate(new DgDelegate());
		}
		if (lstVoeux() != null)

			dgVoeux.setObjectArray(lstVoeux());
		dgVoeux.clearSelection();
		return dgVoeux;
	}

	public void setDgVoeux(WODisplayGroup dgVoeux) {
		this.dgVoeux = dgVoeux;
	}

	public WOComponent editMethodeObject() {
		return this;
	}

	private Boolean showRealise;

	private Boolean showRealise() {
		if (showRealise == null) {
			showRealise = ((selectedEns() != null) && (Enseignant
					.isServiceValidForEnsAndYear(session()
							.defaultEditingContext(), selectedEns(),
							((Session) session()).selectedYear())));
		}
		return showRealise;
	}

	public String colonnesVoeuxKeys() {
		return VoeuxTbV.COL_EC_KEY
				+ ","
				+ VoeuxTbV.COL_AP_KEY
				+ ","
				+ VoeuxTbV.COL_TYPE_AP_KEY
				+ ","
				+ VoeuxTbV.COL_NB_HEURES_VOEUX_KEY
				+ ","
				+ VoeuxTbV.COL_NB_HEURES_TD_VOEUX_KEY
				+ ","
				+  VoeuxTbV.COL_NB_HEURES_REALISE_KEY + ","
						+ VoeuxTbV.COL_NB_HEURES_TD_KEY + "," 
				//+ (!showRealise() ? VoeuxTbV.COL_VALIDE_KEY + "," : "")
				+ VoeuxTbV.COL_DAT_CREATION_KEY + "," + VoeuxTbV.OBJ_KEY
				+ ".action";
	}

	public EOIndividu selectedEns() {
		return ((Session) session()).getIndividuForEnseigant();
	}

	private NSArray<EOVoeux> lstVoeux;

	/**
	 * @return the lstVoeux
	 */
	@SuppressWarnings("unchecked")
	public NSArray<EOVoeux> lstVoeux() {
		if ((lstVoeux == null) && (selectedEns() != null))
			lstVoeux = EOSortOrdering.sortedArrayUsingKeyOrderArray(
					FinderVoeux
							.getVoeuxAPForEnseignantAndYear(selectedEns()
									.editingContext(), selectedEns(),
									getSelectedYear()),
					EOVoeux.SORT_EC_AP_DATE_CREATION);
		return lstVoeux;
	}

	/**
	 * @param lstVoeux
	 *            the lstVoeux to set
	 */
	public void setLstVoeux(NSArray<EOVoeux> lstVoeux) {
		this.lstVoeux = lstVoeux;
	}

	public WOActionResults refreshVoeux() {
		lstVoeux = null;
		if (dgVoeux != null) {
			dgVoeux.setObjectArray(lstVoeux());
			dgVoeux.clearSelection();
		}
		AjaxUpdateContainer.updateContainerWithID(aucvoeux(), context());
		// pour relancer le calcul du total des voeux
		((Session) session()).resetVoeuxEnseignant();
		AjaxUpdateContainer.updateContainerWithID(aucbilanvoeuxid(), context());
		if (hasVoeuxAp()) {
			AjaxUpdateContainer.updateContainerWithID(auctotalvoeuxap(),
					context());
		}
		AjaxUpdateContainer.updateContainerWithID(AucValidServiceZoneid(),
				context());
		return null;
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

	public boolean canAddVoeux() {
		return ((selectedEns() != null) && (getgUser().canEditVoeux(
				selectedEns(), getSelectedYear())));
	}

	public WOActionResults addVoeux() {
		EOEditingContext ecVoeux = new EOEditingContext(getEcEdit());
		setEditedVoeux(EOVoeux.createVoeux(ecVoeux, getSelectedYear()
				.localInstanceIn(ecVoeux)));

		editedVoeux
				.setToFwkpers_IndividuCreateurRelationship(((Session) session())
						.getGUser().individu());
		editedVoeux.setToFwkpers_IndividuEnseignantRelationship(selectedEns());
		editedVoeux.setDCreation(new NSTimestamp());
		// editedVoeux.setValide("O");
		lstVoeux = null;
		setApSelection(true);
		setActivSelection(false);
		setModeEditVoeux(EditVoeux.MODE_CREATE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un voeux");
		return null;
	}

	public WOActionResults addRealise() {
		EOEditingContext ecVoeux = new EOEditingContext(getEcEdit());
		setEditedVoeux(EOVoeux.createVoeux(ecVoeux, getSelectedYear()
				.localInstanceIn(ecVoeux)));

		editedVoeux
				.setToFwkpers_IndividuCreateurRelationship(((Session) session())
						.getGUser().individu());
		editedVoeux.setToFwkpers_IndividuEnseignantRelationship(selectedEns());
		editedVoeux.setDCreation(new NSTimestamp());
		editedVoeux.setValide("O");
		lstVoeux = null;
		setApSelection(true);
		setActivSelection(false);
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

	private boolean isActivSelection;
	private boolean isApSelection;

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
		return (isApSelection() ? "refreshLstVoeux()" : "refreshvoeuxactiv()");
	}

	public WOActionResults onSuccessMethode() {
		return (isApSelection() ? refreshVoeux() : refreshVoeuxActiv());
	}

	public WOActionResults refreshVoeuxActiv() {
		lstVoeuxActiv = null;
		if (dgVoeuxActiv != null) {
			dgVoeuxActiv.setObjectArray(lstVoeuxActiv());
			dgVoeuxActiv.clearSelection();
		}
		AjaxUpdateContainer.updateContainerWithID(aucvoeuxactiv(), context());
		AjaxUpdateContainer.updateContainerWithID(aucbilanvoeuxid(), context());
		if (hasVoeuxActiv()) {
			AjaxUpdateContainer.updateContainerWithID(auctotalvoeuxactiv(),
					context());
		}
		// pour relancer le calcul du total des voeux
		((Session) session()).resetVoeuxEnseignant();
		AjaxUpdateContainer.updateContainerWithID(AucValidServiceZoneid(),
				context());
		return null;
	}

	private WODisplayGroup dgVoeuxActiv;

	public WODisplayGroup dgVoeuxActiv() {
		if (dgVoeuxActiv == null) {
			dgVoeuxActiv = new WODisplayGroup();
			// displayGroup.setDelegate(new DgDelegate());
		}
		if (lstVoeuxActiv() != null)

			dgVoeuxActiv.setObjectArray(lstVoeuxActiv());
		dgVoeuxActiv.clearSelection();
		return dgVoeuxActiv;
	}

	public void setDgVoeuxActiv(WODisplayGroup dgVoeuxActiv) {
		this.dgVoeuxActiv = dgVoeuxActiv;
	}

	public String colonnesVoeuxActivKeys() {
		return VoeuxTbV.COL_ACTIVITE_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_VOEUX_KEY + ","
				+ VoeuxTbV.COL_NB_HEURES_REALISE_KEY + ","
				+ VoeuxTbV.COL_VALIDE_KEY + "," + VoeuxTbV.COL_DAT_CREATION_KEY
				+ "," + VoeuxTbV.OBJ_KEY + ".action";

	}

	private NSArray<EOVoeux> lstVoeuxActiv;

	private EOVoeux voeuxActivOccur;

	private String mailToEns;

	private String modeEditVoeux;

	/**
	 * @return the lstVoeuxActiv
	 */
	public NSArray<EOVoeux> lstVoeuxActiv() {
		if ((lstVoeuxActiv == null) && (selectedEns() != null))
			lstVoeuxActiv = FinderVoeux.getVoeuxActivitesForEnseignantAndYear(
					selectedEns().editingContext(), selectedEns(),
					getSelectedYear());
		return lstVoeuxActiv;
	}

	/**
	 * @param lstVoeuxActiv
	 *            the lstVoeuxActiv to set
	 */
	public void setLstVoeuxActiv(NSArray<EOVoeux> lstVoeuxActiv) {
		this.lstVoeuxActiv = lstVoeuxActiv;
	}

	public WOActionResults addVoeuxActiv() {
		EOEditingContext ecVoeux = new EOEditingContext(getEcEdit());
		setEditedVoeux(EOVoeux.createVoeux(ecVoeux, getSelectedYear()
				.localInstanceIn(ecVoeux)));

		editedVoeux
				.setToFwkpers_IndividuCreateurRelationship(((Session) session())
						.getGUser().individu());
		editedVoeux.setToFwkpers_IndividuEnseignantRelationship(selectedEns());
		editedVoeux.setDCreation(new NSTimestamp());
		// editedVoeux.setValide("O");
		lstVoeuxActiv = null;
		setApSelection(false);
		setActivSelection(true);
		setModeEditVoeux(EditVoeux.MODE_CREATE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un voeux");
		return null;
	}

	public WOActionResults addRealiseActiv() {
		EOEditingContext ecVoeux = new EOEditingContext(getEcEdit());
		setEditedVoeux(EOVoeux.createVoeux(ecVoeux, getSelectedYear()
				.localInstanceIn(ecVoeux)));

		editedVoeux
				.setToFwkpers_IndividuCreateurRelationship(((Session) session())
						.getGUser().individu());
		editedVoeux.setToFwkpers_IndividuEnseignantRelationship(selectedEns());
		editedVoeux.setDCreation(new NSTimestamp());
		editedVoeux.setValide("O");
		lstVoeuxActiv = null;
		setApSelection(false);
		setActivSelection(true);
		setModeEditVoeux(EditVoeux.MODE_CREAT_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(), "Ajout d'un réalisé");
		return null;
	}

	public WOActionResults selectEns() {
		AjaxUpdateContainer.updateContainerWithID(aucvoeuxzoneid(), context());
		refreshVoeux();
		refreshVoeuxActiv();
		return null;
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

	public WOActionResults editRealiseActiv(Object voeux) {
		setEditedVoeux((EOVoeux) voeux);
		dgVoeuxActiv().setSelectedObject(this.editedVoeux);
		setApSelection(false);
		setActivSelection(true);
		setModeEditVoeux(EditVoeux.MODE_REALISE);
		CktlAjaxWindow.open(context(), caweditvoeuxid(),
				"Modification d'un voeux sur activité");
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

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		// refresh des liste si changement d'année
		if (lastFetchedYear == null) {
			lastFetchedYear = getSelectedYear();
		}
		if (!lastFetchedYear.equals(getSelectedYear())) {
			refreshVoeux();
			refreshVoeuxActiv();
			lastFetchedYear = getSelectedYear();
		}
		super.appendToResponse(response, context);
	}

	public boolean canEditVoeux() {
		return ((selectedEns() != null) && (voeuxOccur() != null) && ((Session) session())
				.getGUser().canEditVoeux(voeuxOccur()));
	}

	/**
	 * @return the voeuxActivOccur
	 */
	public EOVoeux voeuxActivOccur() {
		return voeuxActivOccur;
	}

	/**
	 * @param voeuxActivOccur
	 *            the voeuxActivOccur to set
	 */
	public void setVoeuxActivOccur(EOVoeux voeuxActivOccur) {
		this.voeuxActivOccur = voeuxActivOccur;
	}

	public boolean canEditVoeuxActiv() {
		return ((selectedEns() != null) && (voeuxActivOccur() != null) && ((Session) session())
				.getGUser().canEditVoeux(voeuxActivOccur()));
	}

	public String aucbilanvoeuxid() {
		return getComponentId() + "_aucbilanvoeuxid";
	}

	public String totalVoeuxAp() {
		if (((Session) session()).getSelectedEns() != null) {
			return ((Session) session()).getSelectedEns().getTotalVoeuxAp()
					+ "h";
		}
		return null;
	}

	public String totalVoeuxActiv() {
		if (((Session) session()).getSelectedEns() != null) {
			return ((Session) session()).getSelectedEns()
					.getTotalVoeuxActivite() + "h";
		}
		return null;
	}

	public boolean hasVoeuxActiv() {
		return ((dgVoeuxActiv() != null)
				&& (dgVoeuxActiv().allObjects() != null) && (dgVoeuxActiv()
				.allObjects().size() > 0));
	}

	public boolean hasVoeuxAp() {
		return ((dgVoeux() != null) && (dgVoeux().allObjects() != null) && (dgVoeux()
				.allObjects().size() > 0));
	}

	public String auctotalvoeuxap() {
		return getComponentId() + "_auctotalvoeuxap";
	}

	public String auctotalvoeuxactiv() {
		return getComponentId() + "_auctotalvoeuxactiv";
	}

	public String aucvoeuxactiv() {
		return getComponentId() + "_aucvoeuxactiv";
	}

	public String aucvoeux() {
		return getComponentId() + "_aucvoeux";
	}

	public String styleVoeuxZone() {
		if (selectedEns() != null) {
			return "display:block;";
		} else {
			return "display:none;";
		}

	}

	public String aucvoeuxzoneid() {
		return getComponentId() + "_aucvoeuxzoneid";
	}

	private boolean isAllValidate(NSArray<EOVoeux> lst) {
		for (EOVoeux voeux : lst) {
			if (!"O".equals(voeux.valide())) {
				return false;
			}
		}
		return true;
	}

	public boolean canValidService() {
		return ((selectedEns() != null)
				&& ((hasVoeuxAp()) || (hasVoeuxActiv())) && ((Session) session())
				.getGUser().canValidServiceEns(
						((Session) session()).getSelectedEns(),((Session) session()).selectedYear()));
	}

	public boolean isAllVoeuxValides() {
		boolean retour = false;
		if ((selectedEns() != null)) {
			if (hasVoeuxAp()) {
				// verif si tous les vouex ap sont validés
				retour = isAllValidate(dgVoeux().allObjects());
			}
			if (retour) {
				if (hasVoeuxActiv()) {
					retour = isAllValidate(dgVoeuxActiv().allObjects());
				}
			}
		}
		return retour && canValidService();
	}

	public WOActionResults validerService() {
		EOEditingContext ec = new EOEditingContext(session()
				.defaultEditingContext());
		EOServiceEnseignant serv = serviceForEns();
		if (serv == null) {
			serv = EOServiceEnseignant
					.createServiceEnseignant(ec, getgUser().individu()
							.localInstanceIn(ec), selectedEns()
							.localInstanceIn(ec), getSelectedYear()
							.localInstanceIn(ec));
		}
		serv.setDValid(new NSTimestamp());
		showRealise = true;
		try {
			ec.saveChanges();
			if (ec.parentObjectStore()
					.equals(session().defaultEditingContext())) {
				session().defaultEditingContext().saveChanges();
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.creatMessageUtil(session(),
					UtilMessages.ERROR_MESSAGE, e.getMessage());

			return null;
		}
		((Session) session()).getSelectedEns()
				.setServiceEnseignantForYear(serv);

		// mail a l'enseignant
		if ("O".equals(mailToEns())) {
			try {
				MailMessageFactory.sendMailValidService(((Session) session())
						.getSelectedEns().getEmailPro(), ((Session) session())
						.getGUser());
			} catch (Exception e) {
				UtilMessages.creatMessageUtil(session(),
						UtilMessages.ERROR_MESSAGE, e.getMessage());
				e.printStackTrace();
			}
		}
		// AjaxUpdateContainer.updateContainerWithID(aucvoeuxzoneid(),
		// context());
		return null;
	}

	public EOScolFormationAnnee getSelectedYear() {
		return ((Session) session()).selectedYear();
	}

	public String erreurvalidservid() {
		return getComponentId() + "_erreurvalidservid";
	}

	public String messagevalidservid() {
		return getComponentId() + "_messagevalidservid";
	}

	public boolean serviceValided() {
		return Enseignant.isServiceValidForEnsAndYear(selectedEns()
				.editingContext(), selectedEns(), getSelectedYear());
	}

	public EOServiceEnseignant serviceForEns() {
		return ((Session) session()).getSelectedEns().getServiceEnseignant();
	}

	public WOActionResults devaliderService() {
		EOServiceEnseignant serv = serviceForEns();
		if (serv == null) {
			return null;
		}
		serv.setDValid(null);
		showRealise = false;
		try {
			serv.editingContext().saveChanges();
			if (serv.editingContext().parentObjectStore()
					.equals(session().defaultEditingContext())) {
				session().defaultEditingContext().saveChanges();
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.creatMessageUtil(session(),
					UtilMessages.ERROR_MESSAGE, e.getMessage());

			return null;
		}
		// mail a l'enseignant
		if ("O".equals(mailToEns())) {
			try {
				MailMessageFactory.sendMailInvalideService(
						((Session) session()).getSelectedEns().getEmailPro(),
						((Session) session()).getGUser());
			} catch (Exception e) {
				UtilMessages.creatMessageUtil(session(),
						UtilMessages.ERROR_MESSAGE, e.getMessage());
				e.printStackTrace();
			}
		}
		// AjaxUpdateContainer.updateContainerWithID(aucvoeuxzoneid(),
		// context());
		return null;
	}

	public String aucvalidservid() {
		return getComponentId() + "_aucvalidservid";
	}

	public WOActionResults printProposition() {
		return printService(true);
	}

	public WOActionResults printArrete() {
		return printService(false);
	}

	private WOActionResults printService(Boolean proposition) {
		Hashtable<String, Object> reportParams = new Hashtable<String, Object>();

		reportParams.put("PROPOSITION", proposition);
		reportParams.put("VOEUX_VALID", (proposition ? "T" : "O"));
		reportParams.put("NO_INDIVIDU", ((Session) session()).getSelectedEns()
				.getIndividu().noIndividu());
		reportParams.put("ANNEE", ((Session) session()).selectedYear()
				.fannDebut());

		Connection conn = Extraction.getOracleConnectionFromModel("Grille");

		CktlResourceManager resBundle = cktlApp.appResources();

		String subreportDir = resBundle.pathForResource("Reports");

		String fileName = "JASP_ServiceEns.jasper";

		if (subreportDir == null || fileName == null) {
			return null;
		}

		subreportDir = subreportDir.trim()
				+ System.getProperty("file.separator");
		fileName = fileName.trim();

		String reportFullName = subreportDir + fileName;

		reportParams.put("SUBREPORT_DIR", subreportDir);
		// reportParams.put("LOGO",
		// cktlApp.config().stringForKey("LOGO_REPORTS"));
		// URL urlFichierJasper =
		// application().resourceManager().pathURLForResourceNamed("Reports/JASP_SUB_VoeuxActiv.jasper",
		// application().name(),null);

		NSData pdfData = null;
		try {
			byte[] pdfBytes = Extraction.getPdfBytesForReport(reportFullName,
					reportParams, conn);
			if (pdfBytes != null)
				pdfData = new NSData(pdfBytes);
			// NSDictionary<String, Object> dico =
			// Extraction.getPDFByteArray(reportParams,
			// urlFichierJasper,conn,"pdf");
			// pdfData = (NSData) dico.valueForKey("streamOut");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (pdfData != null) {
			return Extraction.prepareFileAsStreamResponse(pdfData,
					"PropositionService.pdf", Extraction.MIME_PDF);
		}
		return null;
	}

	/**
	 * @return the canSendMail
	 */
	public boolean canSendMail() {
		return ((selectedEns() != null) && (((Session) session())
				.getSelectedEns().getEmailPro() != null));
	}

	/**
	 * @return the mailToEns
	 */
	public String mailToEns() {
		return mailToEns;
	}

	/**
	 * @param mailToEns
	 *            the mailToEns to set
	 */
	public void setMailToEns(String mailToEns) {
		this.mailToEns = mailToEns;
	}

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

	public String AucValidServiceZoneid() {
		return getComponentId() + "_AucValidServiceZoneid";
	}

	public Boolean canRealiseVoeux() {
		return ((selectedEns() != null) && (voeuxOccur() != null) && getgUser()
				.canRealiseVoeux(voeuxOccur()));
	}

	public Boolean canRealiseVoeuxActiv() {
		return ((selectedEns() != null) && (voeuxActivOccur() != null) && getgUser()
				.canRealiseVoeux(voeuxActivOccur()));
	}

	public boolean canAddRealise() {
		return ((selectedEns() != null) && (getgUser().canRealiseVoeux(
				selectedEns(), getSelectedYear())));
	}

	public String showColRealise() {
		if ((selectedEns() != null)
				&& (Enseignant.isServiceValidForEnsAndYear(session()
						.defaultEditingContext(), selectedEns(),
						((Session) session()).selectedYear()))) {
			return "true";
		}
		return "false";
	}

}