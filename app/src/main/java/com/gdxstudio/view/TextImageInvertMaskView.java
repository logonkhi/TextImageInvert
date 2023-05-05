package com.gdxstudio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gdxstudio.textimageinvert.R;

public class TextImageInvertMaskView extends View {

    private static final String DEFAULT_TEXT = "TEXT";
    private static final int DEFAULT_TEXT_SIZE = 64;
    private static final int DEFAULT_BITMAP_SIZE = 64;
    private static final LinearGradient DEFAULT_BITMAP_SHADER = new LinearGradient
            (0, 0, 0, DEFAULT_BITMAP_SIZE, Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
    private static final Typeface DEFAULT_FONT = Typeface.DEFAULT;

    private TextPaint textPaint;
    private BitmapDrawable textBitmapDrawable;
    private Bitmap textBitmap;
    private Bitmap bgBitmap;

    //Text related properties
    private int textSize;
    private float textX = Integer.MAX_VALUE;
    private float textY = 0;
    private String text;
    private String fontFileName;
    private Typeface font;

    private float textHeight;

    public TextImageInvertMaskView(Context context, String text, BitmapDrawable textBitmapDrawable, int textSizeInDP, String fontFileName, int textX, int textY) {
        super(context);
        init(context, text, textBitmapDrawable, textSizeInDP, textX, textY, fontFileName);
    }

    /**
     * Creates {@link TextImageInvertMaskView}.
     * <br/>
     * All parameters except context are optional.
     * <br/>
     * Reasonable default values are provided.
     *
     * @param context                      The Context the view is running in
     * @param text                         Text value of this view
     * @param textBitmapDrawableResourceID Image resource ID that will be the drawing pattern of the text
     * @param textSizeInDP                 Text size expressed in DIP
     * @param fontFileName                 File name for custom font that resides in /assets folder
     * @param textX                        Text Coordinates x
     * @param textY                        Text Coordinates y
     */
    public TextImageInvertMaskView(Context context, String text, int textBitmapDrawableResourceID, int textSizeInDP, String fontFileName, int textX, int textY) {
        super(context);
        BitmapDrawable drawable = null;
        try {
            drawable = (BitmapDrawable) getResources().getDrawable(textBitmapDrawableResourceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        init(context, text, drawable, textSizeInDP, textX, textY, fontFileName);
    }

    /**
     * Creates {@link TextImageInvertMaskView}.
     * <br/>
     * All parameters except context are optional.
     * <br/>
     * Reasonable default values are provided.
     *
     * @param context      The Context the view is running in
     * @param text         Text value of this view
     * @param textBitmap   Bitmap that will be the drawing pattern for the text
     * @param textSizeInDP Text size expressed in DIP
     * @param fontFileName File name for custom font that resides in /assets folder
     * @param textX        Text Coordinates x
     * @param textY        Text Coordinates y
     */
    public TextImageInvertMaskView(Context context, String text, Bitmap textBitmap, int textSizeInDP, String fontFileName, int textX, int textY) {
        super(context);
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), textBitmap);
        init(context, text, drawable, textSizeInDP, textX, textY, fontFileName);
    }

    public TextImageInvertMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intFromXml(context, attrs);
    }

    public TextImageInvertMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intFromXml(context, attrs);
    }

    //Assign values for custom xml attributes of that view
    //Case when we make the view from layout editor
    private void intFromXml(Context context, AttributeSet attrs) {
        TypedArray customAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextImageInvertMaskView, 0, 0);
        BitmapDrawable drawable = ((BitmapDrawable) customAttributes.getDrawable(R.styleable.TextImageInvertMaskView_textDrawable));

        int textSize = customAttributes.getDimensionPixelSize(R.styleable.TextImageInvertMaskView_textSize, DEFAULT_TEXT_SIZE);
        int textX = customAttributes.getDimensionPixelSize(R.styleable.TextImageInvertMaskView_textX, Integer.MAX_VALUE);
        int textY = customAttributes.getDimensionPixelSize(R.styleable.TextImageInvertMaskView_textY, 0);
        String text = customAttributes.getString(R.styleable.TextImageInvertMaskView_text);
        String fontFileName = customAttributes.getString(R.styleable.TextImageInvertMaskView_fontName);
        customAttributes.recycle();
        init(context, text, drawable, textSize, textX, textY, fontFileName);
    }

    //Case when we make the view in code
    private void init(Context context, String text, BitmapDrawable drawable, int textSizeInDP, int textXX, int textYY, String fontFileName) {
        textBitmapDrawable = drawable;
        checkValidDrawableOrAssignDefault();
        Bitmap bitmap = textBitmapDrawable.getBitmap();
        textBitmap = bitmap.copy(bitmap.getConfig(), true);
        inverseBitmapColors(textBitmap);
        bgBitmap = bitmap.copy(bitmap.getConfig(), false);
        this.textSize = textSizeInDP;
        this.textX = textXX;
        this.textY = textYY;
        checkTextSizePositiveOrAssignDefault();
        this.text = text;
        checkTextPresentOrAssignDefault();
        this.fontFileName = fontFileName;
        checkFontExistsOrAssignDefault(context);
        makePaint();
        calculateDimensions();
    }

    private void checkValidDrawableOrAssignDefault() {
        if (textBitmapDrawable == null) {
            textBitmapDrawable = generateDefaultBitmapDrawable();
        }
    }

    private BitmapDrawable generateDefaultBitmapDrawable() {

        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_BITMAP_SIZE, DEFAULT_BITMAP_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint gradientPaint = new Paint();
        gradientPaint.setShader(DEFAULT_BITMAP_SHADER);
        canvas.drawPaint(gradientPaint);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }

    private void checkTextSizePositiveOrAssignDefault() {
        if (textSize <= 0) {
            textSize = DEFAULT_TEXT_SIZE;
        }
    }

    private void checkTextPresentOrAssignDefault() {
        if (text == null) {
            text = DEFAULT_TEXT;
        }
    }

    //Fallback to the system default font if custom font is not found or supplied
    private void checkFontExistsOrAssignDefault(Context context) {
        try {
            this.font = Typeface.createFromAsset(context.getAssets(), fontFileName);
        } catch (Exception fontNotFound) {
            fontNotFound.printStackTrace();
            Log.w(getClass().getSimpleName(), fontNotFound);
            this.font = DEFAULT_FONT;
        }
    }

    //Set paint for drawing
    private void makePaint() {
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(font);
        textPaint.setColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textPaint.setBlendMode(BlendMode.DIFFERENCE);
        }else {
            //Drawing related properties
            Paint maskPaint = new Paint();
            maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            setLayerType(LAYER_TYPE_HARDWARE, textPaint);
        }
        setLayerType(LAYER_TYPE_HARDWARE, textPaint);
    }

    //Calculate and cache view dimensions based on supplied text
    //Calculate center of a view for later drawing
    public void calculateDimensions() {
        //View related properties
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHeight = -fm.ascent;
    }

    public void setBitmapDrawable(BitmapDrawable drawable) {
        textBitmapDrawable = drawable;
        checkValidDrawableOrAssignDefault();
        Bitmap bitmap = textBitmapDrawable.getBitmap();
        textBitmap = bitmap.copy(bitmap.getConfig(), true);
        inverseBitmapColors(textBitmap);
        bgBitmap = bitmap.copy(bitmap.getConfig(), false);
        invalidate();
    }


    /**
     * 텍스트 x 좌표 이동
     *
     * @param pixelX
     */
    public void setTextX(float pixelX) {
        if (getWidth() == 0 || textX == pixelX || pixelX > getWidth()) return;
        textX = pixelX;
        invalidate();
    }

    /**
     * 텍스트 y 좌표 이동
     *
     * @param pixelY
     */
    public void setTextY(float pixelY) {
        if (getHeight() == 0 || textY == pixelY || pixelY > getHeight()) return;
        textY = pixelY;
        invalidate();
    }



    //Draw text on screen
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect dst = getBitmapRect(bgBitmap);
        canvas.drawBitmap(bgBitmap, null, dst, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //canvas.drawText(text, 0, textY + textHeight, textPaint);
        }else{
            canvas.drawBitmap(createMaskedText(), 0, 0, null);
        }
    }

    private Bitmap createMaskedText() {
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        tempCanvas.drawText(text, textX, textY + textHeight, textPaint);
        tempCanvas.drawBitmap(textBitmap, null, getBitmapRect(textBitmap), paint);
        paint.setXfermode(null);
        return result;
    }

    private Rect getBitmapRect(Bitmap bitmap) {
        int width = getWidth();
        int height = getHeight();

        float w = bitmap.getWidth();
        float h = bitmap.getHeight();

        //if the length is longer
        if (w < h) {
            float rate = h / w;
            w = width;
            h = width * rate;
        } else {
            float rate = w / h;
            w = height * rate;
            h = height;
        }

        float left = ((width - w) / 2f);
        float top = ((height - h) / 2f);
        float right = left + w;
        float bottom = top + h;

        Rect rect = new Rect((int) left, (int) top, (int) right, (int) bottom);

        return rect;
    }


    //Measure pass performed by parent view
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void inverseBitmapColors(Bitmap bitmap) {
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                bitmap.setPixel(i, j, bitmap.getPixel(i, j) ^ 0x00ffffff);
            }
        }
    }

}
