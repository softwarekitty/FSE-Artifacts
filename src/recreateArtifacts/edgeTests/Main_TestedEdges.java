package recreateArtifacts.edgeTests;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Main_TestedEdges {

	public static void main(String[] args) throws ScriptException {
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("Renjin");
	    if(engine == null) {
	        throw new RuntimeException("Renjin Script Engine not found on the classpath. (needs renjin-script-engine...with-dependencies.jar)");
	    }

	    engine.eval("inputPath = file.path(getwd(),\"rExamples\",\"M0R0V0_CC_SEQ_M0R0V1_CC_SNG.tsv\")");
	    engine.eval("tbl = read.table(inputPath,TRUE,stringsAsFactors=FALSE)");
	    engine.eval("g = as.numeric(tbl[,1])");
	    engine.eval("h = as.numeric(tbl[,2])");
	    engine.eval("results = wilcox.test(h,g)");
	    engine.eval("print(results)");	 

	}

}
