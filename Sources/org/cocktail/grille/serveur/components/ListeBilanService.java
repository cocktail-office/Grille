package org.cocktail.grille.serveur.components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.EOStructure;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.extractions.ColonneExportXls;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.GrilleFwkException;
import org.cocktail.grillefwk.serveur.metier.eof.EOVQuotiteEnseignants;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.ajax.AjaxModalDialog;
import er.ajax.AjaxProgress;
import er.extensions.appserver.ERXRedirect;
import er.extensions.eof.ERXQ;

public class ListeBilanService extends ListeXls {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String PARAM_SERIVICE = "service";
	public final static String PARAM_TOTAL = "total";
	public final static String PARAM_TOTAL_FAIT = "totalfait";

	public ListeBilanService(WOContext context) {
		super(context);

		lstColonnes = new NSMutableArray<ColonneExportXls>();
		ColonneExportXls ens = new ColonneExportXls();
		ens.setCellName("Nom Enseignant");
		ens.setCellType(HSSFCell.CELL_TYPE_STRING);
		ens.setCellOrder(4);
		ens.setCellMandatory(true);
		ens.setCellChecked(true);
		ens.setCellPath(ERXQ.keyPath(
				EOVQuotiteEnseignants.TO_FWKPERS__INDIVIDU_KEY,
				EOIndividu.NOM_AFFICHAGE_KEY));
		ens.setColOrder(10);
		lstColonnes.add(ens);

		ColonneExportXls preEns = new ColonneExportXls();
		preEns.setCellName("Prénom Enseignant");
		preEns.setCellType(HSSFCell.CELL_TYPE_STRING);
		preEns.setCellPath(ERXQ.keyPath(
				EOVQuotiteEnseignants.TO_FWKPERS__INDIVIDU_KEY,
				EOIndividu.PRENOM_AFFICHAGE_KEY));
		preEns.setCellChecked(true);
		preEns.setColOrder(20);
		lstColonnes.add(preEns);

		ColonneExportXls serv = new ColonneExportXls();
		serv.setCellName("Service");
		serv.setCellType(HSSFCell.CELL_TYPE_STRING);
		serv.setCellPath(ERXQ.keyPath(
				EOVQuotiteEnseignants.TO_FWKPERS__STRUCTURE_KEY,
				EOStructure.LC_STRUCTURE_KEY));
		serv.setCellChecked(false);
		serv.setColOrder(1);
		lstColonnes.add(serv);

		ColonneExportXls potentiel = new ColonneExportXls();
		potentiel.setCellName("Service due");
		potentiel.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		potentiel.setCellChecked(true);
		potentiel.setCellPath(Enseignant.SERVICE_DUE);
		potentiel.setColOrder(30);
		potentiel.setCellParamName(PARAM_SERIVICE);
		lstColonnes.add(potentiel);

		ColonneExportXls voeux = new ColonneExportXls();
		voeux.setCellName("Total voeux ");
		voeux.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		voeux.setCellChecked(false);
		voeux.setCellPath(Enseignant.TOTAL_VOEUX);
		voeux.setColOrder(35);
		lstColonnes.add(voeux);	
		
		ColonneExportXls service = new ColonneExportXls();
		service.setCellName("Total voeux validés");
		service.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		service.setCellChecked(true);
		service.setCellPath(Enseignant.TOTAL_VOEUX_VALID);
		service.setColOrder(40);
		service.setCellParamName(PARAM_TOTAL);
		lstColonnes.add(service);	

		ColonneExportXls bilan = new ColonneExportXls();
		bilan.setCellName("Bilan voeux");
		bilan.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		bilan.setCellChecked(true);
		bilan.setCellValue(PARAM_TOTAL + "-" + PARAM_SERIVICE);
		bilan.setColOrder(50);
		lstColonnes.add(bilan);
		
		ColonneExportXls realise = new ColonneExportXls();
		realise.setCellName("Total réalisé");
		realise.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		realise.setCellChecked(true);
		realise.setCellPath(Enseignant.TOTAL_FAIT);
		realise.setColOrder(51);
		realise.setCellParamName(PARAM_TOTAL_FAIT);
		lstColonnes.add(realise);
		
		ColonneExportXls bilanFait = new ColonneExportXls();
		bilanFait.setCellName("Bilan réalisé");
		bilanFait.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		bilanFait.setCellChecked(true);
		bilanFait.setCellValue(PARAM_TOTAL_FAIT + "-" + PARAM_SERIVICE);
		bilanFait.setColOrder(52);
		lstColonnes.add(bilanFait);

		ColonneExportXls quotite = new ColonneExportXls();
		quotite.setCellName("Quotite");
		quotite.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		quotite.setCellChecked(false);
		quotite.setCellPath(Enseignant.QUOTITE);
		quotite.setColOrder(60);
		lstColonnes.add(quotite);

		ColonneExportXls decharge = new ColonneExportXls();
		decharge.setCellName("Décharge");
		decharge.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		decharge.setCellChecked(false);
		decharge.setCellPath(Enseignant.DECHARGE);
		decharge.setColOrder(70);
		lstColonnes.add(decharge);

		ColonneExportXls report = new ColonneExportXls();
		report.setCellName("Report");
		report.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		report.setCellChecked(false);
		report.setCellPath(Enseignant.REPORT);
		report.setColOrder(80);
		lstColonnes.add(report);

		ColonneExportXls vAp = new ColonneExportXls();
		vAp.setCellName("Voeux AP");
		vAp.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		vAp.setCellChecked(false);
		vAp.setCellPath(Enseignant.TT_VOEUX_AP);
		vAp.setColOrder(90);
		lstColonnes.add(vAp);

		ColonneExportXls vActiv = new ColonneExportXls();
		vActiv.setCellName("Activité");
		vActiv.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		vActiv.setCellChecked(false);
		vActiv.setCellPath(Enseignant.TT_VOEUX_ACTIV);
		vActiv.setColOrder(100);
		lstColonnes.add(vActiv);

		ColonneExportXls prest = new ColonneExportXls();
		prest.setCellName("Prestation");
		prest.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		prest.setCellChecked(false);
		prest.setCellPath(Enseignant.PRESTATION);
		prest.setColOrder(60);
		lstColonnes.add(prest);

	}

	public WOActionResults print() throws IOException {
		setSelectedCols(worksColonnes());

		setPrintInProgress(true);

		NSMutableArray<Object> args = new NSMutableArray<Object>();
		String strQual = EOVQuotiteEnseignants.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY
				+ "=%@";
		args.add(((Session) session()).selectedYear());
		if (selectedService() != null) {
			strQual += " AND "
					+ ERXQ.keyPath(EOVQuotiteEnseignants.TO_FWKPERS__STRUCTURE_KEY)
					+ " =%@ ";
			args.addObject(selectedService());
		}

		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(strQual,
				args);

		EOFetchSpecification fetch = new EOFetchSpecification(
				EOVQuotiteEnseignants.ENTITY_NAME, qual,
				new NSArray<EOSortOrdering>(new EOSortOrdering(
						EOVQuotiteEnseignants.NOM_AFFICHAGE_KEY,
						EOSortOrdering.CompareAscending)));
		fetch.setIsDeep(true);
		fetch.setUsesDistinct(true);

		// execution de la requete
		final NSMutableArray<EOVQuotiteEnseignants> lst = session()
				.defaultEditingContext().objectsWithFetchSpecification(fetch)
				.mutableClone();

		if (printProgress() == null)
			setPrintProgress(new AjaxProgress(lst.count()));
		else {
			printProgress().setDone(false);
			printProgress().setMaximum(lst.count());
			printProgress().setValue(0);
			printProgress().setCompletionEventsFired(false); //super important sinon la fin n'est pas déclanchée			
		}

		Thread progressThread = new Thread(new Runnable() {
			short ligne = 2; // variable d'incrementation des lignes de la feuille
			HSSFWorkbook workbook = new HSSFWorkbook();
			String title = "Bilan services enseignants";
			HSSFCellStyle Total = workbook.createCellStyle();
			HSSFCellStyle bdrLR = workbook.createCellStyle();
			HSSFCellStyle bdrLRnum = workbook.createCellStyle();
			HSSFSheet sheet = workbook.createSheet(title);

			public void run() {
				//Contruction entete
				Extraction.buildEnteteXls(sheet, workbook, title,
						worksColonnes(), Total, bdrLR, bdrLRnum);
				NSMutableDictionary<String, String> colEntetes = Extraction
						.getParamsForCols(worksColonnes());
				ByteArrayOutputStream excelOutputStream = null;
				excelOutputStream = new ByteArrayOutputStream();
				// lecture du curseur				
				for (EOVQuotiteEnseignants obj : lst) {
					ligne += 1;
					// Create a row and put some cells in it. Rows are 0 based.
					HSSFRow row = sheet.createRow(ligne);
					int indCol = 0;

					Enseignant ens = new Enseignant(((Session) session())
							.selectedYear(), obj.toFwkpers_Individu());

					for (ColonneExportXls colOccur : worksColonnes()) {
						HSSFCell cell = row.createCell(indCol);
						indCol++;
						if (HSSFCell.CELL_TYPE_FORMULA == colOccur
								.getCellType()) {
							Extraction.traitFormulaCell(
									obj.valueForKeyPath(colOccur.getCellPath()),
									cell, colOccur, colEntetes, ligne, Total);
						}
						if (HSSFCell.CELL_TYPE_STRING == colOccur.getCellType()) {
							Extraction.traitStringCell(
									obj.valueForKeyPath(colOccur.getCellPath()),
									cell, bdrLR);
						}
						if (HSSFCell.CELL_TYPE_NUMERIC == colOccur
								.getCellType()) {
							//C'est une methode de la class Enseignant
							Object value = null;
							try {
								value = ens.valueForKey(colOccur.getCellPath());
							} catch (Exception e) {
								e.printStackTrace();

							}
							if (value != null) {
								Extraction.traitNumericCell(value, cell,
										bdrLRnum);
							}

							cell.setCellStyle(bdrLRnum);
						}
					}

					printProgress().incrementValue(1);
				}

				//set autoSize
				Extraction.setAutosizeForSheet(sheet, worksColonnes());

				try {
					workbook.write(excelOutputStream);
					excelData = null;

					if (excelOutputStream.size() > 0) {
						excelData = new NSData(excelOutputStream.toByteArray());
					}

					excelOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					printProgress().setFailure(new Throwable(e));
				}
				printProgress().setDone(true);

				setPrintInProgress(false);
			}
		});
		
		if (lst.count() > 50) {
			//CktlAjaxWindow.open(context(), winprintprogressid());
			AjaxModalDialog.open(context(), winprintprogressid());
			progressThread.start();
		} else {
			progressThread.start();
			try {
				progressThread.join();
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			
			if (excelData != null) {
				
				ERXRedirect redirect = new ERXRedirect(context());		
				ThrowPdf cmp = (ThrowPdf) pageWithName(ThrowPdf.class.getName());
				cmp.setResponse(Extraction.prepareFileAsStreamResponse(excelData,"ListeBilanService.xls", Extraction.MIME_XLS));
				
				redirect.setComponent(cmp);						
				return redirect;
			}
		}
		return null;
	}

	public WOActionResults exportFile() {
		
		
		if ((excelData != null) && (excelData.toString() != "")) {
			
			WOResponse resp = new WOResponse();
			resp.setContent(excelData);
			resp.setHeader("application/xls", "Content-Type");
			resp.setHeader(String.valueOf(excelData.length()), "Content-Length");
			resp.setHeader("attachment;filename=\"ListeBilanService.xls\"",
					"Content-Disposition");
			resp.disableClientCaching();
			resp.removeHeadersForKey("Cache-Control");
			resp.removeHeadersForKey("pragma");
			return resp.generateResponse();
			
			//return Extraction.prepareFileAsStreamResponse(excelData,"ListeBilanService.xls", Extraction.MIME_XLS);
		}
		return null;
	}

	

}