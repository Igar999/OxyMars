package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Oxigeno oxi = Oxigeno.getOxi();
        TextView textOxigeno = findViewById(R.id.oxigeno);
        TextView textOxiSegundo = findViewById(R.id.textOxiSegundo);
        TextView textOxiToque = findViewById(R.id.textOxiToque);

        ImageView planeta = findViewById(R.id.planeta);

        hideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque));
        hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo));

        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                oxi.aumentarOxigenoSegundo();
                textOxigeno.setText(String.valueOf(oxi.getOxigeno()));
                Log.i("rot",String.valueOf(planeta.getRotation()));

                handler.postDelayed(this, delay);

            }
        }, delay);

        final Handler handler3 = new Handler();
        final int delay3 = 45000; // 1000 milliseconds == 1 second

        handler3.postDelayed(new Runnable() {
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



        final int[] cont2 = {0};
        final Handler handler2 = new Handler();
        final int delay2 = 100; // 1000 milliseconds == 1 second

        handler2.postDelayed(new Runnable() {
            public void run() {
                textOxigeno.setText(String.valueOf(oxi.getOxigeno()));
                textOxiToque.setText(String.valueOf(oxi.getOxiToque()));
                textOxiSegundo.setText(String.valueOf(oxi.getOxiSegundo()));
                handler2.postDelayed(this, delay2);
            }
        }, delay2);

        planeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ObjectAnimator animation = ObjectAnimator.ofFloat(planeta, "translationY", -200f);
                animation.setDuration(2000);
                animation.start();*/
                oxi.aumentarOxigenoToque();
                textOxigeno.setText(String.valueOf(oxi.getOxigeno()));

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


        Button botonToque = findViewById(R.id.botonToque);
        botonToque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque));
                hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo));
                //getFragmentManager().beginTransaction().add(R.id.layout, new MejorasToque(), "mejorasToque");

                /*Fragment f = getFragmentManager().findFragmentByTag("mejoras");
                if(f!=null) getFragmentManager().beginTransaction().remove(f);
                getFragmentManager().beginTransaction().commit();*/

            }
        });
        Button botonSegundo = findViewById(R.id.botonSegundo);
        botonSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo));
                hideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque));
                //getFragmentManager().beginTransaction().add(R.id.layout, new MejorasToque(), "mejorasToque");

                /*Fragment f = getFragmentManager().findFragmentByTag("mejoras");
                if(f!=null) getFragmentManager().beginTransaction().remove(f);
                getFragmentManager().beginTransaction().commit();*/

            }
        });
    }



    //https://www.semicolonworld.com/question/47971/show-hide-fragment-in-android (respuesta de kishan patel)
    public void showHideFragment(final Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);

        if (fragment.isHidden()) {
            ft.show(fragment);
            Log.d("hidden","Show");
        } else {
            ft.hide(fragment);
            Log.d("Shown","Hide");
        }

        ft.commit();
    }
    public void showFragment(final Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        if (fragment.isHidden()) {
            ft.show(fragment);
            Log.d("hidden","Show");
        }
        ft.commit();
    }
    public void hideFragment(final Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        if (!fragment.isHidden()) {
            ft.hide(fragment);
            Log.d("Shown","Hide");
        }
        ft.commit();
    }
}