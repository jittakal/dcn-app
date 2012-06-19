package controllers;

import java.util.List;

import models.User;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.user.*;

public class UserController extends Controller {

	public static Result index(){
		return ok(index.render());
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
