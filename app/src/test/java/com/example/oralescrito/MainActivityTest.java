package com.example.oralescrito;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class MainActivityTest {
    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        mainActivity = new MainActivity();
    }

    @Test
    public void testMainActivityNotNull() {
        assertNotNull(mainActivity);
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}
