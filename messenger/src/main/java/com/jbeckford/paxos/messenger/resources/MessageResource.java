package com.jbeckford.paxos.messenger.resources;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.jbeckford.paxos.messenger.model.Message;
import com.jbeckford.paxos.messenger.model.MessageHash;
import com.jbeckford.paxos.messenger.service.MessageService;

//Challenge #1 - Programming : Publish a small service on the web that has two endpoints:
@Path("/messages")
public class MessageResource {
	private MessageService messageService = new MessageService();

	public MessageResource(){
	}
	
	// 1. /messages takes a message (a string) as a POST and returns the SHA256 hash digest
	// of that message (in hexadecimal format)
	// Example : $ curl -X POST -H "Content-Type: application/json" -d '{"message": "foo"}' http://mywebsite.com/messages
	//
	// Returns : 
	// {
	// "digest": "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae"
	// }

	@POST
	@Path("/{text}")
    @Produces(MediaType.APPLICATION_JSON)
	public MessageHash addMessage(@PathParam("text") String text){
		Message message = new Message(text);
		messageService.addMessage(message);
		return message.getMessageHash();
	}	

	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MessageHash addMessage(Message message){
		if(message != null) {
			// Making a copy of the deserialized message since the hash could be wrong. The copy will contain the correct hash.
			Message messageCopy = new Message(message.getText());
			messageService.addMessage(messageCopy);
			return messageCopy.getMessageHash();
		}
		return null;
	}	

	@GET
	@Path("/autoAdd")
    @Produces(MediaType.APPLICATION_JSON)
	public List<Message> autoIncrementAndGetMessages(){
		addMessage("The current time:" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())); 
		return messageService.getMessages();
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages(){
		return messageService.getMessages();
	}

	// 2. /messages/<hash> is a GET request that returns the original message. A request to a
	//
	// You can calculate that your result is correct on the command line:
	// $ echo -n "foo" | shasum -a 256
	// 2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae -

	
	//You can now query your service for the original message:
	// $ curl http://mywebsite.com/messages/2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae
	//
	// Returns : 
	// {
	// "message": "foo"
	// }
	
	// non-existent <hash> should return a 404 error.
	//
	// $ curl -i http://mywebsite.com/messages/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
	//
	// Returns : 
	// HTTP/1.0 404 NOT FOUND
	// Content-Type: application/json
	// Content-Length: 36
	// Server: Werkzeug/0.11.5 Python/3.5.1
	// Date: Wed, 31 Aug 2016 14:21:11 GMT
	// {
	// "err_msg": "Message not found"
	// }
	// (your specifics may vary, all that matters is that you get a 404)
	// 
	// Hint: When does ordering of messages you POST vs digests you GET matter?
	// Optional bonus question : What would the bottlenecks in your implementation be as you
	// acquire more users. How you might scale your microservice?@Path("/messages")	@GET

//	@GET
//	@Path("/{hash}")
//    @Produces(MediaType.APPLICATION_JSON)
//	public Message getMessage(@PathParam("hash") String hash){
//		Message message = messageService.getMessage(hash);
//		
//		if(message == null) {
//			 throw new HTTPException(404);
//		}
//		return message;
//	}	
	
	@GET
	@Path("/{hash}")
	public Response getMessage(@PathParam("hash") String hash) throws JsonGenerationException, JsonMappingException, IOException{
	    if(hash == null || hash.trim().length() == 0) {
	        return Response.serverError().entity("Hash is null or empty.").build();
	    }

		Message message = messageService.getMessage(hash);

	    if(message == null) {
			// non-existent <hash> should return a 404 error.
			//
			// $ curl -i http://mywebsite.com/messages/aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
			//
			// Returns : 
			// HTTP/1.0 404 NOT FOUND
			// Content-Type: application/json
			// Content-Length: 36
			// Server: Werkzeug/0.11.5 Python/3.5.1
			// Date: Wed, 31 Aug 2016 14:21:11 GMT
			// {
			// "err_msg": "Message not found"
			// }
			// (your specifics may vary, all that matters is that you get a 404)
	        return Response.serverError().status(Response.Status.NOT_FOUND).entity("Message not found for hash: " + hash).build();
	    }

	    ObjectMapper mapper = new ObjectMapper();
	    String messageJson = mapper.writeValueAsString(message);
	    return Response.ok(messageJson, MediaType.APPLICATION_JSON).build();
	}
}

//	Hint: When does ordering of messages you POST vs digests you GET matter?

//	Optional bonus question : What would the bottlenecks in your implementation be as you acquire more users. 

//How you might scale your microservice?
