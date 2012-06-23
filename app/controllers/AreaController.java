package controllers;

import java.util.List;

import models.Area;
import models.SubArea;
import forms.AreaForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.BodyParser;
import play.libs.Json;
import org.codehaus.jackson.*;
import org.codehaus.jackson.node.ObjectNode;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;
import views.html.area.*;


@Authenticated(value = DcnAuthenticator.class)
public class AreaController extends Controller {

	final static Form<AreaForm> areaForm = form(AreaForm.class);
	
	//@Authenticated
	/**
	 * @return
	 */
	public static Result index(){
		return ok(index.render());
	}

	public static Result create_new(){
		return ok(create.render(areaForm));
	}

	public static Result create_save(){
		Form<AreaForm> filledForm = areaForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(views.html.area.create.render(filledForm));
		}
					
		AreaForm areaForm=filledForm.get();
		if(areaForm==null){			
			return badRequest(views.html.area.create.render(filledForm));
		}

		Area area=new Area();
		area.name=areaForm.name;
		area.save();
								
		return redirect(controllers.routes.AreaController.index());
	}

	public static Result update_get(Long id){
		Area area=Area.get(id);		
		AreaForm aForm=new AreaForm();
		aForm.name=area.name;
		return ok(update.render(areaForm.fill(aForm),id));
	}

	public static Result update_save(Long id){
		Form<AreaForm> filledForm = areaForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(views.html.area.update.render(filledForm,id));
		}
					
		AreaForm areaForm=filledForm.get();
		if(areaForm==null){			
			return badRequest(views.html.area.update.render(filledForm,id));
		}

		Area area=new Area();
		area.id=id;
		area.name=areaForm.name;
		area.update();
								
		return  redirect(controllers.routes.AreaController.index());
	}

	public static Result delete(Long id){		
		Area area=Area.get(id);		
		if(area==null){
			return notFound("Area with id [" + id + "] does not exists.");
		}
		if(SubArea.countByArea(id)==0){
			Area.delete(id);			
			return ok("Selected area has been deleted successfully");
		}
		return 	notFound("Area ['" + area.name + "'] is associated with Sub Area(s)");			
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
