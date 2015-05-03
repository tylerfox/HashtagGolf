<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/html5bp.css">    
    <link rel="stylesheet" href="../css/button.css">
    <link rel="stylesheet" href="../css/start.css">
    <link rel="stylesheet" href="../css/multiplayer.css">
  </head>
  <body onload ="checkPlayers()">
  <button onclick="start()" class= "myButton menubutton"> exit </button>
  
    <div id="container">
      <div id ="background">
        <h1> lobby </h1>
		<div id="center">
		<h3> Room Name: ${roomName}</h3>
        <h3> Tell other players to join the room "${roomName}" to play this game with you!</h3>
			<div id ="whitebox">
				<p>
					current players in game: 
					<p id="players"> </p>
				</p>
			</div>
			<br>
			<div>
				<button class="myButton" onclick="readyToPlay()" id="lobbybutton">ready to play!</button>
			</div>
	  	</div>
	  </div>
   </div>
    
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="../js/jquery-2.1.1.js"></script>
     <script src="../js/multiplayer.js"></script>
     <script src="../js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
     