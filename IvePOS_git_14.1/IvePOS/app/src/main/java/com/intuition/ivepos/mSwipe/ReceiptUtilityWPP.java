package com.intuition.ivepos.mSwipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.intuition.ivepos.R;
import com.mswipetech.wisepad.sdk.data.ReceiptData;
import com.socsi.smartposapi.DeviceMaster;
import com.socsi.smartposapi.printer.Align;
import com.socsi.smartposapi.printer.FontLattice;
import com.socsi.smartposapi.printer.Printer2;
import com.socsi.smartposapi.printer.TextEntity;

import java.text.DecimalFormat;

public class ReceiptUtilityWPP {

    private DecimalFormat df = new DecimalFormat(".00");
    private Context context;
    private ReceiptData mReceiptDataModel = null;
    private boolean isPrintSignatureRequired = false;
    private boolean isCustomerCopy = false;

    public enum TYPE {
        CARD, EMI, CASH
    }

    public ReceiptUtilityWPP(Context context) {

        this.context = context;

        try{
            DeviceMaster.getInstance().init(context);
        }catch (Exception e){

        }

    }

    public Printer2 printReceipt(ReceiptData aReceiptDataModel, Bitmap bitmap, boolean isPrintSignatureRequired, TYPE type , boolean isCustomerCopy) {

        this.mReceiptDataModel = aReceiptDataModel;
        this.isPrintSignatureRequired = isPrintSignatureRequired;
        this.isCustomerCopy = isCustomerCopy;

        if (type == TYPE.CARD) {
            return PrintReceipt(bitmap);
        } else if (type == TYPE.EMI) {
            return PrintReceiptEMI(bitmap);
        } else if (type == TYPE.CASH) {
            return PrintReceipt(bitmap);
        } else {
            return null;
        }
    }


    public Printer2 printLTReceipt(ReceiptData aReceiptDataModel, Bitmap bitmap, boolean isPrintSignatureRequired, TYPE type , boolean isCustomerCopy) {

        this.mReceiptDataModel = aReceiptDataModel;
        this.isPrintSignatureRequired = isPrintSignatureRequired;
        this.isCustomerCopy = isCustomerCopy;

        if (type == TYPE.CARD) {
            return PrintReceiptLT(bitmap);
        } else if (type == TYPE.EMI) {
            return PrintReceiptEMI(bitmap);
        } else if (type == TYPE.CASH) {
            return PrintReceiptLT(bitmap);
        } else {
            return null;
        }
    }

    private Printer2 PrintReceiptEMI(Bitmap signature) {
        ReceiptData mReceiptData = mReceiptDataModel;
        final Context mContext = context;
        Printer2 print = Printer2.getInstance();
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ms_mswipe2);
        double emiPerMonthAmountDouble = Double.parseDouble(mReceiptDataModel.emiPerMonthAmount);
        double totalPayAmountDouble = Double.parseDouble(mReceiptDataModel.total_Pay_Amount);

        print.appendImage(icon, Align.CENTER);
        print.setFontsDefineByApp(context.getAssets(), null, "fonts/english/Courier.ttf");
        print.appendTextEntity2(new TextEntity(mReceiptData.merchantName, FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
        for (String add : mReceiptData.merchantAdd.split("<br />")) {
            print.appendTextEntity2(new TextEntity(add, FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        }
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("Date/Time: " + mReceiptData.dateTime, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.tId.equals("") || !mReceiptData.mId.equals(""))
            print.appendTextEntity2(new TextEntity("MID: " + mReceiptData.mId + " TID: " + mReceiptData.tId, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Batch No:"+mReceiptData.batchNo+" Invoice No: " + mReceiptData.refNo.replace("\n",""), FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Invoice No: " + mReceiptData.refNo.replace("\n",""), FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Bill No: " + mReceiptData.billNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Bill No: " + mReceiptData.billNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if (mReceiptDataModel.cashAmount.length() > 0 && mReceiptDataModel.tipAmount.length()<= 0 && mReceiptDataModel.baseAmount.length()<= 0) {
            mReceiptDataModel.saleType = "Cash Only";
        }
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity(mReceiptData.saleType, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity(mReceiptData.trxType, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Card No: " + mReceiptData.cardNo + " " + mReceiptData.trxType, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Card Type: " + mReceiptData.cardType + " Exp Date: " + "xx/xx", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));

        if(!mReceiptData.authCode.equals("") || !mReceiptData.rrNo.equals(""))
            print.appendTextEntity2(new TextEntity("Appr cd: " + mReceiptData.authCode + " RR No: " + mReceiptData.rrNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));

        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        if(mReceiptDataModel.trxType.equalsIgnoreCase("Chip")) {
            print.appendTextEntity2(new TextEntity("TC: " + mReceiptData.certif, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
            print.appendTextEntity2(new TextEntity("App ID: " + mReceiptData.appId, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
            print.appendTextEntity2(new TextEntity("App Name: " + mReceiptData.appName, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
            print.appendTextEntity2(new TextEntity("TVR: " + mReceiptData.tvr + " TSI: " + mReceiptData.tsi, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));


            if(mReceiptData.saleType.equalsIgnoreCase("void"))
            {
                print.appendTextEntity2(new TextEntity("Transaction Voided Successfully", FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
            }
        }
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("EMI Txn ID:  " + mReceiptData.billNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("Tenure: " + mReceiptData.noOfEmi + " Months", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("Card Issuer: " + mReceiptData.cardIssuer, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("Base Amount : "+ mReceiptData.currency+" "+ mReceiptData.totalAmount, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("Applicable Intr.Rate(P.A.): " + mReceiptDataModel.interestRate + "%", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("EMI Amt: "+ (mReceiptDataModel.currency + " " + df.format(emiPerMonthAmountDouble)), FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("Total Amt Payable: "+ (mReceiptDataModel.currency + " " + df.format(totalPayAmountDouble)), FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("CUSTOMER CONSENT FOR EMI", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));

        print.appendTextEntity2(new TextEntity("1. I have been offered the choice of normal as  well as EMI for this purchase and I have chosen EMI", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("2. I have fully understood and accept the terms of EMI scheme and applicable charges mentioned  in this charge-slip", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("3. EMI conversion subject to banks discretion", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("4. GST applicable on Interest and Processing    fees", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("Base Amt: " + mReceiptDataModel.currency + " " + mReceiptDataModel.baseAmount, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        print.appendTextEntity2(new TextEntity("Total Amt Payable (Incl. Interest): " + mReceiptDataModel.currency + " " + df.format(totalPayAmountDouble), FontLattice.TWENTY_FOUR, false, Align.LEFT, true));

        if ( isPrintSignatureRequired) {
            print.appendImage(signature, Align.CENTER);
        }

        if(mReceiptDataModel.isPinVarifed.equalsIgnoreCase("true"))
        {
            print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
            print.appendTextEntity2(new TextEntity("Pin Verified OK", FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
        }
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity(mReceiptData.cardHolderName, FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("I agree to pay the above total amount", FontLattice.EIGHTEEN, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("according to the card issuer agreement", FontLattice.EIGHTEEN, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));

        if(isCustomerCopy){
            print.appendTextEntity2(new TextEntity("****    ****   CUSTOMER COPY   ****    ****", FontLattice.SIXTEEN, true, Align.CENTER, true));
        }
        else {

            print.appendTextEntity2(new TextEntity("****    ****   MERCHANT COPY   ****    ****", FontLattice.SIXTEEN, true, Align.CENTER, true));
        }

        print.appendTextEntity2(new TextEntity("Version No: "+mReceiptData.appVersion, FontLattice.EIGHTEEN, false, Align.CENTER, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Settlement Bank : " + mReceiptData.bankName, FontLattice.EIGHTEEN, false, Align.CENTER, true));
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        return print;    }

    private Printer2 PrintReceipt(Bitmap signature)
    {
        ReceiptData mReceiptData = mReceiptDataModel;
        final Context mContext = context;
        Printer2 print = Printer2.getInstance();
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ms_mswipe2);
        print.appendImage(icon, Align.CENTER);
        print.setFontsDefineByApp(context.getAssets(), null, "fonts/english/Courier.ttf");
        print.appendTextEntity2(new TextEntity(mReceiptData.merchantName, FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
        for (String add : mReceiptData.merchantAdd.split("<br />")) {
            print.appendTextEntity2(new TextEntity(add, FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        }

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "  mReceiptData.merchantAdd "+ mReceiptData.merchantAdd, true, true);

        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("Date/Time: " + mReceiptData.dateTime, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.tId.equals("") || !mReceiptData.mId.equals(""))
            print.appendTextEntity2(new TextEntity("MID: " + mReceiptData.mId + " TID: " + mReceiptData.tId, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Batch No:"+mReceiptData.batchNo+" Invoice No: " + mReceiptData.invoiceNo.replace("\n",""), FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Invoice No: " + mReceiptData.invoiceNo.replace("\n",""), FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Ref No: " + mReceiptData.refNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Ref No: " + mReceiptData.refNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));

        if (mReceiptDataModel.cashAmount.length() > 0 && mReceiptDataModel.tipAmount.length()<= 0 && mReceiptDataModel.baseAmount.length()<= 0) {
            mReceiptDataModel.saleType = "Cash Only"; }
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity(mReceiptData.saleType, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity(mReceiptData.trxType, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Card No: " + mReceiptData.cardNo + " " + mReceiptData.trxType, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Card Type: " + mReceiptData.cardType + " Exp Date: " + "xx/xx", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.baseAmount.equals(""))
            print.appendTextEntity2(new TextEntity("Total Amount : "+ mReceiptData.currency+" "+ mReceiptData.totalAmount, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Total Amount : "+ mReceiptData.currency+" "+ mReceiptData.totalAmount, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        if(!mReceiptData.authCode.equals("") || !mReceiptData.rrNo.equals(""))
            print.appendTextEntity2(new TextEntity("Appr cd: " + mReceiptData.authCode + " RR No: " + mReceiptData.rrNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));

        if ( isPrintSignatureRequired && !mReceiptDataModel.isPinVarifed.equalsIgnoreCase("true")) {
            print.appendImage(signature, Align.CENTER);
        }
        else
        {
            if(mReceiptDataModel.trxType.equalsIgnoreCase("Chip")) {
                print.appendTextEntity2(new TextEntity("TC: " + mReceiptData.certif, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
                print.appendTextEntity2(new TextEntity("App ID: " + mReceiptData.appId, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
                print.appendTextEntity2(new TextEntity("App Name: " + mReceiptData.appName, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
                print.appendTextEntity2(new TextEntity("TVR: " + mReceiptData.tvr + " TSI: " + mReceiptData.tsi, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));


                if(mReceiptData.saleType.equalsIgnoreCase("void"))
                {
                    print.appendTextEntity2(new TextEntity("Transaction Voided Successfully", FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
                }
            }
        }
        if(mReceiptDataModel.isPinVarifed.equalsIgnoreCase("true"))
        {
            print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
            print.appendTextEntity2(new TextEntity("Pin Verified OK", FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
        }
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity(mReceiptData.cardHolderName, FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("I agree to pay the above total amount", FontLattice.EIGHTEEN, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("according to the card issuer agreement", FontLattice.EIGHTEEN, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        //print.appendTextEntity2(new TextEntity("****    ****   CUSTOMER COPY   ****    ****", FontLattice.TWENTY_FOUR, true, Align.CENTER, true));

        if(isCustomerCopy){
            print.appendTextEntity2(new TextEntity("****    ****   CUSTOMER COPY   ****    ****", FontLattice.SIXTEEN, true, Align.CENTER, true));
        }
        else {

            print.appendTextEntity2(new TextEntity("****    ****   MERCHANT COPY   ****    ****", FontLattice.SIXTEEN, true, Align.CENTER, true));
        }
        print.appendTextEntity2(new TextEntity("Version No: "+mReceiptData.appVersion, FontLattice.EIGHTEEN, false, Align.CENTER, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Settlement Bank : " + mReceiptData.bankName, FontLattice.EIGHTEEN, false, Align.CENTER, true));
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        return print;

    }

    private Printer2 PrintReceiptLT(Bitmap signature)
    {
        ReceiptData mReceiptData = mReceiptDataModel;
        final Context mContext = context;
        Printer2 print = Printer2.getInstance();
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ms_mswipe2);
        print.appendImage(icon, Align.CENTER);
        print.setFontsDefineByApp(context.getAssets(), null, "fonts/english/Courier.ttf");
        print.appendTextEntity2(new TextEntity(mReceiptData.merchantName, FontLattice.TWENTY_FOUR, true, Align.CENTER, true));

        for (String add : mReceiptData.merchantAdd.split("<br />")) {
            print.appendTextEntity2(new TextEntity(add, FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        }

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "  mReceiptData.merchantAdd "+ mReceiptData.merchantAdd, true, true);

        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("Date/Time: " + mReceiptData.dateTime, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        if(!mReceiptData.tId.equals("") || !mReceiptData.mId.equals(""))
            print.appendTextEntity2(new TextEntity("MID: " + mReceiptData.mId + " TID: " + mReceiptData.tId, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Batch No:"+mReceiptData.batchNo+" Invoice No: " + mReceiptData.invoiceNo.replace("\n",""), FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Invoice No: " + mReceiptData.invoiceNo.replace("\n",""), FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Ref No: " + mReceiptData.voucherNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Ref No: " + mReceiptData.voucherNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if (mReceiptDataModel.cashAmount.length() > 0 && mReceiptDataModel.tipAmount.length()<= 0 && mReceiptDataModel.baseAmount.length()<= 0) {
            mReceiptDataModel.saleType = "Cash Only"; }
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity(mReceiptData.saleType, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity(mReceiptData.trxType, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Card No: " + mReceiptData.cardNo + " " + mReceiptData.trxType, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Card Type: " + mReceiptData.cardType + " Exp Date: " + "xx/xx", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
        if(!mReceiptData.baseAmount.equals(""))
            print.appendTextEntity2(new TextEntity("Total Amount : "+ mReceiptData.currency+" "+ mReceiptData.baseAmount, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        else
            print.appendTextEntity2(new TextEntity("Total Amount : "+ mReceiptData.currency+" "+ mReceiptData.totalAmount, FontLattice.TWENTY_FOUR, true, Align.LEFT, true));
        if(!mReceiptData.authCode.equals("") || !mReceiptData.rrNo.equals(""))
            print.appendTextEntity2(new TextEntity("Appr cd: " + mReceiptData.authCode + " RR No: " + mReceiptData.rrNo, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));

        if ( isPrintSignatureRequired && !mReceiptDataModel.isPinVarifed.equalsIgnoreCase("true")) {
            print.appendImage(signature, Align.CENTER);
        }
        else
        {
            if(mReceiptDataModel.trxType.equalsIgnoreCase("Chip")) {
                print.appendTextEntity2(new TextEntity("TC: " + mReceiptData.certif, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
                print.appendTextEntity2(new TextEntity("App ID: " + mReceiptData.appId, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
                print.appendTextEntity2(new TextEntity("App Name: " + mReceiptData.appName, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
                print.appendTextEntity2(new TextEntity("TVR: " + mReceiptData.tvr + " TSI: " + mReceiptData.tsi, FontLattice.TWENTY_FOUR, false, Align.LEFT, true));


                if(mReceiptData.saleType.equalsIgnoreCase("void"))
                {
                    print.appendTextEntity2(new TextEntity("Transaction Voided Successfully", FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
                }
            }
        }

        if(mReceiptDataModel.isPinVarifed.equalsIgnoreCase("true"))
        {
            print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
            print.appendTextEntity2(new TextEntity("Pin Verified OK", FontLattice.TWENTY_FOUR, true, Align.CENTER, true));
        }

        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity(mReceiptData.cardHolderName, FontLattice.TWENTY_FOUR, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("I agree to pay the above total amount", FontLattice.EIGHTEEN, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("according to the card issuer agreement", FontLattice.EIGHTEEN, false, Align.CENTER, true));
        print.appendTextEntity2(new TextEntity("--------------------------------", FontLattice.TWENTY_FOUR, false, Align.CENTER, true));

        if(isCustomerCopy){
            print.appendTextEntity2(new TextEntity("****    ****   CUSTOMER COPY   ****    ****", FontLattice.SIXTEEN, true, Align.CENTER, true));
        }
        else {

            print.appendTextEntity2(new TextEntity("****    ****   MERCHANT COPY   ****    ****", FontLattice.SIXTEEN, true, Align.CENTER, true));
        }
        print.appendTextEntity2(new TextEntity("Version No: "+mReceiptData.appVersion, FontLattice.EIGHTEEN, false, Align.CENTER, true));
        if(!mReceiptData.saleType.equals(""))
            print.appendTextEntity2(new TextEntity("Settlement Bank : " + mReceiptData.bankName, FontLattice.EIGHTEEN, false, Align.CENTER, true));

        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        appendTextEntity();
        return print;

    }


    private void appendTextEntity() {
        Printer2.getInstance().appendTextEntity2(new TextEntity("", FontLattice.TWENTY_FOUR, false, Align.LEFT, true));
    }

}
