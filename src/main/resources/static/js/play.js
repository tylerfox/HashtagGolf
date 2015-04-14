var START_X = 328;
var START_Y = 375
var dest_X = 500;
var dest_Y = 350;

window.requestAnimFrame = (function(callback) {
        return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
        function(callback) {
          window.setTimeout(callback, 1000 / 60);
        };
      })();

      function drawRectangle(myBall, context) {
        context.beginPath();
        context.arc(myBall.x, myBall.y, myBall.radius, 0, 2 * Math.PI, false);  
        context.fillStyle = 'rgba(255, 255, 255, 1.0)';
        context.fill();        
        context.stroke();
      }
      function animate(myBall, canvas, context, startTime, ascending, destX, destY) {
        // update
        var time = (new Date()).getTime() - startTime;

        var linearSpeed = 100;
        // pixels / second
        var newX = START_X + linearSpeed * time / 1000;
        var newY = START_Y + linearSpeed * time / 1000;

        if(newX != destX) {
          myBall.x = newX;
        }
        if(newY != destY) {
        	myBall.y = newY;
        }

        if(myBall.radius < 10 && ascending) {
        	myBall.radius += .05;        	
        } else if (myBall.radius >= 5) {        	
        	ascending = false;        	
        	myBall.radius = myBall.radius - .05;
        }

        // clear
        context.clearRect(0, 0, canvas.width, canvas.height);

        drawRectangle(myBall, context);

        // request new frame
        requestAnimFrame(function() {
          animate(myBall, canvas, context, startTime, ascending, destX, destY);
        });
      }
      var canvas = document.getElementById('myCanvas');
      var context = canvas.getContext('2d');

      var myBall = {
        x: START_X,
        y: START_Y,
        radius: 5,        
      };

      drawRectangle(myBall, context);

      // wait one second before starting animation
      setTimeout(function() {
        var startTime = (new Date()).getTime();
        animate(myBall, canvas, context, startTime, true, dest_X, dest_Y);
      }, 1000);