package com.example.qeety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.app.LocaleManager;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.RequiresApi;

import java.util.Locale;
import java.util.HashMap;

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends Activity {

    private HashMap<Integer, String> localeMap; // Associe chaque image à un code de langue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
