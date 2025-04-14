package com.example.qeety;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
//import android.util.Log;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        ImageView dog = new ImageView(activity);
        dog.setImageResource(R.drawable.dog);
        dog.setId(View.generateViewId());
        dog.setTag("dog");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                250, 250 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(5, 0, 0, 0); // Marge à gauche

        dog.setLayoutParams(params);
        dog.setAlpha(0f);

        layout.addView(dog);

        dog.animate()
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
        ImageView arbre = new ImageView(activity);
        arbre.setImageResource(R.drawable.tree);
        arbre.setId(View.generateViewId());
        arbre.setTag("arbre");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(0, 0, 5, 0); // Marge à gauche

        arbre.setLayoutParams(params);
        arbre.setAlpha(0f);

        layout.addView(arbre);

        arbre.animate()
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
        ImageView leo = new ImageView(activity);
        leo.setImageResource(R.drawable.leo);
        leo.setId(View.generateViewId());
        leo.setTag("leo");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(0, 0, 5, 0); // Marge à gauche

        leo.setLayoutParams(params);
        leo.setAlpha(0f);

        layout.addView(leo);

        leo.animate()
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
        ImageView lili = new ImageView(activity);
        lili.setImageResource(R.drawable.lili);
        lili.setId(View.generateViewId());
        lili.setTag("lili");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(0, 0, 5, 0); // Marge à gauche

        lili.setLayoutParams(params);
        lili.setAlpha(0f);

        layout.addView(lili);

        lili.animate()
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
        ImageView zoe = new ImageView(activity);
        zoe.setImageResource(R.drawable.zoe);
        zoe.setId(View.generateViewId());
        zoe.setTag("zoe");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(3, 0, 0, 0); // Marge à gauche

        zoe.setLayoutParams(params);
        zoe.setAlpha(0f);

        layout.addView(zoe);

        zoe.animate()
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

    public static void showFenetre(Activity activity, RelativeLayout layout) {
        ImageView fenetre = new ImageView(activity);
        fenetre.setImageResource(R.drawable.vitre);
        fenetre.setId(View.generateViewId());
        fenetre.setTag("fenetre");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                600, 600 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL); // Centrer verticalement

        params.setMargins(3, 0, 0, 0); // Marge à gauche

        fenetre.setLayoutParams(params);
        fenetre.setAlpha(0f);

        layout.addView(fenetre);

        fenetre.animate()
                .alpha(1f)
                .translationXBy(30f) // petit effet de "pop"
                .setDuration(600)
                .start();
    }

    public static void removeFenetre(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("fenetre".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showHibou(Activity activity, RelativeLayout layout) {
        ImageView hibou = new ImageView(activity);
        hibou.setImageResource(R.drawable.hibou);
        hibou.setId(View.generateViewId());
        hibou.setTag("hibou");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        params.setMargins(5, 0, 0, 0); // ✅ Petite marge à gauche (collé mais pas "touché")

        hibou.setLayoutParams(params);
        hibou.setAlpha(0f);

        layout.addView(hibou);

        hibou.animate()
                .alpha(1f)
                .setDuration(600)
                .start();
    }


    public static void removeHibou(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("hibou".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showChat(Activity activity, RelativeLayout layout) {
        ImageView chat = new ImageView(activity);
        chat.setImageResource(R.drawable.chat);
        chat.setId(View.generateViewId());
        chat.setTag("chat");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        params.setMargins(0, 0, 5, 0); // ✅ Petite marge à gauche (collé mais pas "touché")

        chat.setLayoutParams(params);
        chat.setAlpha(0f);

        layout.addView(chat);

        chat.animate()
                .alpha(1f)
                .setDuration(600)
                .start();
    }


    public static void removeChat(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("chat".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showFleur(Activity activity, RelativeLayout layout) {
        ImageView fleur = new ImageView(activity);
        fleur.setImageResource(R.drawable.fleur);
        fleur.setId(View.generateViewId());
        fleur.setTag("fleur");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                300, 300 // ✅ Taille réduite
        );

        // ✅ Positionner à gauche, verticalement centré
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        params.setMargins(5, 0, 0, 0); // ✅ Petite marge à gauche (collé mais pas "touché")

        fleur.setLayoutParams(params);
        fleur.setAlpha(0f);

        layout.addView(fleur);

        fleur.animate()
                .alpha(1f)
                .setDuration(600)
                .start();
    }


    public static void removeFleur(RelativeLayout layout) {
        for (int i = layout.getChildCount() - 1; i >= 0; i--) {
            View view = layout.getChildAt(i);
            if ("fleur".equals(view.getTag())) {
                fadeOutAndRemove(view, layout);
            }
        }
    }

    public static void showFallingHearts(Context context, RelativeLayout layout, int numberOfHearts) {
        Random random = new Random();

        for (int i = 0; i < numberOfHearts; i++) {
            final ImageView heart = new ImageView(context);

            heart.setImageResource(R.drawable.coeur); // ton image de cœur
            heart.setLayoutParams(new RelativeLayout.LayoutParams(100, 100)); // taille du cœur
            heart.setTag("heart");

            // Position X aléatoire en haut de l'écran
            int x = random.nextInt(layout.getWidth());
            heart.setX(x);
            heart.setY(-100); // juste au-dessus de l'écran

            layout.addView(heart);

            // Animation de chute
            ObjectAnimator fall = ObjectAnimator.ofFloat(heart, "translationY", layout.getHeight() + 200f);
            fall.setDuration(3000 + random.nextInt(2000)); // durée entre 3 et 5s
            fall.setInterpolator(new AccelerateDecelerateInterpolator());

            // Léger mouvement horizontal
            ObjectAnimator sway = ObjectAnimator.ofFloat(heart, "translationX", x, x + random.nextInt(200) - 100);
            sway.setDuration(fall.getDuration());
            sway.setInterpolator(new AccelerateDecelerateInterpolator());

            // Fait disparaître le cœur à la fin
            fall.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layout.removeView(heart);
                }
            });

            AnimatorSet set = new AnimatorSet();
            set.playTogether(fall, sway);
            set.setStartDelay(random.nextInt(1000)); // délai aléatoire
            set.start();
        }
    }


    public static void showFallingStars(Context context, RelativeLayout layout, int numberOfStars) {
        Random random = new Random();

        for (int i = 0; i < numberOfStars; i++) {
            final ImageView etoile = new ImageView(context);

            etoile.setImageResource(R.drawable.stars); // ton image de cœur
            etoile.setLayoutParams(new RelativeLayout.LayoutParams(100, 100)); // taille du cœur
            etoile.setTag("etoile");

            // Position X aléatoire en haut de l'écran
            int x = random.nextInt(layout.getWidth());
            etoile.setX(x);
            etoile.setY(-100); // juste au-dessus de l'écran

            layout.addView(etoile);

            // Animation de chute
            ObjectAnimator fall = ObjectAnimator.ofFloat(etoile, "translationY", layout.getHeight() + 200f);
            fall.setDuration(3000 + random.nextInt(2000)); // durée entre 3 et 5s
            fall.setInterpolator(new AccelerateDecelerateInterpolator());

            // Léger mouvement horizontal
            ObjectAnimator sway = ObjectAnimator.ofFloat(etoile, "translationX", x, x + random.nextInt(200) - 100);
            sway.setDuration(fall.getDuration());
            sway.setInterpolator(new AccelerateDecelerateInterpolator());

            // Fait disparaître le cœur à la fin
            fall.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layout.removeView(etoile);
                }
            });

            AnimatorSet set = new AnimatorSet();
            set.playTogether(fall, sway);
            set.setStartDelay(random.nextInt(1000)); // délai aléatoire
            set.start();
        }
    }

    public static void showFallingSurprise(Context context, RelativeLayout layout, int numberOfSurprise) {
        Random random = new Random();

        for (int i = 0; i < numberOfSurprise; i++) {
            final ImageView surprise = new ImageView(context);

            surprise.setImageResource(R.drawable.astonished); // ton image de cœur
            surprise.setLayoutParams(new RelativeLayout.LayoutParams(100, 100)); // taille du cœur
            surprise.setTag("surprise");

            // Position X aléatoire en haut de l'écran
            int x = random.nextInt(layout.getWidth());
            surprise.setX(x);
            surprise.setY(-100); // juste au-dessus de l'écran

            layout.addView(surprise);

            // Animation de chute
            ObjectAnimator fall = ObjectAnimator.ofFloat(surprise, "translationY", layout.getHeight() + 200f);
            fall.setDuration(3000 + random.nextInt(2000)); // durée entre 3 et 5s
            fall.setInterpolator(new AccelerateDecelerateInterpolator());

            // Léger mouvement horizontal
            ObjectAnimator sway = ObjectAnimator.ofFloat(surprise, "translationX", x, x + random.nextInt(200) - 100);
            sway.setDuration(fall.getDuration());
            sway.setInterpolator(new AccelerateDecelerateInterpolator());

            // Fait disparaître les surprise à la fin
            fall.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layout.removeView(surprise);
                }
            });

            AnimatorSet set = new AnimatorSet();
            set.playTogether(fall, sway);
            set.setStartDelay(random.nextInt(1000)); // délai aléatoire
            set.start();
        }
    }

    public static void showFallingAnxius(Context context, RelativeLayout layout, int numberOfAnxius) {
        Random random = new Random();

        for (int i = 0; i < numberOfAnxius; i++) {
            final ImageView anxius = new ImageView(context);

            anxius.setImageResource(R.drawable.anxious); // ton image de cœur
            anxius.setLayoutParams(new RelativeLayout.LayoutParams(100, 100)); // taille du cœur
            anxius.setTag("anxius");

            // Position X aléatoire en haut de l'écran
            int x = random.nextInt(layout.getWidth());
            anxius.setX(x);
            anxius.setY(-100); // juste au-dessus de l'écran

            layout.addView(anxius);

            // Animation de chute
            ObjectAnimator fall = ObjectAnimator.ofFloat(anxius, "translationY", layout.getHeight() + 200f);
            fall.setDuration(3000 + random.nextInt(2000)); // durée entre 3 et 5s
            fall.setInterpolator(new AccelerateDecelerateInterpolator());

            // Léger mouvement horizontal
            ObjectAnimator sway = ObjectAnimator.ofFloat(anxius, "translationX", x, x + random.nextInt(200) - 100);
            sway.setDuration(fall.getDuration());
            sway.setInterpolator(new AccelerateDecelerateInterpolator());

            // Fait disparaître les anxius à la fin
            fall.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layout.removeView(anxius);
                }
            });

            AnimatorSet set = new AnimatorSet();
            set.playTogether(fall, sway);
            set.setStartDelay(random.nextInt(1000)); // délai aléatoire
            set.start();
        }
    }
}
