package org.cocktail.grille.serveur.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.cocktail.fwkcktlpersonne.common.metier.AFinder;
import org.cocktail.fwkcktlpersonne.common.metier.EOIndividu;
import org.cocktail.fwkcktlpersonne.common.metier.EORepartStructure;
import org.cocktail.fwkcktlpersonne.common.metier.EOStructure;
import org.cocktail.fwkcktlpersonne.common.metier.IPersonne;
import org.cocktail.fwkcktlwebapp.server.CktlWebSession;
import org.cocktail.grillefwk.serveur.GrilleApplicationUser;
import org.cocktail.grillefwk.serveur.metier.eof.EOVPersonnelActuelEns;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.eof.ERXEC;

public class SearchEnseignant extends BaseModule {
	private EOEditingContext nestedEdc;
	private IPersonne selectedPersonne;
	private NSMutableDictionary<String, String> indSearch;
	protected WODisplayGroup dgIndiv;
	private NSArray<String> lstZonesUpdates, lstUpdateSearch;
	private boolean isAffInfo;
	private EOIndividu consultedIndiv;
	private EOIndividu indivOccur;
	private String classRowStr = "row2";
	private String classRowIndiv = "row2";
	private Method selectIndiv;
	private WOComponent parent;

	/**
	 * fonction javascript executé sur le onSuccess de la selection d'un
	 * individu
	 */
	private String onSuccessSelectIndiv;

	public SearchEnseignant(WOContext context) {
		super(context);
		indSearch = new NSMutableDictionary<String, String>();
		dgIndiv = new WODisplayGroup();
		dgIndiv.setNumberOfObjectsPerBatch(10);
		lstZonesUpdates = new NSArray<String>(new String[] { "tbodyRef",
				"aucPages" });

		lstUpdateSearch = new NSArray<String>(new String[] { "affResult",
				"infosIndividu" });
	}

	public EOEditingContext nestedEdc() {
		if (nestedEdc == null) {
			nestedEdc = ERXEC.newEditingContext(getEcEdit());
		}
		return nestedEdc;
	}

	/**
	 * @return the selectedPersonne
	 */
	public IPersonne selectedPersonne() {
		return selectedPersonne;
	}

	/**
	 * @param selectedPersonne
	 *            the selectedPersonne to set
	 */
	public void setSelectedPersonne(IPersonne selectedPersonne) {
		this.selectedPersonne = selectedPersonne;
	}

	public Integer userPersId() {

		return ((CktlWebSession) session()).connectedUserInfo().persId()
				.intValue();
	}

	public WOActionResults annuler() {
		return null;
	}

	/**
	 * @return the indSearch
	 */
	public NSMutableDictionary<String, String> indSearch() {
		return indSearch;
	}

	/**
	 * @param indSearch
	 *            the indSearch to set
	 */
	public void setIndSearch(NSMutableDictionary<String, String> indSearch) {
		this.indSearch = indSearch;
	}

	public WOActionResults searchIndividu() {
		EOEditingContext ec = session().defaultEditingContext();
		NSMutableArray<String> tbKeys1 = new NSMutableArray<String>();
		NSMutableArray<String> tbKeys2 = new NSMutableArray<String>();
		String conditionStr = "";

		if (indSearch.valueForKey("name") != null) {
			conditionStr += (!conditionStr.equals("")) ? " AND " : "";
			conditionStr += "%s caseInsensitiveLike %s ";
			tbKeys1.add(EOVPersonnelActuelEns.TO_FWKPERS__INDIVIDU_KEY + "."
					+ EOIndividu.NOM_USUEL_KEY);
			tbKeys1.add(indSearch.objectForKey("name") + "*");
			tbKeys2.add(EOIndividu.NOM_USUEL_KEY);
			tbKeys2.add(indSearch.objectForKey("name") + "*");
		}

		if (indSearch.valueForKey("firstname") != null) {
			conditionStr += (!conditionStr.equals("")) ? " AND " : "";
			conditionStr += "%s caseInsensitiveLike %s ";
			tbKeys1.add(EOVPersonnelActuelEns.TO_FWKPERS__INDIVIDU_KEY + "."
					+ EOIndividu.PRENOM_KEY);
			tbKeys1.add(indSearch.objectForKey("firstname") + "*");
			tbKeys2.add(EOIndividu.PRENOM_KEY);
			tbKeys2.add(indSearch.objectForKey("firstname") + "*");
		}

		//individus valides
		conditionStr += (!conditionStr.equals("")) ? " AND " : "";
		conditionStr += "%s caseInsensitiveLike %s ";
		tbKeys1.add(EOVPersonnelActuelEns.TO_FWKPERS__INDIVIDU_KEY + "."
				+ EOIndividu.TEM_VALIDE_KEY);
		tbKeys1.add(EOIndividu.TEM_VALIDE_O);
		tbKeys2.add(EOIndividu.TEM_VALIDE_KEY);
		tbKeys2.add(EOIndividu.TEM_VALIDE_O);

		NSArray<EOVPersonnelActuelEns> tmp = (EOVPersonnelActuelEns.fetchVPersonnelActuelEnses(
				ec, EOQualifier.qualifierWithQualifierFormat(conditionStr,
						tbKeys1), null));
		/*new NSArray(new Object[] { new EOSortOrdering(
				EOVPersonnelActuelEns.FWKPERS__INDIVIDU_KEY+"."+EOIndividu.NOM_AFFICHAGE_KEY,
				EOSortOrdering.CompareCaseInsensitiveAscending) })));*/

		NSArray<EOIndividu> tmpInd = (NSArray<EOIndividu>) tmp
				.valueForKey(EOVPersonnelActuelEns.TO_FWKPERS__INDIVIDU_KEY);

		//ajout des enseignatns externes (dans le groupe ENSEXTGRILLE)
		conditionStr += (!conditionStr.equals("")) ? " AND " : "";
		conditionStr += "%s = %s ";

		tbKeys2.add(EOIndividu.TO_REPART_STRUCTURES_KEY + "."
				+ EORepartStructure.TO_STRUCTURE_GROUPE_KEY + "."
				+ EOStructure.LC_STRUCTURE_KEY);
		tbKeys2.add(GrilleApplicationUser.GRP_ENS_EXT_LIC);
		NSArray<EOIndividu> tmpEns = EOIndividu.fetchAll(ec, EOQualifier
				.qualifierWithQualifierFormat(conditionStr, tbKeys2), null);

		NSMutableArray<EOIndividu> lstInd = tmpInd
				.arrayByAddingObjectsFromArray(tmpEns).mutableClone();

		EOSortOrdering.sortArrayUsingKeyOrderArray(lstInd, new NSArray<EOSortOrdering>(
				new EOSortOrdering[] { new EOSortOrdering(EOIndividu.NOM_AFFICHAGE_KEY,
						EOSortOrdering.CompareCaseInsensitiveAscending) }));
		AFinder.removeDuplicatesInNSArray(lstInd);
		dgIndiv.setObjectArray(lstInd);
		dgIndiv.setNumberOfObjectsPerBatch(10);
		dgIndiv.setCurrentBatchIndex(1);
		setAffInfo(false);
		setConsultedIndiv(null);
		return null;
	}

	public WODisplayGroup getDgIndiv() {
		return dgIndiv;
	}

	public void setDgIndiv(WODisplayGroup dgIndiv) {
		this.dgIndiv = dgIndiv;
	}

	/**
	 * @return the isEmptyList
	 */
	public boolean isEmptyList() {
		return (dgIndiv.allObjects().count() <= 0);
	}

	/**
	 * @return the isAffInfo
	 */
	public boolean isAffInfo() {
		return isAffInfo;
	}

	/**
	 * @param isAffInfo
	 *            the isAffInfo to set
	 */
	public void setAffInfo(boolean isAffInfo) {
		this.isAffInfo = isAffInfo;
	}

	/**
	 * @return the consultedIndiv
	 */
	public EOIndividu consultedIndiv() {
		return consultedIndiv;
	}

	/**
	 * @param consultedIndiv
	 *            the consultedIndiv to set
	 */
	public void setConsultedIndiv(EOIndividu consultedIndiv) {
		this.consultedIndiv = consultedIndiv;

	}

	/**
	 * @return the isNomPatrnymique
	 */
	public boolean isNomPatrnymique() {
		return ((consultedIndiv.nomPatronymiqueAffichage() != null) && (!consultedIndiv
				.nomAffichage().equals(
						consultedIndiv.nomPatronymiqueAffichage())));
	}

	/**
	 * @return the classRowStr
	 */
	public String classRowStr() {
		classRowStr = ((classRowStr.equals("row1")) ? "row2" : "row1");
		return classRowStr;
	}

	/**
	 * @return the classRowIndiv
	 */
	public String classRowIndiv() {
		if (indivOccur.equals(consultedIndiv))
			classRowIndiv = "selected";
		else
			classRowIndiv = ((classRowIndiv.equals("row1")) ? "row2" : "row1");
		return classRowIndiv;
	}

	/**
	 * @return the indivOccur
	 */
	public EOIndividu IndivOccur() {
		return indivOccur;
	}

	/**
	 * @param indivOccur
	 *            the indivOccur to set
	 */
	public void setIndivOccur(EOIndividu indivOccur) {
		this.indivOccur = indivOccur;
	}

	public WOActionResults selectIndividu() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		selectIndiv.invoke(getParent(), indivOccur);
		return null;
	}

	public WOActionResults consultIndiv() {
		setConsultedIndiv(indivOccur);
		setAffInfo(true);
		return null;
	}

	public Method getSelectIndiv() {
		return selectIndiv;
	}

	public void setSelectIndiv(Method selectIndiv) {
		this.selectIndiv = selectIndiv;
	}

	public WOComponent getParent() {
		return parent;
	}

	public void setParent(WOComponent parent) {
		this.parent = parent;
	}

	/**
	 * @return the onSuccessSelectIndiv
	 */
	public String onSuccessSelectIndiv() {
		return onSuccessSelectIndiv;
	}

	/**
	 * @param onSuccessSelectIndiv
	 *            the onSuccessSelectIndiv to set
	 */
	public void setOnSuccessSelectIndiv(String onSuccessSelectIndiv) {
		this.onSuccessSelectIndiv = onSuccessSelectIndiv;
	}

	public NSArray<String> getLstZonesUpdates() {
		return lstZonesUpdates;
	}

	public void setLstZonesUpdates(NSArray<String> lstZonesUpdates) {
		this.lstZonesUpdates = lstZonesUpdates;
	}

	public NSArray<String> getLstUpdateSearch() {
		return lstUpdateSearch;
	}

	/*@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		super.appendToResponse(response, context);
		
		CktlAjaxUtils.addStylesheetResourceInHead(context, response, "GrilleModulesFwk", "css/grillepersonneguiajax.css");

	}*/

}