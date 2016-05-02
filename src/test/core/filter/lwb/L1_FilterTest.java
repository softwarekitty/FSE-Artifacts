package test.core.filter.lwb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.lwb.L1_Filter;

public final class L1_FilterTest {
	private static L1_Filter filter;

	@Before
	public void setup() {
		filter = new L1_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(), Model.L1);
	}

	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}
	
	@Test
	public void test_accept() {
		String rawPattern = "Fg{4,}";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject() {
		String rawPattern = "ab[e-F]g";
		assertFalse(filter.accepts(rawPattern));
	}

}
