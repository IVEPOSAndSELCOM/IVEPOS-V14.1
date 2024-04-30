package com.intuition.ivepos.wisepos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.intuition.ivepos.mSwipe.ApplicationData;
import com.intuition.ivepos.mSwipe.Logs;


public class ReceiptBitmap {


	int ypos = 0;
	int xpos = 0;
	int yoffset = 12;

	Bitmap canvasbitmap = null;
	Canvas canvas = null;
	Paint paintText = null;

	ReceiptBitmap mReceiptBitmap = null;

	public ReceiptBitmap getInstance()
	{

		if(mReceiptBitmap == null){
			mReceiptBitmap = new ReceiptBitmap();
		}

		return mReceiptBitmap;
	}

	public void init(int size) {

		Typeface tf = Typeface.SERIF;

		canvasbitmap = Bitmap.createBitmap(384, size, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(canvasbitmap);
		canvas.drawARGB(255, 255, 255, 255);

		paintText = new Paint();
		paintText.setColor(Color.parseColor("#FF000000"));
		paintText.setTypeface(Typeface.create(tf, Typeface.BOLD));
		paintText.setAntiAlias(true);

		paintText.setTextSize(40);

	}

	public void setTextSize(int size) {
		paintText.setTextSize(size);
	}

	public void setTypeface(Typeface typeface) {
		paintText.setTypeface(typeface);
	}

	public Bitmap getReceiptBitmap() {

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "canvasbitmap: " + canvasbitmap.getByteCount(), true, true);

		return canvasbitmap;
	}

	public int getReceiptHeight() {

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "canvasbityposmap: " + ypos, true, true);
		return ypos;
	}

	public void drawCenterText(String text)
	{
		paintText.setTextAlign(Paint.Align.CENTER);

		Rect bounds = new Rect();

		paintText.getTextBounds(text, 0, text.length(), bounds);

		ypos = ypos +  bounds.height() + yoffset;
		canvas.drawText(text, canvas.getWidth() / 2, ((paintText.descent() + paintText.ascent()) / 2) + ypos, paintText);

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "ypos: " + ypos, true, true);

	}

	public void drawNewLine()
	{
		paintText.setTextAlign(Paint.Align.CENTER);
		ypos = ypos +  30 + yoffset;
		canvas.drawText(" ", xpos, ypos, paintText);
	}

	public void drawImage(Bitmap bitmap)
	{
		canvas.drawBitmap(bitmap, 100, ((paintText.descent() + paintText.ascent()) / 2) + ypos, paintText);
		ypos = ypos +  bitmap.getHeight() + yoffset;
		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "ypos: " + ypos, true, true);

	}

	public void drawSignaturImage(Bitmap bitmap)
	{
		Bitmap scaledbitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, false);
		canvas.drawBitmap(scaledbitmap, xpos - 10, ypos, null);
		ypos = ypos +  bitmap.getHeight() + (yoffset + 30);

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "ypos: " + ypos, true, true);


	}

	public void drawLineText(String text)
	{
		paintText.setTextAlign(Paint.Align.CENTER);

		Rect bounds = new Rect();

		paintText.getTextBounds(text, 0, text.length(), bounds);

		ypos = ypos +  bounds.height() + yoffset + 20;
		canvas.drawText(text, canvas.getWidth() / 2, ((paintText.descent() + paintText.ascent()) / 2) + ypos, paintText);

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "ypos: " + ypos, true, true);


	}

	public void drawLeftText(String text)
	{
		paintText.setTextAlign(Paint.Align.LEFT);

		Rect bounds = new Rect();

		paintText.getTextBounds(text, 0, text.length(), bounds);

		ypos = ypos +  bounds.height() + yoffset;
		canvas.drawText(text, xpos, ypos, paintText);

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "ypos: " + ypos, true, true);

	}

	public void drawRightText(String text)
	{
		paintText.setTextAlign(Paint.Align.LEFT);

		Rect bounds = new Rect();

		paintText.getTextBounds(text, 0, text.length(), bounds);

		ypos = ypos +  bounds.height() + yoffset;
		xpos = 384 - bounds.width();
		canvas.drawText(text, xpos, ypos, paintText);
		xpos = 0;

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "ypos: " + ypos, true, true);

	}

	public void drawLeftAndRightText(String lefttext, String righttext)
	{
		paintText.setTextAlign(Paint.Align.LEFT);
		Rect bounds = new Rect();
		paintText.getTextBounds(lefttext, 0, lefttext.length(), bounds);

		ypos = ypos +  bounds.height() + yoffset;

		xpos = 0;
		canvas.drawText(lefttext, xpos, ypos, paintText);

		paintText.getTextBounds(righttext, 0, righttext.length(), bounds);

		xpos = 384 - bounds.width();
		canvas.drawText(righttext, xpos, ypos, paintText);

		xpos = 0;

		if (ApplicationData.IS_DEBUGGING_ON)
			Logs.v(ApplicationData.packName, "ypos: " + ypos, true, true);

	}
}