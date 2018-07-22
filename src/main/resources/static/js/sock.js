

var socketConn = new WebSocket('ws://localhost:8080/gs');
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

        $.ajax({
            type: "POST",
            url: "updateDocument",
            data: JSON.stringify({ id: docid.textContent, name: docname.textContent, text: doctext.value}),
            contentType: 'application/json',
        });

    }

function changeName() {
    var docname = document.getElementById('doc-name');
    var inputName = document.getElementById('InputName');
    inputName.value = docname.textContent;
    inputName.hidden=false;
    docname.hidden = true;

    inputName.focus();


}

function stopChange() {
    console.log("stopping");
    var docname = document.getElementById('doc-name');
    var inputName = document.getElementById('InputName');
    docname.textContent =  inputName.value;
    inputName.hidden=true;
    docname.hidden = false;

    sendtobase();
}




