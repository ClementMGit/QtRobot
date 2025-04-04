package com.example.qeety;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.util.Random;

import com.example.qeety.Animation;
import android.os.Handler;
import android.os.Looper;
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

import com.bumptech.glide.Glide;

import java.util.Locale;
import java.util.HashMap;

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends Activity {
    private final int NUM_BUTTERFLIES = 5; // Nombre de papillons

    private HashMap<Integer, String> localeMap; // Associe chaque image à un code de langue
    private  HashMap<Integer,String> storyMap;
    private TextView phrase;
    private String lastText = "";
    private String voice = "F";
    private ImageButton next,before;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout); // Assurez-vous que l'ID est bien défini

        if (mainLayout == null) {
            throw new NullPointerException("mainLayout is null! Check activity_main.xml.");
        }
        /////////////////////generateButterflies();  // Génère plusieurs papillons avec animation

        initLocalePicker();
        next = findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NextButton", "Bouton Next cliqué !");
                onNextClicked();
            }
        });
        before = findViewById(R.id.previous_btn);
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Previous", "Bouton Previous cliqué !");
                onBeforeClicked();
            }
        });
        phrase = findViewById(R.id.phrase);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentText = phrase.getText().toString();
                if (!currentText.equals(lastText)) {
                    lastText = currentText;
                    onTextChanged(currentText);
                }
                handler.postDelayed(this, 500); // Vérifie toutes les 500ms
            }
        }, 500);
    }

    private void onBeforeClicked() {
        phrase.setText(" (c) Nouveau texte après Next [/papillon jaune] [arc-en-ciel] !");
    }

    private void onNextClicked() {
        // Exemple d'action : changer le texte de `phrase`
        phrase.setText(" (int) Nouveau texte après Next [papillon jaune] [/arc-en-ciel] !");

        // Ajoute ici ce que tu veux faire quand on clique sur Next
    }


    private void initLocalePicker() {
        Spinner spinner = findViewById(R.id.localePicker);
        Spinner voiceChoice = findViewById(R.id.voicePicker);
        Spinner storyChoice = findViewById(R.id.storyPicker);

        // Tableau des images de drapeaux
        int[] flagImages = {
                R.drawable.fr,  // Français
                R.drawable.gb,  // Anglais
                R.drawable.es   // Espagnol
        };

        int[] voiceImage = {
                R.drawable.icone_femme, //Femme
                R.drawable.icone_homme  //Homme
        };

        int[] storyText = {
                R.string.story1, //Titre de l'histoire 1
                R.string.story2  //Titre de l'histoire 2
        };

        // Associer les positions aux codes de langue
        localeMap = new HashMap<>();
        localeMap.put(0, "fr-FR");
        localeMap.put(1, "en-US");
        localeMap.put(2, "es-ES");

        storyMap = new HashMap<>();
        storyMap.put(0,"story1");
        storyMap.put(1,"story2");


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
                    Log.d("voice Change", "Voice changed to Femme (\"F\") ");
                } else if (position == 1) {
                    //Cas pour le voix homme
                    voice = "H";
                    Log.d("voice Change", "Voice changed to MEN (\"H\") ");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        storyChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onTextChanged(String newText) {
        Log.d("TextChange", "Le texte a changé : " + newText);
        // Ajoute ici l'action à effectuer quand le texte change
        verifsianim(newText);

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

        if (texte.contains("grenouille")) {
            Log.d("verifsianim", "Détection de 'grenouille' → TODO : Ajouter animation");
        }

        if (texte.contains("[arc-en-ciel]")) {
            Log.d("verifsianim", "Détection de 'arc-en-ciel' → TODO : Ajouter animation");
            Animation.showRainbowGif(this, mainLayout);
        }
        if (texte.contains("[/arc-en-ciel]")) {
            Log.d("verifsianim", "Disparition de l'arc-en-ciel détectée");
            Animation.removeRainbow(mainLayout);
        }


        if (texte.contains("arbre")) {
            Log.d("verifsianim", "Détection de 'arbre' → TODO : Ajouter animation");
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
