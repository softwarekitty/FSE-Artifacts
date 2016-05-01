package main.core.filter.ccc;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFeatureFilter;
import main.core.filter.ModelNames;

public class C3_Filter extends AbstractFeatureFilter {

	public C3_Filter() {
		super(ModelNames.C3, "any negated char class");
	}

	@Override
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_META_NCC);
	}
}