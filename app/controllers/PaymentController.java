package controllers;

import java.util.List;

import models.Payment;
import models.Invoice;
import models.Customer;
import java.util.Calendar;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;

@Authenticated(value = DcnAuthenticator.class)
public class PaymentController extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static Result payment() {
		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}

		for(JsonNode jsNode:jsonNode){
			Long invoiceId=Long.valueOf(jsNode.path("id").getTextValue());
			Invoice invoice=Invoice.get(invoiceId);

			if(!invoice.paid){
				Payment payment=new Payment();
				payment.invoice=invoice;
				payment.payment_date=Calendar.getInstance().getTime();
				payment.amount=invoice.amount;
				payment.save();

				Customer customer=invoice.customer;
				customer.balance -= invoice.amount;
				customer.update();

				invoice.paid=true;
				invoice.update();	
			}			
		}

		return ok();
	}

	public static Result all() {
		List<Payment> payments = Payment.all();
		return ok(Json.toJson(payments));
	}

	public static Result get(Long id) {
		if (id == null) {
			return badRequest("Expecting Payment Id");
		}

		Payment payment = Payment.get(id);

		if (payment == null) {
			return notFound("Payment with id [" + id + "] does not exists.");
		}
		JsonNode result = Json.toJson(payment);
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
		Payment payment = Json.fromJson(jsonNode, Payment.class);
		if (payment == null) {
			return badRequest("Expecting Payment Json data");
		}
		payment.save();
		ObjectNode result = Json.newObject();
		result.put("id", payment.id);
		return created(result);
	}

	/**
	 * @param id
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		if (id == null) {
			return badRequest("Expecting Payment Id");
		}

		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null) {
			return badRequest("Expecting Json data");
		}
		Payment payment = Json.fromJson(jsonNode, Payment.class);
		if (payment == null) {
			return badRequest("Expecting Payment Json data");
		}
		payment.id = id;
		payment.update();
		return ok();
	}

}
