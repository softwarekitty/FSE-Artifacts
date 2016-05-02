package recreateArtifacts.nodeFilter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

public class Main_CreateMembershipFilesFromFilters {

	public static void main(String[] args)
			throws IllegalArgumentException, IOException, QuoteRuleException, PythonParsingException {
		TreeSet<RegexProjectSet> corpus = LoadUtil
				.loadRegexProjectSetInput(IOUtil.readLines(PathUtil.pathToCorpusFile()));
		Map<String, List<AbstractFilter>> groupnameFilterListMap = Model.getGropunameFilterListMap();
		Map<String, Cluster> nodenameMemberSetMap = new HashMap<String, Cluster>();

		for (Entry<String, List<AbstractFilter>> entry : groupnameFilterListMap.entrySet()) {

			// make a folder for the group
			String groupFolderPath = PathUtil.getPathNodeFilter() + "output/" + entry.getKey() + "/";
			File groupFolder = new File(groupFolderPath);
			if (!groupFolder.exists()) {
				groupFolder.mkdirs();
			}

			// for each filter in the group
			List<AbstractFilter> filterList = entry.getValue();
			for (AbstractFilter filter : filterList) {

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
		//
		// TreeSet<RegexProjectSet> groupsIntersection = new
		// TreeSet<RegexProjectSet>();
		// groupsIntersection.addAll(corpus);
		// ArrayList<String> winners = new ArrayList<String>(Arrays.asList("C1",
		// "D2", "T1", "L2", "S2"));
		// for (String groupName : C.groupNames) {
		// TreeSet<RegexProjectSet> groupUnion = new TreeSet<RegexProjectSet>();
		// NodeGroup group = gd.get(groupName);
		// for (RTNode node : group) {
		// String nodeName = node.getName();
		// if (!winners.contains(nodeName)) {
		// groupUnion.addAll(node);
		// }
		// }
		// System.out.println(groupsIntersection.retainAll(groupUnion));
		// File intersection = new File(IOUtil.dataPath + IOUtil.NODES,
		// groupName + "_Union.tsv");
		// StringBuilder sb = new StringBuilder();
		// for (RegexProjectSet rps : groupUnion) {
		// sb.append(rps.getContent() + "\t" + rps.getProjectsCSV() + "\n");
		// }
		// IOUtil.createAndWrite(intersection, sb.toString());
		// }

	}

	public static String getRegexGroupRows(TreeSet<RegexProjectSet> regexGroup) {
		StringBuilder sb = new StringBuilder();
		for (RegexProjectSet rps : regexGroup) {
			sb.append(DumpUtil.regexRow(rps) + "\n");
		}
		return sb.toString();
	}

}
