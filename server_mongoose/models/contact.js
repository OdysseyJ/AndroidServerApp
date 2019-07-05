var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var contactSchema = new Schema({
    name: String,
    phonenum: String,
    photo: String
});

module.exports = mongoose.model('contact', contactSchema);
