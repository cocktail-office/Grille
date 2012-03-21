package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnsTreeItem;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class FilteredChooseActiviteDelegateCtr extends
		ChooseActiviteDelegateCtr {

	public FilteredChooseActiviteDelegateCtr() {
		// TODO Auto-generated constructor stub
	}

	public FilteredChooseActiviteDelegateCtr(EOEditingContext ec,
			EOScolFormationAnnee selectedYear) {
		super(ec, selectedYear);
		// TODO Auto-generated constructor stub
	}
	
	private GrilleApplicationUser gUser;
	
	public GrilleApplicationUser gUser() {
		return gUser;
	}

	public void setgUser(GrilleApplicationUser gUser) {
		this.gUser = gUser;
	}
	
	private NSMutableDictionary<Object,Boolean > childsNode = new NSMutableDictionary<Object, Boolean>();
	
	@Override
	public NSArray<EnsTreeItem> childrenTreeNodes(Object node) {
		if (!((childsNode.containsKey(node))&&(Boolean.TRUE.equals(childsNode.objectForKey(node))))){					
			boolean edithRight = false;
			NSArray<EnsTreeItem> tmp = super.childrenTreeNodes(node);
			if ((tmp != null)
					&& (tmp.size() > 0)
					&& (tmp.lastObject().getEoObject() instanceof EOActivite)) {
				edithRight = true;
			}
			((EnsTreeItem) node).setChilds(ChooseActivUeDelegateCtr
					.filteredChilds(((EnsTreeItem) node).getChilds(), gUser(),
							getSelectedYear(),edithRight));							
			
			childsNode.setObjectForKey(Boolean.TRUE, node);
		}
		return ((EnsTreeItem) node).getChilds();
	}
	
	@Override
	public NSArray<EOGenericRecord> getLstObjFromSearch() {
		if ((gUser() != null) && (!gUser().canSelectAllMaquette())) {
			NSArray<EOGenericRecord> tmp = super.getLstObjFromSearch();
			NSMutableArray<EOGenericRecord> retour = new NSMutableArray<EOGenericRecord>();

			for (EOGenericRecord ap : tmp) {

				if (gUser.haveRightForActivite((EOActivite) ap,true)) {
					retour.add(ap);
				}
			}
			return retour;
		} else {
			return super.getLstObjFromSearch();
		}
	}
	
}
