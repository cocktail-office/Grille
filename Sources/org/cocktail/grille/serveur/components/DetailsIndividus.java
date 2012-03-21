package org.cocktail.grille.serveur.components;

import org.cocktail.fwkcktlpersonne.common.metier.EOCompte;
import org.cocktail.fwkcktlpersonne.common.metier.EOPersonneTelephone;
import org.cocktail.fwkcktlpersonne.common.metier.EORepartStructure;
import org.cocktail.fwkcktlpersonne.common.metier.EOTypeTel;
import org.cocktail.fwkcktlpersonne.common.metier.IPersonne;
import org.cocktail.grillefwk.serveur.finder.FinderGrade;
import org.cocktail.grillefwk.serveur.metier.eof.EOGrade;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

public class DetailsIndividus extends WOComponent {

	private IPersonne iPersonne;
	private EORepartStructure consultedEtudRepartStrucOccur;
	private String classRowStr = "";
	private String consultedIndivEmailPro;
	private EORepartStructure consultedIndivRepartStrucOccur;
	private EOPersonneTelephone consultedIndivTelOccur;
	private NSArray<EOGrade> gradesConsultedIndiv;
	private EOGrade gradeOccur;

	public DetailsIndividus(WOContext context) {
		super(context);
	}

	/**
	 * @return the consultedEtud
	 */
	public IPersonne consultedIndiv() {
		return (IPersonne) valueForBinding("consultedIndiv");
	}

	/**
	 * @return the consultedIndivEmailPro
	 */
	public String consultedIndivEmailPro() {
		EOCompte cpt = EOCompte.compteForPersId(session()
				.defaultEditingContext(), "P", consultedIndiv().persId());

		return (cpt != null) ? cpt.toCompteEmail().getEmailFormatte() : "";

	}

	/**
	 * @return the mailToConsultedIndivEmailPro
	 */
	public String mailToConsultedIndivEmailPro() {
		return "mailto:" + consultedIndivEmailPro();
	}

	public Integer getCurrentYear() {
		return (Integer) valueForBinding("currentYear");
	}

	/**
	 * @return the consultedEtudRepartStrucOccur
	 */
	public EORepartStructure consultedEtudRepartStrucOccur() {
		return consultedEtudRepartStrucOccur;
	}

	/**
	 * @param consultedEtudRepartStrucOccur
	 *            the consultedEtudRepartStrucOccur to set
	 */
	public void setConsultedEtudRepartStrucOccur(
			EORepartStructure consultedEtudRepartStrucOccur) {
		this.consultedEtudRepartStrucOccur = consultedEtudRepartStrucOccur;
	}

	/**
	 * @return the classRowStr
	 */
	public String classRowStr() {
		classRowStr = ((classRowStr.equals("row1")) ? "row2" : "row1");
		return classRowStr;
	}

	public EOEditingContext getEc() {
		return (EOEditingContext) valueForBinding("ec");
	}

	@Override
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	/**
	 * @return the consultedIndivRepartStrucOccur
	 */
	public EORepartStructure consultedIndivRepartStrucOccur() {
		return consultedIndivRepartStrucOccur;
	}

	/**
	 * @param consultedIndivRepartStrucOccur
	 *            the consultedIndivRepartStrucOccur to set
	 */
	public void setConsultedIndivRepartStrucOccur(
			EORepartStructure consultedIndivRepartStrucOccur) {
		this.consultedIndivRepartStrucOccur = consultedIndivRepartStrucOccur;
	}

	/**
	 * @return the consultedIndivLstTel
	 */
	@SuppressWarnings("unchecked")
	public NSArray<EOPersonneTelephone> consultedIndivLstTel() {
		return EOPersonneTelephone.fetchAll(session().defaultEditingContext(),
				EOQualifier.qualifierWithQualifierFormat(
						EOPersonneTelephone.TO_INDIVIDUS_KEY + " = %@ AND "
								+ EOPersonneTelephone.LISTE_ROUGE_KEY
								+ " <> 'O' AND "
								+ EOPersonneTelephone.TO_TYPE_TEL_KEY + "."
								+ EOTypeTel.C_TYPE_TEL_KEY + " = '"
								+ EOTypeTel.C_TYPE_TEL_PRF + "'",
						new NSArray<IPersonne>(consultedIndiv())), null);

	}

	/**
	 * @return the consultedIndivTelOccur
	 */
	public EOPersonneTelephone consultedIndivTelOccur() {
		return consultedIndivTelOccur;
	}

	/**
	 * @param consultedIndivTelOccur
	 *            the consultedIndivTelOccur to set
	 */
	public void setConsultedIndivTelOccur(
			EOPersonneTelephone consultedIndivTelOccur) {
		this.consultedIndivTelOccur = consultedIndivTelOccur;
	}

	/**
	 * @return the gradesConsultedIndiv
	 */
	public NSArray<EOGrade> gradesConsultedIndiv() {
		return FinderGrade.getActiveGradesForPersonne(getEc(), consultedIndiv().persId());
	}

	/**
	 * @return the gradeOccur
	 */
	public EOGrade gradeOccur() {
		return gradeOccur;
	}

	/**
	 * @param gradeOccur the gradeOccur to set
	 */
	public void setGradeOccur(EOGrade gradeOccur) {
		this.gradeOccur = gradeOccur;
	}

}