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
    <link rel="stylesheet" href="css/jquery.qtip.css">
</head>

  <body onLoad="checkRefresh()">
    <form name="refreshForm" id="refreshForm">
        <input type="hidden" name="visited" id="refreshField" value="0"/>
    </form>
    
    <!-- modal content -->
    <div id="basic-modal-content">
        <h1>score card</h1>
        <h2 id="info"> congrats on completing this course! </h2>
        <button class="myButton" id= "nxtlvlbtn" onclick="nextlevel()"> next level </button>
        <button class="myButton" onclick="start()"> menu </button>
    </div>
    <div id="fullscreen-modal-content" style="display: none;">
        <h2 id="fullscreenInfo"> please enable full screen mode for a better experience. </h2>
        <br>
        <button class="myButton" onclick="fullScreen()"> go fullscreen </button>
        <button class="myButton" onclick="closeFullScreenPopup()"> no thanks </button>
    </div>
    <div id = "watereffect"></div>
    <div id="container1">
        <button class="myButton menubutton" onclick="start()"> menu </button>
        <p class="hud" id= "distancehud"> distance to hole: 658 yards</p>
        <p class="hud" id= "parhud"> par#: 3</p>
        <p class="hud" id="strokehud"> stroke#: 1</p>
    </div>
    <div id="messagediv">
        <p id="mymessage">Message!</p>
    </div>
    <div id="waitmessagediv">
        <p id="waitmessage">Message!</p>
    </div>
    <canvas id="myCanvas" onmousemove="try {linedraw(event);} catch (e) {console.log(e instanceof ReferenceError);}" onclick="toggleline();" width="1280" height="720"></canvas>
    <!--<canvas id="myCanvas" onmousemove="linedraw(event);" onclick="toggleline();" width="900" height="500"></canvas>-->
    
        <div id = "terrainpic" class="terrain_tee">on the tee <br> <img src="css/clock.png"> 60 seconds</div>
        <input type="text" id = "tweetme" name = "tweetme" onkeypress="isenter(event);" placeholder="type a word or phrase"></input> 
        <!--<button class="myButton" onclick="swing()"> swing!</button>-->
        <button id="swingButton" class="load-button myButton zoom-in" onclick="swing()"><span class="label">swing!</span> <span class="spinner"></span></button> 
        <input type="checkbox" id = "check" onclick="toggleable();"> <label for="check">always to hole </label> </input> 

    <!-- Again, we're serving up the unminified source for clarity. -->
    <script src='js/jquery.js'></script>   
    <script src="js/jquery-2.1.1.js"></script>
    <script src='js/jquery.simplemodal.js'></script>    
    <script src="js/ocanvas-2.7.3.js"></script>
    <script src="js/play2.js"></script> 
    <script src="js/main.js"></script>
    <script src="js/jquery.qtip.min.js"></script>
</body>
<!-- See http://html5boilerplate.com/ for a good place to start
    dealing with real world issues like old bowsers.  -->
</html>
