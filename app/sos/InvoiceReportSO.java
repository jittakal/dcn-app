package sos;

import java.io.Serializable;
import java.util.Map;
import java.util.LinkedHashMap;

public class InvoiceReportSO implements Serializable{

	//public String name;

	public Map<Integer,Integer> invoice=new LinkedHashMap<Integer,Integer>();

	public Map<Integer,Integer> payment=new LinkedHashMap<Integer,Integer>();

	public InvoiceReportSO(){
		//this.name=iname;			
		for(int i=1; i<13; i++){
			invoice.put(i,0);
			payment.put(i,0);
		}
	}		

	public void addInvoice(Integer month, Integer itotal){
		invoice.put(month,itotal);
	}

	public void addPayment(Integer month, Integer ptotal){
		payment.put(month,ptotal);
	}
	
}

