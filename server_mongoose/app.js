// app.js

// [LOAD PACKAGES]
var express     = require('express');
var app         = express();
var bodyParser  = require('body-parser');
var mongoose    = require('mongoose');
var socketio = require('socket.io');

// DEFINE MODEL
var Contact = require('./models/contact');
var Photo = require('./models/photo');

// [CONFIGURE APP TO USE bodyParser]
app.use(bodyParser.urlencoded({limit: '10mb', extended: true }));
app.use(bodyParser.json({limit: '10mb'}));

// [CONFIGURE SERVER PORT]
var port = process.env.PORT || 8080;

// [CONFIGURE ROUTER]
app.use('/api/contact', function(req, res, next){
  require('./routes/index')(app, Contact);
  next();
});
app.use('/api/gallery', function(req, res, next){
  require('./routes/gallery')(app, Photo);
  next();
});
//var router = require('./routes')(app, Contact)

// [ CONFIGURE mongoose ]
// CONNECT TO MONGODB SERVER
var db = mongoose.connection;
db.on('error', console.error);
db.once('open', function(){
    // CONNECTED TO MONGODB SERVER
    console.log("Connected to mongod server");
});

mongoose.connect('mongodb://localhost/mongodb_tutorial', { useNewUrlParser: true}, function(err, client){
  if(err){
    console.log("Unable mongoose");
  }
  else{
  }
});

// [RUN SERVER]
var server = app.listen(port, function(){
 console.log("Express server has started on port " + port)
});

// 소켓 서버를 생성한다. 안드로이드통신용.
var io = socketio.listen(server);
io.sockets.on('connection', function (socket){
    console.log('Socket ID : ' + socket.id + ', Connect');
    socket.on('clientMessage', function(data){
        console.log('Client Message : ' + data);

        var message = {
            msg : 'server',
            data : 'data'
        };
        socket.emit('serverMessage', message);
    });
});
