//WebSocket
var socketConn = new WebSocket('wss://wstexteditor.herokuapp.com/gs');
var symbolCount = 0;

//Send to database every 10 sec
setTimeout(sendToDatabase, 10000);

document.onreadystatechange = function () {

    tinymce.init({
        selector: 'textarea',
        width: "750",
        setup: function (ed) {
            ed.on('KeyUp', function (e) {
                sendText();
            });
        }
    });

    tinymce.DOM.bind(document, 'click', function(e) {
        sendText();
    });
}

function start(){
    socketConn = new WebSocket('wss://wstexteditor.herokuapp.com/gs');
}

socketConn.onclose = function(){
        setTimeout(function(){start()}, 5000);
    console.log("reset connection")
    };


//Start session and get users
function startSocket() {

    if(!socketConn.readyState){
        setTimeout(function (){
            startSocket();
        },100);
    }else{
           socketConn.send(JSON.stringify({type: "START_MESSAGE", documentId: $("#document-id").html()}));
    }
}

function closeSocket()
{
    sendToDatabase();
}


//save Text and Document Name to Database
function sendToDatabase() {

    let  documentText = tinyMCE.activeEditor.getContent();
    let documentName = document.getElementById('document-name');
    $.ajax({
        type: "PUT",
        url: "/updateDocument/" + $("#document-id").html(),
        data: JSON.stringify({type: "UPDATE_TEXT", documentText: documentText, documentName: documentName.textContent}),
        contentType: 'application/json',
    });
}


//Send text to all peers
function sendText() {

    var  documentText = tinyMCE.activeEditor.getContent();
    symbolCount++;
    if (documentText) {

        socketConn.send(JSON.stringify({type: "UPDATE_TEXT", documentText: documentText}));
    }
    if (symbolCount == 10) {
        symbolCount = 0;
        sendToDatabase();
    }
}

//Messages from socket or text to update
socketConn.onmessage = function(event) {


    if(event.data.startsWith("TEXT_UPDATED"))
    {
        var newdocumentText = event.data.split("|")[1];
        var  documentText = tinyMCE.activeEditor.getContent();

        var offset = newdocumentText.length - documentText.length;
        var selection = {start: documentText.selectionStart, end: documentText.selectionEnd};
        var startsSame = newdocumentText.startsWith(documentText.substring(0, selection.end));
        var endsSame = newdocumentText.endsWith(documentText.substring(selection.start));

        tinyMCE.activeEditor.setContent(newdocumentText);
        if (startsSame && !endsSame) {
            documentText.setSelectionRange(selection.start, selection.end);
        } else if (!startsSame && endsSame) {
            documentText.setSelectionRange(selection.start + offset, selection.end + offset);
        } else { // this is what google docs does...
            documentText.setSelectionRange(selection.start, selection.end + offset);
        }
    }

    //Peer was added or gone
    else if(event.data.startsWith("PEERS_UPDATED"))
    {
        socketConn.send(JSON.stringify({type: "GET_USERS_NAME", documentId: $("#document-id").html()}));
    }

    //Updated user list
    else if (event.data.startsWith("USER_TABLE_UPDATE")) {

        //Update users table
        var users = event.data.split(":")[1].split(",");
        var usersTable = $("#user-container").find(".table");
        var usersHtml = "";
        for (var index in users) {
            usersHtml += "<tr><td>" + users[index] + "</td></tr>";
        }
        usersTable.html(usersHtml);
    }
    //Update document name
    else if(event.data.startsWith("NAME_UPDATED"))
    {
        var newdocname = event.data.split(":")[1];
        var docname = document.getElementById('document-name');
        console.log( $("#document-name").html());
        docname.textContent = newdocname;
    }
    //Update version table (Version was added)
    else if (event.data.startsWith("VERSION_UPDATED"))
    {
        let newVersionName = event.data.split(":")[1];
        let newVersionId = event.data.split(":")[2];
        let versionTable = document.getElementById('version-table');
        var row = versionTable.insertRow(versionTable.rows.length);
        var cell0  = row.insertCell(0);
        var cell1 = row.insertCell(1);
        var cell2 = row.insertCell(2);
        cell0.innerHTML = "<h0 id=\"version-id\" hidden=\"hidden\">" + newVersionId + "</h0>";
        cell1.innerHTML= "<h0 id=\"version-name\" onclick=\"getVersion(this)\">" + newVersionName + "</h0>";
        cell2.innerHTML = "<i onclick=\"deleteVersion(this)\" class=\"far fa-trash-alt\" id=\"delete\"></i>";

    }

    //Update version table (Version was deleted)
    else if (event.data.startsWith("VERSION_DELETED")) {
        let versionTable = document.getElementById('version-table');
        let i = 0;
        let deletedVersionId = event.data.split(":")[1];
        $('#version-table tr').filter(function () {
            if ($.trim($('h0', this).eq(0).text()) == deletedVersionId) {
                versionTable.deleteRow(i);
                return;
            }
            else {
                i++;
            }
        });
    }
}


//show Input
function changeName() {

    let documentName = document.getElementById('document-name');
    let inputName = document.getElementById('input-name');

    inputName.value = documentName.textContent;
    inputName.hidden=false;
    documentName.hidden = true;
    inputName.focus();

}
//Save new document name to database and sent to all peers
function stopChange() {

    let documentName = document.getElementById('document-name');
    let inputName = document.getElementById('input-name');

    documentName.textContent =  inputName.value;
    inputName.hidden=true;
    documentName.hidden = false;
    sendToDatabase();
    socketConn.send(JSON.stringify({type: "UPDATE_NAME", documentName: documentName.textContent}));

}

//Version

function addNewVersion() {

    let versionName = document.getElementById('new-version-name');
    let documentText = tinyMCE.activeEditor.getContent();
    if(versionName.value != "") {
        $.ajax({
            type: "POST",
            url: "/addNewVersion/" + $("#document-id").html(),
            contentType: 'application/json',
            data: JSON.stringify({versionName: versionName.value, versionText: documentText}),

            success: function (data) {
                console.log("Version was added");
                socketConn.send(JSON.stringify({
                    type: "UPDATE_VERSION",
                    versionName: versionName.value,
                    versionId: data
                }));
            }
        });
    }


}

function deleteVersion(element) {

    let versionId = $(element).closest("tr").find("#version-id").html();

    $.ajax({
        type: "DELETE",
        url: "/deleteDocument/" + versionId,
        success: function () {
            console.log(versionId + " deleted");
            socketConn.send(JSON.stringify({type: "DELETE_VERSION", versionId: versionId}));
        },
        error: function (e) {
            console.log(e);
        }
    });

}

function getVersion(element) {

    sendToDatabase();
    var doc = $(element).closest("tr").find("#version-id");
    var documentId = doc.html();
    window.location.href = "/version/" + documentId;
}

