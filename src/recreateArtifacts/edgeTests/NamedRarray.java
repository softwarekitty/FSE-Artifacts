package recreateArtifacts.edgeTests;

/**
 * a pair class for the WilcoxTest input
 * @author cc
 *
 */
public class NamedRarray {
	private final String name;
	private final String array;
	public NamedRarray(String name, String array) {
		this.name = name;
		this.array = array;
	}
	public String getName() {
		return name;
	}
	public String getArray() {
		return array;
	}
}
