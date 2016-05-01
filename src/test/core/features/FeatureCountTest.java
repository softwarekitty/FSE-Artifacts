package test.core.features;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;

public final class FeatureCountTest {
	private static FeatureDictionary fd;
	
	@Before
	public void setup(){
		fd = new FeatureDictionary();
	}
	
	@Test
	public void test_canCreateWithCorrectSizeArray() {
		
		// create feature count with all set to 1
		int[] counts = new int[fd.getSize()];
		Arrays.fill(counts, 1);
		FeatureCount fc = new FeatureCount(counts,fd);
		assertNotNull(fc);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_cannotCreateWithSmallArray() {
		
		// create feature count with all set to 1, but array is too small
		int[] counts = new int[fd.getSize()-1];
		Arrays.fill(counts, 1);
		FeatureCount fc = new FeatureCount(counts,fd);	
		assertNull(fc);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_cannotCreateWithLargeArray() {
		
		// create feature count with all set to 1, but array is too large
		int[] counts = new int[fd.getSize()+1];
		Arrays.fill(counts, 1);
		FeatureCount fc = new FeatureCount(counts,fd);	
		assertNull(fc);
	}
	
	@Test
	public void test_cannotModifyArray() {
		
		// create feature count with all set to 1
		int[] counts = new int[fd.getSize()];
		Arrays.fill(counts, 1);
		FeatureCount fc = new FeatureCount(counts,fd);
		
		// now try to modify the array, setting all to 0
		int[] shouldBeACopy = fc.getFeatureCountArray();
		Arrays.fill(shouldBeACopy, 0);
		assertFalse(Arrays.equals(shouldBeACopy, fc.getFeatureCountArray()));		
	}
	
	@Test
	public void test_featureSetSubsumes_empty() {
		Integer[] thisFCSet = {FeatureDictionary.I_CC_DECIMAL};
		Integer[] otherFCSet = {};
		FeatureCount thisFC = FeatureCountFactory.getFeatureCount(thisFCSet);
		FeatureCount otherFC = FeatureCountFactory.getFeatureCount(otherFCSet);
		assertTrue(thisFC.featureSetSubsumes(otherFC));	
	}
	
	@Test
	public void test_featureSetSubsumes_same() {
		Integer[] thisFCSet = {FeatureDictionary.I_CC_DECIMAL};
		FeatureCount thisFC = FeatureCountFactory.getFeatureCount(thisFCSet);
		FeatureCount otherFC = FeatureCountFactory.getFeatureCount(thisFCSet);
		assertTrue(thisFC.featureSetSubsumes(otherFC));	
	}
	
	@Test
	public void test_featureSetSubsumes_missingOne() {
		Integer[] thisFCSet = {FeatureDictionary.I_CC_DECIMAL};
		Integer[] otherFCSet = {FeatureDictionary.I_CC_DECIMAL,FeatureDictionary.I_REP_ADDITIONAL};
		FeatureCount thisFC = FeatureCountFactory.getFeatureCount(thisFCSet);
		FeatureCount otherFC = FeatureCountFactory.getFeatureCount(otherFCSet);
		assertFalse(thisFC.featureSetSubsumes(otherFC));	
	}
	
	@Test
	public void test_featureSetSubsumes_emptyMissingOne() {
		Integer[] thisFCSet = {};
		Integer[] otherFCSet = {FeatureDictionary.I_CC_DECIMAL};
		FeatureCount thisFC = FeatureCountFactory.getFeatureCount(thisFCSet);
		FeatureCount otherFC = FeatureCountFactory.getFeatureCount(otherFCSet);
		assertFalse(thisFC.featureSetSubsumes(otherFC));	
	}
	
	@Test
	public void test_featureSetSubsumes_bothAllEqual() {
		
		// assumes indices fill all between 0 and n-1
		Integer[] thisFCSet = new Integer[fd.getSize()];
		Integer[] otherFCSet = new Integer[fd.getSize()];
		for(int i=0;i<thisFCSet.length;i++){
			thisFCSet[i] = i;
			otherFCSet[i] = i;
		}
		FeatureCount thisFC = FeatureCountFactory.getFeatureCount(thisFCSet);
		FeatureCount otherFC = FeatureCountFactory.getFeatureCount(otherFCSet);
		assertTrue(thisFC.featureSetSubsumes(otherFC));	
	}
}
