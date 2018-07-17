

var socketConn = socketConn = new WebSocket('wss://localhost:8080/gs');
var stompClient = null;
var symbolCount = 0;
setTimeout(sendtobase, 10000);

function startsock() {
    console.log("begin");

}


function send() {
    var doctext = document.getElementById('doctext');
    console.log(doctext.value);
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

    function sendtobase() {
        var docid = document.getElementById('docid');
        var doctext = document.getElementById('doctext');
        var docname = document.getElementById('doc-name');
        console.log(doctext.value);
        console.log(docid.textContent);
        console.log(docname.textContent);

        $.ajax({
            type: "POST",
            url: "updateDocument",
            data: JSON.stringify({ id: docid.textContent, name: docname.textContent, text: doctext.value}),
            contentType: 'application/json',
        });



    }





