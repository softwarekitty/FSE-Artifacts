package main.core.filter;

import main.core.features.FeatureCount;

public abstract class AbstractFeatureFilter extends AbstractFilter {

	public AbstractFeatureFilter(String name, String description) {
		super(name, description);
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		return featureCount.featureSetSubsumes(requiredFeatures());
	}

	public abstract FeatureCount requiredFeatures();
}
