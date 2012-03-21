package org.cocktail.grille.serveur.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.cocktail.scolaritemodulesfwk.serveur.components.ChooseApDelegateCtr;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnsTreeItem;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSMutableArray;

public class ChooseAP extends BaseModule {
	private EOGenericRecord selectedEOObjet;
	private ChooseApDelegateCtr delegate;
	private Method actionLink;
	private WOComponent parent;
	private NSMutableArray<EnsTreeItem>expendedNodes;

	public ChooseAP(WOContext context) {
		super(context);
	}

	/**
	 * @return the selectedEC
	 */
	public EOGenericRecord selectedEOObjet() {
		return selectedEOObjet;
	}

	/**
	 * @param selectedEc
	 *            the selectedEc to set
	 */
	public void setSelectedEOObjet(EOGenericRecord selectedEOObjet) {
		this.selectedEOObjet = selectedEOObjet;
	}

	public ChooseApDelegateCtr delegate() {
		if ((delegate != null) && (delegate instanceof ChooseApDelegateCtr)) {

		} else
			delegate = new ChooseApDelegateCtr(getEcEdit(), currentYear());
		return delegate;

	}

	public Method actionLink() {
		return actionLink;
	}

	public void setActionLink(Method actionLink) {
		this.actionLink = actionLink;
	}

	@Override
	public void awake() {
		// TODO Auto-generated method stub
		super.awake();
		if ((getExpendedNodes()!= null)&&(!getExpendedNodes().isEmpty())) {
			Iterator<EnsTreeItem> it = getExpendedNodes().iterator();
			while (it.hasNext()) {
				EnsTreeItem node = it.next();
				delegate().selectObjectInTree(node);
			}					
		}
	}

	public WOComponent getParent() {
		return parent;
	}

	public void setParent(WOComponent parent) {
		this.parent = parent;
	}

	public WOActionResults selectAp() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		setSelectedEOObjet(delegate().treeItem().getEoObject());
		expendedNodes.add(delegate().treeItem().getParent());
		actionLink.invoke(this.parent, delegate().treeItem());
		return null;
	}

	public NSMutableArray<EnsTreeItem> getExpendedNodes() {
		return expendedNodes;
	}

	public void setExpendedNodes(NSMutableArray<EnsTreeItem> expendedNodes) {
		this.expendedNodes = expendedNodes;
	}
}