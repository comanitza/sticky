// Make the DIV element draggable:

function dragElement(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  if (document.getElementById(elmnt.id + "header")) {
    // if present, the header is where you move the DIV from:
    document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
  } else {
    // otherwise, move the DIV from anywhere inside the DIV:
    elmnt.onmousedown = dragMouseDown;
  }

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;

//    document.mousemoveend = alert('end!');
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
  }

  function closeDragElement() {
    // stop moving when mouse button is released:
    document.onmouseup = null;
    document.onmousemove = null;
  }
}

function isStickyContentValid(stickyContent) {

    if (stickyContent.length == 0) {
        return false;
    }

    return true;
}

function createSticky() {

    var stickyContent = $("#addstickycontent").val();

    //todo do some checks

    if (!isStickyContentValid(stickyContent)) {

        alert("content not valid");

        return
    }

    $.ajax({
        url: "/rest/createsticky",
        type: "POST",
        dataType: "json",
        data: '{"content":"'+ stickyContent +'","category":"default"}',
        contentType: "application/json",
                success: function(data, textStatus, jqXHR)
                {
                      var stickyId = "sticky" + data;

                      var txt2 = $('<div class="stickydiv" id="' + stickyId + '"><div class="stickyheader">Click here to move <a href="javascript:deleteSticky(' + data + ');">[X]</a></div><div>' + stickyContent + '</div></div>');
                      $("body").append(txt2);
                      dragElement(document.getElementById(stickyId));

                },
                error: function (jqXHR, textStatus, errorThrown)
                {

                }
    });
}

function deleteSticky(sId) {

    var confirmAction = confirm("Are you sure to execute this action?");

    if (!confirmAction) {
        return;
    }

    $.ajax({
      url: "/rest/deletestickies",
      type: "get",
      data: {
        stickyId: sId
      },
      success: function(response) {
        var stickyId = "sticky" + sId;

        $('#' + stickyId).hide("slow", function(){ $(this).remove(); })

      },
      error: function(xhr) {
        alert("could not delete sticky, please try again or refresh");
      }
    });
}

function moveSticky(sId) {

    alert('move sticky ' + sId);

    var stickyId = 'sticky' + sId;

    var el = document.getElementById(stickyId)

    var newPosX = el.style.left.replace("px", "");
    var newPosY = el.style.top.replace("px", "");

        $.ajax({
          url: "/rest/movesticky",
          type: "get",
          data: {
            stickyId: sId,
            posX: newPosX,
            posY: newPosY
          },success: function(response) {
//                   var stickyId = "sticky" + sId;
//
//                   $('#' + stickyId).hide("slow", function(){ $(this).remove(); })

                 },
          error: function(xhr) {
            alert("could not move sticky, please try again or refresh");
          }
        });
}
