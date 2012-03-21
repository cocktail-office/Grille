package org.cocktail.grille.serveur.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;

public class ThrowPdf extends WOComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WOResponse response;
	
	public WOResponse getResponse() {
		return response;
	}
	
	public void setResponse(WOResponse response) {
		this.response = response;
	}
	
	@Override
	public void appendToResponse(WOResponse arg0, WOContext arg1) {
		if (response!=null){
			arg0.setHeaders(response.headers());			
			arg0.setContent(response.content());						
		}
	}	
}
