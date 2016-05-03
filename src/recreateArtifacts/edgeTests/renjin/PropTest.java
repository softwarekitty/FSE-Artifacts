package recreateArtifacts.edgeTests.renjin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.sexp.ListVector;

import recreateArtifacts.edgeTests.AnswerColumn;

public class PropTest {
	private final double pValue;
	
	public PropTest(AnswerColumn left, AnswerColumn right) throws ScriptException{
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("Renjin");
	    if(engine == null) {
	        throw new RuntimeException("Renjin Script Engine not found on the classpath. (needs renjin-script-engine...with-dependencies.jar)");
	    }
	    engine.eval("results = prop.test(x=c(" + left.getNGreaterThanZero() + "," + right.getNGreaterThanZero()+"),n=c("+left.getNExistingValues()+","+right.getNExistingValues()+"))\n");
	    ListVector results  = (ListVector)engine.get("results");
	    this.pValue = results.getElementAsDouble("p.value");
	}

	public double getpValue() {
		return pValue;
	}
}
