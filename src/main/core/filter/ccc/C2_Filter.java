package main.core.filter.ccc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFilter;
import main.core.filter.ModelNames;

public class C2_Filter extends AbstractFilter {

	private static final Pattern CCC_WITHOUT_ANY_DEFAULT_OR_RANGE = Pattern
			.compile("CHARACTER_CLASS•DOWN•(((\\\\0x..)|.)•)+UP•");

	public C2_Filter() {
		super(ModelNames.C2, "char class explicitly listing all chars");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			// C2 is any CCC that has no ranges or defaults,
			// and we know it has a CCC
			String tokenStream = getTokenStream(rawPattern);

			// this matcher REQUIRES there to be a CCC containing only literals
			Matcher m1 = CCC_WITHOUT_ANY_DEFAULT_OR_RANGE.matcher(tokenStream);

			// so if none can be found, than all CCCs present don't have these
			// features, as needed for C2
			return m1.find();
		}
		return false;
	}

	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_META_CC);

	}
}