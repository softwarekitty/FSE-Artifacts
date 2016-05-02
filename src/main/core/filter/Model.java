package main.core.filter;

import java.util.HashMap;
import java.util.Map;

import main.core.filter.ccc.C1_Filter;
import main.core.filter.ccc.C2_Filter;
import main.core.filter.ccc.C3_Filter;
import main.core.filter.ccc.C4_Filter;
import main.core.filter.ccc.C5_Filter;
import main.core.filter.dbb.D1_Filter;
import main.core.filter.dbb.D2_Filter;
import main.core.filter.dbb.D3_Filter;
import main.core.filter.lit.T1_Filter;
import main.core.filter.lit.T2_Filter;
import main.core.filter.lit.T3_Filter;
import main.core.filter.lit.T4_Filter;
import main.core.filter.lwb.L1_Filter;
import main.core.filter.lwb.L2_Filter;
import main.core.filter.lwb.L3_Filter;
import main.core.filter.sng.S1_Filter;
import main.core.filter.sng.S2_Filter;
import main.core.filter.sng.S3_Filter;

public class Model {
	public static final String CCC = "CCC";
	public static final String DBB = "DBB";
	public static final String LIT = "LIT";
	public static final String LWB = "LWB";
	public static final String SNG = "SNG";

	public static final String D1 = "D1";
	public static final String D2 = "D2";
	public static final String D3 = "D3";

	public static final String S1 = "S1";
	public static final String S2 = "S2";
	public static final String S3 = "S3";

	public static final String L1 = "L1";
	public static final String L2 = "L2";
	public static final String L3 = "L3";

	public static final String C1 = "C1";
	public static final String C2 = "C2";
	public static final String C3 = "C3";
	public static final String C4 = "C4";
	public static final String C5 = "C5";

	public static final String T1 = "T1";
	public static final String T2 = "T2";
	public static final String T3 = "T3";
	public static final String T4 = "T4";

	/**
	 * gets all the filters, in lists pointed to by group names
	 * 
	 * @return A Map going from group names to lists of filters in that group.
	 */
	public static Map<String, HashMap<String, AbstractFilter>> getFiltersMaps() {
		HashMap<String, AbstractFilter> CCC_List = new HashMap<String, AbstractFilter>();
		CCC_List.put(C1, new C1_Filter());
		CCC_List.put(C2, new C2_Filter());
		CCC_List.put(C3, new C3_Filter());
		CCC_List.put(C4, new C4_Filter());
		CCC_List.put(C5, new C5_Filter());

		HashMap<String, AbstractFilter> DBB_List = new HashMap<String, AbstractFilter>();
		DBB_List.put(D1, new D1_Filter());
		DBB_List.put(D2, new D2_Filter());
		DBB_List.put(D3, new D3_Filter());

		HashMap<String, AbstractFilter> LIT_List = new HashMap<String, AbstractFilter>();
		LIT_List.put(T1, new T1_Filter());
		LIT_List.put(T2, new T2_Filter());
		LIT_List.put(T3, new T3_Filter());
		LIT_List.put(T4, new T4_Filter());

		HashMap<String, AbstractFilter> LWB_List = new HashMap<String, AbstractFilter>();
		LWB_List.put(L1, new L1_Filter());
		LWB_List.put(L2, new L2_Filter());
		LWB_List.put(L3, new L3_Filter());

		HashMap<String, AbstractFilter> SNG_List = new HashMap<String, AbstractFilter>();
		SNG_List.put(S1, new S1_Filter());
		SNG_List.put(S2, new S2_Filter());
		SNG_List.put(S3, new S3_Filter());

		Map<String, HashMap<String, AbstractFilter>> groupnameFilterListMap = new HashMap<String, HashMap<String, AbstractFilter>>();
		groupnameFilterListMap.put(CCC, CCC_List);
		groupnameFilterListMap.put(DBB, DBB_List);
		groupnameFilterListMap.put(LIT, LIT_List);
		groupnameFilterListMap.put(LWB, LWB_List);
		groupnameFilterListMap.put(SNG, SNG_List);
		return groupnameFilterListMap;
	}
}
