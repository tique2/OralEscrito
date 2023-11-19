package com.example.oralescrito;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    private MediaPlayer mediaPlayerOral;
    private MediaPlayer mediaPlayerEscrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button botonOral = findViewById(R.id.botonOral);
        Button botonEscrito = findViewById(R.id.botonEscrito);

        // Inicializar los reproductores de audio
        mediaPlayerOral = MediaPlayer.create(this, R.raw.sound_oral);
        mediaPlayerEscrito = MediaPlayer.create(this, R.raw.sound_escrito);

        botonOral.setOnClickListener(v -> {
            // Reproducir el sonido correspondiente al botón Oral
            mediaPlayerOral.start();

            // Crear un intent para cambiar a la actividad OralActivity
            Intent intent = new Intent(Menu.this, Oral.class);
            startActivity(intent);
        });

        botonEscrito.setOnClickListener(v -> {
            // Reproducir el sonido correspondiente al botón Escrito
            mediaPlayerEscrito.start();

            // Crear un intent para cambiar a la actividad EscritoActivity
            Intent intent = new Intent(Menu.this, Escrito.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Liberar recursos de los reproductores de audio
        if (mediaPlayerOral != null) {
            mediaPlayerOral.release();
        }
        if (mediaPlayerEscrito != null) {
            mediaPlayerEscrito.release();
        }
    }
}
