package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Map;
import java.util.HashMap;
import play.db.ebean.Model;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.avaje.ebean.validation.NotNull;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "amount" }))
public class Price extends Model {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long id;
	
	@NotNull
	public Integer amount;
	
	/*@Temporal(value=TemporalType.DATE)
	@NotNull
	public Date start_date;
	
	@Temporal(value=TemporalType.DATE)
	public Date end_date;*/
	
	public static Finder<Long, Price> find = new Finder(Long.class, Price.class);

	public static Price get(Long id) {
		return find.byId(id);
	}

	public static List<Price> all() {
		return find.all();
	}

	public static void delete(Long id) {
		find.byId(id).delete();
	}

	public static Map<String,String> asMap(){
		List<Price> prices=find.select("id,amount").findList();
		Map<String,String> priceMap=new HashMap<String,String>();
		for(Price price: prices){
			priceMap.put(price.id.toString(),price.amount.toString());
		}
		return priceMap;
	}

	public static boolean isAmountExists(Long id,Integer amount){
		if(id==0){
			return find.select("id").where().eq("amount",amount).findList().size()==0?false:true;			
		}
		return find.select("id").where().ne("id",id).eq("amount",amount).findList().size()==0?false:true;		
	}

}
