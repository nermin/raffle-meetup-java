package models;

import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;

public class Member {
	
	@Required(message = "id is required")
	@Pattern(value = "\\d+", message = "Must be all digits")
	public String id;
	
	@Required(message = "key is required")
	public String key;
	
	public Member() {}
	
	public Member(String id, String key) {
		this.id = id;
		this.key = key;
	}
	
}
