package controllers;

import java.util.List;

import models.Price;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class PriceController extends Controller {

	/**
	 * Return List of all Prices in JSON format.
	 * 
	 * @return
	 */
	public static Result all() {
		List<Price> prices = Price.all();
		return ok(Json.toJson(prices));
	}

	/**
	 * Return Price by id.
	 * 
	 * @return
	 */
	public static Result get(Long id) {
		if (id == null) {
			return badRequest("Expecting Price Id");
		}

		Price price = Price.get(id);

		if (price == null) {
			return notFound("Price with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(price);
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
		Price price = Json.fromJson(jsonNode, Price.class);
		if (price == null) {
			return badRequest("Expecting Price Json data");
		}
		price.save();
		ObjectNode result = Json.newObject();
		result.put("id", price.id);
		return created(result);
	}

	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		if (id == null) {
			return badRequest("Expecting Price Id");
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		Price price = Json.fromJson(jsonNode, Price.class);
		if (price == null) {
			return badRequest("Expecting Price Json data");
		}
		price.id = id;
		price.update();
		return ok();
	}

}
