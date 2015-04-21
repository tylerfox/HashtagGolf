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
    <link rel="stylesheet" href="css/player_select.css">
  </head>
  <body>
    <div id="container">
      <div id ="background">
      <h1> choose a player </h1>
     <a onclick = "choosecolor('red');" class="redPlayerButton" id = "start">red</a>
     <a href= "/play" onclick = "choosecolor('blue');" class="bluePlayerButton" id = "multiplayer">blue</a>
     <br><br>

      <a href= "/play" onclick = "choosecolor('green');" class="greenPlayerButton" id ="settings">green</a>
      <a href= "/play" onclick = "choosecolor('yellow');" class="yellowPlayerButton" id ="create">yellow</a>
      

    </div>
   </div>
    
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/main.js"></script>
     <script src="js/player_select.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
     