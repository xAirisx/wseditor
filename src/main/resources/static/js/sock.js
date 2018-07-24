
var socketConn = new WebSocket('ws://localhost:8080/gs');
var symbolCount = 0;
setTimeout(sendtobase, 10000);

socketConn.onopen = function (e) {

    var docid = document.getElementById('docid');
    console.log(docid.textContent);
    //set session to document
    socketConn.send(JSON.stringify({type: "START_MESSAGE", docId: docid.textContent}));
    socketConn.send(JSON.stringify({type: "GET_USERS_MESSAGE", docId: docid.textContent}));
};


function send() {
    var doctext = document.getElementById('doctext');
    symbolCount++;
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

    var docid = document.getElementById('docid');
    var doctext = document.getElementById('doctext');
    var docname = document.getElementById('doc-name');
    if (event.data.startsWith("users:")) {
        //Update users table
        var users = event.data.split(":")[1].split(",");
        var usersTable = $("#users").find(".table");
        var usersHtml = "";
        for (var index in users) {
            usersHtml += "<tr><td>" + users[index] + "</td></tr>";
        }
        usersTable.html(usersHtml);
    } else if (event.data.startsWith("PEERS_UPDATED")) {
        var docid = document.getElementById('docid');
        socketConn.send(JSON.stringify({type: "GET_USERS_MESSAGE", docId: docid.textContent}));
    }
    else if(event.data.startsWith("NAME_UPDATED"))
    {
        var newdocname = event.data.split(":")[1];
        var docname = document.getElementById('doc-name');
        docname.textContent = newdocname;
    }
    else {
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
    socketConn.send(JSON.stringify({ docName: docname.textContent}));

}




