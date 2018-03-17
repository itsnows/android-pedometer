package com.pedometerlibrary.widget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/19 17:17
 * <p>
 * CircleProgressDrawable
 */

public class CircleProgressDrawable extends Drawable {
    private static final float DEFAULT_PROGRESS_WIDTH = 3;
    private static final float DEFAULT_PROGRESS_RADIUS = 20;
    private static final int DEFAULT_PROGRESS_BACKGROUND_COLOR = 0XFFBDBDBD;
    private static final int DEFAULT_PROGRESS_COLOR = 0xFF91C654;
    private static final float DEFAULT_PROGRESS_NUMBER_SIZE = 12;
    private static final int DEFAULT_PROGRESS_NUMBER_COLOR = 0XFF9E9E9E;
    private static final int DEFAULT_MAX_PROGRESS = 100;

    /**
     * 屏幕密度
     */
    private float density;

    /**
     * 进度条宽度
     */
    private float progressWidth;

    /**
     * 进度条半径
     */
    private float progressRadius;

    /**
     * 进度条背景颜色
     */
    private int progressBackgroundColor;

    /**
     * 进度条颜色
     */
    private int progressColor;

    /**
     * 进度条数字大小
     */
    private float progressNumberSize;

    /**
     * 进度条数字颜色
     */
    private int progressNumberColor;

    /**
     * 最大进度
     */
    private int maxProgress;

    /**
     * 当前进度
     */
    private int currentProgress;

    /**
     * 画笔
     */
    private Paint point;

    public CircleProgressDrawable(Resources res) {
        this.density = res.getDisplayMetrics().density;
        init();
    }

    private void init() {
        this.progressWidth = DEFAULT_PROGRESS_WIDTH * density;
        this.progressRadius = DEFAULT_PROGRESS_RADIUS * density;
        this.progressBackgroundColor = DEFAULT_PROGRESS_BACKGROUND_COLOR;
        this.progressColor = DEFAULT_PROGRESS_COLOR;
        this.progressNumberSize = DEFAULT_PROGRESS_NUMBER_SIZE * density;
        this.progressNumberColor = DEFAULT_PROGRESS_NUMBER_COLOR;
        this.maxProgress = DEFAULT_MAX_PROGRESS;
        this.currentProgress = 0;
        this.point = new Paint();
    }

    /**
     * 设置进度条宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(float progressWidth) {
        this.progressWidth = progressWidth;
        invalidateSelf();
    }

    /**
     * 设置进度条半径
     *
     * @param progressRadius
     */
    public void setProgressRadius(float progressRadius) {
        this.progressRadius = progressRadius;
        invalidateSelf();
    }

    /**
     * 设置进度条背景颜色
     *
     * @param progressBackgroundColor
     */
    public void setProgressBackgroundColor(@ColorInt int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
        invalidateSelf();
    }

    /**
     * 设置进度条颜色
     *
     * @param progressColor
     */
    public void setProgressColor(@ColorInt int progressColor) {
        this.progressColor = progressColor;
        invalidateSelf();
    }

    /**
     * 设置进度条数字大小
     *
     * @param progressNumberSize
     */
    public void setProgressNumberSize(int progressNumberSize) {
        this.progressNumberSize = progressNumberSize;
        invalidateSelf();
    }

    /**
     * 设置进度条字体颜色
     *
     * @param progressNumberColor
     */
    public void setProgressNumberColor(int progressNumberColor) {
        this.progressNumberColor = progressNumberColor;
        invalidateSelf();
    }

    /**
     * 获取进度条最大进度
     *
     * @return
     */
    public int getMaxProgress() {
        return maxProgress;
    }

    /**
     * 设置进度条最大大进度
     *
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidateSelf();
    }

    /**
     * 获取进度条当前进度
     *
     * @return
     */
    public int getCurrentProgress() {
        return currentProgress;
    }

    /**
     * 设置进度当前进度
     *
     * @param currentProgress
     */
    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int width = (int) ((progressRadius + 4 * density) * 2);
        int height = width;
        float progress = (float) currentProgress / maxProgress;
        // 绘制进度条背景
        point.setAntiAlias(true);
        point.setColor(progressBackgroundColor);
        point.setStyle(Paint.Style.STROKE);
        point.setStrokeWidth(progressWidth);
        canvas.drawCircle(width / 2, height / 2, progressRadius, point);

        // 绘制进度条进度
        RectF rectF = new RectF();
        rectF.left = width / 2 - progressRadius;
        rectF.top = height / 2 - progressRadius;
        rectF.right = progressRadius * 2 + (width / 2 - progressRadius);
        rectF.bottom = progressRadius * 2 + (height / 2 - progressRadius);
        point.setColor(progressColor);
        canvas.drawArc(rectF, -90, progress * 360, false, point);

        // 绘制进度条文字
        point.setColor(progressNumberColor);
        point.setTextSize(progressNumberSize);
        point.setStyle(Paint.Style.FILL);
        point.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = point.getFontMetrics();
        int baseLineY = (int) (rectF.centerY() - fontMetrics.top / 2 - fontMetrics.bottom / 2);
        canvas.drawText(String.valueOf((int) ((progress > 1.0f ? 1 : progress) * 100)) + "%", rectF.centerX(), baseLineY, point);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        point.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        point.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) ((progressRadius + 4 * density) * 2);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) ((progressRadius + 4 * density) * 2);
    }

    /**
     * 获取Bitmap对象
     *
     * @return
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(this.getIntrinsicWidth(), this.getIntrinsicHeight(), this.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        super.setBounds(0, 0, this.getIntrinsicWidth(), this.getIntrinsicHeight());
        this.draw(canvas);
        return bitmap;
    }
}
