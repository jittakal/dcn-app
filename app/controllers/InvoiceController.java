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
import play.data.*;
import static play.data.Form.*;
import forms.InvoiceSearchForm;
import forms.InvoiceGenerateForm;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import models.Area;
import models.SubArea;
import models.Customer;
import services.InvoiceService;
import views.html.invoice.*;

@Authenticated(value = DcnAuthenticator.class)
public class InvoiceController extends Controller {

	final static Form<InvoiceSearchForm> invoiceSearchForm = form(InvoiceSearchForm.class);

	final static Form<InvoiceGenerateForm> invoiceGenerateForm = form(InvoiceGenerateForm.class);

	final static int curmonth = Calendar.getInstance().get(Calendar.MONTH);

	final static int curyear = Calendar.getInstance().get(Calendar.YEAR);

	public static Result generate_get() {						
		return ok(generate.render(invoiceGenerateForm,null));
	}

	public static Map<String,String> monthAsMap() {
		Map<String,String> monthMap=new LinkedHashMap<String,String>();				
		monthMap.put(String.valueOf(curmonth+1),get_month_map().get(String.valueOf(curmonth+1)));		
		return monthMap;
	}

	public static Map<String,String> yearAsMap() {		
		Map<String,String> yearMap=new LinkedHashMap<String,String>();
		yearMap.put(String.valueOf(curyear),String.valueOf(curyear));
		return yearMap;
	}

	public static Result generate() {				
		Form<InvoiceGenerateForm> filledForm = invoiceGenerateForm.bindFromRequest();				
		
		if(filledForm.hasErrors()){			
			return badRequest(generate.render(filledForm,null));
		}

		InvoiceGenerateForm iGenerateForm=filledForm.get();
		if(iGenerateForm==null){
			return badRequest(generate.render(filledForm,null));
		}

		InvoiceService.generateInvoice(Integer.parseInt(iGenerateForm.month),Integer.parseInt(iGenerateForm.year));
		List<Invoice> invoices=Invoice.search(Integer.parseInt(iGenerateForm.month),Integer.parseInt(iGenerateForm.year));	
		return ok(generate.render(filledForm,invoices));
	}

	public static Result search_get() {				
		InvoiceSearchForm iSearchForm=new InvoiceSearchForm();
		iSearchForm.month=String.valueOf(curmonth+1);
		iSearchForm.year=String.valueOf(curyear);

		return ok(search.render(invoiceSearchForm.fill(iSearchForm),null));
	}

	public static Result on_area_change(){
		Form<InvoiceSearchForm> filledForm = invoiceSearchForm.bindFromRequest();				

		if(filledForm.hasErrors()){			
			return badRequest(search.render(invoiceSearchForm,null));
		}

		InvoiceSearchForm iSearchForm=filledForm.get();
		if(iSearchForm==null){
			return badRequest(search.render(filledForm,null));
		}
				
		return ok(search.render(invoiceSearchForm.fill(iSearchForm),null));	
	}

	public static Result on_subarea_change(){
		Form<InvoiceSearchForm> filledForm = invoiceSearchForm.bindFromRequest();				

		if(filledForm.hasErrors()){			
			return badRequest(search.render(filledForm,null));
		}

		InvoiceSearchForm iSearchForm=filledForm.get();
		if(iSearchForm==null){
			return badRequest(search.render(filledForm,null));
		}		

		return ok(search.render(invoiceSearchForm.fill(iSearchForm),null));	
	}	

	public static Map<String,String> get_month_map(){
		Map<String,String> monthMap=new LinkedHashMap<String,String>();
		monthMap.put("1","January");monthMap.put("2","February");
		monthMap.put("3","March");monthMap.put("4","April");
		monthMap.put("5","May");monthMap.put("6","June");
		monthMap.put("7","July");monthMap.put("8","August");
		monthMap.put("9","September");monthMap.put("10","October");
		monthMap.put("11","November");monthMap.put("12","December");
		return monthMap;
	}

	public static Map<String,String> get_year_map(){
		Map<String,String> yearMap=new LinkedHashMap<String,String>();		
		int cyear=curyear;
		for(int i=0;i<5;i++){
			String cur_year=String.valueOf(cyear);
			yearMap.put(cur_year,cur_year);
			cyear-=1;
		}		
		return yearMap;
	}

	public static List<String> get_year_list(){
		List<String> yearList=new ArrayList<String>();		
		int cyear=curyear;
		for(int i=0;i<5;i++){
			String cur_year=String.valueOf(cyear);
			yearList.add(cur_year);
			cyear-=1;
		}		
		return yearList;
	}

	public static Result search() {
		Form<InvoiceSearchForm> filledForm = invoiceSearchForm.bindFromRequest();		
		
		if(filledForm.hasErrors()){			
			return badRequest(search.render(filledForm,null));
		}

		InvoiceSearchForm iSearchForm=filledForm.get();
		if(iSearchForm==null){
			return badRequest(search.render(filledForm,null));
		}

		List<Invoice> invoices=null;

		if(iSearchForm.subareaid==null || "".equals(iSearchForm.subareaid.trim())){
			invoices=Invoice.search(Integer.parseInt(iSearchForm.month),
				Integer.parseInt(iSearchForm.year),new Long(iSearchForm.areaid));	
		}else{
			invoices=Invoice.search(Integer.parseInt(iSearchForm.month),
				Integer.parseInt(iSearchForm.year),new Long(iSearchForm.areaid),new Long(iSearchForm.subareaid));	
		}
				
		return ok(search.render(invoiceSearchForm.fill(iSearchForm),invoices));	
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
