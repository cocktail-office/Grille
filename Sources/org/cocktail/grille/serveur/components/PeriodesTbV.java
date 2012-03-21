package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.grillefwk.serveur.metier.eof.EOPeriodicite;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;

public class PeriodesTbV extends BaseTbView {
	public static final String COL_DEBUT_KEY = ERXQ.keyPath(PeriodesResaForTbV.PERIODICITE_KEY, EOPeriodicite.DATE_DEB_KEY);
	public static final String COL_FIN_KEY = ERXQ.keyPath(PeriodesResaForTbV.PERIODICITE_KEY, EOPeriodicite.DATE_FIN_KEY);
	public static final String COL_TOTAL_HR_KEY = ERXQ.keyPath(PeriodesResaForTbV.PERIODICITE_KEY, EOPeriodicite.TOTAL_HR_PERIODICITE);
	static {
		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("Début");
		col0.setOrderKeyPath(COL_DEBUT_KEY);
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_DEBUT_KEY, "");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass0.setDateformat("%d/%m/%Y %H:%M");
		col0.setAssociations(ass0);
		col0.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col0, COL_DEBUT_KEY);
		
		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Fin");
		col2.setOrderKeyPath(COL_FIN_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_FIN_KEY, "");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass2.setDateformat("%d/%m/%Y %H:%M");
		col2.setAssociations(ass2);
		col2.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col2, COL_FIN_KEY);

		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Nb heures");
		col1.setOrderKeyPath(COL_TOTAL_HR_KEY);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_TOTAL_HR_KEY, "");
		// ass1.setNumberformat("#0.00");
		col1.setAssociations(ass1);
		col1.setHeaderCssStyle("width:45px;");
		col1.setRowCssStyle("text-align:right");
		_colonnesMap.takeValueForKey(col1, COL_TOTAL_HR_KEY);
	}

	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_DEBUT_KEY, COL_FIN_KEY, COL_TOTAL_HR_KEY });

    public PeriodesTbV(WOContext context) {
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
	
	@Override
	public String classLigne() {
		String style = super.classLigne();
		if ((style!=null)&&(!"".equals(style))){
			return style;
		}
		if ((objectOccur()!=null)&&(1==(((PeriodesResaForTbV)objectOccur()).getPeriodicite().hcomp()))){
			return "valide";
		}
		return null;
	}
}