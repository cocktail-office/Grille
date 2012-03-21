/*
 * Copyright Cocktail, 2001-2008
 * 
 * This software is governed by the CeCILL license under French law and abiding
 * by the rules of distribution of free software. You can use, modify and/or
 * redistribute the software under the terms of the CeCILL license as circulated
 * by CEA, CNRS and INRIA at the following URL "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package org.cocktail.grille.serveur;

import org.cocktail.fwkcktlwebapp.common.CktlLog;
import org.cocktail.fwkcktlwebapp.common.CktlUserInfo;
import org.cocktail.fwkcktlwebapp.common.database.CktlUserInfoDB;
import org.cocktail.fwkcktlwebapp.common.util.StringCtrl;
import org.cocktail.fwkcktlwebapp.server.CktlWebAction;
import org.cocktail.fwkcktlwebapp.server.components.CktlAlertPage;
import org.cocktail.fwkcktlwebapp.server.components.CktlLogin;
import org.cocktail.fwkcktlwebapp.server.components.CktlLoginResponder;
import org.cocktail.fwkcktlwebapp.server.components.CktlWebPage;
import org.cocktail.grille.serveur.components.Login;
import org.cocktail.grille.serveur.components.Main;
import org.cocktail.grille.serveur.components.MessagePage;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.UtilMessages;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WORedirect;
import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSDictionary;

public class DirectAction extends CktlWebAction {
	public DirectAction(WORequest request) {
		super(request);
	}

	public WOActionResults defaultAction() {
		if (useCasService())
			return pageWithName(Main.class.getName());
		else
			return loginNoCasPage(null);
		//return pageWithName(Main.class.getName());
	}	
	
	public WOActionResults mainAction() {
		return pageWithName(Main.class.getName());
	}
	
	public WOActionResults changeIdAction() {		
		return loginCasSuccessPage((String) context().request().formValueForKey("login"));
	}

	/**
	 * CAS : traitement authentification OK
	 */
	public WOActionResults loginCasSuccessPage(String netid) {
		try {
			CktlUserInfoDB user = new CktlUserInfoDB(cktlApp.dataBus());
			user.compteForLogin(netid, null, true);			
			if (user.errorCode() != CktlUserInfoDB.ERROR_NONE) {
				cktlSession().setObjectForKey(
						"Il y a eut un problème avec l'identification !",
						"MessageErreur");
			}
			
			cktlSession().setConnectedUserInfo(user);

			GrilleApplicationUser gUser = new GrilleApplicationUser(session()
					.defaultEditingContext(), (Integer) user.persId().intValue(), cktlApp
					.config().stringForKey("C_STRUCTURE_ADMIN"));
			boolean canUse = gUser.userCanUseAppli(((Session)session()).selectedYear());
			CktlWebPage page = null;
			if (!canUse) {
				page = (CktlWebPage) pageWithName(MessagePage.class.getName());
				((MessagePage) page)
						.setMessage("Vous n'êtes pas autorisé(e) à  utiliser cette application !");
				((MessagePage) page).setShowMenu(false);
			} else {
				((Session) session()).setGUser(gUser);
				if (gUser.isEnseignant(((Session)session()).selectedYear())){
					((Session)session()).setIndividuForEnseigant(gUser.individu());
				}
				String url = context().directActionURLForActionNamed("main",
						null);
				WORedirect redirect = new WORedirect(context());
				redirect.setUrl(url);
				return redirect;
			}

			return page;

		} catch (Exception e) {
			e.printStackTrace();
			return getErrorPage(e.getMessage());
		}

	}

	/**
	 * CAS : traitement authentification en echec
	 */
	public WOActionResults loginCasFailurePage(String errorMessage, String arg1) {
		CktlLog.log("loginCasFailurePage : " + errorMessage + " (" + arg1 + ")");
		StringBuffer msg = new StringBuffer();
		msg
				.append("Une erreur s'est produite lors de l'authentification de l'uilisateur&nbsp;:<br><br>");
		if (errorMessage != null)
			msg.append("&nbsp;:<br><br>").append(errorMessage);
		return getErrorPage(msg.toString());
	}

	/**
	 * CAS : page par defaut si CAS n'est pas parametre
	 */
	public WOActionResults loginNoCasPage() {
		return pageWithName(Main.class.getName());
	}

	/**
	 * affiche une page avec un message d'erreur
	 */
	private WOComponent getErrorPage(String errorMessage) {
		System.out.println("ERREUR = " + errorMessage);
		CktlAlertPage page = (CktlAlertPage) cktlApp.pageWithName(
				CktlAlertPage.class.getName(), context());
		page.showMessage(null, "ERREUR", errorMessage, null, null, null,
				CktlAlertPage.ERROR, null);
		return page;
	}

	/**
	 * Retourne la directAction attendue d'apres son nom <code>daName</code>. Si
	 * rien n'a ete trouve, alors une page d'avertissement est affichee.
	 */
	public WOActionResults performActionNamed(String aName) {
		WOActionResults result = null;
		try {
			result = super.performActionNamed(aName);
		} catch (Exception e) {
			result = getErrorPage("DirectAction introuvable : \"" + aName
					+ "\"");
		}
		return result;
	}

	public WOActionResults loginCasSuccessPage(String arg0, NSDictionary arg1) {
		return loginCasSuccessPage(arg0);
	}
	
	public WOActionResults validerLoginAction() {
		WOActionResults page = null;
		WORequest request = context().request();
		String login = StringCtrl.normalize((String)request.formValueForKey("identifiant"));
		String password = StringCtrl.normalize((String)request.formValueForKey("mot_de_passe"));
		String messageErreur = null;

		CktlLoginResponder loginResponder = getNewLoginResponder(null);
		CktlUserInfo loggedUserInfo = new CktlUserInfoDB(cktlApp.dataBus());
		if (login.length() == 0) {
			messageErreur = "Vous devez renseigner l'identifiant.";
		}  else {
			if (password == null) password = "";
			loggedUserInfo.setRootPass(loginResponder.getRootPassword());
			loggedUserInfo.setAcceptEmptyPass(loginResponder.acceptEmptyPassword());
			loggedUserInfo.compteForLogin(login, password, true);
			if (loggedUserInfo.errorCode() != CktlUserInfo.ERROR_NONE) {
				if (loggedUserInfo.errorMessage() != null)
					messageErreur = loggedUserInfo.errorMessage();
				CktlLog.rawLog(">> Erreur | "+loggedUserInfo.errorMessage());
			}
		}
		if (messageErreur == null) {
			((Session)session()).setConnectedUserInfo(loggedUserInfo);
			
		}
		if (messageErreur != null) {
			page = (Login)pageWithName(Login.class.getName());
			UtilMessages.creatMessageUtil(session(),
					UtilMessages.ERROR_MESSAGE,messageErreur);
			return page;
		}

		return loginResponder.loginAccepted(null);

	}
	
	@Override
	public WOActionResults loginNoCasPage(NSDictionary actionParams) {
		Login page = (Login)pageWithName(Login.class.getName());
	    page.registerLoginResponder(getNewLoginResponder(actionParams));
	    return page;
	}
	
	public CktlLoginResponder getNewLoginResponder(NSDictionary actionParams) {
		return new DefaultLoginResponder(actionParams);
	}

	public class DefaultLoginResponder implements CktlLoginResponder {
		private NSDictionary actionParams;

		public DefaultLoginResponder(NSDictionary actionParams) {
			this.actionParams = actionParams;
		}

		public NSDictionary actionParams() {
			return actionParams;
		}

		public WOComponent loginAccepted(CktlLogin loginComponent) {			
			WOComponent nextPage = (WOComponent) loginCasSuccessPage(((Session)session()).connectedUserInfo().login()) ;
			
			return nextPage;
		}

		public boolean acceptLoginName(String loginName) {
			return cktlApp.acceptLoginName(loginName);
		}

		public boolean acceptEmptyPassword() {
			return cktlApp.config().booleanForKey("ACCEPT_EMPTY_PASSWORD");
		}

		public String getRootPassword() {
			return cktlApp.getRootPassword();
		}

		
	}
}
