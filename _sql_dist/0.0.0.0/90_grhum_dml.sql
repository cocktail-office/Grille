-- Saisir ici vos instructions dml (executables a partir de grhum ou d'un user dba)

--Creation de la responsabilité "Grilleur" si elle n'existe pas
INSERT INTO SCOLARITE.scol_constante_responsabilite(CRES_CODE,CRES_LIBELLE,CRES_PRIORITE)
(select 'G', 'Grilleur', 10 from dual where not exists (select * from SCOLARITE.scol_constante_responsabilite where CRES_CODE='G') );