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
var balls = {}; // for multiplayer
var players = {};
//var ball;
var id; // stored as a string

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
			createBall(colors[i], i.toString())
		}
	}
	/*animate(ball, canvas, context, startTime, true, responseObject.x, responseObject.y);*/
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

	//ball = newBall; // will get rid of this later
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

function moveBall(ball, dest_X, dest_Y, terrain) {
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
										} else {
											var disttohole = Math.round(Math.sqrt(Math.pow(ball.x - hole_x,2) + Math.pow(ball.y - hole_y,2)));
											document.getElementById("distancehud").innerHTML = "distance to hole: " + disttohole + " yards";
										}
										if (isgameover(ball)) {              
											rollIn(ball);
										}

									}
								});
							}
						});
					}
				}
			});
		}
	});
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
		toHole();
	}
	//Terrain
	var myPlayer = players[id];
	var terrainpic = document.getElementById("terrainpic");
	if (myPlayer.terrain == "BUNKER") {
		terrainpic.setAttribute("class", "terrain_bunker");
		terrainpic.innerHTML = "your ball is in<br> the bunker";
	} else if (myPlayer.terrain == "FAIRWAY") {
		terrainpic.setAttribute("class", "terrain_fairway");
		terrainpic.innerHTML = "your ball is on<br> the fairway";
	} else if (myPlayer.terrain == "ROUGH") {
		terrainpic.setAttribute("class", "terrain_rough");
		terrainpic.innerHTML = "your ball is in<br> the rough";
	} else if (myPlayer.terrain == "GREEN") {
		terrainpic.setAttribute("class", "terrain_green");
		terrainpic.innerHTML = "your ball is on<br> the green";
	} else if (myPlayer.terrain == "TEE") {
		terrainpic.setAttribute("class", "terrain_tee");
		terrainpic.innerHTML = "your ball is in<br> the teebox";
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

function rollIn(ball) {
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
					alert("Congratulations!  You've won in " + strokenum + " strokes!");
					window.location.href = "http://" + window.location.hostname + ":" + window.location.port + "/start";
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
	if(linemoveable){  
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
			newX *= -1
			;    }
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
	if (evt.keyCode == 13) {
		swing();
	}
}

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
		moveBall(ball, ball.x + num*Math.cos(angle*Math.PI / 180), ball.y - num*Math.sin(angle*Math.PI / 180));
	} else if (word == "hole!") {    
		moveBall(ball, hole_x, hole_y);
	} else {
		if (usedWords[word] == 1) {
			alert("Word already used!");
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
				//			setTimeout(function() {
								if (newPlayers[i].isGameOver) {
									moveBall(balls[i], hole_x, hole_y);
								} else if (newPlayers[i].outOfBounds) {
									moveBall(balls[i], (target_x - balls[i].x) * 5, (target_y - balls[i].y) * 5);
								} else {
									moveBall(balls[i], newPlayers[i].x, newPlayers[i].y, newPlayers[i].terrain);  
								}
					//		}, 1000);
						}
					}
					players = newPlayers;

					// my player
					//setTimeout(function() {
						if (myPlayer.outOfBounds) {
							console.log("got out of bounds");
							moveBall(balls[id], (target_x - balls[id].x) * 5, (target_y - balls[id].y) * 5);
							addStroke(2);
						} else if (myPlayer.isGameOver) {
							moveBall(balls[id], hole_x, hole_y);
							addStroke(1);
						} else {
							console.log(myPlayer.x);
							console.log(myPlayer.y);
							console.log(myPlayer.terrain);
							console.log(balls[id]);
							moveBall(balls[id], myPlayer.x, myPlayer.y, myPlayer.terrain);      
							addStroke(1);
						}
					//}, 2000);
				}
			});


			//for single player:

//			$.post("/swing", postParameters, function(responseJSON) {    
//			var responseObject = JSON.parse(responseJSON);
//			var outOfBounds = responseObject.outOfBounds; 
//			myPlayer = responseObject.myPlayer;    
//			gameOver = responseObject.gameOver;
//			if (outOfBounds) {
//			addStroke(2);
//			moveBall(ball, (target_x - ball.x) * 5, (target_y - ball.y) * 5);
//			} else if (gameOver) {
//			moveBall(ball, hole_x, hole_y);
//			} else {            
//			moveBall(ball, myPlayer.x, myPlayer.y, myPlayer.terrain);      
//			}
//			});


		}
	}
}