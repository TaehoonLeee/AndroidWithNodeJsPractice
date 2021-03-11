var express = require('express');
var router = express.Router();
const mysql = require('mysql');
const path = require('path');

let client = mysql.createConnection({
	user:"root",
	password:"gksmf92",
	database:"my_db"
})

let pool = mysql.createPool({
	user:"root",
	password:"gksmf92",
	database:"my_db"
})


	
router.post('/createmember', function(req, res, next) {
	var body = req.body;

	client.query("INSERT INTO Person VALUES(?)", [body.userName], function(err, rows, fields) {
		if(err) { console.log(err); }
	});
});

router.get('/friends/:userName', function(req, res, next) {
	var name = req.params.userName;
	client.query("SELECT friend FROM Relationship WHERE me = ?", [name], function(err, rows, fields) {
		if(err) { console.log(err); }
		else {
			let obj = new Object();
			let arr = [];
			for(let i = 0; i < rows.length; i++) {
				let o = new Object();
				o.name = rows[i].friend;
				arr.push(o);
			}
			obj.friends = arr;
			res.json(obj);
		}
	});
});

router.get('/friendcandidate', function(req, res, next) {
	var name = req.query.userName;
	var regex = req.query.regex;
	client.query("SELECT name FROM Person WHERE name LIKE ? AND name NOT IN (SELECT friend FROM Relationship WHERE me = ?) AND name <> ?", [regex, name, name], function(err, rows, fields) {
		if(err) { console.log(err); }
		else {
			let obj = new Object();
			let arr = [];
			for(let i = 0; i < rows.length; i++) {
				let o = new Object();
				o.name = rows[i].name;
				arr.push(o);
			}
			obj.friends = arr;
			res.json(obj);
		}
	});
});

router.post('/friendadd', function(req, res, next) {
	var body = req.body;

	client.query("INSERT INTO Relationship VALUES(?, ?)", [body.myName, body.userName], function(err, rows, fields) {
		if(err) { console.log(err); }
	});

	res.send('{"code":1, "msg":"successed"}');	
});

router.post('/exit', function(req, res, next) {
	var body = req.body;

	client.query("DELETE FROM chatRoomJoin WHERE userName=? AND roomName=?", [body.userName, body.roomName], function(err, rows, fields) {
		if(err) { console.log(err); }
		else {
			res.send('{"code":1, "msg":"successed"}');
		}
	});
});

module.exports = router;