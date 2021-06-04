import * as mysql from 'mysql';
import { stemmer } from 'stemmer';
import * as fs from 'fs';
import express from 'express';


var con = mysql.createConnection({
	host: 'localhost',
	user: 'root',
	password: 'ssmk65108',
	database: 'searchengine',
});

var app = express();

app.use(express.json({ limit: '1mb' }));

app.use(express.static('./'));

var server = app.listen(8000, () => console.log('listening...'));

app.get('/', (request, response) => {
	(async (request, response) => {
		try {
			response.sendFile('./main/index.html', { root: './' });
		} catch (error) {
			console.log(error);
		}
	})(request, response);
});

con.connect(function (err) {
	if (err) throw err;
	console.log('Connected!');
});
app.post('/word', (request, response) => {
	(async (request, response) => {
		try {
			let recivedData = request.body;
			let word = recivedData.word;
			fs.readFile('./results/baseScrip.js', function (err, data) {
				if (err) throw err;
				//================================================
				let urlArr = [];
				let title = [];
				var sql = `SELECT D.*
				FROM
				WORDS_IN_DOCS WID,
				WORDS W,
				DOCUMENTS D
				
				WHERE 
				W._ID = WID.WORD_ID AND
				D.__ID = WID.DOC_ID
				AND
				W.WORD = "${stemmer(word)}"`;
				console.log(`Word:${word}, Stemmed:${stemmer(word)}\nUrls:`);
				con.query(sql, function (error, result, fields) {
					if (error) throw error;
					if (err) throw err;
					for (let i = 0; i < result.length; i++) {
						let tempStr = fs.readFileSync(
							`../indexedPages/${result[i].URL.substring(
								17,
								result[i].URL.length
							)}`
						); //Could be wrong
						let fileContent = tempStr.toString();
						urlArr[i] = '';
						let j = 0;
						while (fileContent[j] != '\n') {
							urlArr[i] += fileContent[j];
							j++;
						}
						urlArr[i] = urlArr[i].substring(
							1,
							urlArr[i].length - 1
						);
						console.log(urlArr[i]);
						let expr =
							/https?:\/\/(www\.)?([-a-zA-Z0-9@:%._\+~#=]{1,256})\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)/g;
						title[i] = expr.exec(urlArr[i]);
						title[i] = title[i][2];
					}
					for (let i = 0; i < result.length; i++) {
						data += addUrlResult(title[i], urlArr[i]);
					}
					//===============================
					fs.writeFile('./results/script.js', data, function (err) {
						if (err) throw err;
						let dataToSend = { status: 1 };
						response.send(dataToSend);
					});
				});
			});
		} catch (error) {
			console.log(error);
		}
	})(request, response);
});

function addUrlResult(title, url) {
	return `\naddResult("${title}", "${url}");`;
}
