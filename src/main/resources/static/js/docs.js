function adddoc() {
    $.ajax({
        type: "POST",
        url: "/addDocument",
        data: JSON.stringify({}),
        contentType: 'application/json',
    }).done(function () {
        location.reload();
    });

}

function deletedoc(element) {

    var docname = $(element).closest("tr").find("#link");
    $.ajax({
        url: "/deleteDocument",
        type: "POST",
        contentType: "text/plain",
        data: docname.html()
    }).done(function () {
        location.reload();
    });

}