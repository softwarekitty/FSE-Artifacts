package main.core.filter;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeIterator;

import main.core.RegexProjectSet;
import main.core.features.FeatureCount;
import main.core.features.FeatureCountFactory;
import main.parse.pcre.PCRE;

/**
 * decides membership for one alternative representational style
 * 
 * @author cc
 *
 */
public abstract class AbstractFilter {
	protected static final char DELIM = '•';

	private final String name;
	private final String description;

	public AbstractFilter(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	public boolean accepts(RegexProjectSet regex){
		return accepts(regex.getRawPattern(),regex.getFeatures());
	}
	
	public boolean accepts(String rawPattern){
		return accepts(rawPattern,FeatureCountFactory.getFeatureCount(rawPattern));
	}

	public abstract boolean accepts(String rawPattern, FeatureCount featureCount);

	protected String getTokenStream(String rawPattern) {
		CommonTree root = new PCRE(rawPattern).getCommonTree();
		TreeIterator it = new TreeIterator(root);
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			sb.append(escape(((CommonTree) it.next()).getText().getBytes()) + DELIM);
		}
		return sb.toString();
	}

	// cite:
	// http://stackoverflow.com/questions/7487917/convert-byte-array-to-escaped-string
	private String escape(byte[] data) {
		StringBuilder cbuf = new StringBuilder();
		for (byte b : data) {
			if (b >= 0x20 && b <= 0x7e) {
				cbuf.append((char) b);
			} else {
				cbuf.append(String.format("\\0x%02x", b & 0xFF));
			}
		}
		return cbuf.toString();
	}
}
