window.onload = initalizePage;

var subjects = null;

function initalizePage() {
    subjects = backendGet("/getsubjects");
    console.log(subjects);
    $("#subjects").empty();
    //find semester count
    var semestermax = 0;
    $.each(subjects, function (i, data) {
        if(data.semester > semestermax){
            semestermax = data.semester;
        }
    });
    console.log(semestermax);
    //semesters setup
    for(i = 1; i <= semestermax;i++){
      $("#subjects").append("<div class='semesterblock'><strong>"+ i +".félév</strong></div>");
    }
    //load subjects to semesters
    for(i = 0; i < subjects.length; i++){
        $(".semesterblock:nth-of-type(" +subjects[i].semester + ")").append(
                "<div class='subject' id='"+ subjects[i].code +"'><div class='subjectname'>"+ subjects[i].name +"</div><div class='subjectcode'>"+ subjects[i].code +"</div><div class='subjectcredit'>"+ subjects[i].creditValue +"</div></div>");
    }

    $(".subject").click(function(event){
      var clickedSubject;
        if($(this).hasClass("subject")){
          clickedSubject = this.id;
        }else{
          clickedSubject = this.parent().id;
        }

      $("#"+clickedSubject).css("border-color","red");
      for(i = 0; i <= semestermax;i++){
        for(y= 0; y < subjects[i].prerequisites.length; y++){
            console.log(subjects[i].prerequisites[y]);
            console.log(clickedSubject);
          if(subjects[i].prerequisites[y] == clickedSubject){
            console.log(subjects[i].prerequisites[y]);
            $("#"+subjects[i].code).css("background-color","green");
          }
        }
      }

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
