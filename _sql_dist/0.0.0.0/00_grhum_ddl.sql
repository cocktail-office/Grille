-- Saisir ici vos instructions ddl (executables a partir de grhum ou d'un user dba)

--Séquences-------------------------------------------
CREATE SEQUENCE GRILLE_ENS.ACTE_PRESTATION_SEQ INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE "GRILLE_ENS"."ACTIVITE_SEQ" INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE "GRILLE_ENS"."EC_VERROUS_SEQ" INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE "GRILLE_ENS"."GRILLE_LISTE_SEQ" INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE GRILLE_ENS.PRESTATION_ENSEIGNANT_SEQ INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE GRILLE_ENS.REPORT_SEQ INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE "GRILLE_ENS"."SERVICE_ENSEIGNANT_SEQ" INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE "GRILLE_ENS"."TYPE_ACTIVITE_SEQ" INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;
CREATE SEQUENCE "GRILLE_ENS"."VOEUX_SEQ" INCREMENT BY 1 START WITH 1 MINVALUE 1 NOCACHE;

--Tables----------------------------------------------
CREATE TABLE "GRILLE_ENS"."ACTE_PRESTATION"
  (
    "ACTE_PREST_KEY" NUMBER NOT NULL ENABLE,
    "LIB_COURT"      VARCHAR2(20 BYTE) NOT NULL ENABLE,
    "LIB_LONG"       VARCHAR2(100 BYTE) NOT NULL ENABLE,
    "DATE_ANNULATION" DATE,
    CONSTRAINT "ACTE_PRESTATION_PK" PRIMARY KEY ("ACTE_PREST_KEY") ENABLE
  ) ;
    
  -----------------------------------------------------
  
CREATE TABLE "GRILLE_ENS"."TYPE_ACTIVITE"
  (
    "TYPE_ACTIVITE_KEY" NUMBER NOT NULL ENABLE,
    "LIB_LONG"          VARCHAR2(300 BYTE),
    "D_CREATION" DATE,
    "LIB_COURT" VARCHAR2(50 BYTE),
    CONSTRAINT "TYPE_ACTIVITE_PK" PRIMARY KEY ("TYPE_ACTIVITE_KEY") ENABLE
  ) ;
COMMENT ON TABLE "GRILLE_ENS"."TYPE_ACTIVITE"
IS
  'Type des activites';
  -----------------------------------------------------
  
  CREATE TABLE "GRILLE_ENS"."ACTIVITE"
  (
    "ACTIVITE_KEY"       NUMBER NOT NULL ENABLE,
    "NB_HEURES_ACTIVITE" NUMBER(3,0) NOT NULL ENABLE,
    "TYPE_ACTIVITE_KEY"  NUMBER NOT NULL ENABLE,
    "MEC_KEY"            NUMBER(6,0),
    "MUE_KEY"            NUMBER(6,0),
    "MSEM_KEY"           NUMBER(6,0),
    "MPAR_KEY"           NUMBER(6,0),
    "FSPN_KEY"           NUMBER(6,0),
    "FANN_KEY"           NUMBER(4,0),
    "COMMENTAIRE"        VARCHAR2(2000 BYTE),
    "FDOM_CODE"          VARCHAR2(5 BYTE),
    "FDIP_CODE"          VARCHAR2(7 BYTE),
    CONSTRAINT "ACTIVITE_PK" PRIMARY KEY ("ACTIVITE_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_FK_EC" FOREIGN KEY ("MEC_KEY") REFERENCES "SCOLARITE"."SCOL_MAQUETTE_EC" ("MEC_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_FK_PARC" FOREIGN KEY ("MPAR_KEY") REFERENCES "SCOLARITE"."SCOL_MAQUETTE_PARCOURS" ("MPAR_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_FK_SEMESTRE" FOREIGN KEY ("MSEM_KEY") REFERENCES "SCOLARITE"."SCOL_MAQUETTE_SEMESTRE" ("MSEM_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_FK_SPEC" FOREIGN KEY ("FSPN_KEY") REFERENCES "SCOLARITE"."SCOL_FORMATION_SPECIALISATION" ("FSPN_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_FK_TYPE" FOREIGN KEY ("TYPE_ACTIVITE_KEY") REFERENCES "GRILLE_ENS"."TYPE_ACTIVITE" ("TYPE_ACTIVITE_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_FK_UE" FOREIGN KEY ("MUE_KEY") REFERENCES "SCOLARITE"."SCOL_MAQUETTE_UE" ("MUE_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_FK" FOREIGN KEY ("FANN_KEY") REFERENCES "SCOLARITE"."SCOL_FORMATION_ANNEE" ("FANN_KEY") ENABLE,
    CONSTRAINT "ACTIVITE_DIPLOME_FK" FOREIGN KEY ("FDIP_CODE") REFERENCES "SCOLARITE"."SCOL_FORMATION_DIPLOME" ("FDIP_CODE") ENABLE,
    CONSTRAINT "ACTIVITE_DOMAINE_FK" FOREIGN KEY ("FDOM_CODE") REFERENCES "SCOLARITE"."SCOL_FORMATION_DOMAINE" ("FDOM_CODE") ENABLE
  ) ;
COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."ACTIVITE_KEY"
IS
  'PK activites';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."NB_HEURES_ACTIVITE"
IS
  'nb d''heures';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."TYPE_ACTIVITE_KEY"
IS
  'FK type_activite';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."MEC_KEY"
IS
  'FK EC si rattache a EC';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."MUE_KEY"
IS
  'FK UE si rattache a UE';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."MSEM_KEY"
IS
  'FK semestre si rattache a semestre';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."MPAR_KEY"
IS
  'FK parcours si rattache a un parcours';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."FSPN_KEY"
IS
  'FK specialite si rattache a specialite';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."FANN_KEY"
IS
  'FK scolFormationAnnee';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."COMMENTAIRE"
IS
  'commentaire';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."FDOM_CODE"
IS
  'Domaine';
  COMMENT ON COLUMN "GRILLE_ENS"."ACTIVITE"."FDIP_CODE"
IS
  'Diplome';
  COMMENT ON TABLE "GRILLE_ENS"."ACTIVITE"
IS
  'Table des activitees (charges hors CM,TD,TP)';
  
  
  -----------------------------------------------------
  
  CREATE TABLE "GRILLE_ENS"."EC_VERROUS"
  (
    "MEC_KEY"            NUMBER NOT NULL ENABLE,
    "VERROU"             VARCHAR2(1 BYTE) NOT NULL ENABLE,
    "NO_INDIVIDU_VERROU" NUMBER(8,0) NOT NULL ENABLE,
    "VERROU_KEY"         NUMBER NOT NULL ENABLE,
    "D_VERROU" DATE,
    CONSTRAINT "EC_VERROU_VERROUCK" CHECK (verrou IN ('O','N')) ENABLE,
    CONSTRAINT "EC_VERROUS_PK" PRIMARY KEY ("VERROU_KEY") ENABLE,
    CONSTRAINT "EC_VERROUS_SCOL_MAQUETTE__FK" FOREIGN KEY ("MEC_KEY") REFERENCES "SCOLARITE"."SCOL_MAQUETTE_EC" ("MEC_KEY") ENABLE,
    CONSTRAINT "EC_VERROUS_INDIVIDU_LOCK_FK" FOREIGN KEY ("NO_INDIVIDU_VERROU") REFERENCES "GRHUM"."INDIVIDU_ULR" ("NO_INDIVIDU") ENABLE
  ) ;
COMMENT ON COLUMN "GRILLE_ENS"."EC_VERROUS"."MEC_KEY"
IS
  'PK et FK scol_maquette_ec';
  COMMENT ON COLUMN "GRILLE_ENS"."EC_VERROUS"."VERROU"
IS
  'EC verouillé (voeux bloqués dans grille) ou non';
  
  -----------------------------------------------------
    
CREATE TABLE "GRILLE_ENS"."GRILLE_LISTE"
  (
    "GL_KEY"      NUMBER NOT NULL ENABLE,
    "FANN_KEY"    NUMBER NOT NULL ENABLE,
    "FSPN_KEY"    NUMBER NOT NULL ENABLE,
    "MPAR_KEY"    NUMBER NOT NULL ENABLE,
    "GL_CODE"     VARCHAR2(20 BYTE) NOT NULL ENABLE,
    "GL_LIBELLE"  VARCHAR2(200 BYTE) NOT NULL ENABLE,
    "NO_INDIVIDU" NUMBER NOT NULL ENABLE,
    CONSTRAINT "GRILLE_LISTE_PK" PRIMARY KEY ("GL_KEY") ENABLE,
    CONSTRAINT "GRILLE_LISTE_SCOL_FORMATI_FK1" FOREIGN KEY ("FSPN_KEY") REFERENCES "SCOLARITE"."SCOL_FORMATION_SPECIALISATION" ("FSPN_KEY") ENABLE,
    CONSTRAINT "GRILLE_LISTE_SCOL_FORMATI_FK2" FOREIGN KEY ("FANN_KEY") REFERENCES "SCOLARITE"."SCOL_FORMATION_ANNEE" ("FANN_KEY") ENABLE,
    CONSTRAINT "GRILLE_LISTE_SCOL_PARCOU_FK1" FOREIGN KEY ("MPAR_KEY") REFERENCES "SCOLARITE"."SCOL_MAQUETTE_PARCOURS" ("MPAR_KEY") ENABLE,
    CONSTRAINT "GRILLE_LISTE_INDIVIDU_ULR_FK1" FOREIGN KEY ("NO_INDIVIDU") REFERENCES "GRHUM"."INDIVIDU_ULR" ("NO_INDIVIDU") ENABLE
  ) ;
COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE"."GL_KEY"
IS
  'clé primaire de la liste';
  COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE"."FANN_KEY"
IS
  'année scolaire : voir scolarite.scol_formation_annee';
  COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE"."FSPN_KEY"
IS
  'clé primaire de la spécialisation qui donne le diplome le grade et le niveau';
  COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE"."MPAR_KEY"
IS
  'clé du parcours concerné';
  COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE"."GL_CODE"
IS
  'code de la liste';
  COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE"."GL_LIBELLE"
IS
  'libellé de la liste';

  -----------------------------------------------------
  
CREATE TABLE "GRILLE_ENS"."GRILLE_LISTE_EC"
  (
    "GL_KEY"  NUMBER NOT NULL ENABLE,
    "MEC_KEY" NUMBER NOT NULL ENABLE,
    "D_CREATION" DATE DEFAULT sysdate NOT NULL ENABLE,
    CONSTRAINT "GRILLE_LISTE_EC_PK" PRIMARY KEY ("GL_KEY", "MEC_KEY") ENABLE,
    CONSTRAINT "GRILLE_LISTE_EC_GRILLE_LI_FK1" FOREIGN KEY ("GL_KEY") REFERENCES "GRILLE_ENS"."GRILLE_LISTE" ("GL_KEY") ENABLE
  ) ;
COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE_EC"."GL_KEY"
IS
  'clé de la liste';
  COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE_EC"."MEC_KEY"
IS
  'clé de l''EC attaché ici';
  COMMENT ON COLUMN "GRILLE_ENS"."GRILLE_LISTE_EC"."D_CREATION"
IS
  'date de création de ce rattachement';
  
  -----------------------------------------------------
  
CREATE TABLE "GRILLE_ENS"."PRESTATION_ENSEIGNANT"
  (
    "PREST_ENS_KEY"  NUMBER NOT NULL ENABLE,
    "ACTE_PREST_KEY" NUMBER NOT NULL ENABLE,
    "C_RNE"          VARCHAR2(8 BYTE),
    "NB_HEURES_CM"   NUMBER,
    "NB_HEURES_TD"   NUMBER,
    "NB_HEURES_TP"   NUMBER,
    "COMMENTAIRE"    VARCHAR2(1000 BYTE),
    "NO_INDIVIDU"    NUMBER NOT NULL ENABLE,
    "FANN_KEY"       NUMBER NOT NULL ENABLE,
    "CON_ORDRE"      NUMBER,
    "DATE_ANNULATION" DATE,
    CONSTRAINT "TABLE1_PK" PRIMARY KEY ("PREST_ENS_KEY") ENABLE,
    CONSTRAINT "PRESTATION_ENSEIGNANT_ACT_FK1" FOREIGN KEY ("ACTE_PREST_KEY") REFERENCES "GRILLE_ENS"."ACTE_PRESTATION" ("ACTE_PREST_KEY") ENABLE,
    CONSTRAINT "PRESTATION_ENSEIGNANT_RNE_FK1" FOREIGN KEY ("C_RNE") REFERENCES "GRHUM"."RNE" ("C_RNE") ENABLE,
    CONSTRAINT "P_E_ANNEE_FK1" FOREIGN KEY ("FANN_KEY") REFERENCES "SCOLARITE"."SCOL_FORMATION_ANNEE" ("FANN_KEY") ENABLE
  ) ;
COMMENT ON COLUMN "GRILLE_ENS"."PRESTATION_ENSEIGNANT"."ACTE_PREST_KEY"
IS
  'cle etrangere vers l''acte de prestation (code)';
  COMMENT ON COLUMN "GRILLE_ENS"."PRESTATION_ENSEIGNANT"."C_RNE"
IS
  'code rne de l''etablissement ou est effectuee la prestation';
  COMMENT ON COLUMN "GRILLE_ENS"."PRESTATION_ENSEIGNANT"."COMMENTAIRE"
IS
  'un commentaire libre';
  
  -----------------------------------------------------
  
CREATE TABLE "GRILLE_ENS"."REPORT"
  (
    "REPORT_KEY"         NUMBER NOT NULL ENABLE,
    "FANN_KEY_REPORT"    NUMBER NOT NULL ENABLE,
    "NB_HEURES"          NUMBER NOT NULL ENABLE,
    "COMMENTAIRE_REPORT" VARCHAR2(1000 BYTE),
    "NO_INDIVIDU_ENS"    NUMBER NOT NULL ENABLE,
    "DATE_CREATION" DATE NOT NULL ENABLE,
    CONSTRAINT "REPORT_PK" PRIMARY KEY ("REPORT_KEY") ENABLE,
    CONSTRAINT "REPORT_INDIVIDU_ULR_FK1" FOREIGN KEY ("NO_INDIVIDU_ENS") REFERENCES "GRHUM"."INDIVIDU_ULR" ("NO_INDIVIDU") ENABLE
  ) ;
COMMENT ON COLUMN "GRILLE_ENS"."REPORT"."REPORT_KEY"
IS
  'clé primaire du report';
  COMMENT ON COLUMN "GRILLE_ENS"."REPORT"."FANN_KEY_REPORT"
IS
  'annee universitaire vers laquelle est fait le report';
  COMMENT ON COLUMN "GRILLE_ENS"."REPORT"."NB_HEURES"
IS
  'nombre d''heures reportées';
  COMMENT ON COLUMN "GRILLE_ENS"."REPORT"."COMMENTAIRE_REPORT"
IS
  'commentaire sur le report';
  COMMENT ON COLUMN "GRILLE_ENS"."REPORT"."NO_INDIVIDU_ENS"
IS
  'no_individu de l''enseignant';
  
  -----------------------------------------------------
  
CREATE TABLE "GRILLE_ENS"."SERVICE_ENSEIGNANT"
  (
    "FANN_KEY"               NUMBER(4,0) NOT NULL ENABLE,
    "NO_INDIVIDU"            NUMBER(8,0) NOT NULL ENABLE,
    "NO_INDIVIDU_VALIDATEUR" NUMBER(8,0),
    "D_VALID" DATE,
    "SERVICE_KEY" NUMBER NOT NULL ENABLE,
    CONSTRAINT "SERVICE_ENSEIGNANT_PK" PRIMARY KEY ("SERVICE_KEY") ENABLE,
    CONSTRAINT "SERVICE_ENSEIGNANT_INDIVI_FK1" FOREIGN KEY ("NO_INDIVIDU") REFERENCES "GRHUM"."INDIVIDU_ULR" ("NO_INDIVIDU") ENABLE,
    CONSTRAINT "SERVICE_ENSEIGNANT_SCOL_F_FK1" FOREIGN KEY ("FANN_KEY") REFERENCES "SCOLARITE"."SCOL_FORMATION_ANNEE" ("FANN_KEY") ENABLE,
    CONSTRAINT "SERVICE_ENSEIGNANT_INDI_VAL_FK" FOREIGN KEY ("NO_INDIVIDU_VALIDATEUR") REFERENCES "GRHUM"."INDIVIDU_ULR" ("NO_INDIVIDU") ENABLE
  ) ;

  
  -----------------------------------------------------
  CREATE TABLE "GRILLE_ENS"."VOEUX"
  (
    "MAP_KEY"              NUMBER(6,0),
    "NO_INDIVIDU"          NUMBER(8,0) NOT NULL ENABLE,
    "NB_HEURES_VOEUX"      NUMBER(6,2),
    "NO_INDIVIDU_CREATEUR" NUMBER(8,0),
    "D_CREATION" DATE,
    "ACTIVITE_KEY"     NUMBER,
    "VALIDE"           VARCHAR2(1 BYTE),
    "NB_HEURE_REALISE" NUMBER(6,2),
    "VOEUX_KEY"        NUMBER NOT NULL ENABLE,
    "FANN_KEY"         NUMBER(4,0),
    CONSTRAINT "VOEUX_PK" PRIMARY KEY ("VOEUX_KEY") ENABLE,
    CONSTRAINT "VOEUX_FK_ACTIVITE" FOREIGN KEY ("ACTIVITE_KEY") REFERENCES "GRILLE_ENS"."ACTIVITE" ("ACTIVITE_KEY") ENABLE,
    CONSTRAINT "VOEUX_FK_AP" FOREIGN KEY ("MAP_KEY") REFERENCES "SCOLARITE"."SCOL_MAQUETTE_AP" ("MAP_KEY") ENABLE,
    CONSTRAINT "VOEUX_FK_CREATEUR" FOREIGN KEY ("NO_INDIVIDU_CREATEUR") REFERENCES "GRHUM"."INDIVIDU_ULR" ("NO_INDIVIDU") ENABLE,
    CONSTRAINT "VOEUX_FK_ENSEIGNANT" FOREIGN KEY ("NO_INDIVIDU") REFERENCES "GRHUM"."INDIVIDU_ULR" ("NO_INDIVIDU") ENABLE,
    CONSTRAINT "VOEUX_FK_ANNEE" FOREIGN KEY ("FANN_KEY") REFERENCES "SCOLARITE"."SCOL_FORMATION_ANNEE" ("FANN_KEY") ENABLE
  ) ;
COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."MAP_KEY"
IS
  'FK AP si voeux sur AP';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."NO_INDIVIDU"
IS
  'FK individu enseignant concerne par le voeux';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."NB_HEURES_VOEUX"
IS
  'nb Heures du voeux';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."NO_INDIVIDU_CREATEUR"
IS
  'FK individu createur du voeux (enseignant ou grilleur)';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."ACTIVITE_KEY"
IS
  'FK activite si voeux sur activite';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."VALIDE"
IS
  'indic validite si voeux valide';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."NB_HEURE_REALISE"
IS
  'nb heures effectivement realisees';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."VOEUX_KEY"
IS
  'PK voeux';
  COMMENT ON COLUMN "GRILLE_ENS"."VOEUX"."FANN_KEY"
IS
  'FK scolFormationAnnee';
  COMMENT ON TABLE "GRILLE_ENS"."VOEUX"
IS
  'Voeux sur action (AP) ou activite';
  
/  

--Vues-------------------------------------------------------------------
  
 CREATE OR REPLACE VIEW "GRILLE_ENS"."V_CORPS_ENSEIGNANTS" ("FANN_KEY", "NO_INDIVIDU", "NOM_AFFICHAGE", "PRENOM_AFFICHAGE", "C_CORPS", "LC_CORPS", "LL_CORPS", "POTENTIEL_BRUT", "DEBUT_CORPS", "FIN_CORPS", "DEBUT_ANNEE", "FIN_ANNEE", "NB_JOURS_ANNEE")
AS
  SELECT DISTINCT ann.fann_key,
    i.no_individu,
    i.nom_affichage,
    i.prenom_affichage,
    corps.c_corps,
    corps.lc_corps,
    corps.ll_corps,
    corps.potentiel_brut,
    ec.d_effet_element debut_corps,
    ec.d_fin_element fin_corps,
    greatest(ec.d_effet_element, to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')) debut_annee,
    least(NVL(ec.d_fin_element,to_date('01/01/2099','DD/MM/YYYY')),to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')) fin_annee,
    least(NVL(ec.d_fin_element,to_date('01/01/2099','DD/MM/YYYY')),to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')) - greatest(ec.d_effet_element, to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')) nb_jours_annee
  FROM GRHUM.v_personnel_actuel_ens ens,
    GRHUM.individu_ulr i,
    GRHUM.corps corps,
    MANGUE.element_carriere ec,
    SCOLARITE.scol_formation_annee ann
  WHERE i.no_individu     =ens.no_dossier_pers
  AND ec.no_dossier_pers  = i.no_individu
  AND ec.c_corps          =corps.c_corps
  AND ec.d_effet_element <= to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')
  AND NVL(ec.d_fin_element,to_date('01/01/2099','DD/MM/YYYY')) >= to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')
    --and sysdate between ec.d_effet_element and nvl(ec.d_fin_element,sysdate+1)
    /*AND ec.d_effet_element =
    (SELECT MAX(d_effet_element)
    FROM MANGUE.element_carriere
    WHERE no_dossier_pers = i.no_individu --and c_corps=ec.c_corps
    )*/
    --and ann.fann_key=2010
  ORDER BY i.no_individu,
    ec.d_effet_element,
    corps.c_corps;
 /   
   -----------------------------------------------------
   
 CREATE OR REPLACE VIEW "GRILLE_ENS"."V_QUOTITE_ENSEIGNANTS" ("FANN_KEY", "NO_INDIVIDU", "NOM_AFFICHAGE", "PRENOM_AFFICHAGE", "C_STRUCTURE", "LL_STRUCTURE", "DEN_QUOT_AFFECTATION", "D_DEB_AFFECTATION", "D_FIN_AFFECTATION", "DEBUT_ANNEE", "FIN_ANNEE", "NB_JOURS_ANNEE")
AS
  SELECT DISTINCT ann.fann_key,
    i.no_individu,
    i.nom_affichage,
    i.prenom_affichage,
    s.c_structure,
    s.ll_structure,
    aff.den_quot_affectation,
    aff.d_deb_affectation,
    aff.d_fin_affectation,
    greatest(aff.d_deb_affectation, to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')) debut_annee,
    least(NVL(aff.d_fin_affectation,to_date('01/01/2099','DD/MM/YYYY')),to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')) fin_annee,
    least(NVL(aff.d_fin_affectation,to_date('01/01/2099','DD/MM/YYYY')),to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY'))  - greatest(aff.d_deb_affectation, to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY'))+1 nb_jours_annee
  FROM GRHUM.v_personnel_actuel_ens ens,
    GRHUM.individu_ulr i,
    MANGUE.affectation aff,
    grhum.structure_ulr s,
    SCOLARITE.scol_formation_annee ann
  WHERE i.no_individu        =ens.no_dossier_pers
  AND aff.no_dossier_pers    = i.no_individu
  AND s.c_structure          = aff.c_structure
  AND aff.d_deb_affectation <= to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')
  AND NVL(aff.d_fin_affectation,to_date('01/01/2099','DD/MM/YYYY')) >= to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')
  ORDER BY i.no_individu,
    aff.d_deb_affectation;
/
 --------------------------------------------------------------------
 
 CREATE OR REPLACE VIEW "GRILLE_ENS"."V_CORPS_ENSEIGNANTS_SERVICE" ("FANN_KEY", "NO_INDIVIDU", "NOM_AFFICHAGE", "PRENOM_AFFICHAGE", "C_CORPS", "LC_CORPS", "LL_CORPS", "C_STRUCTURE", "LL_STRUCTURE")
AS
  SELECT DISTINCT corps."FANN_KEY",
    corps."NO_INDIVIDU",
    corps."NOM_AFFICHAGE",
    corps."PRENOM_AFFICHAGE",
    corps."C_CORPS",
    corps."LC_CORPS",
    corps."LL_CORPS",
    s.c_structure,
    s.ll_structure
  FROM GRILLE_ENS.v_corps_enseignants corps,
    GRILLE_ENS.v_quotite_enseignants s
  WHERE corps.no_individu = s.no_individu
  AND corps.fann_key      =s.fann_key;
 / 
------------------------------------------------------------------

 CREATE OR REPLACE VIEW "GRILLE_ENS"."V_DISTINCT_CORPS_ANNEE" ("LC_CORPS", "C_CORPS", "FANN_KEY")
AS
  SELECT DISTINCT c.lc_corps,
    c.c_corps,
    c.fann_key
  FROM GRILLE_ENS.v_corps_enseignants c;
 / 
    -----------------------------------------------------
    
CREATE OR REPLACE VIEW "GRILLE_ENS"."V_DISTINCT_SERVICE_ANNEE" ("C_STRUCTURE", "LL_STRUCTURE", "FANN_KEY")
AS
  SELECT DISTINCT q.c_structure,
    q.ll_structure,
    q.fann_key
  FROM GRILLE_ENS.v_quotite_enseignants q;
 / 
  -----------------------------------------------------

CREATE OR REPLACE VIEW "GRILLE_ENS"."V_POSITION_ENSEIGNANTS" ("FANN_KEY", "NO_INDIVIDU", "NOM_AFFICHAGE", "PRENOM_AFFICHAGE", "C_POSITION", "LC_POSITION", "TEM_ACTIVITE", "D_DEB_POSITION", "D_FIN_POSITION", "DEBUT_ANNEE", "FIN_ANNEE", "NB_JOURS_ANNEE")
AS
  SELECT DISTINCT ann.fann_key,
    i.no_individu,
    i.nom_affichage,
    i.prenom_affichage,
    pos.c_position,
    pos.lc_position,
    pos.tem_activite,
    cp.d_deb_position,
    cp.d_fin_position,
    greatest(cp.d_deb_position, to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')) debut_annee,
    least(NVL(cp.d_fin_position,to_date('01/01/2099','DD/MM/YYYY')),to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')) fin_annee,
    least(NVL(cp.d_fin_position,to_date('01/01/2099','DD/MM/YYYY')),to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')) - greatest(cp.d_deb_position, to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')) nb_jours_annee
  FROM GRHUM.v_personnel_actuel_ens ens,
    GRHUM.individu_ulr i,
    MANGUE.changement_position cp,
    grhum.position pos,
    SCOLARITE.scol_formation_annee ann
  WHERE i.no_individu    =ens.no_dossier_pers
  AND cp.no_dossier_pers = i.no_individu
  AND pos.c_position     = cp.c_position
  AND cp.d_deb_position <= to_date('31/08/'
    ||ann.fann_fin,'DD/MM/YYYY')
  AND NVL(cp.d_fin_position,to_date('01/01/2099','DD/MM/YYYY')) >= to_date('01/09/'
    ||ann.fann_debut,'DD/MM/YYYY')
    --AND ann.fann_key=2010
  ORDER BY i.no_individu,
    cp.d_deb_position;
  /  

  --Fonctions--------------------------------------------------------------
  --fonctions essentiellmeent utilisées dans les vues
  
  
  create or replace
FUNCTION GRILLE_ENS.F_cal_equTd_For_AP(
      Nb_Hr     NUMBER,
      ap SCOLARITE.scol_maquette_ap.map_key%type )
    RETURN NUMBER
  IS
    -- --------------------------------------
    -- Projet : GRILLE
    -- Auteur :  jl Matas
    -- Date :  25/01/11
    -- Modif :
    -- fonction de calcul des heures equivelent TD pour un AP
    -- Nb_Hr : nombre d'heures (CM ou TP) a convertir en equivalent TD
    -- ap : ap concerné
    -- retour du nombre d'heures multiplie par le coeeficient sinon retour du nombre d'heure sans modification
    -- --------------------------------------
    -- ------------
    -- DECLARATIONS
    -- ------------
    nb     NUMBER;
    hcode SCOLARITE.scol_maquette_horaire_code%rowtype;
    -- ---------
    -- PRINCIPAL
    -- ---------
  BEGIN
  
  IF (ap IS NULL) THEN
      RETURN null;
    END IF;
    
    IF (nb_hr IS NULL) THEN
      RETURN null;
    END IF;
    
    --recherche de horaire code
    SELECT COUNT(*)
    INTO nb
    FROM SCOLARITE.scol_maquette_horaire_code mho,SCOLARITE.scol_maquette_ap ap
    WHERE mho.mhco_code=ap.mhco_code;
    
    IF (nb           <=0) THEN
      RETURN nb_hr;
    END IF;
    
   SELECT mho.*
    INTO hcode
    FROM SCOLARITE.scol_maquette_horaire_code mho,SCOLARITE.scol_maquette_ap ap
    WHERE mho.mhco_code=ap.mhco_code 
    AND ap.map_key = ap;
    
    RETURN ( ROUND(Nb_Hr*hcode.MHCO_NUMERATEUR/hcode.MHCO_DENOMINATEUR ,2));
    
  END; -- PRINCIPAL
 / 
 --------------------------------------------------------------------  
  
create or replace
FUNCTION GRILLE_ENS.F_potentiel_brut_For_ens(
      ann     NUMBER,
      no_ens GRILLE_ENS.v_quotite_enseignants.no_individu%type )
    RETURN NUMBER
  IS
    -- --------------------------------------
    -- Projet : GRILLE
    -- Auteur :  jl Matas
    -- Date :  25/01/11
    -- Modif :
    -- fonction de calcul du potentiel brut d'un enseignant
    -- ann : année
    -- no_ens : no_individu de l'enseignant
    -- retour potentiel
    -- --------------------------------------
    -- ------------
    -- DECLARATIONS
    -- ------------
    nb     NUMBER;
    cursor corps is 
    select * from GRILLE_ENS.v_corps_enseignants v
    where v.fann_key=ann and v.no_individu = no_ens;
    -- ---------
    -- PRINCIPAL
    -- ---------
  BEGIN
  
  IF (ann IS NULL) THEN
      RETURN null;
    END IF;
    
    IF (no_ens IS NULL) THEN
      RETURN null;
    END IF;
    nb:=0;
    for curCorps in corps loop
      nb := nb + (curCorps.POTENTIEL_BRUT*curCorps.NB_JOURS_ANNEE/to_number(to_char(to_date('31/12/'||ann,'DD/MM/YYYY'),'DDD')));
    end loop;        
    
    RETURN ( CEIL(nb ));
    
  END; -- PRINCIPAL
 / 
 --------------------------------------------------------------------
 
create or replace
FUNCTION  GRILLE_ENS.F_quotite_For_ens(
      ann     NUMBER,
      no_ens GRILLE_ENS.v_quotite_enseignants.no_individu%type )
    RETURN NUMBER
  IS
    -- --------------------------------------
    -- Projet : GRILLE
    -- Auteur :  jl Matas
    -- Date :  25/01/11
    -- Modif :
    -- fonction de calcul la quotité d'un enseignant
    -- ann : année
    -- no_ens : no_individu de l'enseignant
    -- retour la quotité
    -- --------------------------------------
    -- ------------
    -- DECLARATIONS
    -- ------------
    nb     NUMBER;
    cursor qtt is 
    select * from GRILLE_ENS.v_quotite_enseignants v
    where v.fann_key=ann and v.no_individu = no_ens;
    -- ---------
    -- PRINCIPAL
    -- ---------
  BEGIN
  
  IF (ann IS NULL) THEN
      RETURN null;
    END IF;
    
    IF (no_ens IS NULL) THEN
      RETURN null;
    END IF;
    nb:=0;
    for curQtt in qtt loop
      nb := nb + (curQtt.NB_JOURS_ANNEE * curQtt.DEN_QUOT_AFFECTATION/100);
    end loop;        
    
    RETURN ( ROUND((nb*100)/to_number(to_char(to_date('31/12/'||ann,'DD/MM/YYYY'),'DDD')) ,0));
    
  END; -- PRINCIPAL
  /
  --------------------------------------------------------------------
  
 create or replace
FUNCTION  GRILLE_ENS.F_total_decharges_For_ens(
      ann     NUMBER,
      no_ens GRILLE_ENS.v_quotite_enseignants.no_individu%type )
    RETURN NUMBER
  IS
    -- --------------------------------------
    -- Projet : GRILLE
    -- Auteur :  jl Matas
    -- Date :  25/01/11
    -- Modif :
    -- fonction de calcul du total des décharges d'un enseignant
    -- ann : année
    -- no_ens : no_individu de l'enseignant
    -- 
    -- --------------------------------------
    -- ------------
    -- DECLARATIONS
    -- ------------
    nb     NUMBER;

    cursor decharges is 
    select nvl(v.nb_h_decharge,0) nb
    from MANGUE.DECHARGE v
    where v.PERIODE_DECHARGE like ann||'%' and v.NO_DOSSIER_PERS = no_ens
    ;
    -- ---------
    -- PRINCIPAL
    -- ---------
  BEGIN
  
  IF (ann IS NULL) THEN
      RETURN null;
    END IF;
    
    IF (no_ens IS NULL) THEN
      RETURN null;
    END IF;    
    
    nb:=0;
    for curDech in decharges loop
      nb := nb + (curdech.nb);
    end loop;        
    
    RETURN ( CEIL(nb));
    
  END; -- PRINCIPAL
 / 
   --------------------------------------------------------------------
   
create or replace
FUNCTION  GRILLE_ENS.F_total_prestation_For_ens(
      ann     NUMBER,
      no_ens GRILLE_ENS.v_quotite_enseignants.no_individu%type )
    RETURN NUMBER
  IS
    -- --------------------------------------
    -- Projet : GRILLE
    -- Auteur :  jl Matas
    -- Date :  25/01/11
    -- Modif :
    -- fonction de calcul du total des prestations d'un enseignant
    -- ann : année
    -- no_ens : no_individu de l'enseignant
    -- 
    -- --------------------------------------
    -- ------------
    -- DECLARATIONS
    -- ------------
    nb     NUMBER;
    mhocCM SCOLARITE.scol_maquette_horaire_code%rowtype;
    mhocTD SCOLARITE.scol_maquette_horaire_code%rowtype;
    mhocTP SCOLARITE.scol_maquette_horaire_code%rowtype;
    cursor prest is 
    select nvl(v.nb_heures_cm,0) cm, nvl(v.nb_heures_td,0) td,nvl(v.nb_heures_tp,0) tp from GRILLE_ENS.PRESTATION_ENSEIGNANT v
    where v.fann_key=ann and v.no_individu = no_ens
    and v.DATE_ANNULATION is null;
    -- ---------
    -- PRINCIPAL
    -- ---------
  BEGIN
  
  IF (ann IS NULL) THEN
      RETURN null;
    END IF;
    
    IF (no_ens IS NULL) THEN
      RETURN null;
    END IF;    
    select * into mhocCM from SCOLARITE.scol_maquette_horaire_code 
      where MHCO_VALIDITE='O' 
      and mhco_code = 'CM'
      and rownum=1;
      
      select * into mhocTD from SCOLARITE.scol_maquette_horaire_code 
      where MHCO_VALIDITE='O' 
      and mhco_code = 'TD'
      and rownum=1;
      
      select * into mhocTP from SCOLARITE.scol_maquette_horaire_code 
      where MHCO_VALIDITE='O' 
      and mhco_code = 'TP'
      and rownum=1;
    nb:=0;
    for curPrest in prest loop
      nb := nb + (curPrest.cm*mhoccm.MHCO_NUMERATEUR/mhoccm.MHCO_DENOMINATEUR);
      nb := nb + (curPrest.td*mhoctd.MHCO_NUMERATEUR/mhoctd.MHCO_DENOMINATEUR);
      nb := nb + (curPrest.tp*mhoctp.MHCO_NUMERATEUR/mhoctp.MHCO_DENOMINATEUR);
    end loop;        
    
    RETURN ( ROUND(nb ,2));
    
  END; -- PRINCIPAL
  /
 --------------------------------------------------------------------
 
 create or replace
FUNCTION  GRILLE_ENS.F_total_reports_For_ens(
      ann     NUMBER,
      no_ens GRILLE_ENS.v_quotite_enseignants.no_individu%type )
    RETURN NUMBER
  IS
    -- --------------------------------------
    -- Projet : GRILLE
    -- Auteur :  jl Matas
    -- Date :  25/01/11
    -- Modif :
    -- fonction de calcul du total des reports d'un enseignant
    -- ann : année
    -- no_ens : no_individu de l'enseignant
    -- 
    -- --------------------------------------
    -- ------------
    -- DECLARATIONS
    -- ------------
    nb     NUMBER;

    cursor reports is 
    select nvl(v.nb_heures,0) nb
    from GRILLE_ENS.REPORT v
    where v.FANN_KEY_REPORT= ann and v.NO_INDIVIDU_ENS = no_ens
    ;
    -- ---------
    -- PRINCIPAL
    -- ---------
  BEGIN
  
  IF (ann IS NULL) THEN
      RETURN null;
    END IF;
    
    IF (no_ens IS NULL) THEN
      RETURN null;
    END IF;    
    
    nb:=0;
    for curRep in reports loop
      nb := nb + (curRep.nb);
    end loop;        
    
    RETURN ( CEIL(nb));
    
  END; -- PRINCIPAL          
  /
  
 


