<!-<!DOCTYPE html>
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
      <p class="hud" id= "distancehud"> distance to hole: 658 yards</p>
      <p class="hud"> par#: 3</p>
      <p class="hud" id="strokehud"> stroke#: 1</p>
      <canvas id="myCanvas" onmousemove="linedraw(event);" onclick="toggleline();" width="1280" height="720"></canvas>
      <!--<canvas id="myCanvas" onmousemove="linedraw(event);" onclick="toggleline();" width="900" height="500"></canvas>-->
      

    </div>
    <input type="text" id = "tweetme" name = "tweetme" onkeypress="isenter(event);" placeholder="type a word or phrase"></input>
    <input type="checkbox" id = "check" onclick="toggleable();"> always to hole </input> 
    <!--<button class="myButton" onclick="swing()"> swing!</button>-->
    <button id="swingButton" class="load-button myButton zoom-in" onclick="swing()"><span class="label">swing!</span> <span class="spinner"></span></button>  
   </div>
    
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/ocanvas-2.7.3.js"></script>
     <script src="js/play2.js"></script>     
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old bowsers.  -->
</html>
     