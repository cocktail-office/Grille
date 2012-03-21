package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;

public class ResaOccupTbV extends BaseTbView {
	public static final String COL_INDIVIDU_KEY = ERXQ.keyPath(
			OccupantResaForTbV.TO_INDIVIDU_KEY);//,	EOIndividu.NOM_PRENOM_AFFICHAGE_KEY);
	public static final String COL_TOTAL_INDIVIDU_KEY = ERXQ.keyPath(
			OccupantResaForTbV.NB_HEURES_KEY);
	static {
		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("Individu");
		col0.setComponent(EnseignantForTbV.class.getName());
		col0.setOrderKeyPath(COL_INDIVIDU_KEY);
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_INDIVIDU_KEY, "");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col0.setAssociations(ass0);
		col0.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col0, COL_INDIVIDU_KEY);

		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Total");
		col1.setOrderKeyPath(COL_TOTAL_INDIVIDU_KEY);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_TOTAL_INDIVIDU_KEY, "");
		// ass1.setNumberformat("#0.00");
		col1.setAssociations(ass1);
		col1.setHeaderCssStyle("width:45px;");
		col1.setRowCssStyle("text-align:right");
		_colonnesMap.takeValueForKey(col1, COL_TOTAL_INDIVIDU_KEY);
	}

	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_INDIVIDU_KEY, COL_TOTAL_INDIVIDU_KEY });

	public ResaOccupTbV(WOContext context) {
		super(context);
	}

	@Override
	public NSArray<String> getColonnesKeys() {
		NSArray<String> keys = DEFAULT_COLONNES_KEYS;
		if (hasBinding(BINDING_colonnesKeys)) {
			String keysStr = (String) valueForBinding(BINDING_colonnesKeys);
			keys = NSArray.componentsSeparatedByString(keysStr, ",");
		}
		return keys;
	}

	public String emptyValue() {
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