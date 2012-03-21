package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteHoraireCode;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;

public class ResaTbV extends BaseTbView {

	public static final String COL_EC_AP_KEY = ERXQ.keyPath(
			ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.TO_ONE_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY);//,EOScolMaquetteEc.CODE_AND_LIB_KEY);

	public static final String COL_TYPE_AP_KEY = ERXQ.keyPath(
			ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_HORAIRE_CODE_KEY,
			EOScolMaquetteHoraireCode.MHCO_ABREVIATION_KEY);

	public static final String COL_LIBELLE_AP_KEY = ERXQ.keyPath(
			ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.MAP_LIBELLE_KEY);

	public static final String COL_VALEUR_AP_KEY = ERXQ.keyPath(
			ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.MAP_VALEUR_KEY);
	
	public static final String COL_COUT_AP_KEY = ERXQ.keyPath(
			ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.MAP_COUT_KEY);
	
	public static final String COL_GRP_AP_KEY = ERXQ.keyPath(
			ApResaForTbV.TO_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.MAP_GROUPES_KEY);

	static {
		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("E.C.");
		col0.setComponent(EcForTbView.class.getName());
		col0.setOrderKeyPath(COL_EC_AP_KEY);
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_EC_AP_KEY, "");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col0.setAssociations(ass0);
		col0.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col0, COL_EC_AP_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Libelle AP");
		col2.setOrderKeyPath(COL_LIBELLE_AP_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_LIBELLE_AP_KEY, "");
		col2.setAssociations(ass2);
		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, COL_LIBELLE_AP_KEY);

		CktlAjaxTableViewColumn col3 = new CktlAjaxTableViewColumn();
		col3.setLibelle("Valeur");
		col3.setOrderKeyPath(COL_VALEUR_AP_KEY);
		CktlAjaxTableViewColumnAssociation ass3 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_VALEUR_AP_KEY, "");
		ass3.setNumberformat("#0.00");
		col3.setAssociations(ass3);
		col3.setHeaderCssStyle("text-align:center;width:45px;");
		col3.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col3, COL_VALEUR_AP_KEY);

		CktlAjaxTableViewColumn col5 = new CktlAjaxTableViewColumn();
		col5.setLibelle("Groupes");
		col5.setOrderKeyPath(COL_GRP_AP_KEY);
		CktlAjaxTableViewColumnAssociation ass5 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_GRP_AP_KEY, "");
		ass5.setNumberformat("#0");
		col5.setAssociations(ass5);
		col5.setHeaderCssStyle("text-align:center;width:45px;");
		col5.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col5, COL_GRP_AP_KEY);

		CktlAjaxTableViewColumn col6 = new CktlAjaxTableViewColumn();
		col6.setLibelle("Type");
		col6.setOrderKeyPath(COL_TYPE_AP_KEY);
		CktlAjaxTableViewColumnAssociation ass6 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_TYPE_AP_KEY, "");
		col6.setAssociations(ass6);
		col6.setHeaderCssStyle("text-align:center;width:45px;");
		col6.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col6, COL_TYPE_AP_KEY);
		
		CktlAjaxTableViewColumn col7 = new CktlAjaxTableViewColumn();
		col7.setLibelle("Coût");
		col7.setOrderKeyPath(COL_COUT_AP_KEY);
		CktlAjaxTableViewColumnAssociation ass7 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_COUT_AP_KEY, "");
		ass7.setNumberformat("#0.00");
		col7.setAssociations(ass7);
		col7.setHeaderCssStyle("text-align:center;width:45px;");
		col7.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col7, COL_COUT_AP_KEY);

	}

	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_EC_AP_KEY, COL_TYPE_AP_KEY, COL_LIBELLE_AP_KEY,
					COL_VALEUR_AP_KEY,COL_GRP_AP_KEY,COL_COUT_AP_KEY });

	public ResaTbV(WOContext context) {
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