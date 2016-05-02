package test.core.filter.ccc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.ccc.C1_Filter;

public final class C1_FilterTest {
	private static C1_Filter filter;

	@Before
	public void setup(){
		filter = new C1_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(),Model.C1);
	}
	
	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}
	
	@Test
	public void test_accept() {
		String rawPattern = "a[1-9]b";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject() {
		String rawPattern = "a[abc]b";
		assertFalse(filter.accepts(rawPattern));
	}

}
