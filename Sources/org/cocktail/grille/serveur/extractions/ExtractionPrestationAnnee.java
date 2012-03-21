package org.cocktail.grille.serveur.extractions;

import java.io.ByteArrayOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.IPersonne;
import org.cocktail.grillefwk.serveur.Enseignant;
import org.cocktail.grillefwk.serveur.finder.FinderEnseignant;
import org.cocktail.grillefwk.serveur.finder.FinderPrestation;
import org.cocktail.grillefwk.serveur.metier.eof.EOPrestationEnseignant;
import org.cocktail.grillefwk.serveur.metier.eof.EOVQuotiteEnseignants;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;

public class ExtractionPrestationAnnee extends Extraction {
	
	
	public NSData getExtractionPrestationsAnnee(EOScolFormationAnnee annee) throws Exception {
		
		EOEditingContext ec = annee.editingContext();
		NSArray<EOPrestationEnseignant> presta = FinderPrestation.findPrestationForYear(ec, annee);
		
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		ByteArrayOutputStream excelOutputStream = null;
		excelOutputStream = new ByteArrayOutputStream();
		
				
		HSSFSheet sheet = workbook.createSheet("Prestations "+annee.fannDebut()+"-"+annee.fannFin());
		
		int rowNum = 0;
		HSSFRow row;
		row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue("Composante");
		row.createCell(1).setCellValue("Etab.");
		row.createCell(2).setCellValue("Nom Etab.");
		row.createCell(3).setCellValue("Code presta");
		row.createCell(4).setCellValue("Libellé presta");
		row.createCell(5).setCellValue("Nb H CM");
		row.createCell(6).setCellValue("Nb H TD");
		row.createCell(7).setCellValue("Nb H TP");
		
		for(int i=0;i<presta.count();i++) {
			rowNum++;
			row = sheet.createRow(rowNum);
			writeRecord(presta.objectAtIndex(i), row);
		}
		
		workbook.write(excelOutputStream);
		NSData excelData = null;
		if(excelOutputStream.size()>0) {
			System.out.println("Creation du NSData");
			excelData = new NSData(excelOutputStream.toByteArray());
		}
		excelOutputStream.close();
		return excelData;
	}
	
	
	/** Permet d'ecrire un enregistrement de type PrestationEnseigant dans le row */
	private void writeRecord(EOPrestationEnseignant record, HSSFRow row) {
		Cell cell = null;
		
		EOScolFormationAnnee annee = record.toFwkScolarite_ScolFormationAnnee();
		EOIndividu ens = record.toFwkpers_Individu();
		
		NSArray<EOVQuotiteEnseignants> vQuotites = FinderEnseignant.getQuotitesForEns(
													ens.editingContext(), 
													ens, 
													annee);
		
		StringBuffer st = new StringBuffer();
		for(int i=0;i<vQuotites.count();i++) {
			st.append( (vQuotites.objectAtIndex(i)).toFwkpers_Structure().llStructure() );
			if(i<vQuotites.count()-1) {
				st.append("\n");
			}
		}
		
		cell = row.createCell(0);
		cell.setCellValue(st.toString());
		cell = row.createCell(1);
		cell.setCellValue( safeToString(record.toFwkpers_Rne().cRne()) );
		cell = row.createCell(2);
		cell.setCellValue( safeToString(record.toFwkpers_Rne().llRne()) );
		cell = row.createCell(3);
		cell.setCellValue( safeToString(record.toActePrestation().libCourt()) );
		cell = row.createCell(4);
		cell.setCellValue( safeToString(record.toActePrestation().libLong()) );
		cell = row.createCell(5);
		cell.setCellValue( safeToString( record.nbHeuresCm() ) );
		cell = row.createCell(6);
		cell.setCellValue( safeToString( record.nbHeuresTd() ) );
		cell = row.createCell(7);
		cell.setCellValue( safeToString( record.nbHeuresTp() ) );
	}
	
	

}
