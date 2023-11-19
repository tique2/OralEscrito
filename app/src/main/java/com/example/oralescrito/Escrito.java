package com.example.oralescrito;

import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler; // Agregado para el retraso
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Escrito extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private EditText editTextEscribir;
    private EditText editTextCorreccion;
    private TextToSpeech textToSpeech;
    private List<String> dictionary;
    private List<String> customWords;
    private Button botonEscuchar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrito);

        editTextEscribir = findViewById(R.id.editTextEscribir);
        editTextCorreccion = findViewById(R.id.editTextCorreccion);
        Button textoBotonOrtografia = findViewById(R.id.texto_boton_ortografia);
        Button botonCompartir = findViewById(R.id.botonCompartir);
        botonEscuchar = findViewById(R.id.botonEscuchar);
        Button botonMenu = findViewById(R.id.botonMenu);

        editTextEscribir.setMaxLines(10);
        editTextCorreccion.setMaxLines(10);

        dictionary = loadDictionary();

        textToSpeech = new TextToSpeech(getApplicationContext(), this);

        customWords = loadCustomWords();
        Collections.sort(customWords);

        textoBotonOrtografia.setOnClickListener(v -> {
            corregirOrtografia();
            playSound(R.raw.sound_ortografia);
        });

        botonCompartir.setOnClickListener(v -> {
            compartirTexto();
            playSound(R.raw.sound_compartir);
        });

        botonEscuchar.setOnClickListener(v -> {
            // Reproducir el sonido primero
            playSound(R.raw.sound_escuchar);

            // Retrasar la acción de hablar en 3 segundos
            new Handler().postDelayed(() -> {
                String textoEscuchar = editTextCorreccion.getText().toString();
                if (!TextUtils.isEmpty(textoEscuchar)) {
                    textToSpeech.speak(textoEscuchar, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    Toast.makeText(this, "No hay texto para escuchar.", Toast.LENGTH_SHORT).show();
                }
            }, 3000); // 3000 milisegundos = 3 segundos
        });

        botonMenu.setOnClickListener(v -> abrirMenu());
    }

    private void abrirMenu() {
        Intent intent = new Intent(Escrito.this, Menu.class);
        startActivity(intent);
        playSound(R.raw.sound_menu);
    }

    private List<String> loadDictionary() {
        List<String> dictionary = new ArrayList<>();
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("es.dic");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim().toLowerCase(Locale.getDefault()));
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    private List<String> loadCustomWords() {
        List<String> customWords = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("es.dic");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                customWords.add(line.trim().toLowerCase(Locale.getDefault()));
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customWords;
    }

    private void saveCustomWord(String word) {
        if (!customWords.contains(word)) {
            try {
                FileOutputStream fos = openFileOutput("es.dic", MODE_APPEND);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
                writer.write(word);
                writer.newLine();
                writer.close();
                customWords.add(word);
                Collections.sort(customWords);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void corregirOrtografia() {
        String textoEntrada = editTextEscribir.getText().toString();
        String[] parrafosEntrada = textoEntrada.split("\n\n");

        StringBuilder textoCorregido = new StringBuilder();

        for (String parrafoEntrada : parrafosEntrada) {
            String[] palabrasEntrada = parrafoEntrada.split("\\s+");

            StringBuilder parrafoCorregido = new StringBuilder();

            for (String palabraEntrada : palabrasEntrada) {
                String palabraSinPuntuacion = palabraEntrada.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]", "").toLowerCase(Locale.getDefault());

                if (!TextUtils.isEmpty(palabraSinPuntuacion)) {
                    String sugerencia = buscarSugerencia(palabraSinPuntuacion);

                    if (!TextUtils.isEmpty(sugerencia)) {
                        // Corregir la ortografía y mantener la puntuación original
                        String palabraCorregida = reemplazarPuntuacion(palabraEntrada, StringUtils.capitalize(sugerencia));
                        parrafoCorregido.append(palabraCorregida).append(" ");
                    } else {
                        // Mantener la palabra original y guardar solo las bien escritas
                        parrafoCorregido.append(palabraEntrada).append(" ");
                        if (dictionary.contains(palabraSinPuntuacion)) {
                            saveCustomWord(palabraSinPuntuacion);
                        }
                    }
                }
            }

            textoCorregido.append(parrafoCorregido.toString().trim()).append("\n\n");
        }

        editTextCorreccion.setText(textoCorregido.toString().trim());
        resizeEditText(editTextCorreccion);
    }


    protected String buscarSugerencia(String palabraIncorrecta) {
        String sugerenciaOptima = palabraIncorrecta;
        int menorDistancia = Integer.MAX_VALUE;

        for (String palabraDiccionario : dictionary) {
            int distancia = LevenshteinDistance.getDefaultInstance().apply(palabraIncorrecta, palabraDiccionario);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                sugerenciaOptima = palabraDiccionario;
            }
        }

        return sugerenciaOptima;
    }

    protected String reemplazarPuntuacion(String palabraOriginal, String sugerencia) {
        StringBuilder puntuacion = new StringBuilder();
        int index = palabraOriginal.length() - 1;

        while (index >= 0 && !Character.isLetter(palabraOriginal.charAt(index))) {
            puntuacion.insert(0, palabraOriginal.charAt(index));
            index--;
        }

        return StringUtils.capitalize(sugerencia) + puntuacion;
    }

    private void compartirTexto() {
        String textoCompartir = editTextCorreccion.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textoCompartir);
        startActivity(Intent.createChooser(intent, "Compartir texto"));
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Idioma no soportado.", Toast.LENGTH_SHORT).show();
            } else {
                botonEscuchar.setEnabled(true);
            }
        } else {
            Toast.makeText(this, "Error al inicializar el motor de texto a voz.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void resizeEditText(EditText editText) {
        editText.post(() -> {
            int lineCount = editText.getLineCount();
            int lineHeight = editText.getLineHeight();
            int paddingTop = editText.getPaddingTop();
            int paddingBottom = editText.getPaddingBottom();
            int totalHeight = (lineCount * lineHeight) + paddingTop + paddingBottom;
            editText.setHeight(totalHeight);
        });
    }

    private void playSound(int soundResource) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResource);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
    }
}