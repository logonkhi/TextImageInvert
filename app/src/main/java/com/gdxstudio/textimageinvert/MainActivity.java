package com.gdxstudio.textimageinvert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.gdxstudio.view.TextImageInvertMaskView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playTextAnimation();
    }

    private void playTextAnimation(){
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

}