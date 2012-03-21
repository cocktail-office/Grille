package org.cocktail.grille.serveur.components;

import java.util.Enumeration;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlwebapp.common.CktlLog;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.Utils;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimeZone;
import com.webobjects.foundation.NSValidation;

import er.extensions.components.ERXClickToOpenSupport;

public abstract class BaseModule extends CktlAjaxWOComponent {
	
	private EOScolFormationAnnee currentYear;	
	private EOEditingContext ecEdit;
	
	private boolean haveAdminRight;

	private boolean isEditMode;	
	private NSArray<String> lstEditZones;
	private NSTimeZone timeZone;
	private String modeAction;
	private boolean isEdited = false;
	private boolean needCancel = false;
	private String modeComposant;
	public static final String MODE_ACTION_CREATION = "C";
	public static final String MODE_ACTION_MODIF = "M";
	public static final String LIB_ACTION_CREATION = "Création";
	public static final String LIB_ACTION_MODIF = "Modification";
	public static final String LAST_PAGE = "LAST_PAGE";
	public static final String THIS_PAGE = "THIS_PAGE";
	public static final String ADMIN_MODE = "ADMIN";
	private GrilleApplicationUser gUser;

	public BaseModule(WOContext context) {
		super(context);
		// TODO Auto-generated constructor stub
		setModeAction(MODE_ACTION_MODIF);		
		gUser = ((Session) session()).getGUser();
	}
	

	/**
	 * @return the currentYear
	 */
	public EOScolFormationAnnee currentYear() {
		return currentYear;
	}

	/**
	 * @param currentYear
	 *            the currentYear to set
	 */
	public void setCurrentYear(EOScolFormationAnnee currentYear) {
		this.currentYear = currentYear;
	}
	

	public EOEditingContext getEcEdit() {
		if (ecEdit==null) {
			ecEdit = session().defaultEditingContext();
		}
		return ecEdit;
	}

	/**
	 * Editing context utilisé pour les MAJ
	 * 
	 * @param ecEdit
	 */
	public void setEcEdit(EOEditingContext ecEdit) {
		this.ecEdit = ecEdit;
		if (needCancel) {
			cancelSave();
			needCancel = false;
		}
	}

	public boolean haveAdminRight() {

		return haveAdminRight;
	}

	/**
	 * @param haveAdminRight
	 *            the haveAdminRight to set
	 */
	public void setHaveAdminRight(boolean haveAdminRight) {
		this.haveAdminRight = haveAdminRight;

	}

	public boolean isNotEditMode() {
		return !isEditMode();
	}

	/**
	 * @return the isEditMode
	 */
	public boolean isEditMode() {
		return isEditMode;
	}

	/**
	 * @param isEditMode
	 *            the isEditMode to set
	 */
	public void setEditMode(boolean isEditMode) {
		this.isEditMode = isEditMode;
	}

	public WOActionResults modeEditionOn() {
		setEditMode(true);

		return null;
	}

	public WOActionResults commitSave() {
		// verif que les insert sont bon (ex objectif sans libelle)

		boolean notOk = false;
		NSMutableArray<EOEnterpriseObject> insertedObj = new NSMutableArray<EOEnterpriseObject>(
				getEcEdit().insertedObjects());
		Enumeration<EOEnterpriseObject> objInsOccur = insertedObj.objectEnumerator();
		NSMutableArray< EOEnterpriseObject> objToDelete = new NSMutableArray<EOEnterpriseObject>();
		while (objInsOccur.hasMoreElements()) {
			EOEnterpriseObject obj = (EOEnterpriseObject) objInsOccur
					.nextElement();
			try {
				obj.validateForInsert();
				obj.validateForSave();
			} catch (NSValidation.ValidationException ev) {
				notOk = true;// il y a une erreur de validation
				objToDelete.addObject(obj);
				CktlLog.log("Erreur catché sur savechanges :" + ev.getMessage());
			}
		}
		if (notOk) {
			// il y a une erreur on vire toute les insertion pourraves de l'ec
			objInsOccur = insertedObj.objectEnumerator();
			while (objInsOccur.hasMoreElements()) {
				EOEnterpriseObject obj = (EOEnterpriseObject) objInsOccur
						.nextElement();
				if (Utils.isInRelationWithObjects(obj, objToDelete))
					objToDelete.addObject(obj);
			}
			
			Enumeration<EOEnterpriseObject> objDelOccur = objToDelete.objectEnumerator();
			while (objDelOccur.hasMoreElements()) {
				EOEnterpriseObject obj = (EOEnterpriseObject) objDelOccur
						.nextElement();
				obj.editingContext().deleteObject(obj);
			}
		}

		try {
			getEcEdit().saveChanges();
		} catch (Exception e) {
			UtilMessages.creatMessageUtil(session(),
					UtilMessages.ERROR_MESSAGE, e.getMessage());
			e.printStackTrace();
		}

		setEditMode(false);
		setEdited(false);
		return null;
	}	

	public WOActionResults cancelSave() {
		//CktlLog.log("CANCEL");
		getEcEdit().revert();
		setEditMode(false);
		setEdited(false);
		return null;
	}

	/**
	 * @return the lstEditZones
	 */
	public NSArray<String> lstEditZones() {
		return lstEditZones;
	}

	/**
	 * @param lstEditZones
	 *            the lstEditZones to set
	 */
	public void setLstEditZones(NSArray<String> lstEditZones) {
		this.lstEditZones = lstEditZones;
	}

	

	public NSTimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(NSTimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the libAction
	 */
	public String libAction() {
		return ((modeAction.equals(MODE_ACTION_CREATION)) ? LIB_ACTION_CREATION
				: LIB_ACTION_MODIF);
	}

	public String getModeAction() {
		return modeAction;
	}

	public void setModeAction(String modeAction) {
		this.modeAction = modeAction;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}
	

	@Override
	public void awake() {
		String lastPage = (String) session().objectForKey(LAST_PAGE);
		String thisPage = (String) session().objectForKey(THIS_PAGE);
		// si on change de page on fait un cancel
		if ((lastPage != null) && (!lastPage.equals(thisPage))) {
			needCancel = true;
			session().setObjectForKey(thisPage, LAST_PAGE);
		}
		if ((lastPage == null) && (thisPage != null))
			session().setObjectForKey(thisPage, LAST_PAGE);

		super.awake();
	}

	
	/**
	 * @return the modeComposant
	 */
	public String modeComposant() {
		return modeComposant;
	}

	/**
	 * @param modeComposant
	 *            the modeComposant to set
	 */
	public void setModeComposant(String modeComposant) {
		this.modeComposant = modeComposant;
	}

	public boolean isAdminMode() {
		return (modeComposant != null) && (modeComposant.equals(ADMIN_MODE));
	}
	public static final boolean isClickToOpenEnabled = true;//Boolean.getBoolean(System.getProperty("er.component.clickToOpen", "false"));

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {			
		ERXClickToOpenSupport.preProcessResponse(response, context, isClickToOpenEnabled);		
		super.appendToResponse(response, context);
		ERXClickToOpenSupport.postProcessResponse(getClass(), response, context, isClickToOpenEnabled);

	}

	public GrilleApplicationUser getgUser() {
		if (gUser==null){
			gUser=((Session)session()).getGUser();
		}
		return gUser;
	}


	public void setgUser(GrilleApplicationUser gUser) {
		this.gUser = gUser;
	}
}
