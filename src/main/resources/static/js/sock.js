
var socketConn = socketConn = new WebSocket('ws://localhost:8080/gs');
var stompClient = null;
var symbolCount = 0;
setTimeout(sendtobase, 10000);

function startsock() {
    console.log("begin");

}


function send() {
    var doctext = document.getElementById('doctext');
    symbolCount++;
    console.log("send");
    if (doctext.value) {
        socketConn.send(doctext.value);
    }
    if(symbolCount==10)
    {
        symbolCount = 0;
        sendtobase();
    }
}

    socketConn.onmessage = function(event) {
        console.log("recive");
        var doctext = document.getElementById('doctext');
        doctext.value = event.data;
    }

    function closesock()
    {
        sendtobase();
    }

    function sendtobase()
    {
        var text = $('#doctext').html();
        $.post("/saveToDb", text, function (data) {
            
        });
    }



/*
var serverMsg = document.getElementById('serverMsg');
serverMsg.value = event.data;
*/
