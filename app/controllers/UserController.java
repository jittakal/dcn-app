package controllers;

import java.util.List;

import models.User;
import forms.ChangePasswdForm;
import play.data.Form;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.user.*;
import views.html.common.*;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;

@Authenticated(value = DcnAuthenticator.class)
public class UserController extends Controller {

	final static Form<ChangePasswdForm> changePasswdForm = form(ChangePasswdForm.class);

	public static Result index(){
		return ok(index.render());
	}

	public static Result get_change_passwd(){
		return ok(passwd.render(changePasswdForm));
	}

	public static Result change_passwd(){
		Form<ChangePasswdForm> filledForm = changePasswdForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return badRequest(passwd.render(filledForm));
		}
					
		ChangePasswdForm cpForm=filledForm.get();
		if(cpForm==null){			
			return badRequest(passwd.render(filledForm));
		}

		User auser=User.authenticate(session().get("username"),cpForm.current_passwd);
		if(auser==null){			
			filledForm.reject("Invalid current password, please try again");
			return badRequest(passwd.render(filledForm));	
		}

		auser.password=cpForm.new_passwd;
		auser.update();
				
		return ok(msg.render("Password changed successfully"));
	}

	/**
	 * Return List of all Users in JSON format.
	 * 
	 * @return
	 */
	public static Result all() {
		List<User> users = User.all();
		return ok(Json.toJson(users));
	}

	/**
	 * Return User by id.
	 * 
	 * @return
	 */
	public static Result get(Long id) {
		if (id == null) {
			return badRequest("Expecting User Id");
		}

		User user = User.get(id);

		if (user == null) {
			return notFound("User with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(user);
		return ok(result);
	}

	/**
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result create() {
		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		User user = Json.fromJson(jsonNode, User.class);
		if (user == null) {
			return badRequest("Expecting User Json data");
		}
		user.save();
		ObjectNode result = Json.newObject();
		result.put("id", user.id);
		return created(result);
	}

	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		if (id == null) {
			return badRequest("Expecting User Id");
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		User user = Json.fromJson(jsonNode, User.class);
		if (user == null) {
			return badRequest("Expecting User Json data");
		}
		user.id = id;
		user.update();
		return ok();
	}

}
