package main.core.filter.lwb;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFeatureFilter;
import main.core.filter.ModelNames;

public class L2_Filter extends AbstractFeatureFilter {

	public L2_Filter() {
		super(ModelNames.L2, "zero-or-more repetition using kleene star");
	}

	@Override
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_REP_KLEENISH);
	}
}
