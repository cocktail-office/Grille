package org.cocktail.grille.serveur.components;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.extractions.ColonneExportXls;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOVDistinctCorpsAnnee;
import org.cocktail.grillefwk.serveur.metier.eof.EOVDistinctServiceAnnee;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;

import er.ajax.AjaxProgress;
import er.ajax.AjaxUpdateContainer;

public abstract class ListeXls extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected NSMutableArray<ColonneExportXls> lstColonnes;
	private ColonneExportXls colItem;
	private NSMutableArray<ColonneExportXls> selectedCols;
	private NSMutableArray<EOVDistinctServiceAnnee> lstSevices;
	private EOVDistinctServiceAnnee serviceOccur;
	private EOVDistinctServiceAnnee selectedService;
	protected EOFetchSpecification fetch;
	
	protected NSData excelData;

	public ListeXls(WOContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the lstColonnes
	 */
	public NSMutableArray<ColonneExportXls> lstColonnes() {
		return lstColonnes;
	}

	/**
	 * @param lstColonnes
	 *            the lstColonnes to set
	 */
	public void setLstColonnes(NSMutableArray<ColonneExportXls> lstColonnes) {
		this.lstColonnes = lstColonnes;
	}

	/**
	 * @return the colItem
	 */
	public ColonneExportXls colItem() {
		return colItem;
	}

	/**
	 * @param colItem
	 *            the colItem to set
	 */
	public void setColItem(ColonneExportXls colItem) {
		this.colItem = colItem;
	}

	public boolean disabledItem() {
		return (colItem != null) && colItem.isCellMandatory();
	}

	/**
	 * @return the selectedCols
	 */
	public NSMutableArray<ColonneExportXls> selectedCols() {
		if (selectedCols == null) {
			selectedCols = new NSMutableArray<ColonneExportXls>();
			for (ColonneExportXls col : lstColonnes()) {
				if ((col.isCellMandatory()) || (col.isCellChecked())) {
					selectedCols.add(col);
				}
			}
		}
		return selectedCols;
	}

	/**
	 * @param selectedCols
	 *            the selectedCols to set
	 */
	public void setSelectedCols(NSMutableArray<ColonneExportXls> selectedCols) {
		this.selectedCols = selectedCols;
	}

	public WOActionResults selAllCols() {
		setSelectedCols(lstColonnes());
		return null;
	}

	public WOActionResults selNoCols() {
		setSelectedCols(new NSMutableArray<ColonneExportXls>());
		checkCols();
		return null;
	}

	/**
	 * @return the lstSevices
	 */
	public NSMutableArray<EOVDistinctServiceAnnee> lstSevices() {
		if ((lstSevices != null)
				&& (lstSevices.lastObject() != null)
				&& (!lstSevices.lastObject().fannKey()
						.equals(((Session) session()).selectedYear().fannKey()))) {
			lstSevices = null;

		}
		if (lstSevices == null) {
			lstSevices = (NSMutableArray<EOVDistinctServiceAnnee>) EOVDistinctServiceAnnee
					.fetchVDistinctServiceAnnees(
							session().defaultEditingContext(),
							EOQualifier.qualifierWithQualifierFormat(
									EOVDistinctCorpsAnnee.FANN_KEY_KEY + "=%@",
									new NSArray<Integer>(((Session) session())
											.selectedYear().fannKey())),
							new NSArray<EOSortOrdering>(
									new EOSortOrdering(
											EOVDistinctServiceAnnee.LL_STRUCTURE_KEY,
											EOSortOrdering.CompareCaseInsensitiveAscending)));
		}
		return lstSevices;
	}

	/**
	 * @param lstSevices
	 *            the lstSevices to set
	 */
	public void setLstSevices(NSMutableArray<EOVDistinctServiceAnnee> lstSevices) {
		this.lstSevices = lstSevices;
	}

	/**
	 * @return the serviceOccur
	 */
	public EOVDistinctServiceAnnee serviceOccur() {
		return serviceOccur;
	}

	/**
	 * @param serviceOccur
	 *            the serviceOccur to set
	 */
	public void setServiceOccur(EOVDistinctServiceAnnee serviceOccur) {
		this.serviceOccur = serviceOccur;
	}

	/**
	 * @return the selectedService
	 */
	public EOVDistinctServiceAnnee selectedService() {
		return selectedService;
	}

	/**
	 * @param selectedService
	 *            the selectedService to set
	 */
	public void setSelectedService(EOVDistinctServiceAnnee selectedService) {
		this.selectedService = selectedService;
	}

	public NSMutableArray<ColonneExportXls> worksColonnes() {
		NSMutableArray<ColonneExportXls> colonnes = new NSMutableArray<ColonneExportXls>();
		NSArray<ColonneExportXls> sortedCols = EOSortOrdering
				.sortedArrayUsingKeyOrderArray(selectedCols(),
						new NSArray<EOSortOrdering>(new EOSortOrdering(
								"colOrder", EOSortOrdering.CompareAscending)));
		for (ColonneExportXls col : sortedCols) {
			if ((col.isCellMandatory())
					|| ((selectedCols() != null) && (selectedCols()
							.contains(col)))) {
				colonnes.add(col);
			}
		}
		return colonnes;
	}

	public NSMutableArray<EOSortOrdering> orderBy() {
		//orderby
		NSMutableArray<EOSortOrdering> orderBy = new NSMutableArray<EOSortOrdering>();
		NSArray<ColonneExportXls> sortedCols = EOSortOrdering
				.sortedArrayUsingKeyOrderArray(selectedCols(),
						new NSArray<EOSortOrdering>(new EOSortOrdering(
								"cellOrder", EOSortOrdering.CompareAscending)));
		for (ColonneExportXls col : selectedCols()) {
			if (col.getCellOrder() != null) {
				EOSortOrdering srt = new EOSortOrdering(col.getCellPath(),
						EOSortOrdering.CompareAscending);
				orderBy.addObject(srt);
			}
		}
		return orderBy;
	}

	public String auclstcolonnesid() {
		return getComponentId() + "_auclstcolonnesid";
	}

	public WOActionResults checkCols() {
		for (ColonneExportXls col : selectedCols()) {
			for (ColonneExportXls colParams : lstColonnes()) {
				//verif si toutes les colonne de la formule sont cochées
				if (HSSFCell.CELL_TYPE_FORMULA == col.getCellType()) {
					if ((colParams.getCellParamName() != null)
							&& (col.getCellValue().contains(colParams
									.getCellParamName()))
							&& (selectedCols().indexOf(colParams) < 0)) {
						selectedCols().add(colParams);
						UtilMessages
								.creatMessageUtil(
										session(),
										UtilMessages.INFO_MESSAGE,
										"La colonne "
												+ colParams.getCellName()
												+ " est obligatoire tant que la colonne "
												+ col.getCellName()
												+ " est cochée ");
					}
				}

			}

		}
		//check des colonnes obligatoires
		for (ColonneExportXls colParams : lstColonnes()) {
			if ((colParams.isCellMandatory())
					&& (selectedCols().indexOf(colParams) < 0)) {
				selectedCols().add(colParams);
				UtilMessages.creatMessageUtil(session(),
						UtilMessages.INFO_MESSAGE,
						"La colonne " + colParams.getCellName()
								+ " est obligatoire ");
			}
		}
		AjaxUpdateContainer.updateContainerWithID(messagecontainerid(),
				context());
		return null;
	}

	public String messagecontainerid() {
		return getComponentId() + "_messagecontainerid";
	}

	public String messageutilid() {
		return getComponentId() + "_messageutilid";
	}
	
	public String getStyleLibelleColonne(){
		if ((colItem()!=null)&&(colItem().isCellMandatory())){
			return "font-weight:bold;";
		}
		return "";
	}
	
	public String aucprintprogressid() {
		return getComponentId() + "_aucprintprogressid";
	}
	
	public NSData getExcelData() {
		return excelData;
	}

	public void setExcelData(NSData excelData) {
		this.excelData = excelData;
	}
	
	public String winprintprogressid() {
		return getComponentId()+"_winprintprogressid";
	}
		
	
	public String printProgressBarid() {
		return getComponentId()+"_printProgressBarid";
	}
	
	public String openProgressBar() {
		return "openAMD_"+winprintprogressid()+"();return true;";
	}
	
	private boolean isPrintInProgress = true;

	/**
	 * @return the isPrintInProgress
	 */
	public boolean isPrintInProgress() {
		
		return isPrintInProgress;
	}

	/**
	 * @param isPrintInProgress
	 *            the isPrintInProgress to set
	 */
	public void setPrintInProgress(boolean isPrintInProgress) {
		
		this.isPrintInProgress = isPrintInProgress;
	}

	public EOFetchSpecification getFetch() {
		return fetch;
	}

	public void setFetch(EOFetchSpecification fetch) {
		this.fetch = fetch;
	}
	
	private AjaxProgress printProgress;

	public AjaxProgress printProgress() {
		return printProgress;
	}

	public void setPrintProgress(AjaxProgress printProgress) {
		this.printProgress = printProgress;
	}
	
	
}
