package com.example.qeety;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import java.util.Random;

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
    private ImageButton next;
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
        next = findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NextButton", "Bouton Next cliqué !");
                onNextClicked();
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

    private void onNextClicked() {
        // Exemple d'action : changer le texte de `phrase`
        phrase.setText("Nouveau texte après Next !");

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
