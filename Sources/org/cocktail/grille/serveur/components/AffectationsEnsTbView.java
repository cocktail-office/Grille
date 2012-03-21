package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.EOStructure;
import org.cocktail.grillefwk.serveur.metier.eof.EOVQuotiteEnseignants;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;

public class AffectationsEnsTbView extends BaseTbView {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5461342607247137157L;
	public static final String COL_STRUCTURE_KEY = ERXQ.keyPath(EOVQuotiteEnseignants.TO_FWKPERS__STRUCTURE_KEY,EOStructure.LL_STRUCTURE_KEY);
	public static final String COL_ANNEE_KEY = ERXQ.keyPath(EOVQuotiteEnseignants.TO_FWK_SCOLARITE__SCOL_FORMATION_ANNEE_KEY,EOScolFormationAnnee.FANN_DEBUT_KEY);
	public static final String COL_DEBU_KEY = EOVQuotiteEnseignants.D_DEB_AFFECTATION_KEY;
	public static final String COL_FIN_KEY = EOVQuotiteEnseignants.D_FIN_AFFECTATION_KEY;
	public static final String COL_QUOTITE_KEY = EOVQuotiteEnseignants.DEN_QUOT_AFFECTATION_KEY;
	public static final String COL_ENS_KEY = ERXQ.keyPath(EOVQuotiteEnseignants.TO_FWKPERS__INDIVIDU_KEY,EOIndividu.NOM_PRENOM_AFFICHAGE_KEY);
	
	static {
		
		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("Enseignant");
		col0.setOrderKeyPath(COL_ENS_KEY);
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_ENS_KEY, "emptyValue");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col0.setAssociations(ass0);
		//col1.setHeaderCssStyle("width:15em;");
		col0.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col0, COL_ENS_KEY);
		
		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Service");
		col1.setOrderKeyPath(COL_STRUCTURE_KEY);
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_STRUCTURE_KEY, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col1.setAssociations(ass1);
		//col1.setHeaderCssStyle("width:15em;");
		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_STRUCTURE_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("Année");
		col2.setOrderKeyPath(COL_ANNEE_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_ANNEE_KEY, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);
		col2.setHeaderCssStyle("width:70px;");
		col2.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(col2, COL_ANNEE_KEY);

		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Quotité");
		col4.setOrderKeyPath(COL_QUOTITE_KEY);
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_QUOTITE_KEY, "emptyValue");
		ass4.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col4.setAssociations(ass4);
		col4.setRowCssStyle("text-align:center;");
		col4.setHeaderCssStyle("width:60px;text-align:center;");
		_colonnesMap.takeValueForKey(col4, COL_QUOTITE_KEY);

		CktlAjaxTableViewColumn col5 = new CktlAjaxTableViewColumn();
		col5.setLibelle("Début");
		col5.setOrderKeyPath(COL_DEBU_KEY);
		CktlAjaxTableViewColumnAssociation ass5 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_DEBU_KEY, "emptyValue");
		ass5.setDateformat("dd/MM/yyyy");
		ass5.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col5.setAssociations(ass5);
		col5.setHeaderCssStyle("width:60px;text-align:center;");
		col5.setRowCssStyle("text-align:center");
		_colonnesMap.takeValueForKey(col5, COL_DEBU_KEY);

		CktlAjaxTableViewColumn col6 = new CktlAjaxTableViewColumn();
		col6.setLibelle("Fin");
		col6.setOrderKeyPath(COL_FIN_KEY);
		CktlAjaxTableViewColumnAssociation ass6 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "."+ COL_FIN_KEY, "emptyValue");
		ass6.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass6.setDateformat("dd/MM/yyyy");
		col6.setAssociations(ass6);
		col6.setHeaderCssStyle("width:60px;text-align:center;");
		col6.setRowCssStyle("text-align:center");
		_colonnesMap.takeValueForKey(col6, COL_FIN_KEY);
				
	}
    public AffectationsEnsTbView(WOContext context) {
        super(context);
    }
    
    public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_STRUCTURE_KEY, COL_QUOTITE_KEY, COL_DEBU_KEY,
					COL_FIN_KEY });
    
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