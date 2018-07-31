//WebSocket
var socketConn = new WebSocket('ws://localhost:8080/gs');
var symbolCount = 0;

//Send to database every 10 sec
setTimeout(sendToDatabase, 10000);

//Start session and get users
function startSocket() {

    socketConn.send(JSON.stringify({type: "START_MESSAGE", documentId: $("#document-id").html()}));
}

function closeSocket()
{
    sendToDatabase();
}


//save Text and Document Name to Database
function sendToDatabase() {

    let  documentText = document.getElementById('document-text');
    let documentName = document.getElementById('document-name');
    $.ajax({
        type: "PUT",
        url: "/updateDocument/" + $("#document-id").html(),
        data: JSON.stringify({type: "UPDATE_TEXT", documentText: documentText.value, documentName: documentName.textContent}),
        contentType: 'application/json',
    });
}


//Send text to all peers
function sendText() {

    symbolCount++;
    var doctext = document.getElementById('document-text');
    if (doctext.value) {

        socketConn.send(JSON.stringify({type: "UPDATE_TEXT", documentText: doctext.value}));
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
        let newdocumentText = event.data.split(":")[1];
        let doctext = document.getElementById('document-text');

        let offset = event.data.length - doctext.value.length;
        let selection = {start: doctext.selectionStart, end: doctext.selectionEnd};
        let startsSame = event.data.startsWith(doctext.value.substring(0, selection.end));
        let endsSame = event.data.endsWith(doctext.value.substring(selection.start));
        doctext.value = newdocumentText;
        if (startsSame && !endsSame) {
            doctext.setSelectionRange(selection.start, selection.end);
        } else if (!startsSame && endsSame) {
            doctext.setSelectionRange(selection.start + offset, selection.end + offset);
        } else { // this is what google docs does...
            doctext.setSelectionRange(selection.start, selection.end + offset);
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
        let row = versionTable.insertRow(versionTable.rows.length);
        let cell  = row.insertCell(0);
        cell.innerHTML = "<tr><td><h0 id=\"version-id\" hidden=\"hidden\">" + newVersionId + "</h0></td>" +
            "<td><h0 id=\"version-name\" onclick=\"getVersion(this)\">" + newVersionName + "</h0></td>  " +
            "<td><i onclick=\"deleteVersion(this)\" class=\"far fa-trash-alt\" id=\"delete\"></i></td></tr>";
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
    let documentText = document.getElementById('document-text');
    if(versionName.value != "") {
        $.ajax({
            type: "POST",
            url: "/addNewVersion/" + $("#document-id").html(),
            contentType: 'application/json',
            data: JSON.stringify({versionName: versionName.value, versionText: documentText.value}),

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

    var doc = $(element).closest("tr").find("#version-id");
    var documentId = doc.html();
    window.location.href = "/version/" + documentId;
}

