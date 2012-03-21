package org.cocktail.grille.serveur.extractions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.ajax.AjaxProgress;
import er.extensions.appserver.ERXResponse;

public abstract class Extraction {

	public static final String MIME_PDF = "application/pdf";
	public static final String MIME_XLS = "application/xls";

	public static ERXResponse preparePDFResponse(NSData pdfData, String fileName) {

		ERXResponse response = new ERXResponse();
		if (pdfData != null) {
			response.setHeader("application/pdf", "Content-Type");
			response.setHeader(String.valueOf(pdfData.length()),
					"Content-Length");
			response.appendHeader(fileName, "Content-Title");
			response.appendHeader("attachement; filename=\"" + fileName + "\"",
					"Content-Disposition");
			response.removeHeadersForKey("cache-control");
			response.setContent(pdfData);
		}
		return response;
	}

	public static ERXResponse prepareFileAsStreamResponse(NSData fileData,
			String fileName, String mimeType) {

		ERXResponse response = new ERXResponse();
		if (fileData != null) {
			response.setHeader(mimeType, "Content-Type");
			response.setHeader(String.valueOf(fileData.length()),
					"Content-Length");
			response.appendHeader(fileName, "Content-Title");
			response.appendHeader("attachement; filename=\"" + fileName + "\"",
					"Content-Disposition");
			response.removeHeadersForKey("cache-control");
			response.setContent(fileData);
			response.removeHeadersForKey("pragma");
		}
		return response;
	}

	public String safeToString(Object object) {
		if (object == null) {
			return "";
		}
		if (object instanceof String) {
			return (String) object;
		} else if (object instanceof Double) {
			return String.valueOf(((Double) object).doubleValue());
		} else if (object instanceof Integer) {
			return String.valueOf(((Integer) object).doubleValue());
		} else if (object instanceof BigDecimal) {
			return String.valueOf(((BigDecimal) object).doubleValue());
		} else {
			return "";
		}
	}

	public static byte[] getPdfBytesForReport(String reportPathName,
			Map parameters, Connection connection) throws Exception {

		System.out.println("connection=" + connection);

		JasperPrint jasperPrint = JasperFillManager.fillReport(reportPathName,
				parameters, connection);

		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		JRPdfExporter jrpdfexporter = new JRPdfExporter();
		jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				bytearrayoutputstream);
		jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT,
				jasperPrint);
		jrpdfexporter.exportReport();

		connection.close();

		return bytearrayoutputstream.toByteArray();
	}

	/**
	 * génère et retourne un java.sql.Connection à partir des parametres de
	 * connection du modele dont on passe le nom en parametre. Cette méthode
	 * fonctionne uniquement pour se connecter à une base Oracle. Cette methode
	 * est à utiliser notamment lors de la génération d'une édition
	 * jasperreports.
	 * 
	 * @param strNomModele
	 *            : String, nom du modele
	 */
	public static Connection getOracleConnectionFromModel(String strNomModele) {
		Connection conn = null;
		boolean erreurDriver = false;
		EOModelGroup grpModel = EOModelGroup.defaultGroup();
		EOModel model = (EOModel) grpModel.modelNamed(strNomModele);

		if (model != null) {

			NSDictionary<String, Object> connectionDictionary = model.connectionDictionary();
			String urlDataBase = (String) connectionDictionary
					.valueForKey("URL");
			String username = (String) connectionDictionary
					.valueForKey("username");
			String password = (String) connectionDictionary
					.valueForKey("password");

			conn = getOracleConnection(urlDataBase, username, password);
		} else {
			NSLog.out.appendln("Le modèle  :  '" + strNomModele
					+ "' n'existe pas.");
		}
		return conn;
	}

	/**
	 * génère et retourne un java.sql.Connection à partir des parametres de
	 * connection qu'on passe en parametres. Cette méthode fonctionne uniquement
	 * pour se connecter à une base Oracle. Cette methode est à utiliser
	 * notamment lors de la génération d'une édition jasperreports.
	 * 
	 * @param strUrl
	 *            : String, url jdbc
	 * @param strUsername
	 *            : String, login
	 * @param strPassword
	 *            : String, mot de passe
	 */
	public static Connection getOracleConnection(String strUrl,
			String strUsername, String strPassword) {
		Connection conn = null;
		boolean erreurDriver = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception ex1) {
			NSLog.out.appendln("Echec du chargement du driver Oracle : " + ex1);
			erreurDriver = true;
		}

		if (!erreurDriver) {
			try {
				conn = DriverManager.getConnection(strUrl, strUsername,
						strPassword);
			} catch (Exception ex2) {
				NSLog.out.appendln("Echec de la connexion Oracle : " + ex2);
				conn = null;
			}
		}
		return conn;
	}

	/**
	 * Methode renvoyant un NSDictionary contenant un String "message" avec les
	 * messages d'erreur eventuels et un
	 * <code>NSData<code> "streamOut" représentant un fichier PDF fait
	 *  avec JasperReport
	 * 
	 * @param parameters
	 *            : Map de parametres JReport.
	 * @param reportTemplate
	 *            : URL du template jasperReport *.jasper ou String contenant le
	 *            jrxml
	 * @param dataSource
	 *            : JRXMLDataSource ou java.sql.Connection definissant la
	 *            connection à la base de données, null si il n'y en a pas
	 * @param format
	 *            : format du fichier de sortie (pdf,html,csv,xls)
	 * @return NSDictionary avec 2 cles : "message" = message d'erreur si il y
	 *         a. "streamOut" = NSData contenant le fichier de sortie.
	 */
	public static NSDictionary<String, Object> getPDFByteArray(
			Map<Object, Object> parameters, Object reportTemplate,
			Object dataSource, String format) {
		boolean blConnection = (dataSource != null);
		NSMutableDictionary<String, Object> retour = new NSMutableDictionary<String, Object>();
		ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		String stErreur = "";

		if (!(parameters != null)) {
			parameters = new HashMap<Object, Object>();
		}

		if ((reportTemplate != null)) {

			//Template Jasper
			JasperReport report = null;
			if (reportTemplate.getClass().equals(String.class)) {
				try {
					report = JasperCompileManager
							.compileReport(new ByteArrayInputStream(
									((String) reportTemplate)
											.getBytes("ISO-8859-1")));
				} catch (Exception ex1) {
					stErreur += "\nERREUR compileReport : " + ex1;
					NSLog.out.appendln("<<<<<<<ERREUR compileReport :" + ex1);
					report = null;
				}
			} else {
				if (reportTemplate.getClass().equals(URL.class)) {
					try {
						report = (JasperReport) JRLoader
								.loadObject(((URL) reportTemplate));
					} catch (Exception ex2) {
						stErreur += "\nERREUR loadObject : " + ex2;
						NSLog.out.appendln("<<<<ERREUR loadReport:" + ex2);
						report = null;
					}
				}
			}

			//Data Source
			JREmptyDataSource ds = null;
			Connection cn = null;
			JRXmlDataSource xmlDs = null;
			if (blConnection) {
				if (dataSource.getClass().equals(JRXmlDataSource.class)) {
					xmlDs = (JRXmlDataSource) dataSource;
				} else {
					cn = (Connection) dataSource;
				}
			} else {
				ds = new JREmptyDataSource();
			}

			//Fill Report
			JasperPrint print = null;
			if (report != null) {
				try {
					if (blConnection) {
						if (dataSource.getClass().equals(JRXmlDataSource.class)) {
							print = JasperFillManager.fillReport(report,
									parameters, xmlDs);
						} else {
							print = JasperFillManager.fillReport(report,
									parameters, cn);
						}
					} else
						print = JasperFillManager.fillReport(report,
								parameters, ds);
				} catch (Exception ex3) {
					stErreur += "\nERREUR fillReport : " + ex3;
					NSLog.out.appendln("<<<<ERREUR fillReport:" + ex3);
					print = null;
				}
			} else {
				print = null;
			}

			//Exporter
			net.sf.jasperreports.engine.JRExporter exporter = null;
			try {
				if (format.equalsIgnoreCase("pdf")) {
					exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
				} else if (format.equalsIgnoreCase("csv")) {
					exporter = new net.sf.jasperreports.engine.export.JRCsvExporter();
				} else if (format.equalsIgnoreCase("html")) {
					exporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
				} else if (format.equalsIgnoreCase("xls")) {
					exporter = new net.sf.jasperreports.engine.export.JRXlsExporter();
				} else if (format.equalsIgnoreCase("java2D")) {
					exporter = new net.sf.jasperreports.engine.export.JRGraphics2DExporter();
				} else {
					//par defaut si le format de sortie n'est pas reconnu, on sort du pdf.
					exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
				}

			} catch (Exception ex4) {
				stErreur += "\nERREUR exporter:" + ex4;
				NSLog.out.appendln("<<<<ERREUR exporter:" + ex4);
				exporter = null;
			}

			//Export Report
			if ((print != null) && (exporter != null)) {
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
						streamOut);
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				try {
					exporter.exportReport();
				} catch (Exception ex5) {
					stErreur += "\nERREUR exportReport:" + ex5;
					NSLog.out.appendln("<<<<ERREUR exportReport : " + ex5);
					streamOut = new ByteArrayOutputStream();
				}
			}
		}

		//Remplissage du dictionnaire de sortie
		retour.setObjectForKey(stErreur, "message");
		retour.setObjectForKey(new NSData(streamOut.toByteArray()), "streamOut");
		return retour;
	}

	public static NSData concatenationPdf(NSArray<NSData> listePdf) {
		boolean erreur = false;
		NSData result = null;

		if (listePdf.count() < 2) {
			erreur = true;
			NSLog.out.appendln("ERREUR : IL FAUT AU MOINS 2 PDF");
		}

		for (int i = 0; i < listePdf.count(); i++) {
			if (!(listePdf.objectAtIndex(i).getClass().equals(NSData.class))) {
				erreur = true;
				NSLog.out
						.appendln("ERREUR : LES PDF DOIVENT ETRE SOUS FORME DE NSDATA");
			}
		}

		if (!erreur) {
			try {
				int pageOffset = 0;
				ArrayList<NSData> master = new ArrayList<NSData>();
				ByteArrayOutputStream outFile = new ByteArrayOutputStream();
				com.lowagie.text.Document document = null;
				PdfCopy writer = null;
				for (int j = 0; j < listePdf.count(); j++) {

					PdfReader reader = new PdfReader(
							((NSData) listePdf.objectAtIndex(j)).bytes());
					reader.consolidateNamedDestinations();
					int n = reader.getNumberOfPages();

					java.util.List bookmarks = SimpleBookmark
							.getBookmark(reader);
					if (bookmarks != null) {
						if (pageOffset != 0) {
							SimpleBookmark.shiftPageNumbers(bookmarks,
									pageOffset, null);
						}
						master.addAll(bookmarks);
					}

					pageOffset = pageOffset + n;

					if (j == 0) {
						document = new com.lowagie.text.Document(
								reader.getPageSizeWithRotation(1));
						writer = new PdfCopy(document, outFile);
						document.open();
					}

					PdfImportedPage page = null;
					for (int cpt = 1; cpt <= n; cpt++) {
						page = writer.getImportedPage(reader, cpt);
						writer.addPage(page);
					}

					PRAcroForm form = reader.getAcroForm();
					if (form != null) {
						writer.copyAcroForm(reader);
					}
				}

				if (master.size() > 0) {
					writer.setOutlines(master);
				}

				document.close();
				result = new NSData(outFile.toByteArray());
			} catch (Exception ex) {
				NSLog.out.appendln("ERREUR CONCATENATION PDF : " + ex);
			}
		}
		return result;
	}

	public static NSData getXlsForColonnes(
			NSMutableArray<ColonneExportXls> cols, EOFetchSpecification fetch,
			EOEditingContext ec, String title,
			NSMutableArray<EOSortOrdering> orderBy, AjaxProgress printProgress)
			throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		ByteArrayOutputStream excelOutputStream = null;
		excelOutputStream = new ByteArrayOutputStream();

		HSSFSheet sheet = workbook.createSheet(title);
		HSSFCellStyle Total = workbook.createCellStyle();
		HSSFCellStyle bdrLR = workbook.createCellStyle();
		HSSFCellStyle bdrLRnum = workbook.createCellStyle();

		//Contruction entete
		buildEnteteXls(sheet, workbook, title, cols, Total, bdrLR, bdrLRnum);

		//construction des lignes a partir d'un fetch
		buildRowsFromFetch(fetch, ec, orderBy, sheet, cols, Total, bdrLR,
				bdrLRnum, printProgress);

		//set autoSize
		setAutosizeForSheet(sheet, cols);

		workbook.write(excelOutputStream);
		NSData excelData = null;

		if (excelOutputStream.size() > 0) {
			excelData = new NSData(excelOutputStream.toByteArray());
		}

		excelOutputStream.close();
		return excelData;
	}

	public static void buildRowsFromFetch(EOFetchSpecification fetch,
			EOEditingContext ec, NSMutableArray<EOSortOrdering> orderBy,
			HSSFSheet sheet, NSMutableArray<ColonneExportXls> cols,
			HSSFCellStyle Total, HSSFCellStyle bdrLR, HSSFCellStyle bdrLRnum,
			AjaxProgress printProgress) {

		// execution de la requete
		fetch.setUsesDistinct(true);
		NSMutableArray<EOGenericRecord> lst = ec.objectsWithFetchSpecification(
				fetch).mutableClone();

		if (orderBy != null) {
			EOSortOrdering.sortArrayUsingKeyOrderArray(lst, orderBy);
		}

		NSMutableDictionary<String, String> colEntetes = getParamsForCols(cols);

		short ligne = 2; // variable d'incrementation des lignes de la feuille

		// lecture du curseur
		for (EOGenericRecord obj : lst) {
			ligne += 1;
			// Create a row and put some cells in it. Rows are 0 based.
			HSSFRow row = sheet.createRow(ligne);
			int indCol = 0;
			for (ColonneExportXls colOccur : cols) {
				HSSFCell cell = row.createCell(indCol);
				indCol++;
				if (HSSFCell.CELL_TYPE_FORMULA == colOccur.getCellType()) {
					traitFormulaCell(
							obj.valueForKeyPath(colOccur.getCellPath()), cell,
							colOccur, colEntetes, ligne, Total);
				}
				if (HSSFCell.CELL_TYPE_STRING == colOccur.getCellType()) {
					traitStringCell(
							obj.valueForKeyPath(colOccur.getCellPath()), cell,
							bdrLR);
				}
				if (HSSFCell.CELL_TYPE_NUMERIC == colOccur.getCellType()) {
					traitNumericCell(
							obj.valueForKeyPath(colOccur.getCellPath()), cell,
							bdrLRnum);
				}
				if (printProgress != null) {
					printProgress.incrementValue(1);
				}

			}

		}

	}

	public static void traitFormulaCell(Object value, HSSFCell cell,
			ColonneExportXls colOccur,
			NSMutableDictionary<String, String> colEntetes, short ligne,
			HSSFCellStyle Total) {
		String formul1 = ""; // formule de totalisation associe au cellule
		formul1 = colOccur.getCellValue();
		for (String param : colEntetes.allKeys()) {
			formul1 = formul1.replaceAll(param, colEntetes.get(param)
					+ (ligne + 1));
		}
		cell.setCellFormula(formul1);
		cell.setCellStyle(Total);
	}

	public static void traitStringCell(Object value, HSSFCell cell,
			HSSFCellStyle bdrLR) {
		if (value instanceof NSArray) {
			String libelle = "";
			for (Object lib : (NSArray<String>) value) {
				if (!libelle.equals("")) {
					libelle += "\n";
				}
				libelle += lib;
			}
			cell.setCellValue(libelle);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(bdrLR);
	}

	public static void traitNumericCell(Object value, HSSFCell cell,
			HSSFCellStyle bdrLRnum) {
		if (value instanceof BigDecimal) {
			cell.setCellValue(((BigDecimal) value).doubleValue());
		} else if (value instanceof Integer) {
			cell.setCellValue(((Integer) value).doubleValue());
		} else if (value instanceof Long) {
			cell.setCellValue(((Long) value).doubleValue());
		} else if (value instanceof Double) {
			cell.setCellValue(((Double) value));
		} else {
			cell.setCellValue(Double.parseDouble((String) (value!=null?value:"0")));
		}
		cell.setCellStyle(bdrLRnum);
	}

	public static void buildEnteteXls(HSSFSheet sheet, HSSFWorkbook workbook,
			String title, NSMutableArray<ColonneExportXls> cols,
			HSSFCellStyle Total, HSSFCellStyle bdrLR, HSSFCellStyle bdrLRnum) {
		// definition des styles de format de cellule
		// style 1 : couleur marron et et bold
		HSSFCellStyle Style1 = workbook.createCellStyle();
		HSSFFont font1 = workbook.createFont();
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font1.setColor(HSSFColor.BROWN.index);
		Style1.setFont(font1);
		Style1.setAlignment(CellStyle.ALIGN_CENTER);
		//Style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		// style 2 : couleur vert et bold
		HSSFCellStyle Style2 = workbook.createCellStyle();
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font2.setColor(HSSFColor.GREEN.index);
		Style2.setFont(font2);
		Style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		// creation style couleur

		//style total : fond jaune

		Total.setFillForegroundColor(HSSFColor.YELLOW.index);
		Total.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		Total.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		Total.setBorderRight(CellStyle.BORDER_THIN);
		Total.setBorderLeft(CellStyle.BORDER_THIN);

		// autre style

		bdrLR.setBorderRight(CellStyle.BORDER_THIN);
		bdrLR.setBorderLeft(CellStyle.BORDER_THIN);

		bdrLRnum.setBorderRight(CellStyle.BORDER_THIN);
		bdrLRnum.setBorderLeft(CellStyle.BORDER_THIN);
		bdrLRnum.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

		HSSFCellStyle header = workbook.createCellStyle();
		header.setBorderRight(CellStyle.BORDER_THIN);
		header.setBorderLeft(CellStyle.BORDER_THIN);
		header.setBorderTop(CellStyle.BORDER_THIN);
		header.setBorderBottom(CellStyle.BORDER_THIN);
		header.setFont(font1);
		header.setAlignment(CellStyle.ALIGN_CENTER);
		// creation des noms de colonne
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 600);
		HSSFCell titre = row.createCell(0);
		titre.setCellValue(title);
		titre.setCellStyle(Style1);
		//titre.getCellStyle().getFont(wb).setFontHeight((short)22);

		row = sheet.createRow(2);
		int indCol = 0;

		for (ColonneExportXls colOccur : cols) {
			HSSFCell cell = row.createCell(indCol);
			cell.setCellValue(colOccur.getCellName());
			cell.setCellStyle(header);
			indCol++;
			row.setHeight((short) 600);
		}

		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, //first row (0-based)
				0, //last row  (0-based)
				0, //first column (0-based)
				indCol - 1 //last column  (0-based)
		));
	}

	public static void setAutosizeForSheet(HSSFSheet sheet,
			NSMutableArray<ColonneExportXls> cols) {
		int indCol = 0;
		for (ColonneExportXls colOccur : cols) {

			sheet.autoSizeColumn(indCol, false);
			sheet.setColumnWidth(indCol, sheet.getColumnWidth(indCol) + 500);
			if (colOccur.getMinWidth() != null) {

				if (sheet.getColumnWidth(indCol) < colOccur.getMinWidth()) {
					sheet.setColumnWidth(indCol, colOccur.getMinWidth());
				}
			}
			indCol++;

		}
	}

	public static NSMutableDictionary<String, String> getParamsForCols(
			NSMutableArray<ColonneExportXls> cols) {
		int indCol = 0;
		NSMutableDictionary<String, String> colEntetes = new NSMutableDictionary<String, String>();
		for (ColonneExportXls colOccur : cols) {
			if (colOccur.getCellParamName() != null) {
				colEntetes.setObjectForKey(((char) (65 + indCol)) + "",
						colOccur.getCellParamName());
			}
			indCol++;
		}
		return colEntetes;
	}
}
