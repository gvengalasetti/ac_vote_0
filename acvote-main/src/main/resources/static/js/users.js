
/*
Deletes User object from database
*/
function deleteUser() {
	var uid = $(this).attr("uid");

	$.ajax({
		url: "/acvote/users/ajax/deleteUser",
		type: "DELETE",
		data: {
			uid: uid
		},
		success: function(response) {
			$("#btnDeleteUser").hide();
			displayMessage("Deleted User \"" + uid + "\"", function(){window.location.reload()});
			postMessageAndReload(response);
		
		},
		error: function(xhr, textStatus, errorThrown) {
			displayMessage("unable to delete user; " + xhr.status);
		}
	});
}

function deleteUserFromList(uid) {
	
	console.log(uid);
	
	$("#btnDeleteUser").attr("uid", uid);
	$("#fillInTitle").html("Do You Really Want to Delete User \"" + uid + "\"?");
}

/*
Creates new User object and adds to the database
*/
function createUser() {
	
	var uid = $("#uidInput").val();
	var role = $("#roleSelect").val();
	
	$.ajax({
		url: "/acvote/users/ajax/createUser",
		type: "POST",
		data: {
			uid: uid,
			role: role
		},
		success: function(response) {
			
			var alertBox = $("#alertBox");
			var alert = $("#alert-content");
			
			if(response.localeCompare("success") == 0){
				$("#createModal").modal("hide");
				postMessageAndReload("Created New User '" + uid + "'");
			}
			else if(response.localeCompare("null uid") == 0){
				alert.html("Missing user ID");
				alertBox.attr("hidden", false);
				
			}
			else if(response.localeCompare("duplicate uid") == 0){
				alert.html("Duplicate user ID");
				alertBox.attr("hidden", false);
			}
			else{
				window.location.assign("/error");
			}
		},
		error: function(xhr, textStatus, errorThrown) {
			displayMessage("unable to delete user; " + xhr.status);
		}
	});
}