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

function getResult4(sql, message, timeStamp, index, roomName) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, [message, timeStamp, index, roomName], function(err, result) {
			if(err) { reject(erR) }
			else { resolve(result) }
		})
	})
}

router.get('/chat/:roomName', function(req, res, next) {
	var roomName = req.params.roomName;
	var userName = req.query.userName;

	if(roomName.includes('-')) {
		var opponentName = roomName.substring(userName.length+1);
		var roomName2 = opponentName + '-' + userName;
		console.log(roomName + ", " + roomName2);
		client.query("SELECT * FROM Message WHERE chatRoomName = ? or chatRoomName = ?", [roomName, roomName2], function(err, rows, fields) {
			if(err) { console.log(err); }
			else {
				let obj = new Object();
				let arr = [];
				for(let i = 0; i < rows.length; i++) {
					let o = new Object();
					o.sender = rows[i].sender;
					o.message = rows[i].message;
					o.timeStamp = rows[i].timeStamp;
					o.category = rows[i].category;
					o.url = rows[i].url;
					arr.push(o);
				}
				obj.messages = arr;
				res.json(obj);
			}
		});
	}
	else {
    	client.query("SELECT * FROM Message WHERE Message.chatRoomName = ?", [roomName], function(err, rows, fields) {
        	if(err) {
           		console.log(err);
        	}
        	else {
           		let obj = new Object();
           		let arr = [];
           		for(let i = 0; i < rows.length; i++) {
               		let o = new Object();
               		o.sender = rows[i].sender;
               		o.message = rows[i].message;
               		o.timeStamp = rows[i].timeStamp;
			o.category = rows[i].category;
					o.url = rows[i].url;
               		arr.push(o);
           		}
           		obj.messages = arr;
        	   	res.json(obj);
    	    }
	    });
	}	
});
	
router.post('/chat', function(req, res, next) {
	var body = req.body;

	pool.query("INSERT INTO Message(sender, message, timeStamp, chatRoomName) VALUES(?, ?, ?, ?)", [body.sender, body.message, body.timeStamp, body.roomName], async function(err, rows, fields) {
		if(err) { console.log(err); }
		else {
			var query = "UPDATE chatRoom SET topMessage=?, topTimeStamp=?, topIndex=? where name=?"
			var queryRes = await getResult4(query, body.message, body.timeStamp, rows.insertId, body.roomName);
			res.send('{"code": 1, "msg": "successed"}');
		}
	});
});

module.exports = router;