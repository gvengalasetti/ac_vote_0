/*

Activates drag and drop on "sortable list" elements
Don't really know how it works, but it do, so I don't ask questions

*/
$(function() {
	
	//items in a "sortable-list" will be drag and droppable
	$("#sortable-list").sortable({
		connectWith: "#sortable-list",
		update: function(event, ui) {
			const rankedItems = $(".item-content").map(function() {
				return $(this).text();
			}).get();
			console.log("Ranked Items:", rankedItems);
		},
		axis: "y"
	}).disableSelection();
});

/*

Grabs users rankings from drag and drop ui and
organizes items in review modal in the same order

*/
function orderReviewItems() {

	//grabbing sortable list and review list
	var list = $(".item-text");
	var reviewList = $("#reviewList");

	//emptying review list, to be repopulated in proper order
	reviewList.empty();

	//repopulating review list with sortable list items
	for (let i = 0; i < list.length; i++) {
		var listItem = $.parseHTML("<li>" + list[i].innerHTML + "</li>");
		reviewList.append(listItem);
	}
}

/*

Casts a user's vote
Grabs bid, user's token, and user's rankings

*/
function castVote() {

	//grabbing things
	var bid = $(this).attr("bid");
	var list = $(".dropzone");
	var options = "";
	var token = $(this).attr("token");

	//appending oids to options, comma delimited
	for (let i = 0; i < list.length; i++) {
		options = options + list[i].getAttribute("oid").trim();
		if (i < list.length - 1) {
			options = options + ",";
		}
		console.log(options[i]);
	}

	$.ajax({
		url: "/acvote/voter/ajax/submitBallot",
		type: "POST",
		data: {
			bid: bid,
			token: token,
			options: options
		},
		success: function(response) {

			if (response.localeCompare("could not submit vote") == 0) {
				window.location.assign("/acvote/error");
			}
			else {
				//redirecting to "i voted" page
				displayMessage("ballot submitted", function() { window.location.assign("/acvote/voter/voted") });
			}

		},
		error: function(xhr, textStatus, errorThrown) {
			alert(textStatus + " " + xhr.status);
			window.location.assign("/acvote/error"); // TODO go to deny page
		}
	});
}

//Selects all options in filter modal
function selectAllCandidates() {

	var table = $('#optionTable').DataTable();

	table.rows().select();
}

//Selects all options in filter modal
function deselectAllCandidates() {
	
	var table = $('#optionTable').DataTable();

	table.rows().deselect();
}

/*

Called when voter is done filtering options on IRV2 ballots.
Grabs their selections, clears existing drag and drop items,
replaces them with voter's selections. Also ensures the review
modal only contains the voter's selected options.

*/
function filterCandidates() {

	var selectedOptions = [];
	var table = $("#optionTable").DataTable();
	
	//grabbing voter's selections
	table.rows({ selected: true }).every(function() {

		var data = this.data();
		selectedOptions.push(data);
	});

	//clearing drag and drop items, to be replaced
	var list = $("#sortable-list");
	list.empty();
	
	//clearing rank numbers, to be replaced
	var numbers = $("#rankNumbers");
	numbers.empty();

	//replacing with new drag and drop items and rank numbers
	for (let i = 1; i <= selectedOptions.length; i++) {

		var listItem = $.parseHTML("<li class=\"sort-item\" oid=\"" + selectedOptions[i-1][2] + "\"><div class=\"item-content border bg-light d-flex justify-content-between\"><div></div><p class=\"item-text m-0\">" + selectedOptions[i-1][1] + "</p><img src=\"/acvote/images/thumb-drag-icon.jpg\" style=\"width: 36px;\" /></div></li>");
		list.append(listItem);
		
		var rankNumber = $.parseHTML("<h1 class=\"item-number\">" + i + "</h1>")
		numbers.append(rankNumber);
	}

	//giving new items ids (helps with review modal)
	$(".sort-item").each(function(idx) {
		$(this).attr("id", idx + 1);
	});
	
}