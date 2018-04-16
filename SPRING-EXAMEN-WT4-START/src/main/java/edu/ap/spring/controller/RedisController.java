package edu.ap.spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.ap.spring.model.InhaalExamen;
import edu.ap.spring.redis.RedisService;

@Controller
@RequestMapping("")
public class RedisController {

   private List<String> redisMessages = new ArrayList<String>();
   private RedisService service;
	
   @Autowired
	public void setRedisService(RedisService service) {
		this.service = service;
   }
     
   @RequestMapping("/messages")
   @ResponseBody
   public String messages() {
	   String html = "<HTML><HEAD><meta http-equiv=\"refresh\" content=\"5\"></HEAD>";
	   html += "<BODY><h1>Messages</h1><br/><br/><ul>";
	   for(String m : redisMessages) {
		   html += "<li>" + m;
	   }
	   html += "</BODY></HTML>";
	   
	   return html;
   }
   /*
   @RequestMapping("/listold")
   @ResponseBody
   public String list() {

	   String html = "<HTML>";
	   // get the bitcount of our counter
	   html += "<BODY><h1>" + service.bitCount("moviescount") + " Movies</h1><br/><br/><ul>";
	   
	   Set<byte[]> movies = service.keys("movies:*");
	   for(byte[] m : movies) {
		   // make a key from the byte array
		   String key = new String(m);
		   // get hash with actors
		   Map<Object, Object> actors = service.hgetAll(key);
		   // get all parts of the key, eg : ["movies", "1998", "The Big Lebowski"]
		   String[] parts = key.split(":");
		   
		   html += "<li>" + parts[2] + " (" + parts[1] + ")<br/>";
		   html += "Actors : ";
		   // iterate over actors
		   for(Entry<Object, Object> entry : actors.entrySet()) {
			   html += entry.getValue() + ", ";
		   }
		   // strip off last ', '
		   html = html.substring(0, html.length() - 2);
	   }
	   html += "</BODY></HTML>";
	   
	   return html;
   }
   */
   // Messaging
   public void onMessage(String message) {
	   this.redisMessages.add(message);
   }
/*
   @RequestMapping(method = RequestMethod.POST, value = "/new")
   ResponseEntity<?> newStudent(@PathVariable String userId, @RequestBody InhaalExamen input) {
	   return service.;
	}
			this.student = student;
    		this.exam = exam;
    		this.reason = reason;
    	    this.date = date;
	*/
   @RequestMapping(method = RequestMethod.POST, value = "/new")
   @ResponseBody
   ResponseEntity<?> newStudent(@ModelAttribute InhaalExamen input) {
	   System.out.println(input.toString());
	   Map<String, String> entry = new HashMap<String, String>();
	   entry.put("student", input.getStudent());
	   entry.put("exam", input.getExam());
	   entry.put("reason", input.getReason());
	   entry.put("date", input.getDate());
	   String key = "inhaalexamen:" + entry.get("student") + entry.get("exam") + entry.get("reason");
	   
	   System.out.println(key);
	   System.out.println(service.getKey(key));
	   HttpHeaders responseHeaders = new HttpHeaders();
       return new ResponseEntity<>(key, responseHeaders, HttpStatus.OK);
	}
   
   @RequestMapping("/list")
   @ResponseBody
	public String listPersons(@RequestParam("name") String naam) {
	   	Map<Object, Object> output = service.hgetAll(naam);
    	System.out.println("Listed");
	
	   String html = "<HTML><HEAD><meta http-equiv=\"refresh\" content=\"5\"></HEAD>";
	   html += "<BODY><h1>Messages</h1><br/><br/><ul>" + output + "</BODY></HTML>";
	   
	   return html;
   }
}
