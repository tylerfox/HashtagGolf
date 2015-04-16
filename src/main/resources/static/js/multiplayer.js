function host() {
  var room = prompt("Room name");
  if (room != null) {
    var player = prompt("Your name");

    postParameters = {
      "room" : room,
      "player" : player
    };

    $.post("/host", postParameters, function(responseJSON) {
      var valid = JSON.parse(responseJSON).success;

      if (valid) {
        window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/lobby/" + room;
      } else {
        alert("Room name has already been taken. Please enter a new room name.");
        host();
      }
    });
  }

}



function join() {
  var room = prompt("Room name");
  if (room != null) {
    var player = prompt("Your name");

    if (player != null) {
      var userId = 0;

      postParameters = {
        "room" : room,
        "player" : player
      };

      $.post("/join", postParameters, function(responseJSON) {
        var roomExists = JSON.parse(responseJSON).roomExists;
        var roomFull = JSON.parse(responseJSON).roomFull;
        if (roomExists && !roomFull) {
          window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/lobby/" + room;
        } else if (!roomExists) {
          alert("No room by this name exists. Please enter a new room name.");
          join();
        } else if (roomFull) {
          alert("This room is full. Please enter a new room name.");
          join();
        }
      });
    }
  }
}
