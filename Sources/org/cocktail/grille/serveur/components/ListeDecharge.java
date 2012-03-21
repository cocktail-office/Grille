package org.cocktail.grille.serveur.components;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.EOStructure;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.extractions.ColonneExportXls;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grillefwk.serveur.metier.eof.EODecharge;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeDechargeService;
import org.cocktail.grillefwk.serveur.metier.eof.EOVQuotiteEnseignants;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.eof.ERXQ;

public class ListeDecharge extends ListeXls {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EOTypeDechargeService typeItem;
	private NSMutableArray<EOTypeDechargeService> lstTypes;
	private NSMutableArray<EOTypeDechargeService> selectedTypes;

	public ListeDecharge(WOContext context) {
        super(context);
        lstColonnes = new NSMutableArray<ColonneExportXls>();
		ColonneExportXls type = new ColonneExportXls();
		type.setCellName("Type court décharge");
		type.setCellType(HSSFCell.CELL_TYPE_STRING);
		type.setCellOrder(1);
		type.setCellChecked(true);
		type.setCellPath(ERXQ.keyPath(EODecharge.TO_TYPE_DECHARGE_SERVICE_KEY,
				EOTypeDechargeService.LC_TYPE_DECHARGE_KEY));
		type.setColOrder(1);
		lstColonnes.add(type);

		ColonneExportXls libType = new ColonneExportXls();
		libType.setCellName("Type long décharge");
		libType.setCellType(HSSFCell.CELL_TYPE_STRING);
		libType.setCellOrder(2);
		libType.setCellChecked(false);
		libType.setCellPath(ERXQ.keyPath(EODecharge.TO_TYPE_DECHARGE_SERVICE_KEY,
				EOTypeDechargeService.LL_TYPE_DECHARGE_KEY));
		libType.setColOrder(2);
		lstColonnes.add(libType);		
		
		ColonneExportXls ens = new ColonneExportXls();
		ens.setCellName("Nom Enseignant");
		ens.setCellType(HSSFCell.CELL_TYPE_STRING);
		ens.setCellOrder(4);
		ens.setCellMandatory(true);
		ens.setCellChecked(true);
		ens.setCellPath(ERXQ.keyPath(
				EODecharge.TO_FWKPERS__INDIVIDU_KEY,
				EOIndividu.NOM_AFFICHAGE_KEY));
		ens.setColOrder(7);
		lstColonnes.add(ens);

		ColonneExportXls preEns = new ColonneExportXls();
		preEns.setCellName("Prénom Enseignant");
		preEns.setCellType(HSSFCell.CELL_TYPE_STRING);
		preEns.setCellPath(ERXQ.keyPath(
				EODecharge.TO_FWKPERS__INDIVIDU_KEY,
				EOIndividu.PRENOM_AFFICHAGE_KEY));
		preEns.setCellChecked(true);
		preEns.setColOrder(6);
		lstColonnes.add(preEns);
		
		ColonneExportXls serv = new ColonneExportXls();
		serv.setCellName("Service");
		serv.setCellType(HSSFCell.CELL_TYPE_STRING);
		serv.setCellPath(ERXQ.keyPath(
				EODecharge.TO_VQUOTITE_ENSEIGNANTSES_KEY,
				EOVQuotiteEnseignants.TO_FWKPERS__STRUCTURE_KEY,EOStructure.LC_STRUCTURE_KEY));
		serv.setCellChecked(false);
		serv.setColOrder(8);
		lstColonnes.add(serv);

		ColonneExportXls hrVoeux = new ColonneExportXls();
		hrVoeux.setCellName("Hr décharge");
		hrVoeux.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		hrVoeux.setCellChecked(true);
		hrVoeux.setCellPath(ERXQ.keyPath(EODecharge.NB_H_DECHARGE_KEY));
		hrVoeux.setColOrder(9);
		lstColonnes.add(hrVoeux);
		
		selectedTypes = lstTypes();
    }
	
	public String auctypesid() {
		return getComponentId() + "_auctypesid";
	}

	/**
	 * @return the typeItem
	 */
	public EOTypeDechargeService typeItem() {
		return typeItem;
	}

	/**
	 * @param typeItem the typeItem to set
	 */
	public void setTypeItem(EOTypeDechargeService typeItem) {
		this.typeItem = typeItem;
	}

	/**
	 * @return the lstTypes
	 */
	public NSMutableArray<EOTypeDechargeService> lstTypes() {
		if (lstTypes == null) {
			lstTypes = (NSMutableArray<EOTypeDechargeService>) EOTypeDechargeService
					.fetchAllTypeDechargeServices(
							session().defaultEditingContext(),
							new NSArray<EOSortOrdering>(
									new EOSortOrdering(
											EOTypeDechargeService.LC_TYPE_DECHARGE_KEY,
											EOSortOrdering.CompareCaseInsensitiveAscending)));

		}
		return lstTypes;
	}

	/**
	 * @param lstTypes the lstTypes to set
	 */
	public void setLstTypes(NSMutableArray<EOTypeDechargeService> lstTypes) {
		this.lstTypes = lstTypes;
	}

	/**
	 * @return the selectedTypes
	 */
	public NSMutableArray<EOTypeDechargeService> selectedTypes() {
		return selectedTypes;
	}

	/**
	 * @param selectedTypes the selectedTypes to set
	 */
	public void setSelectedTypes(NSMutableArray<EOTypeDechargeService> selectedTypes) {
		this.selectedTypes = selectedTypes;
	}
	
	public WOActionResults selAllTypes() {
		setSelectedTypes(lstTypes());
		return null;
	}

	public WOActionResults selNoTypes() {
		setSelectedTypes(null);
		return null;
	}

	public WOActionResults print() {
		NSMutableArray<Object> args = new NSMutableArray<Object>();
		String strQual = EODecharge.PERIODE_DECHARGE_KEY +" caseInsensitiveLike %@";
		args.add(((Session) session()).selectedYear().fannDebut()+"*");
		if (selectedService() != null) {
			strQual += " AND "
					+ ERXQ.keyPath(EODecharge.TO_VQUOTITE_ENSEIGNANTSES_KEY,EOVQuotiteEnseignants.TO_FWKPERS__STRUCTURE_KEY)
					+ " =%@ ";
			args.addObject(selectedService());
		}		
		
		String strQualTypes="";
		for (EOTypeDechargeService type : selectedTypes()) {
			if (!strQualTypes.equals("")){
				strQualTypes+=" OR ";
			}
			strQualTypes+= "("+ ERXQ.keyPath(EODecharge.TO_TYPE_DECHARGE_SERVICE_KEY)+"=%@ )";
			args.addObject(type);
		}
		
		if (!strQualTypes.equals("")){
			strQual += " AND ("+strQualTypes+") ";
		}
		
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(strQual,args);
				
		setSelectedCols(worksColonnes());		

		EOFetchSpecification fetch = new EOFetchSpecification(
				EODecharge.ENTITY_NAME, qual, null);
		fetch.setIsDeep(true);
		fetch.setUsesDistinct(true);

		NSData pdfData = null;
		boolean showProgress=false;
		try {
			pdfData = Extraction.getXlsForColonnes(selectedCols(), fetch,
					session().defaultEditingContext(),
					"Liste des décharges",orderBy(),null);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (pdfData != null) {
			return Extraction.prepareFileAsStreamResponse(pdfData,
					"ListeDecharges.xls", Extraction.MIME_XLS);
		}
		
		return null;
	}
}