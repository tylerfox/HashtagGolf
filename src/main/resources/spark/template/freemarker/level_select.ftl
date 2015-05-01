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
         <link rel="stylesheet" href="css/level_select.css">
       </head>
       <body>
        <button onclick="start()" class= "myButton menubutton"> menu </button>
        <div id="container">
          <div id ="background">
            <h1> choose a level </h1>
            <div id = "left">
             <a onclick = "chooselevel('1');" class="myButton" id = "start">level 1</a>
             <a onclick = "chooselevel('2');" class="myButton" id = "multiplayer">level 2</a>
             <br><br>

             <a onclick = "chooselevel('3');" class="myButton" id ="settings">level 4</a>
             <a onclick = "chooselevel('4');" class="myButton" id ="create">level 3</a>
             <br> <br>
             <button onclick= "play();" id = "playbutton" class="myButton"> play </button>
           </div>
           <div id="levelimage"></div>
           <br>
           <p id ="leveldescript">level 1: par: 3 distance to hole: 661 yards</p>

         </div>
       </div>

       <!-- Again, we're serving up the unminified source for clarity. -->
       <script src="js/jquery-2.1.1.js"></script>
       <script src="js/main.js"></script>
       <script src="js/level_select.js"></script>
     </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
  dealing with real world issues like old browsers.  -->
  </html>
