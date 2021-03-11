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

function getResult(sql, name) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, name, function(err, result) {
			if(err) { reject(err) }
			else { resolve(result) }
		})
	})
}

function getResult2(sql, name, access, message, timeStamp) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, [name, access, message, timeStamp], function(err, result) {
			if(err) { reject(err) }
			else { resolve(result) }
		})
	})
}

function getResult3(sql, userName, roomName) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, [userName, roomName], function(err, result) {
			if(err) { reject(err) }
			else { resolve(result) }
		})
	})
}

function getResult4(sql, message, timeStamp, index, roomName) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, [message, timeStamp, index, roomName], function(err, result) {
			if(err) { reject(erR) }
			else { resolve(result) }
		})
	})
}

router.get('/chatList', function(req, res, next) {
	var body = req.query.name;
	let obj = new Object();

	var query = "select a.* from chatroom a where a.name in (select roomname from chatroomjoin where roomname not in (select roomname from chatroomjoin where username=?)) and a.access='public'";
	pool.query(query, [body], async function(err, rows, fields) {
		if(err) { console.log(err); }
		
		let arr = [];
		for(let i = 0; i < rows.length; i++) {
			let o = new Object();
			console.log(rows[i]);
			o.name = rows[i].name;
			o.access = rows[i].access;
			o.topMessage = rows[i].topMessage;
			o.topTimeStamp = rows[i].topTimeStamp;
			o.topIndex = rows[i].topIndex;
			
			var roomName = rows[i].name;
			var query = "SELECT COUNT(roomName) AS memberNumber FROM chatRoomJoin WHERE roomName =? GROUP BY roomName";
			var queryRes = await getResult(query, roomName);
			o.memberNumber = queryRes[0].memberNumber;
			arr.push(o);
		}
		obj.chatRoomList = arr;
		console.log(obj);
		res.json(obj);
	});
});

module.exports = router;
