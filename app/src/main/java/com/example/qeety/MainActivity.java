package com.example.qeety;

import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.InputStream;

import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.app.LocaleManager;
import android.os.Build;
import android.os.LocaleList;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Locale;
import java.util.HashMap;

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends Activity {

    private HashMap<Integer, String> localeMap; // Associe chaque image à un code de langue
    private  HashMap<Integer,String> storyMap;
    private TextView phrase;
    private String voice = "F";
    MediaPlayer voiceAudio ;
    private ImageButton next,before;
    RelativeLayout mainLayout;
    private int storySelected = 0;
    private InputStream storyContent;
    String lang = "fr";

    private String[] text;
    private int textIndex = 0;

    private ImageButton anxiousButton;

    private ImageButton heartButton;

    private ImageButton starsButton;

    private ImageButton wowButton;

    private Handler audioStopHandler = new Handler();
    private Runnable stopAudioRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anxiousButton = findViewById(R.id.buttonAnxious);
        heartButton = findViewById(R.id.buttonHeart);
        starsButton = findViewById(R.id.buttonStars);
        wowButton = findViewById(R.id.buttonWow);

        anxiousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialise les propriétés au cas où
                anxiousButton.setScaleX(1f);
                anxiousButton.setScaleY(1f);
                anxiousButton.setAlpha(1f);

                // Lance l'animation
                anxiousButton.animate()
                        .scaleX(2f)
                        .scaleY(2f)
                        .alpha(0f)
                        .setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Remet le bouton à sa taille d’origine et visible
                                anxiousButton.setScaleX(1f);
                                anxiousButton.setScaleY(1f);
                                anxiousButton.setAlpha(1f);
                            }
                        })
                        .start();
                Animation.showFallingAnxius(MainActivity.this, mainLayout, 20);
            }
        });

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialise les propriétés au cas où
                heartButton.setScaleX(1f);
                heartButton.setScaleY(1f);
                heartButton.setAlpha(1f);

                // Lance l'animation
                heartButton.animate()
                        .scaleX(2f)
                        .scaleY(2f)
                        .alpha(0f)
                        .setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Remet le bouton à sa taille d’origine et visible
                                heartButton.setScaleX(1f);
                                heartButton.setScaleY(1f);
                                heartButton.setAlpha(1f);
                            }
                        })
                        .start();
                Animation.showFallingHearts(MainActivity.this, mainLayout, 20); // génère 20 cœurs
            }
        });

        starsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialise les propriétés au cas où
                starsButton.setScaleX(1f);
                starsButton.setScaleY(1f);
                starsButton.setAlpha(1f);

                // Lance l'animation
                starsButton.animate()
                        .scaleX(2f)
                        .scaleY(2f)
                        .alpha(0f)
                        .setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Remet le bouton à sa taille d’origine et visible
                                starsButton.setScaleX(1f);
                                starsButton.setScaleY(1f);
                                starsButton.setAlpha(1f);
                            }
                        })
                        .start();
                Animation.showFallingStars(MainActivity.this, mainLayout, 20); // génère 20 étoiles
            }
        });

        wowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialise les propriétés au cas où
                wowButton.setScaleX(1f);
                wowButton.setScaleY(1f);
                wowButton.setAlpha(1f);

                // Lance l'animation
                wowButton.animate()
                        .scaleX(2f)
                        .scaleY(2f)
                        .alpha(0f)
                        .setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Remet le bouton à sa taille d’origine et visible
                                wowButton.setScaleX(1f);
                                wowButton.setScaleY(1f);
                                wowButton.setAlpha(1f);
                            }
                        })
                        .start();
                Animation.showFallingSurprise(MainActivity.this, mainLayout, 20);
            }
        });

        mainLayout = findViewById(R.id.mainLayout);
        initLocalePicker();
        next = findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Commenter le if pour pouvoir cliquer vite sans attendre la fin de l'audio
                if(!voiceAudio.isPlaying()) {
                    if (textIndex < text.length - 1) {
                        textIndex++;
                    }
                    changeText();
                }



            }
        });
        before = findViewById(R.id.previous_btn);
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Commenter le if pour pouvoir cliquer vite sans attendre la fin de l'audio
                if(!voiceAudio.isPlaying()) {
                    if (textIndex != 0) {
                        textIndex--;
                    }
                    changeText();
                }
            }
        });




        phrase = findViewById(R.id.phrase);
        changeFiles();
    }
    private void changeText(){
        String[] buffer = text[textIndex].split("@//@");

        // Changer le texte
        phrase.setText(buffer[1]);
        verifsianim(text[textIndex]);

        // Nettoyer un éventuel ancien timer
        if (stopAudioRunnable != null) {
            audioStopHandler.removeCallbacks(stopAudioRunnable);
        }

        // Lire l'audio si disponible
        if (buffer.length > 2 && !buffer[2].trim().isEmpty()) {
            String time = buffer[2].trim();
            int startTime = (int)(Float.parseFloat(time) * 1000); // secondes → ms

            // Stopper et remettre à zéro l'audio s'il joue
            if (voiceAudio != null) {
                if (voiceAudio.isPlaying()) {
                    voiceAudio.pause();
                }
                voiceAudio.seekTo(0); // ou .reset() si tu veux tout changer
            }

            // Lancer l'audio au bon endroit
            voiceAudio.seekTo(startTime);
            voiceAudio.start();

            // Définir le moment où on doit arrêter
            if (textIndex + 1 < text.length) {
                String[] buffer_next = text[textIndex + 1].split("@//@");
                if (buffer_next.length > 2 && !buffer_next[2].trim().isEmpty()) {
                    int endTime = (int)(Float.parseFloat(buffer_next[2].trim()) * 1000);

                    stopAudioRunnable = () -> {
                        if (voiceAudio != null && voiceAudio.isPlaying()) {
                            voiceAudio.pause();
                        }
                    };

                    audioStopHandler.postDelayed(stopAudioRunnable, endTime - startTime);
                }
            }
        }
    }



    private void initLocalePicker() {
        Spinner spinner = findViewById(R.id.localePicker);
        Spinner voiceChoice = findViewById(R.id.voicePicker);
        Spinner storyChoice = findViewById(R.id.storyPicker);

        // Tableau des images de drapeaux
        int[] flagImages = {
                R.drawable.fr,  // Français
                R.drawable.gb,  // Anglais
        };

        int[] voiceImage = {
                R.drawable.icone_femme, //Femme
                R.drawable.icone_homme  //Homme
        };

        int[] storyText = {
                R.string.story1, //Titre de l'histoire 1
                R.string.story2,
                R.string.story3,
                R.string.story4,
                R.string.story5,
        };

        // Associer les positions aux codes de langue
        localeMap = new HashMap<>();
        localeMap.put(0, "fr-FR");
        localeMap.put(1, "en-US");


        ImageSpinnerAdapter adapter = new ImageSpinnerAdapter(this, flagImages);
        spinner.setAdapter(adapter);

        ImageSpinnerAdapter adapterVoice = new ImageSpinnerAdapter(this,voiceImage);
        voiceChoice.setAdapter(adapterVoice);

        StorySelectorAdapter adapterStory = new StorySelectorAdapter(this,storyText);
        storyChoice.setAdapter(adapterStory);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocale = localeMap.get(position);
                if (position == 0) {
                    lang = "fr";
                } else if (position == 1) {
                    lang = "en";
                }
                if(selectedLocale != null){
                    LocaleManager localeManager = getSystemService(LocaleManager.class);
                    localeManager.setApplicationLocales(new LocaleList(Locale.forLanguageTag(selectedLocale)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        voiceChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    //Cas pour la voix femme
                    voice = "F";
                }
                else{
                    voice="H";
                }
                changeFiles();
                java.util.Scanner s = new java.util.Scanner(storyContent).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                text = result.split("\n");
                textIndex = 0;
                changeText();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        storyChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                storySelected =position;
                changeFiles();
                java.util.Scanner s = new java.util.Scanner(storyContent).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                text = result.split("\n");
                textIndex = 0;
                changeText();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void changeFiles(){
        if (voiceAudio != null) {
            if (voiceAudio.isPlaying()) {
                voiceAudio.stop();
            }
            voiceAudio.release();
            voiceAudio = null;
        }
        if(voice.equals("F")){
            if(lang.equals("fr")){
                switch (storySelected){
                    case 0:
                        storyContent = getResources().openRawResource(R.raw.fr_story1_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio1_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 1:
                        storyContent = getResources().openRawResource(R.raw.fr_story2_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio2_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 2:
                        storyContent = getResources().openRawResource(R.raw.fr_story3_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio3_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 3:
                        storyContent = getResources().openRawResource(R.raw.fr_story4_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio4_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage_nuit);

                        break;
                    case 4:
                        storyContent = getResources().openRawResource(R.raw.fr_story5_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio5_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                }
            } else if (lang.equals("en")) {
                switch (storySelected){
                    case 0:
                        storyContent = getResources().openRawResource(R.raw.en_story1_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio1_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 1:
                        storyContent = getResources().openRawResource(R.raw.en_story2_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio2_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 2:
                        storyContent = getResources().openRawResource(R.raw.en_story3_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio3_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 3:
                        storyContent = getResources().openRawResource(R.raw.en_story4_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio4_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage_nuit);
                        break;
                    case 4:
                        storyContent = getResources().openRawResource(R.raw.en_story5_f);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio5_f);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                }
            }
        } else if (voice.equals("H")) {
            if(lang.equals("fr")){
                switch (storySelected){
                    case 0:
                        storyContent = getResources().openRawResource(R.raw.fr_story1_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio1_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 1:
                        storyContent = getResources().openRawResource(R.raw.fr_story2_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio2_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 2:
                        storyContent = getResources().openRawResource(R.raw.fr_story3_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio3_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 3:
                        storyContent = getResources().openRawResource(R.raw.fr_story4_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio4_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage_nuit);
                        break;
                    case 4:
                        storyContent = getResources().openRawResource(R.raw.fr_story5_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.fr_audio5_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                }
            } else if (lang.equals("en")) {
                switch (storySelected){
                    case 0:
                        storyContent = getResources().openRawResource(R.raw.en_story1_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio1_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 1:
                        storyContent = getResources().openRawResource(R.raw.en_story2_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio2_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 2:
                        storyContent = getResources().openRawResource(R.raw.en_story3_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio3_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                    case 3:
                        storyContent = getResources().openRawResource(R.raw.en_story4_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio4_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage_nuit);
                        break;
                    case 4:
                        storyContent = getResources().openRawResource(R.raw.en_story5_h);
                        voiceAudio = MediaPlayer.create(getBaseContext(), R.raw.en_audio5_h);
                        mainLayout.setBackgroundResource(R.drawable.paysage);
                        break;
                }
            }
        }
    }

    private void verifsianim(String texte) {
        if (texte == null || texte.isEmpty()) return;

        // Gestion des animations
        if (texte.contains("[papillon jaune]")) {
            Log.d("verifsianim", "Détection de '[papillon jaune]' → Génération de papillons !");
            Animation.generateButterflies(this, mainLayout, 5);
        }

        if (texte.contains("[/papillon jaune]")) {
            Log.d("verifsianim", "Détection de '[/papillon jaune]' → Disparition de papillons !");
            Animation.removeButterfliesWithAnimation(mainLayout);
        }

        if (texte.contains("[grenouille]")) {
            Log.d("verifsianim", "Détection de 'grenouille' → TODO : Ajouter animation");
            Animation.showFrog(this,mainLayout);
        }

        if (texte.contains("[/grenouille]")) {
            Log.d("verifsianim", "Détection de 'grenouille' → TODO : Ajouter animation");
            Animation.removeFrog(mainLayout);
        }

        if (texte.contains("[arc-en-ciel]")) {
            Log.d("verifsianim", "Détection de 'arc-en-ciel' → TODO : Ajouter animation");
            Animation.showRainbowGif(this, mainLayout);
        }
        if (texte.contains("[/arc-en-ciel]")) {
            Log.d("verifsianim", "Disparition de l'arc-en-ciel détectée");
            Animation.removeRainbow(mainLayout);
        }


        if (texte.contains("[arbre]")) {
            Log.d("verifsianim", "Détection de 'arbre' → TODO : Ajouter animation");
            Animation.showArbre(this,mainLayout);
        }

        if (texte.contains("[/arbre]")) {
            Log.d("verifsianim", "Détection de 'arbre' → TODO : Disparition animation");
            Animation.removeArbre(mainLayout);
        }

        if (texte.contains("[fleur]")) {
            Log.d("verifsianim", "Détection de 'fleur' → TODO : Ajouter animation");
            Animation.showFleur(this,mainLayout);
        }

        if (texte.contains("[/fleur]")) {
            Log.d("verifsianim", "Détection de 'fleur' → TODO : Disparition animation");
            Animation.removeFleur(mainLayout);
        }

        if (texte.contains("[chien]")) {
            Log.d("verifsianim", "Détection de 'chien' → TODO : Ajouter animation");
            Animation.showDog(this,mainLayout);
        }

        if (texte.contains("[/chien]")) {
            Log.d("verifsianim", "Détection de 'chien' → TODO : Disparition animation");
            Animation.removeDog(mainLayout);
        }
        if (texte.contains("[chat]")) {
            Log.d("verifsianim", "Détection de 'chat' → TODO : Ajouter animation");
            Animation.showChat(this,mainLayout);
        }

        if (texte.contains("[/chat]")) {
            Log.d("verifsianim", "Détection de 'chat' → TODO : Disparition animation");
            Animation.removeChat(mainLayout);
        }
        if (texte.contains("[hibou]")) {
            Log.d("verifsianim", "Détection de 'hibou' → TODO : Ajouter animation");
            Animation.showHibou(this,mainLayout);
        }

        if (texte.contains("[/hibou]")) {
            Log.d("verifsianim", "Détection de 'hibou' → TODO : Disparition animation");
            Animation.removeHibou(mainLayout);
        }
        if (texte.contains("[fenetre]")) {
            Log.d("verifsianim", "Détection de 'fenetre' → TODO : Ajouter animation");
            Animation.showFenetre(this,mainLayout);
        }

        if (texte.contains("[/fenetre]")) {
            Log.d("verifsianim", "Détection de 'fenetre' → TODO : Disparition animation");
            Animation.removeFenetre(mainLayout);
        }
        if (texte.contains("[zoe]")) {
            Log.d("verifsianim", "Détection de 'zoe' → TODO : Ajouter animation");
            Animation.showZoe(this,mainLayout);
        }

        if (texte.contains("[/zoe]")) {
            Log.d("verifsianim", "Détection de 'zoe' → TODO : Disparition animation");
            Animation.removeZoe(mainLayout);
        }
        if (texte.contains("[lili]")) {
            Log.d("verifsianim", "Détection de 'lili' → TODO : Ajouter animation");
            Animation.showLili(this,mainLayout);
        }

        if (texte.contains("[/lili]")) {
            Log.d("verifsianim", "Détection de 'lili' → TODO : Disparition animation");
            Animation.removeLili(mainLayout);
        }
        if (texte.contains("[leo]")) {
            Log.d("verifsianim", "Détection de 'leo' → TODO : Ajouter animation");
            Animation.showLeo(this,mainLayout);
        }

        if (texte.contains("[/leo]")) {
            Log.d("verifsianim", "Détection de 'leo' → TODO : Disparition animation");
            Animation.removeLeo(mainLayout);
        }

        // Détection des émotions
        detectEmotions(texte);
    }

    // Méthode pour détecter et loguer les émotions

    private void detectEmotions(String texte) {
        // Récupérer l'ImageView qui affichera les émotions
        ImageView emotionImageView = findViewById(R.id.imageView2);

        // Associer chaque émotion à une ressource drawable
        HashMap<String, Integer> emotionImages = new HashMap<>();

        //emotionImages.put("(b)", R.drawable.baillement);    // Baillement
        emotionImages.put("(p)", R.drawable.qt_triste);       // Pleurs
        emotionImages.put("(c)", R.drawable.qt_colere);       // Colère
        //emotionImages.put("(tt)", R.drawable.tourne_tete); // Tourne la tête
        //emotionImages.put("(thaut)", R.drawable.leve_tete); // Lève la tête
        emotionImages.put("(e)", R.drawable.qt_surpris);  // Saut avec exclamation
        emotionImages.put("(et)", R.drawable.qt_langue);      // Étoile
        emotionImages.put("(s)", R.drawable.qt_omg);      // Stupeur
        emotionImages.put("(sou)", R.drawable.qt_content);    // Sourire
        emotionImages.put("(soupir)", R.drawable.qt_decu);  // Soupir
        emotionImages.put("(l)", R.drawable.qt_timide);         // Love
        emotionImages.put("(t)", R.drawable.qt_peur);       // Triste
        emotionImages.put("(cc)", R.drawable.qt_bonheur);      // Coucou
        emotionImages.put("(int)", R.drawable.qt_suspicieux); // Interrogation
        emotionImages.put("(hatchoum)", R.drawable.qt_honte); // Enrumer

        // Vérifier si le texte contient une émotion et mettre à jour l'image
        for (String emotion : emotionImages.keySet()) {
            if (texte.contains(emotion)) {
                Log.d("detectEmotions", "Détection de '" + emotion + "' → Changement d'image !");
                emotionImageView.setImageResource(emotionImages.get(emotion));
                emotionImageView.setVisibility(View.VISIBLE); // Afficher l'émotion
                return; // Stop après la première détection pour éviter les conflits
            }
        }

        // Si aucune émotion détectée, cacher l'image
        emotionImageView.setVisibility(View.VISIBLE);
    }
}