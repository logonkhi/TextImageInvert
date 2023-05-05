
# TextImageInvertMaskView
이미지 위의 반전된 텍스트를 그리는 Android View
Text Inverted Mask View for Android

<img src="Readme_textImageInvertMaskCrop.gif">

### What is that?
TextImageInvertMaskView는 이미지 위에 반전된 이미지의 텍스트를 그리는 Android View입니다.
TextImageInvertMaskView is an Android view that draws the text of an inverted image on top of an image.

It has the following features:
- 폰트명으로 custom font 지정.
- 리소스 ID로 이미지 경로 지정.
- 문자열로 화면에 보여줄 Text 지정.
- dp로 text 사이즈 조절.
- dp로 x축 y축 text의 위치를 조절.
- Font Name for custom font as String
- Image for text painting as Image resource identifier.
- Text for the screen as String.
- Text size with DP.
- Position of the x-axis and y-axis text with DP.

### Code
```java
//Start animation moving from left to right
void playTextAnimation(){
    ConstraintLayout container = findViewById(R.id.container);
    TextView movingText = findViewById(R.id.moving_text);
    TextImageInvertMaskView textImageInvertMaskView = findViewById(R.id.masked_text_image);

    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        @Override
        public void run() {

            int containerW = container.getWidth();
            int left = container.getLeft() - movingText.getWidth();
            movingText.animate().translationX(containerW).setDuration(0).start();

            float gap = (containerW - textImageInvertMaskView.getWidth())/2f;

            ObjectAnimator animation = ObjectAnimator.ofFloat(movingText, "translationX", left);
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float x = (float)animation.getAnimatedValue() - gap;
                    if(left < x){
                        textImageInvertMaskView.setTextX(x);
                    }
                }
            });
            animation.setDuration(15000);
            animation.setRepeatCount(-1);
            animation.start();
        }
    }, 500);
}
```

### XML
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="340dp"
    android:layout_height="340dp"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    android:layout_margin="20dp"
    android:background="#000000"
    >
    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <com.gdxstudio.view.FontTextView
            android:id="@+id/moving_text"
            android:layout_width="600dp"
            android:layout_height="wrap_content"
            android:includeFontPadding="false"
            android:textColor="@android:color/white"
            android:textSize="40dp"
            android:gravity="start"
            android:maxLines="1"
            android:layout_marginTop="22dp"
            android:text="Text Image Invert Mask View"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:fontFileName="AbrilFatface-Regular.ttf"
            />

        <com.gdxstudio.view.TextImageInvertMaskView
            android:id="@+id/masked_text_image"
            android:layout_width="240dp"
            android:layout_height="240dp"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:fontName="AbrilFatface-Regular.ttf"
            app:text="Text Image Invert Mask View"
            app:textDrawable="@drawable/image"
            app:textSize="40dp"
            app:textX="240dp"
            app:textY="104dp"
            />

    </androidx.constraintlayout.widget.ConstraintLayout>
</androidx.constraintlayout.widget.ConstraintLayout>
```