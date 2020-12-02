import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import spark.Spark;
import spark.Request;
import spark.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

class ServerError {
    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private final String errorCode;
    @SuppressWarnings({"unused","FieldCanBeLocal"})
    private final String message;

    public ServerError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

public class RestfulServer {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(RestfulServer.class);
    private final MessageStorageBackend group_messages;
    // Default constructor for RestfulServer
    public RestfulServer() {
        this.configureRestfulApiServer();
        this.processRestfulApiRequests();
        group_messages = new MessageStorageBackend();
    }

    // Starts REST-ful API on port 8080
    private void configureRestfulApiServer() {
        Spark.port(80); // Starts Spark MicroServer
        System.out.println("Server Configured to listen on port 8080");
    }

    // Configures Spark's REST-ful API routes
    private void processRestfulApiRequests() {
        Spark.get("/", this::echoRequest); // Uses root path and calls echoRequest
        Spark.post("/", this::echoRequest);
        Spark.post("/send_chat", this::processIncomingChatMessage);
        Spark.get("/chat",this::processGetRequest);
        Spark.get("/retrieve_messages",this::processRetrievingMessages);
       // Spark.put("/",this::echoRequest);
       // Spark.patch("/", this::echoRequest);
       // Spark.head("/", this::echoRequest);
       // Spark.delete("/", this::echoRequest);
        
    }

    // Called by Spark.get, can be called anything. Must return a String and take Request & Response args
    private String echoRequest(Request request, Response response) {
        response.type("application/json"); // Output response as JSON
        response.header("Access-Control-Allow-Origin", "*"); //Set to wildcard to share with any calling code
        response.status(200); // Reports an OK status

        return HttpRequestToJson(request);
    }

    // Returns String for echoRequest, which is in turn for Spark.get()
    private String HttpRequestToJson(Request request) {
        System.out.println(request.body());
        return request.body();
    }

    private String getHTTPError(String errorCode,String msg){
        Gson gson = new Gson();
        ServerError err = new ServerError(errorCode,msg);
        return gson.toJson(err);
    }

    private String processIncomingChatMessage(Request request, Response response){

        response.type("application/json"); // Output response as JSON
        response.header("Access-Control-Allow-Origin", "*"); //Set to wildcard to share with any calling code
        response.status(200); // Reports an OK status
        String json_body = request.body();
        Gson gson = new Gson();
        JsonObject msgJson;
        try {
             msgJson = gson.fromJson(json_body, JsonObject.class);
        }catch (JsonSyntaxException e){
            response.status(400);
            return getHTTPError("400", "Invalid JSON Data");
        }
        String type;
        if(msgJson.has("type")) {
            type = msgJson.get("type").getAsString();
        }
        else{
            response.status(400);
            return getHTTPError("400", "Invalid Chat message, missing type field");
        }


        if(type.equals("group_message") && msgJson.has("from") && msgJson.has("subject") && msgJson.has("body") && msgJson.has("thread") && msgJson.has("room")){
            GroupMessage newMessage = new GroupMessage(msgJson.get("from").getAsString(),msgJson.get("subject").getAsString(),msgJson.get("body").getAsString(),msgJson.get("thread").getAsString(),msgJson.get("room").getAsString()); //Construct new Group Message Object
            System.out.println("from: "+newMessage.getFrom());
            System.out.println("subject: "+ newMessage.getSubject());
            System.out.println("body: "+ newMessage.getBody());
            System.out.println("thread: "+ newMessage.getThread());
            System.out.println("msg_id: "+newMessage.getMsgId());
            group_messages.insertMessage(newMessage);
            return gson.toJson(newMessage);
        }
        else{
            response.status(400);
            return getHTTPError("400", "Invalid Chat message");
        }


    }
    /*
     * Processes get request to retrieve messages
     * Looks for the count or since_id parameters in the request and calls their corresponding methods
     * Precedence is given to count if both are specified (DON'T DO THAT)
     */

    private String processRetrievingMessages(Request request, Response response){
        response.type("application/json"); // Output response as JSON
        response.header("Access-Control-Allow-Origin", "*");
        Set<String> params = request.queryParams();
        List<Message> messageList;
        if(params.contains("count")){
            try {
                messageList = group_messages.getLastNMessages(Integer.parseInt(request.queryParams("count")));
            }catch (NumberFormatException ex){
                response.status(400);
                return getHTTPError("400", "Invalid Parameters in GET Request");
            }
        }
        else if(params.contains("since_id")){
            try{
                messageList = group_messages.getMessagesPostedSince(Long.parseLong(request.queryParams("since_id")));
            }catch (NumberFormatException ex){
                response.status(400);
                return getHTTPError("400", "Invalid Parameters in GET Request");
            }
        }
        else{
            response.status(400);
            return getHTTPError("400", "Invalid Parameters in GET Request");
        }
        Gson gson = new Gson();
        return gson.toJson(messageList);

    }

    private String processGetRequest(Request request, Response response){
        response.type("application/json"); // Output response as JSON
        response.header("Access-Control-Allow-Origin", "*"); //Set to wildcard to share with any calling code
        response.status(200); // Reports an OK status
        GroupMessage msg = new GroupMessage("testbot","Hello World", "I'm the body","1","main");
        Gson gson = new Gson();
        return gson.toJson(msg);
    }
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        RestfulServer restfulServer = new RestfulServer();
    }
}