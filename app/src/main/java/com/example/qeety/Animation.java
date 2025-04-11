package com.example.qeety;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.animation.ScaleAnimation;
import android.view.animation.DecelerateInterpolator;

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
            butterfly.setTag("butterfly");
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
            if ("butterfly".equals(view.getTag())) { // ✅ Ne cible que les papillons
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showRainbowGif(Activity activity, RelativeLayout layout) {
        ImageView rainbowGif = new ImageView(activity);
        rainbowGif.setTag("rainbow");

        // Paramètres pour positionner l'arc-en-ciel entre le menu et l'image qt_image
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Largeur de l'écran
                200 // Hauteur de l'arc-en-ciel
        );

        // Positionner l'arc-en-ciel juste en dessous du menu
        params.addRule(RelativeLayout.BELOW, R.id.menu); // Juste en dessous du menu
        params.addRule(RelativeLayout.ABOVE, R.id.qt_image); // Juste au-dessus de qt_image

        // Ajouter de la marge pour éviter la collision
        params.setMargins(0, 20, 0, 20); // Marge en haut et en bas

        rainbowGif.setLayoutParams(params);

        // Charger l'image gif dans ImageView
        Glide.with(activity)
                .asGif()
                .load(R.drawable.arc_en_ciel) // Charge ton gif ici
                .into(rainbowGif);

        // Ajouter l'arc-en-ciel au layout
        layout.addView(rainbowGif);

        // Animation : effet de slide de gauche à droite avec descente
        animateSlideFromLeft(rainbowGif);
    }

    public static void animateSlideFromLeft(final View view) {
        // Animation de translation de gauche à droite
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(
                view, "translationX", -view.getWidth(), 0f
        );
        translateXAnimator.setDuration(1000);  // Durée de l'animation
        translateXAnimator.setInterpolator(new DecelerateInterpolator());

        // Animation de fondu
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(
                view, "alpha", 0f, 1f
        );
        fadeAnimator.setDuration(1000);  // Durée du fondu


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateXAnimator, fadeAnimator);
        animatorSet.start();
    }

    public static void removeRainbow(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("rainbow".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }


    public static void showFrog(Activity activity, RelativeLayout layout) {
        ImageView frog = new ImageView(activity);
        frog.setImageResource(R.drawable.frog);
        frog.setId(View.generateViewId());
        frog.setTag("frog");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                200, 200 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(5, 0, 0, 0); // Marge à gauche

        frog.setLayoutParams(params);
        frog.setAlpha(0f);

        layout.addView(frog);

        frog.animate()
                .alpha(1f)
                .translationXBy(30f) // petit effet de "pop"
                .setDuration(600)
                .start();
    }

    public static void animateFrogJump(ImageView frog) {
        // Animation de saut (monter puis descendre)
        ObjectAnimator jumpUp = ObjectAnimator.ofFloat(frog, "translationY", 0f, -100f);
        jumpUp.setDuration(300);
        jumpUp.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator jumpDown = ObjectAnimator.ofFloat(frog, "translationY", -100f, 0f);
        jumpDown.setDuration(300);
        jumpDown.setInterpolator(new DecelerateInterpolator());

        AnimatorSet jumpSet = new AnimatorSet();
        jumpSet.playSequentially(jumpUp, jumpDown);
        jumpSet.setStartDelay(200); // Petit délai entre les sauts
        //jumpSet.setRepeatCount(ValueAnimator.INFINITE); // Répéter à l'infini

        jumpSet.start();
    }

    public static void startFrogJumpByTag(RelativeLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if ("frog".equals(view.getTag()) && view instanceof ImageView) {
                animateFrogJump((ImageView) view);
                break; // 🛑 Stop après la première grenouille trouvée
            }
        }
    }

    public static void removeFrog(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("frog".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showDog(Activity activity, RelativeLayout layout) {
        ImageView frog = new ImageView(activity);
        frog.setImageResource(R.drawable.dog);
        frog.setId(View.generateViewId());
        frog.setTag("dog");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                250, 250 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(5, 0, 0, 0); // Marge à gauche

        frog.setLayoutParams(params);
        frog.setAlpha(0f);

        layout.addView(frog);

        frog.animate()
                .alpha(1f)
                .translationXBy(30f) // petit effet de "pop"
                .setDuration(600)
                .start();
    }

    public static void removeDog(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("dog".equals(view.getTag())) {

                // 🐶 Animation : glisse vers la droite + fondu
                ObjectAnimator slideOut = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), view.getTranslationX() + 500f);
                slideOut.setDuration(600);

                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
                fadeOut.setDuration(600);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(slideOut, fadeOut);
                animatorSet.addListener(new android.animation.AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        layout.removeView(view); // 🧼 Supprimer le chien une fois l'animation terminée
                    }
                });

                animatorSet.start();
            }
        }
    }

    public static void showArbre(Activity activity, RelativeLayout layout) {
        ImageView frog = new ImageView(activity);
        frog.setImageResource(R.drawable.tree);
        frog.setId(View.generateViewId());
        frog.setTag("arbre");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(0, 0, 5, 0); // Marge à gauche

        frog.setLayoutParams(params);
        frog.setAlpha(0f);

        layout.addView(frog);

        frog.animate()
                .alpha(1f)
                .translationXBy(30f) // petit effet de "pop"
                .setDuration(600)
                .start();
    }

    public static void removeArbre(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("arbre".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showLeo(Activity activity, RelativeLayout layout) {
        ImageView frog = new ImageView(activity);
        frog.setImageResource(R.drawable.leo);
        frog.setId(View.generateViewId());
        frog.setTag("leo");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(0, 0, 5, 0); // Marge à gauche

        frog.setLayoutParams(params);
        frog.setAlpha(0f);

        layout.addView(frog);

        frog.animate()
                .alpha(1f)
                .translationXBy(30f) // petit effet de "pop"
                .setDuration(600)
                .start();
    }

    public static void removeLeo(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("leo".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showLili(Activity activity, RelativeLayout layout) {
        ImageView frog = new ImageView(activity);
        frog.setImageResource(R.drawable.lili);
        frog.setId(View.generateViewId());
        frog.setTag("lili");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(0, 0, 5, 0); // Marge à gauche

        frog.setLayoutParams(params);
        frog.setAlpha(0f);

        layout.addView(frog);

        frog.animate()
                .alpha(1f)
                .translationXBy(30f) // petit effet de "pop"
                .setDuration(600)
                .start();
    }

    public static void removeLili(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("lili".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showZoe(Activity activity, RelativeLayout layout) {
        ImageView frog = new ImageView(activity);
        frog.setImageResource(R.drawable.zoe);
        frog.setId(View.generateViewId());
        frog.setTag("zoe");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(5, 0, 0, 0); // Marge à gauche

        frog.setLayoutParams(params);
        frog.setAlpha(0f);

        layout.addView(frog);

        frog.animate()
                .alpha(1f)
                .translationXBy(30f) // petit effet de "pop"
                .setDuration(600)
                .start();
    }

    public static void removeZoe(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("zoe".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }
}
