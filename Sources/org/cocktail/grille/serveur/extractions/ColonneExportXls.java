package org.cocktail.grille.serveur.extractions;

public class ColonneExportXls {
	protected String cellName;
	protected String cellValue;
	protected int cellType;
	protected boolean cellChecked;
	protected boolean cellMandatory;
	protected String cellPath;
	protected Integer cellOrder;
	protected int colOrder;
	protected String cellParamName;
	
	protected Integer minWidth;
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public int getCellType() {
		return cellType;
	}
	public void setCellType(int cellType) {
		this.cellType = cellType;
	}
	public boolean isCellChecked() {
		return cellChecked;
	}
	public void setCellChecked(boolean cellChecked) {
		this.cellChecked = cellChecked;
	}
	public String getCellPath() {
		return cellPath;
	}
	public void setCellPath(String cellPath) {
		this.cellPath = cellPath;
	}
	public Integer getCellOrder() {
		return cellOrder;
	}
	public void setCellOrder(Integer cellOrder) {
		this.cellOrder = cellOrder;
	}
	public boolean isCellMandatory() {
		return cellMandatory;
	}
	public void setCellMandatory(boolean cellMandatory) {
		this.cellMandatory = cellMandatory;
	}
	public String getCellValue() {
		return cellValue;
	}
	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}
	public Integer getMinWidth() {
		return minWidth;
	}
	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}
	public int getColOrder() {
		return colOrder;
	}
	public void setColOrder(int colOrder) {
		this.colOrder = colOrder;
	}
	public String getCellParamName() {
		return cellParamName;
	}
	public void setCellParamName(String cellParamName) {
		this.cellParamName = cellParamName;
	}

}
