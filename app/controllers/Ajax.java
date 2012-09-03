package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.codehaus.jackson.JsonNode;

import play.cache.Cache;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

public class Ajax extends Controller {
	private static Random random = new Random();
	
	public static Result winner(String gid) {
		WS.WSRequestHolder request = WS.url("http://api.meetup.com/2/members");
		request.setQueryParameter("key", Cache.get("key" + session("memberId")).toString());
		request.setQueryParameter("group_urlname", gid);
		Promise<Response> response = request.get();
		
		return async(
		    response.map(new Function<Response, Result>() {
		    	public Result apply(Response r) {
		    		Iterator<JsonNode> memberResults = r.asJson().get("results").getElements();
		    		
		    		List<String> members = new ArrayList<String>();
		    		while(memberResults.hasNext()) {
		    			members.add(memberResults.next().get("name").asText());
		    		}

		    		return ok(members.get(random.nextInt(members.size())));
		    	}
		    }
		    )		
		);
		
		
		
	}
}
