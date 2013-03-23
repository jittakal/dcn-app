package controllers;

import java.util.List;

import models.User;
import forms.ChangePasswdForm;
import play.data.*;
import static play.data.Form.*;
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
import play.i18n.Messages;


public class UserController extends Controller {

	final static Form<ChangePasswdForm> changePasswdForm = form(ChangePasswdForm.class);

	@Authenticated(value = DcnAuthenticator.class)
	public static Result index(){
		return ok(index.render());
	}

	@Authenticated(value = DcnAuthenticator.class)
	public static Result get_change_passwd(){
		return ok(passwd.render(changePasswdForm));
	}

	@Authenticated(value = DcnAuthenticator.class)
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
			filledForm.reject(Messages.get("invalid.current.password"));
			return badRequest(passwd.render(filledForm));	
		}

		auser.password=cpForm.new_passwd;
		auser.update();
				
		return ok(msg.render(Messages.get("password.changed.succ")));
	}

	/**
	 * Return List of all Users in JSON format.
	 * 
	 * @return
	 */
	@Authenticated(value = DcnAuthenticator.class)
	public static Result all() {
		List<User> users = User.all();
		return ok(Json.toJson(users));
	}

	/**
	 * Return User by id.
	 * 
	 * @return
	 */
	@Authenticated(value = DcnAuthenticator.class)
	public static Result get(Long id) {
		if (id == null) {
			return badRequest(Messages.get("expecting.id"));
		}

		User user = User.get(id);

		if (user == null) {
			return notFound(Messages.get("id.not.exists",id));
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
			return badRequest(Messages.get("expecting.json"));
		}
		User user = Json.fromJson(jsonNode, User.class);
		if (user == null) {
			return badRequest(Messages.get("expecting.json"));
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
	@Authenticated(value = DcnAuthenticator.class)
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		if (id == null) {
			return badRequest(Messages.get("expecting.id"));
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest(Messages.get("expecting.json"));
		}
		User user = Json.fromJson(jsonNode, User.class);
		if (user == null) {
			return badRequest(Messages.get("expecting.json"));
		}
		user.id = id;
		user.update();
		return ok();
	}

}
