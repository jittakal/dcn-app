package controllers;

import java.util.List;

import models.SubArea;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class SubAreaController extends Controller {
	
	
	/**
	 * Return List of all Areas in JSON format.
	 * @return
	 */
	public static Result all(){
		List<SubArea> areas=SubArea.all();
		return ok(Json.toJson(areas));
	}


	/**
	 * Return Area by id.
	 * @return
	 */	
	public static Result get(Long id){
		if(id==null){
			return badRequest("Expecting Sub Area Id");
		}
		
		SubArea subarea=SubArea.get(id);
		
		if(subarea==null){
			return notFound("Sub Area with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(subarea);		
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
		SubArea subarea=Json.fromJson(jsonNode, SubArea.class);
		if(subarea==null){
			return badRequest("Expecting Sub Area Json data");
		}
		subarea.save();
		ObjectNode result=Json.newObject();
		result.put("id",subarea.id);
		return created(result);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id){
		if(id==null){
			return badRequest("Expecting Sub Area Id");
		}
		
		JsonNode jsonNode = request().body().asJson();
		if(jsonNode ==null){
			return badRequest("Expecting Json data");
		}		
		SubArea subarea=Json.fromJson(jsonNode, SubArea.class);
		if(subarea==null){
			return badRequest("Expecting Sub Area Json data");
		}
		subarea.id=id;
		subarea.update();
		return ok();
	}

}
