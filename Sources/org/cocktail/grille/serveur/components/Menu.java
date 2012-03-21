package org.cocktail.grille.serveur.components;

import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.extractions.Extraction;
import org.cocktail.grille.serveur.extractions.ExtractionActiviteAnnee;
import org.cocktail.grille.serveur.extractions.ExtractionPrestationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;

import er.extensions.appserver.ERXResponse;

public class Menu extends WOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Menu(WOContext context) {
		super(context);
	}

	public WOActionResults goToArbre() {
		return null;
	}

	public WOActionResults goToVoeux() {
		VoeuxEnseignant next = (VoeuxEnseignant) pageWithName(VoeuxEnseignant.class
				.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());

		return next;
	}

	public WOActionResults goToGestionEc() {
		GestionEC next = (GestionEC) pageWithName(GestionEC.class.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());
		return next;
	}

	public WOActionResults goToVoeuxElp() {
		VoeuxElp next = (VoeuxElp) pageWithName(VoeuxElp.class.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());
		return next;
	}

	public WOActionResults goToVoeuxActiv() {
		PageGestVoeuxActiv next = (PageGestVoeuxActiv) pageWithName(PageGestVoeuxActiv.class
				.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());
		return next;
	}

	public WOActionResults goToGestionTypeActivite() {
		PageGestTypeActivite next = (PageGestTypeActivite) pageWithName(PageGestTypeActivite.class
				.getName());
		return next;
	}

	public WOActionResults goToGestionActePrestation() {
		return (PageGestionActePrestation) pageWithName(PageGestionActePrestation.class
				.getName());
	}

	public WOActionResults goToGestionActivite() {
		GestionActivite next = (GestionActivite) pageWithName(GestionActivite.class
				.getName());
		next.setCurrentYear(((Session) session()).currentYear());
		next.setEcEdit(session().defaultEditingContext());
		next.setHaveAdminRight(((Session) session()).getGUser()
				.haveAdminRight());
		return next;

	}

	public WOActionResults goPrintCoefEqTD() {
		PageGestionImpressions next = (PageGestionImpressions) pageWithName(PageGestionImpressions.class
				.getName());
		return next;
	}

	public WOActionResults goPrintListeActes() {
		PageGestionImpressions next = (PageGestionImpressions) pageWithName(PageGestionImpressions.class
				.getName());
		return next;
	}

	public WOActionResults goCreationListe() {
		PageGestionListes next = (PageGestionListes) pageWithName(PageGestionListes.class
				.getName());
		return next;
	}

	public WOActionResults goEtatService() {
		PageGestionImpressions next = (PageGestionImpressions) pageWithName(PageGestionImpressions.class
				.getName());
		next.setCurrentSubcomponentDisplay(RepEtatService.class.getName());
		return next;
	}

	public boolean canShowMenuEns() {
		return ((((Session) session()).getGUser()
				.isEnseignant(((Session) session()).selectedYear()))
				||(((Session) session()).getGUser()
				.isGrilleur(((Session) session()).selectedYear()))
				|| (((Session) session()).getGUser().haveAdminRight()) || (((Session) session())
				.getGUser().isResponsable(((Session) session()).selectedYear())));
	}

	public WOActionResults goActivitesAnnee() {
		EOScolFormationAnnee annee = ((Session) session()).selectedYear();
		ExtractionActiviteAnnee epa = new ExtractionActiviteAnnee();
		NSData dataActAnnee = null;
		try {
			dataActAnnee = epa.getExtractionActiviteAnnee(annee);
			String fileName = "activites_" + annee.fannDebut() + "-"
					+ annee.fannFin() + ".xls";
			ERXResponse resp = Extraction.prepareFileAsStreamResponse(
					dataActAnnee, fileName, Extraction.MIME_XLS);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public WOActionResults goPrestaAnnee() {
		EOScolFormationAnnee annee = ((Session) session()).selectedYear();
		ExtractionPrestationAnnee epa = new ExtractionPrestationAnnee();
		NSData prestaAnnee = null;
		try {
			prestaAnnee = epa.getExtractionPrestationsAnnee(annee);
			String fileName = "prestations_" + annee.fannDebut() + "-"
					+ annee.fannFin() + ".xls";
			ERXResponse resp = Extraction.prepareFileAsStreamResponse(
					prestaAnnee, fileName, Extraction.MIME_XLS);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public WOActionResults goListeEnsCorps() {
		return pageWithName(ListeEnseignants.class.getName());
	}

	public WOActionResults goListeVoeuxActiv() {
		return pageWithName(ListeVoeuxActivites.class.getName());
		
	}

	public WOActionResults goListeDecharges() {
		return pageWithName(ListeDecharge.class.getName());
	}
	
	public WOActionResults goChargeEnseignement() {
		return pageWithName(RepChargeEnseignement.class.getName());
	}
	
	
	
	public WOActionResults goBilanService() {
		return pageWithName(ListeBilanService.class.getName());
	}

	public boolean canShowTypePresta() {
		return ((((Session) session()).getGUser()
				.isGrilleur(((Session) session()).selectedYear()))
				|| (((Session) session()).getGUser().haveAdminRight()) || (((Session) session())
				.getGUser().isResponsable(((Session) session()).selectedYear())));
	}
	
	public boolean canShowTypesActiv() {
		return ((((Session) session()).getGUser()
				.isGrilleur(((Session) session()).selectedYear()))
				|| (((Session) session()).getGUser().haveAdminRight()) || (((Session) session())
				.getGUser().isResponsable(((Session) session()).selectedYear())));
	}

	public boolean canShowEditions() {
		return ((((Session) session()).getGUser()
				.isGrilleur(((Session) session()).selectedYear()))
				|| (((Session) session()).getGUser().haveAdminRight()) || (((Session) session())
				.getGUser().isResponsable(((Session) session()).selectedYear())));
	}

	public boolean canShowAdmin() {
		return ((Session) session()).getGUser().haveAdminRight();
	}

	public WOActionResults gotoEquipeDipl() {
		return pageWithName(PageGestUtilisateurs.class.getName());
	}

	public boolean canShowGestionRealise() {
		return ((Session) session()).getGUser().canGestionRealise(((Session)session()).selectedYear());
	}
}