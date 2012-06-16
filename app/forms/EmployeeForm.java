package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.MaxLength;
import java.util.Date;

public class EmployeeForm implements Serializable{

	@Required
	public String name;

	@Required
	public String address;

	@MaxLength(value=10)
	public String mobile_number;

	@Required
	public Date joining_date;

	public Date terminating_date;
}