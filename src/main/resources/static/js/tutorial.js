var currparanum = 1;
totalparas = 11;
function nexttut() {
	currpara = document.getElementById("para" + currparanum);
	console.log(currpara);
	currpara.style.visibility = "hidden";
	currparanum ++;
	if (currparanum > totalparas) {
		document.location.href = "/start";
	} else {
		if (currparanum == totalparas) {
			nextbutton = document.getElementById("nextbtn");
			nextbutton.innerHTML = "finish";
		}
		nextpara = document.getElementById("para" + currparanum);
		nextpara.style.visibility = "visible";
	}
}