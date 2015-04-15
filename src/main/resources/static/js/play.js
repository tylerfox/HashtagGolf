var START_X = 328;
var START_Y = 375;
var dest_X = 500;
var dest_Y = 375;

window.requestAnimFrame = (function(callback) {
  return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
  function(callback) {
    window.setTimeout(callback, 1000 / 60);
  };
})();

function drawCircle(myBall, context) {
  context.beginPath();
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
        if (Math.abs(myBall.x - destX) > 2 || Math.abs(myBall.y - destY) > 2) {
        // request new frame
        requestAnimFrame(function() {
          animate(myBall, canvas, context, startTime, ascending, destX, destY);
        });
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
      //console.log(canvas.width + " " + canvas.height); 1280x720

      // wait one second before starting animation
      setTimeout(function() {
        var startTime = (new Date()).getTime();
        animate(myBall, canvas, context, startTime, true, dest_X, dest_Y);
      }, 1000);