package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOEcVerrous;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxUpdateContainer;

public class ModVerrouEC extends CktlAjaxWOComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4678308696542649434L;
	public static final String BIND_SELECTED_EC_KEY = "selectedEc";
	public static final String BIND_G_USER_KEY = "gUser";

	public ModVerrouEC(WOContext context) {
		super(context);
	}

	public boolean isVerrouEC() {
		return EOEcVerrous.IsEcLocked(selectedEC());
	}

	public EOScolMaquetteEc selectedEC() {
		return (EOScolMaquetteEc) valueForBinding(BIND_SELECTED_EC_KEY);
	}

	public void setSelectedEC(EOScolMaquetteEc selectedEC) {
		if (canSetValueForBinding(BIND_SELECTED_EC_KEY)) {
			setValueForBinding(selectedEC, BIND_SELECTED_EC_KEY);
		}
	}

	public String auclockecid() {
		return getComponentId() + "_auclockecid";
	}

	public boolean canLockEc() {
		return ((selectedEC() != null) && gUser().canLockEc(selectedEC()));
	}

	public GrilleApplicationUser gUser() {
		return (GrilleApplicationUser) valueForBinding(BIND_G_USER_KEY);
	}

	public void setGuser(GrilleApplicationUser guser) {
		if (canSetValueForBinding(BIND_G_USER_KEY)) {
			setValueForBinding(guser, BIND_G_USER_KEY);
		}
	}

	public EOEcVerrous verrouForEc() {
		return EOEcVerrous.getVerrousForEc(selectedEC());
	}

	public WOActionResults lockEc() {
		EOEditingContext ec = new EOEditingContext(session()
				.defaultEditingContext());
		EOEcVerrous lock = verrouForEc();
		if (lock == null) {
			lock = EOEcVerrous.createEcVerrous(ec, "O");
		} else {
			lock = lock.localInstanceIn(ec);
		}
		lock.setVerrou("O");
		lock.setToFwkpers_IndividuVerrouRelationship(gUser().individu().localInstanceIn(ec));
		lock.setToFwkScolarite_ScolMaquetteEcRelationship(selectedEC()
				.localInstanceIn(ec));
		lock.setDVerrou(new NSTimestamp());
		try {
			ec.saveChanges();
			if (ec.parentObjectStore()
					.equals(session().defaultEditingContext())) {
				session().defaultEditingContext().saveChanges();
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.creatMessageUtil(session(),
					UtilMessages.ERROR_MESSAGE, e.getMessage());

			return null;
		}

		if (hasBinding(CktlAjaxWOComponent.BINDING_updateContainerID)) {
			AjaxUpdateContainer.updateContainerWithID(updateContainerID(),
					context());
		}
		return null;
	}

	public String erreurlockecid() {
		return getComponentId() + "_erreurlockecid";
	}

	public WOActionResults unLockEc() {
		EOEditingContext ec = new EOEditingContext(session()
				.defaultEditingContext());
		EOEcVerrous lock = verrouForEc();
		if (lock != null) {
			lock.setVerrou("N");
		}
		lock.setToFwkpers_IndividuVerrouRelationship(gUser().individu());
		lock.setDVerrou(new NSTimestamp());
		try {
			ec.saveChanges();
			if (ec.parentObjectStore()
					.equals(session().defaultEditingContext())) {
				session().defaultEditingContext().saveChanges();
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilMessages.creatMessageUtil(session(),
					UtilMessages.ERROR_MESSAGE, e.getMessage());

			return null;
		}

		if (hasBinding(CktlAjaxWOComponent.BINDING_updateContainerID)) {
			AjaxUpdateContainer.updateContainerWithID(updateContainerID(),
					context());
		}
		return null;
	}

	public String messageverrouid() {
		return getComponentId() + "_messageverrouid";
	}

}