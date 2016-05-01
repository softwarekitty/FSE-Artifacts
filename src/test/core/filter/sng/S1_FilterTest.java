package test.core.filter.sng;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.ModelNames;
import main.core.filter.sng.S1_Filter;

public final class S1_FilterTest {
	private static S1_Filter filter;

	@Before
	public void setup() {
		filter = new S1_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(), ModelNames.S1);
	}

	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}

	@Test
	public void test_accept() {
		String rawPattern = "ab{6}";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject() {
		String rawPattern = "ab{4,5}";
		assertFalse(filter.accepts(rawPattern));
	}

}
