package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;

public class EmployeeForm implements Serializable{

	@Required
	public String name;

	@Required
	public String address;

	public String mobile_number;

	@Required
	public String joining_date;

	public String terminating_date;
}