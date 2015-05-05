window.onbeforeunload = confirmExit;

function confirmExit(e) {  
	var e = e || window.event;

	if (e) {   
		var postParameters = {};
		$.post("/exit", postParameters, function(responseJSON) {
			
		});
	}
}