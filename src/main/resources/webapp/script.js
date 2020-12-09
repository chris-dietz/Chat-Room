function postMessage() {
    data = $("#registrationForm").serializeArray();
    // manually converting the data into a form that is readable by the backend
    var startingPoint = JSON.stringify(data).indexOf("subject") + 11 + "subject".length;
    var endingPoint = nextIndexOf(JSON.stringify(data), startingPoint, "\"");
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
    data = "{ \"type\":\"group_message\", \"from\":\"" + document.cookie + "\", \"subject\":\"" + subject + "\", \"body\":\"" + body + "\", \"thread\":\"" + "thread" + "\", \"room\":\"" + "room" + "\"}";

    $.ajax({
        url:      'http://localhost:8080/send_chat',
        method:   'POST',
        dataType: 'group_message',
        data:     data,
        success:  function() { },
        error:    function(error) { }
    });
    window.alert("Your message has been sent!");
}

function nextIndexOf(strToIndex, index, char) {
    var newString = strToIndex.substring(index, strToIndex.length);
    return newString.indexOf(char) + index;
}

function setupChat() {
    if(document.cookie == "") { // If no cookies exits
        var name = prompt("What is your name?", "John Doe");
        document.cookie = name;
    }
    const Http = new XMLHttpRequest();
    const url='retrieve_messages?count=10';
    Http.open("GET", url);
    Http.send();

    Http.onreadystatechange = (e) => {
      document.getElementById("demo").innerHTML = Http.responseText; //Unformmatted Past 10 Messages
    }
}