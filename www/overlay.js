var exec = require('cordova/exec');

var OverlayDetection = {
    isOverlayEnabled: function(success, error) {
        exec(success, error, "OverlayDetectionPlugin", "isOverlayEnabled", []);
    }
};

module.exports = OverlayDetection;
