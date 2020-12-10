function postMessage() {
    data = $("#registrationForm").serializeArray();
    // manually converting the data into a form that is readable by the backend
    var startingPoint = JSON.stringify(data).indexOf("subject") + 11 + "subject".length;
    var endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
    var subject = JSON.stringify(data).substring(startingPoint, endingPoint);

    startingPoint = JSON.stringify(data).indexOf("body") + 11 + "body".length;
    endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
    var body = JSON.stringify(data).substring(startingPoint, endingPoint);
    data = "{\"type\":\"group_message\", \"from\":\"" + document.cookie + "\", \"subject\":\"" + subject + "\", \"body\":\"" + body + "\", \"thread\":\"" + "thread" + "\", \"room\":\"" + "room" + "\"}";
    $.ajax({
        url:      'http://localhost:8080/send_chat',
        method:   'POST',
        dataType: 'group_message',
        data:     data,
        success:  function() { },
        error:    function(error) { }
    });
    window.alert("Sending...");
}

function nextIndexOf(strToIndex, index, char) {
    var newString = strToIndex.substring(index, strToIndex.length);
    return newString.indexOf(char) + index;
}

function setupChat() {
    var Http = new XMLHttpRequest(); // creates a new HttpRequest Object

    if(document.cookie == "") { // if no cookies exits
        var name = prompt("What is your name?", "John Doe");
        Http.open("POST", '/register_user');
        data = "{\"name\"=\"" + name + "\"}";
        Http.send(data);
        document.cookie = name;
    }
    updateChat();
}

function updateChat() {
    var Http = new XMLHttpRequest(); // creates a new HttpRequest Object
    var url='retrieve_messages?count=10';
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
        if(Http.responseText != "[]") { // if not blank
//            document.getElementById("MessageHistory").innerHTML = Http.responseText; //Unformatted Past 10 Messages
            document.getElementById("MessageHistory").innerHTML = formatAllMessages(Http.responseText);
        }
        else
            document.getElementById("MessageHistory").innerHTML = "No messages sent";
    }
}

function formatAllMessages(input) {
    var inputParsed = JSON.parse(input);
    var output = "";
    inputParsed.forEach((msg) => {
        output = output + formatAMessage(JSON.stringify(msg)) + "<br>";
    });
//    window.alert(output);
    return output;
}

function formatAMessage(input, startingIndex = 0) {
    var index = nextIndexOf(input, startingIndex, "from") + 3 + 4;
    var from = input.substring(index, nextIndexOf(input, index, "\""))
    index = nextIndexOf(input, startingIndex, "subject") + 3 + 7;
    var subject = input.substring(index, nextIndexOf(input, index, "\""))
    index = nextIndexOf(input, startingIndex, "body") + 3 + 4;
    var body = input.substring(index, nextIndexOf(input, index, "\""))
    index = nextIndexOf(input, startingIndex, "timestamp") + 3 + 9;
    var timestamp = input.substring(index + 12, nextIndexOf(input, index, "Z\""))

    var output = from + " @ " + timestamp + ": " + body + "\n";
    if(subject != "")
        output = from + " @ " + timestamp + ": " + subject + " - " + body + "\n";
    return output;
}