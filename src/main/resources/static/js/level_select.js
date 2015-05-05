
document.getElementById("playbutton").disabled = true;
function chooselevel(lvl) {
	postParameters={"level":lvl};
	levelimg = document.getElementById("levelimage");
	leveldescrip = document.getElementById("leveldescript");
	playbtn = document.getElementById("playbutton");
	switch(lvl) {
		case "1": levelimg.style.backgroundImage = "url(css/gui_hole1.png)";
		levelimg.style.backgroundSize = "512px 288px";
		leveldescrip.innerHTML = "level 1: par: 3 distance to hole: 189 yards <br>" + 
		"difficulty: <img src='css/starfilled.png'/><img src='css/starfilled.png'/><img src='css/starunfilled.png'/><img src='css/starunfilled.png'/>";
		playbtn.disabled = false;
		break;
		case "2": levelimg.style.backgroundImage = "url(css/gui_hole2.png)";
		levelimg.style.backgroundSize = "512px 288px";
		leveldescrip.innerHTML = "level 2: par: 4 distance to hole: 412 yards <br>" +  
		"difficulty: <img src='css/starfilled.png'><img src='css/starfilled.png'><img src='css/starfilled.png'><img src='css/starunfilled.png'>";
		playbtn.disabled = false;
		break;
		case "3": levelimg.style.backgroundImage = "url(css/gui_hole3.png)";
		levelimg.style.backgroundSize = "512px 288px";
		leveldescrip.innerHTML = "level 3: par: 5 distance to hole: 593 yards <br>" +  
		"difficulty: <img src='css/starfilled.png'><img src='css/starfilled.png'><img src='css/starfilled.png'><img src='css/starfilled.png'>";
		playbtn.disabled = false;
		break;
		/*case "4": levelimg.style.background = "none";
		leveldescrip.innerHTML = "level not yet made";
		playbtn.disabled = true;
		break;*/
	}
    $.get("/level_select", postParameters, function(responseJSON) {
    	//document.location.href = "/play";
    })
}

function play() {
	document.location.href = "/play";
}

function hostlobby() {
	document.location.href = "/hostlobby";
}