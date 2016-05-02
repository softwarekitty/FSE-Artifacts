package main.core.filter.dbb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.core.features.FeatureDictionary;
import main.core.filter.AbstractFilter;
import main.core.filter.Model;

public class D1_Filter extends AbstractFilter{
	
	public D1_Filter(){
		super(Model.D1,"curly brace repetition like \\{M,N\\} with M<N");
	}	

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if(featureCount.featureSetSubsumes(requiredFeatures())){
			String modifiedPattern = rawPattern.replaceAll("\\{(\\d+),\\1\\}", "{$1}");
			Pattern anyDBB = Pattern.compile("\\{\\d+,\\d+\\}");
			Matcher m1 = anyDBB.matcher(modifiedPattern);
			return m1.find();
		}
		return false;
	}
	
	public FeatureCount requiredFeatures() {
		return FeatureCountFactory.getFeatureCount(FeatureDictionary.I_REP_DOUBLEBOUNDED);
	}
}
