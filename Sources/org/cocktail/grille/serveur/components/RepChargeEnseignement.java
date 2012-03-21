package org.cocktail.grille.serveur.components;

import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grille.serveur.extractions.ExtractionChargeEnseignement;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDomaine;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;

import er.ajax.AjaxUpdateContainer;
import er.extensions.appserver.ERXResponse;

public class RepChargeEnseignement extends GrilleComponent {
	
    private static final long serialVersionUID = 1L;
    
    public EOScolFormationDomaine itemDomaine;
    private EOScolFormationDomaine selectedDomaine;

	private EOScolFormationSpecialisation selectedSpecialisation;

	private EOScolFormationDiplome selectedDiplome;
    
	public RepChargeEnseignement(WOContext context) {
        super(context);
    }
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public WOActionResults extract() {
		
		if(selectedDomaine==null) {
			UtilMessages.creatMessageUtil(getSession(), UtilMessages.ERROR_MESSAGE, "Veuillez sélectionner un domaine !");
			return null;
		}
		
		if(selectedDiplome==null) {
			UtilMessages.creatMessageUtil(getSession(), UtilMessages.ERROR_MESSAGE, "Veuillez sélectionner une formation !");
			return null;
		}
		
		EOScolFormationAnnee annee = getSession().selectedYear();
		
		ExtractionChargeEnseignement ext = new ExtractionChargeEnseignement();
		
		NSData excelData = null;
		
		try {
			excelData = ext.getExtractionChargeEnseignement(selectedDomaine,selectedDiplome,selectedSpecialisation,annee);
		}
		catch(Exception e) {
			UtilMessages.creatMessageUtil(getSession(), UtilMessages.ERROR_MESSAGE, "Une erreur est survenues lors de la génération de l'état !");
			e.printStackTrace();
		}
		
		
		ERXResponse resp = null;
		if(excelData!=null) {
			resp = Extraction.prepareFileAsStreamResponse(excelData, "charges_enseignement.xls", Extraction.MIME_XLS);
		}
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	public NSArray<EOScolFormationDomaine> listeDomaines() {
		EOQualifier qualVal = new EOKeyValueQualifier(
														EOScolFormationDomaine.FDOM_VALIDITE_KEY,
														EOQualifier.QualifierOperatorEqual,
														EOScolFormationDomaine.DOMAINE_VALIDE
														);
		return EOScolFormationDomaine.fetchAll(edc(), qualVal, null);
	}
    
    public EOScolFormationDomaine getSelectedDomaine() {
		return selectedDomaine;
	}

	public void setSelectedDomaine(EOScolFormationDomaine object) {
		this.selectedDomaine = object;
	}

    public String listeDiplomesForSelectedDomaine() {
    	return null;
    }

	public EOScolFormationSpecialisation selectedSpecialisation() {
		return selectedSpecialisation;
	}

	public void setSelectedSpecialisation(
			EOScolFormationSpecialisation object) {
		this.selectedSpecialisation = object;
	}

	public EOScolFormationDiplome selectedDiplome() {
		return selectedDiplome;
	}

	public void setSelectedDiplome(EOScolFormationDiplome object) {
		this.selectedDiplome = object;
	}
	
	public String formchargeensId() {
    	return getComponentId()+"_formchargeens";
    }
    
    public String pubdomaineId() {
    	return getComponentId()+"_pubdomaine";
    }
    
    public String pubdiplomeId() {
    	return getComponentId()+"_pubdiplome";
    }




}