package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.IPersonne;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.finder.FinderEnseignant;
import org.cocktail.grillefwk.serveur.metier.eof.EOPrestationEnseignant;
import org.cocktail.grillefwk.serveur.metier.eof.EOReport;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxUpdateContainer;
import er.extensions.appserver.ERXDisplayGroup;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;

@SuppressWarnings("serial")
public class PageGestionEnseignants extends BaseGestionRef {

	private static final String COL_KEYS_VOEUX_AP = VoeuxTbV.COL_AP_KEY + ","
			+ VoeuxTbV.COL_NB_HEURES_VOEUX_KEY + ","
			+ VoeuxTbV.COL_NB_HEURES_REALISE_KEY;

	private static final String COL_KEYS_VOEUX_ACTIVITE = VoeuxTbV.COL_ACTIVITE_KEY
			+ ","
			+ VoeuxTbV.COL_NB_HEURES_VOEUX_KEY
			+ ","
			+ VoeuxTbV.COL_NB_HEURES_REALISE_KEY;

	private WODisplayGroup dgSearch;
	private boolean searchMode = false;

	public PageGestionEnseignants(WOContext context) {
		super(context);
		dgSearch = new ERXDisplayGroup();
	}

	public WODisplayGroup dgSearch() {
		return dgSearch;
	}

	public void setDgSearch(WODisplayGroup dgSearch) {
		this.dgSearch = dgSearch;
	}

	

	public EOQualifier enseignantsQualifier() {
		return FinderEnseignant.qualEnseignant(((Session) session())
				.selectedYear());
	}

	public WOActionResults setSearchOn() {
		setSearchMode(true);
		dgSearch().clearSelection();
		return null;
	}

	public WOActionResults setSearchOff() {
		setSearchMode(false);
		CktlAjaxWindow.close(context(), "searchZoneId");
		return null;
	}

	public WOActionResults cancelSearch() {
		return setSearchOff();
	}

	public boolean searchMode() {
		return searchMode;
	}

	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}

	public String getColKeysVoeuxAP() {
		return COL_KEYS_VOEUX_AP;
	}

	public String getColKeysVoeuxActivite() {
		return COL_KEYS_VOEUX_ACTIVITE;
	}

	public String formsearchid() {
		return getComponentId() + "_formsearch";
	}

	public String aucenseignantid() {
		return getComponentId() + "_aucenseignant";
	}

	public String aucsearchensId() {
		return getComponentId() + "_aucsearchens";
	}

	public String textButtonChoisirPersonne() {
		EOIndividu ens = session().getIndividuForEnseigant();
		if (ens != null) {
			return "Choisir " + ens.getNomPrenomAffichage();
		}
		return null;
	}

	@Override
	public NSArray<EOGenericRecord> getLstObj() {
		return null;
	}

	@Override
	public WOActionResults editObj(Object disc) {
		return null;
	}

	public String aucreportsid() {
		return getComponentId() + "_aucreportsid";
	}

	public String aulrefreshreportsid() {
		return getComponentId() + "_aulrefreshreportsid";
	}

	private WODisplayGroup dgReports;

	public WODisplayGroup dgReports() {
		if (dgReports == null) {
			dgReports = new WODisplayGroup();
			// displayGroup.setDelegate(new DgDelegate());
		}
		if (dgReports != null)

			dgReports.setObjectArray(lstReports());
		dgReports.clearSelection();
		return dgReports;
	}

	public void setDgReports(WODisplayGroup dgReports) {
		this.dgReports = dgReports;
	}

	public NSArray<EOReport> lstReports() {
		EOIndividu ens = session().getIndividuForEnseigant();
		if (ens != null) {
			NSMutableArray<EOQualifier> array = new NSMutableArray<EOQualifier>();
			array.add(ERXQ.equals(EOReport.TO_FWKPERS__INDIVIDU_KEY, ens));
			array.add(ERXQ.equals(
					EOReport.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY,
					session().selectedYear()));
			return EOReport
					.fetchReports(edc(), new EOAndQualifier(array), null);
		}
		return null;
	}

	public WOActionResults editReport(Object report) {
		if (report != null) {
			editedReport = (EOReport) report;
			CktlAjaxWindow.open(context(), cawgestionreportid(),
					"Modifier le report");
		}
		return null;
	}

	private EOReport editedReport;

	public EOReport editedReport() {
		return editedReport;
	}

	public void setEditedReport(EOReport object) {
		this.editedReport = object;
	}

	public String cawgestionreportid() {
		return getComponentId() + "_cawgestionreport";
	}

	public WOActionResults refreshReports() {

		if (dgReports != null) {
			dgReports.setObjectArray(lstReports());
			dgReports.clearSelection();
		}
		AjaxUpdateContainer.updateContainerWithID(aucreportsid(), context());
		//pour relancer le calcul du total des reports
		((Session) session()).getSelectedEns().setReportsForYear(null);
		AjaxUpdateContainer.updateContainerWithID(aucBilanEnsid(), context());
		
		return null;
	}

	public String aucBilanEnsid() {
		return getComponentId() + "_aucBilanEnsid";
	}

	public String aucgestionreportid() {
		return getComponentId() + "_aucgestionreport";
	}

	public boolean canAddReport() {
		return ((((Session) session()).getSelectedEns() != null) && (((Session) session())
				.getGUser().canEditEnseignant(((Session) session())
				.selectedYear())));
	}

	public WOActionResults AddReport() {
		ERXEC ec = new ERXEC(session().defaultEditingContext());
		editedReport = (EOReport) EOUtilities.createAndInsertInstance(ec,
				EOReport.ENTITY_NAME);
		editedReport.setDateCreation(new NSTimestamp());
		EOIndividu ens = session().getIndividuForEnseigant();
		editedReport.setToFwkpers_IndividuRelationship(ens.localInstanceIn(ec));
		editedReport.setToFwkScolarite_ScolFormationAnneeRelationship(session()
				.selectedYear().localInstanceIn(ec));
		CktlAjaxWindow.open(context(), cawgestionreportid(),
				"Ajouter un report");
		return null;
	}

	public String aucprestationsid() {
		return getComponentId() + "_aucprestationsid";
	}

	private WODisplayGroup dgPrestations;

	public WODisplayGroup dgPrestations() {
		if (dgPrestations == null) {
			dgPrestations = new WODisplayGroup();
		}
		if (dgPrestations != null)

			dgPrestations.setObjectArray(lstPrestations());
		dgPrestations.clearSelection();
		return dgPrestations;
	}

	public void setDgPrestations(WODisplayGroup dgPrestations) {
		this.dgPrestations = dgPrestations;
	}

	public NSArray<EOPrestationEnseignant> lstPrestations() {
		Enseignant ens = session().getSelectedEns();
		if (ens != null) {
			return ens.getPrestationsForYear();
		}
		return null;
	}

	public WOActionResults editPrestation(Object presta) {
		if (presta != null) {
			editedPrestation = (EOPrestationEnseignant) presta;
			CktlAjaxWindow.open(context(), cawgestionprestationsid(),
					"Modifier la prestation");
		}
		return null;
	}

	private EOPrestationEnseignant editedPrestation;	

	public EOPrestationEnseignant editedPrestation() {
		return editedPrestation;
	}

	public void setEditedPrestation(EOPrestationEnseignant object) {
		this.editedPrestation = object;
	}

	public String cawgestionprestationsid() {
		return getComponentId() + "_cawgestionprestationsid";
	}

	public WOActionResults refreshPrestations() {

		session().getSelectedEns().setPrestationsForYear(null);
		if (dgPrestations != null) {
			dgPrestations.setObjectArray(lstPrestations());
			dgPrestations.clearSelection();
		}
		AjaxUpdateContainer.updateContainerWithID(aucprestationsid(), context());
		//pour relancer le calcul du total des prestations
		((Session) session()).getSelectedEns().setPrestationsForYear(null);
		AjaxUpdateContainer.updateContainerWithID(aucBilanEnsid(), context());

		return null;
	}

	public String aulrefreshprestationsid() {
		return getComponentId() + "_aulrefreshprestationsid";
	}

	public String aucgestionprestationid() {
		return getComponentId() + "_aucgestionprestationid";
	}

	public WOActionResults AddPrestation() {
		EOIndividu ens = session().getIndividuForEnseigant();
		if(ens!=null) {
			ERXEC ec = new ERXEC( session().defaultEditingContext() );
			editedPrestation = (EOPrestationEnseignant)EOUtilities.createAndInsertInstance(ec,EOPrestationEnseignant.ENTITY_NAME);
			editedPrestation.setToFwkpers_IndividuRelationship(ens.localInstanceIn(ec));
			editedPrestation.setToFwkScolarite_ScolFormationAnneeRelationship(session().selectedYear().localInstanceIn(ec));
			CktlAjaxWindow.open(context(), cawgestionprestationsid(), "Ajouter une prestation");
		}
		return null;
	}

	public WOActionResults selectEns(){
		AjaxUpdateContainer.updateContainerWithID(aucinfosensid(), context());
		return null;
	}

	public String aucinfosensid() {
		return getComponentId() + "_aucinfosensid";
	}
	

	
}