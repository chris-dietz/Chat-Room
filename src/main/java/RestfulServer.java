import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import spark.Spark;
import spark.Request;
import spark.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ServerError {
    private String errorCode;
    private String message;

    public ServerError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

public class RestfulServer {
    private final Logger log = LoggerFactory.getLogger(RestfulServer.class);
    // Default constructor for RestfulServer
    public RestfulServer() {
        this.configureRestfulApiServer();
        this.processRestfulApiRequests();
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
        Spark.post("/send_chat", this::processChatMessage);
        Spark.get("/chat",this::processGetRequest);
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

    private String processChatMessage(Request request, Response response){
        response.type("application/json"); // Output response as JSON
        response.header("Access-Control-Allow-Origin", "*"); //Set to wildcard to share with any calling code
        response.status(200); // Reports an OK status
        String json_body = request.body();
        Gson gson = new Gson();
        JsonObject msgJson;
        //Todo: Change this so that it calls the message constructor instead of deserializing directly, use JsonObject from gson. Also figure out how to make an object of the correct subclass.
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
            String response_body = gson.toJson(newMessage);
            return response_body;
        }
        else{
            response.status(400);
            return getHTTPError("400", "Invalid Chat message");
        }


    }
    private String processGetRequest(Request request, Response response){
        response.type("application/json"); // Output response as JSON
        response.header("Access-Control-Allow-Origin", "*"); //Set to wildcard to share with any calling code
        response.status(200); // Reports an OK status
        GroupMessage msg = new GroupMessage("testbot","Hello World", "I'm the body","1","main");
        Gson gson = new Gson();
        String responseBody = gson.toJson(msg);
        return responseBody;
    }
    public static void main(String[] args) {
        RestfulServer restfulServer = new RestfulServer();
    }
}