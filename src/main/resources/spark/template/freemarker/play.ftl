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
    <link rel="stylesheet" href="css/play.css">
  </head>
  <body>
    <div id="container">
      <div id ="background">
      <p class="hud" id= "distancehud"> Distance To Hole: 900 yards</p>
      <p class="hud"> Par#: 3</p>
      <p class="hud" id="strokehud"> Stroke#: 1</p>
      <canvas id="myCanvas" onmousemove="linedraw(event);" onclick="toggleline();" width="1280" height="720"></canvas>
      

    </div>
    <input type="text" id = "tweetme" name = "tweetme" placeholder="Type a Word or Phrase"></input>
    <button class="myButton" onclick="swing()"> Swing!</button>
   </div>
    
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/play.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old bowsers.  -->
</html>
     