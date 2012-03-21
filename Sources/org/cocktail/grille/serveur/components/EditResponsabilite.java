package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolConstanteResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationResponsabilite;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.eocontrol.EOEditingContext.EditingContextEvent;
import com.webobjects.foundation.NSValidation.ValidationException;

import er.ajax.AjaxModalDialog;
import er.extensions.eof.ERXQ;

import com.webobjects.foundation.NSArray;

public class EditResponsabilite extends CktlAjaxWOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditResponsabilite(WOContext context) {
		super(context);
	}

	public String aucindividuid() {
		return getComponentId() + "_aucindividuid";
	}

	/**
	 * @return the editedResp
	 */
	public EOScolFormationResponsabilite editedResp() {
		return (EOScolFormationResponsabilite) valueForBinding("editedResp");
	}

	/**
	 * @param editedResp
	 *            the editedResp to set
	 */
	public void setEditedResp(EOScolFormationResponsabilite editedResp) {
		setValueForBinding(editedResp, "editedResp");
	}

	public String aucSearch() {
		return getComponentId() + "_aucSearchid";
	}

	private boolean searchMode = false;

	/**
	 * @return the searchMode
	 */
	public boolean searchMode() {
		return searchMode;
	}

	/**
	 * @param searchMode
	 *            the searchMode to set
	 */
	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}

	private WODisplayGroup dgSearch;

	/**
	 * @return the dgSearch
	 */
	public WODisplayGroup dgSearch() {
		return dgSearch;
	}

	/**
	 * @param dgSearch
	 *            the dgSearch to set
	 */
	public void setDgSearch(WODisplayGroup dgSearch) {
		this.dgSearch = dgSearch;
	}

	public WOActionResults setSearchOn() {
		setSearchMode(true);
		AjaxModalDialog.open(context(), searchZoneId(), "Chercher un individu");
		if (dgSearch() != null)
			dgSearch().clearSelection();
		return null;
	}

	public String searchZoneId() {
		return getComponentId() + "_searchZoneId";
	}

	public String aucselectlinkid() {
		return getComponentId() + "_aucselectlink";
	}

	private EOIndividu selIndiv;
	private NSArray<EOScolFormationResponsabilite> lstResp;
	private EOScolConstanteResponsabilite respOccur;

	public EOIndividu getSelIndiv() {
		return selIndiv;
	}

	public void setSelIndiv(EOIndividu selIndiv) {
		this.selIndiv = selIndiv;
	}

	public WOActionResults setSearchOff() {
		setSearchMode(false);
		editedResp().setToOneIndividu(getSelIndiv());
		AjaxModalDialog.close(context());
		return null;
	}

	public String textButtonChoisirPersonne() {
		if (getSelIndiv() != null) {
			return "Choisir " + getSelIndiv().getNomAndPrenom();
		}
		return null;
	}

	public WOActionResults valide() {

		try {
			editedResp().validateForSave();

			editedResp().setFannKey(
					editedResp().toFwkScolarite_ScolFormationAnnee().fannKey());
			editedResp().setFspnKey(
					editedResp().toFwkScolarite_ScolFormationSpecialisation()
							.fspnKey());

			editedResp().setPersId(editedResp().toOneIndividu().persId());
			editedResp().setToOneIndividu(null);

			if (editedResp().editingContext() == null)
				session().defaultEditingContext().insertObject(editedResp());

			if ((editedResp().toFwkScolarite_ScolConstanteResponsabilite() != null)
					&& (!editedResp()
							.editingContext()
							.equals(editedResp()
									.toFwkScolarite_ScolConstanteResponsabilite()
									.editingContext()))) {
				editedResp()
						.setToFwkScolarite_ScolConstanteResponsabiliteRelationship(
								editedResp()
										.toFwkScolarite_ScolConstanteResponsabilite()
										.localInstanceIn(
												editedResp().editingContext()));
			}

			if ((editedResp().toFwkScolarite_ScolFormationAnnee() != null)
					&& (!editedResp().editingContext().equals(
							editedResp().toFwkScolarite_ScolFormationAnnee()
									.editingContext()))) {
				editedResp()
						.setToFwkScolarite_ScolFormationAnneeRelationship(
								editedResp()
										.toFwkScolarite_ScolFormationAnnee()
										.localInstanceIn(
												editedResp().editingContext()));
			}
			if ((editedResp().toFwkScolarite_ScolFormationSpecialisation() != null)
					&& (!editedResp()
							.editingContext()
							.equals(editedResp()
									.toFwkScolarite_ScolFormationSpecialisation()
									.editingContext()))) {
				editedResp()
						.setToFwkScolarite_ScolFormationSpecialisationRelationship(
								editedResp()
										.toFwkScolarite_ScolFormationSpecialisation()
										.localInstanceIn(
												editedResp().editingContext()));
			}

			editedResp().editingContext().saveChanges();
			if (editedResp().editingContext().parentObjectStore()
					.equals(session().defaultEditingContext()))
				session().defaultEditingContext().saveChanges();

		} catch (ValidationException e) {
			UtilMessages.creatMessageUtil(session(), UtilMessages.INFO_MESSAGE,
					e.getMessage());

			return null;
		}

		setEditedResp(null);

		CktlAjaxWindow.close(context(), idWindow());
		return null;
	}

	public String onSuccessValid() {
		return "function (ok) {" + functionRefresh() + ";}";
	}

	public String functionRefresh() {
		return (String) valueForBinding("functionRefresh");
	}

	public void setFunctionRefresh(String functionRefresh) {
		setValueForBinding(functionRefresh, "functionRefresh");
	}

	public WOActionResults cancel() {
		if ((editedResp() != null) && (editedResp().editingContext() != null))
			editedResp().editingContext().revert();
		setEditedResp(null);

		CktlAjaxWindow.close(context(), idWindow());
		return null;
	}

	/**
	 * @return the lstResp
	 */
	public NSArray<EOScolFormationResponsabilite> lstResp() {
		
		//if (lstResp == null) {
			lstResp = EOScolConstanteResponsabilite
					.fetchAll(
							editedResp().editingContext(),
							new NSArray<EOSortOrdering>(
									new EOSortOrdering[] { new EOSortOrdering(
											ERXQ.keyPath(EOScolConstanteResponsabilite.CRES_PRIORITE_KEY),
											EOSortOrdering.CompareAscending) }));
		//}
		return lstResp;
	}

	/**
	 * @param lstResp
	 *            the lstResp to set
	 */
	public void setLstResp(NSArray<EOScolFormationResponsabilite> lstResp) {
		this.lstResp = lstResp;
	}

	/**
	 * @return the respOccur
	 */
	public EOScolConstanteResponsabilite respOccur() {
		return respOccur;
	}

	/**
	 * @param respOccur
	 *            the respOccur to set
	 */
	public void setRespOccur(EOScolConstanteResponsabilite respOccur) {
		this.respOccur = respOccur;

	}

	public String erreureditrespid() {
		return getComponentId() + "_erreureditrespid";
	}

	public String idWindow() {
		return (String) valueForBinding("idWindow");
	}

	public String mode() {
		return (String) valueForBinding("mode");
	}

	public void setMode(String mode) {
		setValueForBinding(mode,"mode");
	}

	public boolean isCreationMode() {
		
		return "A".equals(mode());
	}

public boolean isEditMode() {
		
		return "M".equals(mode());
	}
}
