package controllers;

import java.util.List;

import models.Invoice;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class InvoiceController extends Controller {

	
	public static Result all() {
		List<Invoice> invoices = Invoice.all();
		return ok(Json.toJson(invoices));
	}

	
	public static Result get(Long id) {
		if (id == null) {
			return badRequest("Expecting Invoice Id");
		}

		Invoice invoice = Invoice.get(id);

		if (invoice == null) {
			return notFound("Invoice with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(invoice);
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
		Invoice invoice = Json.fromJson(jsonNode, Invoice.class);
		if (invoice == null) {
			return badRequest("Expecting Invoice Json data");
		}
		invoice.save();
		ObjectNode result = Json.newObject();
		result.put("id", invoice.id);
		return created(result);
	}

	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		if (id == null) {
			return badRequest("Expecting Invoice Id");
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		Invoice invoice = Json.fromJson(jsonNode, Invoice.class);
		if (invoice == null) {
			return badRequest("Expecting Invoice Json data");
		}
		invoice.id = id;
		invoice.update();
		return ok();
	}

}
