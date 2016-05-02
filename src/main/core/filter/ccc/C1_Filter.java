package main.core.filter.ccc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFilter;
import main.core.filter.Model;

public class C1_Filter extends AbstractFilter {

	private static final Pattern CCC_WITH_RANGE = Pattern.compile(
			"CHARACTER_CLASS•DOWN•(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W))•)*(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP•)(((.|\\\\0x..)|(\\\\d|\\\\D|\\\\s|\\\\S|\\\\w|\\\\W)|(RANGE•DOWN•(.|\\\\0x..)•(.|\\\\0x..)•UP))•)*UP•");

	public C1_Filter() {
		super(Model.C1, "char class using ranges");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			String tokenStream = getTokenStream(rawPattern);
			Matcher m1 = CCC_WITH_RANGE.matcher(tokenStream);
			return m1.find();
		}
		return false;
	}

	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_CC_RANGE, FeatureDictionary.I_META_CC);

	}
}
