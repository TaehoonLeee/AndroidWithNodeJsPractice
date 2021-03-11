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

router.get('/tmpchatList/:name', function(req, res, next) {
        var name = req.params.name;
        let obj = new Object();
        let query = "SELECT a.* FROM chatRoom a, chatRoomJoin b WHERE a.name = b.roomName and b.username = ?";
        let object = new Object();
        let arr = [];
        pool.query(query, [name], async function(err, result) {
                if(err) console.log(err);
                for(let i = 0; i < result.length; i++) {
						let o = new Object();
						o.name = result[i].name;
						o.access = result[i].access;
						o.topMessage = result[i].topMessage;
						o.topTimeStamp = result[i].topTimeStamp;
						o.topIndex = result[i].topIndex;

						var roomName = result[i].name;
						var query2 = "SELECT COUNT(roomName) AS memberNumber FROM chatRoomJoin WHERE roomName = ? GROUP BY roomName";
						var queryRes = await getResult(query2, roomName);
						o.memberNumber = queryRes[0].memberNumber;
						arr.push(o);
				}
                obj.chatRoomList = arr;
                res.json(obj);
        });
});

router.post('/enter', function(req, res, next) {
	var body = req.body;
	var myName = body.userName;
	var roomName = body.roomName;
	var opponentName = roomName.substring(myName.length+1);
	var roomName2 = opponentName + '-' + myName;
	
	pool.query("SELECT * FROM chatRoomJoin where (userName=? and roomName=?) or (username=? and roomname=?)", [body.userName, body.roomName, body.userName, roomName2], async function(err, rows, fields) {
		if(err) { console.log(err); }
		else {
			if(rows.length == 0) {
				var query = "INSERT INTO chatRoomJoin(userName, roomName) VALUES(?, ?)"
				var queryRes = await getResult3(query, body.userName, body.roomName);
			}
		}

		res.send('{"code": 1, "msg": "successed"}');
	});
});

router.post('/createroom', function(req, res, next) {
	var body = req.body;
	
	var myName = body.userName;
	var roomName = body.roomName;
	var opponentName = roomName.substring(myName.length+1);
	var roomName2 = opponentName + '-' + myName;		
	pool.query("SELECT * FROM chatRoomJoin where (userName=? and roomName=?) or (username=? and roomname=?)", [body.userName, body.roomName, body.userName, roomName2], async function(err, rows, fields) {
		if(err) { console.log(err); }
		else {
			if(rows.length == 0) {
				var query = "INSERT INTO chatRoom(name, access, topMessage, topTimeStamp) VALUES(?, ?, ?, ?)";
				var queryRes = await getResult2(query, body.roomName, body.access, "", "");
				
				query = "INSERT INTO chatRoomJoin(userName, roomName) VALUES(?, ?)";
				queryRes = await getResult3(query, body.userName, body.roomName);
				query = "INSERT INTO chatRoomJoin(userName, roomName) VALUES(?, ?)";
				queryRes = await getResult3(query, opponentName, roomName);
			} 
		}
		res.send('{"code":1, "msg":"successed"}');
	});

});

router.get('/chatmember/:roomName', function(req, res, next) {
	var name = req.params.roomName;
	client.query("SELECT userName FROM chatRoomJoin WHERE roomName = ?", [name], function(err, rows, fields) {
		if(err) { console.log(err); }
		else {
			let obj = new Object();
			let arr = [];
			for(let i = 0; i < rows.length; i++) {
				let o = new Object();
				o.name = rows[i].userName;
				arr.push(o);
			}
			obj.members = arr;
			res.json(obj);
		}
	});
});

module.exports = router;
