package com.av.movieshowcase.utils;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.widget.TextView;

public class GradientTextView {

    public static void gradientTextView(TextView textView){
        TextPaint paint = textView.getPaint();
        float width = paint.measureText(textView.getText().toString());

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#E2859F"),
                        Color.parseColor("#FCCF31"),
                }, null, Shader.TileMode.CLAMP);
    }

}
