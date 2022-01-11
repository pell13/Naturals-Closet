//mongo db model -> mvc model based
//mongoose object declaration
var mongoose = require("mongoose");
var Schema = mongoose.Schema;

var userSchema = new Schema({
  email: {
    type: String,
    required: true,
    unique: true
  },
  password: {
    type: String,
    required: true
  },
  name: {
    type: String,
    required: true,
    unique: true
  },
  profile: {
    type: String,
    required: true,
  }
});

module.exports = mongoose.model('user', userSchema);