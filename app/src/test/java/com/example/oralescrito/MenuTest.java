package com.example.oralescrito;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MenuTest {
    private Menu menuActivity;

    @Before
    public void setUp() throws Exception {
        menuActivity = new Menu();
    }

    @Test
    public void testMenuActivityNotNull() {
        assertNotNull(menuActivity);
    }

    @Test
    public void testButtonOralNotNull() {
        assertNotNull(menuActivity.findViewById(R.id.botonOral));
    }

    @Test
    public void testButtonEscritoNotNull() {
        assertNotNull(menuActivity.findViewById(R.id.botonEscrito));
    }

    @After
    public void tearDown() throws Exception {
        menuActivity = null;
    }
}
