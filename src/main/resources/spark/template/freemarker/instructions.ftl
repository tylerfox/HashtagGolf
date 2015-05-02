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
      
      <a href="/start" class= "myButton menubutton"> menu </a>
      <h1> how to play </h1>
      <div>
	      <p id = "firstpara">the aim of the game is just like in regular golf: get the ball in the hole in as few shots as possible.</p>
	      <p>however, the distance you hit the ball will depend on a word that you input. how far the ball goes will be determined by the number of times your word was tweeted in the last 60 seconds. </p>
	      <p>guess something too popular, and you will fly past the hole.</p> 
	      <p>guess something too obscure, and you won't go anywhere. </p>
	      <p>you'll have to think of just the right words to get you to the hole. so get playing!  </p>
      </div>
      
       <!-- Again, we're serving up the unminified source for clarity. -->
       <script src="js/jquery-2.1.1.js"></script>
       <script src="js/main.js"></script>
       <script src="js/player_select.js"></script>       
     </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
  dealing with real world issues like old browsers.  -->
  </html>
