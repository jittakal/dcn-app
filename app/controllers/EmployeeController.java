package controllers;

import java.util.List;

import models.Employee;
import models.SubArea;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.employee.*;

public class EmployeeController extends Controller {

	public static Result index(){
		return ok(index.render());
	}
	
	/**
	 * Return List of all Employees in JSON format.
	 * @return
	 */
	public static Result all(){
		List<Employee> employees=Employee.all();
		return ok(Json.toJson(employees));
	}


	/**
	 * Return Area by id.
	 * @return
	 */	
	public static Result get(Long id){
		if(id==null){
			return badRequest("Expecting Employee Id");
		}
		
		Employee employee=Employee.get(id);
		
		if(employee==null){
			return notFound("Employee with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(employee);		
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
		Employee employee=Json.fromJson(jsonNode, Employee.class);
		if(employee==null){
			return badRequest("Expecting Employee Json data");
		}
		employee.save();
		ObjectNode result=Json.newObject();
		result.put("id",employee.id);
		return created(result);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id){
		if(id==null){
			return badRequest("Expecting Employee Id");
		}
		
		JsonNode jsonNode = request().body().asJson();
		if(jsonNode ==null){
			return badRequest("Expecting Json data");
		}		
		Employee employee=Json.fromJson(jsonNode, Employee.class);
		if(employee==null){
			return badRequest("Expecting Employee Json data");
		}
		employee.id=id;
		employee.update();
		return ok();
	}

}
