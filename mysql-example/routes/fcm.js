var express = require('express');
var router = express.Router();
const mysql = require('mysql');
const admin = require('firebase-admin');


let serAccount = require('/Users/taehoonlee//nodejs-556bd-firebase-adminsdk-4rvpy-8100fd94bb.json');

admin.initializeApp({
	credential: admin.credential.cert(serAccount),
});

function getResult3(sql, userName, roomName) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, [userName, roomName], function(err, result) {
			if(err) { reject(err) }
			else { resolve(result) }
		})
	})
}

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

module.exports = router;