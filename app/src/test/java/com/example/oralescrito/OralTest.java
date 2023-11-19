package com.example.oralescrito;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class OralTest {
    private Oral oralActivity;

    @Before
    public void setUp() throws Exception {
        oralActivity = new Oral();
    }

    @Test
    public void testOralActivityNotNull() {
        assertNotNull(oralActivity);
    }

    @Test
    public void testEditTextEscribirNotNull() {
        assertNotNull(oralActivity.findViewById(R.id.editTextEscribir));
    }

    @Test
    public void testButtonVozNotNull() {
        assertNotNull(oralActivity.findViewById(R.id.botonVoz));
    }

    @Test
    public void testButtonCompartirNotNull() {
        assertNotNull(oralActivity.findViewById(R.id.botonCompartir));
    }

    @Test
    public void testButtonEscucharNotNull() {
        assertNotNull(oralActivity.findViewById(R.id.botonEscuchar));
    }

    @Test
    public void testButtonMenuNotNull() {
        assertNotNull(oralActivity.findViewById(R.id.botonMenu));
    }

    @After
    public void tearDown() throws Exception {
        oralActivity = null;
    }
}
