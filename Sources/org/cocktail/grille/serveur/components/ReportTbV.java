package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.grillefwk.serveur.metier.eof.EOPrestationEnseignant;
import org.cocktail.grillefwk.serveur.metier.eof.EOReport;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXQ;

@SuppressWarnings({ "serial", "unchecked" })
public class ReportTbV extends BaseTbView {
	
	
	public static final String COL_ANNEE = ERXQ.keyPath(EOReport.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY,EOScolFormationAnnee.FANN_DEBUT_KEY);
	public static final String COL_NB_HEURES = EOReport.NB_HEURES_KEY;
	public static final String COL_DATE_CREATION = EOReport.DATE_CREATION_KEY;
	public static final String COL_COMM_REPORT = EOReport.COMMENTAIRE_REPORT_KEY;
	
	
	static {
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Année");
		col1.setOrderKeyPath(COL_ANNEE);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_ANNEE, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		col1.setRowCssStyle("text-align:center;");
		col1.setHeaderCssStyle("text-align:center;width:40px;");
		_colonnesMap.takeValueForKey(col1, COL_ANNEE);
		
		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Nombre heures");
		col2.setOrderKeyPath(COL_NB_HEURES);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_NB_HEURES, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setHeaderCssStyle("text-align:center;width:95px;");
		col2.setRowCssStyle("text-align:right;padding-right:3px;");
		_colonnesMap.takeValueForKey(col2, COL_NB_HEURES);

		CktlAjaxTableViewColumn col3 = new CktlAjaxTableViewColumn();
		col3.setLibelle("Création");
		col3.setOrderKeyPath(COL_DATE_CREATION);
		CktlAjaxTableViewColumnAssociation ass3 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_DATE_CREATION, "emptyValue");
		ass3.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass3.setDateformat("%d/%m/%Y");
		col3.setAssociations(ass3);
		col3.setRowCssStyle("text-align:center;");
		col3.setHeaderCssStyle("text-align:center;width:70px;");
		_colonnesMap.takeValueForKey(col3, COL_DATE_CREATION);
		
		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Commentaire");
		col4.setOrderKeyPath(COL_COMM_REPORT);
		col4.setComponent(CommentZoneTbView.class.getName());
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_COMM_REPORT, "emptyValue");
		ass4.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass4.setObjectForKey("40", "cols");
		col4.setAssociations(ass4);
		col4.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col4, COL_COMM_REPORT);
		
		_colonnesMap.takeValueForKey(getColonneAction(), OBJ_KEY + ".action");
	}
	
	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_ANNEE, COL_NB_HEURES, COL_DATE_CREATION, COL_COMM_REPORT, OBJ_KEY + ".action" }
		);
	
	
	public ReportTbV(WOContext context) {
        super(context);
        
    }

	@Override
	public NSArray<String> DEFAULT_COLONNES_KEYS() {
		return DEFAULT_COLONNES_KEYS;
	}

	@Override
	public WOActionResults commitSave() {
		NSArray<NSKeyValueCoding> delObjects = getDeletedObjects();
		if (delObjects.size() > 0) {
			EOEditingContext ec = ((EOGenericRecord) delObjects.lastObject()).editingContext();
			for(NSKeyValueCoding object : delObjects) {
				EOReport delObject = (EOReport)object;
				delObject.removeObjectFromBothSidesOfRelationshipWithKey(	
							delObject.toFwkpers_Individu(), 
							EOReport.TO_FWKPERS__INDIVIDU_KEY
							);
				delObject.removeObjectFromBothSidesOfRelationshipWithKey(
							delObject.toFwkScolarite_ScolFormationAnnee(), 
							EOReport.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY
							);
				ec.deleteObject(delObject);
			}
			if (isCommitOnValid())
				ec.saveChanges();
		}
		return cancelSave();
	}
}