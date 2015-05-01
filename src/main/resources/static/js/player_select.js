function choosecolor(col) {
	postParameters={"color":col};
	playerimg = document.getElementById("playerimage");
	switch(col) {
		case "red": playerimg.style.backgroundImage = "url(css/RedPlayer.png)";
		break;
		case "blue": playerimg.style.backgroundImage = "url(css/BluePlayer.png)";
		break;
		case "yellow": playerimg.style.backgroundImage = "url(css/YellowPlayer.png)";
		break;
		case "green": playerimg.style.backgroundImage = "url(css/GreenPlayer.png)";
		break;
	}
    $.get("/play", postParameters, function(responseJSON) {
    	//document.location.href = "/play";
    })
}

function tolevel() {
	document.location.href = "/level_select";
}