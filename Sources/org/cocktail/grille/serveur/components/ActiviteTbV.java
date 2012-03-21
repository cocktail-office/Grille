package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

@SuppressWarnings("unchecked")
public class ActiviteTbV extends BaseTbView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5016987084882806189L;

	public static final String BINDING_updateContainerID = "updateContainerID";
	
	/**
	 * Bindings pour les colonnes a afficher,
	 * {@link StructureTableView#DEFAULT_COLONNES_KEYS}
	 */

	public static final String COL_LIBELLE_KEY = EOActivite.COMMENTAIRE_KEY;
	public static final String COL_TYPE_KEY = EOActivite.TO_TYPE_ACTIVITE_KEY
			+ "." + EOTypeActivite.LIB_COURT_KEY;
	public static final String COL_TYPE_LONG_KEY = EOActivite.TO_TYPE_ACTIVITE_KEY
	+ "." + EOTypeActivite.LIB_LONG_KEY;
	public static final String COL_NB_HEURES_KEY = EOActivite.NB_HEURES_ACTIVITE_KEY;
	public static final String COL_LIEN_KEY = EOActivite.LIEN_KEY;
	
	static {
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Libelle");
		col1.setOrderKeyPath(COL_LIBELLE_KEY);
		
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_LIBELLE_KEY, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		//col1.setHeaderCssStyle("width:15em;");
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_LIBELLE_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Type");
		col2.setOrderKeyPath(COL_TYPE_KEY);
		col2.setComponent(AcronymCompo.class.getName());
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_TYPE_KEY, "");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass2.setObjectForKey(OBJ_KEY + "."+ COL_TYPE_LONG_KEY, AcronymCompo.BIND_TITLE_KEY);		
		col2.setAssociations(ass2);
		//col2.setHeaderCssStyle(headerCssStyle);
		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, COL_TYPE_KEY);

		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Heures");
		col4.setOrderKeyPath(COL_NB_HEURES_KEY);
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_NB_HEURES_KEY, "emptyValue");
		ass4.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col4.setAssociations(ass4);
		col4.setRowCssStyle("text-align:right;margin-right:3px;");
		col4.setHeaderCssStyle("width:60px;text-align:center;");
		_colonnesMap.takeValueForKey(col4, COL_NB_HEURES_KEY);

		CktlAjaxTableViewColumn col5 = new CktlAjaxTableViewColumn();
		col5.setLibelle("Lié à");
		col5.setComponent("ActiviteLieeA");
		col5.setOrderKeyPath(COL_LIEN_KEY);
		CktlAjaxTableViewColumnAssociation ass5 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_LIEN_KEY, "emptyValue");
		ass5.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col5.setAssociations(ass5);
		//col5.setHeaderCssStyle("width:15em;");
		col5.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col5, COL_LIEN_KEY);

		_colonnesMap.takeValueForKey(getColonneAction(), OBJ_KEY + ".action");
	}

	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_LIBELLE_KEY, COL_TYPE_KEY, COL_NB_HEURES_KEY,
					COL_LIEN_KEY, OBJ_KEY + ".action" });

	public ActiviteTbV(WOContext context) {
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
		NSArray<NSKeyValueCoding> inscs = getDeletedObjects();

		if (inscs.size() > 0) {
			EOEditingContext ec = ((EOActivite) inscs.lastObject())
					.editingContext();

			for (NSKeyValueCoding delActObj : inscs) {
				EOActivite delAct = (EOActivite) delActObj;
				delAct.deleteLien(null);
				delAct.editingContext().deleteObject(delAct);
			}
			if (isCommitOnValid()) {
				ec.saveChanges();
				if ((ec.parentObjectStore().equals(session()
						.defaultEditingContext())))
					session().defaultEditingContext().saveChanges();
			}
			setDg(null);

		}

		return cancelSave();
	}

}