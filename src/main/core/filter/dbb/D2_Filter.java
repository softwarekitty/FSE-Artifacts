package main.core.filter.dbb;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFeatureFilter;
import main.core.filter.Model;

public class D2_Filter extends AbstractFeatureFilter {

	public D2_Filter() {
		super(Model.D2, "zero-or-one repetition using question mark", "^http(s)?://");
	}

	@Override
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_REP_QUESTIONABLE);
	}
}