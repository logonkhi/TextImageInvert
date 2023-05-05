package com.gdxstudio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.widget.AutoSizeableTextView;

import com.gdxstudio.textimageinvert.R;

public class FontTextView extends androidx.appcompat.widget.AppCompatTextView implements AutoSizeableTextView {

    private static Typeface font;
    private String fontFileName;

    public FontTextView(Context context) {
        super(context);
        setFont(context);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont(attrs);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(attrs);
    }

    public Typeface getTypeface(Context context) {
        if (font == null) {
            try {
                font = Typeface.createFromAsset(context.getAssets(), fontFileName);
            } catch (Exception fontNotFound) {
                fontNotFound.printStackTrace();
                font = Typeface.DEFAULT;
            }
        }
        return font;
    }


    public void setFont(Context context) {
        setIncludeFontPadding(false);
        setTypeface(getTypeface(context));
    }

    public void setFont(AttributeSet attrs) {
        setIncludeFontPadding(false);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
        fontFileName = typedArray.getString(R.styleable.FontTextView_fontFileName);

        setTypeface(getTypeface(getContext()));
        typedArray.recycle();
    }

    /**
     * marquee 양쪽 끝 blur 제거
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setHorizontalFadingEdgeEnabled(false);
    }
}