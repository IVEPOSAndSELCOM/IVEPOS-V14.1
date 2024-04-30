package com.intuition.ivepos.mSwipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

import com.intuition.ivepos.R;
import com.intuition.ivepos.wisepos.ReceiptBitmap;
import com.mswipetech.wisepad.sdk.data.ReceiptData;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController;

public class ReceiptUtilityNeo {

    private final String log_tab = "ReceiptUtility=>";
    private DecimalFormat df = new DecimalFormat(".00");


    private Context context = null;
    private ReceiptData mReceiptDataModel = null;
    private boolean isPrintSignatureRequired = false;
    private byte[] bitmapBytes = null;
    private int mswipeTargetWidth = 380;
    private boolean isCustomerCopy = false;
    private int isFirstTimeApproval = 0;

    private String mTotalAmount = "";
    private String mBatchNo = "";
    private String mOpendate = "";
    private String mClosedate = "";

    public static enum TYPE{
        CARD, EMI, CASH
    }

    public static final byte[] INIT = {0x1B,0x40};
    public static final  byte[] POWER_ON = {0x1B,0x3D,0x01};
    public static final  byte[] POWER_OFF = {0x1B,0x3D,0x02};
    public static final  byte[] NEW_LINE = {0x0A};
    public static final  byte[] ALIGN_LEFT = {0x1B,0x61,0x00};
    public static final  byte[] ALIGN_CENTER = {0x1B,0x61,0x01};
    public static final  byte[] ALIGN_RIGHT = {0x1B,0x61,0x02};
    public static final  byte[] EMPHASIZE_ON = {0x1B,0x45,0x01};
    public static final  byte[] EMPHASIZE_OFF = {0x1B,0x45,0x00};

    public static final  byte[] FONT_5X8 = {0x1B,0x4D,0x00};
    public static final  byte[] FONT_5X12 = {0x1B,0x4D,0x01};
    public static final  byte[] FONT_8X12 = {0x1B,0x4D,0x02};
    public static final  byte[] FONT_10X18 = {0x1B,0x4D,0x03};

    public static final  byte[] FONT_SIZE_0 = {0x1D,0x21,0x00};
    public static final  byte[] FONT_SIZE_1 = {0x1D,0x21,0x11};

    public static final  byte[] CHAR_SPACING_0 = {0x1B,0x20,0x00};
    public static final  byte[] CHAR_SPACING_1 = {0x1B,0x20,0x01};

    public ReceiptUtilityNeo(Context context){

        this.context = context;
    }


    public byte[] genReceipt4(ReceiptData aReceiptDataModel, Bitmap bitmap, boolean isPrintSignatureRequired, TYPE type, boolean isCustomerCopy) {


        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"genReceipt4",true,true);

        ReceiptData mReceiptDataModel = aReceiptDataModel;
        boolean PrintSignatureRequired = isPrintSignatureRequired;
        boolean CustomerCopy = isCustomerCopy;

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ms_mswipe2);

        if (bitmap != null) {

            bitmapBytes = convertBitmap(bitmap, mswipeTargetWidth, 160);
        }else{

            this.isPrintSignatureRequired = false;
        }

        //Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/english/Courier.ttf");
        Typeface tf = Typeface.SERIF;

        String strTipLbl = "TIP AMOUNT:    ";
        if(mReceiptDataModel.isConvenceFeeEnable.equalsIgnoreCase("true")){
            strTipLbl = "CONVENIENCE FEE:";
        }

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  " isConvenceFeeEnable " + mReceiptDataModel.isConvenceFeeEnable + strTipLbl, true, true);

        double tipamount = 0.00;
        if (mReceiptDataModel.tipAmount.length() > 0 ) {
            tipamount = Double.parseDouble(mReceiptDataModel.tipAmount);
        }

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"tipamount"+tipamount,true,true);

        try {
            ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
            receiptBitmap.init(2000);

            receiptBitmap.setTextSize(40);
            receiptBitmap.setTypeface(Typeface.create(tf, Typeface.BOLD));

            receiptBitmap.drawLeftText("");
            receiptBitmap.drawLeftText("");

            receiptBitmap.drawImage(icon);

            receiptBitmap.setTextSize(19);
            //drawCenterText(canvas, paintText, mReceiptDataModel.bankName);
            receiptBitmap.drawCenterText(mReceiptDataModel.merchantName);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,"address  "+ mReceiptDataModel.merchantAdd,true,true);

            for (String add : mReceiptDataModel.merchantAdd.split("<br />")) {

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName,"address  "+add,true,true);

                receiptBitmap.setTextSize(16);
                receiptBitmap.drawCenterText(add);
            }

            receiptBitmap.setTextSize(19);
            receiptBitmap.drawLeftText("----------------------------------------------------------");

            String tempDate = removeampm(mReceiptDataModel.dateTime);
            String dateTime = Constants.getDateWithFormate(tempDate, "dd-MMMM-yyyy HH:mm", "dd MMM yyyy hh:mm a");

            receiptBitmap.drawLeftText("Date/Time: " + dateTime.toUpperCase());


            String mMidTid = "MID:" + mReceiptDataModel.mId + "   TID:"+ mReceiptDataModel.tId;
            String mBatchInvoide = "Batch no:" +mReceiptDataModel.batchNo + "   Inv. no:"+mReceiptDataModel.invoiceNo.replace("\n","");

            if(!mReceiptDataModel.tId.equals("") || !mReceiptDataModel.mId.equals(""))
            {

                receiptBitmap.drawLeftText(mMidTid);
            }

            //receiptBitmap.drawLeftText("Batch No: " + mReceiptDataModel.batchNo +  "Inv. No:" + mReceiptDataModel.invoiceNo.replace("\n",""));

            receiptBitmap.drawLeftText(mBatchInvoide);


            receiptBitmap.drawLeftText("REF. NO.:" + mReceiptDataModel.refNo);

            if (mReceiptDataModel.cashAmount.length() > 0 && mReceiptDataModel.tipAmount.length()<= 0 && mReceiptDataModel.baseAmount.length()<= 0) {

                mReceiptDataModel.saleType = "Cash Only";

            }

            receiptBitmap.drawCenterText("");

            receiptBitmap.drawCenterText( mReceiptDataModel.saleType.toLowerCase());


            receiptBitmap.drawLeftText( "Card No:" + mReceiptDataModel.cardNo+" "+mReceiptDataModel.trxType);
            receiptBitmap.drawLeftText( "Card Type:" + mReceiptDataModel.cardType);
            receiptBitmap.drawLeftText( "Exp Dt:" + "xx/xx");

            if (mReceiptDataModel.cashAmount.length() > 0 && mReceiptDataModel.baseAmount.length() > 0) {


                receiptBitmap.drawLeftText( "Sale Amt:   " + mReceiptDataModel.currency + " " +mReceiptDataModel.baseAmount);
                receiptBitmap.drawLeftText( "Cash Paid:    " + mReceiptDataModel.currency + " " +mReceiptDataModel.cashAmount);

                if (mReceiptDataModel.tipAmount.length() > 0 && tipamount >0) {

                    receiptBitmap.drawLeftText(  mReceiptDataModel.currency + " " +mReceiptDataModel.tipAmount);
                }

                receiptBitmap.drawLeftText("----------------------------------------------------------");
                receiptBitmap.drawLeftText("Total Amount:   " + mReceiptDataModel.currency + " " +String.format("%.2f",(Double.parseDouble(mReceiptDataModel.totalAmount))));


            }else if (mReceiptDataModel.cashAmount.length() > 0 && mReceiptDataModel.baseAmount.length()<= 0) {

                if (mReceiptDataModel.tipAmount.length() > 0 && tipamount > 0) {

                    receiptBitmap.drawLeftText("Sale Amt:   " + mReceiptDataModel.currency + " 0.00");
                    receiptBitmap.drawLeftText("Cash Paid:   " + mReceiptDataModel.currency + " " +mReceiptDataModel.cashAmount);
                    receiptBitmap.drawLeftText(strTipLbl + mReceiptDataModel.currency + " " +mReceiptDataModel.tipAmount);
                    receiptBitmap.drawLeftText("----------------------------------------------------------");
                    receiptBitmap.drawLeftText("Total Amount:   " + mReceiptDataModel.currency + " " +mReceiptDataModel.totalAmount);

                }else
                {

                    receiptBitmap.drawLeftText("Cash Paid:    " + mReceiptDataModel.currency + " " +mReceiptDataModel.cashAmount);
                }
            }else {

                if (mReceiptDataModel.tipAmount.length() > 0 && tipamount > 0) {


                    receiptBitmap.drawLeftText("Base Amount:    " + mReceiptDataModel.currency + " " + mReceiptDataModel.baseAmount);
                    receiptBitmap.drawLeftText(strTipLbl + mReceiptDataModel.currency + " " +mReceiptDataModel.tipAmount);
                    receiptBitmap.drawLeftText("----------------------------------------------------------");
                    receiptBitmap.drawLeftText("Total Amount:   " + mReceiptDataModel.currency + " " +mReceiptDataModel.totalAmount);

                }
                else {

                    receiptBitmap.drawLeftText("Total Amount:   " + mReceiptDataModel.currency + " " +mReceiptDataModel.totalAmount);
                }
            }

            receiptBitmap.drawLeftText("Appr cd:" + mReceiptDataModel.authCode +" RRN:" + mReceiptDataModel.rrNo);


            if (mReceiptDataModel.trxType.equalsIgnoreCase("Chip")) {


                receiptBitmap.drawLeftText("--------------------------------------------------------");
                receiptBitmap.drawLeftText("TC:" + mReceiptDataModel.certif);
                receiptBitmap.drawLeftText("App Id:" + mReceiptDataModel.appId);
                receiptBitmap.drawLeftText("App Name:" + mReceiptDataModel.appName);
                receiptBitmap.drawLeftText("TVR:" + mReceiptDataModel.tvr.trim() +" TSI:" + mReceiptDataModel.tsi.trim());
            }

            if (mReceiptDataModel.isPinVarifed.equalsIgnoreCase("true"))
            {
                receiptBitmap.drawLeftText("----------------------------------------------------------");
                receiptBitmap.drawLeftText("");
                receiptBitmap.drawCenterText("Pin Verified Ok");
                receiptBitmap.drawLeftText("");
                receiptBitmap.drawLeftText("----------------------------------------------------------");
            }
            else {

                if (isPrintSignatureRequired) {

                    if (bitmap != null)
                        receiptBitmap.drawSignaturImage(bitmap);
                }
                else {

                    receiptBitmap.drawLeftText("               ");

                    receiptBitmap.drawLeftText("Signature");
                    receiptBitmap.drawLineText("");
                    receiptBitmap.drawLineText("");
                    receiptBitmap.drawLeftText("----------------------------------------------------------");
                }
            }

            receiptBitmap.drawLeftText(mReceiptDataModel.cardHolderName);
            receiptBitmap.setTextSize(15);

            receiptBitmap.drawLeftText("I AGREE TO PAY THE ABOVE TOTAL AMOUNT");
            receiptBitmap.drawLeftText("ACCORDING TO THE CARD ISSUER AGREEMENT");
            receiptBitmap.drawLeftText("----------------------------------------------------------");

            if(isCustomerCopy){

                receiptBitmap.drawCenterText("*** CUSTOMER COPY ***");
            }
            else {
                receiptBitmap.drawCenterText("*** MERCHANT COPY ***");
            }

            receiptBitmap.drawCenterText("Version No :"+mReceiptDataModel.appVersion);

            if(!mReceiptDataModel.saleType.equals(""))
                receiptBitmap.drawCenterText("Settlement Bank : " + mReceiptDataModel.bankName);

            receiptBitmap.drawLineText("            ");
            receiptBitmap.drawLineText("            ");

            Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();
            int canvasHeight = receiptBitmap.getReceiptHeight();

            Bitmap croppedBitmap = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasHeight);

            Bitmap croppedBmp = Bitmap.createBitmap(croppedBitmap, 0, 0,
                    croppedBitmap.getWidth(), croppedBitmap.getHeight());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageCommand = MSWisepadDeviceController.getPrintData(croppedBmp, 150);
            baos.write(imageCommand, 0, imageCommand.length);

            if(ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,"end of reciept",true,true);

            return baos.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public byte[] genEMIReceipt4(ReceiptData aReceiptDataModel,  Bitmap bitmap, boolean isPrintSignatureRequired, TYPE type, boolean isCustomerCopy) {


        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"genReceipt4",true,true);

        ReceiptData mReceiptDataModel = aReceiptDataModel;
        boolean PrintSignatureRequired = isPrintSignatureRequired;
        boolean CustomerCopy = isCustomerCopy;

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ms_mswipe2);

        if (bitmap != null) {

            bitmapBytes = convertBitmap(bitmap, mswipeTargetWidth, 160);
        }else{

            this.isPrintSignatureRequired = false;
        }

        //Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/english/Courier.ttf");
        Typeface tf = Typeface.SERIF;

        double emiPerMonthAmountDouble = Double.parseDouble(mReceiptDataModel.emiPerMonthAmount);
        double totalPayAmountDouble = Double.parseDouble(mReceiptDataModel.total_Pay_Amount);



        try {


            ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
            receiptBitmap.init(2000);

            receiptBitmap.setTextSize(40);
            receiptBitmap.setTypeface(Typeface.create(tf, Typeface.BOLD));

            receiptBitmap.drawLeftText("");
            receiptBitmap.drawLeftText("");

            receiptBitmap.drawImage(icon);

            receiptBitmap.setTextSize(19);
            //receiptBitmap.drawCenterText( mReceiptDataModel.bankName);
            receiptBitmap.drawCenterText( mReceiptDataModel.merchantName);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,"address  "+ mReceiptDataModel.merchantAdd,true,true);


            for (String add : mReceiptDataModel.merchantAdd.split("<br />")) {

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName,"address  "+add,true,true);
                receiptBitmap.setTextSize(16);
                receiptBitmap.drawCenterText( add);
            }

            receiptBitmap.setTextSize(19);
            receiptBitmap.drawLeftText("-----------------------------------------------------------");
            String tempDate = removeampm(mReceiptDataModel.dateTime);
            String dateTime = Constants.getDateWithFormate(tempDate, "dd-MMMM-yyyy HH:mm", "dd MMM yyyy hh:mm a");

            receiptBitmap.drawLeftText("Date/Time: " + dateTime.toUpperCase());


            String mMidTid = "MID:" + mReceiptDataModel.mId + "   TID:"+ mReceiptDataModel.tId;
            String mBatchInvoide = "Batch no:" +mReceiptDataModel.batchNo + "   Inv. no:"+mReceiptDataModel.invoiceNo.replace("\n","");

            if(!mReceiptDataModel.tId.equals("") || !mReceiptDataModel.mId.equals(""))
            {
                receiptBitmap.drawLeftText(mMidTid);
            }

            //receiptBitmap.drawLeftText("Batch No: " + mReceiptDataModel.batchNo +  "Inv. No:" + mReceiptDataModel.invoiceNo.replace("\n",""));

            receiptBitmap.drawLeftText(mBatchInvoide);


            if(!mReceiptDataModel.saleType.equals(""))
                receiptBitmap.drawLeftText("Bill No: " + mReceiptDataModel.billNo);
            else
                receiptBitmap.drawLeftText("Bill No: " + mReceiptDataModel.billNo);

            if (mReceiptDataModel.cashAmount.length() > 0 && mReceiptDataModel.tipAmount.length()<= 0 && mReceiptDataModel.baseAmount.length()<= 0) {
                mReceiptDataModel.saleType = "Cash Only"; }

			/*if(!mReceiptDataModel.saleType.equals(""))
				drawCenterText(canvas,paintText,mReceiptDataModel.saleType.toUpperCase());
			else
			drawCenterText(canvas,paintText,mReceiptDataModel.trxType.toUpperCase());*/
            receiptBitmap.drawCenterText("EMI");



            receiptBitmap.drawCenterText("  ");


            receiptBitmap.drawLeftText( "Card No:" + mReceiptDataModel.cardNo+" "+mReceiptDataModel.trxType);
            receiptBitmap.drawLeftText( "Card Type:" + mReceiptDataModel.cardType);
            receiptBitmap.drawLeftText( "Exp Dt:" + "xx/xx");
            receiptBitmap.drawLeftText("Appr cd:" + mReceiptDataModel.authCode +" RRN:" + mReceiptDataModel.rrNo);


            if (mReceiptDataModel.trxType.equalsIgnoreCase("Chip")) {


                receiptBitmap.drawLeftText("-----------------------------------------------------------");
                receiptBitmap.drawLeftText("TC:" + mReceiptDataModel.certif);
                receiptBitmap.drawLeftText("App Id:" + mReceiptDataModel.appId);
                receiptBitmap.drawLeftText("App Name:" + mReceiptDataModel.appName);
                receiptBitmap.drawLeftText("TVR:" + mReceiptDataModel.tvr.trim() +" TSI:" + mReceiptDataModel.tsi.trim());
                receiptBitmap.drawLeftText("-----------------------------------------------------------");
            }


            receiptBitmap.drawLeftText("EMI Txn ID:  " + mReceiptDataModel.billNo);
            receiptBitmap.drawLeftText("Tenure: " + mReceiptDataModel.noOfEmi + " Months");
            receiptBitmap.drawLeftText("Card Issuer: " + mReceiptDataModel.cardIssuer);
            receiptBitmap.drawLeftText("Base Amount : "+ mReceiptDataModel.currency+" "+ mReceiptDataModel.totalAmount);
            receiptBitmap.drawLeftText("Applicable Intr.Rate(P.A.): " + mReceiptDataModel.interestRate + "%");
            receiptBitmap.drawLeftText("EMI Amt: "+ (mReceiptDataModel.currency + " " + df.format(emiPerMonthAmountDouble)));
            receiptBitmap.drawLeftText("Total Amt Payable: "+ (mReceiptDataModel.currency + " " + df.format(totalPayAmountDouble)));
            receiptBitmap.drawLeftText("-----------------------------------------------------------");
            receiptBitmap.drawCenterText("                   ");
            receiptBitmap.drawCenterText("CUSTOMER CONSENT FOR EMI");

            receiptBitmap.drawLeftText("1. I have been offered the choice of ");
            receiptBitmap.drawLeftText("normal as  well as EMI for this purchase");
            receiptBitmap.drawLeftText(" and I have chosen EMI");

            receiptBitmap.drawLeftText("2. I have fully understood and accept ");
            receiptBitmap.drawLeftText("the terms of EMI scheme and applicable ");
            receiptBitmap.drawLeftText("charges mentioned  in this charge-slip");

            receiptBitmap.drawLeftText("3. EMI conversion subject to banks ");
            receiptBitmap.drawLeftText("discretion");

            receiptBitmap.drawLeftText("4. GST applicable on Interest and ");
            receiptBitmap.drawLeftText("Processing    fees");
            receiptBitmap.drawLeftText("-----------------------------------------------------------");
            receiptBitmap.drawLeftText("Base Amt: " + mReceiptDataModel.currency + " " + mReceiptDataModel.baseAmount);
            receiptBitmap.drawLeftText("Total Amt Payable (Incl. Interest): ");
            receiptBitmap.drawLeftText(mReceiptDataModel.currency + " " + df.format(totalPayAmountDouble));


            if (mReceiptDataModel.isPinVarifed.equalsIgnoreCase("true"))
            {
                receiptBitmap.drawLeftText("----------------------------------------------------------");
                receiptBitmap.drawLeftText("");
                receiptBitmap.drawCenterText("Pin Verified Ok");
                receiptBitmap.drawLeftText("");
                receiptBitmap.drawLeftText("----------------------------------------------------------");
            }
            else {

                if (isPrintSignatureRequired) {

                    if (bitmap != null)
                        receiptBitmap.drawSignaturImage(bitmap);



                }else {



                    receiptBitmap.drawLeftText("               ");

                    receiptBitmap.drawLeftText("Signature");
                    receiptBitmap.drawLineText("");
                    receiptBitmap.drawLineText("");
                    receiptBitmap.drawLeftText("----------------------------------------------------------");
                }


            }


            receiptBitmap.drawLeftText(mReceiptDataModel.cardHolderName);

            receiptBitmap.setTextSize(15);

            receiptBitmap.drawLeftText("I AGREE TO PAY THE ABOVE TOTAL AMOUNT");
            receiptBitmap.drawLeftText("ACCORDING TO THE CARD ISSUER AGREEMENT");
            receiptBitmap.drawLeftText("----------------------------------------------------------");

            if(isCustomerCopy){

                receiptBitmap.drawCenterText("*** CUSTOMER COPY ***");

            }
            else {
                receiptBitmap.drawCenterText("*** MERCHANT COPY ***");

            }

            receiptBitmap.drawCenterText("Version No :"+mReceiptDataModel.appVersion);

            if(!mReceiptDataModel.saleType.equals(""))
                receiptBitmap.drawCenterText("Settlement Bank : " + mReceiptDataModel.bankName);

            receiptBitmap.drawLineText("            ");
            receiptBitmap.drawLineText("            ");

            Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();
            int canvasHeight = receiptBitmap.getReceiptHeight();

            Bitmap croppedBitmap = Bitmap.createBitmap(canvasbitmap, 0, 0, canvasbitmap.getWidth(), canvasHeight);

            Bitmap croppedBmp = Bitmap.createBitmap(croppedBitmap, 0, 0,
                    croppedBitmap.getWidth(), croppedBitmap.getHeight());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageCommand = MSWisepadDeviceController.getPrintData(croppedBmp, 150);
            baos.write(imageCommand, 0, imageCommand.length);

            if(ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,"end of reciept",true,true);

            return baos.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public byte[] genCashReceipt4(Context context, ReceiptData aReceiptDataModel,  Bitmap bitmap, boolean isPrintSignatureRequired, TYPE type, boolean isCustomerCopy) {

        if(ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,"genCashReceipt4"+isPrintSignatureRequired,true,true);

        ReceiptData mReceiptDataModel = aReceiptDataModel;
        boolean PrintSignatureRequired = isPrintSignatureRequired;
        boolean CustomerCopy = isCustomerCopy;
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ms_mswipe2);

        if (bitmap != null) {

            bitmapBytes = convertBitmap(bitmap, mswipeTargetWidth, 160);
        }else{

            this.isPrintSignatureRequired = false;
        }

        Typeface tf = Typeface.SERIF;
        //Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/english/Courier.ttf");


        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ReceiptBitmap receiptBitmap = new ReceiptBitmap().getInstance();
            receiptBitmap.init(900);

            receiptBitmap.setTextSize(60);
            receiptBitmap.setTypeface(Typeface.create(tf, Typeface.BOLD));

            receiptBitmap.drawLeftText("");
            receiptBitmap.drawLeftText("");

            receiptBitmap.drawImage(icon);

            receiptBitmap.setTextSize(19);
            //receiptBitmap.drawCenterText( mReceiptDataModel.bankName);
            receiptBitmap.drawCenterText( mReceiptDataModel.merchantName);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,"address  "+ mReceiptDataModel.merchantAdd,true,true);

            for (String add : mReceiptDataModel.merchantAdd.split("<br />")) {

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName,"address  "+add,true,true);

                receiptBitmap.drawCenterText( add);
            }


            receiptBitmap.drawLeftText("----------------------------------------------------------");


            String tempDate = removeampm(mReceiptDataModel.dateTime);
            String dateTime = Constants.getDateWithFormate(tempDate, "dd-MMMM-yyyy HH:mm", "dd MMM yyyy hh:mm a");

            receiptBitmap.drawLeftText("Date/Time: " + dateTime.toUpperCase());

            receiptBitmap.drawLeftText( "Inv. No:" + mReceiptDataModel.invoiceNo.replace("\n",""));

            receiptBitmap.drawLeftText("REF. NO.:" + mReceiptDataModel.refNo);


            receiptBitmap.drawCenterText( mReceiptDataModel.trxType);

            receiptBitmap.drawLeftText("TOTAL AMOUNT:   " + mReceiptDataModel.currency + " " +String.format("%.2f",(Double.parseDouble(mReceiptDataModel.totalAmount))));
            receiptBitmap.drawLineText( "");
            receiptBitmap.drawLineText( "");

            if (isPrintSignatureRequired) {


                if (bitmap!= null)
                    receiptBitmap.drawSignaturImage(bitmap);


            }
            else {

                receiptBitmap.drawLeftText("               ");

                receiptBitmap.drawLeftText("Signature");
                receiptBitmap.drawLineText("");
                receiptBitmap.drawLineText("");
                receiptBitmap.drawLeftText("----------------------------------------------------------");

            }

            receiptBitmap.setTextSize(15);
            receiptBitmap.drawLeftText("----------------------------------------------------------");
            receiptBitmap.drawLeftText("I AGREE TO PAY THE ABOVE TOTAL AMOUNT");
            receiptBitmap.drawLeftText("ACCORDING TO THE CARD ISSUER AGREEMENT");
            receiptBitmap.drawLeftText("----------------------------------------------------------");

            if(isCustomerCopy){

                receiptBitmap.drawCenterText("*** CUSTOMER COPY ***");
            }
            else {
                receiptBitmap.drawCenterText("*** MERCHANT COPY ***");
            }


            receiptBitmap.drawCenterText("Version No :"+mReceiptDataModel.appVersion);
            if(!mReceiptDataModel.saleType.equals(""))
                receiptBitmap.drawCenterText("Settlement Bank : " + mReceiptDataModel.bankName);


            receiptBitmap.drawLineText("");
            receiptBitmap.drawLineText("");
            receiptBitmap.drawLineText("");
            receiptBitmap.drawLineText("");

            Bitmap canvasbitmap = receiptBitmap.getReceiptBitmap();
            Bitmap croppedBmp = Bitmap.createBitmap(canvasbitmap, 0, 0,
                    canvasbitmap.getWidth(), canvasbitmap.getHeight());

            byte[] imageCommand = MSWisepadDeviceController.getPrintData(croppedBmp, 150);
            baos.write(imageCommand, 0, imageCommand.length);

            if(ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,"end of reciept",true,true);

            return baos.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private String removeampm(String aDate)
    {
        //as the date issue from the service level we are removing the am/pm from the date
        //we are getting with 24 form include am/pm which createing the error while converting.

        aDate.replaceAll("am", "");
        aDate.replaceAll("AM", "");
        aDate.replaceAll("pm", "");
        aDate.replaceAll("PM", "");

        return aDate.trim();

    }


    private  byte[] hexToByteArray(String s) {
        if(s == null) {
            s = "";
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for(int i = 0; i < s.length() - 1; i += 2) {
            String data = s.substring(i, i + 2);
            bout.write(Integer.parseInt(data, 16));
        }
        return bout.toByteArray();
    }

    private  byte[] convertBitmap(Bitmap bitmap, int targetWidth, int threshold) {
        int targetHeight = (int)Math.round((double)targetWidth / (double)bitmap.getWidth() * (double)bitmap.getHeight());

        byte[] pixels = new byte[targetWidth * targetHeight];
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
        for(int j = 0; j < scaledBitmap.getHeight(); ++j) {
            for(int i = 0; i < scaledBitmap.getWidth(); ++i) {
                int pixel = scaledBitmap.getPixel(i, j);
                int alpha = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                if(alpha < 50) {
                    pixels[i + j * scaledBitmap.getWidth()] = 0;
                } else if((r + g + b) / 3 >= threshold) {
                    pixels[i + j * scaledBitmap.getWidth()] = 0;
                } else {
                    pixels[i + j * scaledBitmap.getWidth()] = 1;
                }
            }
        }

        byte[] output = new byte[scaledBitmap.getWidth() * (int)Math.ceil((double)scaledBitmap.getHeight() / (double)8)];

        for(int i = 0; i < scaledBitmap.getWidth(); ++i) {
            for(int j = 0; j < (int)Math.ceil((double)scaledBitmap.getHeight() / (double)8); ++j) {
                for(int n = 0; n < 8; ++n) {
                    if(j * 8 + n < scaledBitmap.getHeight()) {
                        output[i + j * scaledBitmap.getWidth()] |= pixels[i + (j * 8 + n) * scaledBitmap.getWidth()] << (7 - n);
                    }
                }
            }
        }

        return output;
    }

}
