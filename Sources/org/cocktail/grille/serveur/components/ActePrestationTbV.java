package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.grillefwk.serveur.metier.eof.EOActePrestation;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;

public class ActePrestationTbV extends BaseTbView {
	
	
	public static final String COL_LIB_COURT = EOActePrestation.LIB_COURT_KEY;
	public static final String COL_LIB_LONG = EOActePrestation.LIB_LONG_KEY;
	
	static {
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Code");
		col1.setOrderKeyPath(COL_LIB_COURT);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_LIB_COURT, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		col1.setHeaderCssStyle("width:15em;");
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_LIB_COURT);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Libellé");
		col2.setOrderKeyPath(COL_LIB_LONG);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_LIB_LONG, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, COL_LIB_LONG);

		_colonnesMap.takeValueForKey(getColonneAction(), OBJ_KEY + ".action");
	}
	
	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
					new String[] { COL_LIB_COURT, COL_LIB_LONG, OBJ_KEY + ".action" }
				);
	
	
	
    public ActePrestationTbV(WOContext context) {
        super(context);
    }
    
	
	
	@Override
	public NSArray<String> DEFAULT_COLONNES_KEYS() {
		return DEFAULT_COLONNES_KEYS;
	}

	@Override
	public WOActionResults commitSave() {
		if (getDeletedObjects().size() > 0) {
			EOEditingContext ec = ((EOGenericRecord)getDeletedObjects().lastObject()).editingContext();
			for(NSKeyValueCoding delActPrest : getDeletedObjects()) {
				EOActePrestation delObject = ((EOActePrestation) delActPrest);
				delObject.setDateAnnulation( new NSTimestamp() );
			}
			if (isCommitOnValid())
				ec.saveChanges();
		}

		return cancelSave();
	}
}