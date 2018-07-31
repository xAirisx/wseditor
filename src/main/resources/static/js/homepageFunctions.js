
//Document function

//Delete Document or Version
function deleteDocument(element) {

    var doc = $(element).closest("tr").find("#document-id");

    $.ajax({
        type: "DELETE",
        url: "/deleteDocument/" + doc.html(),
        success: function () {
            console.log(doc.html() + " deleted");
            location.reload();
        },
        error: function (e) {
            console.log(e);
        }
    });

}

function addNewDocument() {
    $.ajax({
        type: "POST",
        url: "/addDocument",
        data: JSON.stringify({}),
        contentType: 'application/json',
        success: function () {
            console.log("Document was added");
            location.reload();
        },
        error: function (e) {
            console.log(e);
        }
    });

}


//Version function


function showVersionTable(element) {

    let documentId = $(element).closest("tr").find("#document-id").html();

    $.ajax({
        url: "/showVersionTable/" + documentId,
        type: "GET",
        contentType: "application/json",
        success: function (data) {
            if (data.length > 0) {

                //show table
                let table = $(`#version-table-container-${documentId}`).find(".table");
                let row = "";
                $.each(data, function (index, obj) {
                    row += "<tr><td onclick='getVersion(this)'>" + obj.name + "</td>" +
                        " <td><i onclick=\"deleteDocument(this)\" class=\"far fa-trash-alt\" id=\"delete\"></i></td>" +
                        "<td><h0 id=\"document-id\" hidden=\"hidden\">" + obj.id + "</h0></td></tr>";
                });
                table.html(row);
            }
            else {

                let table = $(`#version-table-container-${documentId}`).find(".table");
                let row = "<tr><td id='no-version-message'>There is no version!</td></tr>";
                table.html(row);
            }

            //change arrow
            let arrowUp = $(element).closest("tr").find("#arrow-up");
            let arrowDown = $(element).closest("tr").find("#arrow-down");
            arrowUp.prop("hidden", false);
            arrowDown.prop("hidden", true);

        }
    });
}

function deleteVersionTable(element)
{
    let documentId = $(element).closest("tr").find("#document-id").html();
    let table = $(`#version-table-container-${documentId}`).find(".table");
     table.empty();


    //Change arrow
    $(element).closest("tr").find("#arrow-up").prop("hidden", true);
    $(element).closest("tr").find("#arrow-down").prop("hidden", false);
}


function getVersion(element) {

    let documentId = $(element).closest("tr").find("#document-id").html();
    window.location.href = "/version/" + documentId;
}