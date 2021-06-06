let id=0
function addResult(title, url) {
	var div = document.createElement('div');
	let element = `\n<div class="card" id="res${id}">
	<div class="card-title">
	<h2>
	${title}
	<small>${url}</small>
	</h2>
	</div>
	</div>`;
	div.innerHTML = element;

	document.querySelector("#cardsarr").appendChild(div);
	
	document.querySelector(`#res${id}`).onclick = () => {
		window.location = `${url}`;
	}
	id++;
}
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Doi_(identifier)");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Computer_science");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Film_speed");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/International_Organization_for_Standardization");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/ISBN_(identifier)");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/ISO_metric_screw_thread");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Prolog");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/S2CID_(identifier)");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Algorithm");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Game_theory");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/International_Standard_Book_Number");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/ISO_19092-1");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/ISO/IEC_27002");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Meta-Object_Facility");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/On-board_diagnostics");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/OSI_model");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Probability_theory");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/Shoe_size");
addResult("en.wikipedia", "https://en.wikipedia.org/wiki/XML_Metadata_Interchange");