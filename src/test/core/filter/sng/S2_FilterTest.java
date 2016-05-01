package test.core.filter.sng;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.ModelNames;
import main.core.filter.sng.S2_Filter;

public final class S2_FilterTest {
    private static S2_Filter filter;

    @Before
    public void setup(){
        filter = new S2_Filter();
    }

    @Test
    public void test_getName() {
        assertEquals(filter.getName(),ModelNames.S2);
    }
    
	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}
	
	@Test
	public void test_accept() {
		String rawPattern = "abb";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject() {
		String rawPattern = "ab{5}";
		assertFalse(filter.accepts(rawPattern));
	}

}
