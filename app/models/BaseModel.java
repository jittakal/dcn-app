package models;

import java.util.List;

import play.db.ebean.Model;

public abstract class BaseModel extends Model{	
			
	public static <I,T extends Model>  T get(Finder<I,T> finder,I id){
		return finder.byId(id);
	}
	
	public static <I,T extends Model> List<T> all(Finder<I,T> finder) {
		return finder.all();
	}
	
	public static <T extends Model> void create(T t) {
		t.save();		
	}
	
	public static <T extends Model> void update(T t){
		t.update();
	}

	public static <I, T extends Model> void delete(Finder<I,T> finder,I id) {
		finder.byId(id).delete();
	}
}