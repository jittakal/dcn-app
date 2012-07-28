package forms;

import java.io.Serializable;
import play.data.validation.Constraints.Required;
import play.data.validation.Constraints.MaxLength;

public class NodeForm implements Serializable{

	@Required
	@MaxLength(value=120)
	public String name;

	@Required
	public String areaid;		
	
}