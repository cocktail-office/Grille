package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.fwkcktlpersonne.common.metier.EORne;
import org.cocktail.grillefwk.serveur.metier.eof.EOActePrestation;
import org.cocktail.grillefwk.serveur.metier.eof.EOPrestationEnseignant;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;


import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;

public class PrestationEnseignantTbV extends BaseTbView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6325353463511017189L;

	public EOPrestationEnseignant unRow;
	
	public static final String BINDING_colonnesKeys = "colonnesKeys";

	public static final String ACTE_PREST_KEY 	= EOPrestationEnseignant.TO_ACTE_PRESTATION_KEY+"."+EOActePrestation.LIB_COURT_KEY;
	public static final String ETABLISSEMENT_KEY = EOPrestationEnseignant.TO_FWKPERS__RNE_KEY+"."+EORne.LL_RNE_KEY;
	public static final String NB_HEURES_CM_KEY = EOPrestationEnseignant.NB_HEURES_CM_KEY;
	public static final String NB_HEURES_TD_KEY = EOPrestationEnseignant.NB_HEURES_TD_KEY;
	public static final String NB_HEURES_TP_KEY = EOPrestationEnseignant.NB_HEURES_TP_KEY;
	
	static {
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Acte");
		col1.setOrderKeyPath(ACTE_PREST_KEY);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + ACTE_PREST_KEY, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, ACTE_PREST_KEY);
	
		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Etablissement");
		col2.setOrderKeyPath(ETABLISSEMENT_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + ETABLISSEMENT_KEY, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, ETABLISSEMENT_KEY);
	
		CktlAjaxTableViewColumn col3 = new CktlAjaxTableViewColumn();
		col3.setLibelle("Hr CM");
		col3.setOrderKeyPath(NB_HEURES_CM_KEY);
		CktlAjaxTableViewColumnAssociation ass3 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + NB_HEURES_CM_KEY, "emptyValue");
		ass3.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col3.setAssociations(ass3);
		col3.setHeaderCssStyle("text-align:center;width:40px;");
		col3.setRowCssStyle("text-align:right;padding-right:3px;");
		_colonnesMap.takeValueForKey(col3, NB_HEURES_CM_KEY);

		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Hr TD");
		col4.setOrderKeyPath(NB_HEURES_TD_KEY);
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + NB_HEURES_TD_KEY, "emptyValue");
		ass4.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col4.setAssociations(ass4);
		col4.setHeaderCssStyle("text-align:center;width:40px;");
		col4.setRowCssStyle("text-align:right;padding-right:3px;");
		_colonnesMap.takeValueForKey(col4, NB_HEURES_TD_KEY);

		CktlAjaxTableViewColumn col5 = new CktlAjaxTableViewColumn();
		col5.setLibelle("Hr TP");
		col5.setOrderKeyPath(NB_HEURES_TP_KEY);
		CktlAjaxTableViewColumnAssociation ass5 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + NB_HEURES_TP_KEY, "emptyValue");
		ass5.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col5.setAssociations(ass5);
		col5.setHeaderCssStyle("text-align:center;width:40px;");
		col5.setRowCssStyle("text-align:right;padding-right:3px;");
		_colonnesMap.takeValueForKey(col5, NB_HEURES_TP_KEY);
		
		_colonnesMap.takeValueForKey(getColonneAction(), OBJ_KEY + ".action");
	}
	
	
	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { 
					ACTE_PREST_KEY, ETABLISSEMENT_KEY, NB_HEURES_CM_KEY, NB_HEURES_TD_KEY, NB_HEURES_TP_KEY, OBJ_KEY + ".action" } //
	);
	
	public PrestationEnseignantTbV(WOContext context) {
		super(context);
	}
	
	/*
	@Override
	public NSArray<String> getColonnesKeys() {
		NSArray<String> keys = DEFAULT_COLONNES_KEYS;
		if (hasBinding(BINDING_colonnesKeys)) {
			String keysStr = (String) valueForBinding(BINDING_colonnesKeys);
			keys = NSArray.componentsSeparatedByString(keysStr, ",");
		}
		return keys;
	}

	public String getEmptyValue() {
		return " ";
	}

	@Override
	public NSArray<String> DEFAULT_COLONNES_KEYS() {
		return getColonnesKeys();
	}
	*/
	

	@Override
	public WOActionResults commitSave() {
		
		if (getDeletedObjects().size() > 0) {
			EOEditingContext ec = ((EOGenericRecord)getDeletedObjects().lastObject()).editingContext();
			for(NSKeyValueCoding delPrestEns : getDeletedObjects()) {
				EOPrestationEnseignant delObject = ((EOPrestationEnseignant) delPrestEns);
				delObject.setDateAnnulation( new NSTimestamp() );
			}
			if (isCommitOnValid())
				ec.saveChanges();
		}

		return cancelSave();
	}

	
	@Override
	public NSArray<String> DEFAULT_COLONNES_KEYS() {
		return DEFAULT_COLONNES_KEYS;
	}
	
}