package com.example.oralescrito;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Oral extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 100;

    private EditText editTextEscribir;
    private TextToSpeech textToSpeech;
    private MediaPlayer mediaPlayerVoz;
    private MediaPlayer mediaPlayerCompartir;
    private MediaPlayer mediaPlayerEscuchar;
    private MediaPlayer mediaPlayerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oral);

        editTextEscribir = findViewById(R.id.editTextEscribir);
        Button botonVoz = findViewById(R.id.botonVoz);
        Button botonCompartir = findViewById(R.id.botonCompartir);
        Button botonEscuchar = findViewById(R.id.botonEscuchar);
        Button botonMenu = findViewById(R.id.botonMenu);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());

                if (result == TextToSpeech.LANG_MISSING_DATA) {
                    // Si falta información del idioma, puedes intentar descargarla.
                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Si el idioma no es compatible, puedes seleccionar otro idioma o mostrar un mensaje de error al usuario.
                    textToSpeech.setLanguage(Locale.US);  // Ejemplo: Configuración en inglés
                    // O puedes mostrar un mensaje de error al usuario
                    Toast.makeText(this, "Idioma no compatible. Se ha configurado el idioma en inglés.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mediaPlayerVoz = MediaPlayer.create(this, R.raw.sound_voz);
        mediaPlayerCompartir = MediaPlayer.create(this, R.raw.sound_compartir);
        mediaPlayerEscuchar = MediaPlayer.create(this, R.raw.sound_escuchar);
        mediaPlayerMenu = MediaPlayer.create(this, R.raw.sound_menu);

        botonVoz.setOnClickListener(v -> {
            mediaPlayerVoz.start();
            iniciarEntradaDeVoz();
        });

        botonCompartir.setOnClickListener(v -> {
            mediaPlayerCompartir.start();
            compartirTexto();
        });

        botonEscuchar.setOnClickListener(v -> {
            mediaPlayerEscuchar.start();
            new android.os.Handler().postDelayed(this::pronunciarTexto, 3000); // Retraso de 3 segundos (3000 milisegundos)
        });

        botonMenu.setOnClickListener(v -> {
            mediaPlayerMenu.start();
            abrirOtraActividad();
        });
    }

    private void iniciarEntradaDeVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla algo...");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Reconocimiento de voz no compatible en tu dispositivo.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                editTextEscribir.setText(spokenText);
            }
        }
    }

    private void compartirTexto() {
        String texto = editTextEscribir.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);
        startActivity(Intent.createChooser(intent, "Compartir usando"));
    }

    private void pronunciarTexto() {
        String texto = editTextEscribir.getText().toString();
        try {
            if (!textToSpeech.isSpeaking()) {
                textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Motor TTS no disponible en tu dispositivo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirOtraActividad() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (mediaPlayerVoz != null) {
            mediaPlayerVoz.release();
        }
        if (mediaPlayerCompartir != null) {
            mediaPlayerCompartir.release();
        }
        if (mediaPlayerEscuchar != null) {
            mediaPlayerEscuchar.release();
        }
        if (mediaPlayerMenu != null) {
            mediaPlayerMenu.release();
        }
    }
}