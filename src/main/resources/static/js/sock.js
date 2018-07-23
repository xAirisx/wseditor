

var socketConn = new WebSocket('ws://localhost:8080/gs');
var stompClient = null;
var symbolCount = 0;
setTimeout(sendtobase, 10000);

socketConn.onopen = function (e) {

    var docname = document.getElementById('doc-name');
    socketConn.send(JSON.stringify({docName: docname.textContent}));
    socketConn.send(JSON.stringify({documentName: docname.textContent}))
};


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
        if (event.data.startsWith("users:")) {
            var users = event.data.split(":")[1].split(",");
            console.log(users);
            var usersTable = $("#users").find(".table");
            var usersHtml = "";
            for (var index in users) {
                usersHtml += "<tr><td>" + users[index] + "</td></tr>";
            }
            usersTable.html(usersHtml);
        } else if (event.data.startsWith("PEERS_UPDATED")) {
            var docname = document.getElementById('doc-name');
            socketConn.send(JSON.stringify({documentName: docname.textContent}))
        } else {
            var doctext = document.getElementById('doctext');
            var offset = event.data.length - doctext.value.length;
            var selection = {start: doctext.selectionStart, end: doctext.selectionEnd};
            var startsSame = event.data.startsWith(doctext.value.substring(0, selection.end));
            var endsSame = event.data.endsWith(doctext.value.substring(selection.start));
            doctext.value = event.data;
            if (startsSame && !endsSame) {
                doctext.setSelectionRange(selection.start, selection.end);
            } else if (!startsSame && endsSame) {
                doctext.setSelectionRange(selection.start + offset, selection.end + offset);
            } else { // this is what google docs does...
                doctext.setSelectionRange(selection.start, selection.end + offset);
            }
        }
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




