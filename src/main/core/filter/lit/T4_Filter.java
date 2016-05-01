package main.core.filter.lit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.filter.ModelNames;

public class T4_Filter extends AbstractLITRequiredFilter{
	private static final Pattern T4_OCT = Pattern.compile("((\\\\0\\d*)|(\\\\\\d{3}))");

	
	public T4_Filter(){
		super(ModelNames.T4,"has OCT literal like \\verb!\\0177!");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			Matcher m = T4_OCT.matcher(rawPattern);
			return m.find();
		}
		return false;
	}
}