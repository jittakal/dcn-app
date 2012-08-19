package sos;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class CollectionReportSO implements Serializable{

	public String id_number;

	public String area_name;

	public String sub_area_name;

	public String customer_name;

	public Map<Integer,Integer> month_collection=new HashMap<Integer,Integer>();

	public CollectionReportSO() {
		for(int i=1;i<=12;i++){
			month_collection.put(i,0);	
		}		
	}
	
}