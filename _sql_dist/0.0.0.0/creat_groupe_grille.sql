--Connecte GRILLE_ENS
CREATE OR REPLACE PROCEDURE grille_ens.creat_groupe_grille(cStructureBase grhum.structure_ulr.c_structure%TYPE) IS
  /*--------------------Created on 13/03/2009 by JLMATAS-----------------------
  ---                                                                        --
  --- Creation des groupes necessaires a l'appli grille
  --- le cStructureBase est le cStructure du groupe pere des groupes
  ----------------------------------------------------------------------------*/

  cStructGrille      grhum.structure_ulr.c_structure%TYPE;
  cStructGrilleAdmin grhum.structure_ulr.c_structure%TYPE;
  cStructGrilleur    grhum.structure_ulr.c_structure%TYPE;
  pid                grhum.individu_ulr.pers_id%TYPE;
  nb                 NUMBER;

BEGIN
  --groupe grille dans autres groupes et structures
  select count(*)
    into nb
    from grhum.structure_ulr s
   where s.lc_structure = 'GRILLE';
  IF (nb <= 0) THEN
    grhum.Ins_Groupe(cStructGrille,
                     pid,
                     'G',
                     'GRILLE',
                     'GRILLE',
                     cStructureBase,
                     null,
                     null,
                     null,
                     null,
                     null,
                     '+',
                     null,
                     null,
                     null,
                     null,
                     null,
                     null);
  ELSE
    select s.c_structure
      into cStructGrille
      from grhum.structure_ulr s
     where s.lc_structure = 'GRILLE'
       and rownum = 1;
  END IF;

  --groupe admin 
  select count(*)
    into nb
    from grhum.structure_ulr s
   where s.lc_structure = 'ADMINGRILLE';
  IF (nb <= 0) THEN
    grhum.Ins_Groupe(cStructGrilleAdmin,
                     pid,
                     'G',
                     'ADMINISTRATEURS GRILLE',
                     'ADMINGRILLE',
                     cStructGrille,
                     null,
                     null,
                     null,
                     null,
                     null,
                     '+',
                     null,
                     null,
                     null,
                     null,
                     null,
                     null);
  ELSE
    select s.c_structure
      into cStructGrilleAdmin
      from grhum.structure_ulr s
     where s.lc_structure = 'ADMINGRILLE'
       and rownum = 1;
  END IF;
  COMMIT;
END;
/
