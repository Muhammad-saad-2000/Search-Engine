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