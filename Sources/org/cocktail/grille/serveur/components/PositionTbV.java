package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.grillefwk.serveur.metier.eof.EOPosition;
import org.cocktail.grillefwk.serveur.metier.eof.EOVPositionEnseignants;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;

public class PositionTbV extends BaseTbView {
	
	private static final long serialVersionUID = 1L;


	public static final String BINDING_updateContainerID = "updateContainerID";
	
	
	public static final String COL_CODE_KEY = EOVPositionEnseignants.C_POSITION_KEY;
	public static final String COL_LIBELLE_KEY = EOVPositionEnseignants.LC_POSITION_KEY;
	public static final String COL_DEBUT_KEY = EOVPositionEnseignants.D_DEB_POSITION_KEY;
	public static final String COL_FIN_KEY = EOVPositionEnseignants.D_FIN_POSITION_KEY;
	public static final String COL_CREATION_KEY = ERXQ.keyPath(EOVPositionEnseignants.TO_POSITION_KEY,EOPosition.D_CREATION_KEY);
	
	static {
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Position");
		col1.setOrderKeyPath(COL_CODE_KEY);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_CODE_KEY, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_CODE_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Libellé");
		col2.setOrderKeyPath(COL_LIBELLE_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_LIBELLE_KEY, "");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, COL_LIBELLE_KEY);

		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Début");
		col4.setOrderKeyPath(COL_DEBUT_KEY);
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_DEBUT_KEY, "emptyValue");
		ass4.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass4.setDateformat("%d/%m/%Y");
		col4.setAssociations(ass4);
		col4.setRowCssStyle("text-align:center;");
		col4.setHeaderCssStyle("width:60px;text-align:center;");
		_colonnesMap.takeValueForKey(col4, COL_DEBUT_KEY);

		CktlAjaxTableViewColumn col5 = new CktlAjaxTableViewColumn();
		col5.setLibelle("Fin");
		col5.setOrderKeyPath(COL_FIN_KEY);
		CktlAjaxTableViewColumnAssociation ass5 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_FIN_KEY, "emptyValue");
		ass5.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass5.setDateformat("%d/%m/%Y");
		col5.setAssociations(ass5);
		col5.setRowCssStyle("text-align:left;padding-left:3px;");
		col5.setHeaderCssStyle("width:60px;text-align:center;");
		_colonnesMap.takeValueForKey(col5, COL_FIN_KEY);
				
		CktlAjaxTableViewColumn col6 = new CktlAjaxTableViewColumn();
		col6.setLibelle("Enregistré le");
		col6.setOrderKeyPath(COL_CREATION_KEY);
		CktlAjaxTableViewColumnAssociation ass6 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_CREATION_KEY, "emptyValue");
		ass6.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass6.setDateformat("%d/%m/%Y");
		col6.setAssociations(ass6);
		col6.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col6, COL_CREATION_KEY);
		
	}
	
	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_CODE_KEY, COL_LIBELLE_KEY, COL_DEBUT_KEY, COL_FIN_KEY, COL_CREATION_KEY});

	public PositionTbV(WOContext context) {
		super(context);

	}

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

	@Override
	public WOActionResults commitSave() {
		return null;
	}

}