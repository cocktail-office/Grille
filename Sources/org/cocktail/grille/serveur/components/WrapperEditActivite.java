package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;

import com.webobjects.appserver.WOContext;

public class WrapperEditActivite extends BaseModule {
    public WrapperEditActivite(WOContext context) {
        super(context);
    }

	private EOActivite editedActivite;

	/**
	 * @return the editedActivite
	 */
	public EOActivite editedActivite() {
		return editedActivite;
	}

	/**
	 * @param editedActivite the editedActivite to set
	 */
	public void setEditedActivite(EOActivite editedActivite) {
		this.editedActivite = editedActivite;
	}
}