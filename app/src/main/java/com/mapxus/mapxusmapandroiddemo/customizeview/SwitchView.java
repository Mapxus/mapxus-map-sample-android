package com.mapxus.mapxusmapandroiddemo.customizeview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.mapxus.mapxusmapandroiddemo.R;

import me.jessyan.autosize.utils.AutoSizeUtils;


public class SwitchView extends View {
   public static final int DEFAULT_ANIMATION_DURATION = 250;

   private Paint textPaint;
   private int height;
   private int width;
   private ShapeDrawable backgroundDrawable;
   private ShapeDrawable genderDrawable;

   private float drawTextX;
   private float drawTextY;
   private float mStartX;
   private float mStartY;
   private float mLastX;

   private float mProgress;
   private int mTouchSlop;
   private int mClickTimeout;
   private ValueAnimator mProgressAnimator;
   private Rect bounds;
   private int boundsWidth;
   private int bundsX;

   private onCheckChangedListener onCheckChangedListener;

   public SwitchView(Context context) {
      this(context, null);
   }

   public SwitchView(Context context, @Nullable AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public SwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      initView(context);
   }

   private void initView(Context context) {
      int textSize = AutoSizeUtils.sp2px(context, 14);
      height = AutoSizeUtils.dp2px(context, 30);
      width = AutoSizeUtils.dp2px(context, 150);
      int radiis = AutoSizeUtils.dp2px(context, 5);

      mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
      mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();

      textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      textPaint.setTypeface(Typeface.DEFAULT_BOLD);
      textPaint.setTextAlign(Paint.Align.CENTER);
      textPaint.setTextSize(textSize);
      textPaint.setColor(Color.BLACK);

      mProgressAnimator = new ValueAnimator();

      float[] outerRadii = {radiis, radiis, radiis, radiis, radiis, radiis, radiis, radiis};//外矩形 左上、右上、右下、左下的圆角半径
      RectF inset = new RectF(0, 0, 0, 0);//内矩形距外矩形，左上角x,y距离， 右下角x,y距离
      float[] innerRadii = {0, 0, 0, 0, 0, 0, 0, 0};//内矩形 圆角半径
      RoundRectShape roundRectShape = new RoundRectShape(outerRadii, inset, innerRadii);
      backgroundDrawable = new ShapeDrawable(roundRectShape);
      int back_color = ContextCompat.getColor(context, R.color.lighter_gray);
      backgroundDrawable.getPaint().setColor(back_color);
      backgroundDrawable.setBounds(0, 0, width, height);

      RoundRectShape shape = new RoundRectShape(outerRadii, inset, innerRadii);
      LinearGradient linearGradient = new LinearGradient(0, 0, boundsWidth, height, Color.WHITE, Color.WHITE, Shader.TileMode.REPEAT);
      genderDrawable = new ShapeDrawable(shape);
      genderDrawable.getPaint().setShader(linearGradient);
      genderDrawable.getPaint().setStyle(Paint.Style.FILL);
      boundsWidth = width / 2;
      bundsX = (int) (mProgress * boundsWidth);
      bounds = new Rect(bundsX, 0, boundsWidth + bundsX, height);
      genderDrawable.setBounds(bounds);


   }

   public float getProgress() {
      return mProgress;
   }

   public void setProcess(float progress) {
      float tp = progress;
      if (tp > 1) {
         tp = 1;
      } else if (tp < 0) {
         tp = 0;
      }
      updatePaintStyle();
      this.mProgress = tp;
      bundsX = (int) (mProgress * boundsWidth);
      bounds.left = bundsX;
      bounds.right = boundsWidth + bundsX;
      genderDrawable.setBounds(bounds);
      invalidate();
   }

   private void updatePaintStyle() {
      LinearGradient linearGradient = new LinearGradient(0, 0, boundsWidth, height, Color.WHITE, Color.WHITE, Shader.TileMode.REPEAT);
      genderDrawable.getPaint().setShader(linearGradient);
   }


   @SuppressLint("ClickableViewAccessibility")
   @Override
   public boolean onTouchEvent(MotionEvent event) {
      int action = event.getAction();
      float deltaX = event.getX() - mStartX;
      float deltaY = event.getY() - mStartY;
      switch (action) {
         case MotionEvent.ACTION_DOWN:
            mStartX = event.getX();
            mStartY = event.getY();
            mLastX = mStartX;
            setPressed(true);
            break;
         case MotionEvent.ACTION_MOVE:
            float x = event.getX();
            //计算滑动的比例 boundsWidth为整个宽度的一半
            setProcess(getProgress() + (x - mLastX) / boundsWidth);
            //这里比较x轴方向的滑动 和y轴方向的滑动 如果y轴大于x轴方向的滑动 事件就不在往下传递
            if ((Math.abs(deltaX) > mTouchSlop / 2f || Math.abs(deltaY) > mTouchSlop / 2f)) {
               if (Math.abs(deltaY) > Math.abs(deltaX)) {
                  return false;
               }
            }
            mLastX = x;
            break;
         case MotionEvent.ACTION_UP:
         case MotionEvent.ACTION_CANCEL:
            setPressed(false);
            //计算从手指触摸到手指抬起时的时间
            float time = event.getEventTime() - event.getDownTime();
            //如果x轴和y轴滑动距离小于系统所能识别的最小距离 切从手指按下到抬起时间 小于系统默认的点击事件触发的时间  整个行为将被视为触发点击事件
            if (Math.abs(deltaX) < mTouchSlop && Math.abs(deltaY) < mTouchSlop && time < mClickTimeout) {
               //获取事件触发的x轴区域 主要用于区分是左边还是右边
               float clickX = event.getX();
               //如果是在左边
               if (clickX > boundsWidth) {
                  if (mProgress == 1.0f) {
                     return false;
                  } else {
                     animateToState(true);
                  }
               } else {
                  if (mProgress == 0.0f) {
                     return false;
                  } else {
                     animateToState(false);
                  }
               }
               return false;
            } else {
               boolean nextStatus = getProgress() > 0.5f;
               animateToState(nextStatus);
            }
            break;
      }
      return true;
   }

   protected void animateToState(boolean checked) {
      float progress = mProgress;
      if (mProgressAnimator == null) {
         return;
      }
      if (mProgressAnimator.isRunning()) {
         mProgressAnimator.cancel();
         mProgressAnimator.removeAllUpdateListeners();
      }
      mProgressAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
      if (checked) {
         mProgressAnimator.setFloatValues(progress, 1f);
      } else {
         mProgressAnimator.setFloatValues(progress, 0.0f);
      }
      mProgressAnimator.addUpdateListener(animation -> {
         mProgress = (float) animation.getAnimatedValue();

         updatePaintStyle();
         bundsX = (int) (mProgress * boundsWidth);
         bounds.left = bundsX;
         bounds.right = boundsWidth + bundsX;
         genderDrawable.setBounds(bounds);
         postInvalidate();
      });
      mProgressAnimator.start();
      if (onCheckChangedListener != null) {
         onCheckChangedListener.onCheck(checked);
      }
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      setMeasuredDimension(width, height);
   }

   @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
      super.onSizeChanged(w, h, oldw, oldh);
      int textMargin = AutoSizeUtils.dp2px(this.getContext(), 5);
      String mText = getContext().getString(R.string.wheelchair);
      Rect bounds = new Rect();
      textPaint.getTextBounds(mText, 0, mText.length(), bounds);
      int textHeight = bounds.height();
      drawTextX = textMargin + bounds.width() / 2f;
      drawTextY = height / 2f + textHeight / 2f;
   }

   @Override
   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      backgroundDrawable.draw(canvas);
      genderDrawable.draw(canvas);
      canvas.drawText(getContext().getString(R.string.foot), drawTextX, drawTextY, textPaint);
      canvas.drawText(getContext().getString(R.string.wheelchair), width / 2f + drawTextX, drawTextY, textPaint);
   }

   public interface onCheckChangedListener {
      void onCheck(boolean checked);
   }

   public void setOnCheckChangedListener(SwitchView.onCheckChangedListener onCheckChangedListener) {
      this.onCheckChangedListener = onCheckChangedListener;
   }
}

