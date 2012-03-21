package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumn;
import org.cocktail.fwkcktlajaxwebext.serveur.component.tableview.column.CktlAjaxTableViewColumnAssociation;
import org.cocktail.fwkcktlpersonneguiajax.serveur.components.StructureTableView;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOVoeux;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteAp;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteHoraireCode;
import org.cocktail.scolaritemodulesfwk.serveur.components.BaseTbView;
import org.cocktail.scolaritemodulesfwk.serveur.components.TableAction;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.eof.ERXQ;

@SuppressWarnings("unchecked")
public class VoeuxTbV extends BaseTbView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String BINDING_updateContainerID = "updateContainerID";

	public EOVoeux unRow;

	/**
	 * Bindings pour les colonnes a afficher,
	 * {@link StructureTableView#DEFAULT_COLONNES_KEYS}
	 */
	public static final String BINDING_colonnesKeys = "colonnesKeys";
	
	public static final String SHOW_COL_REALISE = "showColRealise";

	public static final String COL_EC_KEY = ERXQ.keyPath(
			EOVoeux.TO_FWK_SCOLARITE__SCOLARITE_FWK_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.TO_ONE_FWK_SCOLARITE__SCOL_MAQUETTE_EC_KEY
			//,EOScolMaquetteEc.MEC_LIBELLE_KEY
			);
	public static final String COL_ACTIVITE_KEY = ERXQ.keyPath(
			EOVoeux.TO_ACTIVITE_KEY
			//, EOActivite.TO_TYPE_ACTIVITE_KEY,EOTypeActivite.LIB_COURT_KEY);//EOActivite.LIBELLE_ACTIVITE
			);
	public static final String COL_TITLE_ACTIVITE_KEY = ERXQ.keyPath(
			EOVoeux.TO_ACTIVITE_KEY, EOActivite.TO_TYPE_ACTIVITE_KEY,EOTypeActivite.LIB_LONG_KEY);
	public static final String COL_LIB_ACTIVITE_KEY = ERXQ.keyPath(
			EOVoeux.TO_ACTIVITE_KEY, EOActivite.COMMENTAIRE_KEY);
	public static final String COL_AP_KEY = EOVoeux.TO_FWK_SCOLARITE__SCOLARITE_FWK_SCOL_MAQUETTE_AP_KEY
			+ "." + EOScolMaquetteAp.MAP_LIBELLE_KEY;
	public static final String COL_TYPE_AP_KEY = ERXQ.keyPath(
			EOVoeux.TO_FWK_SCOLARITE__SCOLARITE_FWK_SCOL_MAQUETTE_AP_KEY,
			EOScolMaquetteAp.TO_FWK_SCOLARITE__SCOL_MAQUETTE_HORAIRE_CODE_KEY,
			EOScolMaquetteHoraireCode.MHCO_ABREVIATION_KEY);
	public static final String COL_INDIV_KEY = EOVoeux.TO_FWKPERS__INDIVIDU_ENSEIGNANT_KEY;
			//+ "." + EOIndividu.NOM_PRENOM_AFFICHAGE_KEY;
	public static final String COL_NB_HEURES_VOEUX_KEY = EOVoeux.NB_HEURES_VOEUX_KEY;
	public static final String COL_NB_HEURES_REALISE_KEY = EOVoeux.NB_HEURE_REALISE_KEY;
	public static final String COL_NB_HEURES_TD_KEY = EOVoeux.NB_HEURE_TD_KEY;
	public static final String COL_NB_HEURES_TD_VOEUX_KEY = EOVoeux.NB_HEURE_TD_VOEUX_KEY;
	public static final String COL_DAT_CREATION_KEY = EOVoeux.D_CREATION_KEY;
	public static final String COL_VALIDE_KEY = EOVoeux.VALIDE_KEY;

	static {
		CktlAjaxTableViewColumn col0 = new CktlAjaxTableViewColumn();
		col0.setLibelle("E.C.");
		col0.setOrderKeyPath(COL_EC_KEY);
		col0.setComponent(EcForTbView.class.getName());
		CktlAjaxTableViewColumnAssociation ass0 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_EC_KEY, "emptyValue");
		ass0.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col0.setAssociations(ass0);

		col0.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col0, COL_EC_KEY);

		CktlAjaxTableViewColumn col1 = new CktlAjaxTableViewColumn();
		col1.setLibelle("Activité");
		col1.setOrderKeyPath(COL_ACTIVITE_KEY);
		//col1.setComponent(AcronymCompo.class.getName());
		col1.setComponent(ActiviteForTbView.class.getName());
		CktlAjaxTableViewColumnAssociation ass1 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_ACTIVITE_KEY, "emptyValue");
		ass1.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass1.setObjectForKey(OBJ_KEY + "."+ COL_TITLE_ACTIVITE_KEY, AcronymCompo.BIND_TITLE_KEY);		
		ass1.setObjectForKey(OBJ_KEY + "."+ COL_LIB_ACTIVITE_KEY, AcronymCompo.BIND_LIBELLE_KEY);
		ass1.setObjectForKey("masqueLibActiv", AcronymCompo.BIND_MASQUE_KEY);
		col1.setAssociations(ass1);

		col1.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col1, COL_ACTIVITE_KEY);

		CktlAjaxTableViewColumn col2 = new CktlAjaxTableViewColumn();
		col2.setLibelle("AP");
		col2.setOrderKeyPath(COL_AP_KEY);
		CktlAjaxTableViewColumnAssociation ass2 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_AP_KEY, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col2.setAssociations(ass2);

		col2.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col2, COL_AP_KEY);

		CktlAjaxTableViewColumn tAp = new CktlAjaxTableViewColumn();
		tAp.setLibelle("Type");
		tAp.setOrderKeyPath(COL_TYPE_AP_KEY);		
		CktlAjaxTableViewColumnAssociation assTap = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_TYPE_AP_KEY, "emptyValue");
		assTap.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);		
		tAp.setAssociations(assTap);
		tAp.setHeaderCssStyle("width:25px;text-align:center;");
		tAp.setRowCssStyle("text-align:center;");
		_colonnesMap.takeValueForKey(tAp, COL_TYPE_AP_KEY);

		CktlAjaxTableViewColumn col4 = new CktlAjaxTableViewColumn();
		col4.setLibelle("Enseignant");
		col4.setOrderKeyPath(COL_INDIV_KEY);
		col4.setComponent(EnseignantForTbV.class.getName());
		CktlAjaxTableViewColumnAssociation ass4 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_INDIV_KEY, "emptyValue");
		ass2.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col4.setAssociations(ass4);
		col4.setRowCssStyle("text-align:left;padding-left:3px;");
		_colonnesMap.takeValueForKey(col4, COL_INDIV_KEY);

		CktlAjaxTableViewColumn col5 = new CktlAjaxTableViewColumn();
		col5.setLibelle("Hr voeux");
		col5.setOrderKeyPath(COL_NB_HEURES_VOEUX_KEY);
		CktlAjaxTableViewColumnAssociation ass5 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_NB_HEURES_VOEUX_KEY, "emptyValue");
		ass5.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col5.setAssociations(ass5);
		col5.setHeaderCssStyle("width:35px;text-align:center;");
		col5.setRowCssStyle("text-align:right;margin-right:3px;");
		_colonnesMap.takeValueForKey(col5, COL_NB_HEURES_VOEUX_KEY);

		CktlAjaxTableViewColumn colTdVoeux = new CktlAjaxTableViewColumn();
		colTdVoeux.setLibelle("Hr TD voeux");
		colTdVoeux.setOrderKeyPath(COL_NB_HEURES_TD_VOEUX_KEY);
		CktlAjaxTableViewColumnAssociation assTdV = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_NB_HEURES_TD_VOEUX_KEY, "emptyValue");
		assTdV.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		colTdVoeux.setAssociations(assTdV);
		colTdVoeux.setRowCssStyle("text-align:right;margin-right:3px;");
		colTdVoeux.setHeaderCssStyle("width:35px;text-align:center;");
		_colonnesMap.takeValueForKey(colTdVoeux, COL_NB_HEURES_TD_VOEUX_KEY);
		
		CktlAjaxTableViewColumn col6 = new CktlAjaxTableViewColumn();
		col6.setLibelle("Fait");
		col6.setOrderKeyPath(COL_NB_HEURES_REALISE_KEY);
		CktlAjaxTableViewColumnAssociation ass6 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_NB_HEURES_REALISE_KEY, "emptyValue");
		ass6.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		// Marche pas bien ass6.setObjectForKey(SHOW_COL_REALISE, "visible");
		col6.setAssociations(ass6);
		col6.setRowCssStyle("text-align:right;margin-right:3px;");
		col6.setHeaderCssStyle("width:35px;text-align:center;");
		_colonnesMap.takeValueForKey(col6, COL_NB_HEURES_REALISE_KEY);
		
		CktlAjaxTableViewColumn col9 = new CktlAjaxTableViewColumn();
		col9.setLibelle("Hr TD Fait");
		col9.setOrderKeyPath(COL_NB_HEURES_TD_KEY);
		CktlAjaxTableViewColumnAssociation ass9 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_NB_HEURES_TD_KEY, "emptyValue");
		ass9.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col9.setAssociations(ass9);
		col9.setRowCssStyle("text-align:right;margin-right:3px;");
		col9.setHeaderCssStyle("width:35px;text-align:center;");
		_colonnesMap.takeValueForKey(col9, COL_NB_HEURES_TD_KEY);

		CktlAjaxTableViewColumn col7 = new CktlAjaxTableViewColumn();
		col7.setLibelle("Date");
		col7.setOrderKeyPath(COL_DAT_CREATION_KEY);
		CktlAjaxTableViewColumnAssociation ass7 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_DAT_CREATION_KEY, "emptyValue");
		ass7.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		ass7.setDateformat("%d/%m/%y");
		col7.setAssociations(ass7);
		col7.setHeaderCssStyle("width:50px;text-align:center;");
		col7.setRowCssStyle("text-align:center;");

		_colonnesMap.takeValueForKey(col7, COL_DAT_CREATION_KEY);

		CktlAjaxTableViewColumn col8 = new CktlAjaxTableViewColumn();
		col8.setLibelle("Va li dé");
		col8.setOrderKeyPath(COL_VALIDE_KEY);
		CktlAjaxTableViewColumnAssociation ass8 = new CktlAjaxTableViewColumnAssociation(
				OBJ_KEY + "." + COL_VALIDE_KEY, "emptyValue");
		ass8.setValueWhenEmptyDynamique(VALUE_WHEN_EMPTY_KEY);
		col8.setAssociations(ass8);
		col8.setRowCssStyle("text-align:center;");
		col8.setHeaderCssStyle("width:10px;font-size:10px;text-align:center;");
		_colonnesMap.takeValueForKey(col8, COL_VALIDE_KEY);

		
		CktlAjaxTableViewColumn colEditAction = getColonneAction();
		colEditAction.setComponent(ActionVoeuxTbV.class.getName());
		CktlAjaxTableViewColumnAssociation assEdit = colEditAction.associations();
		assEdit.setObjectForKey(ActionVoeuxTbV.EDIT_REALISE_METHODE_NAME_KEY,
				ActionVoeuxTbV.EDIT_REALISE_METHODE_NAME_KEY);
		assEdit.setObjectForKey(ActionVoeuxTbV.EDIT_REALISE_METHODE_OBJECT_KEY,
				ActionVoeuxTbV.EDIT_REALISE_METHODE_OBJECT_KEY);
		assEdit.setObjectForKey(ActionVoeuxTbV.CAN_REALISE_KEY,ActionVoeuxTbV.CAN_REALISE_KEY);
		colEditAction.setAssociations(assEdit);
		colEditAction.setHeaderCssStyle("width:35px;text-align:center;");
			
		_colonnesMap.takeValueForKey(colEditAction, OBJ_KEY + ".action");
	}

	public static NSArray<String> DEFAULT_COLONNES_KEYS = new NSArray<String>(
			new String[] { COL_AP_KEY, COL_TYPE_AP_KEY, COL_ACTIVITE_KEY,
					COL_INDIV_KEY, COL_NB_HEURES_VOEUX_KEY,
					COL_NB_HEURES_TD_KEY, COL_DAT_CREATION_KEY,COL_VALIDE_KEY,
					OBJ_KEY + ".action" });

	public VoeuxTbV(WOContext context) {
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
		// TODO Auto-generated method stub
		return getColonnesKeys();
	}

	@Override
	public WOActionResults commitSave() {
		NSArray<NSKeyValueCoding> inscs = getDeletedObjects();
		if (inscs.size() > 0) {
			EOEditingContext ec = ((EOVoeux) inscs.lastObject())
					.editingContext();

			for (NSKeyValueCoding delActObj : inscs) {
				EOVoeux delVoeux = (EOVoeux) delActObj;
				delVoeux.setToActiviteRelationship(null);
				delVoeux.setToFwkScolarite_ScolariteFwkScolMaquetteApRelationship(null);
				delVoeux.setToFwkpers_IndividuEnseignantRelationship(null);
				delVoeux.setToFwkpers_IndividuCreateurRelationship(null);
				delVoeux.editingContext().deleteObject(delVoeux);
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
	
	public String masqueLibActiv = "("+AcronymCompo.JOCKER_MASQUE_KEY+")-";
	
	/**
	 * Nom de la methode a invoquer pour l'édition d'un object
	 * 
	 * @return String methodeName
	 */
	public String getEditRealiseMethodName() {
		return (String) valueForBinding(ActionVoeuxTbV.EDIT_REALISE_METHODE_NAME_KEY);
	}

	public void setEditRealiseMethodName(String lst) {
		setValueForBinding(lst, ActionVoeuxTbV.EDIT_REALISE_METHODE_NAME_KEY);
	}

	public String showColRealise() {
		return (String) valueForBinding(SHOW_COL_REALISE);
	}

	public void setShowColRealisee(String lst) {
		setValueForBinding(lst, SHOW_COL_REALISE);
	}
	/**
	 * WOComponent contenant la methode a invoquer pour l'édition d'un object
	 * 
	 * @return WOComponent methodeObject
	 */
	public WOComponent getEditRealiseMethodObject() {
		return (WOComponent) valueForBinding(ActionVoeuxTbV.EDIT_REALISE_METHODE_OBJECT_KEY);
	}

	public void setEditRealiseMethodObject(WOComponent act) {
		setValueForBinding(act, ActionVoeuxTbV.EDIT_REALISE_METHODE_OBJECT_KEY);
	}
	
	/**
	 * Boolean indiquant si il est possible de saisir le réalisé
	 * 
	 * @return boolean
	 */
	public Boolean getCanRealise() {
		return (Boolean) valueForBinding(ActionVoeuxTbV.CAN_REALISE_KEY);
	}

	public void setCanRealise(Boolean act) {
		setValueForBinding(act, ActionVoeuxTbV.CAN_REALISE_KEY);
	}

	public String rowStyle() {
		String style = super.classLigne();
		if ((style!=null)&&(!"".equals(style))){
			return style;
		}
		if ((objectOccur()!=null)&&("O".equals(((EOVoeux)objectOccur()).valide()))){
			return "valide";
		}
		return null;
	}

}
