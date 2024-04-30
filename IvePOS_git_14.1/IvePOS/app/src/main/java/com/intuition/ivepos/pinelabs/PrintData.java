package com.intuition.ivepos.pinelabs;

import com.google.gson.annotations.SerializedName;

public class PrintData {

    @SerializedName("PrinterWidth")
    private Integer iPrinterWidth = 24;
    @SerializedName("DataToPrint")
    private String strDataToPrint;
    @SerializedName("ImagePath")
    private String strImagePath;
    @SerializedName("ImageData")
    private String strImageData;
    @SerializedName("IsCenterAligned")
    private Boolean IsCenterAligned = true;
    @SerializedName("PrintDataType")
    private String iPrintDataType;

    public PrintData(Integer iPrinterWidth, String strDataToPrint, String strImagePath, String strImageData, Boolean isCenterAligned, String iPrintDataType) {
        this.iPrinterWidth = iPrinterWidth;
        this.strDataToPrint = strDataToPrint;
        this.strImagePath = strImagePath;
        this.strImageData = strImageData;
        IsCenterAligned = isCenterAligned;
        this.iPrintDataType = iPrintDataType;
    }

    public Integer getiPrinterWidth() {
        return iPrinterWidth;
    }

    public void setiPrinterWidth(Integer iPrinterWidth) {
        this.iPrinterWidth = iPrinterWidth;
    }

    public String getStrDataToPrint() {
        return strDataToPrint;
    }

    public void setStrDataToPrint(String strDataToPrint) {
        this.strDataToPrint = strDataToPrint;
    }

    public String getStrImagePath() {
        return strImagePath;
    }

    public void setStrImagePath(String strImagePath) {
        this.strImagePath = strImagePath;
    }

    public String getStrImageData() {
        return strImageData;
    }

    public void setStrImageData(String strImageData) {
        this.strImageData = strImageData;
    }

    public Boolean getCenterAligned() {
        return IsCenterAligned;
    }

    public void setCenterAligned(Boolean centerAligned) {
        IsCenterAligned = centerAligned;
    }

    public String getiPrintDataType() {
        return iPrintDataType;
    }

    public void setiPrintDataType(String iPrintDataType) {
        this.iPrintDataType = iPrintDataType;
    }
}
