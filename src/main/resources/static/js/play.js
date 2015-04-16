var START_X = 310;
var START_Y = 355;
var target_x = 0;
var target_y = 0;
var strokenum = 1;
var hole_x = 968;
var hole_y = 350;
var dest_X = hole_x;
var dest_Y = hole_y;
var linetoggleable = true;
var linemoveable = true;
var angle = 0;
/*var START_X = 900;
var START_Y = 375;
var dest_X = 500;
var dest_Y = 400;*/

var postParameters = {"startx":START_X, "starty":START_Y, "holex":hole_x, "holey":hole_y};
$.post("/playgame", postParameters, function(responseJSON){
    responseObject = JSON.parse(responseJSON);
    /*animate(myBall, canvas, context, startTime, true, responseObject.x, responseObject.y);*/
  });

window.requestAnimFrame = (function(callback) {
  return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
  function(callback) {
    window.setTimeout(callback, 1000 / 60);
  };
})();

function drawCircle(myBall, context) {
  //if (!outofbounds(myBall,context)) {
  context.beginPath();
  /*context.setLineDash([0]);
  context.lineWidth = 1;*/
  context.arc(myBall.x, myBall.y, myBall.radius, 0, 2 * Math.PI, false);  
  //console.log(myBall.x + " " + myBall.y)
  context.fillStyle = 'rgba(255, 255, 255, 1.0)';
  context.fill();        
  context.stroke();
//}
}
function animate(myBall, canvas, context, startTime, ascending, destX, destY) {
  // update
  var time = (new Date()).getTime() - startTime;

  //var linearSpeed = 50/ (Math.sqrt(Math.pow(destY-START_Y, 2) + Math.pow(destX-START_X, 2)));
  var linearSpeed = 200;
  var xspeed = 0;
  var yspeed = 0;
  /*var xspeed = linearSpeed * (destX - START_X);
  var yspeed = linearSpeed * (destY - START_Y);*/
  var xspeed = linearSpeed * Math.cos(angle*Math.PI / 180);
  var yspeed = -1* linearSpeed * Math.sin(angle * Math.PI / 180);
  // pixels / second
  var newX = START_X + xspeed * time / 1000;
  var newY = START_Y + yspeed * time / 1000;

  /*console.log(destX-newX);
  if(Math.abs(destX - newX) < 2) {
    myBall.x = newX;
  }
  if(Math.abs(newY - destY) < 2) {
    myBall.y = newY;
  }*/
  if(newX != destX) {
    myBall.x = newX;
  }
  if(newY != destY) {
    myBall.y = newY;
  }

  //bouncing
  if(myBall.radius < 10 && ascending) {
    myBall.radius += .05;         
  } else if (myBall.radius >= 5) {          
    ascending = false;
    myBall.radius = myBall.radius - .05;
  }

  // clear
  context.clearRect(0, 0, canvas.width, canvas.height);

  drawCircle(myBall, context);
  var disttohole = Math.round(Math.sqrt(Math.pow(myBall.x - hole_x,2) + Math.pow(myBall.y - hole_y,2)));
  document.getElementById("distancehud").innerHTML = "Distance to Hole: " + disttohole + " yards";
  //check if at destination
    //console.log(Math.abs(myBall.x - destX)/xspeed + " " + Math.abs(myBall.y - destY)/yspeed);
  if (outofbounds(myBall, canvas)) {
    myBall.x = START_X;
    myBall.y = START_Y;
    context.clearRect(0, 0, canvas.width, canvas.height);
    drawCircle(myBall, context);
  //} else if (Math.abs(myBall.x - destX) > 10 || Math.abs(myBall.y - destY) > 10) {
  } else if (Math.abs(myBall.x - destX)/xspeed > .05 || Math.abs(myBall.y - destY)/yspeed > .05) {
  // request new frame
    requestAnimFrame(function() {
      animate(myBall, canvas, context, startTime, ascending, destX, destY);
    });
  } else {
    myBall.x = destX;
    myBall.y = destY;
    context.clearRect(0, 0, canvas.width, canvas.height);
    drawCircle(myBall, context);
    START_X = destX;
    START_Y = destY;
  if (isgameover(myBall)) {
    console.log("win!");
    alert("win!");
  }
  }
}
var canvas = document.getElementById('myCanvas');
var context = canvas.getContext('2d');

var myBall = {
  x: START_X,
  y: START_Y,
  radius: 5,        
};

drawCircle(myBall, context);

function linedraw(evt) {
  if(linemoveable){
  context.clearRect(0, 0, canvas.width, canvas.height);
  //context.setLineDash([2,2]);
  //context.lineWidth = 3;
  context.moveTo(myBall.x, myBall.y);
  target_x = evt.pageX - canvas.offsetLeft;
  target_y = evt.pageY - canvas.offsetTop;
  context.lineTo(evt.pageX - canvas.offsetLeft, evt.pageY - canvas.offsetTop);
  context.stroke();
  drawCircle(myBall, context);


  var deltax = target_x - myBall.x;
  var deltay = myBall.y - target_y;
  //angle = Math.atan(deltay/deltax) *180 / Math.PI;
  angle = Math.atan2(deltay, deltax) *180 / Math.PI;
  }
}



/*function drawmyline(evt) {
  context.setLineDash([2,2]);
  context.moveTo(myBall.x, myBall.y);
  console.log(canvas.offsetTop);
  console.log(evt.pageX - canvas.offsetTop);
  context.lineTo(evt.pageX, evt.pageY);
  context.stroke();
}*/
//console.log(canvas.width + " " + canvas.height); 1280x720

// wait one second before starting animation
/*setTimeout(function() {
  var startTime = (new Date()).getTime();
  animate(myBall, canvas, context, startTime, true, dest_X, dest_Y);
}, 1000);*/
function toggleline() {
  if (linetoggleable) {
    if (linemoveable) {
      linemoveable = false;
    } else {
      linemoveable = true;
    }
  }
}

function toHole() {
  context.clearRect(0, 0, canvas.width, canvas.height);
  context.moveTo(myBall.x, myBall.y);
  context.lineTo(hole_x, hole_y);
  context.stroke();
  drawCircle(myBall, context);
  var deltax = hole_x - myBall.x;
  var deltay = myBall.y - hole_y;
  angle = Math.atan2(deltay, deltax) *180 / Math.PI;
}

function toggleable() {
  if (linetoggleable) {
    linetoggleable = false;
    linemoveable = false;
    toHole();
  } else {
    linetoggleable = true;
  }
}

function outofbounds(myBall, canvas) {
  return myBall.x < 0 || myBall.x > canvas.width || myBall.y < 0 || myBall.y > canvas.height;
}

function isgameover(myBall) {
  return Math.abs(myBall.x - hole_x) < 5 && Math.abs(myBall.y - hole_y) < 5
}

function isenter(evt) {
  if (evt.keyCode == 13) {
    swing();
  }
}

function swing() {
  var word = document.getElementById("tweetme").value;
  if (!linetoggleable) {
    toHole();
  }
  postParameters={"word":word, "angle":angle};
  //cheat
  if (word == "hole!") {
    var startTime = (new Date()).getTime();
    animate(myBall, canvas, context, startTime, true, hole_x, hole_y);
  } else if (word == "10"){
    var startTime = (new Date()).getTime();
    animate(myBall, canvas, context, startTime, true, myBall.x + 10*Math.cos(angle*Math.PI / 180), myBall.y - 10*Math.sin(angle*Math.PI / 180));
  } else if (word == "100"){
    var startTime = (new Date()).getTime();
    animate(myBall, canvas, context, startTime, true, myBall.x + 100*Math.cos(angle*Math.PI / 180), myBall.y - 100*Math.sin(angle*Math.PI / 180));
  } else {

  $.post("/swing", postParameters, function(responseJSON){
    responseObject = JSON.parse(responseJSON);
    var startTime = (new Date()).getTime();
    if(responseObject[1]) {
      //outofbounds
      animate(myBall, canvas, context, startTime, true, (target_x- myBall.x) * 5, (target_y - myBall.y) * 5);
    } else {
      animate(myBall, canvas, context, startTime, true, responseObject[0].x, responseObject[0].y);
    }
  });
}
  strokenum += 1;
  document.getElementById("strokehud").innerHTML = "Stroke#: " + strokenum;
  //var disttohole = Math.round(Math.sqrt(Math.pow(myBall.x - hole_x,2) + Math.pow(myBall.y - hole_y,2)));
  //document.getElementById("distancehud").innerHTML = "Distance to Hole: " + disttohole + " yards";
}
