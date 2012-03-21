package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.grille.serveur.Session;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSMutableDictionary;

public class PageChangeIndentite extends CktlAjaxWOComponent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String login;

	public PageChangeIndentite(WOContext context) {
        super(context);
    }

	/**
	 * @return the login
	 */
	public String login() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	public String erreurid() {
		return getComponentId()+"_erreurid"; 
	}

	public WOActionResults valide() {
		session().terminate();		
		NSMutableDictionary<String, Object> dicLogin = new NSMutableDictionary<String, Object>();
		dicLogin.setObjectForKey(login, "login");
		String url = context().directActionURLForActionNamed("changeId",dicLogin );
		
		WORedirect redirect = new WORedirect(context());
		redirect.setUrl(url);
		return redirect;		
	}
}