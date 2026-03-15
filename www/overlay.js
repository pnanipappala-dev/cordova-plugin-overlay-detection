var exec = require('cordova/exec');

var OverlayDetection = {
    isOverlayEnabled: function(success, error) {
        exec(function(result) {
            // result is now a JSON object
            success(result);
        }, error, "OverlayDetectionPlugin", "isOverlayEnabled", []);
    }
};

module.exports = OverlayDetection;
