var express = require('express');
var router = express.Router();
var multer = require('multer');
var fs = require('fs');
var cloudinary = require('cloudinary').v2;
const mysql = require('mysql');

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

function getResult5(sql, sender, timeStamp, chatRoomName, category, url) {
	return new Promise(function(resolve, reject) {
		pool.query(sql, [sender, timeStamp, chatRoomName, category, url], function(err, result) {
			if(err) { reject(err) }
			else { resolve(result) }
		})
	})
}

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