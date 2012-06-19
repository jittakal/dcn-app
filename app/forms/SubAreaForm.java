package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;

public class SubAreaForm implements Serializable{

	@Required
	public String name;

	@Required
	public String areaid;

	@Required
	public String employeeid;	
	
}