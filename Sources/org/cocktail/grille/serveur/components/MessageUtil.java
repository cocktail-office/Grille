package org.cocktail.grille.serveur.components;



import java.util.UUID;

import org.cocktail.grillefwk.serveur.UtilMessages;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.foundation.ERXStringUtilities;

public class MessageUtil extends WOComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1287116959947239484L;

	private NSMutableArray<UtilMessages> lstMessages;

	private UtilMessages errorOccur;
	private UtilMessages infoOccur;

	public MessageUtil(WOContext context) {
		super(context);		
	}

	public boolean isErrorMessages() {
		return !getLstErrorMessages().isEmpty();
	}

	public boolean isInfoMessages() {
		return !getLstInfoMessages().isEmpty();
	}

	public boolean isMessages() {
		return (getLstMessages() != null) && (!getLstMessages().isEmpty());
	}

	@SuppressWarnings("unchecked")
	public NSMutableArray<UtilMessages> getLstMessages() {
		if (lstMessages == null)
			lstMessages = (NSMutableArray<UtilMessages>) session()
					.objectForKey(UtilMessages.ARRAY_MESSAGES_NAME);
						
		return lstMessages;
	}

	@SuppressWarnings("unchecked")
	public NSArray<UtilMessages> getLstErrorMessages() {
		return EOQualifier.filteredArrayWithQualifier(getLstMessages(),
				EOQualifier.qualifierWithQualifierFormat(
						UtilMessages.MESSAGE_TYPE_FIELD + "=%@", new NSArray<Integer>(
								UtilMessages.ERROR_MESSAGE)));
	}

	@SuppressWarnings("unchecked")
	public NSArray<UtilMessages> getLstInfoMessages() {
		return EOQualifier.filteredArrayWithQualifier(getLstMessages(),
				EOQualifier.qualifierWithQualifierFormat(
						UtilMessages.MESSAGE_TYPE_FIELD + "=%@", new NSArray<Integer>(
								UtilMessages.INFO_MESSAGE)));
	}

	/**
	 * @return the errorOccur
	 */
	public UtilMessages errorOccur() {
		
		return errorOccur;
	}

	/**
	 * @param errorOccur
	 *            the errorOccur to set
	 */
	public void setErrorOccur(UtilMessages errorOccur) {
		this.errorOccur = errorOccur;
	}

	/**
	 * @return the infoOccur
	 */
	public UtilMessages infoOccur() {
		return infoOccur;
	}

	/**
	 * @param infoOccur
	 *            the infoOccur to set
	 */
	public void setInfoOccur(UtilMessages infoOccur) {
		this.infoOccur = infoOccur;
	}

	@Override
	public void appendToResponse(WOResponse arg0, WOContext arg1) {
		super.appendToResponse(arg0, arg1);
		UtilMessages.clearMessagesUtil(session());
	}

	/**
	 * @return the displayMessage
	 */
	public String displayMessage() {
		return isMessages()?"":"display:none;";
	}

	public WOActionResults checkErrorPersistant() {
		errorOccur.setPersistant(false);
		UtilMessages.clearMessagesUtil(session());
		return null;
	}

	public WOActionResults checkInfoPersistant() {
		infoOccur.setPersistant(false);
		UtilMessages.clearMessagesUtil(session());
		return null;
	}

	private String idMessage;

	/**
	 * @return the idMessage
	 */
	public String idMessage() {
		if (idMessage==null)
		idMessage = ERXStringUtilities.safeIdentifierName("message"
				+ UUID.randomUUID());
		return idMessage;
	}

	private String idErreur;

	/**
	 * @return the idErreur
	 */
	public String idErreur() {
		return idErreur;
	}

	/**
	 * @param idErreur the idErreur to set
	 */
	public void setIdErreur(String idErreur) {
		this.idErreur = idErreur;
	}

	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}

}