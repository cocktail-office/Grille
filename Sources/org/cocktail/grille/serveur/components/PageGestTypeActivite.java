package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.eof.ERXEC;

public class PageGestTypeActivite extends BaseGestionRef {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PageGestTypeActivite(WOContext context) {
        super(context);
    }
    private String searchLibObj;

	/**
	 * @return the searchLibObj
	 */
	public String searchLibObj() {
		return searchLibObj;
	}

	
	public void setSearchLibObj(String searchLibObj) {
		this.searchLibObj = searchLibObj;
	}

	@Override
	public NSArray getLstObj() {
		String qualStr = "";
		NSMutableArray<Object> qualArray = new NSMutableArray<Object>();

		if ((searchLibObj() != null) && (!"".equals(searchLibObj().trim()))) {
			qualStr = EOTypeActivite.LIB_LONG_KEY + " caseInsensitiveLike %@ OR "
					+ EOTypeActivite.LIB_COURT_KEY + " caseInsensitiveLike %@ ";
			qualArray.add(searchLibObj() + "*");
			qualArray.add(searchLibObj() + "*");

		}

		return EOTypeActivite
				.fetchTypeActivites(
						session().defaultEditingContext(),
						EOQualifier.qualifierWithQualifierFormat(qualStr,
								qualArray),
						new NSArray<EOSortOrdering>(
								new EOSortOrdering[] { new EOSortOrdering(
										EOTypeActivite.LIB_COURT_KEY,
										EOSortOrdering.CompareCaseInsensitiveAscending) }));
		
	}

	@Override
	public WOActionResults editObj(Object obj) {
		setEditedObj((EOTypeActivite) obj);
		setEdited(Boolean.TRUE);
		CktlAjaxWindow.open(context(), caweditobjid(),
				"Modification d'un type");
		return null;
	}
	
	public WOActionResults addObj() {
		setEditedObj(EOTypeActivite.createTypeActivite(ERXEC.newEditingContext(
				session().defaultEditingContext()), "", ""));
		setEdited(Boolean.TRUE);
		CktlAjaxWindow.open(context(), caweditobjid(),
				"Ajout d'un type");
		return null;
	}
	
	public EOTypeActivite getEditedType() {
		return (EOTypeActivite) getEditedObj();
	}
	
	@Override
	public WOActionResults valid() {
		int mode = -1;
		if (getEditedObj().editingContext().insertedObjects().size() > 0)
			mode = 0;
		else
			mode = 1;
		try {
			getEditedType().validateForSave();
			if (mode == 0)
				getEditedType().validateForInsert();
		} catch (ValidationException e) {
			UtilMessages.creatMessageUtil(session(), UtilMessages.INFO_MESSAGE,
					e.getMessage());
			return null;
		}

		super.valid();

		if (mode == 0) {
			searchObj();
			UtilMessages.creatMessageUtil(session(), UtilMessages.INFO_MESSAGE,
					"Type créé ");
			setEditedObj(EOTypeActivite.createTypeActivite(ERXEC.newEditingContext(
					session().defaultEditingContext()), "", ""));
		} else {			
			setEdited(Boolean.FALSE);
			CktlAjaxWindow.close(context(), caweditobjid());
		}

		return null;
	}
	
	
}