package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.MaxLength;
import java.util.Date;
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
}