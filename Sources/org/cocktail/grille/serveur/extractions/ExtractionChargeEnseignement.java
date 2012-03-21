package org.cocktail.grille.serveur.extractions;

import java.io.ByteArrayOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDomaine;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteParcours;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteRepartitionAp;

import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;

public class ExtractionChargeEnseignement extends Extraction {

	NSMutableArray<EOScolMaquetteEc> listeEc = new NSMutableArray<EOScolMaquetteEc>();
	
	
	
	public NSData getExtractionChargeEnseignement(EOScolFormationDomaine dom, 
													EOScolFormationDiplome dipl, 
													EOScolFormationSpecialisation specialisation, 
													EOScolFormationAnnee annee) throws Exception {
		
		
		EOEditingContext ec = annee.editingContext();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		ByteArrayOutputStream excelOutputStream = null;
		excelOutputStream = new ByteArrayOutputStream();
		
				
		HSSFSheet sheet = workbook.createSheet("Charges enseignement "+annee.fannDebut()+"-"+annee.fannFin());
		
		int rowNum = 0;
		HSSFRow row;
		row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue("Code Dipl");
		row.createCell(1).setCellValue("Lib Dipl");
		row.createCell(2).setCellValue("Specialisation");
		row.createCell(3).setCellValue("Code EC");
		row.createCell(4).setCellValue("Lib EC");
		//row.createCell(5).setCellValue("Heures Hab.");
		row.createCell(5).setCellValue("Type AP");
		row.createCell(6).setCellValue("Nb Groupes");
		row.createCell(7).setCellValue("Nb Heures");
		row.createCell(8).setCellValue("Hr Voeux");
		row.createCell(9).setCellValue("Bilan Voeux");
		row.createCell(10).setCellValue("Hr réalisées");		
		row.createCell(11).setCellValue("Bilan réalisé");
		/*
		row.createCell(12).setCellValue("Heures activité");
		row.createCell(13).setCellValue("Heures voeux activité");
		*/
		
		
		
		NSArray<EOScolFormationDiplome> diplomes = null;
		
		if(dipl!=null) {
			diplomes = new NSArray<EOScolFormationDiplome>(dipl);
		}
		else {
			diplomes = FinderScolFormationDiplome.getDiplomesForDomaineAndYear(ec, dom, annee);
		}
		
		
		// Boucle diplomes
		for(EOScolFormationDiplome aDipl : diplomes) {
			NSArray<EOScolFormationSpecialisation> specs = null;
			
			if(specialisation==null) {
				specs = aDipl.toFwkScolarite_ScolFormationSpecialisations();
			}
			else {
				specs = new NSArray<EOScolFormationSpecialisation>(specialisation);
			}
			
			NSArray<EOScolMaquetteRepartitionAp> listMrap;
			
			// Boucle specialisations
			for(EOScolFormationSpecialisation spec : specs) {
				NSArray<EOScolMaquetteParcours> parcours = spec.toFwkScolarite_ScolMaquetteParcourss();
				NSArray<EOScolMaquetteEc> listeEc = new NSArray<EOScolMaquetteEc>();
				
				// Boucle parcours
				for(EOScolMaquetteParcours par : parcours) {
					listeEc = FinderScolMaquetteEc.getEcForParcoursAndLibelle(ec, par, null,annee);
					
					EOScolMaquetteAp ap;
					// Boucle EC
					for(EOScolMaquetteEc mec : listeEc) {
						
						listMrap = mec.toFwkScolarite_ScolMaquetteRepartitionAps();
						// Boucle RepartitionAp
						for(EOScolMaquetteRepartitionAp mrap : listMrap) {
						
							ap = mrap.toFwkScolarite_ScolMaquetteAp();
							
							rowNum++;
							row = sheet.createRow(rowNum);
							
							Cell cell = null;
							
							cell = row.createCell(0);
							cell.setCellValue( aDipl.fdipCode() );
							
							cell = row.createCell(1);
							cell.setCellValue( aDipl.fdipAbreviation() );
							
							cell = row.createCell(2);
							cell.setCellValue( spec.fspnLibelle() );
							
							cell = row.createCell(3);
							cell.setCellValue( mec.mecCode() );
							
							cell = row.createCell(4);
							cell.setCellValue( mec.mecLibelle() );
							
							cell = row.createCell(5);
							cell.setCellValue( ap.toFwkScolarite_ScolMaquetteHoraireCode().mhcoAbreviation() );
							
							cell = row.createCell(6);
							cell.setCellValue( ap.mapGroupeReel() );
							
							Integer seuil = ap.mapSeuil();
							cell = row.createCell(7);
							cell.setCellValue( seuil );
							
							
							Double heuresEns = nbHeuresVoeuxForAp(ap, annee);
							
							cell = row.createCell(8);
							cell.setCellValue( heuresEns.doubleValue() );
							
							/*double bilan = 0d;
							if(seuil!=null) {
								if(heuresEns!=null) {
									bilan = heuresEns.doubleValue() - seuil.doubleValue();
								}
								else {
									bilan = -seuil.doubleValue();
								}
							}							
							*/
							cell = row.createCell(9);
							//cell.setCellValue( bilan );
							cell.setCellFormula("I"+(rowNum+1)+"-H"+(rowNum+1));
							
							Double heuresFait = nbHeuresFaitForAp(ap, annee);
							cell = row.createCell(10);
							cell.setCellValue( heuresFait.doubleValue() );
														
							cell = row.createCell(11);
							cell.setCellFormula( "K"+(rowNum+1)+"-H"+(rowNum+1) );
							
						}
					}
				}
			}			
			
		}
		
		
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		

		workbook.write(excelOutputStream);
		NSData excelData = null;
		if(excelOutputStream.size()>0) {
			System.out.println("Creation du NSData");
			excelData = new NSData(excelOutputStream.toByteArray());
		}
		excelOutputStream.close();
		return excelData;
	}
	
	
	
	public NSData getExtractionChargeEnseignementDiplomeOld(EOScolFormationDiplome dipl, EOScolFormationAnnee annee) throws Exception {
		
		EOEditingContext ec = annee.editingContext();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		ByteArrayOutputStream excelOutputStream = null;
		excelOutputStream = new ByteArrayOutputStream();
		
				
		HSSFSheet sheet = workbook.createSheet("Charges enseignement "+annee.fannDebut()+"-"+annee.fannFin());
		
		int rowNum = 0;
		HSSFRow row;
		row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue("Code Dipl");
		row.createCell(1).setCellValue("Lib Dipl");
		row.createCell(2).setCellValue("Specialisation");
		row.createCell(3).setCellValue("Code EC");
		row.createCell(4).setCellValue("Lib EC");
		//row.createCell(5).setCellValue("Heures Hab.");
		row.createCell(5).setCellValue("Type AP");
		row.createCell(6).setCellValue("Nb Groupes");
		row.createCell(7).setCellValue("Nb Heures");
		row.createCell(8).setCellValue("Hr voeux");		
		row.createCell(9).setCellValue("Bilan voeux");
		row.createCell(10).setCellValue("Hr réalisées");		
		row.createCell(11).setCellValue("Bilan réalisé");
		
		/*
		row.createCell(12).setCellValue("Heures activité");
		row.createCell(13).setCellValue("Heures voeux activité");
		*/
		
		NSArray<EOScolFormationSpecialisation> specs = dipl.toFwkScolarite_ScolFormationSpecialisations();
		NSArray<EOScolMaquetteEc> listEc = null;
		NSArray<EOScolMaquetteRepartitionAp> listMrap;
		for(EOScolFormationSpecialisation spec : specs) {
			
			NSArray<EOScolMaquetteParcours> parcours = spec.toFwkScolarite_ScolMaquetteParcourss();
			NSArray<EOScolMaquetteEc> listeEc = new NSArray<EOScolMaquetteEc>();
			
			for(EOScolMaquetteParcours par : parcours) {
				
				listeEc = FinderScolMaquetteEc.getEcForParcoursAndLibelle(ec, par, null,annee);
				System.out.println("listeEc:"+listeEc);
				EOScolMaquetteAp ap;
				for(EOScolMaquetteEc mec : listeEc) {
					
					listMrap = mec.toFwkScolarite_ScolMaquetteRepartitionAps();
					for(EOScolMaquetteRepartitionAp mrap : listMrap) {
					
						ap = mrap.toFwkScolarite_ScolMaquetteAp();
						
						rowNum++;
						row = sheet.createRow(rowNum);
						
						Cell cell = null;
						
						cell = row.createCell(0);
						cell.setCellValue( dipl.fdipCode() );
						
						cell = row.createCell(1);
						cell.setCellValue( dipl.fdipAbreviation() );
						
						cell = row.createCell(2);
						cell.setCellValue( spec.fspnLibelle() );
						
						cell = row.createCell(3);
						cell.setCellValue( mec.mecCode() );
						
						cell = row.createCell(4);
						cell.setCellValue( mec.mecLibelle() );
						
						cell = row.createCell(5);
						cell.setCellValue( ap.toFwkScolarite_ScolMaquetteHoraireCode().mhcoAbreviation() );
						
						cell = row.createCell(6);
						cell.setCellValue( ap.mapGroupeReel() );
												
						Integer seuil = ap.mapSeuil();
						cell = row.createCell(7);
						cell.setCellValue( seuil );
						
						Double heuresEns = nbHeuresVoeuxForAp(ap, annee);
						
						cell = row.createCell(8);
						cell.setCellValue( heuresEns.doubleValue() );
																		
						double bilan = 0d;
						if(seuil!=null) {
							if(heuresEns!=null) {
								bilan = heuresEns.doubleValue() - seuil.doubleValue();
							}
							else {
								bilan = -seuil.doubleValue();
							}
						}
						cell = row.createCell(9);
						cell.setCellValue( bilan );
						
						Double heuresFait = nbHeuresFaitForAp(ap, annee);
						cell = row.createCell(10);
						cell.setCellValue( heuresEns.doubleValue() );
						
						double bilanFait = 0d;
						if(seuil!=null) {
							if(heuresFait!=null) {
								bilanFait = heuresFait.doubleValue() - seuil.doubleValue();
							}
							else {
								bilanFait = -seuil.doubleValue();
							}
						}
						cell = row.createCell(11);
						cell.setCellValue( bilanFait );
						
					}
				}
			}
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
	
	
	
	
	

	Double nbHeuresVoeuxForAp(EOScolMaquetteAp ap, EOScolFormationAnnee annee) {
		
		NSMutableArray<EOQualifier> array = new NSMutableArray<EOQualifier>();
		array.add(
				new EOKeyValueQualifier(EOVoeux.TO_FWK_SCOLARITE__SCOLARITE_FWK_SCOL_MAQUETTE_AP_KEY,EOQualifier.QualifierOperatorEqual,ap)
		);
		
		array.add(
				new EOKeyValueQualifier(EOVoeux.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY,EOQualifier.QualifierOperatorEqual,ap)
		);
		
		NSArray<EOVoeux> data = EOVoeux.fetchVoeuxes(ap.editingContext(), new EOAndQualifier(array), null);
		double totalVoeux = 0d;
		for(EOVoeux v : data) {
			if ("O".equals(v.valide())){
				totalVoeux += (v.nbHeuresVoeux()!=null?v.nbHeuresVoeux():0);
			}
		}
		
		return new Double(totalVoeux);
		
	}
	
	Double nbHeuresFaitForAp(EOScolMaquetteAp ap, EOScolFormationAnnee annee) {
		
		NSMutableArray<EOQualifier> array = new NSMutableArray<EOQualifier>();
		array.add(
				new EOKeyValueQualifier(EOVoeux.TO_FWK_SCOLARITE__SCOLARITE_FWK_SCOL_MAQUETTE_AP_KEY,EOQualifier.QualifierOperatorEqual,ap)
		);
		
		array.add(
				new EOKeyValueQualifier(EOVoeux.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY,EOQualifier.QualifierOperatorEqual,ap)
		);
		
		NSArray<EOVoeux> data = EOVoeux.fetchVoeuxes(ap.editingContext(), new EOAndQualifier(array), null);
		double totalVoeux = 0d;
		for(EOVoeux v : data) {
			if ("O".equals(v.valide())){
				totalVoeux += (v.nbHeureRealise()!=null?v.nbHeureRealise():0);
			}
		}
		
		return new Double(totalVoeux);
		
	}
	

}







/*
Integer nbGrTd;
Integer nbGrTp;
Integer nbHeuresCm;
Integer nbHeuresTd;
Integer nbHeuresTp;
*/
