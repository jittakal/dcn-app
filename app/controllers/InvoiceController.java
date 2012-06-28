package controllers;

import java.util.List;

import models.Invoice;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import util.DcnAuthenticator;
import play.data.Form;
import forms.InvoiceSearchForm;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedHashMap;
import models.Area;
import models.SubArea;
import models.Customer;
import views.html.invoice.*;

@Authenticated(value = DcnAuthenticator.class)
public class InvoiceController extends Controller {

	final static Form<InvoiceSearchForm> invoiceSearchForm = form(InvoiceSearchForm.class);

	public static Result search_get() {
		Map<String,String> areaMap=Area.asMap();
		Map<String,String> subareaMap=new LinkedHashMap<String,String>();
		Map<String,String> customerMap=new LinkedHashMap<String,String>();
		Map<String,String> monthMap=get_month_map();
		Map<String,String> yearMap=get_year_map();

		if(!areaMap.isEmpty()){
			String areaid=areaMap.keySet().iterator().next();
			subareaMap.putAll(SubArea.asMapByAreaId(new Long(areaid)));
		}

		return ok(search.render(invoiceSearchForm,areaMap,subareaMap,customerMap,monthMap,yearMap));
	}

	public static Result on_area_change(){
		Form<InvoiceSearchForm> filledForm = invoiceSearchForm.bindFromRequest();

		Map<String,String> areaMap=Area.asMap();
		Map<String,String> subareaMap=new LinkedHashMap<String,String>();
		Map<String,String> customerMap=new LinkedHashMap<String,String>();
		Map<String,String> monthMap=get_month_map();
		Map<String,String> yearMap=get_year_map();		

		if(filledForm.hasErrors()){			
			return badRequest(search.render(invoiceSearchForm,areaMap,subareaMap,customerMap,monthMap,yearMap));
		}

		InvoiceSearchForm iSearchForm=filledForm.get();
		if(iSearchForm==null){
			return badRequest(search.render(invoiceSearchForm,areaMap,subareaMap,customerMap,monthMap,yearMap));
		}

		if(iSearchForm.areaid!=null){
			subareaMap.putAll(SubArea.asMapByAreaId(new Long(iSearchForm.areaid)));
		}

		return ok(search.render(invoiceSearchForm.fill(iSearchForm),areaMap,subareaMap,customerMap,monthMap,yearMap));	
	}

	public static Result on_subarea_change(){
		Form<InvoiceSearchForm> filledForm = invoiceSearchForm.bindFromRequest();

		Map<String,String> areaMap=Area.asMap();
		Map<String,String> subareaMap=new LinkedHashMap<String,String>();
		Map<String,String> customerMap=new LinkedHashMap<String,String>();
		Map<String,String> monthMap=get_month_map();
		Map<String,String> yearMap=get_year_map();		

		if(filledForm.hasErrors()){			
			return badRequest(search.render(invoiceSearchForm,areaMap,subareaMap,customerMap,monthMap,yearMap));
		}

		InvoiceSearchForm iSearchForm=filledForm.get();
		if(iSearchForm==null){
			return badRequest(search.render(invoiceSearchForm,areaMap,subareaMap,customerMap,monthMap,yearMap));
		}

		if(iSearchForm.areaid!=null){
			subareaMap.putAll(SubArea.asMapByAreaId(new Long(iSearchForm.areaid)));
		}

		if(iSearchForm.subareaid!=null){
			customerMap.putAll(Customer.asMapBySubAreaId(new Long(iSearchForm.subareaid)));
		}

		return ok(search.render(invoiceSearchForm.fill(iSearchForm),areaMap,subareaMap,customerMap,monthMap,yearMap));	
	}	

	private static Map<String,String> get_month_map(){
		Map<String,String> monthMap=new LinkedHashMap<String,String>();
		monthMap.put("1","Jan");monthMap.put("2","Feb");
		monthMap.put("3","March");monthMap.put("4","April");
		monthMap.put("5","May");monthMap.put("6","June");
		monthMap.put("7","July");monthMap.put("8","August");
		monthMap.put("9","Sept");monthMap.put("10","Oct");
		monthMap.put("11","Nov");monthMap.put("12","Dec");
		return monthMap;
	}

	private static Map<String,String> get_year_map(){
		Map<String,String> yearMap=new LinkedHashMap<String,String>();
		yearMap.put("2012","2012");yearMap.put("2011","2011");
		yearMap.put("2010","2010");yearMap.put("2009","2009");		
		return yearMap;
	}

	public static Result search() {
		List<Invoice> invoices = Invoice.all();
		return ok(Json.toJson(invoices));
	}

	
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
