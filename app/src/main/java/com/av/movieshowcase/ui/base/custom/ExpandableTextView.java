package com.av.movieshowcase.ui.base.custom;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.av.movieshowcase.R;

public class ExpandableTextView extends AppCompatTextView {

    private Integer textMaxLength = 250;
    private Integer seeMoreTextColor = R.color.materialBlue;
    int initialHeight;

    private String collapsedTextWithSeeMoreButton;
    private String expandedTextWithSeeMoreButton;
    private String orignalContent;

    private SpannableString collapsedTextSpannable;
    private SpannableString expandedTextSpannable;

    private Boolean isExpanded = false;

    private String seeMore = "See more", seeLess = "See less";

    public ExpandableTextView(Context context)
    {
        super(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setMaxLength(Integer maxLength)
    {
        textMaxLength = maxLength;
    }

    public void setMoreButtonTextColor(Integer color)
    {
        seeMoreTextColor = color;
    }

    public void expandText(Boolean expand)
    {
        if (expand)
        {
            isExpanded = true;
            setText(expandedTextSpannable);
        }
        else
        {
            isExpanded = false;
            setText(collapsedTextSpannable);
        }

    }

    public void setMoreAndLessText(String seeMoreText,String seeLessText)
    {
        seeMore = seeMoreText;
        seeLess = seeLessText;
    }

    public Boolean isExpanded()
    {
        return isExpanded;
    }

    //toggle the state
    public void toggle()
    {
        if (isExpanded)
        {
            isExpanded = false;
            setText(collapsedTextSpannable);
            animate(this,true);
        }
        else
        {
            isExpanded = true;
            setText(expandedTextSpannable);
            animate(this,false);
        }

        //cycleTextViewExpansion(this);
    }

    private void cycleTextViewExpansion(TextView tv){
        int collapsedMaxLines = 3;
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines",
                tv.getMaxLines() == collapsedMaxLines? tv.getLineCount() : collapsedMaxLines);
        animation.setDuration(200).start();
    }


    private void animate(final View v, boolean isExpanded) {
        int matchParentMeasureSpec = MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        if(isExpanded) a.setDuration(200);
        else a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));

        v.startAnimation(a);
    }



    public void setContentText(String text)
    {
        orignalContent = text;
        this.setMovementMethod(LinkMovementMethod.getInstance());
        //show see more
        if (orignalContent.length() >=textMaxLength)
        {
            collapsedTextWithSeeMoreButton = orignalContent.substring(0,textMaxLength) +"... "+seeMore;
            expandedTextWithSeeMoreButton = orignalContent+" "+seeLess;

            //creating spannable strings
            collapsedTextSpannable = new SpannableString(collapsedTextWithSeeMoreButton);
            expandedTextSpannable = new SpannableString(expandedTextWithSeeMoreButton);


            collapsedTextSpannable.setSpan(clickableSpan,textMaxLength+4,collapsedTextWithSeeMoreButton.length(),0);
            collapsedTextSpannable.setSpan(new StyleSpan(Typeface.BOLD),textMaxLength+4,collapsedTextWithSeeMoreButton.length(),0);
            collapsedTextSpannable.setSpan(new RelativeSizeSpan(1.1f),textMaxLength+4,collapsedTextWithSeeMoreButton.length(),0);

            expandedTextSpannable.setSpan(clickableSpan,orignalContent.length()+1,expandedTextWithSeeMoreButton.length(),0);
            expandedTextSpannable.setSpan(new StyleSpan(Typeface.BOLD),orignalContent.length()+1,expandedTextWithSeeMoreButton.length(),0);
            expandedTextSpannable.setSpan(new RelativeSizeSpan(1.1f),orignalContent.length()+1,expandedTextWithSeeMoreButton.length(),0);

            if (isExpanded)
                setText(expandedTextSpannable);
            else
                setText(collapsedTextSpannable);
        }
        else
        {
            //to do: don't show see more
            setText(orignalContent);
        }

        initialHeight = this.getMeasuredHeight();
    }


    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget)
        {
            toggle();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(getResources().getColor(seeMoreTextColor));
        }
    };

}