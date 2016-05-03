package recreateArtifacts.edgeTests.renjin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.sexp.ListVector;

public class WilcoxTest {
	private final double pValue;
	
	public WilcoxTest(String v0Line, String v1Line) throws ScriptException{
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("Renjin");
	    if(engine == null) {
	        throw new RuntimeException("Renjin Script Engine not found on the classpath. (needs renjin-script-engine...with-dependencies.jar)");
	    }
	    engine.eval(v0Line);
	    engine.eval(v1Line);
	    engine.eval("results = wilcox.test(v0,v1)");
	    ListVector results  = (ListVector)engine.get("results");
	    this.pValue = results.getElementAsDouble("p.value");
	}

	public double getpValue() {
		return pValue;
	}
}
