<!DOCTYPE html>
<head>
  <meta charset="utf-8">
  <title>${title}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
         <link rel="stylesheet" href="css/normalize.css">
         <link rel="stylesheet" href="css/html5bp.css">    
         <link rel="stylesheet" href="css/button.css">
         <link rel="stylesheet" href="css/main.css">
         <link rel="stylesheet" href="css/instructions.css">            
       </head>
       <body>
      
      <button onclick="document.location.href= '/start'" class= "myButton menubutton"> menu </button>
      <h1> how to play </h1>
      <div>
	      <p id = "firstpara">objective: get the ball in the hole in as few strokes as possible. </p>
	      <p>1. choose a direction -  move the black line on the
	      screen with your mouse to select a direction for your swing. click to lock-in direction.
	      <br><br>
	      2. input a word - the distance the ball travels will be based on 
	      how many times this word (case insensitive) has been tweeted in the past 60 seconds.
	      <br><br>
	      different types of terrain will cause the number of seconds to vary, so be careful where you aim!
	      <br><br>
	      note: your game data will not be saved if you refresh or close the page.
	      <br><br>
	      </p>
        <button id="tutbutton" class="myButton" onclick="document.location.href = '/tutorial';">tutorial</button>
      </div>
      
       <!-- Again, we're serving up the unminified source for clarity. -->
       <script src="js/jquery-2.1.1.js"></script>
       <script src="js/main.js"></script>
       <script src="js/player_select.js"></script>       
     </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
  dealing with real world issues like old browsers.  -->
  </html>
