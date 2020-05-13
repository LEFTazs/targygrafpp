window.onload = getCurriculums;

function getCurriculums(){
  allcuriculums = backendGet("/getallcurriculumnames");
  allcuriculumsid = backendGet("/getallcurriculumids");
  console.log(allcuriculums);
  console.log(allcuriculumsid);
  allcuriculums.forEach(element =>
     $(".curriculums").append("<div class='curriculum'>"+ element +"</div>")
   );
   $(".curriculum").click(function(){
     var selectedindex = 0;
     for(var i = 0; i<allcuriculums.length;i++){
       if(this.innerText == allcuriculums[i]){
         initalizePage(allcuriculumsid[i]);
       }
     }
   });
}
var subjects = null;


function initalizePage(curriculum) {
    selectedCurriculum = backendGet("/getcurriculum/"+curriculum);
    subjects = selectedCurriculum.subjects;
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
      $("#subjects").append("<div class='semesterblock'><p class ='semestertitle'>"+ i +".félév</p></div>");
    }
    //load subjects to semesters
    for(i = 0; i < subjects.length; i++){
        $(".semesterblock:nth-of-type(" +subjects[i].semester + ")").append(
                "<div class='subject' id='"+ subjects[i].code +"'><div class='subjectname'>"+ subjects[i].name +"</div><div class='subjectcode'>"+ subjects[i].code +"</div><div class='subjectcredit'>"+ subjects[i].creditValue +"</div></div>");
    }

    //load diff subjects
    var addedTitle=false;
    for(i = 0; i < subjects.length; i++){
      if(subjects[i].semester == 0){
          if (!addedTitle) {
              $("#subjects").append("<br><p class = 'semestertitle'>Differenciált szakmai ismeretek</p>");
              addedTitle=true;
          }
        $("#subjects").append("<div class='subject diffsubject' id='"+ subjects[i].code +"'><div class='subjectname'>"+ subjects[i].name +"</div><div class='subjectcode'>"+ subjects[i].code +"</div><div class='subjectcredit'>"+ subjects[i].creditValue +"</div></div>");
      }
    }

    //color effects
    /*$(".subject").hover(function(event){
      subjectClicked(this);
    });
    $(".subject").click(function(event){
      subjectClicked(this);

    });*/
    var colorActive = true;
      $(".subject").hover(function(event){
        if(!colorActive){
            subjectClicked(this);
        }
      });
    $(".subject").click(function(event){
      subjectClicked(this);
    });
    $("#subjects").click(function(event){
      if(colorActive){
        colorActive = false;
      }else{
        colorActive = true;
      }
    });
}


function subjectClicked(clicked){
  $(".subject").css("background-color","white");
  $(".subject").css("border-color","rgb(195,195,195)");
  $(".subject").css("border-width","1px");
  var clickedSubject;
    if($(clicked).hasClass("subject")){
      clickedSubject = clicked.id;
    }else{
      clickedSubject = clicked.parent().id;
    }
  console.log(clickedSubject);
  $("#"+clickedSubject).css("border-color","blue");
  $("#"+clickedSubject).css("border-width","1px");
  for(i = 0; i <= subjects.length;i++){
    //preconditions
    if(subjects[i].code == clickedSubject){
        for(y= 0; y < subjects[i].prerequisites.length; y++){
          $("#"+subjects[i].prerequisites[y]).css("background-color","rgb(221,188,0)");
        }
    }
    for(y= 0; y < subjects[i].prerequisites.length; y++){
      //avaiable
      if(subjects[i].prerequisites[y] == clickedSubject){
        $("#"+subjects[i].code).css("background-color","rgb(153,75,0)");
      }
    }
  }
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
