package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import models.Member;
import play.*;
import play.cache.Cache;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.*;
import play.mvc.Http.Context;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render("Your new application is ready."));
  }

  public static Result raffle() {  
	return ok(raffle.render(getEmptyMemberForm()));
  }
  
  public static Result showGroups() {
	  Form<Member> memberForm = getEmptyMemberForm().bindFromRequest();
	  if(memberForm.hasErrors()) {
		  flash("error", "Please correct the form below");
		  return badRequest(raffle.render(memberForm));
	  } else {
		  Member member = memberForm.get();
		  session("memberId", member.id);
		  Cache.set("key" + member.id, member.key);
		  
		  WS.WSRequestHolder request = WS.url("http://api.meetup.com/2/groups");
		  request.setQueryParameter("key", member.key);
		  request.setQueryParameter("member_id", member.id);
		  Promise<Response> response = request.get();
		  
//		  return ok(groups.render(response.get().asJson().findValuesAsText("urlname")));
		  
		  return async(
		      response.map(
			  new Function<Response, Result>() {
				  public Result apply(Response r) {
					  return ok(groups.render(r.asJson().findValuesAsText("urlname")));
				  }
			  }
		   )
		 );
	  }
  }
  
  private static Form<Member> getEmptyMemberForm() {
	  return form(Member.class);
  }
}