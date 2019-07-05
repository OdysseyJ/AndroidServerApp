var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var photoSchema = new Schema({
    name: String,
    photo: String,
});

module.exports = mongoose.model('photo', photoSchema);
