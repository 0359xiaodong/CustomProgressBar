package com.example.customui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint({ "DrawAllocation", "HandlerLeak" })
public class CustomProgressBar extends View {

	public static int BIG = 64;

	public static int MEDIUM = 48;

	public static int SMALL = 32;

	private int mainColor;
	private int textColor;
	
	private int padding;
	
	private int centerX;
	private int centerY;

	private int width;
	private int textSize;
	
	private float currentProgress;

	private int outsideRadius;
	private int insideRadius;
	private int middleRadius;

	private Paint borderPaint;
	private Paint mainPaint;
	private Paint textPaint;
	
	private boolean isSpinning;

	private String text;
	
	private RectF outside;
	private RectF middle;
	private RectF inside;
	
	private Handler spinHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			invalidate();
			if(isSpinning) {
				currentProgress+=5;
				if(currentProgress>360) {
					currentProgress = 0;
				}
				spinHandler.sendEmptyMessageDelayed(0, 10);
			}
		}
	};
	
	@SuppressLint("DrawAllocation")
	@Override
	protected synchronized void onDraw(Canvas canvas) {
					
		canvas.drawArc(outside, 0, 360, false, borderPaint);
		canvas.drawArc(inside, 0, 360, false, borderPaint);
		
		if (isSpinning){
			canvas.drawArc(middle, currentProgress, 50, false, mainPaint);
		    if(text!=null) canvas.drawText(text,centerX, centerY + 5, textPaint);
		}
		else {
			currentProgress = (float) (currentProgress * 3.6);
			canvas.drawArc(middle, 0, currentProgress, false, mainPaint);
			canvas.drawText(Integer.toString((int) (currentProgress / 3.6)),centerX, centerY + 5, textPaint);
		}
		super.onDraw(canvas);
	}
	
	public void spin() {
		isSpinning = true;
		spinHandler.sendEmptyMessage(0);
	}
	
	public void setPadding(int padding){
		this.padding = padding;
	}
	
	public void setProgress(int progress) {
		isSpinning = false;
		currentProgress = progress;
		invalidate();
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public void setTextColor(int color){
		textColor = color;
	}
	
	public void setColor(int color){
		mainColor = color;
	}
	
	public void setTextSize(int size){
		textSize = size;
	}
	
	public void setRadius(int radius) {
		outsideRadius = radius;
		width = outsideRadius/5;
		insideRadius = outsideRadius - width*2;
		middleRadius = outsideRadius - width;
	}

	public void setWidth(int wigth){
		this.width = wigth;
		insideRadius = outsideRadius - width*2;
		middleRadius = outsideRadius - width;
	}
	
	public void setOutSideColor(int color) {
		borderPaint.setColor(color);
	}

	public void setMiddleColor(int color) {
		borderPaint.setColor(color);
	}

	private void setUpPaints() {
		
		RadialGradient gradient = new RadialGradient(centerX, centerY, outsideRadius, Color.rgb(0, 0, 0), mainColor,
				android.graphics.Shader.TileMode.MIRROR);
		
		mainPaint = new Paint();
		mainPaint.setDither(true);
		mainPaint.setShader(gradient);
		mainPaint.setStyle(Paint.Style.STROKE);
		mainPaint.setStrokeWidth(width * 2);
		mainPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setColor(mainColor);
		borderPaint.setStyle(Paint.Style.STROKE);
		
		textPaint = new Paint();
		textPaint.setTextSize(outsideRadius - middleRadius);
		textPaint.setColor(textColor);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);
	}
	
	public void setGradient(RadialGradient gradient){
		mainPaint.setShader(gradient);
	}
	
	public void setGradient(LinearGradient gradient){
		mainPaint.setShader(gradient);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int viewWidth;
		int viewHeight;
		int desiredWidth = getDesiredWidth();
		int desiredHeight = getDesiredHeight();
	
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.EXACTLY) {
			viewWidth = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			viewWidth = Math.min(desiredWidth, widthSize);
		} else {
			viewWidth = desiredWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			viewHeight = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			viewHeight = Math.min(desiredHeight, heightSize);
		} else {
			viewHeight = desiredHeight;
		}

		centerX = viewWidth / 2;
		centerY = viewHeight / 2;

		setUpPaints();
		initArcs();
		setMeasuredDimension(viewWidth, viewHeight);
	}

	private int getDesiredWidth() {
		return outsideRadius * 2 + padding;
	}

	private int getDesiredHeight() {
		return outsideRadius * 2 + padding;
	}
	
	private void initDefaultMesures() {
		spinHandler.sendEmptyMessage(0);
		padding = 2;
		mainColor = Color.rgb(0, 186, 255);
		textColor = Color.BLACK;
		outsideRadius = MEDIUM;
		width = outsideRadius / 5;
		textSize = width * 2;
		insideRadius = outsideRadius - width * 2;
		middleRadius = outsideRadius - width;
		isSpinning = true;
	}
	
	private void initArcs(){
		outside = new RectF(centerX - outsideRadius, centerY - outsideRadius, centerX + outsideRadius, centerY + outsideRadius);
		middle = new RectF(centerX - middleRadius, centerY - middleRadius, centerX + middleRadius, centerY + middleRadius);
		inside = new RectF(centerX - insideRadius, centerY - insideRadius, centerX + insideRadius, centerY + insideRadius);
	}
	
	public CustomProgressBar(Context context) {
		super(context);
		initDefaultMesures();
	}

	public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDefaultMesures();
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDefaultMesures();
	}
	
	public enum ProgressBarStyle{
	    SMALL, MEDIUM, BIG
	}
}
