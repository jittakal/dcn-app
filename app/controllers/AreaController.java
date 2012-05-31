package controllers;

import java.util.List;

import models.Area;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.BodyParser;
import play.libs.Json;
import org.codehaus.jackson.*;
import org.codehaus.jackson.node.ObjectNode;

import views.html.area.*;

public class AreaController extends Controller {
	
	//@Authenticated
	/**
	 * @return
	 */
	public static Result index(){
		return ok(index.render(Area.all()));
	}
	
	/**
	 * Return List of all Areas in JSON format.
	 * @return
	 */
	public static Result all(){
		List<Area> areas=Area.all();
		return ok(Json.toJson(areas));
	}


	/**
	 * Return Area by id.
	 * @return
	 */	
	public static Result get(Long id){
		if(id==null){
			return badRequest("Expecting Area Id");
		}
		
		Area area=Area.get(id);
		
		if(area==null){
			return notFound("Area with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(area);		
		return ok(result);
	}
	
	/**
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result create(){		
		JsonNode jsonNode = request().body().asJson();
		if(jsonNode ==null){
			return badRequest("Expecting Json data");
		}		
		Area area=Json.fromJson(jsonNode, Area.class);
		if(area==null){
			return badRequest("Expecting Area Json data");
		}
		area.save();
		ObjectNode result=Json.newObject();
		result.put("id",area.id);
		return created(result);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id){
		if(id==null){
			return badRequest("Expecting Area Id");
		}
		
		JsonNode jsonNode = request().body().asJson();
		if(jsonNode ==null){
			return badRequest("Expecting Json data");
		}		
		Area area=Json.fromJson(jsonNode, Area.class);
		if(area==null){
			return badRequest("Expecting Area Json data");
		}
		area.id=id;
		area.update();
		return ok();
	}

}
