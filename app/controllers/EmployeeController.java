package controllers;

import java.util.List;

import models.Employee;
import models.SubArea;
import forms.EmployeeForm;
import play.data.Form;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.employee.*;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;

@Authenticated(value = DcnAuthenticator.class)
public class EmployeeController extends Controller {

	final static Form<EmployeeForm> employeeForm = form(EmployeeForm.class);

	public static Result index(){
		return ok(index.render());
	}

	public static Result create_new(){
		return ok(create.render(employeeForm));
	}

	public static Result create_save(){
		Form<EmployeeForm> filledForm = employeeForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(create.render(filledForm));
		}
					
		EmployeeForm employeeForm=filledForm.get();
		if(employeeForm==null){			
			return badRequest(create.render(filledForm));
		}

		Employee employee=new Employee();
		employee.name=employeeForm.name;
		employee.address=employeeForm.address;
		employee.mobile_number=employeeForm.mobile_number;
		employee.joining_date=employeeForm.joining_date;	
		employee.save();
								
		return  redirect(controllers.routes.EmployeeController.index());
	}

	public static Result update_get(Long id){
		Employee employee=Employee.get(id);		
		EmployeeForm eForm=new EmployeeForm();
		eForm.name=employee.name;
		eForm.address=employee.address;
		eForm.mobile_number=employee.mobile_number;
		eForm.joining_date=employee.joining_date;
		eForm.terminate_date=employee.terminate_date;
		
		return ok(update.render(employeeForm.fill(eForm),id));
	}

	public static Result update_save(Long id){
		Form<EmployeeForm> filledForm = employeeForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(update.render(filledForm,id));
		}
					
		EmployeeForm employeeForm=filledForm.get();
		if(employeeForm==null){			
			return badRequest(update.render(filledForm,id));
		}

		Employee employee=Employee.get(id);		
		employee.name=employeeForm.name;
		employee.address=employeeForm.address;
		employee.mobile_number=employeeForm.mobile_number;
		employee.joining_date=employeeForm.joining_date;
		employee.terminate_date=employeeForm.terminate_date;		
		employee.update();
								
		return  redirect(controllers.routes.EmployeeController.index());
	}

	public static Result delete(Long id){		
		Employee employee=Employee.get(id);
		if(employee==null){
			return notFound("Employee having id [" + id + "] does not exists.");
		}
		if(!SubArea.isBelongsToEmployee(id)){
			Employee.delete(id);			
			return ok("Selected Employee has been deleted successfully");
		}
		return 	notFound("Employee ['" + employee.name + "'] is associated with Sub Area(s)");			
	}
	
	/**
	 * Return List of all Employees in JSON format.
	 * @return
	 */
	public static Result all(){
		List<Employee> employees=Employee.all();
		return ok(Json.toJson(employees));
	}

	public static Result all_active(){
		List<Employee> employees=Employee.allActive();
		return ok(Json.toJson(employees));
	}

	public static Result all_terminated(){
		List<Employee> employees=Employee.allTerminated();
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
