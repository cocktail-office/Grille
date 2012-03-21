package org.cocktail.grille.serveur.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;

import com.webobjects.appserver.WOContext;

public class CommentZoneTbView extends CktlAjaxWOComponent {
	public CommentZoneTbView(WOContext context) {
		super(context);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String VALUE_KEY = "value";

	public Object value() {
		return (Object) valueForBinding(VALUE_KEY);
	}

	public void setValue(Object value) {
		setValueForBinding(value, VALUE_KEY);
	}

	public Integer nbCommentRows() {
		if (hasBinding("rows")) {
			return Integer.decode(valueForBinding("rows") + "");
		}
		int occur = rowsInValues();
		
		if ((occur > 0) && (occur < 2)) {
			occur = 2;
		}
		if (occur > 10) {

			occur = 10;
		}

		return occur + 1;
	}
	
	private int rowsInValues(){
		int occur = 0;
		if (value() != null) {
			Matcher matcher = Pattern.compile("\n").matcher(((String) value()).trim() + "");
			while (matcher.find()) {
				occur++;
			}
		}
		return occur;
	}

	public String style() {
		String retour = "background-color:transparent;overflow:auto;margin:0;padding:0;border:none;font-size:12px;";
		int h = 15;
		int occur = rowsInValues();
		if (occur<=0) {
			return retour+"height:"+h+"px;";
		}
		if (occur>10) {
			return retour+"height:"+(10*h)+"px;";
		}
		
		return retour+"height:"+((occur+1)*h)+"px;";
	}

}