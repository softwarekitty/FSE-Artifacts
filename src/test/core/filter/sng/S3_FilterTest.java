package test.core.filter.sng;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.sng.S3_Filter;

public final class S3_FilterTest {
    private static S3_Filter filter;

    @Before
    public void setup(){
        filter = new S3_Filter();
    }

    @Test
    public void test_getName() {
        assertEquals(filter.getName(),Model.S3);
    }

    
	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}
	
	@Test
	public void test_accept() {
		String rawPattern = "ab{5,5}";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject() {
		String rawPattern = "ab{5}";
		assertFalse(filter.accepts(rawPattern));
	}
}
