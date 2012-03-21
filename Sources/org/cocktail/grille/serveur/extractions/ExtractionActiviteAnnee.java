package org.cocktail.grille.serveur.extractions;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.cocktail.grillefwk.serveur.finder.FinderActivite;
import org.cocktail.grillefwk.serveur.finder.FinderVoeux;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;

public class ExtractionActiviteAnnee extends Extraction {

	
	
	public NSData getExtractionActiviteAnnee(EOScolFormationAnnee annee) throws Exception {
		EOEditingContext ec = annee.editingContext();
		NSArray<EOActivite> activites = FinderActivite.getAllActiviteForYear(ec, annee);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		ByteArrayOutputStream excelOutputStream = null;
		excelOutputStream = new ByteArrayOutputStream();
		
				
		HSSFSheet sheet = workbook.createSheet("Activités "+annee.fannDebut()+"-"+annee.fannFin());
		
		int rowNum = 0;
		HSSFRow row;
		row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue("Code Activité");
		row.createCell(1).setCellValue("Libellé Activité");
		row.createCell(2).setCellValue("Heures");
		row.createCell(3).setCellValue("Heures enseignant");
		row.createCell(4).setCellValue("Heures solde");

		for(int i=0;i<activites.count();i++) {
			rowNum++;
			row = sheet.createRow(rowNum);
			writeRecord(activites.objectAtIndex(i), row);
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
	
	/** Permet d'ecrire un enregistrement de type EOActivite dans le hssfrow */
	private void writeRecord(EOActivite record, HSSFRow row) {
		
		EOEditingContext ec = record.editingContext();
		Cell cell = null;
		
		cell = row.createCell(0);
		cell.setCellValue( safeToString(record.toTypeActivite().libCourt()) );
		
		cell = row.createCell(1);
		cell.setCellValue( safeToString(record.toTypeActivite().libLong()) );
		
		cell = row.createCell(2);
		cell.setCellValue( safeToString(record.nbHeuresActivite()) );
		
		cell = row.createCell(3);
		BigDecimal totalAct = FinderVoeux.getTotalVoeuxForActivite(ec,record);
		cell.setCellValue( safeToString(totalAct) );
		
		cell = row.createCell(4);
		BigDecimal solde = record.nbHeuresActivite().subtract(totalAct);
		cell.setCellValue( safeToString( solde ) );
	}
	
	
}
