var MAX_NAME_LENGTH = 15;
/**
 * Redirects to the start page
 */
function back() {
	window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/start";
}

/**
 *  Host popup.  Prompts for room and name.
 */
function host() {
	var room = prompt("Room name");
	if (room != null) {
		if (room.length > MAX_NAME_LENGTH) {
			alert("Please input a room name less than or equal to "+ MAX_NAME_LENGTH + " characters.");
			host();
		} else {
			var player = prompt("Your name");

			if (player == null && room.length > MAX_NAME_LENGTH) {
				alert("Please input a name less than or equal to "+ MAX_NAME_LENGTH + " characters.");
				host();
			} else {
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
		
	}
}

/**
 * Join popup.  Prompts for room and name to join a game.
 */
function join() {
	var room = prompt("Room name");

	if (room != null) {
		var player = prompt("Your name");

		if (player != null) {
			if (player.length > MAX_NAME_LENGTH) {
				alert("Please input a name less than or equal to "+ MAX_NAME_LENGTH + " characters.");
				join();
			} else {
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
}

/**
 * When the host clicks start game, send request to backend to see if all players are ready yet.
 */
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

/**
 * All non-host players click readyToPlay before game begins.
 */
function readyToPlay() {
	var playButton = document.getElementById("lobbybutton");
	playButton.disabled = true;
	playButton.className = "load-button myButtonGrey zoom-in";  
	playButton.setAttribute( 'data-loading', '' ); 

	var postParameters = {};
	$.post("/ready", postParameters, function(responseJSON) {
		window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/play";
	});

}

/**
 * Checks who is in the game every seconds.
 */
function checkPlayers() {
	setInterval(function() {
		var postParameters = {};
		$.post("/joinedPlayers", postParameters, function(responseJSON) {
			var players = JSON.parse(responseJSON).players;
			var displayPlayers = "";
			for (var i = 0; i < players.length; i++) {
				var myPlayer = players[i];
				if (i == 0) {
					displayPlayers = displayPlayers + "<font color =\"green\">host: " + myPlayer.name.toLowerCase() + "</font><br>";
				} else if (myPlayer.ready) {
					displayPlayers = displayPlayers + "<font color =\"green\">" + myPlayer.name.toLowerCase() + " - ready</font><br>";
				} else {
					displayPlayers  = displayPlayers  + "<font color =\"red\">" +  myPlayer.name.toLowerCase() + " - not ready</font><br>";
				}
			}
			document.getElementById("players").innerHTML = displayPlayers;
		});
		
	}, 1000);
}

/**
 * Any time a player exits the host/join pages, alerts the backend to drop the player from the game.
 */
function exit() {
	var postParameters = {};
	$.post("/exit", postParameters, function(responseJSON){
		window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/start";
	});
}
