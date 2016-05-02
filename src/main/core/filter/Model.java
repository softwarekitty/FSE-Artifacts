package main.core.filter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	public static Map<String,List<AbstractFilter>> getGropunameFilterListMap(){
		LinkedList<AbstractFilter> CCC_List = new LinkedList<AbstractFilter>();
		CCC_List.add(new C1_Filter());
		CCC_List.add(new C2_Filter());
		CCC_List.add(new C3_Filter());
		CCC_List.add(new C4_Filter());
		CCC_List.add(new C5_Filter());
		
		LinkedList<AbstractFilter> DBB_List = new LinkedList<AbstractFilter>();
		DBB_List.add(new D1_Filter());
		DBB_List.add(new D2_Filter());
		DBB_List.add(new D3_Filter());
		
		LinkedList<AbstractFilter> LIT_List = new LinkedList<AbstractFilter>();
		LIT_List.add(new T1_Filter());
		LIT_List.add(new T2_Filter());
		LIT_List.add(new T3_Filter());
		LIT_List.add(new T4_Filter());
		
		LinkedList<AbstractFilter> LWB_List = new LinkedList<AbstractFilter>();
		LWB_List.add(new L1_Filter());
		LWB_List.add(new L2_Filter());
		LWB_List.add(new L3_Filter());
		
		LinkedList<AbstractFilter> SNG_List = new LinkedList<AbstractFilter>();
		SNG_List.add(new S1_Filter());
		SNG_List.add(new S2_Filter());
		SNG_List.add(new S3_Filter());
		
		Map<String,List<AbstractFilter>> groupnameFilterListMap = new HashMap<String,List<AbstractFilter>>();
		groupnameFilterListMap.put(CCC, CCC_List);
		groupnameFilterListMap.put(DBB, DBB_List);
		groupnameFilterListMap.put(LIT, LIT_List);
		groupnameFilterListMap.put(LWB, LWB_List);
		groupnameFilterListMap.put(SNG, SNG_List);
		return groupnameFilterListMap;	
	}
}
