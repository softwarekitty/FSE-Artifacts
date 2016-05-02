package main.core.filter.sng;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFeatureFilter;
import main.core.filter.Model;

public class S1_Filter extends AbstractFeatureFilter{
	
	public S1_Filter(){
		super(Model.S1,"curly brace repetition like \\{M\\}", "^[a-f0-9]{40}$");
	}
	
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_REP_SINGLEEXACTLY);
	}
}