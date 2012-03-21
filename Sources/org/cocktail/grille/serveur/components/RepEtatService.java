package org.cocktail.grille.serveur.components;

import java.sql.Connection;
import java.util.Hashtable;

import org.cocktail.fwkcktlwebapp.server.CktlResourceManager;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grillefwk.serveur.metier.eof.EOGrilleListe;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;

@SuppressWarnings("serial")
public class RepEtatService extends GrilleComponent {

	private String inputLibListe;

	private boolean isEcDeficit;
	private boolean isEcExcedent;
	private boolean isEcTous;

	public EOGrilleListe itemGrilleListe;
	private EOGrilleListe selectedListe;

	private NSArray<EOGrilleListe> grilleListes = null;

	public RepEtatService(WOContext context) {
		super(context);
	}

	public NSArray<EOGrilleListe> listOfGrilleListes() {
		if (grilleListes == null) {
			filtreListes();
		}
		return grilleListes;
	}

	/* methode de filtrage de la liste des listes d'EC en fonction des critères sélectionnés */
	public WOActionResults filtreListes() {
		System.out.println("filtrer");
		NSMutableArray<EOQualifier> array = new NSMutableArray<EOQualifier>();
		if (inputLibListe != null) {
			array.add(new EOKeyValueQualifier(EOGrilleListe.GL_LIBELLE_KEY,
					EOQualifier.QualifierOperatorCaseInsensitiveLike, "*"
							+ inputLibListe + "*"));
		}
		EOQualifier qual = null;
		if (array.count() > 0) {
			qual = new EOAndQualifier(array);
		}

		grilleListes = EOGrilleListe.fetchGrilleListes(edc(), qual,
				new NSArray<EOSortOrdering>(EOGrilleListe.SORT_LIBELLE_ASC));
		System.out.println("grilleListes.count=" + grilleListes.count());
		return null;
	}

	/* permet de télécharger la sortie au format pdf */
	public WOActionResults doDownloadPdf() {
		return null;
	}

	public EOGrilleListe selectedListe() {
		return selectedListe;
	}

	public void setSelectedListe(EOGrilleListe selectedListe) {
		this.selectedListe = selectedListe;
	}

	public String inputLibListe() {
		return inputLibListe;
	}

	public void setInputLibListe(String inputLibListe) {
		this.inputLibListe = inputLibListe;
	}

	public boolean isEcDeficit() {
		return isEcDeficit;
	}

	public void setEcDeficit(boolean isEcDeficit) {
		this.isEcDeficit = isEcDeficit;
	}

	public boolean isEcExcedent() {
		return isEcExcedent;
	}

	public void setEcExcedent(boolean isEcExcedent) {
		this.isEcExcedent = isEcExcedent;
	}

	public boolean isEcTous() {
		return isEcTous;
	}

	public void setEcTous(boolean isEcTous) {
		this.isEcTous = isEcTous;
	}

	public String radioetatecid() {
		return getComponentId() + "_radioetatec";
	}

	public String pubgrillelistesid() {
		return getComponentId() + "_pubgrillelistes";
	}

	public String formlisteid() {
		return getComponentId() + "_formliste";
	}

	public String aucgrillelistesid() {
		return getComponentId() + "_aucgrillelistes";
	}

	public WOActionResults reportEtatService() {
		if (selectedListe() == null) {
			return null;
		}
		Hashtable<String, Object> reportParams = new Hashtable<String, Object>();
		
		Integer glKey = (Integer) EOUtilities.primaryKeyForObject(selectedListe()
				.editingContext(), selectedListe()).valueForKey("glKey");

		reportParams.put("GL_KEY", glKey);
		reportParams.put("DEFICIT_EXEDENT", "T");

		Connection conn = Extraction.getOracleConnectionFromModel("Grille");

		CktlResourceManager resBundle = cktlApp.appResources();

		String subreportDir = resBundle.pathForResource("Reports");

		String fileName = "act_ens_liste.jasper";

		if (subreportDir == null || fileName == null) {
			return null;
		}

		subreportDir = subreportDir.trim()
				+ System.getProperty("file.separator");
		fileName = fileName.trim();

		String reportFullName = subreportDir + fileName;

		reportParams.put("SUBREPORT_DIR", subreportDir);
		//reportParams.put("LOGO", cktlApp.config().stringForKey("LOGO_REPORTS"));
		//URL urlFichierJasper = application().resourceManager().pathURLForResourceNamed("Reports/JASP_SUB_VoeuxActiv.jasper", application().name(),null);

		NSData pdfData = null;
		try {
			byte[] pdfBytes = Extraction.getPdfBytesForReport(reportFullName,
					reportParams, conn);
			if (pdfBytes != null)
				pdfData = new NSData(pdfBytes);
			//NSDictionary<String, Object> dico = Extraction.getPDFByteArray(reportParams, urlFichierJasper,conn,"pdf");
			//pdfData = (NSData) dico.valueForKey("streamOut");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (pdfData != null) {
			return Extraction.prepareFileAsStreamResponse(pdfData,
					selectedListe().glCode() + ".pdf", Extraction.MIME_PDF);
		}
		return null;

	}

}