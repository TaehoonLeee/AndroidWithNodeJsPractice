exports = module.exports = function(io) {
	io.on('connection', (socket) => {
		console.log("SOCKETIO connection EVENT: ", socket.id, " client connected");
		socket.on("event1", function(msg) {
			console.log("SOCKETIO event1 EVENT : ", msg);
		})
	})
};
