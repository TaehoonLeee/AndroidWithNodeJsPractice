var express = require('express');
var router = express.Router();
var multer = require('multer');
var fs = require('fs');
var cloudinary = require('cloudinary').v2;
const mysql = require('mysql');
const admin = require('firebase-admin');
const path = require('path');

cloudinary.config({
	cloud_name: 'dnj8sgghi',
	api_key: '331428219932199',
	api_secret: 'X8e1GGcwX0Ya3_u0ahfpJPn72qA'
});

const imageUpload = multer({
	storage: multer.diskStorage({
		destination: (req, file, cb) => {
			cb(null, `${__dirname}/../public/images`);
		},
		filename: (req, file, cb) => {
			cb(null, file.originalname);
		},
	}),
});

let serAccount = require('/Users/taehoonlee//nodejs-556bd-firebase-adminsdk-4rvpy-8100fd94bb.json');

admin.initializeApp({
	credential: admin.credential.cert(serAccount),
});

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

function getResult5(sql, sender, timeStamp, chatRoomName, category, url) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, [sender, timeStamp, chatRoomName, category, url], function(err, result) {
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
                //console.log(result);
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
						console.log(queryRes[0].memberNumber);
						o.memberNumber = queryRes[0].memberNumber;
						arr.push(o);
				}
                obj.chatRoomList = arr;
                //console.log(obj);
                res.json(obj);
        });
});

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
				//client.query("INSERT INTO chatRoomJoin(userName, roomName) VALUES(?, ?)", [body.userName, body.roomName], function(err, rows, fields) {
				//	if(err) { console.log(err); }
				//res.send('{"code":1, "msg":"successed"}');
				//});
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

router.get('/fcmsend', async function(req, res, next) {
	var sender = req.query.sender;
	var sender_message = req.query.message;
	var roomName = req.query.roomName;

	var query = "SELECT name, token FROM Person WHERE name=(SELECT userName FROM chatRoomJoin WHERE roomName=? AND userName<>?)";
	var queryRes = await getResult3(query, roomName, sender);
	var target_token = queryRes[0].token;
	var userName = queryRes[0].name; 
	
	let message = {
		data: {
			sender: sender,
			message: sender_message,
			roomName: roomName,
			userName: userName,
		},
		token: target_token,
	}

	admin
		.messaging()
		.send(message)
		.then(function(response) {
			console.log('Successfully send the message : ', response);
		})
		.catch(function(err) {
			console.log('Error to send the message : ', err);
		})
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

router.post('/uploadImage', imageUpload.single('img'), function(req, res) {
	console.log(req.body);
	var sender = req.body.sender;
	var roomName = req.body.roomName;
	var timeStamp = req.body.timeStamp;

	var url = "";

	cloudinary.uploader.upload(req.file.path, async (error, result) => {
		url = result.url;
		
		var query = "INSERT INTO Message(sender, message, timeStamp, chatRoomName, category, url) VALUES(?, 'image', ?, ?, ?, ?)";
		var queryRes = await getResult5(query, sender, timeStamp, roomName, 'img', url);
		
		query = "UPDATE chatRoom SET topMessage=?, topTimeStamp=?, topIndex=? where name=?"
		await getResult4(query, 'image', timeStamp, queryRes.insertId, roomName);
		console.log(result, error);
		
		res.send('{"code":1, "msg":"successed"}');
	});
});

module.exports = router;
