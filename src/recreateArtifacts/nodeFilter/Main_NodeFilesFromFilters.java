package recreateArtifacts.nodeFilter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import main.core.RegexProjectSet;
import main.core.categories.Cluster;
import main.core.filter.AbstractFilter;
import main.core.filter.Model;
import main.io.DumpUtil;
import main.io.IOUtil;
import main.io.LoadUtil;
import main.parse.PythonParsingException;
import main.parse.QuoteRuleException;
import recreateArtifacts.PathUtil;

public class Main_NodeFilesFromFilters {

	public static void main(String[] args)
			throws IllegalArgumentException, IOException, QuoteRuleException, PythonParsingException {
		TreeSet<RegexProjectSet> corpus = LoadUtil
				.loadRegexProjectSetInput(IOUtil.readLines(PathUtil.pathToCorpusFile()));
		Map<String, HashMap<String, AbstractFilter>> groupnameFilterListMap = Model.getFiltersMaps();
		Map<String, Cluster> nodenameMemberSetMap = new HashMap<String, Cluster>();

		for (Entry<String, HashMap<String, AbstractFilter>> entry : groupnameFilterListMap.entrySet()) {

			// make a folder for the group
			String groupFolderPath = PathUtil.getPathNodeFilter() + "output/nodes/" + entry.getKey() + "/";
			File groupFolder = new File(groupFolderPath);
			if (!groupFolder.exists()) {
				groupFolder.mkdirs();
			}

			// for each filter in the group
			HashMap<String, AbstractFilter> filterList = entry.getValue();
			for (AbstractFilter filter : filterList.values()) {

				// make a cluster for that node
				String nodeName = filter.getName();
				Cluster nodeMembers = new Cluster();
				for (RegexProjectSet regex : corpus) {
					if (filter.accepts(regex)) {
						nodeMembers.add(regex);
					}
				}

				// then write it to file
				File nodeOutFile = new File(groupFolderPath, nodeName + ".tsv");
				IOUtil.createAndWrite(nodeOutFile, getRegexGroupRows(nodeMembers));

				// add the cluster to a map (for whatever other computations)
				nodenameMemberSetMap.put(nodeName, nodeMembers);
			}

		}
	}

	public static String getRegexGroupRows(TreeSet<RegexProjectSet> regexGroup) {
		StringBuilder sb = new StringBuilder();
		for (RegexProjectSet rps : regexGroup) {
			sb.append(DumpUtil.regexRow(rps) + "\n");
		}
		return sb.toString();
	}

}
