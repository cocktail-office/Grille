package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;

import com.webobjects.foundation.NSArray;

public class ActiviteTreeItem {
	
	private NSArray<EOActivite> childs;

	public ActiviteTreeItem(String libelle,NSArray<EOActivite> activites ) {
		super();
		this.childs=activites;
		this.libelle=libelle;		
	}	

	public NSArray<EOActivite> getChilds() {
		return childs;
	}
	
	
	public void setChilds(NSArray<EOActivite> childs) {
		this.childs = childs;
	}
	
	protected String libelle;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

}
