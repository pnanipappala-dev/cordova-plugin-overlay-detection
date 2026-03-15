var exec = require('cordova/exec');

exports.isOverlayEnabled = function(success, error) {
    exec(success, error, "OverlayDetectionPlugin", "isOverlayEnabled", []);
};
