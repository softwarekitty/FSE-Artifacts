package test.core.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * tests the base filter class, especially the getTokenStream function, for
 * input that would apply for filters that use it.
 * 
 * @author cc
 *
 */
public final class ConcreteAbstractFilterTest {
	private static ConcreteAbstractFilter filter;
	private static String ANY_NAME = "anyName";
	private static String ANY_DESCRIPTION = "anyDescription";

	@Before
	public void setup() {
		filter = new ConcreteAbstractFilter(ANY_NAME, ANY_DESCRIPTION);
	}

	@Test
	public void test_getName() {
		assertEquals(filter.getName(), ANY_NAME);
	}

	@Test
	public void test_descriptionNotNull() {
		assertEquals(filter.getDescription(), ANY_DESCRIPTION);
	}

	@Test
	public void test_getTokenStream_C1a() {
		String rawPattern = "[1-5]";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•RANGE•DOWN•1•5•UP•UP•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C1b() {
		String rawPattern = "[a-zE-T]";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•RANGE•DOWN•a•z•UP•RANGE•DOWN•E•T•UP•UP•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C2() {
		String rawPattern = "[eikz]";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•e•i•k•z•UP•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C4_WSP() {
		String rawPattern = "ab[\\s]1";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•b•UP•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•\\s•UP•UP•ELEMENT•DOWN•1•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C4_DEC() {
		String rawPattern = "ab[\\d]1";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•b•UP•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•\\d•UP•UP•ELEMENT•DOWN•1•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C4_WRD() {
		String rawPattern = "ab[\\w]1";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•b•UP•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•\\w•UP•UP•ELEMENT•DOWN•1•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C5_LIT() {
		String rawPattern = "a(b|f|h|1)c";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•CAPTURING_GROUP•DOWN•OR•DOWN•ALTERNATIVE•DOWN•ELEMENT•DOWN•b•UP•UP•ALTERNATIVE•DOWN•ELEMENT•DOWN•f•UP•UP•ALTERNATIVE•DOWN•ELEMENT•DOWN•h•UP•UP•ALTERNATIVE•DOWN•ELEMENT•DOWN•1•UP•UP•UP•UP•UP•ELEMENT•DOWN•c•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C5_DEC_WSP() {
		String rawPattern = "a(\\d|\\s)c";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•CAPTURING_GROUP•DOWN•OR•DOWN•ALTERNATIVE•DOWN•ELEMENT•DOWN•\\d•UP•UP•ALTERNATIVE•DOWN•ELEMENT•DOWN•\\s•UP•UP•UP•UP•UP•ELEMENT•DOWN•c•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_C5_RNG() {
		String rawPattern = "ab([a-f]|[x-z])1";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•b•UP•ELEMENT•DOWN•CAPTURING_GROUP•DOWN•OR•DOWN•ALTERNATIVE•DOWN•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•RANGE•DOWN•a•f•UP•UP•UP•UP•ALTERNATIVE•DOWN•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•RANGE•DOWN•x•z•UP•UP•UP•UP•UP•UP•UP•ELEMENT•DOWN•1•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_S2_LIT() {
		String rawPattern = "abbc";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•b•UP•ELEMENT•DOWN•b•UP•ELEMENT•DOWN•c•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_CG_ADD() {
		String rawPattern = "b(c+)d";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•b•UP•ELEMENT•DOWN•CAPTURING_GROUP•DOWN•ALTERNATIVE•DOWN•ELEMENT•DOWN•c•QUANTIFIER•DOWN•1•2147483647•ADDITIONAL•GREEDY•UP•UP•UP•UP•UP•ELEMENT•DOWN•d•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_RNG_DEC() {
		String rawPattern = "x[f-m]\\d";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•x•UP•ELEMENT•DOWN•CHARACTER_CLASS•DOWN•RANGE•DOWN•f•m•UP•UP•UP•ELEMENT•DOWN•\\d•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_OR_QST() {
		String rawPattern = "a(b|c|d)H?";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•CAPTURING_GROUP•DOWN•OR•DOWN•ALTERNATIVE•DOWN•ELEMENT•DOWN•b•UP•UP•ALTERNATIVE•DOWN•ELEMENT•DOWN•c•UP•UP•ALTERNATIVE•DOWN•ELEMENT•DOWN•d•UP•UP•UP•UP•UP•ELEMENT•DOWN•H•QUANTIFIER•DOWN•0•1•QUESTIONABLE•GREEDY•UP•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getTokenStream_NCG() {
		String rawPattern = "\\(a[^)]+\\)";
		String expected = "ALTERNATIVE•DOWN•ELEMENT•DOWN•(•UP•ELEMENT•DOWN•a•UP•ELEMENT•DOWN•NEGATED_CHARACTER_CLASS•DOWN•)•UP•QUANTIFIER•DOWN•1•2147483647•ADDITIONAL•GREEDY•UP•UP•ELEMENT•DOWN•)•UP•UP•EOF•";
		String actual = filter.exposeGetTokenStream(rawPattern);
		assertEquals(expected, actual);
	}

}