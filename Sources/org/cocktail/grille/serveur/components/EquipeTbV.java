package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolConstanteResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.eof.ERXQ;

public class EquipeTbV extends BaseTbView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String COL_SPEC_KEY = ERXQ.keyPath(EOScolFormationResponsabilite.TO_FWK_SCOLARITE__SCOL_FORMATION_SPECIALISATION_KEY,EOScolFormationSpecialisation.FSPN_LIBELLE_KEY);
	public static final String COL_INDIV_KEY = ERXQ.keyPath(EOScolFormationResponsabilite.TO_ONE_INDIVIDU,EOIndividu.NOM_PRENOM_AFFICHAGE_KEY);
	public static final String COL_TYPE_KEY = ERXQ.keyPath(EOScolFormationResponsabilite.TO_FWK_SCOLARITE__SCOL_CONSTANTE_RESPONSABILITE_KEY,EOScolConstanteResponsabilite.CRES_LIBELLE_KEY);	
	
	static {
		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("Spécialisation");
		col0.setOrderKeyPath(COL_SPEC_KEY);
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_SPEC_KEY, "emptyValue");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col0.setAssociations(ass0);
		col0.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col0, COL_SPEC_KEY);
		
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Individu");
		col1.setOrderKeyPath(COL_INDIV_KEY);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_INDIV_KEY, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_INDIV_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Type");
		col2.setOrderKeyPath(COL_TYPE_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_TYPE_KEY, "");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, COL_TYPE_KEY);

		_colonnesMap.takeValueForKey(getColonneAction(), OBJ_KEY + ".action");
		
	}
	
	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_INDIV_KEY, COL_TYPE_KEY});
	
    public EquipeTbV(WOContext context) {
        super(context);
    }
    
    @Override
	public NSArray<String> DEFAULT_COLONNES_KEYS() {
		return new NSArray<String>(
				new String[] {COL_SPEC_KEY,COL_INDIV_KEY ,COL_TYPE_KEY, OBJ_KEY + ".action" });
	}

	@Override
	public WOActionResults commitSave() {
		if (getDeletedObjects().size() > 0) {
			EOEditingContext ec = ((EOGenericRecord)getDeletedObjects().lastObject())
					.editingContext();
			for (NSKeyValueCoding delUtil : getDeletedObjects()) {				
				((EOGenericRecord)delUtil).editingContext().deleteObject((EOEnterpriseObject) delUtil);
			}
			if (isCommitOnValid())
				ec.saveChanges();
		}

		return cancelSave();
	}
}