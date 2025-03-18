package com.example.qeety;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.app.LocaleManager;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

import java.util.Locale;
import java.util.HashMap;

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends Activity {
    private final int NUM_BUTTERFLIES = 5; // Nombre de papillons

    private HashMap<Integer, String> localeMap; // Associe chaque image à un code de langue

    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout); // Assurez-vous que l'ID est bien défini

        if (mainLayout == null) {
            throw new NullPointerException("mainLayout is null! Check activity_main.xml.");
        }
        generateButterflies();  // Génère plusieurs papillons avec animation

        initLocalePicker();
    }


    private void initLocalePicker() {
        Spinner spinner = findViewById(R.id.localePicker);

        // Tableau des images de drapeaux
        int[] flagImages = {
                R.drawable.fr,  // Français
                R.drawable.gb,  // Anglais
                R.drawable.es   // Espagnol
        };

        // Associer les positions aux codes de langue
        localeMap = new HashMap<>();
        localeMap.put(0, "fr-FR");
        localeMap.put(1, "en-US");
        localeMap.put(2, "es-ES");

        ImageSpinnerAdapter adapter = new ImageSpinnerAdapter(this, flagImages);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocale = localeMap.get(position);
                if(selectedLocale != null){
                    LocaleManager localeManager = getSystemService(LocaleManager.class);
                    localeManager.setApplicationLocales(new LocaleList(Locale.forLanguageTag(selectedLocale)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void generateButterflies2() {
        Random random = new Random();

        for (int i = 0; i < NUM_BUTTERFLIES; i++) {
            ImageView butterfly = new ImageView(this);
            butterfly.setBackgroundResource(R.drawable.butterfly_anim);
            butterfly.setLayoutParams(new RelativeLayout.LayoutParams(100, 100));

            // Position aléatoire
            int x = random.nextInt(800);
            int y = random.nextInt(1200);
            butterfly.setX(x);
            butterfly.setY(y);

            mainLayout.addView(butterfly);

            // Démarrer l'animation d'ailes
            AnimationDrawable butterflyAnimation = (AnimationDrawable) butterfly.getBackground();
            butterflyAnimation.start();

            // Animation de vol en zigzag
            ObjectAnimator flyAnimationX = ObjectAnimator.ofFloat(butterfly, "translationX", x, x + 100, x - 100, x);
            flyAnimationX.setDuration(4000);
            flyAnimationX.setRepeatCount(ValueAnimator.INFINITE);
            flyAnimationX.setRepeatMode(ValueAnimator.REVERSE);
            flyAnimationX.start();

            ObjectAnimator flyAnimationY = ObjectAnimator.ofFloat(butterfly, "translationY", y, y - 200f, y);
            flyAnimationY.setDuration(3000);
            flyAnimationY.setRepeatCount(ValueAnimator.INFINITE);
            flyAnimationY.setRepeatMode(ValueAnimator.REVERSE);
            flyAnimationY.start();
        }
    }

    private void generateButterflies() {
        Random random = new Random();

        for (int i = 0; i < NUM_BUTTERFLIES; i++) {
            ImageView butterfly = new ImageView(this);
            butterfly.setLayoutParams(new RelativeLayout.LayoutParams(200, 200)); // Augmente la taille si nécessaire

            // Charger le GIF avec Glide
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.yellow_butterfly)
                    .into(butterfly);

            // Position aléatoire
            int x = random.nextInt(800);
            int y = random.nextInt(1200);
            butterfly.setX(x);
            butterfly.setY(y);

            mainLayout.addView(butterfly);

            // Animation de vol en zigzag
            ObjectAnimator flyAnimationX = ObjectAnimator.ofFloat(butterfly, "translationX", x, x + 100, x - 100, x);
            flyAnimationX.setDuration(4000);
            flyAnimationX.setRepeatCount(ValueAnimator.INFINITE);
            flyAnimationX.setRepeatMode(ValueAnimator.REVERSE);
            flyAnimationX.start();

            ObjectAnimator flyAnimationY = ObjectAnimator.ofFloat(butterfly, "translationY", y, y - 200f, y);
            flyAnimationY.setDuration(3000);
            flyAnimationY.setRepeatCount(ValueAnimator.INFINITE);
            flyAnimationY.setRepeatMode(ValueAnimator.REVERSE);
            flyAnimationY.start();
        }
    }



}
