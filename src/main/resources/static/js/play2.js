
var START_X;
var START_Y;
var target_x;
var target_y;
var strokenum = 1;
var hole_x;
var hole_y;
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
var gameover = false;
var displayPlayerDeparture = [true, true, true, true];
var endPlayers = {};
var par = 3;
var entireGameOver = false;
var myguihole = "";
var canvas;
var image;
var qtipHidden;
var qtipContent = "balls";
var canenter = true;
var colors = ["red", "blue", "green", "yellow"];
var fullScreenPopup;
var fullScreenModal;
var allPlayersSwung = false;

window.onbeforeunload = confirmExit;

function confirmExit(e) {  
  var e = e || window.event;

  if (e && !gameover) {   
    var postParameters = {};
    $.post("/exit", postParameters, function(responseJSON) {
    	window.location.href = "/start";
    });
  }
}

function checkRefresh() {
    console.log("On load detected");
    console.log(document.getElementById("refreshField").value);
    
    if (document.getElementById("refreshField").value == "0") { //fresh page load
        console.log("Fresh page load");
        document.getElementById("refreshField").value = "1";
        console.log("refreshField has been changed to " + document.getElementById("refreshField").value);
	} else { //on refresh
		console.log("Refresh detected");
	    window.location.href = "/start";  
	}
}

function redirectOnRefresh(evt) {
  /**
  if (evt.keyCode == 116) {
  	alert("Redirecting to home page."); //Do NOT delete this alert. It is necessary for functionality.
    window.location.href = "/start";
  } */
}

$(document).bind("keydown", redirectOnRefresh);

function waitForOthers() {
  disableSwingButton();
  var postParameters={};
  $.post("/spectate", postParameters, function(responseJSON) {
    animateTurn(responseJSON);
  });
}

function displayFullscreenModal() {
  fullScreenPopup = $('#fullscreen-modal-content');
  fullscreenModal = fullScreenPopup.modal({                               
    containerId: 'fullscreen-modal-container'
  }); 
}

function fullScreen() {
  var element = document.head;
  // Supports most browsers and their versions.
  var requestMethod = element.requestFullScreen || element.webkitRequestFullScreen || element.mozRequestFullScreen || element.msRequestFullscreen;

  if (requestMethod) { // Native full screen.
    requestMethod.call(element);
  } else if (typeof window.ActiveXObject !== "undefined") { // Older IE.
    var wscript = new ActiveXObject("WScript.Shell");
    if (wscript !== null) {
      wscript.SendKeys("{F11}");
    }
  }
  fullScreenPopup.hide();
  $.modal.close();       
}

function closeFullScreenPopup() { 
  $.modal.close();
}

//displayScorecard();

function displayScorecard() {
  console.log("displaying score card");
  var playerInfo = "<h3>par: " + par + "</h3><br>";
  for (var i = 0; i < players.length; i++) {
    var name = endPlayers[i].name.toLowerCase();
    playerInfo = playerInfo + name + ": " + (endPlayers[i].stroke - 1) + "<br>";
  }
  document.getElementById("info").innerHTML = playerInfo;
  $('#basic-modal-content').modal();
}

function nextlevel() {
  singlepostParameters ={};
  $.get("/single_player_select", singlepostParameters, function(responseJSON) {
    colpostParameters={"color":"red"};
    console.log("c");
    $.get("/play", colpostParameters, function(responseJSON) {
      lvlpostParameters={"level":"2"};
      console.log("b");
      $.get("/level_select", lvlpostParameters, function(responseJSON) {
        console.log("a");
        document.location.href = "/play";
      });
    });
  });




  //js/gui_hole1.png
  //console.log(myguihole);
  //location.reload();
  //console.log("after");
  /*var postParameters = {};
  $.post("/setup", postParameters, function(responseJSON){
    console.log(responseObject);
    responseObject = JSON.parse(responseJSON);
    id = responseObject.id;
    players = responseObject.players;
    START_X = responseObject.startx;
    START_Y = responseObject.starty;
    hole_x = responseObject.holex;
    hole_y = responseObject.holey;
    par = responseObject.par;
    /*document.getElementById("myCanvas").style.backgroundImage = responseObject.guihole;*/
    /*document.getElementById("myCanvas").style.background = "white";*/
    /*myguihole = "js/" + responseObject.guihole;
    console.log(myguihole);
    loadcanvas();
    document.getElementById("parhud").innerHTML = "par#: " + par;
    if (players.length == 1) {
      createBall(responseObject.color, id, 2);
    } else {
      for (var i = 0; i < players.length; i++) {
        console.log("drawing player " + i + "'s ball!");
        if (id != i.toString()) {
          createBall(colors[i], i.toString(), 2);
        }
      }

      // draws your ball on top of everyone else's ball
      createBall(colors[parseInt(id)], id, "front");
    }

    disttohole = calcDistToHole(balls[id]);
    document.getElementById("distancehud").innerHTML = "distance to hole: " + disttohole + " yards";

    if (players.length > 1) {
      messagepopup("let's play! your ball color is " + colors[id]); 
    } else {
      messagepopup("let's play!");
    }

  });*/
}

var postParameters = {};
$.post("/setup", postParameters, function(responseJSON){
  responseObject = JSON.parse(responseJSON);
  id = responseObject.id;
  players = responseObject.players;
  START_X = responseObject.startx;
  START_Y = responseObject.starty;
  hole_x = responseObject.holex;
  hole_y = responseObject.holey;
  par = responseObject.par;
  /*document.getElementById("myCanvas").style.backgroundImage = responseObject.guihole;*/
  /*document.getElementById("myCanvas").style.background = "white";*/
  myguihole = "js/" + responseObject.guihole;
  console.log(id + " " + players + " " + START_X + " " + hole_x + " " + hole_y + " " + par);
  loadcanvas();
  document.getElementById("parhud").innerHTML = "par#: " + par;
  if (players.length == 1) {
    createBall(responseObject.color, id, 2);
  } else {
    for (var i = 0; i < players.length; i++) {
      console.log("drawing player " + i + "'s ball!");
      if (id != i.toString()) {
        createBall(colors[i], i.toString(), 2);
      }
    }

    // draws your ball on top of everyone else's ball
    createBall(colors[parseInt(id)], id, "front");

  }

  disttohole = calcDistToHole(balls[id]);
  document.getElementById("distancehud").innerHTML = "distance to hole: " + disttohole + " yards";

  //Ball Colors Hud
  ballcolorhud = document.getElementById("ballcolorhud");
  if (players.length !=1) {
    switch (balls[id].fill) {
        case "#fff": hudballcolor = "white";
        break;
        case "#f00" : hudballcolor = "red";
        break;
        case "#00f" : hudballcolor = "blue";
        break;
        case "#0f0" : hudballcolor = "green";
        break;
        case "#ff0" : hudballcolor = "yellow";
        break;
    }
    ballcolorhud.innerHTML = ballcolorhud.innerHTML + "<br>" +
        "<b>you: " + hudballcolor + "</b>";
    for (person in players) {
      var hudballcolor;
      switch(balls[person].fill) {
        case "#fff": hudballcolor = "white";
        break;
        case "#f00" : hudballcolor = "red";
        break;
        case "#00f" : hudballcolor = "blue";
        break;
        case "#0f0" : hudballcolor = "green";
        break;
        case "#ff0" : hudballcolor = "yellow";
        break;
        default: hudballcolor = "white";
      }
      ballcolorhud.style.visibility = "visible";
      if (person != id) {
        ballcolorhud.innerHTML = ballcolorhud.innerHTML + "<br>" +
        players[person].name.toLowerCase() + ": " + hudballcolor;
      }
    }
  }

  if (players.length > 1) {
    messagepopup("let's play! your ball color is " + colors[id]); 
  } else {
    messagepopup("let's play!");
  }

});

function createBall(color, id, z) {
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
    fill: ballcolor,
    zIndex: z
  }).add();

  balls[id] = newBall;
}

function loadcanvas() {
  canvas = oCanvas.create({ canvas: "#myCanvas"});

  image = canvas.display.image({
    x: canvas.width / 2,
    y: canvas.height / 2,
    origin: { x: "center", y: "center" },
    image: /*"js/"+*/myguihole
  }).add();

  $("#myCanvas").qtip({
    content: qtipContent,
    show: false,
    hide: false,
    position: {
      target: 'mouse'
    },
    style: {          
      classes: 'qtip-light qtip-rounded'
    }        


  })
  .bind('mousemove', function(evt) {    
    // Check the x and y coordinates and valid
    var nearby = false;

    for (var i in balls) {
      var someball = balls[i];    
      if(Math.abs(someball.x - evt.pageX) < 5  && Math.abs(someball.y - evt.pageY) < 5) {   
        nearby = true;
        if (players[1] == null) {
          $("#myCanvas").qtip('option', 'content.text', "<b>your ball</b><br>" + "distance to hole: "  + disttohole);
          $(this).qtip('show'); // Show the qTip
          qtipHidden = false;       
        } else {
          $("#myCanvas").qtip('option', 'content.text', "<b>" + players[i].name.toLowerCase() + "'s ball</b><br>" + "distance to hole: " + disttohole);
          $(this).qtip('show'); // Show the qTip
          qtipHidden = false;
        }
      }
    }
    if (!nearby && !qtipHidden) {
      $(this).qtip('hide'); 
      qtipHidden = true;
    }   
  })

  if (!(screen.width == window.outerWidth && screen.height == window.outerHeight)) {
    displayFullscreenModal();
  }

}


function magnitude(x, y) {
  return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))
}

function moveBall(ball, dest_X, dest_Y, player) { 
  var terrain = player.terrain;
  var playerId = player.id;
  var preX = ball.x;
  var preY = ball.y;
  var deltaX = dest_X - ball.x;
  var deltaY = dest_Y - ball.y;
  var mag = magnitude(deltaX, deltaY);
  var scale;
  var bounce; 
  if ((mag / 3.5) <= 25) {
    scale = 0;
    bounce = 0;
  } else {
    scale = Math.min(maxRad, mag * .05);
    bounce = Math.min(maxBounce, mag * .01);
  }
  var airX;
  var airY;
  if (terrain === "WATER") {    
    airX = deltaX * .5;
    airY = deltaY * .5;
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
            if (player.id == id) {
              messagepopup("your ball is sleeping with the fishes!");
              splash2(ball.x,ball.y);
            }
            sink(ball, preX, preY);
          } else if (terrain == "OUT_OF_BOUNDS") {
            outOfBounds(ball, preX, preY);
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
            /*if (outofbounds(ball, canvas)) {
                      ball.x = preX;
                      ball.y = preY;
                      if (playerId == id) {
                        messagepopup("that went way too far!"); 
                      }
                    } else {*/
                      if (playerId == id) {
                        disttohole = calcDistToHole(ball);
                        document.getElementById("distancehud").innerHTML = "distance to hole: " + disttohole + " yards";
                      }
            //}
            if (player.isGameOver) {                      
              rollIn(ball, playerId);
            }

          }
        });
              }
            });
}

if (distance == -14) {
  messagepopup("ball went too far!");
}
}
});
}
});
}

function calcDistToHole(ball) {
  return Math.round(Math.round(Math.sqrt(Math.pow(ball.x - hole_x,2) + Math.pow(ball.y - hole_y,2))) / 3.5);
}

function addStroke(num) {
  strokenum += num;
  document.getElementById("strokehud").innerHTML = "stroke#: " + strokenum;
}

function enableSwingButton() {
  if (players[id] != null && !players[id].isGameOver) {
    swingButton.className = "load-button myButton zoom-in";  
    swingButton.removeAttribute( 'data-loading'); 
    swingButton.disabled = false;
    canenter = true;
    document.getElementById("check").disabled = false;
    document.getElementById("tweetme").value = "";
    //Terrain
    var myPlayer = players[id];
    var terrainpic = document.getElementById("terrainpic");
    var oldterrain = terrainpic.className;
    random = Math.floor((Math.random() * 3) + 1);
    if (myPlayer.terrain == "BUNKER") {
      terrainpic.setAttribute("class", "terrain_bunker");
      terrainpic.innerHTML = "in the bunker <br> <img src='css/clock.png'> 30 seconds";
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
      terrainpic.innerHTML = "on the fairway <br> <img src='css/clock.png'> 60 seconds";
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
      terrainpic.innerHTML = "in the rough <br> <img src='css/clock.png'> 45 seconds";
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
      terrainpic.innerHTML = "on the green <br> <img src='css/clock.png'> 60 seconds";
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
      terrainpic.innerHTML = "in the teebox <br> <img src='css/clock.png'> 60 seconds";
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
  }
  //end terrain
  linetoggleable = wastoggleable;
  if (linetoggleable) {
    linemoveable = true;
  } else {
    toHole(balls[id]);
  }
}

function disableSwingButton() {
  swingButton.className = "load-button myButtonGrey zoom-in";  
  swingButton.setAttribute( 'data-loading', '' ); 
  swingButton.disabled = true;
  linemoveable = false;
  wastoggleable = linetoggleable;
  linetoggleable = false;
  canenter = false;
  document.getElementById("check").disabled = true;
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
        callback: function() {
          endPlayers[parseInt(playerId)] = players[playerId];

          if (playerId == id && !entireGameOver) {
            if (strokenum == 1) {
              messagepopup("congratulations, " + players[id].name.toLowerCase() + "! you got a hole-in-one!");
            } else if (!entireGameOver){
              messagepopup("congratulations, " + players[id].name.toLowerCase() + "! you finished in " + (players[playerId].stroke - 1) + " strokes!");
            }
          } else {
            if (players[playerId].stroke == 1 && !entireGameOver) {
              messagepopup(players[playerId].name.toLowerCase() + " got a hole-in-one!");
            } else if (!entireGameOver){
              messagepopup(players[playerId].name.toLowerCase() + " finished in " + (players[playerId].stroke - 1) + " strokes!");
            }
          }

          if (entireGameOver && allPlayersSwung) {
            displayScorecard();
          }

        }
      });
}
});
}

function outOfBounds(ball, x, y) {     
  ball.x = x;
  ball.y = y;
  ball.radius = 5;
  messagepopup("that went way too far!");
  enableSwingButton(); 
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
  ball = balls[id];

  if (!players[id].isGameOver && linemoveable && typeof ball != 'undefined') {  
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
}

function isenter(evt) {
  //TODO: CHANGE ME TO ACCOUNT FOR NOT ABLE TO SWING
  if (evt.keyCode == 13 && canenter) {
    swing();
  }
}

function messagepopup(message){
  messagediv = document.getElementById("mymessage");
  messagediv.innerHTML = message;
  messagediv.style.visibility = "visible";
  messagediv.style.opacity = 1;
  setTimeout(function() {
    messageinterval = setInterval(function(){ 
      if (messagediv.style.opacity > 0) {
        messagediv.style.opacity -= 0.01;
      } else {
        clearInterval(messageinterval);
        messagediv.style.visibility = "hidden";
      }
    }, 10);
  }, 2000);
}

function splash() {
  var waterdiv = document.getElementById("watereffect");
  waterdiv.style.visibility = "visible";
  waterdiv.style.opacity = 1;
  waterdiv.style.zIndex = 50;
  setTimeout(function() {
    waterinterval = setInterval(function(){ 
      if (waterdiv.style.opacity > 0) {
        waterdiv.style.opacity -= 0.01;
      } else {
        clearInterval(waterinterval);
        waterdiv.style.visibility = "hidden";
        waterdiv.style.zIndex = -50;
      }
    }, 10);
  }, 2000);
}

function splash2(x,y) {
  var waterdiv = document.getElementById("watereffect");
  waterdiv.style.visibility = "visible";
  waterdiv.style.opacity = 0.7;
  waterdiv.style.zIndex = 50;
  waterdiv.style.left = (x-50) + "px"
  waterdiv.style.top = (y-50) + "px"
  /*waterdiv.style.height = "50px";
  waterdiv.style.width = "50";
  waterdiv.style.left = "500px";*/
  //waterdiv.style.backgroundImage = "url(splash2.png)";

  setTimeout(function() {
    waterinterval = setInterval(function(){ 
      if (waterdiv.style.opacity > 0) {
        waterdiv.style.opacity -= 0.01;
      } else {
        clearInterval(waterinterval);
        waterdiv.style.visibility = "hidden";
        waterdiv.style.zIndex = -50;
      }
    }, 10);
  }, 500);
}

function swing() {
  var word = document.getElementById("tweetme").value.toLowerCase();
  if (word == "") {
    messagepopup("you didn't input a word!");
    return;
  }
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
      balls[id].x + num * Math.cos(angle * Math.PI / 180), 
      balls[id].y - num * Math.sin(angle * Math.PI / 180),
      players[id]);
  } else if (word == "hole!") {    
    moveBall(balls[id], hole_x, hole_y, players[id]);
  } else {
    var wordSplit = word.split(" ");
    if (usedWords[word] == 1) {
      messagepopup("word already used!");
      enableSwingButton();
    } else if(wordSplit[wordSplit.length - 1] === "-n") {
      $.post("/swing", postParameters, function(responseJSON) {
        animateTurn(responseJSON);
      });
    } else {
      usedWords[word]  = 1;
      $.post("/swing", postParameters, function(responseJSON) {
        animateTurn(responseJSON);
      });
    }
  }
}


function animateTurn(responseJSON) {
  var responseObject = JSON.parse(responseJSON);
  var newPlayers = responseObject.players;
  var myPlayer = newPlayers[parseInt(id)];
  entireGameOver = responseObject.entireGameOver;
  var oldPlayers = players;   
  animateBalls(0);

  function animateBalls(i) {
    var timeDelay = 1;

    if (i > 0) {
      timeDelay = 4000;
    }

    if (i < players.length) {
      var otherPlayerOld = oldPlayers[i.toString()];
      var otherPlayerNew = newPlayers[i];
      
      if (otherPlayerOld != null && !otherPlayerOld.isGameOver && otherPlayerNew != null) {
        setTimeout(function() {
          console.log("inside the settimeout function!!");
          if (otherPlayerNew.isGameOver) {
            moveBall(balls[i], hole_x, hole_y, otherPlayerNew);
          } else if (otherPlayerNew.outOfBounds || distance == -14) {
            if (otherPlayerNew.id == id) {
              addStroke(2);
            }             
            moveBall(balls[i], balls[i].x + 1000 * Math.cos(angle * Math.PI / 180), 
              balls[i].y - 1000 * Math.sin(angle * Math.PI / 180),
              otherPlayerNew);

          } else {
            if (otherPlayerNew.id == id) {
              addStroke(1);
            }
            moveBall(balls[i], otherPlayerNew.x, otherPlayerNew.y, otherPlayerNew);  
          }
          players[i] = newPlayers[i];
          animateBalls(i + 1);
        }, timeDelay);
      } else {
        players[i] = newPlayers[i];
        animateBalls(i + 1);
      }
    } else { // after all players have gone:
      allPlayersSwung = true;

      if (myPlayer != null && myPlayer.isGameOver) {
        waitForOthers();
      } 
    } 
  }
}
