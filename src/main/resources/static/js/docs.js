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

    var doc = $(element).closest("tr").find("#id");

    $.ajax({
        url: "/deleteDocument",
        type: "POST",
        contentType: "text/plain",
        data: doc.html()
    }).done(function () {
        location.reload();
    });

}