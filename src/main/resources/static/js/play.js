var START_X = 380;
var START_Y = 430;
var dest_X = 200;
var dest_Y = 375;
var target_x = 0;
var target_y = 0;
var strokenum = 1;
var hole_x = 900;
var hole_y = 375;
var linemoveable = true;
var angle = 0;
/*var START_X = 900;
var START_Y = 375;
var dest_X = 500;
var dest_Y = 400;*/

window.requestAnimFrame = (function(callback) {
  return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
  function(callback) {
    window.setTimeout(callback, 1000 / 60);
  };
})();

function drawCircle(myBall, context) {
  context.beginPath();
  context.setLineDash([0]);
  context.lineWidth = 1;
  context.arc(myBall.x, myBall.y, myBall.radius, 0, 2 * Math.PI, false);  
  context.fillStyle = 'rgba(255, 255, 255, 1.0)';
  context.fill();        
  context.stroke();
}
function animate(myBall, canvas, context, startTime, ascending, destX, destY) {
  // update
  var time = (new Date()).getTime() - startTime;

  //var linearSpeed = 50/ (Math.sqrt(Math.pow(destY-START_Y, 2) + Math.pow(destX-START_X, 2)));
  var linearSpeed = 1.5;
  var xspeed = 0;
  var yspeed = 0;
  var xspeed = linearSpeed * (destX - START_X);
  var yspeed = linearSpeed * (destY - START_Y);
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
  //check if at destination
  if (Math.abs(myBall.x - destX) > 10 || Math.abs(myBall.y - destY) > 10) {
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
  angle = Math.atan(deltay/deltax) *180 / Math.PI;
  }
}

function toggleline() {
  if (linemoveable) {
    linemoveable = false;
  } else {
    linemoveable = true;
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

function swing() {
  var word = document.getElementById("tweetme").value;

  postParameters={"word":word, "angle":angle};
  $.post("/swing", postParameters, function(responseJSON){
    responseObject = JSON.parse(responseJSON);
    var startTime = (new Date()).getTime();
    animate(myBall, canvas, context, startTime, true, responseObject.x, responseObject.y);
  });
  strokenum += 1;
  document.getElementById("strokehud").innerHTML = "Stroke#: " + strokenum;
  var disttohole = Math.round(Math.sqrt(Math.pow(myBall.x - hole_x,2) + Math.pow(myBall.y - hole_y,2)));
  console.log(disttohole);
  document.getElementById("distancehud").innerHTML = "Distance to Hole: " + disttohole + " yards";
}
