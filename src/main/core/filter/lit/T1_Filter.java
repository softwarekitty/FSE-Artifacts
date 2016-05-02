package main.core.filter.lit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.core.features.FeatureCount;
import main.core.filter.Model;

public class T1_Filter extends AbstractLITRequiredFilter{
	private static final Pattern HEX_OR_OCTAL = Pattern.compile("(\\\\x[a-f0-9A-F]{2})|((\\\\0\\d*)|(\\\\\\d{3}))");

	
	public T1_Filter(){
		super(Model.T1,"no HEX, OCT or char-class-wrapped literals", "get_tag");
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		if (featureCount.featureSetSubsumes(requiredFeatures())) {
			Matcher m = T3_Filter.CCC_WRAPPED_CHAR.matcher(rawPattern);
			Matcher m3 = HEX_OR_OCTAL.matcher(rawPattern);
			return !m.find() && !m3.find();
		}
		return false;
	}
}