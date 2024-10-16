//--------------------------------------------
// called when user saves/commits a custom option
//
function addCustomOption(bid) {

	var oid = parseSingleWord($('#optid').val());
	var otitle = $('#opttitle').val();

	console.log("add custom option not implemented yet [" + bid + "][" + oid + "][" + otitle + "]");

	$.ajax({
		url: "/acvote/ballot/ajax/addOption",
		type: "POST",
		data: {
			bid: bid,
			oid: oid,
			title: otitle
		},
		success: function(response) {
			$("#newOptionDialog").modal("hide");
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			// alert(textStatus + " " + xhr.status);
			displayMessage("unable to add option; " + xhr.status);
		}
	});


}
function parseSingleWord(inputString) {
  // Replace all spaces with underscores
  let parsedString = inputString.replace(/\s+/g, '_');

  // Remove duplicate underscores (caused by multiple spaces)
  parsedString = parsedString.replace(/_+/g, '_');

  // Remove special characters using regex (only keep letters, numbers, and underscores)
  parsedString = parsedString.replace(/[^\w]/g, '');

  return parsedString;
}

//--------------------------------------------
// opens the correct add option modal dialog
// based on the option basis value.
//
function addOption(bid) {

	var basis = $('#selBasis').val();  // current ballots basis = true or false

	if (basis == 'true') {
		$("#addFacCanModal").modal("show");   // faculty based
	} else {
		$("#newOptionDialog").modal("show");
	}
}



//--------------------------------------------
// deletes the option from the specified ballot
//
function deleteOption(bid, oid) {

	$.ajax({
		url: "/acvote/ballot/ajax/deleteOption",
		type: "DELETE",
		data: {
			bid: bid,
			oid: oid
		},
		success: function(response) {
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			alert(textStatus + " " + xhr.status);
		}
	});
}

//--------------------------------------------
// disables the ballot's option; option will be skipped when tallied
//
function disableOption(bid, oid) {
	alert('disable not implemented yet');
}

//--------------------------------------------
// deletes all options from the specified ballot
//
function deleteAllOptions(bid) {

	$.ajax({
		url: "/acvote/ballot/ajax/deleteAllOptions",
		type: "DELETE",
		data: {
			bid: bid
		},
		success: function(response) {
			postMessageAndReload(response);
		
		},
		error: function(xhr, textStatus, errorThrown) {
			alert(textStatus + " " + xhr.status);
		}
	});
}

//--------------------------------------------
// When are you sure button in confirmation dialog
// is clicked we tell server to delete the ballot
// specified by data-bid attribute on button and 
// reload the ballot list view.  ballot should be
// gone.
//
function deleteBallot() {
	
	var id = $(this).attr("data-bid");

	console.log("deleting ballot "+id);

	$.ajax({
		url: "/acvote/ballot/ajax/deleteBallot",
		type: "DELETE",
		data: {
			bid: id
		},
		success: function(response) {
			$("#btnDeleteBallot").hide();
			
			if ( $( "#ballotTable" ).length ) {
 				postMessageAndReload(response);
    		} else {
				window.location.assign("/acvote/ballots")
			}
		},
		error: function(xhr, textStatus, errorThrown) {
			$("#confirm-contents").html(textStatus + " " + xhr.status);
		}
	});
}

//--------------------------------------------------
//Fills data-bid attribute of delete button in delete modal.
//Called when dropdown delete button is clicked to pass on
//data-bid from dropdown delete button to delete modal button.
//Delete button in delete modal needs data-bid for deleteBallot().
//
function deleteFromList(bid) {

	console.log(bid);

	$("#btnDeleteBallot").attr("data-bid", bid);
	$("#fillInTitle").html("Do You Really Want to Delete Ballot #" + bid + "?");
}

//----------------------------------------------------
//Redirects user to edit ballot page once new ballot is created.
//Grabs bid from new ballot to properly redirect.
function createBallot() {

	$.ajax({
		url: "/acvote/ballot/ajax/createBallot",
		type: "POST",
		data: {
		},
		success: function(bid) {
			window.location.assign("/acvote/ballot?bid=" + bid);
		},
		error: function(xhr, textStatus, errorThrown) {

		}
	});
}

//----------------------------------------------------
//Redirects user to edit ballot page once new ballot is created.
//Grabs bid from new ballot to properly redirect.
function createBallotFromTemplate() {
	var table = $('#templateList').DataTable();
	
	var id = $.map(table.rows('.selected').data(), function (item) {
        return item[0]
    });

	var tid = parseInt(id);
	console.log("The id of the template is: " + tid);
	

	$.ajax({
		url: "/acvote/ballot/ajax/createBallotFromTemplate",
		type: "POST",
		data: {
			tid:tid,
		},
		success: function(bid) {
			window.location.assign("/acvote/ballot?bid=" + bid);
		},
		error: function(xhr, textStatus, errorThrown) {

		}
	});
}


function showBallotToEdit() {
	var bid = $(this).attr("bid");

	$.ajax({
		url: "/acvote/ballot/ajax/editForm",
		type: "get",
		data: {
			bid: bid,
		},


		success: function(response) {
			$("#dialog-contents").html(response);
			$("#editBallotDialog").modal("show");
		},
		error: function(xhr, textStatus, errorThrown) {
			$("#dialog-contents").html(textStatus + " " + xhr.status);
		}
	});
}


function adjustBallot() {
	var bid = $(this).attr("bid");
	var title = $("#titleBox").val();
	var instructions = $("#instructionsBox").val();
	var description = $("#descriptionBox").val();
	var vote_type = $("#voteType").val();
	var min_vote_selections = $("#minimumvoteselections").val();
	var max_vote_selections = $("#maximumvoteselections").val();
	var polls_open = $("#pollsopen").val();
	var polls_close = $("#pollsclose").val();
	var voters = $("#voters").val();

	

	 
	$.ajax({
		url: "/acvote/ballot/ajax/editBallot",
		type: "get",
		data: {
			bid: bid,
			title: title,
			instructions: instructions,
			description: description,
			vote_type: vote_type,
			min_vote_selections: min_vote_selections,
			max_vote_selections: max_vote_selections,
			polls_open: polls_open,
			polls_close: polls_close,
			voters: voters,
		},
		success: function(response) {
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			$("#dialog-contents").html(textStatus + " " + xhr.status);
		}
	});
}



// -------------------------------------------------------------------------
// Adds the selected faculty candidates as options for the current ballot.
//
function addCandidates() {
	var newOptionIDs = [];
	var table = $('#candidateOptions').DataTable();

	table.rows({ selected: true }).every(function() {
		var data = this.data();
		newOptionIDs.push(data[1]);
	});
	var bid = $(this).attr("bid");

	console.log(bid);
	console.log(newOptionIDs);


	$.ajax({
		url: "/acvote/ballot/ajax/addCandidates",
		type: "POST",
		data: {
			bid: bid,
			newOptionIDs: newOptionIDs
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

function showBallot(data) {
	var bname = $(this).attr("ballot-bid")

	consol.log(bname)


	$.ajax({
		url: "/acvote/ballots/testBallot/ajax",
		type: "get",
		data: {
			bid: bname
		},
		success: function(response) {

		},
		error: function(xhr, textStatus, errorThrown) {

		}
	});
}

function showEmptyBallot() {

	$.ajax({
		url: "/acvote/ballot/ajax/createForm",
		type: "get",


		success: function(response) {
			$("#dialog-contents").html(response);
			$("#ballotDialog").modal("show");
		},
		error: function(xhr, textStatus, errorThrown) {
			$("#dialog-contents").html(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot title to whatever is in the input field
 */
function setBallotTitle() {
	var bid = $('#idHolder').attr("bid");
	var newTitle = $("#titleInput").val();

	$.ajax({
		url: "/acvote/ballot/ajax/setBallotTitle",
		type: "PUT",
		data: {
			bid: bid,
			ballotTitle: newTitle
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot description to whatever is in the input field
 */
function setBallotDescription(text) {
	var bid = $('#idHolder').attr("bid");
	var newDescription = text;

	$.ajax({
		url: "/acvote/ballot/ajax/setBallotDescription",
		type: "PUT",
		data: {
			bid: bid,
			ballotDescription: newDescription
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot instructions to whatever is in the input field
 */
function setBallotInstructions(text) {
	var bid = $('#idHolder').attr("bid");
	var newInstructions = text;

	$.ajax({
		url: "/acvote/ballot/ajax/setBallotInstructions",
		type: "PUT",
		data: {
			bid: bid,
			ballotInstructions: newInstructions
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}


function setBallotBasis() {

	var bid = $('#idHolder').attr("bid");
	var newbasis = $('#selBasis').val();

	$.ajax({
		url: "/acvote/ballot/ajax/setBasis",
		type: "PUT",
		data: {
			bid: bid,
			basis: newbasis
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot typeOfVote to whatever is in the input field
 */
function setBallotTypeOfVote() {
	var bid = $('#idHolder').attr("bid");
	var newTypeOfVote = $("#typeOfVoteInput").val();

	$.ajax({
		url: "/acvote/ballot/ajax/setBallotTypeOfVote",
		type: "PUT",
		data: {
			bid: bid,
			ballotTypeOfVote: newTypeOfVote
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot outcomes to whatever is in the input field
 */
function setBallotOutcomes() {
	var bid = $('#idHolder').attr("bid");
	var newOutcomes = $("#outcomesInput").val();

	$.ajax({
		url: "/acvote/ballot/ajax/setBallotOutcomes",
		type: "PUT",
		data: {
			bid: bid,
			ballotOutcomes: newOutcomes
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot startTime to whatever is in the input field
 */
function setBallotStartTime() {
	var bid = $('#idHolder').attr("bid");
	var newStartTime = $("#startTimeInput").val();

	$.ajax({
		url: "/acvote/ballot/ajax/setBallotStartTime",
		type: "PUT",
		data: {
			bid: bid,
			ballotStartTime: newStartTime
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot endTime to whatever is in the input field
 */
function setBallotEndTime() {
	var bid = $('#idHolder').attr("bid");
	var newEndTime = $("#endTimeInput").val();
	var newStartTime = document.getElementById("startTimeDate").textContent;
	var dateObj = new Date(newStartTime);

	//create element (input field) 
	var input = document.createElement("input");
	input.id = "startTimeInput";
	input.type = "datetime-local";

	//format the date and time
	var year = dateObj.getFullYear();
	var month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
	var day = dateObj.getDate().toString().padStart(2, '0');
	var hours = dateObj.getHours().toString().padStart(2, '0');
	var minutes = dateObj.getMinutes().toString().padStart(2, '0');

	//set required format: "YYYY-MM-DDTHH:MM"
	var formattedDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;

if(formattedDateTime>newEndTime){
 	newEndTime=formattedDateTime;
	displayMessage("ballot end time must be after start date",function(){window.location.reload()});
	
}
	$.ajax({
		url: "/acvote/ballot/ajax/setBallotEndTime",
		type: "PUT",
		data: {
			bid: bid,
			ballotEndTime: newEndTime
		},
		success: function(response) {
			if(newEndTime!=formattedDateTime)	
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
		
			console.log(textStatus + " " + xhr.status);
		}
	});
}

/**
 * Function will take change the ballot endTime to whatever is in the input field
 */
function setVotingGroup() {
	console.log("setting vote group");
	var bid = $('#idHolder').attr("bid");
	var dropdownSelection = document.getElementById("division").value;
	console.log(dropdownSelection);
	$.ajax({
		url: "/acvote/ballot/ajax/setVotingGroup",
		type: "PUT",
		data: {
			bid: bid,
			group: dropdownSelection
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

//the following group of methods will be for editing the values of the ballot

//method to change the Description of the ballot
function editDescription() {
	//this prevents multiple tables from being made
	description.onclick = null;

	var div = document.getElementById("description");
	var paragraph = document.getElementById("descriptionText");
	
	
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
			setBallotDescription($(paragraph).summernote('code'));
		}

	});
}


//method to change the Instructions of the ballot
function editInstructions() {
	//this prevents multiple tables from being made
	instructions.onclick = null;

	var div = document.getElementById("instructions");
	var paragraph = document.getElementById("instructionsText");

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
			setBallotInstructions($(paragraph).summernote('code'));
		}

	});
}


//method to change the Title of the ballot
function editTitle() {
	var div = document.getElementById("title");
	var paragraph = document.getElementById("titleText");
	var text = paragraph.textContent;

	//create element (input field) 
	var input = document.createElement("input");
	input.id = "titleInput";
	input.type = "text";
	input.value = text;
	input.maxLength = 50;
	input.style="width:100%"

	//switch text with new element
	div.replaceChild(input, paragraph);

	//set focus on the new element
	input.focus();
	input.select();

	// Attach an event listener to save changes when the input loses focus
	input.addEventListener("blur", setBallotTitle);
}

//method to change the typeOfVote of the ballot
function editTypeOfVote() {	
	console.log("setting vote group");
	var bid = $('#idHolder').attr("bid");
	var dropdownSelection = document.getElementById("typeOfVoteSelect").value;
	console.log(dropdownSelection);
	$.ajax({
		url: "/acvote/ballot/ajax/setBallotTypeOfVote",
		type: "PUT",
		data: {
			bid: bid,
			ballotTypeOfVote: dropdownSelection
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});
}

//method to change the outcomes of the ballot
function editOutcomes() {
	var div = document.getElementById("outcomes");
	var paragraph = document.getElementById("outcomesNum");
	var num = parseInt(paragraph.textContent);

	//create element (input field) 
	var input = document.createElement("input");
	input.id = "outcomesInput";
	input.type = "number";
	input.value = num;
	
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
	input.addEventListener("blur", setBallotOutcomes);
}

//method to change the startTime of the ballot
function editStartTime() {
	var div = document.getElementById("startTime");
	var paragraph = document.getElementById("startTimeDate");
	var date = paragraph.innerText;
	var dateObj = new Date(date);

	//create element (input field) 
	var input = document.createElement("input");
	input.id = "startTimeInput";
	input.type = "datetime-local";

	//format the date and time
	var year = dateObj.getFullYear();
	var month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
	var day = dateObj.getDate().toString().padStart(2, '0');
	var hours = dateObj.getHours().toString().padStart(2, '0');
	var minutes = dateObj.getMinutes().toString().padStart(2, '0');

	//set required format: "YYYY-MM-DDTHH:MM"
	var formattedDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;

	//set the value of the input field
	input.value = formattedDateTime;

	//switch text with new element
	div.replaceChild(input, paragraph);

	//set focus on the new element
	input.focus();
	input.select();

	// Attach an event listener to save changes when the input loses focus
	input.addEventListener("blur", setBallotStartTime);
}

//method to change the endTime of the ballot
function editEndTime() {
	var div = document.getElementById("endTime");
	var paragraph = document.getElementById("endTimeDate");
	var date = paragraph.innerText;
	var dateObj = new Date(date);


	//create element (input field) 
	var input = document.createElement("input");
	input.id = "endTimeInput";
	input.type = "datetime-local";

	//format the date and time
	var year = dateObj.getFullYear();
	var month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
	var day = dateObj.getDate().toString().padStart(2, '0');
	var hours = dateObj.getHours().toString().padStart(2, '0');
	var minutes = dateObj.getMinutes().toString().padStart(2, '0');

	//set required format: "YYYY-MM-DDTHH:MM"
	var formattedDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;

	//set the value of the input field
	input.value = formattedDateTime;

	//switch text with new element
	div.replaceChild(input, paragraph);

	//set focus on the new element
	input.focus();
	input.select();

	// Attach an event listener to save changes when the input loses focus
	input.addEventListener("blur", setBallotEndTime);
}


function setOptionEnabled(oid) {

	var bid = $('#idHolder').attr("bid");
	var enabled = document.getElementById("op_" + oid).checked;

	$.ajax({

		url: "/acvote/ballot/ajax/enableOption",
		type: "PUT",
		data: {
			bid: bid,
			oid: oid,
			state: enabled
		},
		success: function(response) {
			console.log(response);
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log(textStatus + " " + xhr.status);
		}
	});

}

function filterDivision() {
	
	var div = $("#facDiv").val();
	
	console.log(div);
	console.log(div.localeCompare("no filter"));
	
	var table = $('#candidateOptions').DataTable();
	
	if(div.localeCompare("no filter")==0){
		table.column(5).search("").draw();
	}
	else{
		table.column(5).search(div).draw();
	}
}

function filterRank() {
	
	var ran = $("#facRan").val();
	
	console.log(ran);
	console.log(ran.localeCompare("no filter"));
	
	var table = $('#candidateOptions').DataTable();
	
	if(ran.localeCompare("no filter")==0){
		table.column(6).search("").draw();
	}
	else{
		table.column(6).search(ran).draw();
	}
}

function filterTenure() {
	
	var ten = $("#facTen").val();
	
	console.log(ten);
	console.log(ten.localeCompare("no filter"));
	
	var table = $('#candidateOptions').DataTable();
	
	if(ten.localeCompare("no filter")==0){
		table.column(7).search("").draw();
	}
	else if(ten.localeCompare("T")==0){
		table.column(7).search("^T$", true, true, true).draw();
	}
	else if(ten.localeCompare("NA")==0){
		table.column(7).search("[-]", true, true, true).draw();
	}
	else{
		table.column(7).search(ten).draw();
	}
}

function selectAllCandidates() {
	var table = $('#candidateOptions').DataTable();
	
	table.rows({filter : 'applied'}).select();
}

function deselectAllCandidates() {
	var table = $('#candidateOptions').DataTable();
	
	table.rows().deselect();
}

function createTemplateFromBallot(bid){


	$.ajax({
		url: "/acvote/template/ajax/createTemplateFromBallot",
		type: "get",
		data: {
			bid: bid
		},
		success: function(response) {

		},
		error: function(xhr, textStatus, errorThrown) {

		}
	});
}

function startVoteForBallot(bid)
{
	$.ajax({
		url: "/acvote/ballot/ajax/startVote",
		type: "POST",
		data: {
			bid: bid
		},
		success: function(response) {
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {

		}
	});
}

function viewResults(bid) {
	window.location.assign("/acvote/ballot/"+ bid +"/results")
}

function remindVoters()
{
	var bid = $(this).attr("data-bid");
	var message = $("#remindVotersMessage").summernote('code');
	
	var faculty = [];
	var tokens = [];
	var table = $('#votingFacultyTable').DataTable();

	table.rows({ selected: true }).every(function() {
		var data = this.data();
		faculty.push(data[0]);
	});
	
	console.log(faculty);
	
	$.ajax({
		url: "/acvote/ballot/ajax/remindVoters",
		type: "POST",
		data: {
			bid: bid,
			facIds: faculty,
			body: message
		},
		success: function(response) {
			postMessageAndReload(response);
		},
		error: function(xhr, textStatus, errorThrown) {

		}
	});
}

function fillRemindVotersModal(bid) {

	console.log(bid);

	$("#btnRemindVotersSend").attr("data-bid", bid);
	
	var paragraph = document.getElementById("remindVotersMessage");

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

				// Select the text inside the Summernote editor
				var editor = $(paragraph).siblings('.note-editor').find('.note-editable');
				var range = document.createRange();
				range.selectNodeContents(editor[0]);
				var sel = window.getSelection();
				sel.removeAllRanges();
				sel.addRange(range);
			}

		}
	});
	
	$.ajax({
		url: "/acvote/ballot/ajax/tokenToFaculty",
		type: "get",
		data: {
			bid: bid
		},
		success: function(response) {

				console.log(response);
				
				const table = $('#votingFacultyTable').DataTable();

		table.clear().draw();

      response.forEach(item => {
         table.row.add( [
			 item.acId,
			 item.lastName,
			 item.firstName,
			 item.dept,
			 item.div,
			 item.rank,
			 item.email,
			 item.tenure,
			 item.voting,
			 item.active
		 ]).draw();
      });

		},
		error: function(xhr, textStatus, errorThrown) {

		}
	});
	
	
}

function selectRemindAllVoters() {
	var table = $('#votingFacultyTable').DataTable();
	
	table.rows({filter : 'applied'}).select();
}

function deselectRemindAllVoters() {
	var table = $('#votingFacultyTable').DataTable();
	
	table.rows().deselect();
}

