package com.infolangues.mondico;

import static com.infolangues.mondico.dictData.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.appcompat.app.AppCompatActivity;
/**
 * Created by bonnet on 29/03/2016.
 * display presentation picture and load data
 */
public class SplashScreen extends AppCompatActivity  {
    private final long debut = System.currentTimeMillis();
    private ProgressBar progressBar;
    private ExecutorService executorService;

    // version display SplashScreen while loading data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        executorService = Executors.newSingleThreadExecutor();

        System.out.println("SplashScreen after setContentView : " + (System.currentTimeMillis() - debut));

        /**
         * Showing splash screen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        loadDataFromDatabase();
        System.out.println("SplashScreen after loadDataFromDatabase : " + (System.currentTimeMillis() - debut));
    }
    // fin version

    private void loadDataFromDatabase() {       // CC 021024
        progressBar.setVisibility(View.VISIBLE); // Afficher le ProgressBar
        executorService.execute(() -> {
            // Charger les données de la base de données
            Context context = getApplicationContext();
            dictData.initData4usersFromSQL(context);
            // Mettre à jour l'UI sur le thread principal
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE); // Masquer le ProgressBar

                // Démarrer l'activité principale
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                System.out.println("SplashScreen after loadDataFromDatabase : " + (System.currentTimeMillis() - debut));
                finish(); // Terminer l'activité de splash
            });
        });
    }
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Fermer l'ExecutorService
    }

    // Afficher la barre de progression
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // Masquer la barre de progression
    private void hideProgressBar() { progressBar.setVisibility(View.GONE); }

    private void updateProgressBar(int progress) {
        progressBar.setProgress(progress);
        if (progress >= 100) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
