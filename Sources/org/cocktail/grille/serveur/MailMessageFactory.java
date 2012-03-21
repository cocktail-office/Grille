package org.cocktail.grille.serveur;

import org.cocktail.fwkcktlwebapp.common.util.CktlMailMessage;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;

import com.webobjects.appserver.WOApplication;

public abstract class MailMessageFactory {
	static Application app = (Application) WOApplication.application();

	public static final String LBR = "\n";
	public static final String HTML_LBR = "<br/>";

	public static String mailHost = app.config().stringForKey(Application.GRHUM_HOST_MAIL);;

	/**
	 * Envoi de mail de validation du service enseignant
	 */
	public static void sendMailValidService(String email, GrilleApplicationUser user)
			throws Exception {
		if (mailHost == null) {
			throw new Exception(
					"Le serveur de mail est inconnu !\nNo mail server available in grhum or configfile !");
		}	
		StringBuffer buf = new StringBuffer();
		if (app.isModeTest()) {
			buf.append("--CECI EST UN TEST - NE PAS EN TENIR COMPTE--")
					.append(HTML_LBR).append(HTML_LBR);
		}
		String url = app.getAppURL();
		buf.append("<html><body>");
		buf.append("<p>");
		buf.append("Bonjour, "+HTML_LBR);
		buf.append("Votre service a &eacute;t&eacute; valid&eacute; par "+user.getNomAndPrenom()+ " sur l'application "+VersionMe.APPLICATIONFINALNAME+".");
		buf.append(HTML_LBR);
		buf.append("Tant que votre service est valid&eacute;, vous ne pouvez plus le modifier.");		
		buf.append(HTML_LBR);
		buf.append("Vous pouvez v&eacute;rifier votre service sur <a href=").append("\"").append(url).append("\">").append(url).append("</a>");
		buf.append("<p>");

		String to = email;

		if (app.isModeTest()) {
			to = app.adminMail();
		}

		buf.append("<html><body>");

		CktlMailMessage mailMessage = new CktlMailMessage(mailHost);
		mailMessage.setEncoding("UTF-8");
		mailMessage.setContentType("text/html");

		String subject = "["+VersionMe.APPLICATIONFINALNAME+"] Validation de votre service";

		String from = (user.getEmailPro()!=null?user.getEmailPro():app.appMail());
		mailMessage.initMessage(from, to, subject, buf.toString(),
				null, null);
		// mailMessage.addBCC(from);
		mailMessage.safeSend();
	}
	
	/**
	 * Envoi de mail de dévalidation du service enseignant
	 */
	public static void sendMailInvalideService(String email, GrilleApplicationUser user)
			throws Exception {
		if (mailHost == null) {
			throw new Exception(
					"Le serveur de mail est inconnu !\nNo mail server available in grhum or configfile !");
		}	
		StringBuffer buf = new StringBuffer();
		if (app.isModeTest()) {
			buf.append("--CECI EST UN TEST - NE PAS EN TENIR COMPTE--")
					.append(HTML_LBR).append(HTML_LBR);
		}
		String url = app.getAppURL();
		buf.append("<html><body>");
		buf.append("<p>");
		buf.append("Bonjour, "+HTML_LBR);
		buf.append("Votre service a &eacute;t&eacute; d&eacute;-valid&eacute; par "+user.getNomAndPrenom()+ " sur l'application "+VersionMe.APPLICATIONFINALNAME+".");
		buf.append(HTML_LBR);
		buf.append("Vous pouvez modifier votre service tant que celui-ci n'est pas valid&eacute; par un grilleur.");
		buf.append(HTML_LBR);
		buf.append("Vous pouvez v&eacute;rifier votre service et faire des voeux sur <a href=").append("\"").append(url).append("\">").append(url).append("</a>");
		buf.append("<p>");

		String to = email;

		if (app.isModeTest()) {
			to = app.adminMail();
		}

		buf.append("<html><body>");

		CktlMailMessage mailMessage = new CktlMailMessage(mailHost);
		mailMessage.setEncoding("UTF-8");
		mailMessage.setContentType("text/html");

		String subject = "["+VersionMe.APPLICATIONFINALNAME+"] Dé-validation de votre service";
		String from = (user.getEmailPro()!=null?user.getEmailPro():app.appMail());
		mailMessage.initMessage(from, to, subject, buf.toString(),
				null, null);
		// mailMessage.addBCC(from);
		mailMessage.safeSend();
	}
}
