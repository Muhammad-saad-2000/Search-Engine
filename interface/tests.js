var fs = require('fs');
var mysql = require('mysql');

var con = mysql.createConnection({
	host: "localhost",
	user: "root",
	password: "ssmk65108",
	database: "searchengine"
});
let fileArr=[];
con.connect(function(err) {
	if (err) throw err;
	console.log("Connected!");
	var sql = "SELECT * FROM Seeds";
	con.query(sql, function (err, result) {
		if (err) throw err;
		console.log(result[0]);
	});
});

let urlArr=[];
let title=[];
for(let i=0;i<fileArr.length;i++){
	let tempStr= fs.readFileSync(`../Web crawler/retrievedPages/${fileArr[i]}`);
	let fileContent=tempStr.toString();
	urlArr[i]="";
	let j=0;
	while(fileContent[j]!='\n'){
		urlArr[i]+=fileContent[j];
		j++;
	}
	let expr=/https?:\/\/(www\.)?([-a-zA-Z0-9@:%._\+~#=]{1,256})\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)/g;
	title[i]=expr.exec(urlArr[i]);
	title[i]=title[i][2];
}