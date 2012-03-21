package org.cocktail.grille.serveur.components;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.extractions.ColonneExportXls;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grillefwk.serveur.metier.eof.EOVCorpsEnseignantsService;
import org.cocktail.grillefwk.serveur.metier.eof.EOVDistinctCorpsAnnee;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.ajax.AjaxProgress;
import er.extensions.appserver.ERXRedirect;

public class ListeEnseignants extends ListeXls {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EOVDistinctCorpsAnnee corpsItem;
	private NSMutableArray<EOVDistinctCorpsAnnee> lstCorps;
	private NSMutableArray<EOVDistinctCorpsAnnee> selectedCorps;

	public ListeEnseignants(WOContext context) {
		super(context);

		lstColonnes = new NSMutableArray<ColonneExportXls>();
		ColonneExportXls service = new ColonneExportXls();
		service.setCellName("Service");
		service.setCellType(HSSFCell.CELL_TYPE_STRING);
		service.setCellOrder(1);
		service.setCellChecked(true);
		service.setCellPath(EOVCorpsEnseignantsService.LL_STRUCTURE_KEY);
		lstColonnes.add(service);

		ColonneExportXls ens = new ColonneExportXls();
		ens.setCellName("Nom Enseignant");
		ens.setCellType(HSSFCell.CELL_TYPE_STRING);
		ens.setCellOrder(3);
		ens.setCellMandatory(true);
		ens.setCellChecked(true);
		ens.setCellPath(EOVCorpsEnseignantsService.NOM_AFFICHAGE_KEY);
		lstColonnes.add(ens);

		ColonneExportXls preEns = new ColonneExportXls();
		preEns.setCellName("Prénom Enseignant");
		preEns.setCellType(HSSFCell.CELL_TYPE_STRING);
		preEns.setCellPath(EOVCorpsEnseignantsService.PRENOM_AFFICHAGE_KEY);
		preEns.setCellChecked(true);
		lstColonnes.add(preEns);

		ColonneExportXls corps = new ColonneExportXls();
		corps.setCellName("Corps");
		corps.setCellType(HSSFCell.CELL_TYPE_STRING);
		corps.setCellOrder(2);
		corps.setCellChecked(true);
		corps.setCellPath(EOVCorpsEnseignantsService.LL_CORPS_KEY);
		lstColonnes.add(corps);

		selectedCorps = lstCorps();
	}

	/**
	 * @return the corpsItem
	 */
	public EOVDistinctCorpsAnnee corpsItem() {
		return corpsItem;
	}

	/**
	 * @param corpsItem
	 *            the corpsItem to set
	 */
	public void setCorpsItem(EOVDistinctCorpsAnnee corpsItem) {
		this.corpsItem = corpsItem;
	}

	/**
	 * @return the lstCorps
	 */
	public NSMutableArray<EOVDistinctCorpsAnnee> lstCorps() {
		if ((lstCorps != null)
				&& (lstCorps.lastObject() != null)
				&& (!lstCorps.lastObject().fannKey()
						.equals(((Session) session()).selectedYear().fannKey()))) {
			lstCorps = null;

		}
		if (lstCorps == null) {
			lstCorps = (NSMutableArray<EOVDistinctCorpsAnnee>) EOVDistinctCorpsAnnee
					.fetchVDistinctCorpsAnnees(
							session().defaultEditingContext(),
							EOQualifier.qualifierWithQualifierFormat(
									EOVDistinctCorpsAnnee.FANN_KEY_KEY + "=%@",
									new NSArray<Integer>(((Session) session())
											.selectedYear().fannKey())),
							new NSArray<EOSortOrdering>(
									new EOSortOrdering(
											EOVDistinctCorpsAnnee.LC_CORPS_KEY,
											EOSortOrdering.CompareCaseInsensitiveAscending)));
		}
		return lstCorps;
	}

	/**
	 * @param lstCorps
	 *            the lstCorps to set
	 */
	public void setLstCorps(NSMutableArray<EOVDistinctCorpsAnnee> lstCorps) {
		this.lstCorps = lstCorps;
	}

	/**
	 * @return the selectedCorps
	 */
	public NSMutableArray<EOVDistinctCorpsAnnee> selectedCorps() {
		return selectedCorps;
	}

	/**
	 * @param selectedCorps
	 *            the selectedCorps to set
	 */
	public void setSelectedCorps(
			NSMutableArray<EOVDistinctCorpsAnnee> selectedCorps) {
		this.selectedCorps = selectedCorps;
	}

	public String auccorpsid() {
		return getComponentId() + "_auccorpsid";
	}

	public WOActionResults selAllCorps() {
		setSelectedCorps(lstCorps());
		return null;
	}

	public WOActionResults selNoCorps() {
		setSelectedCorps(null);
		return null;
	}

	public WOActionResults print() {
		NSMutableArray<Object> args = new NSMutableArray<Object>();
		String strQual = EOVCorpsEnseignantsService.FANN_KEY_KEY + "=%@";
		args.add(((Session) session()).selectedYear().fannKey());
		if (selectedService() != null) {
			strQual += " AND "
					+ EOVCorpsEnseignantsService.TO_FWKPERS__STRUCTURE_KEY
					+ " =%@ ";
			args.addObject(selectedService());
		}

		String strQualCorps = "";
		for (EOVDistinctCorpsAnnee crp : selectedCorps()) {
			if (!strQualCorps.equals("")) {
				strQualCorps += " OR ";
			}
			strQualCorps += "("
					+ EOVCorpsEnseignantsService.TO_FWKPERS__CORPS_KEY
					+ "=%@ )";
			args.addObject(crp.toFwkpers_Corps());
		}

		if (!strQualCorps.equals("")) {
			strQual += " AND (" + strQualCorps + ") ";
		}

		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(strQual,
				args);

		setSelectedCols(worksColonnes());

		fetch = new EOFetchSpecification(
				EOVCorpsEnseignantsService.ENTITY_NAME, qual, orderBy());
		fetch.setIsDeep(true);
		fetch.setUsesDistinct(true);

		NSMutableArray<EOGenericRecord> lst = session().defaultEditingContext().objectsWithFetchSpecification(fetch).mutableClone();
		excelData = null;
		
		//tentative d'utilisation d'une progress barre mais pour le moment ca marche pas.
		boolean showProgress = false;//((lst!=null)&&(lst.count()>50));

		//progress bar

		if (printProgress() == null)
			setPrintProgress(new AjaxProgress(lst.count()));
		else {
			printProgress().setDone(false);
			printProgress().setMaximum(lst.count());
			printProgress().setValue(0);
			printProgress().setCompletionEventsFired(false); //super important sinon la fin n'est pas déclanchée			
		}

		Thread progressThread = new Thread(new Runnable() {

			public void run() {
				for (int i = 0; i <723; i++) {
					printProgress().incrementValue(1);
				}
				try {
					excelData = Extraction.getXlsForColonnes(selectedCols(),
							fetch, session().defaultEditingContext(),
							"Liste des enseignants par corps", null,
							printProgress());

					printProgress().setDone(true);

				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		
		if (showProgress) {
			CktlAjaxWindow.open(context(), winprintprogressid());
			
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
				cmp.setResponse(Extraction.prepareFileAsStreamResponse(
						excelData, "ListeEnseignants.xls", Extraction.MIME_XLS));

				redirect.setComponent(cmp);
				return redirect;
			}
		}
		
		return null;
	}

	public WOActionResults exportFile() {
		
		if ((excelData != null) && (excelData.toString() != "")) {
			return Extraction.prepareFileAsStreamResponse(excelData,
					"ListeEnseignants.xls", Extraction.MIME_XLS);
		}
		return null;
	}

}