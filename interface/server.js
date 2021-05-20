var mysql = require('mysql');
var fs = require('fs');

var con = mysql.createConnection({
	host: "localhost",
	user: "root",
	password: "ssmk65108",
	database: "searchengine"
});

var express = require('express');

var app = express();

app.use(express.json({ limit: '1mb' }));

app.use(express.static('./'));

var server = app.listen(8080, () => console.log('listening...'));

app.get('/', (request, response) => {
	(async (request, response) => {
		try {
			response.sendFile('./main/index.html', {root: __dirname })
		} catch (error) {
			console.log(error);
		}
	})(request, response);
});

app.post('/word', (request, response) => {
	(async (request, response) => {
		try {
			let recivedData = request.body;
			let word = recivedData.word;
			fs.readFile('./results/baseScrip.js', function(err, data) {
				if (err) throw err;
				//TODO:retrive and add the urls
				//==============================
				console.log(word);//use that word for qurey
				data += addUrlResult("Facebook", "https://facebook.com");
				data += addUrlResult("CNN", "https://edition.cnn.com");
				//===============================
				fs.writeFile('./results/script.js', data, function (err) {
					if (err) throw err;
					let dataToSend = { status: 1 };
 					response.send(dataToSend);
				});
			});
		} catch (error) {
			console.log(error);
		}
	})(request, response);
});

function addUrlResult(title, url) {
	return `\naddResult("${title}", "${url}");`
}

// app.get('/zag', (request, response) => {
// 	(async (request, response) => {
// 		try {
// 			let dataToSend = { Zag: 1 };
// 			response.send(dataToSend);
// 		} catch (error) {
// 			console.log(error);
// 		}
// 	})(request, response);
// });

// document.querySelector("#buscar").onclick = async () => {
//       let response = await fetch("http://127.0.0.1:8080/zag");
//       let resivedData = await response.json();
// }

// con.connect(function(err) {
// 	if (err) throw err;
// 	console.log("Connected!");
// 	var sql = "SELECT * FROM Seeds";
// 	con.query(sql, function (err, result) {
// 		if (err) throw err;
// 		console.log(result[0]);
// 	});
// });