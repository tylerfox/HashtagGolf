function chooselevel(lvl) {
	postParameters={"level":lvl};
	levelimg = document.getElementById("levelimage");
	leveldescrip = document.getElementById("leveldescript");
	playbtn = document.getElementById("playbutton");
	switch(lvl) {
		case "1": levelimg.style.backgroundImage = "url(css/gui_hole1.png)";
		levelimg.style.backgroundSize = "512px 288px";
		leveldescrip.innerHTML = "level 1: par: 3 distance to hole: 661 yards";
		playbtn.disabled = false;
		break;
		case "2": levelimg.style.background = "white";
		leveldescrip.innerHTML = "level not yet made";
		playbtn.disabled = true;
		break;
		case "3": levelimg.style.background = "white";
		leveldescrip.innerHTML = "level not yet made";
		playbtn.disabled = true;
		break;
		case "4": levelimg.style.background = "white";
		leveldescrip.innerHTML = "level not yet made";
		playbtn.disabled = true;
		break;
	}
    $.get("/level_select", postParameters, function(responseJSON) {
    	//document.location.href = "/play";
    })
}

function play() {
	document.location.href = "/play";
}