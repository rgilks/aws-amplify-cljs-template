// WARNING: DO NOT EDIT!
// THIS FILE WAS GENERATED BY SHADOW-CLJS AND WILL BE OVERWRITTEN!

var ALL = {};
ALL["@mui/material/styles"] = require("@mui/material/styles");
ALL["react-ios-pwa-prompt"] = require("react-ios-pwa-prompt");
ALL["aws-exports"] = require("aws-exports");
ALL["@aws-amplify/ui-react"] = require("@aws-amplify/ui-react");
ALL["aws-amplify"] = require("aws-amplify");
ALL["@mui/material"] = require("@mui/material");
ALL["react-dom"] = require("react-dom");
ALL["models"] = require("models");
ALL["react"] = require("react");
ALL["howler"] = require("howler");
ALL["react-div-100vh"] = require("react-div-100vh");
global.shadow$bridge = function shadow$bridge(name) {
  var ret = ALL[name];

  if (ret === undefined) {
     throw new Error("Dependency: " + name + " not provided by external JS. Do you maybe need a recompile?");
  }

  return ret;
};

shadow$bridge.ALL = ALL;
