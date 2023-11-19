package com.example.oralescrito;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EscritoTest {
    private Escrito escritoActivity;

    @Before
    public void setUp() throws Exception {
        escritoActivity = new Escrito();
    }

    @Test
    public void testEscritoActivityNotNull() {
        assertNotNull(escritoActivity);
    }

    @Test
    public void testEditTextEscribirNotNull() {
        assertNotNull(escritoActivity.findViewById(R.id.editTextEscribir));
    }

    @Test
    public void testEditTextCorreccionNotNull() {
        assertNotNull(escritoActivity.findViewById(R.id.editTextCorreccion));
    }

    @Test
    public void testTextoBotonOrtografiaNotNull() {
        assertNotNull(escritoActivity.findViewById(R.id.texto_boton_ortografia));
    }

    @Test
    public void testBotonCompartirNotNull() {
        assertNotNull(escritoActivity.findViewById(R.id.botonCompartir));
    }

    @Test
    public void testBotonEscucharNotNull() {
        assertNotNull(escritoActivity.findViewById(R.id.botonEscuchar));
    }

    @Test
    public void testBotonMenuNotNull() {
        assertNotNull(escritoActivity.findViewById(R.id.botonMenu));
    }

    @Test
    public void testBuscarSugerencia() {
        String palabraIncorrecta = "helo";
        String sugerencia = escritoActivity.buscarSugerencia(palabraIncorrecta);
        assertEquals("hello", sugerencia);
    }

    @Test
    public void testReemplazarPuntuacion() {
        String palabraOriginal = "hello!";
        String sugerencia = "world";
        String palabraCorregida = escritoActivity.reemplazarPuntuacion(palabraOriginal, sugerencia);
        assertEquals("World!", palabraCorregida);
    }

    @After
    public void tearDown() throws Exception {
        escritoActivity = null;
    }
}
