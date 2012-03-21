package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;

import com.webobjects.eocontrol.EOEditingContext;

public class ConsultArbreDelegateCtr extends ChooseActiviteDelegateCtr {

	public ConsultArbreDelegateCtr() {
		
	}

	@Override
	public boolean isLink() {
		return (treeItem().getEoObject() instanceof EOActivite)||(treeItem().getEoObject() instanceof EOScolMaquetteEc);
	}
	
	public ConsultArbreDelegateCtr(EOEditingContext ec,
			EOScolFormationAnnee selectedYear) {
		super(ec, selectedYear);
		
	}

}
