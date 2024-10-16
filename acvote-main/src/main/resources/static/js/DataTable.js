$(document).ready(function() {
	$('.dataTable').DataTable();
	$('.ballotTable').DataTable({
		order: [[2, 'desc']]
		// TODO sort progress bar by asc
	});
	$('.selectDataTable').DataTable({
		paging: false,
		columnDefs: [{
			orderable: false,
			className: 'select-checkbox',
			targets: 0
		}],
		select: {
			style: 'multi',
			selector: 'td:first-child'
		},
		order: [[1, 'asc']]
	});
	$('.singleSelectDataTable').DataTable({
		paging: false,
		columnDefs: [{
			orderable: true,
			targets: 0
		}],
		select: {
			style: 'single',
		},
		order: [[1, 'asc']]
	});
	$('.multiSelectDataTable').DataTable({
		paging: false,
		columnDefs: [{
			orderable: true,
			targets: 0
		}],
		select: {
			style: 'multi'
		},
		order: [[1, 'asc']]
	});
	$('.voterSelectDataTable').DataTable({
		paging: false,
		columnDefs: [{
			orderable: false,
			className: 'select-checkbox',
			targets: 0
		}],
		select: {
			style: 'multi',
			selector: 'td:first-child'
		}
	});
});


// In addtion to creating a table, call this in the class tag to make the table a DataTable

// Reference for more information: datatables.net