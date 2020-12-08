function eleven() {
    document.getElementById("demo").innerHTML += "<br/><br/>Goodbye World!";
}
function two() {
    document.getElementById("demo").innerHTML += "<br/><br/>YAY!";
}

function createProfile() {
    data = $("#registrationForm").serializeArray();
    // manually converting the data into a form that is readable by the backend
    var startingPoint = JSON.stringify(data).indexOf("from") + 11 + "from".length;
    var endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
    var from = JSON.stringify(data).substring(startingPoint, endingPoint);

    startingPoint = JSON.stringify(data).indexOf("subject") + 11 + "subject".length;
    endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
    var subject = JSON.stringify(data).substring(startingPoint, endingPoint);

    startingPoint = JSON.stringify(data).indexOf("body") + 11 + "body".length;
    endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
    var body = JSON.stringify(data).substring(startingPoint, endingPoint);

    startingPoint = JSON.stringify(data).indexOf("thread") + 11 + "thread".length;
    endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
    var thread = JSON.stringify(data).substring(startingPoint, endingPoint);

    startingPoint = JSON.stringify(data).indexOf("room") + 11 + "room".length;
    endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
    var room = JSON.stringify(data).substring(startingPoint, endingPoint);
    data = "{ \"type\":\"group_message\", \"from\":\"" + from + "\", \"subject\":\"" + subject + "\", \"body\":\"" + body + "\", \"thread\":\"" + thread + "\", \"room\":\"" + room + "\"}";

    $.ajax({
        url:         'http://localhost:8080/send_chat',
        method:      'POST',
        dataType:    'group_message',
        data:         data,
        success:      function() { two(); },
        error:        function(error) { /*window.location.replace("invalid.html");*/ }
    });
    window.alert("Your message has been sent!");
}

function nextIndexOf(strToIndex, index, char) {
    var newString = strToIndex.substring(index, strToIndex.length);
    return newString.indexOf(char) + index;
}