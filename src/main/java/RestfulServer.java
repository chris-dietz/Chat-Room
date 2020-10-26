import spark.Spark;
import spark.Request;
import spark.Response;

public class RestfulServer {
    // Default constructor for RestfulServer
    public RestfulServer() {
        this.configureRestfulApiServer();
        this.processRestfulApiRequests();
    }

    // Starts RESTful API on port 8080
    private void configureRestfulApiServer() {
        Spark.port(8080); // Starts Spark MicroServer
        System.out.println("Server Configured to listen on port 8080");
    }

    // Configures Spark's RESTful API routes
    private void processRestfulApiRequests() {
         Spark.get("/", this::echoRequest); // Uses root path and calls echoRequest
    }

    // Called by Spark.get, can be called anything. Must return a String and take Request & Response args
    private String echoRequest(Request request, Response response) {
        response.type("application/json"); // Output response as JSON
        response.header("Access-Control-Allow-Origin", "*"); //Set to wildcard to share with any calling code
        response.status(200); // Reports an OK status
        // response.status(418); // Reports that I am a teapot

        return HttpRequestToJson(request);
    }

    // Returns String for echoRequest, which is inturn for Spark.get()
    private String HttpRequestToJson(Request request) {
        return "{\n"
                +"\"attributes\":\""    + request.attributes()      + "\",\n"
                +"\"body\":\""          + request.body()            + "\",\n"
                +"\"contentType\":\""   + request.contentType()     + "\",\n"
                +"\"contextPath\":\""   + request.contextPath()     + "\",\n"
                +"\"cookies\":\""       + request.cookies()         + "\",\n"
                +"\"headers\":\""       + request.headers()         + "\",\n"
                +"\"host\":\""          + request.host()            + "\",\n"
                +"\"ip\":\""            + request.ip()              + "\",\n"
                +"\"params\":\""        + request.params()          + "\",\n"
                +"\"pathInfo\":\""      + request.pathInfo()        + "\",\n"
                +"\"port\":\""          + request.port()            + "\",\n"
                +"\"protocol\":\""      + request.protocol()        + "\",\n"
                +"\"queryParams\":\""   + request.queryParams()     + "\",\n"
                +"\"requestMethod\":\"" + request.requestMethod()   + "\",\n"
                +"\"scheme\":\""        + request.scheme()          + "\",\n"
                +"\"servletPath\":\""   + request.servletPath()     + "\",\n"
                +"\"session\":\""       + request.session()         + "\",\n"
                +"\"uri\":\""           + request.uri()             + "\",\n"
                +"\"url\":\""           + request.url()             + "\",\n"
                +"\"userAgent\":\""     + request.userAgent()       + "\",\n"
                + "}";
    }

    public static void main(String[] args) {
        RestfulServer restfulServer = new RestfulServer();
    }
}