package com.example.qeety;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.Random;

public class Animation {
    private static final int NUM_BUTTERFLIES = 5; // Nombre de papillons par défaut

    // 🦋 Génération des papillons
    public static void generateButterflies(Activity activity, RelativeLayout layout, int num) {
        Random random = new Random();

        for (int i = 0; i < num; i++) {
            ImageView butterfly = new ImageView(activity);
            butterfly.setLayoutParams(new RelativeLayout.LayoutParams(200, 200)); // Taille ajustable

            // Charger le GIF avec Glide
            Glide.with(activity)
                    .asGif()
                    .load(R.drawable.yellow_butterfly)
                    .into(butterfly);

            // Position aléatoire
            int x = random.nextInt(800);
            int y = random.nextInt(1200);
            butterfly.setX(x);
            butterfly.setY(y);

            // Définir l'opacité initiale à 0 (invisible)
            butterfly.setAlpha(0f);
            layout.addView(butterfly);

            // Animation de fondu en entrée
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(butterfly, "alpha", 0f, 1f);
            fadeIn.setDuration(1500);
            fadeIn.start();

            // Générer des variations de mouvement aléatoires
            int deltaX = random.nextInt(300) + 100;
            int deltaY = random.nextInt(300) + 100;
            int durationX = random.nextInt(3000) + 2000;
            int durationY = random.nextInt(3000) + 2000;

            // Animation sinusoïdale en X
            ObjectAnimator flyAnimationX = ObjectAnimator.ofFloat(
                    butterfly, "translationX", x, x + deltaX, x - deltaX, x
            );
            flyAnimationX.setDuration(durationX);
            flyAnimationX.setRepeatCount(ValueAnimator.INFINITE);
            flyAnimationX.setRepeatMode(ValueAnimator.REVERSE);
            flyAnimationX.setInterpolator(new AccelerateDecelerateInterpolator());

            // Animation sinusoïdale en Y
            ObjectAnimator flyAnimationY = ObjectAnimator.ofFloat(
                    butterfly, "translationY", y, y - deltaY, y + deltaY, y
            );
            flyAnimationY.setDuration(durationY);
            flyAnimationY.setRepeatCount(ValueAnimator.INFINITE);
            flyAnimationY.setRepeatMode(ValueAnimator.REVERSE);
            flyAnimationY.setInterpolator(new AccelerateDecelerateInterpolator());

            // Démarrer les animations après l'apparition
            fadeIn.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    flyAnimationX.start();
                    flyAnimationY.start();
                }
            });
        }
    }



    // 💨 Disparition progressive d'un élément
    public static void fadeOutAndRemove(final View view, RelativeLayout layout) {
        view.animate()
                .alpha(0f)
                .setDuration(1000) // 1 seconde
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeView(view);
                    }
                })
                .start();
    }

    // ❌ Supprime tous les papillons avec effet de fondu
    public static void removeButterfliesWithAnimation(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if (view instanceof ImageView) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    // ❌ Supprime immédiatement tous les papillons
    public static void removeButterflies(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if (view instanceof ImageView) {
                layout.removeView(view);
            }
        }
    }
}
