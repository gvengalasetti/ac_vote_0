function adjustFaculty() {
	var acId = $("#acIdBox").text().trim();
	var nameBox = $("#nameBox")
	var spaceIndex = nameBox.text().indexOf(" ");
	//this is the max length assigned for both names
	var maxNameLength = 20;
	
	//get the smallest length either the current length or max length
	var endFirstName = Math.min(spaceIndex, maxNameLength);
	//set first name to go to the smallest length
	var fName = nameBox.text().substring(0, endFirstName);
	
	//last name starts one index after space
	var startLastName = spaceIndex + 1;
	//get the smallest length of last name and set the lName to that string
	var endLastName = Math.min(nameBox.text().substring(startLastName).length, nameBox.text().substring(startLastName, startLastName + maxNameLength).length);
	var lName = nameBox.text().substring(startLastName, startLastName + endLastName);
	
	//set max department length
	var deptMax = 5;
	var dept;
	if($("#deptBox").text().length > deptMax) //if longer than max
	{
		dept = $("#deptBox").text().substring(0, deptMax); //end string at max
	}
	else
	{
		dept = $("#deptBox").text(); //get whole string
	}
	
	var div = $("#divBox").val();
	var rank = $("#rankBox").val();
	
	//set max email length
	var emailMax = 50;
	var email;
	if(document.getElementById('emailBox').textContent.length > emailMax) //if longer than max
	{
		email = document.getElementById('emailBox').textContent.substring(0, emailMax); // Set string to sub string of email
	}
	else
	{
		email = document.getElementById('emailBox').textContent; //get whole string
	}
	
	var tenure = $("#tenureBox").val();
	var voting = $("#votingStatusBox").val() === "true";
	var active = $("#activeStatusBox").val() === "true";
	if (tenure.localeCompare("NA") == 0) {
		tenure = null;

	}

	$.ajax({
		url: "/acvote/faculty/ajax/editfaculty",
		type: "PUT",
		data: {
			acId: acId,
			firstName: fName,
			lastName: lName,
			dept: dept,
			div: div,
			rank: rank,
			email: email,
			tenure: tenure,
			voting: voting,
			active: active
		},
		success: function() {
			console.log(acId);
			postMessageAndReload("faculty updated");
		},
		error: function(xhr, textStatus) {

			$("#dialog-contents").html(textStatus + " " + xhr.status);
		}
	});
	//sessionStorage.setItem("message", "edit saved");

}

/*
creates a new dummy faculty and moves to edit page to allow user to customize
*/
function createFaculty() {
	var acId = $("#facultyIdInput").val();
	$.ajax({
		url: "/acvote/faculty/ajax/create",
		type: "POST",
		data: {
			acId: acId
		},
		success: function(acId) {
		alert
			window.location.assign("/acvote/faculty/details?acId=" + acId);
		},
		error: function(xhr, textStatus, errorThrown) {
			displayMessage(" DuplicateFaculty", function() { window.location.assign("/acvote/faculty/details?acId=" + $("#facultyIdInput").val()); });			
		}
	})
}

/*
Used to transform user's input is all numeric digits required
for ac ids.
*/
function onInput(event){
// Get the current value of the input field
	const currentValue = event.target.value;

	// Remove any non-numeric characters from the input
	const numericValue = currentValue.replace(/[^0-9]/g, "");

	// Update the input field with the numeric value
	event.target.value = numericValue;

	// Ensure the numeric value is positive (greater than or equal to 0)
	if (numericValue <= 0) {
		event.target.value = "";
	}
}


/* 
Function to delete faculty from detailed view or list page after confirmation dialog
*/
function deleteFaculty() {
	var id = $(this).attr("data-acId");

	console.log(id);

	$.ajax({
		url: "/acvote/faculty/ajax/delete",
		type: "DELETE",
		data: {
			acId: id
		},
		success: function(response) {

			if ( $( "#facultyTable" ).length ) {
 				postMessageAndReload(response);  // on list page, show message only
    		} else {
				window.location.assign("/acvote/faculty/list")  // on details page, return to list page
			}

		},
		error: function(xhr, textStatus, errorThrown) {
			$("#confirm-contents").html(textStatus + " " + xhr.status);
		}
	});
}

/* 
Function to delete faculty from regular list view by rigging the confirmation
dialog with the faculty ac id. 
*/
function deleteFromList(acId) {

	console.log(acId);

	$("#btnDeleteFaculty").attr("data-acId", acId);
	$("#fillInTitle").html("Do You Really Want to Delete Faculty #" + acId + "?");
}
