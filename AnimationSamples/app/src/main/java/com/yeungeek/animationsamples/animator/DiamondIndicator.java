package com.yeungeek.animationsamples.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeungeek on 2016/2/26.
 */
public class DiamondIndicator extends BaseIndicatorController {
    float[] translateX = new float[4], translateY = new float[4];
    int[] colors = {Color.parseColor("#FFD600"), Color.parseColor("#00BCD4"),
            Color.parseColor("#F44336"),
            Color.parseColor("#7C4DFF")};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);
            canvas.save();
            canvas.translate(translateX[i], translateY[i]);
            canvas.drawCircle(0, 0, getWidth() / 6, paint);
            canvas.restore();
        }
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        float startX = getWidth() / 4;
        float startY = getWidth() / 4;
        for (int i = 0; i < 4; i++) {
            final int index = i;
            ValueAnimator translateXAnim = ValueAnimator.ofFloat(startX, getWidth() - startX, getWidth() - startX, startX, startX);
            if (i == 1) {
                translateXAnim = ValueAnimator.ofFloat(getWidth() - startX, getWidth() - startX, startX, startX, getWidth() - startX);
            } else if (i == 2) {
                translateXAnim = ValueAnimator.ofFloat(getWidth() - startX, startX, startX, getWidth() - startX, getWidth() - startX);
            } else if (i == 3) {
                translateXAnim = ValueAnimator.ofFloat(startX, startX, getWidth() - startX, getWidth() - startX, startX);
            }

            ValueAnimator translateYAnim = ValueAnimator.ofFloat(startY, startY, getHeight() - startY, getHeight() - startY, startY);
            if (i == 1) {
                translateYAnim = ValueAnimator.ofFloat(startY, getHeight() - startY, getHeight() - startY, startY, startY);
            } else if (i == 2) {
                translateYAnim = ValueAnimator.ofFloat(getHeight() - startY, getHeight() - startY, startY, startY, getHeight() - startY);
            } else if (i == 3) {
                translateYAnim = ValueAnimator.ofFloat(getHeight() - startY, startY, startY, getHeight() - startY, getHeight() - startY);
            }

            translateXAnim.setDuration(3000);
            translateXAnim.setInterpolator(new LinearInterpolator());
            translateXAnim.setRepeatCount(-1);
            translateXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateX[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateXAnim.start();

            translateYAnim.setDuration(3000);
            translateYAnim.setInterpolator(new LinearInterpolator());
            translateYAnim.setRepeatCount(-1);
            translateYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateY[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateYAnim.start();

            animators.add(translateXAnim);
            animators.add(translateYAnim);
        }
        return animators;
    }
}
