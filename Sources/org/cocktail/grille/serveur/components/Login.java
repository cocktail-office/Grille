package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlwebapp.server.components.CktlLoginResponder;

import com.webobjects.appserver.WOContext;

public class Login extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Le gestionnaire des evenements de composant de login local. */
	private CktlLoginResponder loginResponder;

	private String login;
	private String password;
	private String messageErreur;
	
    public Login(WOContext context) {
        super(context);
    }
    /**
	 * Retourne la reference vers une instance de gestionnaire des evenements
	 * du composant de login local.
	 */
	public CktlLoginResponder loginResponder() {
		return loginResponder;
	}

	/**
	 * Definit le gestionnaire des evenements du composant de login local.
	 */
	public void registerLoginResponder(CktlLoginResponder responder) {
		loginResponder = responder;
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

	/**
	 * @return the password
	 */
	public String password() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the messageErreur
	 */
	public String messageErreur() {
		return messageErreur;
	}

	/**
	 * @param messageErreur the messageErreur to set
	 */
	public void setMessageErreur(String messageErreur) {
		this.messageErreur = messageErreur;
		System.out.println("setErreur"+messageErreur);
	}
	
	public String messagecontainerid() {	
		return getComponentId()+"_messagecontainerid";
	}
	public String messageutilid() {
		return getComponentId()+"_messageutilid";
	}
}