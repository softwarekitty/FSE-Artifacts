package test.core.filter.dbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.dbb.D1_Filter;

public final class D1_FilterTest {
	private static D1_Filter filter;

	@Before
	public void setup(){
		filter = new D1_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(),Model.D1);
	}
	
	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}
	
	@Test
	public void test_accept() {
		String rawPattern = "b{1,2}";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject_sameBound() {
		String rawPattern = "b{4,4}";
		assertFalse(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject_noDBB() {
		String rawPattern = "bcd";
		assertFalse(filter.accepts(rawPattern));
	}
	

}
