package org.cocktail.grille.serveur.components;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.EOStructure;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.extractions.ColonneExportXls;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOVCorpsEnseignantsService;
import org.cocktail.grillefwk.serveur.metier.eof.EOVDistinctCorpsAnnee;
import org.cocktail.grillefwk.serveur.metier.eof.EOVDistinctServiceAnnee;
import org.cocktail.grillefwk.serveur.metier.eof.EOVQuotiteEnseignants;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.eof.ERXQ;

public class ListeVoeuxActivites extends ListeXls {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EOTypeActivite typeItem;
	private NSMutableArray<EOTypeActivite> lstTypes;
	private NSMutableArray<EOTypeActivite> selectedTypes;
	

	public ListeVoeuxActivites(WOContext context) {
		super(context);
		lstColonnes = new NSMutableArray<ColonneExportXls>();
		ColonneExportXls type = new ColonneExportXls();
		type.setCellName("Type court activité");
		type.setCellType(HSSFCell.CELL_TYPE_STRING);
		type.setCellOrder(1);
		type.setCellChecked(true);
		type.setCellPath(ERXQ.keyPath(EOVoeux.TO_ACTIVITE_KEY,
				EOActivite.TO_TYPE_ACTIVITE_KEY, EOTypeActivite.LIB_COURT_KEY));
		type.setColOrder(1);
		lstColonnes.add(type);

		ColonneExportXls libType = new ColonneExportXls();
		libType.setCellName("Type long activité");
		libType.setCellType(HSSFCell.CELL_TYPE_STRING);
		libType.setCellOrder(2);
		libType.setCellChecked(false);
		libType.setCellPath(ERXQ.keyPath(EOVoeux.TO_ACTIVITE_KEY,
				EOActivite.TO_TYPE_ACTIVITE_KEY, EOTypeActivite.LIB_LONG_KEY));
		libType.setColOrder(2);
		lstColonnes.add(libType);

		ColonneExportXls libAct = new ColonneExportXls();
		libAct.setCellName("Libelle activité");
		libAct.setCellType(HSSFCell.CELL_TYPE_STRING);
		libAct.setCellChecked(true);
		libAct.setCellPath(ERXQ.keyPath(EOVoeux.TO_ACTIVITE_KEY,
				EOActivite.COMMENTAIRE_KEY));
		libAct.setColOrder(3);
		lstColonnes.add(libAct);

		ColonneExportXls lien = new ColonneExportXls();
		lien.setCellName("Lié à");
		lien.setCellType(HSSFCell.CELL_TYPE_STRING);
		lien.setCellOrder(3);
		lien.setCellChecked(true);
		lien.setCellPath(ERXQ.keyPath(EOVoeux.TO_ACTIVITE_KEY,
				EOActivite.LIB_LIEN_KEY));
		lien.setColOrder(4);
		lstColonnes.add(lien);

		ColonneExportXls hrActiv = new ColonneExportXls();
		hrActiv.setCellName("Hr Activité");
		hrActiv.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		hrActiv.setCellChecked(false);
		hrActiv.setCellPath(ERXQ.keyPath(EOVoeux.TO_ACTIVITE_KEY,
				EOActivite.NB_HEURES_ACTIVITE_KEY));
		hrActiv.setColOrder(5);
		lstColonnes.add(hrActiv);

		ColonneExportXls ens = new ColonneExportXls();
		ens.setCellName("Nom Enseignant");
		ens.setCellType(HSSFCell.CELL_TYPE_STRING);
		ens.setCellOrder(4);
		ens.setCellMandatory(true);
		ens.setCellChecked(true);
		ens.setCellPath(ERXQ.keyPath(
				EOVoeux.TO_FWKPERS__INDIVIDU_ENSEIGNANT_KEY,
				EOIndividu.NOM_AFFICHAGE_KEY));
		ens.setColOrder(7);
		lstColonnes.add(ens);

		ColonneExportXls preEns = new ColonneExportXls();
		preEns.setCellName("Prénom Enseignant");
		preEns.setCellType(HSSFCell.CELL_TYPE_STRING);
		preEns.setCellPath(ERXQ.keyPath(
				EOVoeux.TO_FWKPERS__INDIVIDU_ENSEIGNANT_KEY,
				EOIndividu.PRENOM_AFFICHAGE_KEY));
		preEns.setCellChecked(true);
		preEns.setColOrder(6);
		lstColonnes.add(preEns);
		
		ColonneExportXls serv = new ColonneExportXls();
		serv.setCellName("Service");
		serv.setCellType(HSSFCell.CELL_TYPE_STRING);
		serv.setCellPath(ERXQ.keyPath(
				EOVoeux.TO_VQUOTITE_ENSEIGNANTS_KEY,
				EOVQuotiteEnseignants.TO_FWKPERS__STRUCTURE_KEY,EOStructure.LC_STRUCTURE_KEY));
		serv.setCellChecked(false);
		serv.setColOrder(8);
		lstColonnes.add(serv);

		ColonneExportXls hrVoeux = new ColonneExportXls();
		hrVoeux.setCellName("Hr Voeux");
		hrVoeux.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		hrVoeux.setCellChecked(true);
		hrVoeux.setCellPath(ERXQ.keyPath(EOVoeux.NB_HEURES_VOEUX_KEY));
		hrVoeux.setColOrder(9);
		lstColonnes.add(hrVoeux);
		
		ColonneExportXls hrFait = new ColonneExportXls();
		hrFait.setCellName("Hr réalisées");
		hrFait.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		hrFait.setCellChecked(true);
		hrFait.setCellPath(ERXQ.keyPath(EOVoeux.NB_HEURE_REALISE_KEY));
		hrFait.setColOrder(10);
		lstColonnes.add(hrFait);
		
		selectedTypes = lstTypes();
	}

	

	public EOTypeActivite getTypeItem() {
		return typeItem;
	}

	public void setTypeItem(EOTypeActivite typeItem) {
		this.typeItem = typeItem;
	}

	public NSMutableArray<EOTypeActivite> lstTypes() {

		if (lstTypes == null) {
			lstTypes = (NSMutableArray<EOTypeActivite>) EOTypeActivite
					.fetchAllTypeActivites(
							session().defaultEditingContext(),
							new NSArray<EOSortOrdering>(
									new EOSortOrdering(
											EOTypeActivite.LIB_COURT_KEY,
											EOSortOrdering.CompareCaseInsensitiveAscending)));

		}
		return lstTypes;
	}

	public void setLstTypes(NSMutableArray<EOTypeActivite> lstTypes) {
		this.lstTypes = lstTypes;
	}

	public NSMutableArray<EOTypeActivite> selectedTypes() {
		return selectedTypes;
	}

	public void setSelectedTypes(NSMutableArray<EOTypeActivite> selectedTypes) {
		this.selectedTypes = selectedTypes;
	}		

	public String auctypesid() {
		return getComponentId() + "_auctypesid";
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
		String strQual = EOVoeux.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY + "=%@";
		args.add(((Session) session()).selectedYear());
		if (selectedService() != null) {
			strQual += " AND "
					+ ERXQ.keyPath(EOVoeux.TO_VQUOTITE_ENSEIGNANTS_KEY,EOVQuotiteEnseignants.TO_FWKPERS__STRUCTURE_KEY)
					+ " =%@ ";
			args.addObject(selectedService());
		}		
		
		String strQualTypes="";
		for (EOTypeActivite type : selectedTypes()) {
			if (!strQualTypes.equals("")){
				strQualTypes+=" OR ";
			}
			strQualTypes+= "("+ ERXQ.keyPath(EOVoeux.TO_ACTIVITE_KEY,EOActivite.TO_TYPE_ACTIVITE_KEY)+"=%@ )";
			args.addObject(type);
		}
		
		if (!strQualTypes.equals("")){
			strQual += " AND ("+strQualTypes+") ";
		}
		
		EOQualifier qual = EOQualifier.qualifierWithQualifierFormat(strQual,args);
				
		setSelectedCols(worksColonnes());		
		
		EOFetchSpecification fetch = new EOFetchSpecification(
				EOVoeux.ENTITY_NAME, qual, null);
		fetch.setIsDeep(true);
		fetch.setUsesDistinct(true);

		NSData pdfData = null;
		boolean showProgress=false;
		try {
			pdfData = Extraction.getXlsForColonnes(selectedCols(), fetch,
					session().defaultEditingContext(),
					"Liste des voeux sur activité",orderBy(),null);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (pdfData != null) {
			return Extraction.prepareFileAsStreamResponse(pdfData,
					"ListeVoeuxActivite.xls", Extraction.MIME_XLS);
		}
		return null;		
	}

}