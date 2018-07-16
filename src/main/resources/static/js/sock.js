

var socketConn = socketConn = new WebSocket('ws://localhost:8080/gs');
var stompClient = null;
var symbolCount = 0;
setTimeout(sendtobase, 30000);

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

    function sendtobase() {
        var docid = document.getElementById('docid');
        var doctext = document.getElementById('doctext');

        console.log(doctext.value);
        console.log(docid.value);
        console.log("we are here");
        // $.post("/savedocument", { json_string:JSON.stringify({Document:doc}) });
        // $.post("/updatedocument", { json_string:JSON.stringify({id:"1", text: doctext.value}) });

         $.ajax({
             type: 'POST',
             dataType: 'json',
             contentType:'application/json',
             url: "/adddocument",
             data:JSON.stringify(doctext.value),
             success: function(data, textStatus ){
                 console.log(data);
                 alert("success");
             },
             error: function(xhr, textStatus, errorThrown){
                // alert('request failed'+errorThrown);
             }
         });



    }






/*
var serverMsg = document.getElementById('serverMsg');
serverMsg.value = event.data;
*/
