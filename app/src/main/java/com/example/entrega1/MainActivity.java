package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int oxigeno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oxigeno = 0;
        TextView textOxigeno = findViewById(R.id.oxigeno);
        ImageView planeta = findViewById(R.id.planeta);

        Button boton = findViewById(R.id.boton);
        final int[] cont = {0};
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                boton.setText("" + cont[0]);
                cont[0] = cont[0] + 1;
                handler.postDelayed(this, delay);
                oxigeno++;
                textOxigeno.setText(String.valueOf(oxigeno));
                Log.i("rot",String.valueOf(planeta.getRotation()));
            }
        }, delay);

        final Handler handler3 = new Handler();
        final int delay3 = 45000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                int i = planeta.getMeasuredHeight();
                int j = planeta.getMeasuredWidth();

                ObjectAnimator animation = ObjectAnimator.ofFloat(planeta, "rotation", 0, 360);
                animation.setDuration(45000);
                planeta.setPivotX(i/2);
                planeta.setPivotY(j/2);
                animation.setRepeatCount(ObjectAnimator.INFINITE);
                animation.setInterpolator(new LinearInterpolator());
                animation.start();
                handler3.postDelayed(this, delay3);
            }
        }, delay);



        Button boton2 = findViewById(R.id.boton2);
        final int[] cont2 = {0};
        final Handler handler2 = new Handler();
        final int delay2 = 100; // 1000 milliseconds == 1 second

        handler2.postDelayed(new Runnable() {
            public void run() {
                boton2.setText("" + cont2[0]);
                cont2[0] = cont2[0] + 1;
                handler2.postDelayed(this, delay2);
            }
        }, delay2);

        planeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ObjectAnimator animation = ObjectAnimator.ofFloat(planeta, "translationY", -200f);
                animation.setDuration(2000);
                animation.start();*/
                oxigeno++;
                textOxigeno.setText(String.valueOf(oxigeno));

                ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.025f, 1.0f,
                        1.025f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                scaleAnim.setDuration(75);
                // scaleAnim.setFillEnabled(true);
                scaleAnim.setFillAfter(true);

                scaleAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ScaleAnimation scaleAnim2 = new ScaleAnimation(1.025f, 1.0f, 1.025f,
                                1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnim2.setDuration(75);
                        // scaleAnim.setFillEnabled(true);
                        scaleAnim2.setFillAfter(true);
                        planeta.setAnimation(scaleAnim2);
                        planeta.startAnimation(scaleAnim2);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                planeta.setAnimation(scaleAnim);
                planeta.startAnimation(scaleAnim);

            }
        });

        Button mar = findViewById(R.id.button);
        mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oxigeno > 20){
                    oxigeno = oxigeno - 20;
                    textOxigeno.setText(String.valueOf(oxigeno));
                    planeta.setImageResource(R.drawable.marte_mar);
                }


            }
        });
    }
}