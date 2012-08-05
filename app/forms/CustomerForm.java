package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Calendar;
import play.data.format.Formats.DateTime;

public class CustomerForm implements Serializable{

	@Required
	@MaxLength(value=120)
	public String name;

	@Required
	public String areaid;
		
	@Required
	public String subareaid;

	@Required
	public String nodeid;

	@Required
	public String amplyid;	
	
	@Required
	@MaxLength(value=255)
	public String address;
	
	@MaxLength(value=10)
	@Pattern(regexp="[0-9]*")
	public String mobile_number;
	
	@MaxLength(value=10)
	@Pattern(regexp="[0-9]*")
	public String id_number;		
	
	@Required	
	@DateTime(pattern="dd/MM/yyyy")
	public Date joining_date;
	
	@DateTime(pattern="dd/MM/yyyy")
	public Date terminate_date;
		
	@Required
	public String priceid;
	
	@Required
	public Integer deposite;

	public String validate() {  
		if(joining_date!=null && joining_date.after(Calendar.getInstance().getTime())){
			return "Joining date should not be future date.";
		}

		if(terminate_date!=null && terminate_date.equals(joining_date)){
			return "Terminate date should not be same as joining date.";
		}

		if(terminate_date!=null && terminate_date.after(Calendar.getInstance().getTime())){
			return "Terminate date should not be the future date.";
		}

		if(terminate_date!=null && terminate_date.before(joining_date)){
			return "Terminate date should be after joining date.";
		} 			
        return null;
    }
	
}