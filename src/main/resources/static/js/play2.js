var START_X = 310;
var START_Y = 355;
var target_x = 0;
var target_y = 0;
var strokenum = 1;
var hole_x = 971;
var hole_y = 350;
var dest_X = hole_x;
var dest_Y = hole_y;
var linetoggleable = true;
var linemoveable = true;
var angle = 0;
var gameOver;
var curLine;
var maxRad = 12;
var maxBounce = 2;
var usedWords = {};
var swingButton = document.getElementById("swingButton");
swingButton.disabled = false;
var wastoggleable = false;
var balls = {};
var players = {}; // keys are string ids
var id; // this is a string
var distance = 0;
var disttohole;
var playerId;

var postParameters = { };
$.post("/setup", postParameters, function(responseJSON){
	responseObject = JSON.parse(responseJSON);
	id = responseObject.id;
	players = responseObject.players;

	if (players.length == 1) {
		createBall(responseObject.color, id);
	} else {
		var colors = ["red", "blue", "green", "yellow"];
		for (var i = 0; i < players.length; i++) {
			console.log("drawing player " + i + "'s ball!");
			if (id != i.toString()) {
				createBall(colors[i], i.toString());
			}
		}

		// draws your ball on top of everyone else's ball
		createBall(colors[parseInt(id)], id);
	}
	disttohole = calcDistToHole(balls[id]);
});

function createBall(color, id) {
	var ballcolor = "#fff";
	switch(color) {
	case "white": ballcolor = "#fff";
	break;

	case "red" : ballcolor = "#f00";
	break;

	case "blue" : ballcolor = "#00f";
	break;

	case "green" : ballcolor = "#0f0";
	break;

	case "yellow" : ballcolor = "#ff0";
	break;

	default: ballcolor = "#fff";
	}

	var newBall = canvas.display.ellipse({
		x: START_X,
		y: START_Y,
		radius: 5,
		fill: ballcolor
	}).add();

	balls[id] = newBall;
}

var canvas = oCanvas.create({ canvas: "#myCanvas"});

var image = canvas.display.image({
	x: canvas.width/2,
	y: canvas.height/2,
	origin: { x: "center", y: "center" },
	image: "js/gui_hole1.png"
}).add();



function magnitude(x, y) {
	return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))
}

function moveBall(ball, dest_X, dest_Y, terrain, playerId) {
	var preX = ball.x;
	var preY = ball.y;
	var deltaX = dest_X - ball.x;
	var deltaY = dest_Y - ball.y;
	var mag = magnitude(deltaX, deltaY);
	var scale = Math.min(maxRad, mag * .05);
	var bounce = Math.min(maxBounce, mag * .01);
	var airX;
	var airY;
	if (terrain === "WATER") {
		airX = deltaX;
		airY = deltaY;
	} else {
		airX = deltaX * .45;
		airY = deltaY * .45;
	}
	ball.animate({
		x: ball.x + airX,
		y: ball.y + airY,
		radius: ball.radius + scale
	}, {
		duration: "normal",
		easing: "linear",
		callback: function () {
			ball.animate({
				x: ball.x + airX,
				y: ball.y + airY,
				radius: ball.radius - scale
			}, {
				duration: "normal",
				easing: "linear",
				callback: function () {        
					if (terrain === "WATER") {
						sink(ball, preX, preY);
            messagepopup("your ball is sleeping with the fishes!");
					} else {
						ball.animate({
							x: ball.x + deltaX * .05,
							y: ball.y + deltaY * .05,
							radius: ball.radius + bounce      
						}, {
							duration: "short",
							easing: "linear",
							callback: function () {
								ball.animate({
									x: ball.x + deltaX * .05,
									y: ball.y + deltaY * .05,
									radius: ball.radius - bounce
								}, {
									duration: "normal",
									easing: "linear",
									callback: function () { 
										enableSwingButton();
										if (outofbounds(ball, canvas)) {
											ball.x = preX;
											ball.y = preY;
                      messagepopup("that went way too far!"); 
										} else {
											disttohole = calcDistToHole(ball);
											document.getElementById("distancehud").innerHTML = "distance to hole: " + disttohole + " yards";
										}
										if (isgameover(ball)) {
											console.log(playerId + " in moveBall");
											rollIn(ball, playerId);
										}

									}
								});
							}
						});
					}
					if (distance == -14) {
						messagepopup("ball went too far!");
					} //else if (disttohole != 0) {
					//	messagepopup("good job!");
					//}
				}
			});
		}
	});
}

function calcDistToHole(ball) {
	return Math.round(Math.sqrt(Math.pow(ball.x - hole_x,2) + Math.pow(ball.y - hole_y,2)));
}
function addStroke(num) {
	strokenum += num;
	document.getElementById("strokehud").innerHTML = "stroke#: " + strokenum;
}

function enableSwingButton() {
	swingButton.className = "load-button myButton zoom-in";  
	swingButton.removeAttribute( 'data-loading'); 
	swingButton.disabled = false;
	linetoggleable = wastoggleable;
	if (linetoggleable) {
		linemoveable = true;
	} else {
		toHole(balls[id]);
	}
	//Terrain
	var myPlayer = players[id];
	var terrainpic = document.getElementById("terrainpic");
  var oldterrain = terrainpic.className;
  random = Math.floor((Math.random() * 3) + 1);
	if (myPlayer.terrain == "BUNKER") {
		terrainpic.setAttribute("class", "terrain_bunker");
		terrainpic.innerHTML = "your ball is in<br> the bunker";
    if (oldterrain != "terrain_bunker") {
      switch (random) {
        case 1: messagepopup("fun in the sand");
        break;
        case 2: messagepopup("bunker down for some chipping");
        break;
        case 3: messagepopup("not where you want to be");
        break;
      }
    }
	} else if (myPlayer.terrain == "FAIRWAY") {
		terrainpic.setAttribute("class", "terrain_fairway");
		terrainpic.innerHTML = "your ball is on<br> the fairway";
    if (oldterrain != "terrain_fairway") {
      switch (random) {
        case 1: messagepopup("nice shot!");
        break;
        case 2: messagepopup("nice one!");
        break;
        case 3: messagepopup("great shot!");
        break;
      }
    }
	} else if (myPlayer.terrain == "ROUGH") {
		terrainpic.setAttribute("class", "terrain_rough");
		terrainpic.innerHTML = "your ball is in<br> the rough";
    if (oldterrain != "terrain_rough") {
      switch (random) {
        case 1: messagepopup("you're going to have a rough time");
        break;
        case 2: messagepopup("this shot's going to be tricky");
        break;
        case 3: messagepopup("not your best shot");
        break;
      }
    }
	} else if (myPlayer.terrain == "GREEN") {
		terrainpic.setAttribute("class", "terrain_green");
		terrainpic.innerHTML = "your ball is on<br> the green";
    console.log(disttohole);
    if (oldterrain != "terrain_green" && disttohole > 10) {
      switch (random) {
        case 1: messagepopup("it's all putting from here!");
        break;
        case 2: messagepopup("nice setup!");
        break;
        case 3: messagepopup("just tap it in happy");
        break;
      }
    }
	} else if (myPlayer.terrain == "TEE") {
		terrainpic.setAttribute("class", "terrain_tee");
		terrainpic.innerHTML = "your ball is in<br> the teebox";
    if (oldterrain != "terrain_tee") {
      switch (random) {
        case 1: messagepopup("how'd you get back here?");
        break;
        case 2: messagepopup("did you not hit it very far?");
        break;
        case 3: messagepopup("a little closer to the hole next time");
        break;
      }
    }
	}
	//end terrain
}

function disableSwingButton() {
	swingButton.className = "load-button myButtonGrey zoom-in";  
	swingButton.setAttribute( 'data-loading', '' ); 
	swingButton.disabled = true;
	wastoggleable = linetoggleable;
	linetoggleable = false;
}

function rollIn(ball, playerId) {
	ball.animate({
		x: hole_x,
		y: hole_y
	}, {
		duration: "short",
		easing: "linear",
		callback: function () { 
			ball.animate({
				radius: 0
			}, {
				duration: "normal",
				easing: "linear",
				callback: function () {
					console.log(playerId);
					console.log(id);
					if (playerId == id) {
						alert("Congratulations, " + players[id].name + "! You finished in " + strokenum + " strokes!");
						window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/start";
					} else {
						alert(players[playerId].name + " finished in " + players[playerId].stroke + " strokes!");
					}
				}
			});
		}
	});
}

function sink(ball, x, y) {
	ball.animate({
		radius: 0
	}, {
		duration: "normal",
		easing: "linear",
		callback: function () {      
			ball.x = x;
			ball.y = y;
			ball.radius = 5;
			enableSwingButton(); 
		}
	});
}

function linedraw(evt) {
	var ball = balls[id];

	if (linemoveable && typeof ball != 'undefined') {  
		var mouseX = canvas.mouse.x;
		var mouseY = canvas.mouse.y;
		var oldcanvas = document.getElementById("myCanvas");
		if (mouseX == 0) {
			mouseX = evt.pageX - oldcanvas.offsetLeft;
		}
		if (mouseY == 0) {
			mouseY = evt.pageY - oldcanvas.offsetTop;
		}
		var slope = (mouseY - ball.y) / (mouseX - ball.x);
		var b = mouseY - slope * mouseX;
		var newX = 3000;
		if (mouseX < ball.x) {
			newX *= -1;
		}
		var newY = newX*slope + b;    
		if (curLine != null) {
			curLine.remove();
		} 
		curLine = canvas.display.line({
			start: { x: ball.x, y: ball.y },
			end: { x: newX, y: newY },
			stroke: "1px #000000",
			cap: "round"
		}).add();        
		target_x = canvas.mouse.x;
		target_y = canvas.mouse.y;
		var deltax = mouseX - ball.x;
		var deltay = ball.y - mouseY;
		angle = Math.atan2(deltay, deltax) * 180 / Math.PI;
	}
}

function toggleline() {
	if (linetoggleable) {
		if (linemoveable) {
			linemoveable = false;
		} else {
			linemoveable = true;
		}
	}
}

function toHole(ball) {
	if (curLine != null) {
		curLine.remove();
	} 
	curLine = canvas.display.line({
		start: { x: ball.x, y: ball.y },
		end: { x: hole_x, y: hole_y },
		stroke: "2px #0aa",
		cap: "round"
	}).add();
	var deltax = hole_x - ball.x;
	var deltay = ball.y - hole_y;
	angle = Math.atan2(deltay, deltax) *180 / Math.PI;
}

function toggleable() {
	var ball = balls[id];
	if (linetoggleable) {
		linetoggleable = false;
		linemoveable = false;
		toHole(ball);
	} else {
		linetoggleable = true;
	}
}

function outofbounds(ball, canvas) {
	return ball.x < 0 || ball.x > canvas.width || ball.y < 0 || ball.y > canvas.height;
}

function isgameover(ball) {
	return Math.abs(ball.x - hole_x) < 5 && Math.abs(ball.y - hole_y) < 5;
	//return gameOver;
}

function isenter(evt) {
	//TODO: CHANGE ME TO ACCOUNT FOR NOT ABLE TO SWING
	if (evt.keyCode == 13) {
		swing();
	}
}


function messagepopup(message){
	messagediv = document.getElementById("mymessage");
	messagediv.innerHTML = message;
	messagediv.style.visibility = "visible";
	messagediv.style.opacity = 1;
	setTimeout(function(){
		messageinterval = setInterval(function(){ 
			if (messagediv.style.opacity > 0) {
				messagediv.style.opacity -= 0.01;
			} else {
				clearInterval(messageinterval);
				messagediv.style.visibility = "hidden";
			}
		}, 10);
	},2000);

}
messagepopup("lets play!");

function swing() { 
	var word = document.getElementById("tweetme").value.toLowerCase();
	if (!linetoggleable) {
		toHole(balls[id]);
	}  
	disableSwingButton();
	postParameters={"word":word, "angle":angle};
	curLine.remove();
	//cheats
	if(!isNaN(+word)) {
		var num = +word;    
		moveBall(balls[id], 
				balls[id].x + num*Math.cos(angle*Math.PI / 180), 
				balls[id].y - num*Math.sin(angle*Math.PI / 180),
				"TEE", id);
	} else if (word == "hole!") {    
		moveBall(balls[id], hole_x, hole_y,"TEE", id);
	} else {
		if (usedWords[word] == 1) {
			messagepopup("word already used!");
			enableSwingButton();
		} else {
			usedWords[word]  = 1;

			// multiplayer: 
			$.post("/swing", postParameters, function(responseJSON) {
				
				var responseObject = JSON.parse(responseJSON);
				var newPlayers = responseObject.players;
				var myPlayer = newPlayers[parseInt(id)];
								
				// all other players go first
				for (var i = 0; i < players.length; i++) {
					if(i.toString() != id) {
						var otherPlayerOld = players[i.toString()];
						var otherPlayerNew = newPlayers[i];
						if (!otherPlayerOld.isGameOver) {
							//	setTimeout(function() {
							if (otherPlayerNew.isGameOver) {
								moveBall(balls[i], hole_x, hole_y,otherPlayerNew.terrain, otherPlayerNew.id);
							} else if (otherPlayerNew.outOfBounds || distance == -14) {
								moveBall(balls[i], (balls[i].x + 1000*Math.cos(angle*Math.PI / 180)), 
										balls[i].y + 1000*Math.sin(angle*Math.PI / 180),
										otherPlayerNew.terrain, otherPlayerNew.id);
							} else {
								moveBall(balls[i], otherPlayerNew.x, otherPlayerNew.y, otherPlayerNew.terrain, otherPlayerNew.id);  
							}
							//}, 1000);
						}
					}
					//note: players keys = String, newPlayers keys = numerical
					players = newPlayers;
				}
				
				// my player
				//setTimeout(function() {
				if (myPlayer.outOfBounds) {
					moveBall(balls[id],
							(balls[id].x + 1000*Math.cos(angle*Math.PI / 180)),
							balls[id].y - 1000*Math.sin(angle*Math.PI / 180),
							myPlayer.terrain,
							id);
					addStroke(2);
				} else if (myPlayer.isGameOver) {
					moveBall(balls[id], hole_x, hole_y, myPlayer.terrain, id);
					addStroke(1);
				} else {
					moveBall(balls[id], myPlayer.x, myPlayer.y, myPlayer.terrain, id);      
					addStroke(1);
				}
				//}, 2000);
			});

		}
	}
}