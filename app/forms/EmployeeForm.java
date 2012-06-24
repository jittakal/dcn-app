package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.MaxLength;
import java.util.Date;
import java.util.Calendar;
import play.data.format.Formats.DateTime;

public class EmployeeForm implements Serializable{

	@Required
	public String name;

	@Required
	public String address;

	@MaxLength(value=10)
	public String mobile_number;

	@Required
	@DateTime(pattern="dd/MM/yyyy")
	public Date joining_date;

	@DateTime(pattern="dd/MM/yyyy")
	public Date terminate_date;

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