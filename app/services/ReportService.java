package services;

import java.util.*;
import models.*;
import com.avaje.ebean.*;

public class ReportService{

	public static Map<String,Integer> customersByArea(){
		Map<String,Integer> reportMap=new LinkedHashMap<String,Integer>();
		List<Area> areas=Area.all();
		for(Area area:areas){			
			reportMap.put(area.name,0);
		}

		String sql="select a.name as name, count(c.id) as cnt "
		+ " from customer c, area a "
		+ " where c.area_id=a.id "		
		+ " group by a.name order by a.id";

		SqlQuery sqlQuery = Ebean.createSqlQuery(sql); 		
		List<SqlRow> sqlRows=sqlQuery.findList();			

		for(SqlRow sqlRow: sqlRows){					
			reportMap.put(sqlRow.getString("name"),sqlRow.getInteger("cnt"));						
		}		

		return reportMap;
	}
	
}