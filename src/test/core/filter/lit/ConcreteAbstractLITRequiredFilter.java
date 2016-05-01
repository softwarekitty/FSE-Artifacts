package test.core.filter.lit;

import main.core.features.FeatureCount;
import main.core.filter.lit.AbstractLITRequiredFilter;

public class ConcreteAbstractLITRequiredFilter extends AbstractLITRequiredFilter {

	public ConcreteAbstractLITRequiredFilter(String name, String description) {
		super(name, description);
	}

    @Override
	public boolean accepts(String rawPattern, FeatureCount featureCount){
        return false;
    }

	public FeatureCount exposeRequiredFeatures() {
        return super.requiredFeatures();
		//return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_META_LITERAL);
	}

}
