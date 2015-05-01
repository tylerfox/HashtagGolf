/*
 * Redirects to the start page
 */
function back() {
  window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/start";
}

function host() {
  var room = prompt("Room name");
  if (room != null) {
    var player = prompt("Your name");

    var postParameters = {
      "room" : room,
      "player" : player
    };

    $.post("/host", postParameters, function(responseJSON) {
      var valid = JSON.parse(responseJSON).success;

      if (valid) {
        window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/multi_levelselect";
        //window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/hostlobby/" + room;
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

      var postParameters = {
        "room" : room,
        "player" : player
      };

      $.post("/join", postParameters, function(responseJSON) {
        var roomExists = JSON.parse(responseJSON).roomExists;
        var roomFull = JSON.parse(responseJSON).roomFull;
        var duplicateIp = JSON.parse(responseJSON).duplicateIp;
        
        if (roomExists && !roomFull && !duplicateIp) {
         //brings user to the lobby
          window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/lobby/" + room;
        } else if (!roomExists) {
          alert("No room by this name exists. Please enter a new room name.");
          join();
        } else if (roomFull) {
          alert("This room is full. Please enter a new room name.");
          join();
        } else if (duplicateIp) {
          alert("You are already participating in this game in another window.");
        }
      });
    }
  }
}

function startGame() {
	var postParameters = {};

	$.post("/hoststart", postParameters, function(responseJSON) {
	    var startGame = JSON.parse(responseJSON).startGame;
	    console.log(startGame);
	    if (startGame){
	      window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/play";
	    } else {
	      alert("Not all players are ready yet. Please tell all players to click the ready button.");
	    }
	});
}

function readyToPlay() {
  var postParameters = {};
  $.post("/ready", postParameters, function(responseJSON) {
     window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/play";
  });

}

function exit() {
	var postParameters = {};
	$.post("/exit", postParameters, function(responseJSON){
		 window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/start";
	});
}















