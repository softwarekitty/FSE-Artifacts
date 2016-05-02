package test.core.filter.dbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.core.filter.Model;
import main.core.filter.dbb.D3_Filter;

public final class D3_FilterTest {
	private static D3_Filter filter;

	@Before
	public void setup(){
		filter = new D3_Filter();
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(),Model.D3);
	}
	
	@Test
	public void test_descriptionNotNull() {
		assertNotNull(filter.getDescription());
	}
	
	@Test
	public void test_accept() {
		String rawPattern = "c|cc";
		assertTrue(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject_sameBound() {
		String rawPattern = "abc|abc";
		assertFalse(filter.accepts(rawPattern));
	}

	@Test
	public void test_reject_noOR() {
		String rawPattern = "bcd";
		assertFalse(filter.accepts(rawPattern));
	}

}
