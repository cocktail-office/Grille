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

import java.util.TimeZone;

import org.cocktail.fwkcktlajaxwebext.serveur.CocktailAjaxApplication;
import org.cocktail.fwkcktlwebapp.common.CktlLog;
import org.cocktail.fwkcktlwebapp.server.version.A_CktlVersion;
import org.cocktail.grille.serveur.components.Main;
import org.cocktail.scolaritefwk.serveur.finder.Finder;
import org.cocktail.scolarix.serveur.metier.eos.EOBac;

import com.webobjects.eocontrol.EOSharedEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimeZone;
import com.webobjects.foundation.NSTimestampFormatter;
import com.webobjects.foundation._NSUtilities;

import er.extensions.appserver.ERXApplication;

public class Application extends CocktailAjaxApplication  {
	private static final String CONFIG_FILE_NAME = VersionMe.APPLICATIONINTERNALNAME + ".config";
	private static final String CONFIG_TABLE_NAME = "FwkCktlWebApp_GrhumParametres";
	private static final String MAIN_MODEL_NAME = VersionMe.APPLICATIONINTERNALNAME;  
	public static final String	ADMIN_MAIL		= "ADMIN_MAIL";
	public static final String MODE_TEST 	= "MODE_TEST";	
	public static final String APP_MAIL 	= "APP_MAIL";	
	public static final String	GRHUM_HOST_MAIL		= "GRHUM_HOST_MAIL";    	
	
    /**
     * Liste des parametres obligatoires (dans fichier de config ou table grhum_parametres) pour que l'application se lance. 
     * Si un des parametre n'est pas initialisé, il y a une erreur bloquante.
     */
    public static final String[] MANDATORY_PARAMS=new String[]{};
    
    /**
     * Liste des parametres optionnels (dans fichier de config ou table grhum_parametres). 
     * Si un des parametre n'est pas initialisé, il y a un warning.
     */
    public static final String[] OPTIONAL_PARAMS=new String[]{};	
    
    /**
     * Mettre à true pour que votre application renvoie les informations de collecte au serveur de collecte de Cocktail.
     */
    public static final boolean APP_SHOULD_SEND_COLLECTE=false;	
    
    public final NSTimestampFormatter _dateFormat = new NSTimestampFormatter("%d/%m/%Y");
    public final NSTimestampFormatter _dateHourFormat = new NSTimestampFormatter("%d/%m/%Y %H:%M");
    
    
    public static NSTimeZone ntz = null;
	
	public static void main(String[] argv) {
		ERXApplication.main(argv, Application.class);
	}

	private Version _appVersion;
	
	public Application() {
		super();
		_NSUtilities.setClassForName(Main.class, "Main");
	}
	
	public void initApplication() {
		System.out.println("Lancement de l'application serveur " + this.name() + "...");
		super.initApplication();
		initTimeZones();
		//Afficher les infos de connexion des modeles de donnees
		rawLogModelInfos();
		//Verifier la coherence des dictionnaires de connexion des modeles de donnees
		boolean isInitialisationErreur = !checkModel() ;
		System.out.println("Initialisation erreur :"+isInitialisationErreur);
		// test d'un fetch sur le model des constantes scol
		Finder.fetchArray(EOSharedEditingContext.defaultSharedEditingContext(),
				EOBac.ENTITY_NAME, "%s = %s ", new NSArray<String>(
						new String[] { EOBac.BAC_CODE_KEY,
								"S" }), null, true);
		
		//TODO ajouter votre code pour l'initialisation
		
		
	}
	
	public String configFileName() {
		return CONFIG_FILE_NAME;
	}
	
	public String configTableName() {
		return CONFIG_TABLE_NAME;
	}
	
	public String[] configMandatoryKeys() {
		return MANDATORY_PARAMS;
	}
	
	public String[] configOptionalKeys() {
		return OPTIONAL_PARAMS;
	}
	
	public boolean appShouldSendCollecte() {
		return APP_SHOULD_SEND_COLLECTE;
	}

	public String copyright() {
		return appVersion().copyright();
	}
	
	public A_CktlVersion appCktlVersion() {
		return appVersion();
	}
	
    public Version appVersion() {
    	if (_appVersion == null) {
    		_appVersion = new Version();
    	}
    	return _appVersion;
    }
    
	public String mainModelName() {
		return MAIN_MODEL_NAME;
	}
	
	protected void initTimeZones() {
		CktlLog.log("Initialisation du NSTimeZone");
		String tz = config().stringForKey("DEFAULT_NS_TIMEZONE");
		if (tz==null || tz.equals("")) {
			CktlLog.log("Le parametre DEFAULT_NS_TIMEZONE n'est pas defini dans le fichier .config.");
			TimeZone.setDefault( TimeZone.getTimeZone("Europe/Paris" ) );
			NSTimeZone.setDefaultTimeZone(NSTimeZone.timeZoneWithName("Europe/Paris", false) );
		}
		else {
			ntz = NSTimeZone.timeZoneWithName(tz, false);
			if (ntz==null) {
				CktlLog.log("Le parametre DEFAULT_NS_TIMEZONE defini dans le fichier .config n'est pas valide ("+ tz +")");
				TimeZone.setDefault( TimeZone.getTimeZone("Europe/Paris" ) );
				NSTimeZone.setDefaultTimeZone(NSTimeZone.timeZoneWithName("Europe/Paris", false) );
			} 
			else {
				TimeZone.setDefault( ntz);
				NSTimeZone.setDefaultTimeZone( ntz );
			}
		}
		ntz = NSTimeZone.defaultTimeZone();
		CktlLog.log("NSTimeZone par defaut utilise dans l'application:"+NSTimeZone.defaultTimeZone());
		NSTimestampFormatter ntf = new NSTimestampFormatter();
		CktlLog.log("Les NSTimestampFormatter analyseront les dates avec le NSTimeZone: "+ntf.defaultParseTimeZone());
		CktlLog.log("Les NSTimestampFormatter afficheront les dates avec le NSTimeZone: "+ntf.defaultFormatTimeZone());
	}
	
	
	public boolean isModeTest() {		
		return (Boolean) config().booleanForKey(MODE_TEST);
	}
	
	public String adminMail() {
		return config().stringForKey(ADMIN_MAIL);
	}
	
	public String getAppURL() {
		return cgiAdaptorURL()+"/"+VersionMe.APPLICATIONINTERNALNAME+".woa";
	}
	
	public String appMail() {
		return config().stringForKey("APP_MAIL");
	}
	
}
