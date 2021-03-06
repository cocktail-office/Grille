package org.cocktail.grille.serveur.components;

import java.util.GregorianCalendar;
import java.util.Iterator;

import org.cocktail.fwkcktlwebapp.common.CktlLog;
import org.cocktail.fwkcktlwebapp.common.util.DateCtrl;
import org.cocktail.fwkcktlwebapp.server.components.CktlWebPage;
import org.cocktail.fwkcktlwebapp.server.util.EOModelCtrl;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grille.serveur.VersionMe;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationAnnee;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteResponsableEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteResponsablePar;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteResponsableUe;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.appserver.ERXResponseRewriter;

// Generated by the WOLips Templateengine Plug-in at 3 sept. 2008 14:27:34
public class Wrapper extends CktlWebPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String titre;

	public boolean useDatePicker;
	private EOScolFormationAnnee yearOccur;

	private String connectionBase;

	private boolean showMenu = true;

	private String profilOccur;

	private EOScolFormationResponsabilite respOccur;

	private EOScolMaquetteResponsablePar respParOccur;

	private EOScolMaquetteResponsableUe respUeOccur;

	private EOScolMaquetteResponsableEc respEcOccur;

	public Wrapper(WOContext context) {
		super(context);

	}

	/**
	 * @return the titre
	 */
	public String titre() {
		return titre;
	}

	/**
	 * @param titre
	 *            the titre to set
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	public boolean isConnected() {
		return (cktlSession().connectedUserInfo() != null);
	}

	public NSArray<EOScolFormationAnnee> getLstAnnees() {
		return FinderScolFormationAnnee.getLstAnnees(session()
				.defaultEditingContext());
	}

	public EOScolFormationAnnee getYearOccur() {
		return yearOccur;
	}

	public void setYearOccur(EOScolFormationAnnee yearOccur) {
		this.yearOccur = yearOccur;
	}

	public WOActionResults changeSelectedYear() {
		CktlLog.log("annee :" + ((Session) session()).selectedYear());
		return null;
	}

	/**
	 * @return the libAnnee
	 */
	public String libAnnee() {
		return yearOccur.fannDebut() + "/" + yearOccur.fannFin();
	}

	public EOScolFormationAnnee selectedYear() {
		return ((Session) session()).selectedYear();
	}

	public void setSelectedYear(EOScolFormationAnnee year) {
		((Session) session()).setSelectedYear(year);
	}

	public String copyright() {
		return "(c) " + DateCtrl.nowDay().get(GregorianCalendar.YEAR);
	}

	public String version() {
		return VersionMe.appliVersion();
	}

	/**
	 * @return the connectionBase
	 */
	public String connectionBase() {
		if (connectionBase == null) {
			connectionBase = "";
			EOModelGroup vModelGroup = EOModelGroup.defaultGroup();
			NSMutableArray<String> lstUrl = new NSMutableArray<String>();
			for (int i = 0; i < vModelGroup.models().count(); i++) {
				EOModel tmpEOModel = (EOModel) vModelGroup.models()
						.objectAtIndex(i);
				String urlModel = EOModelCtrl.bdConnexionUrl(tmpEOModel);
				urlModel = urlModel.substring(urlModel.lastIndexOf(":") + 1);
				if (!lstUrl.contains(urlModel))
					lstUrl.addObject(urlModel);
			}
			Iterator<String> it = lstUrl.iterator();
			while (it.hasNext()) {
				connectionBase += (String) it.next() + "<br>";
			}
		}

		return connectionBase;
	}

	/**
	 * @return the showMenu
	 */
	public boolean showMenu() {
		return isConnected() && showMenu;
	}

	/**
	 * @param showMenu
	 *            the showMenu to set
	 */
	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}

	public WOActionResults killSession() {
		return ((Session) session()).goToMainSite();

		/*
		 * session().terminate(); String url =
		 * context().directActionURLForActionNamed("default", null); WORedirect
		 * redirect = new WORedirect(context()); redirect.setUrl(url); return
		 * redirect;
		 */
	}

	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		super.appendToResponse(response, context);
		ERXResponseRewriter.addScriptResourceInHead(response, context, "app",
				"scripts/GrilleModules.js");
	}

	public String onLoadWarpper() {
		String retour = "initMenu();";
		if ((hasBinding("onLoadPage"))
				&& (!"".equals(valueForBinding("onLoadPage"))))
			retour += valueForBinding("onLoadPage");
		return retour;
	}

	public NSArray<String> lstProfilsUser() {
		return ((Session) session()).getGUser().getLstProfilsUser(
				((Session) session()).selectedYear());
	}

	/**
	 * @return the profilOccur
	 */
	public String profilOccur() {
		return profilOccur;
	}

	/**
	 * @param profilOccur
	 *            the profilOccur to set
	 */
	public void setProfilOccur(String profilOccur) {
		this.profilOccur = profilOccur;
	}

	public NSArray<EOScolFormationResponsabilite> lstRespForProfil() {
		if (profilOccur != null) {
			if (GrilleApplicationUser.LIB_PROFIL_GRILLEUR.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolFormationResponsabilitesGrilleurForYear(
								((Session) session()).selectedYear());
			}
			if (GrilleApplicationUser.LIB_PROFIL_RESP.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolFormationResponsabilitesResponsableForYear(
								((Session) session()).selectedYear());
			}
			return null;
		}
		return null;
	}

	public NSArray<EOScolMaquetteResponsablePar> lstRespParForProfil() {
		if (profilOccur != null) {
			if (GrilleApplicationUser.LIB_PROFIL_GRILLEUR.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolMaquetteResponsabilitesGrilleurParForYear(
								((Session) session()).selectedYear());
			}
			if (GrilleApplicationUser.LIB_PROFIL_RESP.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolMaquetteResponsabilitesResponsableParForYear(
								((Session) session()).selectedYear());
			}
			return null;
		}
		return null;
	}

	public NSArray<EOScolMaquetteResponsableUe> lstRespUeForProfil() {
		if (profilOccur != null) {
			if (GrilleApplicationUser.LIB_PROFIL_GRILLEUR.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolMaquetteResponsabilitesGrilleurUeForYear(
								((Session) session()).selectedYear());
			}
			if (GrilleApplicationUser.LIB_PROFIL_RESP.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolMaquetteResponsabilitesResponsableUeForYear(
								((Session) session()).selectedYear());
			}
			return null;
		}
		return null;
	}

	public NSArray<EOScolMaquetteResponsableEc> lstRespEcForProfil() {
		if (profilOccur != null) {
			if (GrilleApplicationUser.LIB_PROFIL_GRILLEUR.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolMaquetteResponsabilitesGrilleurEcForYear(
								((Session) session()).selectedYear());
			}
			if (GrilleApplicationUser.LIB_PROFIL_RESP.equals(profilOccur)) {
				return ((Session) session()).getGUser()
						.scolMaquetteResponsabilitesResponsableEcForYear(
								((Session) session()).selectedYear());
			}
			return null;
		}
		return null;
	}

	/**
	 * @return the respOccur
	 */
	public EOScolFormationResponsabilite respOccur() {
		return respOccur;
	}

	/**
	 * @param respOccur
	 *            the respOccur to set
	 */
	public void setRespOccur(EOScolFormationResponsabilite respOccur) {
		this.respOccur = respOccur;
	}

	public boolean hasResponsabilites() {
		return ((lstRespForProfil() != null) || (lstRespParForProfil() != null)
				|| (lstRespUeForProfil() != null) || (lstRespEcForProfil() != null));
	}

	/**
	 * @return the respParOccur
	 */
	public EOScolMaquetteResponsablePar respParOccur() {
		return respParOccur;
	}

	/**
	 * @param respParOccur the respParOccur to set
	 */
	public void setRespParOccur(EOScolMaquetteResponsablePar respParOccur) {
		this.respParOccur = respParOccur;
	}

	/**
	 * @return the respUeOccur
	 */
	public EOScolMaquetteResponsableUe respUeOccur() {
		return respUeOccur;
	}

	/**
	 * @param respUeOccur the respUeOccur to set
	 */
	public void setRespUeOccur(EOScolMaquetteResponsableUe respUeOccur) {
		this.respUeOccur = respUeOccur;
	}

	/**
	 * @return the respEcOccur
	 */
	public EOScolMaquetteResponsableEc respEcOccur() {
		return respEcOccur;
	}

	/**
	 * @param respEcOccur the respEcOccur to set
	 */
	public void setRespEcOccur(EOScolMaquetteResponsableEc respEcOccur) {
		this.respEcOccur = respEcOccur;
	}

	public String parcoursProfilLib() {
		return respParOccur.toFwkScolarite_ScolMaquetteParcours().toFwkScolarite_ScolFormationSpecialisation().fspnLibelle()+" - "+respParOccur.toFwkScolarite_ScolMaquetteParcours().mparLibelle();
	}

	public String ueProfilLib() {
		return "UE:"+respUeOccur.toFwkScolarite_ScolMaquetteUe().mueLibelle();
	}
	
	public String ecProfilLib() {
		return "EC:"+respEcOccur.toFwkScolarite_ScolMaquetteEc().mecLibelle();
	}

}