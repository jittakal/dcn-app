package controllers;

import java.util.List;

import models.*;

import forms.AreaForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.BodyParser;
import play.libs.Json;
import org.codehaus.jackson.*;
import org.codehaus.jackson.node.ObjectNode;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;
import play.i18n.Messages;
import views.html.area.*;


@Authenticated(value = DcnAuthenticator.class)
public class AreaController extends Controller {

	final static Form<AreaForm> areaForm = form(AreaForm.class);
		
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
			return badRequest(create.render(filledForm));
		}
					
		AreaForm areaForm=filledForm.get();
		if(areaForm==null){			
			return badRequest(create.render(filledForm));
		}
		
		if(Area.isNameExists(0L,areaForm.name)){
			filledForm.reject(Messages.get("area.name.exists"));
			return badRequest(create.render(filledForm));	
		}

		Area area=new Area();
		area.name=areaForm.name;
		Employee employee=new Employee();
		employee.id=new Long(areaForm.employeeid);		
		area.employee=employee;
		area.save();
		area.refresh();

		Node node=new Node();
		node.name=area.name + Messages.get("default.node");
		node.area=area;
		node.save();

		Amply amply=new Amply();
		amply.name=area.name + Messages.get("default.amplify");
		amply.area=area;
		amply.save();
								
		return redirect(controllers.routes.AreaController.index());
	}

	public static Result update_get(Long id){
		Area area=Area.get(id);		
		AreaForm aForm=new AreaForm();
		aForm.name=area.name;
		aForm.employeeid=area.employee.id.toString();		
		return ok(update.render(areaForm.fill(aForm),id));
	}

	public static Result update_save(Long id){
		Form<AreaForm> filledForm = areaForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(update.render(filledForm,id));
		}
					
		AreaForm areaForm=filledForm.get();
		if(areaForm==null){			
			return badRequest(update.render(filledForm,id));
		}

		if(Area.isNameExists(id,areaForm.name)){
			filledForm.reject(Messages.get("area.name.exists"));
			return badRequest(update.render(filledForm,id));	
		}

		Area area=Area.get(id);		
		area.name=areaForm.name;
		Employee employee=new Employee();
		employee.id=new Long(areaForm.employeeid);		
		area.employee=employee;
		area.update();
								
		return  redirect(controllers.routes.AreaController.index());
	}

	public static Result delete(Long id){		
		Area area=Area.get(id);		
		if(area==null){
			return notFound(Messages.get("area.not.exists", id));
		}
		if(!SubArea.isBelongsToArea(id)){
			Area.delete(id);			
			return ok(Messages.get("area.delete.succ"));
		}
		return 	notFound(Messages.get("area.assoc.subarea",area.name));			
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
			return badRequest(Messages.get("area.id.expecting"));
		}
		
		Area area=Area.get(id);
		
		if(area==null){
			return notFound(Messages.get("area.not.exists", id));
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
			return badRequest(Messages.get("common.json.expecting"));
		}		
		Area area=Json.fromJson(jsonNode, Area.class);
		if(area==null){
			return badRequest(Messages.get("common.json.expecting"));
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
			return badRequest(Messages.get("area.id.expecting"));
		}
		
		JsonNode jsonNode = request().body().asJson();
		if(jsonNode ==null){
			return badRequest(Messages.get("common.json.expecting"));
		}		
		Area area=Json.fromJson(jsonNode, Area.class);
		if(area==null){
			return badRequest(Messages.get("common.json.expecting"));
		}
		area.id=id;
		area.update();
		return ok();
	}

}
