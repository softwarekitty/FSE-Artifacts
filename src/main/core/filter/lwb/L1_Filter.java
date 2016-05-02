package main.core.filter.lwb;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFeatureFilter;
import main.core.filter.Model;

public class L1_Filter extends AbstractFeatureFilter{

	public L1_Filter() {
		super(Model.L1, "curly brace repetition like \\{M,\\}");
	}

	@Override
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_REP_LOWERBOUNDED);
	}
}