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
         <link rel="stylesheet" href="css/tutorial.css">
       </head>
       <body>

      <button onclick="document.location.href= '/start'" class= "myButton menubutton"> menu </button>
      <p id= "para1"> welcome to the tutorial screen! <br> i'll teach you what everything on the play screen does. <br>click next at the top left</p>
      <p id= "para2"> here is your main play screen. you can move this line around with your mouse,<br> and click to toggle when you have it at the angle you want. <br> in multiplayer, you can hover your mouse over a ball to see whose it is.</p>
      <p id= "para3"> this is where you type in <br> the word or phrase you want to swing with. <br> be creative, you can't use the same word twice!</p>
      <p id= "para4"> when you have input your word, <br>click this 'swing' button or press enter <br>to query with your word.</p>
      <p id= "para5"> checking this box will <br>always aim the angle of your <br> swing at the hole. useful! </p>
      <p id= "para6"> this box shows the terrain you are in. <br>rougher terrain (like the bunker) <br> makes your swings less powerful <br> by shortening the time it searches for, <br>so keep that in mind!</p>
      <p id= "para7"> heres your hud for the course. this shows you...</p>
      <p id= "para8"> the stroke you are on...</p>
      <p id= "para9"> the par for the course... </p>
      <p id= "para10"> and your current distance to the hole</p>
      <p id= "para11"> and thats about it! now you know how to play #golf</p>
      <button id="nextbtn" class="myButton" onclick="nexttut()"> next </button>
       <!-- Again, we're serving up the unminified source for clarity. -->
       <script src="js/jquery-2.1.1.js"></script>
       <script src="js/main.js"></script>
       <script src="js/tutorial.js"></script>
     </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
  dealing with real world issues like old browsers.  -->
  </html>
