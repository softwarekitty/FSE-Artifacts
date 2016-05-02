package test.core.filter.dbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.dbb.D2_Filter;

public final class D2_FilterTest {
	private static D2_Filter filter;

	@Before
	public void setup() {
		filter = new D2_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(), Model.D2);
	}

	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}

	@Test
	public void test_accept() {
		String rawPattern = "ab?c";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject_noQST() {
		String rawPattern = "bcd";
		assertFalse(filter.accepts(rawPattern));
	}

}
