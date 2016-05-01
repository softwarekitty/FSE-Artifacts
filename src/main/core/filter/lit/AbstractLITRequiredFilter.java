package main.core.filter.lit;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFilter;

public abstract class AbstractLITRequiredFilter extends AbstractFilter {

	public AbstractLITRequiredFilter(String name, String description) {
		super(name,description);
	}

	public abstract boolean accepts(String rawPattern, FeatureCount featureCount);
	
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_META_LITERAL);
	}

}