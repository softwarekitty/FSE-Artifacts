package main.core.filter.ccc;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFeatureFilter;
import main.core.filter.Model;

public class C3_Filter extends AbstractFeatureFilter {

	public C3_Filter() {
		super(Model.C3, "any negated char class", "[^A-Za-z0-9.]+");
	}

	@Override
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_META_NCC);
	}
}