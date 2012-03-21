package org.cocktail.grille.serveur.components;

import java.lang.reflect.InvocationTargetException;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.scolaritemodulesfwk.serveur.components.TableAction;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class ActionVoeuxTbV extends CktlAjaxWOComponent {
	public final static String EDIT_REALISE_METHODE_NAME_KEY="editRealiseMethodName";
	public final static String EDIT_REALISE_METHODE_OBJECT_KEY="editRealiseMethodObject";
	public final static String CAN_REALISE_KEY="canRealise";
	public ActionVoeuxTbV(WOContext context) {
		super(context);
	}

	public WOActionResults editRealise() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return (WOActionResults) TableAction.invokeMethod(getEditRealiseMethodObject(),
				getEditRealiseMethodName(), new Object[] { value() },
				new Class[] { Object.class });
	}
	
	public String getEditRealiseMethodName() {
		return (String) valueForBinding(EDIT_REALISE_METHODE_NAME_KEY);
	}
	
	public WOComponent getEditRealiseMethodObject() {
		return (WOComponent) valueForBinding(EDIT_REALISE_METHODE_OBJECT_KEY);
	}
	
	public Object value(){		
		return (Object) valueForBinding("value");
	}
	
	
}