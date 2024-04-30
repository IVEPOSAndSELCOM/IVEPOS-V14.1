package com.intuition.ivepos.mSwipe;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ReceiptDataModel implements Parcelable {
	
	public String bankName= "";
	public String merchantName= "";
	public String merchantAdd= "";
	public String dateTime= "";
	public String mId= "";
	public String tId= "";
	public String batchNo= "";
	public String voucherNo= "";
	public String refNo= "";
	public String saleType= "";
	public String cardNo= "";
	public String trxType= "";
	public String cardType= "";
	public String expDate= "";
	public String emvSigExpDate= "";
	public String cardHolderName= "";
	public String currency= "";
	public String cashAmount= "";
	public String baseAmount= "";
	public String tipAmount= "";
	public String totalAmount= "";
	public String authCode= "";
	public String rrNo= "";
	public String certif= "";
	public String appId= "";
	public String appName= "";
	public String tvr= "";
	public String tsi= "";
	public String appVersion= "";
	public String isPinVarifed= "";
	public String stan= "";
	public String cardIssuer="";
	public String emiPerMonthAmount="";
	public String total_Pay_Amount = "";
	public String noOfEmi = "";
	public String interestRate = "";
	public String billNo = "";
	public String firstDigitsOfCard = "";

	public String isConvenceFeeEnable = "false";
	
	//for cash and bank

	public String invoiceNo= "";
	public String trxDate= "";
	public String trxImgDate= "";
	public String chequeDate= "";
	public String chequeNo= "";
	
	
	 public static final Creator<ReceiptDataModel> CREATOR = new Creator<ReceiptDataModel>() {
	    	public ReceiptDataModel createFromParcel(Parcel source) {
	    		
	    		ReceiptDataModel receiptDataModel = new ReceiptDataModel();  
	    		receiptDataModel.bankName = source.readString(); 
	    		receiptDataModel.merchantName = source.readString(); 
	    		receiptDataModel.merchantAdd = source.readString(); 
	    		receiptDataModel.dateTime = source.readString(); 
	    		receiptDataModel.mId = source.readString(); 
	    		receiptDataModel.tId = source.readString(); 
	    		receiptDataModel.batchNo = source.readString(); 
	    		receiptDataModel.voucherNo = source.readString(); 
	    		receiptDataModel.refNo = source.readString(); 
	    		receiptDataModel.saleType = source.readString(); 
	    		receiptDataModel.cardNo = source.readString(); 
	    		receiptDataModel.trxType = source.readString(); 
	    		receiptDataModel.cardType = source.readString(); 
	    		receiptDataModel.expDate = source.readString(); 
	    		receiptDataModel.emvSigExpDate = source.readString(); 
	    		receiptDataModel.cardHolderName = source.readString(); 
	    		receiptDataModel.currency = source.readString(); 
	    		receiptDataModel.cashAmount = source.readString(); 
	    		receiptDataModel.baseAmount = source.readString(); 
	    		receiptDataModel.tipAmount = source.readString(); 
	    		receiptDataModel.totalAmount = source.readString(); 
	    		receiptDataModel.authCode = source.readString(); 
	    		receiptDataModel.rrNo = source.readString(); 
	    		receiptDataModel.certif = source.readString(); 
	    		receiptDataModel.appId = source.readString(); 
	    		receiptDataModel.appName = source.readString(); 
	    		receiptDataModel.tvr = source.readString(); 
	    		receiptDataModel.tsi = source.readString(); 
	    		receiptDataModel.appVersion = source.readString(); 
	    		receiptDataModel.isPinVarifed = source.readString(); 
	    		receiptDataModel.stan = source.readString(); 
	    		receiptDataModel.cardIssuer = source.readString();
	    		receiptDataModel.emiPerMonthAmount = source.readString();
	    		receiptDataModel.total_Pay_Amount = source.readString(); 
	    		receiptDataModel.noOfEmi  = source.readString(); 
	    		receiptDataModel.interestRate  = source.readString(); 
	    		receiptDataModel.billNo  = source.readString(); 
	    		receiptDataModel.firstDigitsOfCard  = source.readString();
				receiptDataModel.isConvenceFeeEnable = source.readString();
	    		
	    		//for cash and bank
	    		
	    		receiptDataModel.invoiceNo = source.readString(); 
	    		receiptDataModel.trxDate = source.readString(); 
	    		receiptDataModel.trxImgDate = source.readString(); 
	    		receiptDataModel.chequeDate = source.readString(); 
	    		receiptDataModel.chequeNo = source.readString();

	    			
	    		return receiptDataModel;  
	    		
	    	}  
	    	public ReceiptDataModel[] newArray(int size) {  

	    		Log.e("", " ********* newArray");
	    		return new ReceiptDataModel[size];  
	    	}  
	    };  
	    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// TODO Auto-generated method stub
		
	    parcel.writeString(bankName); 
		parcel.writeString(merchantName); 
		parcel.writeString(merchantAdd); 
		parcel.writeString(dateTime); 
		parcel.writeString(mId); 
		parcel.writeString(tId); 
		parcel.writeString(batchNo); 
		parcel.writeString(voucherNo); 
		parcel.writeString(refNo); 
		parcel.writeString(saleType); 
		parcel.writeString(cardNo); 
		parcel.writeString(trxType); 
		parcel.writeString(cardType); 
		parcel.writeString(expDate); 
		parcel.writeString(emvSigExpDate); 
		parcel.writeString(cardHolderName); 
		parcel.writeString(currency); 
		parcel.writeString(cashAmount); 
		parcel.writeString(baseAmount); 
		parcel.writeString(tipAmount); 
		parcel.writeString(totalAmount); 
		parcel.writeString(authCode); 
		parcel.writeString(rrNo); 
		parcel.writeString(certif); 
		parcel.writeString(appId); 
		parcel.writeString(appName); 
		parcel.writeString(tvr); 
		parcel.writeString(tsi); 
		parcel.writeString(appVersion); 
		parcel.writeString(isPinVarifed); 
		parcel.writeString(stan); 
		parcel.writeString(cardIssuer);
		parcel.writeString(emiPerMonthAmount);
		parcel.writeString(total_Pay_Amount); 
		parcel.writeString(noOfEmi ); 
		parcel.writeString(interestRate); 
		parcel.writeString(billNo ); 
		parcel.writeString(firstDigitsOfCard);
		parcel.writeString(isConvenceFeeEnable);
		
		//for cash and bank

		parcel.writeString(invoiceNo); 
		parcel.writeString(trxDate); 
		parcel.writeString(trxImgDate); 
		parcel.writeString(chequeDate); 
		parcel.writeString(chequeNo);


		
		
	}
}
