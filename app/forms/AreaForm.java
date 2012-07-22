package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import models.Area;
import play.data.validation.Constraints.MaxLength;

public class AreaForm implements Serializable{

	@Required
	@MaxLength(value=120)
	public String name;	

	@Required
	public String employeeid;
	
}