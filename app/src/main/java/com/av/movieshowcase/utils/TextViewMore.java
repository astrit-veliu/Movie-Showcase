package com.av.movieshowcase.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by aveliu2 on 28,January,2020
 */
public class TextViewMore extends AppCompatTextView {

    private static final String TAG = TextViewMore.class.getName();

    private int showingLine = 1;
    private int showingChar;
    private boolean isCharEnable;
    private boolean collapsed = true;

    private String showMore = "Show more";
    private String showLess = "Show less";
    private String dotdot = "   ";

    private int MAGIC_NUMBER = 5;

    private int showMoreTextColor = Color.BLACK;
    private int showLessTextColor = Color.BLACK;

    private String mainText;

    private boolean isAlreadySet;


    public TextViewMore(Context context) {
        super(context);
    }

    public TextViewMore(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mainText = getText().toString();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    private void addShowMore() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                String text = getText().toString();
                if (!isAlreadySet) {
                    mainText = getText().toString();
                    isAlreadySet = true;
                }
                String showingText = "";
                if (isCharEnable) {
                    if (showingChar >= text.length()) {
                        try {
                            throw new Exception("Character count cannot be exceed total line count");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    String newText = text.substring(0, showingChar);
                    newText += dotdot + showMore;

                    collapsed = true;

                    setText(newText);
                    Log.d(TAG, "Text: " + newText);
                } else {

                    if (showingLine >= getLineCount()) {
                        try {
                            throw new Exception("Line Number cannot be exceed total line count");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error: " + e.getMessage());
                        }
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        return;
                    }
                    int start = 0;
                    int end;
                    for (int i = 0; i < showingLine; i++) {
                        end = getLayout().getLineEnd(i);
                        showingText += text.substring(start, end);
                        start = end;
                    }

                    String newText = showingText.substring(0, showingText.length() - (dotdot.length() + showMore.length() + MAGIC_NUMBER));
                    Log.d(TAG, "Text: " + newText);
                    Log.d(TAG, "Text: " + showingText);
                    newText += dotdot + showMore;

                    collapsed = true;

                    setText(newText);
                }

                setShowMoreColoringAndClickable();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });


    }

    private void setShowMoreColoringAndClickable() {
        final SpannableString spannableString = new SpannableString(getText());

        Log.d(TAG, "Text: " + getText());
        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                    }

                                    @Override
                                    public void onClick(@Nullable View view) {
                                        setMaxLines(Integer.MAX_VALUE);
                                        setText(mainText);
                                        collapsed = false;
                                        showLessButton();
                                        Log.d(TAG, "Item clicked: " + mainText);

                                    }
                                },
                getText().length() - (dotdot.length() + showMore.length()),
                getText().length(), 0);

        spannableString.setSpan(new ForegroundColorSpan(showMoreTextColor),
                getText().length() - (dotdot.length() + showMore.length()),
                getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    private void showLessButton() {

        String text = getText() + dotdot + showLess;
        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new ClickableSpan() {
                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        ds.setUnderlineText(false);
                                    }

                                    @Override
                                    public void onClick(@Nullable View view) {

                                        setMaxLines(showingLine);

                                        addShowMore();

                                        Log.d(TAG, "Item clicked: ");

                                    }
                                },
                text.length() - (dotdot.length() + showLess.length()),
                text.length(), 0);

        spannableString.setSpan(new ForegroundColorSpan(showLessTextColor),
                text.length() - (dotdot.length() + showLess.length()),
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setMovementMethod(LinkMovementMethod.getInstance());
        setText(spannableString, TextView.BufferType.SPANNABLE);
    }


    /*
     * User added field
     * */

    /**
     * User can add minimum line number to show collapse text
     *
     * @param lineNumber int
     */
    public void setShowingLine(int lineNumber) {
        if (lineNumber == 0) {
            try {
                throw new Exception("Line Number cannot be 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        isCharEnable = false;

        showingLine = lineNumber;

        setMaxLines(showingLine);

        if (collapsed) {
            addShowMore();
        } else {
            setMaxLines(Integer.MAX_VALUE);
            showLessButton();
        }

    }

    /**
     * User can limit character limit of text
     *
     * @param character int
     */
    public void setShowingChar(int character) {
        if (character == 0) {
            try {
                throw new Exception("Character length cannot be 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        isCharEnable = true;
        this.showingChar = character;

        if (collapsed) {
            addShowMore();
        } else {
            setMaxLines(Integer.MAX_VALUE);
            showLessButton();
        }
    }

    /**
     * User can add their own  show more text
     *
     * @param text String
     */
    public void addShowMoreText(String text) {
        showMore = text;
    }

    /**
     * User can add their own show less text
     *
     * @param text String
     */
    public void addShowLessText(String text) {
        showLess = text;
    }

    /**
     * User Can add show more text color
     *
     * @param color Integer
     */
    public void setShowMoreColor(int color) {
        showMoreTextColor = color;
    }

    /**
     * User can add show less text color
     *
     * @param color Integer
     */
    public void setShowLessTextColor(int color) {
        showLessTextColor = color;
    }

}
