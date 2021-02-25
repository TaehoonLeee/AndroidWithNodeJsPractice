var express = require('express');
var router = express.Router();
const mysql = require('mysql');

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

router.get('/chatList/:name', function(req, res, next) {
	var name = req.params.name;
	let obj = new Object();
	let query = "SELECT a.name FROM chatRoom a, chatRoomJoin b WHERE a.name = b.roomName and b.username = ?";
	let object = new Object();
	let arr = [];
	pool.query(query, [name], async function(err, result) {
		if(err) console.log(err);
		console.log(result);
		for(let i = 0; i < result.length; i++) {
			let o = new Object();
			o.name = result[i].name;

			var roomName = result[i].name;
			var query2 = "SELECT COUNT(roomName) AS memberNumber FROM chatRoomJoin WHERE roomName = ? GROUP BY roomName";
			var queryRes = await getResult(query2, roomName);
			console.log(queryRes[0].memberNumber);
			o.memberNumber = queryRes[0].memberNumber;
			arr.push(o);			
		}
		obj.chatRoomList = arr;
		console.log(obj);
		res.json(obj);
	});
});

router.get('/tmpchatList/:name', function(req, res, next) {
        var name = req.params.name;
        let obj = new Object();
        let query = "SELECT a.* FROM chatRoom a, chatRoomJoin b WHERE a.name = b.roomName and b.username = ?";
        let object = new Object();
        let arr = [];
        pool.query(query, [name], async function(err, result) {
                if(err) console.log(err);
                console.log(result);
                for(let i = 0; i < result.length; i++) {
                        let o = new Object();
                        o.name = result[i].name;
			o.access = result[i].access;
			o.topMessage = result[i].topMessage;
			o.topTimeStamp = result[i].topTimeStamp;

                        var roomName = result[i].name;
                        var query2 = "SELECT COUNT(roomName) AS memberNumber FROM chatRoomJoin WHERE roomName = ? GROUP BY roomName";
                        var queryRes = await getResult(query2, roomName);
                        console.log(queryRes[0].memberNumber);
                        o.memberNumber = queryRes[0].memberNumber;
                        arr.push(o);
                }
                obj.chatRoomList = arr;
                console.log(obj);
                res.json(obj);
        });
});

router.get('/chatList', function(req, res, next) {
	var body = req.query.name;
	let obj = new Object();

	pool.query("SELECT roomName FROM chatRoomJoin WHERE roomName NOT IN (SELECT roomName FROM chatRoomJoin WHERE userName = ?) GROUP BY roomName", [body], async function(err, rows, fields) {
		if(err) { console.log(err); }
		
		let arr = [];
		for(let i = 0; i < rows.length; i++) {
			let o = new Object();
			console.log(rows[i]);
			o.name = rows[i].roomName;
			
			var roomName = rows[i].roomName;
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
	
router.get('/chat/:roomName', function(req, res, next) {
	var name = req.params.roomName;
	client.query("SELECT * FROM Message WHERE Message.chatRoomName = ?", [name], function(err, rows, fields) {
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
				arr.push(o);
			}
			obj.messages = arr;
			console.log(arr);
			res.json(obj);
		}
	});
});
	
router.post('/chat', function(req, res, next) {
	var body = req.body;

	client.query("INSERT INTO Message VALUES(?, ?, ?, ?)", [body.sender, body.message, body.timeStamp, body.roomName], function(err, rows, fields) {
			if(err) {
				console.log(err);
			}
	});
	client.query("UPDATE chatRoom SET topMessage=?, topTimeStamp=? where name=?", [body.message, body.timeStamp, body.roomName], function(err, rows, fields) {
	});
	res.send('{"code" : 1, "msg" : "successed"}');
});

router.post('/enter', function(req, res, next) {
	var body = req.body;

	client.query("INSERT INTO chatRoomJoin(userName, roomName) VALUES(?, ?)", [body.userName, body.roomName], function(err, rows, fields) {
		if(err) { console.log(err); }
		res.send('{"code":1, "msg":"successed"}');
	});
});

router.post('/createroom', function(req, res, next) {
	var body = req.body;

	client.query("INSERT INTO chatRoom(name) VALUES(?)", [body.roomName], function(err, rows, fields) {
		if(err) { console.log("chatroom" + err); }
	});

	client.query("INSERT INTO chatRoomJoin(userName, roomName) VALUES(?, ?)", [body.userName, body.roomName], function(err, rows, fields) {
		if(err) { console.log("chatroomjoin" + err); }	
	});
	res.send('{"code":1, "msg":"successed"}');
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
			console.log(arr);
			res.json(obj);
		}
	});
});

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
			console.log(arr);
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
			console.log(arr);
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
		
module.exports = router;
