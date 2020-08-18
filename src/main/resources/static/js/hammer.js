/**
 * name:    自定义工具集合
 * author:  qping
 * desc:    仿造jquery实现
 */
(function(hammer){

    function each(data, callback){
        for (var i = 0, len = data.length; i < len; i++) {
            callback(data[i], i);
        }
    };

    function ajax(config){
        var url = config.url;
        var postData = (typeof config.data =="string") ? config.data : stringfy(config.data);
        var dataType = config.dataType;
        var fnSuccess = config.success;
        var fnFaild = config.error ? config.error : function(e){console.log(e)};
        var method = config.method ? config.method.toUpperCase() : "POST";
        var async = "async" in config ? config.async : true;

        if (window.XMLHttpRequest) {
            //非IE6
            var xhr = new XMLHttpRequest();
        } else {
            //IE6
            var xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xhr.open(method, url, async);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(postData);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if ((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
                    var responseText = xhr.responseText
                    if(dataType == 'json'){
                        responseText = eval("(" + responseText + ")")
                    }
                    fnSuccess(responseText);
                } else {
                    fnFaild(xhr.status);
                }
            }
        }
        xhr.ontimeout = function(event){
            console.log('请求超时');
        }
        xhr.onerror = function(e){
            fnFaild(e)
        }
    }

    function stringfy(obj){
        var params = []
        for(var i in obj){
            if(obj[i] !== null){
                params.push(i + "=" + obj[i]);
            }
        }
        return params.join("&")
    }

    function getUrlParams() {
        var paramsStr = window.location.search.slice(1);
        if (!paramsStr) {
            return {}
        }
        var params = paramsStr.split("&");
        var map = {}
        for (var i = 0; i < params.length; i++) {
            var param = params[i];
            var valuePair = param.split("=");
            var key = valuePair[0];
            var value = valuePair[1];
            map[key] = value;
        }
        return map;
    }

    var isObjFunc = function(name) {
        var toString = Object.prototype.toString
        return function() {
            return toString.call(arguments[0]) === '[object ' + name + ']'
        }
    }

    var  isObject = isObjFunc('Object'),
        isArray = isObjFunc('Array'),
        isBoolean = isObjFunc('Boolean');

    function extend() {
        var index = 0,isDeep = false,obj,copy,destination,source,i
        if(isBoolean(arguments[0])) {
            index = 1
            isDeep = arguments[0]
        }
        for(i = arguments.length - 1;i>index;i--) {
            destination = arguments[i - 1]
            source = arguments[i]
            if(isObject(source) || isArray(source)) {
                for(var property in source) {
                    obj = source[property]
                    if(isDeep && ( isObject(obj) || isArray(obj) ) ) {
                        copy = isObject(obj) ? {} : []
                        var extended = extend(isDeep,copy,obj)
                        destination[property] = extended
                    }else {
                        destination[property] = source[property]
                    }
                }
            } else {
                destination = source
            }
        }
        return destination
    }

    var EventUtil = {
        // 获取事件对象，兼容IE
        getEvent: function (event) {
            return event ? event : window.event;
        },
        // 获取事件目标，兼容IE
        getTarget: function (event) {
            return event.target || event.srcElement;
        },
        // 阻止默认事件，兼容IE
        preventDefault: function (event) {
            if (event.preventDefault) {
                event.preventDefault();
            }
            else {
                event.returnValue = false;
            }
        },
        // 阻止事件冒泡，兼容IE
        stopPropagation: function (event) {
            if (event.stopPropagation) {
                event.stopPropagation();
            }
            else {
                event.cancelBubble = true;
            }
        },
        // 注册事件
        addHandler: function (element, type, handler) {
            if (element.addEventListener) {
                // DOM2级的事件处理程序，最后一个参数为false，设置该事件处理程序在事件流的冒泡阶段触发
                element.addEventListener(type, handler, false);
            }
            else if (element.attachEvent){
                // IE的事件处理程序
                element.attachEvent('on' + type, handler);
            }
            else {
                // DOM0级的事件处理程序
                element['on' + type] = handler;
            }
        },
        // 删除事件
        removeHandler: function (element, type, handler) {
            if (element.removeEventListener) {
                element.removeEventListener(type, handler, false);
            }
            else if (element.detachEvent) {
                element.detachEvent('on' + type, handler);
            }
            else {
                element['on' + type] = null;
            }
        },
        // 获取事件(mouseover, mouseout)相关元素的信息
        getRelatedTarget: function (event) {
            if (event.relatedTarget) {
                return event.relatedTarget;
            }
            else if (event.toElement) {
                return event.toElement;
            }
            else if (event.fromElement) {
                return event.fromElement;
            }
            else {
                return null;
            }
        },
        // 获取鼠标点击事件的鼠标按钮
        getButton: function (event) {
            if (document.implementation.hasFeature('MouseEvents', '2.0')) {
                return event.button;
            }
            else {
                switch (event.button) {
                    case 0:
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        // 返回0表示主鼠标按钮
                        return 0;
                    case 2:
                    case 6:
                        // 返回2表示次鼠标按钮
                        return 2;
                    case 4:
                        // 返回1表示中间的鼠标按钮
                        return 1;
                }
            }
        },
        // 获取键盘事件的字符编码
        getCharCode: function (event) {
            if (typeof event.charCode === 'number') {
                return event.charCode;
            }
            else {
                return event.keyCode;
            }
        }
    };

    // 仿造jquery 创建 $ 对象，集成所有方法
    function hammer(selector){
        return new hammer.prototype.init(selector);
    };

    var init = hammer.prototype.init = function(selector){
        if ( selector && typeof selector === "string" ) {
            var idReg = /^#([\w-]+)$/;
            var classReg = /^.([\w-]+)$/;
            if(idReg.exec(selector)){
                var ele = document.getElementById(idReg.exec(selector)[1])
                if(ele){
                    this[0] = ele;
                    this.length = 1;
                }
            }else if( classReg.exec(selector)){
                var classname = classReg.exec(selector)[1];
                if(document.getElementsByClassName){
                    var eles =  document.getElementsByClassName(classname)
                    for (var i = 0;i < eles.length; i++){
                        this[i] = eles[i];
                    }
                    this.length = eles.length;
                }else{
                    var eles = document.getElementsByTagName("*");
                    var length = 0;
                    for (var i = 0;i < eles.length; i++){
                        if(eles[i].className.indexOf(classname) != -1){
                            this[i] = eles[i];
                            length ++;
                        }
                    }
                    this.length = length;
                }
            }

        } else if(selector && selector instanceof Object){
            this[0] = selector;
            this.length = 1;
        }
        return this;

    }

    init.prototype = hammer.prototype;

    hammer.prototype.ready = function(callback){

        if ( document.readyState === "complete" ) {
            setTimeout( callback );

        }else if ( document.addEventListener ) {
            // Use the handy event callback
            document.addEventListener( "DOMContentLoaded", callback, false );

            // A fallback to window.onload, that will always work
            // window.addEventListener( "load", callback, false );
        }
    }
    hammer.prototype.on = function(type, handler){
        hammer.each(this, function (ele) {
            EventUtil.addHandler(ele, type, handler)
        })
    }

    hammer.prototype.remove = function(){
        hammer.each(this, function (ele) {
            ele.parentNode.removeChild(ele)
        })
    }

    hammer.prototype.css = function(name, value){
        hammer.each(this, function (ele) {
            ele.style[name] = value
        })

    }

    hammer.prototype.hide = function(){
        hammer.each(this, function (ele) {
            ele.style["display"] = "none"
        })
    }

    hammer.prototype.val = function(val){
        if(typeof val !== "undefined" ){
            hammer.each(this, function (ele) {
                ele.value = val
            })
        }else{
            if(this.length < 1 || !this[0]){
                return;
            }
            var ele = this[0];
            return ele.value
        }
    }

    hammer.prototype.html = function(html){
        if(typeof html !== "undefined"){
            hammer.each(this, function (ele) {
                ele.innerHTML = html
            })
        }else{
            if(this.length < 1 || !this[0]){
                return;
            }
            var ele = this[0];
            return ele.innerHTML
        }
    }

    hammer.prototype.hasClass = function(ele, cls) {
        if(cls == undefined){
            cls = ele;
            ele = this[0];
        }

        if(ele && ele.getAttribute('class')){
            return ele.getAttribute('class').indexOf(cls) > -1;
        }else{
            return false;
        }
        
    }

    hammer.prototype.addClass = function(cls){
        var that = this;
        hammer.each(this, function (ele) {
            if (ele.classList) {
                ele.classList.add(cls);
            } else if (!that.hasClass(ele, cls)) {
                ele.setAttribute('class', ele.getAttribute('class') + ' ' + cls);
            }
        })
    }

    hammer.prototype.removeClass = function(cls){
        var that = this;
        hammer.each(this, function (ele) {
            if (ele.classList) {
                ele.classList.remove(cls);
            } else if (that.hasClass(ele, cls)) {
                ele.setAttribute('class', ele.getAttribute('class').replace(cls, ' '));
            }

        })
    }

    hammer.prototype.data = function(name, value){
        if(typeof value !== "undefined"){
            hammer.each(this, function (ele) {
                ele.setAttribute("data-" + name, value)
            })
        }else{
            if(this.length < 1 || !this[0]){
                return;
            }
            var ele = this[0];
            var data = ele.getAttribute( "data-" + name );
            return data;
        }
    }

    hammer.prototype.attr = function(name, value){
        if(typeof value !== "undefined"){
            hammer.each(this, function (ele) {
                ele.setAttribute(name, value)
            })
        }else{
            if(this.length < 1 || !this[0]){
                return;
            }
            var ele = this[0];
            var data = ele.getAttribute(name );
            return data;
        }
    }

    hammer.prototype.focus = function(name, value){
        if(this.length < 1 || !this[0]){
            return;
        }
        var ele = this[0];
        ele.focus();
        ele.select()
    }

    // --------------------------- 扩展静态方法 ------------------------ //
    hammer.each = each;
    hammer.extend = extend;
    hammer.isArray = isArray;
    hammer.on = function(ele, type, handler){
        if(ele){
            EventUtil.addHandler(ele, type, handler)
        }
    }
    hammer.trim = function(value){
        return value.replace(/^\s+|\s+$/gm,'');
    }

    hammer.ajax = ajax;
    hammer.setCookie = function(name, value, option){
        var valueSet = name + "="+ encodeURIComponent(value);
        var expires = '';
        if (option && option.expires) {
            var exp = new Date();
            exp.setTime(exp.getTime() + option.expires*24*60*60*1000);
            expires = ";expires=" + exp.toGMTString();
        }
        var path = option.path ? ';path=' + option.path : '';
        var domain = option.domain ? ';domain=' + option.domain : '';

        document.cookie = [valueSet, expires, path, domain].join('');
    }

    hammer.getCookie = function(name){
        if (!document.cookie || document.cookie == '') {
            return null;
        }
        var arr,reg = new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr = document.cookie.match(reg)){
            return decodeURIComponent(arr[2]);
        }
        return null;
    }

    hammer.deleteCoolie = function(name){
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval= this.getCookie(name);
        if(cval!=null) document.cookie = name + "="+cval+";expires="+exp.toGMTString();
    }

    hammer.getUrlParams = getUrlParams;

    window.$ = hammer;
})()