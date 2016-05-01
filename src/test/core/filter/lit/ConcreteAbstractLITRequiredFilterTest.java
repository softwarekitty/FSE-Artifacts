package test.core.filter.lit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public final class ConcreteAbstractLITRequiredFilterTest {
    private static ConcreteAbstractLITRequiredFilter filter;
	private static String ANY_NAME = "anyName";
	private static String ANY_DESCRIPTION = "anyDescription";


    @Before
    public void setup(){
        filter = new ConcreteAbstractLITRequiredFilter(ANY_NAME,ANY_DESCRIPTION);
    }
    
	@Test
	public void test_getName() {
		assertEquals(filter.getName(),ANY_NAME);
	}
    
	@Test
	public void test_descriptionNotNull() {
		assertEquals(filter.getDescription(), ANY_DESCRIPTION);
	}

}
