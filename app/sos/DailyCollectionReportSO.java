package sos;

import java.io.Serializable;
import java.util.Map;
import java.util.LinkedHashMap;

public class DailyCollectionReportSO implements Serializable{

	public String name;

	public Map<Integer,Integer> day_total=new LinkedHashMap<Integer,Integer>();

	public DailyCollectionReportSO(String iname){
		this.name=iname;				
	}	

	public DailyCollectionReportSO(String iname, Integer iday, Integer itotal){
		this.name=iname;		
		this.day_total.put(iday,itotal);
	}

	public void addDayTotal(Integer day, Integer total){
		day_total.put(day,total);
	}
	
}

