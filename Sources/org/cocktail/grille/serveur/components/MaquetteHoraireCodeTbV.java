package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteHoraireCode;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

@SuppressWarnings("serial")
public class MaquetteHoraireCodeTbV extends BaseTbView {
	
	public static final String BINDING_updateContainerID = "updateContainerID";
	public static final String BINDING_colonnesKeys = "colonnesKeys";
	
	public static final String COL_CODE_KEY = EOScolMaquetteHoraireCode.MHCO_CODE_KEY;
	public static final String COL_LIBELLE_KEY = EOScolMaquetteHoraireCode.MHCO_LIBELLE_KEY;
	public static final String COL_VALEUR = EOScolMaquetteHoraireCode.METHODE_VALEUR_KEY;
	public static final String COL_VALIDITE_KEY = EOScolMaquetteHoraireCode.MHCO_VALIDITE_KEY;

	static {
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Code");
		col1.setOrderKeyPath(COL_CODE_KEY);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_CODE_KEY, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_LIBELLE_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Libellé");
		col2.setOrderKeyPath(COL_LIBELLE_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_LIBELLE_KEY, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, COL_LIBELLE_KEY);

		CktlAjaxTableViewColumn col3 = new CktlAjaxTableViewColumn();
		col3.setLibelle("Valeur");
		col3.setOrderKeyPath(COL_VALEUR);
		CktlAjaxTableViewColumnAssociation ass3 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_VALEUR, "emptyValue");
		ass3.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col3.setAssociations(ass3);
		col3.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col3, COL_VALEUR);

		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Valide");
		col4.setOrderKeyPath(COL_VALIDITE_KEY);
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_VALIDITE_KEY, "emptyValue");
		ass4.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col4.setAssociations(ass4);
		col4.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col4, COL_VALIDITE_KEY);
	}
	
	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_CODE_KEY, COL_LIBELLE_KEY,COL_VALEUR, COL_VALIDITE_KEY });
	
	public EOScolMaquetteHoraireCode unRow;

	public MaquetteHoraireCodeTbV(WOContext context) {
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