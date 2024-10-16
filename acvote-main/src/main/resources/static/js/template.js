

/**
 * Populates the modal with info from the template
 */
function fillDeleteModal(id) {

	console.log(id);

	$("#btnDeleteTemplate").attr("data-tid", id);
	$("#fillInTitle").html("Do You Really Want to Delete Ballot #" + id + "?");
}

/**
 * Deletes the template that is currently filling the modal diagram
 */
function deleteTemplate() {
	var id = $(this).attr("data-tid");

	$.ajax({
		url: "/acvote/template/ajax/deleteTemplate",
		type: "delete",
		data: {
			tid: id
		},
		success: function(response) {
			$("#btnDeleteTemplate").hide();
			postMessageAndReload(response);

			if ( $( "#templateTable" ).length ) {
 				postMessageAndReload(response);
    		} else {
				window.location.assign("/acvote/templates")
			}
			
		},
		error: function(xhr, textStatus, errorThrown) {
			$("#confirm-contents").html(textStatus + " " + xhr.status);
		}
	});
}

/*

Lots of functions to edit template fields next

*/

//Edits template title field
function editTemplateTitle() {

	var div = document.getElementById("tTitle");
	var paragraph = document.getElementById("tTitleText");
	var text = paragraph.textContent;

	//create element (input field) 
	var input = document.createElement("input");
	input.id = "tTitleInput";
	input.type = "text";
	input.value = text;
	input.classList.add("tTitle");
	input.maxLength = 50;
	input.style="width:100%"

	//switch text with new element
	div.replaceChild(input, paragraph);

	//set focus on the new element
	input.focus();
	input.select();

	// Attach an event listener to save changes when the input loses focus
	input.addEventListener("blur", function() {

		//grabbing values and calling ajax
		var tid = $("#idHolder").attr("tid");
		var tTitle = $(this).val();
		var bTitle = $(".bTitle").html();
		var description = $(".desc").html();
		var instructions = $(".instr").html();
		var voteType = $("#voteTypeSelect").val();
		var outcomes = $(".outcomes").html();
		var basis = $("#basisSelect").val();

		$.ajax({
			url: "/acvote/template/ajax/editTemplate",
			type: "put",
			data: {
				tid: tid,
				tTitle: tTitle,
				bTitle: bTitle,
				description: description,
				instructions: instructions,
				voteType: voteType,
				outcomes: outcomes,
				basis: basis,
			},
			success: function(response) {
				postMessageAndReload(response);
			},
			error: function(xhr, textStatus, errorThrown) {
				displayMessage("unable to update template title; "+xhr.status);
			}
		});
	});
}

//Edits ballot title field
function editBallotTitle() {

	var div = document.getElementById("bTitle");
	var paragraph = document.getElementById("bTitleText");
	var text = paragraph.textContent;

	//create element (input field) 
	var input = document.createElement("input");
	input.id = "bTitleInput";
	input.type = "text";
	input.value = text;
	input.classList.add("bTitle", "hov");
	input.maxLength = 50;
	input.style="width:100%"

	//switch text with new element
	div.replaceChild(input, paragraph);

	//set focus on the new element
	input.focus();
	input.select();

	// Attach an event listener to save changes when the input loses focus
	input.addEventListener("blur", function() {

		//grabbing values and calling ajax
		var tid = $("#idHolder").attr("tid");
		var tTitle = $(".tTitle").html();
		var bTitle = $(this).val();
		var description = $(".desc").html();
		var instructions = $(".instr").html();
		var voteType = $("#voteTypeSelect").val();
		var outcomes = $(".outcomes").html();
		var basis = $("#basisSelect").val();

		$.ajax({
			url: "/acvote/template/ajax/editTemplate",
			type: "put",
			data: {
				tid: tid,
				tTitle: tTitle,
				bTitle: bTitle,
				description: description,
				instructions: instructions,
				voteType: voteType,
				outcomes: outcomes,
				basis: basis,
			},
			success: function(response) {
				postMessageAndReload(response);
			},
			error: function(xhr, textStatus, errorThrown) {
				displayMessage("unable to update template ballot title; "+xhr.status);
			}
		});
	});
}

//Edits description field
function editDescription() {

	//this prevents multiple tables from being made
	description.onclick = null;

	var div = document.getElementById("description");
	var paragraph = document.getElementById("descText");

	//set the max amount of characters the field will allow
	var max = 512;
	
	// Create a <p> element for character count
  var charCountElement = document.createElement("p");
  charCountElement.id = "charCount";
  charCountElement.textContent = "Characters left: " + max;

  // Append the <p> element to the container
  div.appendChild(charCountElement);
	//creating the summer note editor
	$(paragraph).summernote({
		toolbar: [
			['style', ['style', 'bold', 'underline', 'clear']],
			['font', ['strikethrough', 'superscript', 'subscript']],
			['fontname', ['fontname']],
			['fontsize', ['fontsize']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph']],
			['insert', ['link']],
			['view', ['codeview', 'help']],
		],
		callbacks: {
			onInit: function() {
				// Set focus to the Summernote editor
				$(paragraph).summernote('focus');
				var t = $(paragraph).summernote('code');
        		var remainingChars = max - t.length;
				charCountElement.textContent = "Characters remaining: " + remainingChars;
				// Select the text inside the Summernote editor
				var editor = $(paragraph).siblings('.note-editor').find('.note-editable');
				var range = document.createRange();
				range.selectNodeContents(editor[0]);
				var sel = window.getSelection();
				sel.removeAllRanges();
				sel.addRange(range);
			},
			//Set Max Characters
			onKeydown: function(e) {
          var t = $(paragraph).summernote('code');
          if (t.length >= max) {
            //delete key
            if (e.keyCode != 8)
              e.preventDefault();
          }
        },
        onKeyup: function(e) {
        var t = $(paragraph).summernote('code');
        var remainingChars = max - t.length;
        
        // Update the character count element
        charCountElement.textContent = "Characters remaining: " + remainingChars;

        if (typeof callbackMax == 'function') {
          callbackMax(remainingChars);
        }
      },
onPaste: function(e) {
  e.preventDefault();
  var t = $(paragraph).summernote('code');
  var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
  var currentLength = t.length;
  var bufferLength = bufferText.length;
  var remainingChars = max - currentLength;

  if (remainingChars <= 0) {
    // Prevent pasting if the current content is already at the limit
    return;
  }

  var charactersToInsert = Math.min(bufferLength, remainingChars);
  var truncatedBufferText = bufferText.substring(0, charactersToInsert);

  // Calculate the new content to be inserted after the paste
  var all = t + truncatedBufferText;

  // If the new content will exceed the character limit, truncate it
  if (all.length > max) {
    charactersToInsert = max - t.length;
    truncatedBufferText = bufferText.substring(0, charactersToInsert);
  }

  // Insert the truncated text
  var range = document.getSelection().getRangeAt(0);
  range.deleteContents();
  range.insertNode(document.createTextNode(truncatedBufferText));

  // Update the character count element
  var newRemainingChars = max - t.length + charactersToInsert;
  charCountElement.textContent = "Characters remaining: " + newRemainingChars;

  if (typeof callbackMax == 'function') {
    callbackMax(newRemainingChars);
  }
}



		}
	});

	//when clicking anywhere not in the div
	document.addEventListener('click', function(event) {
		var editorContainer = div;
		if (!editorContainer.contains(event.target)) {

			//grabbing values and calling ajax
			var tid = $("#idHolder").attr("tid");
			var tTitle = $(".tTitle").html();
			var bTitle = $(".bTitle").html();
			var description = $(paragraph).summernote('code');
			var instructions = $(".instr").html();
			var voteType = $("#voteTypeSelect").val();
			var outcomes = $(".outcomes").html();
			var basis = $("#basisSelect").val();

			$.ajax({
				url: "/acvote/template/ajax/editTemplate",
				type: "put",
				data: {
					tid: tid,
					tTitle: tTitle,
					bTitle: bTitle,
					description: description,
					instructions: instructions,
					voteType: voteType,
					outcomes: outcomes,
					basis: basis,
				},
				success: function(response) {
					postMessageAndReload(response);
				},
				error: function(xhr, textStatus, errorThrown) {
					displayMessage("unable to update description; "+xhr.status);
				}
			});
		}

	});
}

//Edits instructions field
function editInstructions() {

	//this prevents multiple tables from being made
	instructions.onclick = null;

	var div = document.getElementById("instructions");
	var paragraph = document.getElementById("instrText");

	//set the max amount of characters the field will allow
	var max = 512;
	
	// Create a <p> element for character count
  var charCountElement = document.createElement("p");
  charCountElement.id = "charCount";
  charCountElement.textContent = "Characters left: " + max;

  // Append the <p> element to the container
  div.appendChild(charCountElement);
	//creating the summer note editor
	$(paragraph).summernote({
		toolbar: [
			['style', ['style', 'bold', 'underline', 'clear']],
			['font', ['strikethrough', 'superscript', 'subscript']],
			['fontname', ['fontname']],
			['fontsize', ['fontsize']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph']],
			['insert', ['link']],
			['view', ['codeview', 'help']],
		],
		callbacks: {
			onInit: function() {
				// Set focus to the Summernote editor
				$(paragraph).summernote('focus');
				var t = $(paragraph).summernote('code');
        		var remainingChars = max - t.length;
				charCountElement.textContent = "Characters remaining: " + remainingChars;
				// Select the text inside the Summernote editor
				var editor = $(paragraph).siblings('.note-editor').find('.note-editable');
				var range = document.createRange();
				range.selectNodeContents(editor[0]);
				var sel = window.getSelection();
				sel.removeAllRanges();
				sel.addRange(range);
			},
			//Set Max Characters
			onKeydown: function(e) {
          var t = $(paragraph).summernote('code');
          if (t.length >= max) {
            //delete key
            if (e.keyCode != 8)
              e.preventDefault();
          }
        },
        onKeyup: function(e) {
        var t = $(paragraph).summernote('code');
        var remainingChars = max - t.length;
        
        // Update the character count element
        charCountElement.textContent = "Characters remaining: " + remainingChars;

        if (typeof callbackMax == 'function') {
          callbackMax(remainingChars);
        }
      },
onPaste: function(e) {
  e.preventDefault();
  var t = $(paragraph).summernote('code');
  var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
  var currentLength = t.length;
  var bufferLength = bufferText.length;
  var remainingChars = max - currentLength;

  if (remainingChars <= 0) {
    // Prevent pasting if the current content is already at the limit
    return;
  }

  var charactersToInsert = Math.min(bufferLength, remainingChars);
  var truncatedBufferText = bufferText.substring(0, charactersToInsert);

  // Calculate the new content to be inserted after the paste
  var all = t + truncatedBufferText;

  // If the new content will exceed the character limit, truncate it
  if (all.length > max) {
    charactersToInsert = max - t.length;
    truncatedBufferText = bufferText.substring(0, charactersToInsert);
  }

  // Insert the truncated text
  var range = document.getSelection().getRangeAt(0);
  range.deleteContents();
  range.insertNode(document.createTextNode(truncatedBufferText));

  // Update the character count element
  var newRemainingChars = max - t.length + charactersToInsert;
  charCountElement.textContent = "Characters left: " + newRemainingChars;

  if (typeof callbackMax == 'function') {
    callbackMax(newRemainingChars);
  }
}



		}
	});

	//when clicking anywhere not in the div
	document.addEventListener('click', function(event) {
		var editorContainer = div;
		if (!editorContainer.contains(event.target)) {

			//grabbing values and calling ajax
			var tid = $("#idHolder").attr("tid");
			var tTitle = $(".tTitle").html();
			var bTitle = $(".bTitle").html();
			var description = $(".desc").html();
			var instructions = $(paragraph).summernote('code');
			var voteType = $("#voteTypeSelect").val();
			var outcomes = $(".outcomes").html();
			var basis = $("#basisSelect").val();

			$.ajax({
				url: "/acvote/template/ajax/editTemplate",
				type: "put",
				data: {
					tid: tid,
					tTitle: tTitle,
					bTitle: bTitle,
					description: description,
					instructions: instructions,
					voteType: voteType,
					outcomes: outcomes,
					basis: basis,
				},
				success: function(response) {
					postMessageAndReload(response);
				},
				error: function(xhr, textStatus, errorThrown) {
					displayMessage("unable to update instructions; "+xhr.status);
				}
			});
		}

	});
}

//Edits vote type field
function editVoteType() {

	//grabbing values and calling ajax
	var tid = $("#idHolder").attr("tid");
	var tTitle = $(".tTitle").html();
	var bTitle = $(".bTitle").html();
	var description = $(".desc").html();
	var instructions = $(".instr").html();
	var voteType = $("#voteTypeSelect").val();
	var outcomes = $(".outcomes").html();
	var basis = $("#basisSelect").val();

	$.ajax({
		url: "/acvote/template/ajax/editTemplate",
		type: "put",
		data: {
			tid: tid,
			tTitle: tTitle,
			bTitle: bTitle,
			description: description,
			instructions: instructions,
			voteType: voteType,
			outcomes: outcomes,
			basis: basis,
		},
		success: function(response) {
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			displayMessage("unable to update vote type; "+xhr.status);
		}
	});
}

//Edits outcomes field
function editOutcomes() {

	var div = document.getElementById("outcomes");
	var paragraph = document.getElementById("outcomesText");
	var text = paragraph.textContent;

	//create element (input field) 
	var input = document.createElement("input");
	input.id = "outcomesInput";
	input.type = "number";
	input.value = text;
	input.classList.add("outcomes");
	
	// Add an event listener to limit the input to two digits
    input.addEventListener('input', function () {
      input.value = Math.abs(input.value.slice(0, 2));
    });

	//switch text with new element
	div.replaceChild(input, paragraph);

	//set focus on the new element
	input.focus();
	input.select();

	// Attach an event listener to save changes when the input loses focus
	input.addEventListener("blur", function() {

		//grabbing values and calling ajax
		var tid = $("#idHolder").attr("tid");
		var tTitle = $(".tTitle").html();
		var bTitle = $(".bTitle").html();
		var description = $(".desc").html();
		var instructions = $(".instr").html();
		var voteType = $("#voteTypeSelect").val();
		var outcomes = $(this).val();
		var basis = $("#basisSelect").val();

		$.ajax({
			url: "/acvote/template/ajax/editTemplate",
			type: "put",
			data: {
				tid: tid,
				tTitle: tTitle,
				bTitle: bTitle,
				description: description,
				instructions: instructions,
				voteType: voteType,
				outcomes: outcomes,
				basis: basis,
			},
			success: function(response) {
				postMessageAndReload(response);
			},
			error: function(xhr, textStatus, errorThrown) {
				displayMessage("unable to update number of outcomes; "+xhr.status);
			}
		});
	});
}

//Edits basis field
function editBasis() {

	//grabbing values and calling ajax
	var tid = $("#idHolder").attr("tid");
	var tTitle = $(".tTitle").html();
	var bTitle = $(".bTitle").html();
	var description = $(".desc").html();
	var instructions = $(".instr").html();
	var voteType = $("#voteTypeSelect").val();
	var outcomes = $(".outcomes").html();
	var basis = $("#basisSelect").val();

	$.ajax({
		url: "/acvote/template/ajax/editTemplate",
		type: "put",
		data: {
			tid: tid,
			tTitle: tTitle,
			bTitle: bTitle,
			description: description,
			instructions: instructions,
			voteType: voteType,
			outcomes: outcomes,
			basis: basis,
		},
		success: function(response) {
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			displayMessage("unable to update options basis; "+xhr.status);
		}
	});
}

/*

Creates a new template

*/
function createTemplate(){
	
	$.ajax({
		url: "/acvote/template/ajax/createTemplate",
		type: "post",
	
		success: function(tid) {
		
			window.location.assign("/acvote/template?tid=" + tid);
			
		},
		error: function(xhr,textStatus, errorThrown){
			
			console.log("unable to call create template");
			displayMessage("unable to create template; "+xhr.status);
			
		}

		});
}
