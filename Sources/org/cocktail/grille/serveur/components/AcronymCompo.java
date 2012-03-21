package org.cocktail.grille.serveur.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class AcronymCompo extends WOComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3094697303789205L;
	public static final String BIND_TITLE_KEY = "title";
	public final static String BIND_VALUE_KEY = "value";
	public final static String BIND_LIBELLE_KEY = "libelle";
	public final static String BIND_MASQUE_KEY = "masque";
	public final static String JOCKER_MASQUE_KEY = "%";

	public AcronymCompo(WOContext context) {
		super(context);
	}

	public Object value() {
		return (Object) valueForBinding(BIND_VALUE_KEY);
	}

	public void setValue(Object value) {

	}

	public Object title() {
		return (Object) valueForBinding(BIND_TITLE_KEY);
	}

	public void setTitle(Object value) {

	}

	public Object libelle() {
		return (Object) valueForBinding(BIND_LIBELLE_KEY);
	}

	public void setLibelle(Object value) {

	}

	public String masque() {
		return (String) valueForBinding(BIND_MASQUE_KEY);
	}

	public void setMasque(String value) {

	}

	public String valueWhenEmpty;

	public String libAcronym() {
		if ((hasBinding(BIND_MASQUE_KEY)) && (masque() != null)) {
			return masque().replaceAll(JOCKER_MASQUE_KEY,
					(String) valueForBinding(BIND_VALUE_KEY));

		}
		return (String) valueForBinding(BIND_VALUE_KEY);
	}
}