function timeConverter(timestamp){
  var a = new Date(timestamp);
  var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  var year = a.getFullYear();
  var month = months[a.getMonth()];
  var date = a.getDate();
  var hour = a.getHours();
  var min = a.getMinutes();
  var sec = a.getSeconds();
  var time = date + ' ' + month + ' ' + year + ' ' + pad(hour) + ':' + pad(min) + ':' + pad(sec) ;
  return time;
}

function pad(d) {
    return (d < 10) ? '0' + d.toString() : d.toString();
}

/*
 * Parses for any elements with the class "unixTime" and converts it to a human-readable format
 * 
 * This is built for ballots, so we can store their start and end times as simple integers, and 
 * display them as full dates easily
 */
window.onload = function() {
	var theTimes = document.getElementsByClassName("dateTime");
	console.log(theTimes)

	for (var i = 0; i < theTimes.length; i++) {
    	theTimes[i].innerHTML = timeConverter(theTimes[i].innerHTML.replaceAll(",", ""));

	}
};

