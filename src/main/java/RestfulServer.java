import spark.Spark;
import spark.Request;
import spark.Response;

public class RestfulServer {
    // Default constructor for RestfulServer
    public RestfulServer() {
        this.configureRestfulApiServer();
        this.processRestfulApiRequests();
    }

    // Starts REST-ful API on port 8080
    private void configureRestfulApiServer() {
        Spark.port(8080); // Starts Spark MicroServer
        System.out.println("Server Configured to listen on port 8080");
    }

    // Configures Spark's REST-ful API routes
    private void processRestfulApiRequests() {
        Spark.get("/", this::echoRequest); // Uses root path and calls echoRequest
        Spark.post("/", this::echoRequest);
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

    public static void main(String[] args) {
        RestfulServer restfulServer = new RestfulServer();
    }
}