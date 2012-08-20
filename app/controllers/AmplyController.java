package controllers;

import java.util.List;
import models.Amply;
import models.Area;
import models.Customer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.Map;

import forms.AmplyForm;
import play.data.Form;
import views.html.amply.*;

import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;
import play.i18n.Messages;

@Authenticated(value = DcnAuthenticator.class)
public class AmplyController extends Controller {

	final static Form<AmplyForm> amplyForm = form(AmplyForm.class);

	public static Result index(){
		return ok(index.render());
	}

	public static Result create_new(){		
		return ok(create.render(amplyForm));
	}

	public static Result create_save(){
		Form<AmplyForm> filledForm = amplyForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(create.render(filledForm));
		}
					
		AmplyForm amplyForm=filledForm.get();
		if(amplyForm==null){						
			return badRequest(create.render(filledForm));
		}

		if(Amply.isNameExists(0L,amplyForm.name)){			
			filledForm.reject(Messages.get("name.already.exists"));
			return badRequest(create.render(filledForm));	
		}

		Amply amply=formToModel(amplyForm);
		amply.save();
								
		return redirect(controllers.routes.AmplyController.index());
	}

	public static Result update_get(Long id){		
		Amply amply=Amply.get(id);
		AmplyForm saForm=new AmplyForm();
		saForm.name=amply.name;
		saForm.areaid=amply.area.id.toString();				
		return ok(update.render(amplyForm.fill(saForm),id));
	}

	public static Result update_save(Long id){
		Form<AmplyForm> filledForm = amplyForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(update.render(filledForm,id));
		}
					
		AmplyForm amplyForm=filledForm.get();
		if(amplyForm==null){						
			return badRequest(update.render(filledForm,id));
		}		

		if(Amply.isNameExists(id,amplyForm.name)){			
			filledForm.reject(Messages.get("name.already.exists"));
			return badRequest(update.render(filledForm,id));	
		}

		Amply amply=formToModel(amplyForm);
		amply.id=id;
		amply.update();
								
		return  redirect(controllers.routes.AmplyController.index());
	}

	private static Amply formToModel(AmplyForm amplyForm){
		Area area=new Area();
		area.id=new Long(amplyForm.areaid);				

		Amply amply=new Amply();		
		amply.name=amplyForm.name;
		amply.area=area;		
		return amply;
	}

	public static Result delete(Long id){		
		Amply amply=Amply.get(id);
		if(amply==null){
			return notFound(Messages.get("id.not.exists",id));
		}
		if(!Customer.isBelongsToAmply(id)){
			Amply.delete(id);			
			return ok(Messages.get("record.deleted.succ"));
		}
		return notFound(Messages.get("amplifier.assoc.customer",amply.name));		 
	}
	
	/**
	 * Return List of all Areas in JSON format.
	 * @return
	 */
	public static Result all(){
		List<Amply> areas=Amply.all();
		return ok(Json.toJson(areas));
	}


	/**
	 * Return Area by id.
	 * @return
	 */	
	public static Result get(Long id){
		if(id==null){
			return badRequest(Messages.get("id.expecting"));
		}
		
		Amply amply=Amply.get(id);
		
		if(amply==null){
			return notFound(Messages.get("id.not.exists",id));
		}
		JsonNode result = Json.toJson(amply);		
		return ok(result);
	}
	
	/**
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result create(){		
		JsonNode jsonAmply = request().body().asJson();
		if(jsonAmply ==null){
			return badRequest(Messages.get("json.expecting"));
		}		
		Amply amply=Json.fromJson(jsonAmply, Amply.class);
		if(amply==null){
			return badRequest(Messages.get("json.expecting"));
		}
		amply.save();
		ObjectNode result=Json.newObject();
		result.put("id",amply.id);
		return created(result);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id){
		if(id==null){
			return badRequest(Messages.get("id.expecting"));
		}
		
		JsonNode jsonAmply = request().body().asJson();
		if(jsonAmply ==null){
			return badRequest(Messages.get("json.expecting"));
		}		
		Amply amply=Json.fromJson(jsonAmply, Amply.class);
		if(amply==null){
			return badRequest(Messages.get("json.expecting"));
		}
		amply.id=id;
		amply.update();
		return ok();
	}
}