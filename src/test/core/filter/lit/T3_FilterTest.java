package test.core.filter.lit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.lit.T3_Filter;

public final class T3_FilterTest {
	private static T3_Filter filter;

	@Before
	public void setup() {
		filter = new T3_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(), Model.T3);
	}

	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}

	@Test
	public void test_reject_oct() {
		String rawPattern = "\\007";
		assertFalse(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject_hex() {
		String rawPattern = "\\xF5";
		assertFalse(filter.accepts(rawPattern));
	}

	@Test
	public void test_accept_T3() {
		String rawPattern = "a[$]d";
		assertTrue(filter.accepts(rawPattern));
	}

}
