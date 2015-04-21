function choosecolor(col) {
	postParameters={"color":col};
	console.log(col);
    $.get("/play", postParameters, function(responseJSON) {
    	document.location.href = "/play";
    })
}