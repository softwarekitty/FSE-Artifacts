package test.core.filter;

import main.core.features.FeatureCount;
import main.core.filter.AbstractFilter;

public class ConcreteAbstractFilter extends AbstractFilter{

	public ConcreteAbstractFilter(String name, String description, String example) {
		super(name,description,example);
	}

	public String exposeGetTokenStream(String rawString) {
		return super.getTokenStream(rawString);
	}

	@Override
	public boolean accepts(String rawPattern, FeatureCount featureCount) {
		return false;
	}
}
