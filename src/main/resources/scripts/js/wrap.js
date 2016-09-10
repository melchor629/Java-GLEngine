function __exec_func_java_(name, args) {
    if(__eng.functionMap.containsKey(name)) {
        return __eng.wrapperFunctionCall(name, args);
    }
}

function require(moduleName) {
    if(__eng.moduleMap.containsKey(moduleName)) {
        var m = __eng.moduleMap.get(moduleName);
        m.moduleDidLoad();
        return m;
    } else throw new Error('Module not ' + moduleName + ' found');
}

var console = {
    log: function log(msg) {
        var l = [];
        for(var i = 1; i < arguments.length; i++) {
            l.push(arguments[i]);
        }
        java.lang.System.out.printf(msg + "\n", Java.to(l, Java.type("java.lang.Object[]")));
    },

    error: function error(msg) {
        var l = [];
        for(var i = 1; i < arguments.length; i++) {
            l.push(arguments[i]);
        }
        java.lang.System.out.printf(msg + "\n", Java.to(l, Java.type("java.lang.Object[]")));
    }
};

var gui = {
    Animation: org.melchor629.engine.gui.Animation,
    Box: org.melchor629.engine.gui.Box,
    Button: org.melchor629.engine.gui.Button,
    Color: org.melchor629.engine.gui.Color,
    Container: org.melchor629.engine.gui.Container,
    Frame: org.melchor629.engine.gui.Frame,
    Gradient: org.melchor629.engine.gui.Gradient,
    GUIDrawUtils: org.melchor629.engine.gui.GUIDrawUtils,
    Image: org.melchor629.engine.gui.Image,
    MouseEvent: org.melchor629.engine.gui.MouseEvent,
    ScrollContainer: org.melchor629.engine.gui.ScrollContainer,
    TextInput: org.melchor629.engine.gui.TextInput,
    TextLabel: org.melchor629.engine.gui.TextLabel,
    View: org.melchor629.engine.gui.View,

    easing: {
        Easing: org.melchor629.engine.gui.easing.Easing,
        BackEasing: org.melchor629.engine.gui.easing.BackEasing,
        CircEasing: org.melchor629.engine.gui.easing.CircEasing,
        CubicEasing: org.melchor629.engine.gui.easing.CubicEasing,
        ElasticEasing: org.melchor629.engine.gui.easing.ElasticEasing,
        ExpoEasing: org.melchor629.engine.gui.easing.ExpoEasing,
        LinearEasing: org.melchor629.engine.gui.easing.LinearEasing,
        QuadEasing: org.melchor629.engine.gui.easing.QuadEasing,
        QuartEasing: org.melchor629.engine.gui.easing.QuartEasing,
        QuintEasing: org.melchor629.engine.gui.easing.QuintEasing,
        SineEasing: org.melchor629.engine.gui.easing.SineEasing,
        BounceEasing: org.melchor629.engine.gui.easing.BounceEasing,
    },

    eventListeners: {
        OnBlur: org.melchor629.engine.gui.eventListeners.OnBlur,
        OnCharKey: org.melchor629.engine.gui.eventListeners.OnCharKey,
        OnFocus: org.melchor629.engine.gui.eventListeners.OnFocus,
        OnKeyDown: org.melchor629.engine.gui.eventListeners.OnKeyDown,
        OnKeyUp: org.melchor629.engine.gui.eventListeners.OnKeyUp,
        OnMouseDown: org.melchor629.engine.gui.eventListeners.OnMouseDown,
        OnMouseEnter: org.melchor629.engine.gui.eventListeners.OnMouseEnter,
        OnMouseExit: org.melchor629.engine.gui.eventListeners.OnMouseExit,
        OnMouseMove: org.melchor629.engine.gui.eventListeners.OnMouseMove,
        OnMouseUp: org.melchor629.engine.gui.eventListeners.OnMouseUp,
        OnPropertyChange: org.melchor629.engine.gui.eventListeners.OnPropertyChange,
    }
};

var loaders = {
    audio: {
        AudioDecoder: org.melchor629.engine.loaders.audio.AudioDecoder
    },

    Collada: org.melchor629.engine.loaders.Collada
};

function Float(number) {
    return java.lang.Float.valueOf(number);
}