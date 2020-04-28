window.onload = initalizePage;

var subjects = null;

function initalizePage() {
    curriculums = backendGet("/getcurriculum/proginfo");
    console.log(curriculums);
    $("#subjects").empty();
    $.each(curriculums.subjects, function (i, data) {
        $("#subjects").append("Név: " + data.name + ", ");
        $("#subjects").append("Kód: " + data.code + ", ");
        $("#subjects").append("Kreditérték: " + data.creditValue + ", ");
        $("#subjects").append("<br>     Előfeltételek kódjai: ");
        $.each(data.prerequisites, function (j, prerequisite) {
            $("#subjects").append(prerequisite + ", ");
        });
        $("#subjects").append("<br>");
    });
}

function backendGet(endpoint) {
    var url = "http://" + window.location.host + "/" + endpoint;

    var response = null;
    $.ajax({
        type: "GET",
        url: url,
        async: false,
        contentType: "application/json; charset=utf-8",
        success: function(result) {
            response = result;
        },
        error: function(e) {
            handleEndpointException(e);
        }
    });
    return response;
}
 
function backendPost(endpoint, json) {
    var url = "http://" + window.location.host + "/" + endpoint;

    var response = null;
    $.ajax({
        type: "POST",
        url: url,
        async: false,
        data: json,
        contentType: "application/json; charset=utf-8",
        success: function(result) {
            response = result;
        },
        error: function(e) {
            handleEndpointException(e);
        }
    });
    return response;
}

function handleEndpointException(error) {
    console.log(error);
}
