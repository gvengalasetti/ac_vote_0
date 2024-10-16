
/* 
Function to delete faculty from detailed list view.
*/
function deleteFaculty(){
	var id = $(this).attr("data-acId");
	
	console.log(id);
	
	$.ajax({
		url: "/acvote/faculty/ajax/delete",
		type: "get",
		data: {
			acId: id
		},
		success: function(response) {
			$("#btnDeleteFaculty").hide();
			window.location.assign("/acvote/faculty/list");
		},
		error: function(xhr, textStatus, errorThrown) {
			$("#confirm-contents").html(textStatus+" "+xhr.status);
		}
	});
}

/* 
Function to delete faculty from regular list view.
*/
function deleteFromList(acId){
	
	console.log(acId);
	
	$("#btnDeleteFaculty").attr("data-acId", acId);
	$("#fillInTitle").html("Do You Really Want to Delete Faculty #" + acId + "?");
}
