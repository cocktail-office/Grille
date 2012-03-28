package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.IPersonne;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.finder.FinderEnseignant;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOQualifier;

import er.extensions.appserver.ERXDisplayGroup;

public class ModSearchEnseignant extends GrilleComponent {
	
	private static final long serialVersionUID = 1L;
	
	public static final String B_enseignant = "enseignant";
	public static final String B_onSelectEnseignant = "onSelectEnseignant";
	public static final String B_onCancelSearchEnseignant = "onCancelSearchEnseignant";
	
	
	//private EOIndividu enseignant;
	private WODisplayGroup dgEnseignant;
	
	private boolean searchMode;
	
	public ModSearchEnseignant(WOContext context) {
        super(context);
        dgEnseignant = new ERXDisplayGroup<IPersonne>();
    }

	private String searchTypeIntExt = "interne";

	/**
	 * @return the searchTypeIntExt
	 */
	public String searchTypeIntExt() {
		return searchTypeIntExt;
	}

	/**
	 * @param searchTypeIntExt
	 *            the searchTypeIntExt to set
	 */
	public void setSearchTypeIntExt(String searchTypeIntExt) {
		this.searchTypeIntExt = searchTypeIntExt;
	}

	private String searchTypePhysMoral = "individu";

	/**
	 * @return the searchTypePhysMoral
	 */
	public String searchTypePhysMoral() {
		return searchTypePhysMoral;
	}

	/**
	 * @param searchTypePhysMoral
	 *            the searchTypePhysMoral to set
	 */
	public void setSearchTypePhysMoral(String searchTypePhysMoral) {
		this.searchTypePhysMoral = searchTypePhysMoral;
	}
	
	public EOIndividu enseignant() {
		return (EOIndividu)valueForBinding(B_enseignant);
	}

	public void setEnseignant(EOIndividu enseignant) {
		setValueForBinding(enseignant, B_enseignant);
	}

	public EOQualifier enseignantsQualifier() {
		return FinderEnseignant.qualEnseignant(((Session)session()).selectedYear());
	}
	
	public WODisplayGroup dgEnseignant() {
		return dgEnseignant;
	}
	
	public WOActionResults setSearchOn() {
		setSearchMode(true);
		dgEnseignant().clearSelection();
		return null;
	}

	public WOActionResults setSearchOff() {
		setSearchMode(false);
		if(hasBinding(B_onSelectEnseignant)) {
			return (WOActionResults)valueForBinding(B_onSelectEnseignant);
		}
		return null;
	}
	
	public String textButtonChoisirPersonne() {
		if(enseignant()!=null) {
			return "Choisir "+enseignant().getNomAndPrenom();
		} 
		return null;
	}
	
	public WOActionResults cancelSearch(){
		if(hasBinding(B_onCancelSearchEnseignant)) {
			return (WOActionResults)valueForBinding(B_onCancelSearchEnseignant);
		}
		return null;
	}
	
	public boolean searchMode() {
		return searchMode;
	}

	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}
	
	public String aucselectlinkid() {
		return getComponentId()+"_aucselectlink";
	}
	
	public String aucenseignantid() {
		return getComponentId()+"_aucenseignant";
	}
	


	
}