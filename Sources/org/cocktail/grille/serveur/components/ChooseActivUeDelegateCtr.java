package org.cocktail.grille.serveur.components;

import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteParcours;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteSemestre;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteUe;
import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseUeDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnsTreeItem;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class ChooseActivUeDelegateCtr extends ChooseUeDelegateCtr {	
	
	private GrilleApplicationUser gUser;

	public ChooseActivUeDelegateCtr(EOEditingContext defaultEditingContext,
			EOScolFormationAnnee selectedYear) {
		super(defaultEditingContext,selectedYear);
	}

	public GrilleApplicationUser gUser() {
		return gUser;
	}

	public void setgUser(GrilleApplicationUser gUser) {
		this.gUser = gUser;
	}

	public static NSArray<EnsTreeItem> filteredChilds(NSArray<EnsTreeItem> tmp, GrilleApplicationUser user, EOScolFormationAnnee an){
		return filteredChilds(tmp,user,an,false);
	}

	
	public static NSArray<EnsTreeItem> filteredChilds(NSArray<EnsTreeItem> tmp, GrilleApplicationUser user, EOScolFormationAnnee an,boolean editRight){
		if ((user != null) && (!user.canSelectAllMaquette())) {			
			if (tmp != null) {
				NSMutableArray<EnsTreeItem> retour = new NSMutableArray<EnsTreeItem>();
				for (EnsTreeItem item : tmp) {
					if (item.getEoObject() != null) {
						if (user.haveRightForObjMaquette(item.getEoObject(),an,editRight)) {
							retour.addObject(item);
						}
					} else {
						if ((item.getItemObject() != null)
								&& (item.getItemObject() instanceof ActiviteTreeItem)) {
							retour.addObject(item);
						}
					}
				}
				return retour;
			} else {
				return tmp;
			}
		}
		return tmp;
	}
	
	private NSMutableDictionary<Object,Boolean > childsNode = new NSMutableDictionary<Object, Boolean>();
	
	@Override
	public NSArray<EnsTreeItem> childrenTreeNodes(Object node) {
		if (!((childsNode.containsKey(node))&&(Boolean.TRUE.equals(childsNode.objectForKey(node))))){
			boolean edithRight = false;
			NSArray<EnsTreeItem> tmp = ((EnsTreeItem) node).getChilds();
			if ((tmp != null)
					&& (tmp.size() > 0)
					&& (tmp.lastObject().getEoObject() instanceof EOScolMaquetteUe)) {
				edithRight = true;
			}
						
			childsNode.setObjectForKey(Boolean.TRUE, node);
			((EnsTreeItem) node).setChilds(filteredChilds(tmp, gUser(),getSelectedYear(),edithRight));	
		}
		return super.childrenTreeNodes(node);
	}
	
	public static NSArray<EOGenericRecord> filteredObjFromSearch(NSArray<EOGenericRecord> tmp, GrilleApplicationUser user,EOScolFormationAnnee an){
		return filteredObjFromSearch(tmp, user, an,false);
	}
		
	
	public static NSArray<EOGenericRecord> filteredObjFromSearch(NSArray<EOGenericRecord> tmp, GrilleApplicationUser user,EOScolFormationAnnee an,boolean editRight){
		if ((user != null)&&(!user.canSelectAllMaquette())) {			
			NSMutableArray<EOGenericRecord> retour = new NSMutableArray<EOGenericRecord>();
			for (EOGenericRecord item : tmp) {
				if (user.haveRightForObjMaquette(item,an,editRight)) {
					retour.add(item);
				}
			}
			return retour;
		} else {
			return tmp;
		}
	}
	
	@Override
	public NSArray<EOGenericRecord> getLstObjFromSearch() {
		return filteredObjFromSearch(super.getLstObjFromSearch(), gUser(),getSelectedYear(),true);		
	}
}
