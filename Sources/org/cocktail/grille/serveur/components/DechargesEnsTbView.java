package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grillefwk.serveur.metier.eof.EODecharge;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeDechargeService;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;

@SuppressWarnings("unchecked")
public class DechargesEnsTbView extends BaseTbView {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1548317987904448240L;
	public static final String COL_ENS_KEY = ERXQ.keyPath(EODecharge.TO_FWKPERS__INDIVIDU_KEY,EOIndividu.NOM_PRENOM_AFFICHAGE_KEY);
	public static final String COL_TYPE_KEY = ERXQ.keyPath(EODecharge.TO_TYPE_DECHARGE_SERVICE_KEY,EOTypeDechargeService.LC_TYPE_DECHARGE_KEY);
	public static final String COL_TYPE_LONG_KEY = ERXQ.keyPath(EODecharge.TO_TYPE_DECHARGE_SERVICE_KEY,EOTypeDechargeService.LL_TYPE_DECHARGE_KEY);
	public static final String COL_PERIODE_KEY = ERXQ.keyPath(EODecharge.PERIODE_DECHARGE_KEY);
	public static final String COL_HEURES_KEY = ERXQ.keyPath(EODecharge.NB_H_DECHARGE_KEY);
static {
		
		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("Enseignant");
		col0.setOrderKeyPath(COL_ENS_KEY);
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_ENS_KEY, "emptyValue");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col0.setAssociations(ass0);		
		col0.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col0, COL_ENS_KEY);
		
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Type");
		col1.setOrderKeyPath(COL_TYPE_KEY);
		col1.setComponent(AcronymCompo.class.getName());
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_TYPE_KEY, "");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass1.setObjectForKey(OBJ_KEY + "."+ COL_TYPE_LONG_KEY, AcronymCompo.BIND_TITLE_KEY);		
		col1.setAssociations(ass1);	
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_TYPE_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Période");
		col2.setOrderKeyPath(COL_PERIODE_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_PERIODE_KEY, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setHeaderCssStyle("width:65px;text-align:center;");
		col2.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col2, COL_PERIODE_KEY);

		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Nb heures");
		col4.setOrderKeyPath(COL_HEURES_KEY);
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_HEURES_KEY, "emptyValue");
		ass4.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col4.setAssociations(ass4);
		col4.setHeaderCssStyle("text-align:center;width:65px;");
		col4.setRowCssStyle("text-align:right;margin-right:5px;");
		_colonnesMap.takeValueForKey(col4, COL_HEURES_KEY);
				
	}
    public DechargesEnsTbView(WOContext context) {
        super(context);
    }
    
    public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_PERIODE_KEY, COL_TYPE_KEY, COL_HEURES_KEY });
    
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