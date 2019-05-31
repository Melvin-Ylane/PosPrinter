var exec = require('cordova/exec');

module.exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'PosPrinter', 'coolMethod', [arg0]);
};

module.exports.startPrint = function (arg0, success, error) {
    exec(success, error, 'PosPrinter', 'startPrint', [arg0]);
};
