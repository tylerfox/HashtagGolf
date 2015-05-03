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
    <link rel="stylesheet" href="css/start.css">
  </head>
  <body>
    <div id="container">
      <div id ="background">
      <h1> #golf </h1>
  	 <button onclick="document.location.href= '/single_player_select'" class="myButton" id = "left">start</button>
     <button onclick="document.location.href= '/multiplayer'" class="myButton" id ="right">multiplayer</button>
     <br><br>

      <button onclick="document.location.href= '/tutorial'" class="myButton" id ="tutorial">tutorial</button>
      <button onclick="document.location.href= '/instructions'" class="myButton" id ="instructions">instructions</button>

    </div>
   </div>
  	
     <!-- Again, we're serving up the unminified source for clarity. -->
    <script src="js/jquery-2.1.1.js"></script>
    <script src="js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
  	 