package test.core.filter.lwb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.lwb.L2_Filter;

public final class L2_FilterTest {
	private static L2_Filter filter;

	@Before
	public void setup() {
		filter = new L2_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(), Model.L2);
	}

	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}
	
	@Test
	public void test_accept() {
		String rawPattern = "ab*";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject() {
		String rawPattern = "ab+";
		assertFalse(filter.accepts(rawPattern));
	}

}
