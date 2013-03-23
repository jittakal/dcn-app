package controllers;

import java.util.List;
import models.Node;
import models.Area;
import models.Customer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.Map;

import forms.NodeForm;
import play.data.*;
import static play.data.Form.*;
import views.html.node.*;

import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;
import play.i18n.Messages;

@Authenticated(value = DcnAuthenticator.class)
public class NodeController extends Controller {

	final static Form<NodeForm> nodeForm = form(NodeForm.class);

	public static Result index(){
		return ok(index.render());
	}

	public static Result create_new(){		
		return ok(create.render(nodeForm));
	}

	public static Result create_save(){
		Form<NodeForm> filledForm = nodeForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(create.render(filledForm));
		}
					
		NodeForm nodeForm=filledForm.get();
		if(nodeForm==null){						
			return badRequest(create.render(filledForm));
		}

		if(Node.isNameExists(0L,nodeForm.name)){			
			filledForm.reject(Messages.get("name.already.exists"));
			return badRequest(create.render(filledForm));	
		}

		Node node=formToModel(nodeForm);
		node.save();
								
		return redirect(controllers.routes.NodeController.index());
	}

	public static Result update_get(Long id){		
		Node node=Node.get(id);
		NodeForm saForm=new NodeForm();
		saForm.name=node.name;
		saForm.areaid=node.area.id.toString();				
		return ok(update.render(nodeForm.fill(saForm),id));
	}

	public static Result update_save(Long id){
		Form<NodeForm> filledForm = nodeForm.bindFromRequest();

		if(filledForm.hasErrors()){			
			return badRequest(update.render(filledForm,id));
		}
					
		NodeForm nodeForm=filledForm.get();
		if(nodeForm==null){						
			return badRequest(update.render(filledForm,id));
		}		

		if(Node.isNameExists(id,nodeForm.name)){			
			filledForm.reject(Messages.get("name.already.exists"));
			return badRequest(update.render(filledForm,id));	
		}

		Node node=formToModel(nodeForm);
		node.id=id;
		node.update();
								
		return  redirect(controllers.routes.NodeController.index());
	}

	private static Node formToModel(NodeForm nodeForm){
		Area area=new Area();
		area.id=new Long(nodeForm.areaid);				

		Node node=new Node();		
		node.name=nodeForm.name;
		node.area=area;		
		return node;
	}

	public static Result delete(Long id){		
		Node node=Node.get(id);
		if(node==null){
			return notFound(Messages.get("id.not.exists",id));
		}
		if(!Customer.isBelongsToNode(id)){
			Node.delete(id);			
			return ok(Messages.get("record.deleted.succ"));
		}
		return notFound(Messages.get("node.assoc.customer",node.name));			
	}
	
	/**
	 * Return List of all Areas in JSON format.
	 * @return
	 */
	public static Result all(){
		List<Node> areas=Node.all();
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
		
		Node node=Node.get(id);
		
		if(node==null){
			return notFound(Messages.get("id.not.exists",id));
		}
		JsonNode result = Json.toJson(node);		
		return ok(result);
	}
	
	/**
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result create(){		
		JsonNode jsonNode = request().body().asJson();
		if(jsonNode ==null){
			return badRequest(Messages.get("json.expecting"));
		}		
		Node node=Json.fromJson(jsonNode, Node.class);
		if(node==null){
			return badRequest(Messages.get("json.expecting"));
		}
		node.save();
		ObjectNode result=Json.newObject();
		result.put("id",node.id);
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
		
		JsonNode jsonNode = request().body().asJson();
		if(jsonNode ==null){
			return badRequest(Messages.get("json.expecting"));
		}		
		Node node=Json.fromJson(jsonNode, Node.class);
		if(node==null){
			return badRequest(Messages.get("json.expecting"));
		}
		node.id=id;
		node.update();
		return ok();
	}
}