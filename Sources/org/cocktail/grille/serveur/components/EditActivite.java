package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlajaxwebext.serveur.CktlAjaxWOComponent;
import org.cocktail.fwkcktlajaxwebext.serveur.component.CktlAjaxWindow;
import org.cocktail.grille.serveur.Session;
import org.cocktail.grillefwk.serveur.UtilMessages;
import org.cocktail.grillefwk.serveur.metier.eof.EOActivite;
import org.cocktail.grillefwk.serveur.metier.eof.EOTypeActivite;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.finder.FinderScolFormationDomaine;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDiplome;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationDomaine;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationResponsabilite;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolFormationSpecialisation;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteEc;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteParcours;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteRepartitionSem;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteResponsablePar;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteResponsableUe;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteSemestre;
import org.cocktail.scolaritefwk.serveur.metier.eos.EOScolMaquetteUe;
import org.cocktail.scolaritemodulesfwk.serveur.components.DiplomePickerDefaultDelegate;
import org.cocktail.scolaritemodulesfwk.serveur.components.EnseignementsTreeDelegate;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.ajax.AjaxModalDialog;
import er.ajax.AjaxUpdateContainer;

/**
 * Composant d'édition d'une activité <br>
 * binding : <br>
 * ec : editing context editedActivite : activité a éditer
 * 
 * @author jlmatas
 * 
 */
public class EditActivite extends CktlAjaxWOComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8836626491382226921L;
	private Boolean isSaveNoProbleme = false;

	public EditActivite(WOContext context) {
		super(context);
	}

	// private EOActivite editedActivite;

	/**
	 * @return the editedActivite
	 */
	public EOActivite editedActivite() {
		return (EOActivite) valueForBinding("editedActivite");
	}

	/**
	 * @param editedActivite
	 *            the editedActivite to set
	 */
	public void setEditedActivite(EOActivite editedActivite) {
		setValueForBinding(editedActivite, "editedActivite");
	}

	private NSArray<EOTypeActivite> lstTypesActivites;

	/**
	 * @return the lstTypesActivites
	 */
	public NSArray<EOTypeActivite> lstTypesActivites() {
		if (lstTypesActivites == null)
			lstTypesActivites = EOTypeActivite.fetchAllTypeActivites(
					editedActivite().editingContext(),
					new NSArray<EOSortOrdering>(new EOSortOrdering(
							EOTypeActivite.LIB_COURT_KEY,
							EOSortOrdering.CompareAscending)));
		return lstTypesActivites;
	}

	/**
	 * @param lstTypesActivites
	 *            the lstTypesActivites to set
	 */
	public void setLstTypesActivites(NSArray<EOTypeActivite> lstTypesActivites) {
		this.lstTypesActivites = lstTypesActivites;
	}

	private EOTypeActivite typeActiviteOccur;

	/**
	 * @return the typeActiviteOccur
	 */
	public EOTypeActivite typeActiviteOccur() {
		return typeActiviteOccur;
	}

	/**
	 * @param typeActiviteOccur
	 *            the typeActiviteOccur to set
	 */
	public void setTypeActiviteOccur(EOTypeActivite typeActiviteOccur) {
		this.typeActiviteOccur = typeActiviteOccur;
	}

	private String typeLien;

	/**
	 * @return the typeLien
	 */
	public String typeLien() {
		if ((typeLien == null) && (hasBinding("typeLien")))
			typeLien = (String) valueForBinding("typeLien");
		return typeLien;
	}

	/**
	 * @param typeLien
	 *            the typeLien to set
	 */
	public void setTypeLien(String typeLien) {
		this.typeLien = typeLien;
	}

	public WOActionResults chooseTypeLink() {
		return null;
	}

	public boolean isDomSelect() {
		return EOActivite.TYPE_LIEN_DOMAINE.equals(typeLien);
	}

	public boolean isDiplSelect() {
		return EOActivite.TYPE_LIEN_DIPLOME.equals(typeLien);
	}

	public boolean isSpecSelect() {
		return EOActivite.TYPE_LIEN_SPECIALITE.equals(typeLien);
	}

	public boolean isSemSelect() {
		return EOActivite.TYPE_LIEN_SEMESTRE.equals(typeLien);
	}

	public boolean isParSelect() {
		return EOActivite.TYPE_LIEN_PARCOURS.equals(typeLien);
	}

	public boolean isUeSelect() {
		return EOActivite.TYPE_LIEN_UE.equals(typeLien);
	}

	public boolean isEcSelect() {
		return EOActivite.TYPE_LIEN_EC.equals(typeLien);
	}

	private DiplomePickerDefaultDelegate dpSpecDelegate;

	/**
	 * @return the dpSpecDelegate
	 */
	public DiplomePickerDefaultDelegate dpSpecDelegate() {
		if (dpSpecDelegate == null) {
			dpSpecDelegate = new DiplomePickerDefaultDelegate(getEc(),
					editedActivite().toFwkScolarite_ScolFormationAnnee());
		}
		return dpSpecDelegate;
	}

	/**
	 * @param dpSpecDelegate
	 *            the dpSpecDelegate to set
	 */
	public void setDpSpecDelegate(DiplomePickerDefaultDelegate dpSpecDelegate) {
		this.dpSpecDelegate = dpSpecDelegate;
	}

	public EOEditingContext getEc() {
		if (hasBinding("ec"))
			return (EOEditingContext) valueForBinding("ec");

		return session().defaultEditingContext();

	}

	public void setEc(EOEditingContext ec) {
		if (hasBinding("ec"))
			setValueForBinding(ec, "ec");
	}

	/**
	 * @return the treeParDelegate
	 */
	public ChooseActivParcoursDelegateCtr treeParDelegate() {
		return ((Session) session()).currentChooseActivParcoursDelegateCtr();
	}

	/**
	 * @param treeParDelegate
	 *            the treeParDelegate to set
	 */
	public void setTreeParDelegate(
			ChooseActivParcoursDelegateCtr treeParDelegate) {
		((Session) session())
				.setCurrentChooseActivParcoursDelegateCtr(treeParDelegate);
	}

	/**
	 * @return the treeSemDelegate
	 */
	public ChooseActivSemestreDelegateCtr treeSemDelegate() {
		return ((Session) session()).currentChooseActivSemestreDelegateCtr();
	}

	/**
	 * @param treeSemDelegate
	 *            the treeSemDelegate to set
	 */
	public void setTreeSemDelegate(
			ChooseActivSemestreDelegateCtr treeSemDelegate) {
		((Session) session())
				.setCurrentChooseActivSemestreDelegateCtr(treeSemDelegate);
	}

	/**
	 * @return the treeUeDelegate
	 */
	public ChooseActivUeDelegateCtr treeUeDelegate() {
		return ((Session) session()).currentChooseActivUeDelegateCtr();
	}

	/**
	 * @param treeUeDelegate
	 *            the treeUeDelegate to set
	 */
	public void setTreeUeDelegate(ChooseActivUeDelegateCtr treeUeDelegate) {
		((Session) session())
				.setCurrentChooseActivUeDelegateCtr(treeUeDelegate);
	}

	/**
	 * @return the treeEcDelegate
	 */
	public ChooseActivEcDelegateCtr treeEcDelegate() {
		return ((Session) session()).currentChooseActivEcDelegateCtr();
	}

	/**
	 * @param treeEcDelegate
	 *            the treeEcDelegate to set
	 */
	public void setTreeEcDelegate(ChooseActivEcDelegateCtr treeEcDelegate) {
		((Session) session())
				.setCurrentChooseActivEcDelegateCtr(treeEcDelegate);
	}

	public WOActionResults cancel() {
		setSaveNoProbleme(new Boolean(true));
		if ((editedActivite() != null)
				&& (editedActivite().editingContext() != null))
			editedActivite().editingContext().revert();
		setEditedActivite(null);
		setTypeLien(null);
		CktlAjaxWindow.close(context(), idWindow());
		return null;
	}

	public WOActionResults valide() {
		setSaveNoProbleme(new Boolean(true));

		try {
			editedActivite().validateForSave();

		} catch (ValidationException e) {
			UtilMessages.creatMessageUtil(session(), UtilMessages.INFO_MESSAGE,
					e.getMessage());
			setSaveNoProbleme(new Boolean(false));
			return null;
		}

		if (editedActivite().editingContext() == null)
			getEc().insertObject(editedActivite());

		if ((editedActivite().lien() != null)
				&& (!editedActivite().editingContext().equals(
						editedActivite().lien()))) {
			editedActivite().setLien(
					(EOGenericRecord) EOUtilities
							.localInstanceOfObject(editedActivite()
									.editingContext(), editedActivite()));
		}

		if ((editedActivite().toTypeActivite() != null)
				&& (!editedActivite().editingContext().equals(
						editedActivite().toTypeActivite().editingContext()))) {
			editedActivite().setToTypeActiviteRelationship(
					editedActivite().toTypeActivite().localInstanceIn(
							editedActivite().editingContext()));
		}

		editedActivite().editingContext().saveChanges();
		if (editedActivite().editingContext().parentObjectStore()
				.equals(session().defaultEditingContext()))
			session().defaultEditingContext().saveChanges();

		setEditedActivite(null);
		setTypeLien(null);
		CktlAjaxWindow.close(context(), idWindow());
		return null;
	}

	/**
	 * @return the saveNoProbleme
	 */
	public Boolean saveNoProbleme() {
		if (isSaveNoProbleme == null)
			isSaveNoProbleme = new Boolean(true);

		if (isSaveNoProbleme) {
			isSaveNoProbleme = new Boolean(false);
			return new Boolean(true);
		}

		return isSaveNoProbleme;
	}

	/**
	 * @param saveNoProbleme
	 *            the saveNoProbleme to set
	 */
	public void setSaveNoProbleme(Boolean saveNoProbleme) {
		this.isSaveNoProbleme = saveNoProbleme;
	}

	@Override
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	public String libSemestre() {
		if (editedActivite().toFwkScolarite_ScolMaquetteSemestre() != null)
			return "Semestre "
					+ editedActivite().toFwkScolarite_ScolMaquetteSemestre()
							.msemOrdre()
					+ " ("
					+ ((EOScolMaquetteRepartitionSem) editedActivite()
							.toFwkScolarite_ScolMaquetteSemestre()
							.toFwkScolarite_ScolMaquetteRepartitionSems()
							.lastObject())
							.toFwkScolarite_ScolMaquetteParcours()
							.toFwkScolarite_ScolFormationSpecialisation()
							.fspnAbreviation() + ")";
		return null;
	}

	public WOActionResults refreshLink() {
		return null;
	}

	public String onSuccessValid() {
		return "function (ok) {" + functionRefresh() + ";}";
	}

	public String functionRefresh() {
		return (String) valueForBinding("functionRefresh");
	}

	public void setFunctionRefresh(String functionRefresh) {
		setValueForBinding(functionRefresh, "functionRefresh");
	}

	/**
	 * @return the treeSpecDelegate
	 */
	public ChooseActivSpecialisationDelegateCtr treeSpecDelegate() {
		return ((Session) session())
				.currentChooseActivSpecialisationDelegateCtr();
	}

	/**
	 * @param treeSpecDelegate
	 *            the treeSpecDelegate to set
	 */
	public void setTreeSpecDelegate(
			ChooseActivSpecialisationDelegateCtr treeSpecDelegate) {
		((Session) session())
				.setCurrentChooseActivSpecialisationDelegateCtr(treeSpecDelegate);
	}

	public String idWindow() {
		return (String) valueForBinding("idWindow");
	}

	public String typeSemestre = EOActivite.TYPE_LIEN_SEMESTRE;
	public String typeParcours = EOActivite.TYPE_LIEN_PARCOURS;
	public String typeSpecialisation = EOActivite.TYPE_LIEN_SPECIALITE;
	public String typeUe = EOActivite.TYPE_LIEN_UE;
	public String typeEc = EOActivite.TYPE_LIEN_EC;
	public String typeDomaine = EOActivite.TYPE_LIEN_DOMAINE;
	public String typeDiplome = EOActivite.TYPE_LIEN_DIPLOME;
	private NSArray<EOScolFormationDomaine> lstDomaines;
	private EOScolFormationDomaine domaineOccur;

	/**
	 * @return the lstDomaines
	 */
	public NSArray<EOScolFormationDomaine> lstDomaines() {

		if (lstDomaines == null) {
			Session sess = (Session) session();
			NSMutableArray<EOScolFormationDomaine> retour = new NSMutableArray<EOScolFormationDomaine>();
			if (sess.getGUser().haveAdminRight()) {

				@SuppressWarnings("unchecked")
				NSArray<EOScolFormationDomaine> tmp = FinderScolFormationDomaine
						.getScolFormationDomainesValidesFiltre(editedActivite()
								.editingContext(), "*");

				NSArray<EOScolFormationDiplome> dipl = null;

				for (EOScolFormationDomaine domOccur : tmp) {
					dipl = FinderScolFormationDiplome
							.getDiplomesForDomaineAndYear(
									editedActivite().editingContext(),
									domOccur,
									editedActivite()
											.toFwkScolarite_ScolFormationAnnee());
					if ((dipl != null) && (dipl.size() > 0))
						retour.addObject(domOccur);
				}
			} else {
				if (sess.getGUser().isGrilleurSpec(sess.selectedYear())) {
					for (EOScolFormationResponsabilite resp : sess.getGUser()
							.scolFormationResponsabilitesGrilleurForYear(
									sess.selectedYear())) {
						if (!retour.contains(resp
								.toFwkScolarite_ScolFormationSpecialisation()
								.toFwkScolarite_ScolFormationDiplome()
								.toFwkScolarite_ScolFormationDomaine())) {
							retour.add(resp
									.toFwkScolarite_ScolFormationSpecialisation()
									.toFwkScolarite_ScolFormationDiplome()
									.toFwkScolarite_ScolFormationDomaine());
						}
					}
				}
			}

			lstDomaines =  retour;
		}

		return lstDomaines;
	}

	/**
	 * @param lstDomaines
	 *            the lstDomaines to set
	 */
	public void setLstDomaines(NSArray<EOScolFormationDomaine> lstDomaines) {
		this.lstDomaines = lstDomaines;
	}

	/**
	 * @return the domaineOccur
	 */
	public EOScolFormationDomaine domaineOccur() {
		return domaineOccur;
	}

	/**
	 * @param domaineOccur
	 *            the domaineOccur to set
	 */
	public void setDomaineOccur(EOScolFormationDomaine domaineOccur) {
		this.domaineOccur = domaineOccur;
	}

	public String cawsearchzoneid() {
		return getComponentId() + "_cawsearchzoneid";
	}

	public WOActionResults typeLienChange() {
		AjaxUpdateContainer.updateContainerWithID("selectionlink", context());
		return null;
	}

	public WOActionResults openSearchMaquette() {
		// CktlAjaxWindow.open(context(), cawsearchzoneid());
		AjaxModalDialog.open(context(), cawsearchzoneid());
		return null;
	}

	public WOActionResults closeSearchWin() {
		// CktlAjaxWindow.close(context(), cawsearchzoneid());
		AjaxModalDialog.close(context());
		return null;
	}

	private EOGenericRecord editedObj;

	/**
	 * @return the editedObj
	 */
	public EOGenericRecord editedObj() {
		return editedObj;
	}

	/**
	 * @param editedObj
	 *            the editedObj to set
	 */
	public void setEditedObj(EOGenericRecord editedObj) {
		this.editedObj = editedObj;
		if (isParSelect())
			editedActivite()
					.setToFwkScolarite_ScolMaquetteParcoursRelationship(
							(EOScolMaquetteParcours) editedObj);
		if (isSpecSelect())
			editedActivite()
					.setToFwkScolarite_ScolFormationSpecialisationRelationship(
							(EOScolFormationSpecialisation) editedObj);
		if (isSemSelect())
			editedActivite()
					.setToFwkScolarite_ScolMaquetteSemestreRelationship(
							(EOScolMaquetteSemestre) editedObj);
		if (isUeSelect())
			editedActivite().setToFwkScolarite_ScolMaquetteUeRelationship(
					(EOScolMaquetteUe) editedObj);
		if (isEcSelect())
			editedActivite().setToFwkScolarite_ScolMaquetteEcRelationship(
					(EOScolMaquetteEc) editedObj);
	}

	public EnseignementsTreeDelegate treeDelegate() {
		if (isParSelect())
			return treeParDelegate();
		if (isSpecSelect())
			return treeSpecDelegate();
		if (isSemSelect())
			return treeSemDelegate();
		if (isUeSelect())
			return treeUeDelegate();
		if (isEcSelect())
			return treeEcDelegate();
		return null;
	}

	public String windowTitle() {
		if (isParSelect())
			return "Selection d'un parcours";
		if (isSpecSelect())
			return "Selection d'une spécialisation";
		if (isSemSelect())
			return "Selection d'un semestre";
		if (isUeSelect())
			return "Selectction d'une U.E.";
		if (isEcSelect())
			return "Selection d'une E.C.";
		return null;
	}

	public boolean isSameLink() {
		return ((typeLien != null) && (editedActivite() != null) && (typeLien
				.equals(editedActivite().typeLien())));
	}

	public EOScolFormationDiplome getSelectedDiplome() {
		EOGenericRecord lien = editedActivite().lien();
		if ((lien != null) && (lien instanceof EOScolFormationDiplome)) {
			return (EOScolFormationDiplome) lien;
		}
		return null;
	}

	public void setSelectedDiplome(EOScolFormationDiplome selectedDiplome) {
		editedActivite().setLien(
				selectedDiplome.localInstanceIn(editedActivite()
						.editingContext()));
	}

	/**
	 * @return the diplomePickerDelegate
	 */
	public FilteredDiplomePickerDelegate diplomePickerDelegate() {
		return ((Session) session()).currentFilteredDiplomePickerDelegate();
	}

	/**
	 * @param diplomePickerDelegate
	 *            the diplomePickerDelegate to set
	 */
	public void setDiplomePickerDelegate(
			FilteredDiplomePickerDelegate diplomePickerDelegate) {
		((Session) session())
				.setCurrentFilteredDiplomePickerDelegate(diplomePickerDelegate);
	}
	
	/**
	 * @return the diplomePickerDelegate
	 */
	public FilteredDiplomePickerDelegate diplomePickerEditRightDelegate() {
		return ((Session) session()).currentFilteredDiplomePickerEditRightDelegate();
	}

	/**
	 * @param diplomePickerDelegate
	 *            the diplomePickerDelegate to set
	 */
	public void setDiplomePickerEditRightDelegate(
			FilteredDiplomePickerDelegate diplomePickerDelegate) {
		((Session) session())
				.setCurrentFilteredDiplomePickerEditRightDelegate(diplomePickerDelegate);
	}

	public boolean isGrilleurSpec() {
		Session sess = (Session) session();
		return sess.getGUser().haveAdminRight()
				|| sess.getGUser().isGrilleurSpec(sess.selectedYear());
	}

	public boolean canChooseParcours() {
		Session sess = (Session) session();
		return sess.getGUser().canChooseParcours(sess.selectedYear());
	}

	public boolean canChooseUe() {
		Session sess = (Session) session();
		return sess.getGUser().canChooseUe(sess.selectedYear());
	}

	public boolean canChooseEc() {
		Session sess = (Session) session();
		return sess.getGUser().canChooseEc(sess.selectedYear());
	}

}