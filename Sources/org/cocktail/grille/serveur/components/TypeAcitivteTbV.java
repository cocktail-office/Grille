package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;



public class TypeAcitivteTbV extends BaseTbView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LIB_COURT = EOTypeActivite.LIB_COURT_KEY;
	private static final String LIB_LONG = EOTypeActivite.LIB_LONG_KEY;

	static {

		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("Lib. court");
		col0.setOrderKeyPath(LIB_COURT);
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + LIB_COURT, "emptyValue");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col0.setAssociations(ass0);
		col0.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col0, LIB_COURT);

		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Libelle");
		col1.setOrderKeyPath(LIB_LONG);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + LIB_LONG, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, LIB_LONG);

		_colonnesMap.takeValueForKey(getColonneAction(), OBJ_KEY + ".action");

	}
    public TypeAcitivteTbV(WOContext context) {
        super(context);
    }

	@Override
	public NSArray<String> DEFAULT_COLONNES_KEYS() {
		return new NSArray<String>(
				new String[] {LIB_COURT ,LIB_LONG, OBJ_KEY + ".action" });
	}

	@Override
	public WOActionResults commitSave() {
		if (getDeletedObjects().size() > 0) {
			EOEditingContext ec = ((EOGenericRecord) getDeletedObjects().lastObject())
					.editingContext();
			for (NSKeyValueCoding delDepos : getDeletedObjects()) {
				EOTypeActivite delDisc = ((EOTypeActivite) delDepos);
				if ((delDisc.toActivites() != null)
						&& (delDisc.toActivites().size() > 0)) {
					UtilMessages.creatMessageUtil(session(),
							UtilMessages.INFO_MESSAGE,
							"Ce type est utilisé par des activitées !");
					return null;
				}

				
				((EOGenericRecord) delDepos).editingContext().deleteObject((EOEnterpriseObject) delDepos);
			}
			if (isCommitOnValid())
				ec.saveChanges();
		}

		return cancelSave();
	}
}