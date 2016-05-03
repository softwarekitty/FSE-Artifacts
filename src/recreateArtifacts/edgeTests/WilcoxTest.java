package recreateArtifacts.edgeTests;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.sexp.ListVector;

public class WilcoxTest {
	private final double pValue;
	
	public WilcoxTest(NamedRarray v0, NamedRarray v1) throws ScriptException{
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("Renjin");
	    if(engine == null) {
	        throw new RuntimeException("Renjin Script Engine not found on the classpath. (needs renjin-script-engine...with-dependencies.jar)");
	    }
	    engine.eval(v0.getArray());
	    engine.eval(v1.getArray());
	    engine.eval("results = wilcox.test("+v0.getName()+","+v1.getName()+")");
	    ListVector results  = (ListVector)engine.get("results");
	    this.pValue = results.getElementAsDouble("p.value");
	}

	/**
	 * The result of the wilcox test
	 * @return The p-value
	 */
	public double getpValue() {
		return pValue;
	}
}
