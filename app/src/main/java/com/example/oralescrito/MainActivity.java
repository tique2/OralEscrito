package com.example.oralescrito;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 3000; // 3 segundos
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView fondoImageView = findViewById(R.id.fondo);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Inicializar el reproductor de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.intro);

        // Reproducir el sonido
        mediaPlayer.start();

        new Handler().postDelayed(() -> {
            // Detener la reproducción y pasar a la siguiente actividad
            mediaPlayer.stop();
            mediaPlayer.release();
            startActivity(new Intent(MainActivity.this, Menu.class));
            finish();
        }, SPLASH_DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Liberar recursos del reproductor de audio
        }
    }
}
