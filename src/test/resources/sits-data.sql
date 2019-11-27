SET SCHEMA INTUIT;

INSERT INTO INS_MOD (MOD_CODE, MOD_NAME, MOD_IUSE) VALUES ('CS118-15', 'Programming for Computer Scientists', 'Y');
INSERT INTO INS_MOD (MOD_CODE, MOD_NAME, MOD_IUSE) VALUES ('CS261-15', 'Software Engineering', 'Y');
INSERT INTO INS_MOD (MOD_CODE, MOD_NAME, MOD_IUSE) VALUES ('CS343-30', 'Computer & Business Studies Project', 'Y');

INSERT INTO INS_LCA (LCA_CODE, LCA_NAME) VALUES ('U', 'University of Warwick');

INSERT INTO CAM_AST (AST_CODE, AST_NAME, AST_IUSE) VALUES ('ES', 'Examination - Summer', 'Y');
INSERT INTO CAM_AST (AST_CODE, AST_NAME, AST_IUSE) VALUES ('WRI', 'Essay', 'Y');
INSERT INTO CAM_AST (AST_CODE, AST_NAME, AST_IUSE) VALUES ('O', 'Other', 'Y');
INSERT INTO CAM_AST (AST_CODE, AST_NAME, AST_IUSE) VALUES ('DA', 'Design Assignment', 'Y');
INSERT INTO CAM_AST (AST_CODE, AST_NAME, AST_IUSE) VALUES ('T', 'In-Class Test', 'Y');
INSERT INTO CAM_AST (AST_CODE, AST_NAME, AST_IUSE) VALUES ('D', 'Dissertation', 'Y');

INSERT INTO SRS_FAC (FAC_CODE, FAC_NAME, FAC_IUSE) VALUES ('A', 'Arts', 'Y');
INSERT INTO SRS_FAC (FAC_CODE, FAC_NAME, FAC_IUSE) VALUES ('S', 'Science', 'Y');
INSERT INTO SRS_FAC (FAC_CODE, FAC_NAME, FAC_IUSE) VALUES ('T', 'Social Sciences', 'Y');
INSERT INTO SRS_FAC (FAC_CODE, FAC_NAME, FAC_IUSE) VALUES ('X', 'No Faculty', 'Y');
INSERT INTO SRS_FAC (FAC_CODE, FAC_NAME, FAC_IUSE) VALUES ('M', 'Medicine', 'Y');
INSERT INTO SRS_FAC (FAC_CODE, FAC_NAME, FAC_IUSE) VALUES ('I', 'Interdisciplinary/Cross-Faculty', 'Y');

INSERT INTO INS_DPT (DPT_CODE, DPT_NAME, DPT_TYPE, DPT_IUSE, DPT_FACC) VALUES ('CS', 'Computer Science', 'DEP', 'Y', 'S');
INSERT INTO INS_DPT (DPT_CODE, DPT_NAME, DPT_TYPE, DPT_IUSE, DPT_FACC) VALUES ('MA', 'Mathematics Institute', 'DEP', 'Y', 'S');
INSERT INTO INS_DPT (DPT_CODE, DPT_NAME, DPT_TYPE, DPT_IUSE, DPT_FACC) VALUES ('ES', 'School of Engineering', 'SCH', 'Y', 'S');
INSERT INTO INS_DPT (DPT_CODE, DPT_NAME, DPT_TYPE, DPT_IUSE, DPT_FACC) VALUES ('LN', 'School of Modern Languages and Cultures', 'DEP', 'Y', 'A');
INSERT INTO INS_DPT (DPT_CODE, DPT_NAME, DPT_TYPE, DPT_IUSE, DPT_FACC) VALUES ('EC', 'Economics', 'DEP', 'Y', 'T');
INSERT INTO INS_DPT (DPT_CODE, DPT_NAME, DPT_TYPE, DPT_IUSE, DPT_FACC) VALUES ('IN', 'IT Services', 'ADM', 'Y', 'X');

INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('1', 'Undergraduate Level 1', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('2', 'Undergraduate Level 2', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('3', 'Undergraduate Level 3', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('4', 'Undergraduate Level 4', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('5', 'Undergraduate Level 5', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('6', 'Undergraduate Level 6', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('7', 'Undergraduate Level 7', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('8', 'Undergraduate Level 8', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('9', 'Undergraduate Level 9', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('10', 'Undergraduate Level 10', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('11', 'Undergraduate Level 11', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('12', 'Undergraduate Level 12', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('F', 'Foundation', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('M1', 'Taught Postgraduate Level', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('M2', 'Research Postgraduate Level', 'Y');
INSERT INTO CAM_LEV (LEV_CODE, LEV_NAME, LEV_IUSE) VALUES ('X', 'Post-Experience', 'Y');

INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('COY', 'CO-REQ BY YEAR', 'CO-REQUISITE BY YEAR', 'N');
INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('PRC', 'PRE OR CO-REQ', 'PRE-REQUISITE OR CO-REQUISITE', 'N');
INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('CO', 'CO-REQUISITE', 'CO-REQUISITE', 'N');
INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('NON', 'NON-REQUISITE', 'NON-REQUISITE', 'N');
INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('POST', 'POST-REQUISITE', 'POST-REQUISITE', 'N');
INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('PRE', 'PRE-REQUISITE', 'PRE-REQUISITE', 'N');
INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('REQ', 'REQUISITE', 'REQUISITE', 'Y');
INSERT INTO CAM_REX (REX_CODE, REX_SNAM, REX_NAME, REX_APPLY) VALUES ('ANTI', 'ANTI-REQUISITE', 'ANTI-REQUISITE', 'N');

INSERT INTO CAM_RCL (RCL_CODE, RCL_SNAM, RCL_NAME, RCL_APPLY) VALUES ('COMP', 'COMPULSORY', 'COMPULSORY RULES', 'Y');
INSERT INTO CAM_RCL (RCL_CODE, RCL_SNAM, RCL_NAME, RCL_APPLY) VALUES ('OPEN', 'OPEN', 'OPEN RULES', 'N');
INSERT INTO CAM_RCL (RCL_CODE, RCL_SNAM, RCL_NAME, RCL_APPLY) VALUES ('RECO', 'RECOMMENDED', 'RECOMMENDED RULES', 'N');

INSERT INTO CAM_MMR (MOD_CODE, MMR_CODE, MMR_AYRC, REX_CODE, RCL_CODE, MMR_DESC) VALUES ('CS261-15', '001', '19/20', 'PRE', 'COMP', 'To take CS261-15 you must have taken and passed CS118-15 and CS126-15');
INSERT INTO CAM_MMR (MOD_CODE, MMR_CODE, MMR_AYRC, REX_CODE, RCL_CODE, MMR_DESC) VALUES ('CS261-15', '002', '19/20', 'ANTI', 'COMP', 'If you take CS261-15 you cannot take CS343-30');
INSERT INTO CAM_MMR (MOD_CODE, MMR_CODE, MMR_AYRC, REX_CODE, RCL_CODE, MMR_DESC) VALUES ('CS126-15', '001', '19/20', 'PRE', 'COMP', 'To take CS126-15 you must have taken and passed CS118-15');

INSERT INTO CAM_MMB (MOD_CODE, MMR_CODE, MMB_SEQ, MMB_TFLAG, MMB_MIN, MMB_MAX, FMC_CODE, MMB_UCRD) VALUES ('CS261-15', '001', '001', 'P', 1.00, 1.00, 'CS118-15', 'M');
INSERT INTO CAM_MMB (MOD_CODE, MMR_CODE, MMB_SEQ, MMB_TFLAG, MMB_MIN, MMB_MAX, FMC_CODE, MMB_UCRD) VALUES ('CS261-15', '001', '002', 'P', 1.00, 1.00, 'CS126-15', 'M');
INSERT INTO CAM_MMB (MOD_CODE, MMR_CODE, MMB_SEQ, MMB_TFLAG, MMB_MIN, MMB_MAX, FMC_CODE, MMB_UCRD) VALUES ('CS261-15', '002', '001', 'T', 0.00, 0.00, 'CS343-30', 'M');
INSERT INTO CAM_MMB (MOD_CODE, MMR_CODE, MMB_SEQ, MMB_TFLAG, MMB_MIN, MMB_MAX, FMC_CODE, MMB_UCRD) VALUES ('CS126-15', '001', '001', 'P', 0.00, 0.00, 'CS118-15', 'M');

INSERT INTO CAM_FMC (FMC_CODE, FMC_SNAM, FMC_NAME, FMC_IUSE) VALUES ('CS118-15', 'CS118', 'Programming for Computer Scientists', 'Y');
INSERT INTO CAM_FMC (FMC_CODE, FMC_SNAM, FMC_NAME, FMC_IUSE) VALUES ('CS126-15', 'CS126', 'Design of Information Structures', 'Y');
INSERT INTO CAM_FMC (FMC_CODE, FMC_SNAM, FMC_NAME, FMC_IUSE) VALUES ('CS343-30', 'CS343', 'Computer & Business Studies Project', 'Y');

INSERT INTO CAM_FME (FMC_CODE, FME_SEQ, FME_MODP, MTC_CODE) VALUES ('CS118-15', '001', 'CS118-15', 'S');
INSERT INTO CAM_FME (FMC_CODE, FME_SEQ, FME_MODP, MTC_CODE) VALUES ('CS126-15', '001', 'CS126-15', 'S');
INSERT INTO CAM_FME (FMC_CODE, FME_SEQ, FME_MODP, MTC_CODE) VALUES ('CS343-30', '001', 'CS343-30', 'S');

INSERT INTO INS_MOD (MOD_CODE, DPT_CODE, MAP_CODE, MOD_CRDT, MOD_NAME, MOD_IUSE) VALUES ('CS126-15', 'CS', 'CS126-15', '15', 'Design of Information Structures', 'Y');
INSERT INTO CAM_MAV (MOD_CODE, MAV_OCCUR, AYR_CODE, PSL_CODE, DPT_CODE, LEV_CODE, LCA_CODE, MAV_MAVN, PRS_CODE) VALUES ('CS126-15', 'A', '19/20', 'A', 'CS', '1', 'U', 'Design of Information Structures', 'CS0000126');
INSERT INTO CAM_MAVT (MOD_CODE, MAV_OCCUR, AYR_CODE, PSL_CODE, MAV_CRED) VALUES ('CS126-15', 'A', '19/20', 'A', '15');
INSERT INTO CAM_MDS (MOD_CODE, MDS_SEQN, MDS_DVNC, MDS_AYRC, MOD_DESC) VALUES ('CS126-15', '0001', 'TMB003', '19/20', 'The module aims for students to gain familiarity with the specification, implementation and use of some standard abstract data types (ADTs) such as linked lists, stacks, queues, graphs etc.');
INSERT INTO CAM_MDS (MOD_CODE, MDS_SEQN, MDS_DVNC, MDS_AYRC, MOD_DESC) VALUES ('CS126-15', '0002', 'TMB005', '19/20', 'https://warwick.ac.uk/fac/sci/dcs/teaching/material/cs126/');
INSERT INTO CAM_MDS (MOD_CODE, MDS_SEQN, MDS_DVNC, MDS_AYRC, MDS_UDF1, MDS_UDF2) VALUES ('CS126-15', '0003', 'MA013', '19/20', '30', '1');
INSERT INTO CAM_MDS (MOD_CODE, MDS_SEQN, MDS_DVNC, MDS_AYRC, MDS_UDF1, MDS_UDF2) VALUES ('CS126-15', '0004', 'MA018', '19/20', '8', '2');
INSERT INTO CAM_MDS (MOD_CODE, MDS_SEQN, MDS_DVNC, MDS_AYRC, MDS_TITL) VALUES ('CS126-15', '0005', 'MA026', '19/20', '20');
INSERT INTO CAM_TOP (TOP_CODE, MOD_CODE, DPT_CODE, TOP_PERC, TOP_UDF1) VALUES ('CS126-15-1', 'CS126-15', 'CS', '80', '19/20');
INSERT INTO CAM_TOP (TOP_CODE, MOD_CODE, DPT_CODE, TOP_PERC, TOP_UDF1) VALUES ('CS126-15-2', 'CS126-15', 'MA', '20', '19/20');

INSERT INTO CAM_MAP (MAP_CODE, MAP_AGRP) VALUES ('CS126-15', 'C');
INSERT INTO CAM_MAB (MAP_CODE, MAB_SEQ, MAB_AGRP, AST_CODE, MAB_PERC, MAB_TSHA, MAB_NAME, MAB_UDF4) VALUES ('CS126-15', 'A01', 'C', 'WRI', 50, 100, '1000 word essay', '19/20');
INSERT INTO CAM_MABD (MAB_MAPC, MAB_MABS, MAB_DESC) VALUES ('CS126-15', 'A01', 'Students write an essay');
INSERT INTO CAM_MAB (MAP_CODE, MAB_SEQ, MAB_AGRP, AST_CODE, MAB_PERC, MAB_TSHA, MAB_NAME, MAB_APAC, MAB_ADVC, MAB_UDF4) VALUES ('CS126-15', 'E01', 'C', 'ES', 50, 100, '2 hour exam', 'CS1260', 'X', '19/20');
INSERT INTO CAM_MABD (MAB_MAPC, MAB_MABS, MAB_DESC) VALUES ('CS126-15', 'E01', 'Students sit an exam');
INSERT INTO CAM_APA (APA_CODE, APA_APTC, APA_APSC) VALUES ('CS1260', 'C', 'STAN');
INSERT INTO CAM_ADV (ADV_APAC, ADV_CODE) VALUES ('CS1260', 'X');

-- Create a Computer Science course with three blocks occurring from 19/20 to 21/22
INSERT INTO SRS_CRS (CRS_CODE, CRS_NAME, CRS_DPTC) VALUES ('UCSA-G500', 'Computer Science', 'CS');

INSERT INTO SRS_CBK (CBK_CRSC, CBK_BLOK, CBK_YEAR) VALUES ('UCSA-G500', '1', '1');
INSERT INTO SRS_CBK (CBK_CRSC, CBK_BLOK, CBK_YEAR) VALUES ('UCSA-G500', '2', '2');
INSERT INTO SRS_CBK (CBK_CRSC, CBK_BLOK, CBK_YEAR) VALUES ('UCSA-G500', '3', '3');

INSERT INTO SRS_CBO (CBO_CRSC, CBO_BLOK, CBO_AYRC, CBO_OCCL, CBO_NBLK) VALUES ('UCSA-G500', '1', '19/20', 'A', '2');
INSERT INTO SRS_CBO (CBO_CRSC, CBO_BLOK, CBO_AYRC, CBO_OCCL, CBO_NBLK) VALUES ('UCSA-G500', '2', '20/21', 'A', '3');
INSERT INTO SRS_CBO (CBO_CRSC, CBO_BLOK, CBO_AYRC, CBO_OCCL, CBO_NBLK) VALUES ('UCSA-G500', '3', '21/22', 'A', null);

-- Create a Computer Science route and pathway, with a diet for three blocks
INSERT INTO INS_ROU (ROU_CODE, ROU_NAME) VALUES ('G500', 'Computer Science');
INSERT INTO INS_PWY (PWY_CODE, PWY_NAME, PWY_PWTC, PWY_AWDN) VALUES ('G500', 'Computer Science', 'UG', 'in Computer Science');

INSERT INTO CAM_PDT (PDT_CODE, PDT_MATC, PDT_TYPE, PDT_ROUC, PDT_PRDC, PDT_BLOK) VALUES ('G500-1-19', 'M', 'R', 'G500', 'Y', '1');
INSERT INTO CAM_PDT (PDT_CODE, PDT_MATC, PDT_TYPE, PDT_ROUC, PDT_PRDC, PDT_BLOK) VALUES ('G500-2-20', 'M', 'R', 'G500', 'Y', '2');
INSERT INTO CAM_PDT (PDT_CODE, PDT_MATC, PDT_TYPE, PDT_ROUC, PDT_PRDC, PDT_BLOK) VALUES ('G500-3-21', 'M', 'R', 'G500', 'Y', '3');

-- The Computer Science route is valid for the Computer Science course
INSERT INTO SRS_VCO (VCO_CRSC, VCO_SEQ6, VCO_PRGC, VCO_ROUC) VALUES ('UCSA-G500', '1', 'BSC', 'G500');

INSERT INTO INS_PRG (PRG_CODE, PRG_NAME) VALUES ('BSC', 'Degree of Bachelor of Science (with Honours)');

-- Create a core diet for block 1 containing CS126-15
INSERT INTO CAM_PDM (PDM_PDTC, PDM_SEQN, PDM_SESC, PDM_FMCC) VALUES ('G500-1-19', '001', 'C', 'G500-1-19-CXX');

INSERT INTO CAM_FMC (FMC_CODE) VALUES ('G500-1-19-CXX');

INSERT INTO CAM_FME (FMC_CODE, FME_SEQ, FME_MODP) VALUES ('G500-1-19-CXX', '001', 'CS126-15');

