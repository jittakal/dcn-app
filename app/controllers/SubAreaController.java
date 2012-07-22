package controllers;

import java.util.List;

import models.SubArea;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.Map;
import models.Area;
import models.Customer;
import forms.SubAreaForm;
import play.data.Form;
import views.html.subarea.*;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;

@Authenticated(value = DcnAuthenticator.class)
public class SubAreaController extends Controller {

	final static Form<SubAreaForm> subareaForm = form(SubAreaForm.class);

	public static Result index(){
		return ok(index.render());
	}

	public static Result create_new(){		
		return ok(create.render(subareaForm));
	}

	public static Result create_save(){
		Form<SubAreaForm> filledForm = subareaForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(create.render(filledForm));
		}
					
		SubAreaForm subareaForm=filledForm.get();
		if(subareaForm==null){						
			return badRequest(create.render(filledForm));
		}

		if(SubArea.isNameExists(0L,subareaForm.name)){			
			filledForm.reject("Sub Area name already exists.");
			return badRequest(create.render(filledForm));	
		}

		SubArea subarea=formToModel(subareaForm);
		subarea.save();
								
		return redirect(controllers.routes.SubAreaController.index());
	}

	public static Result update_get(Long id){		
		SubArea subArea=SubArea.get(id);
		SubAreaForm saForm=new SubAreaForm();
		saForm.name=subArea.name;
		saForm.areaid=subArea.area.id.toString();				
		return ok(update.render(subareaForm.fill(saForm),id));
	}

	public static Result update_save(Long id){
		Form<SubAreaForm> filledForm = subareaForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(update.render(filledForm,id));
		}
					
		SubAreaForm subareaForm=filledForm.get();
		if(subareaForm==null){						
			return badRequest(update.render(filledForm,id));
		}		

		if(SubArea.isNameExists(id,subareaForm.name)){			
			filledForm.reject("Sub Area name already exists.");
			return badRequest(update.render(filledForm,id));	
		}

		SubArea subarea=formToModel(subareaForm);
		subarea.id=id;
		subarea.update();
								
		return  redirect(controllers.routes.SubAreaController.index());
	}

	private static SubArea formToModel(SubAreaForm subareaForm){
		Area area=new Area();
		area.id=new Long(subareaForm.areaid);				

		SubArea subarea=new SubArea();		
		subarea.name=subareaForm.name;
		subarea.area=area;		
		return subarea;
	}

	public static Result delete(Long id){		
		SubArea subarea=SubArea.get(id);
		if(subarea==null){
			return notFound("Sub Area having id [" + id + "] does not exists.");
		}
		if(!Customer.isBelongsToSubArea(id)){
			SubArea.delete(id);			
			return ok("Selected Sub Area has been deleted successfully");
		}
		return notFound("Sub Area ['" + subarea.name + "'] is associated with Customer(s)");			
	}
	
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
