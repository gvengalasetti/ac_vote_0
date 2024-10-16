function csrfTokenValue(){
	var token = $("meta[name='_csrf']").attr("content"); 
	var header = $("meta[name='_csrf_header']").attr("content");
}



// assumes there exists a form with id=flash-msg that can post
// with the right parameters to the current page for reload
function postMessageAndReload(response) {
	$("#flash-msg").val(response);
	$("#flasher").submit();
}


function flashMessage(msg) {
	if (msg.length>0) {
		displayMessage(msg)
	}
}


function displayMessage(text, onExit) {
	
	//if (message) {
      var message = text;

      var dialogue = document.createElement('div');
      dialogue.setAttribute("id", "toast");
      dialogue.style.position = 'fixed';
      dialogue.style.bottom = '100px';
      dialogue.style.right = '20px';
      dialogue.style.backgroundColor = '#fff';
      dialogue.style.color = '#000';
      dialogue.style.padding = '10px';
      dialogue.style.border = '1px solid #2aa33f';
      dialogue.style.borderRadius = '5px';
      dialogue.style.transition = 'transform 0.5s ease-in-out';
	dialogue.style.fontFamily
      dialogue.textContent = message;
   dialogue.style.fontSize = '150%';
      dialogue.style.lineHeight = '150%';
      document.body.appendChild(dialogue);

      setTimeout(function() {
        dialogue.style.transform = 'translateX(-100%)';
      }, 100);

      setTimeout(function() {
        dialogue.remove();
        if(onExit!=null){
		onExit();	
		}
      }, 3000);
    }
//}