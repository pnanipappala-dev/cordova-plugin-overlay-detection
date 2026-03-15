var exec = require('cordova/exec');

var OverlayDetection = {
    isOverlayEnabled: function(success, error) {
        exec(function(result) {
            success(result);
        }, error, "OverlayDetectionPlugin", "isOverlayEnabled", []);
    }
};

module.exports = OverlayDetection;
