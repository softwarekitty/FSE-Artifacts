package test.core.filter.ccc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.ModelNames;
import main.core.filter.ccc.C4_Filter;

public final class C4_FilterTest {
	private static C4_Filter filter;

	@Before
	public void setup() {
		filter = new C4_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(), ModelNames.C4);
	}

	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}

	@Test
	public void test_accept() {
		String rawPattern = "a[\\d]b";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject() {
		String rawPattern = "a[1-9]b";
		assertFalse(filter.accepts(rawPattern));
	}

}
