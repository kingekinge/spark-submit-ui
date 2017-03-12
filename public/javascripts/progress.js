/**
 * 纯js进度条
 * Created by kiner on 15/3/22.
 */

function progress(options){

    this.w = (options && options.width)?parseFloat(options.width) : parseFloat(this.options.width);
    this.h = (options && options.height)?parseFloat(options.height) : parseFloat(this.options.height);
    this.bgColor = (options && options.bgColor)?options.bgColor : this.options.bgColor;
    this.proColor = (options && options.proColor)?options.proColor : this.options.proColor;
    this.fontColor = (options && options.fontColor)?options.fontColor : this.options.fontColor;
    this.showPresent = (options && options.showPresent != undefined)?options.showPresent : this.options.showPresent;
    this.completeCallback = (options && options.completeCallback)?options.completeCallback : this.options.completeCallback;
    this.changeCallback = (options && options.changeCallback)?options.changeCallback : this.options.changeCallback;
    this.text = (options && options.text)?options.text : this.options.text;
    this.val = (options && options.val)?options.val : this.options.val;

    this.strTemp = this.text.substring(0,this.text.indexOf('#*'))+"{{pro}}"+this.text.substring(this.text.indexOf('*#')+2);

    this.init();

}
/**
 * 默认选项
 * @type {{width: number, height: number, bgColor: string, proColor: string, fontColor: string, val: number, text: string, showPresent: boolean, completeCallback: Function, changeCallback: Function}}
 */
progress.prototype.options = {

    width : 200,
    height: 30,
    bgColor : "#005538",
    proColor : "#009988",
    fontColor : "#FFFFFF",
    val : 10,
    text:"当前进度为#*val*#%",
    showPresent : true,
    completeCallback:function(){},
    changeCallback:function(){}

};

/**
 * 初始化
 */
progress.prototype.init = function(){

    this.proBox = document.createElement('div');
    this.proBg = document.createElement('div');
    this.proPre = document.createElement('div');
    this.proFont = document.createElement('div');

    addClass(this.proBox,'proBox');
    addClass(this.proBg,'proBg');
    addClass(this.proPre,'proPre');
    addClass(this.proFont,'proFont');

    this.proBox.setAttribute("style","width:"+this.w+"px; height:"+this.h+"px; position:relative; overflow:hidden; box-shadow:0 0 5px #FFFFFF; -moz-box-shadow:0 0 5px #FFFFFF; -webkit-box-shadow:0 0 5px #FFFFFF; -o-box-shadow:0 0 5px #FFFFFF;");
    this.proBg.setAttribute("style","background-color:"+this.bgColor+"; position:absolute; z-index:1; width:100%; height:100%; top:0; left:0;");
    this.proPre.setAttribute("style","transition:all 300ms; -moz-transition:all 300ms; -webkit-transition:all 300ms; -o-transition:all 300ms; width:"+this.val+"%; height:100%; background-color:"+this.proColor+"; position:absolute; z-index:2; top:0; left:0;");
    if(this.showPresent){

        this.proFont.setAttribute("style","overflow:hidden;text-overflow:ellipsis; white-space:nowrap; *white-space:nowrap; width:100%; height:100%; color:"+this.fontColor+"; text-align:center; line-height:"+this.h+"px; z-index:3; position:absolute; font-size:12px;");

        var text = this.parseText();
        this.proFont.innerHTML = text;
        this.proFont.setAttribute("title",text);
        this.proBox.appendChild(this.proFont);
    }


    this.proBox.appendChild(this.proBg);
    this.proBox.appendChild(this.proPre);

};

/**
 *
 */
progress.prototype.refresh = function(){
    this.proPre.style.width = this.val+"%";

    this.proFont.innerHTML = this.parseText();
};

/**
 * 转换文字
 * @returns {options.text|*}
 */
progress.prototype.parseText = function(){
    this.text = this.strTemp.replace("{{pro}}",this.val);
    return this.text;
};

/**
 * 更新进度条进度
 * @param val
 */
progress.prototype.update = function(val){

    this.val = val;
    this.refresh();

    this.changeCallback.call(this,val);
    if(val==100){

        this.completeCallback.call(this,val);

    }
};
/**
 * 获取进度条本身的html对象，可直接将其塞入容器中
 * @returns {HTMLElement|*}
 */
progress.prototype.getBody = function(){
    return this.proBox;
};
/**
 * 获取当前进度条的值
 * @returns {*}
 */
progress.prototype.getVal = function(){
    return this.val;
};

function hasClass(obj, cls) {
    return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}

function addClass(obj, cls) {
    if (!this.hasClass(obj, cls)) obj.className += " " + cls;
}

function removeClass(obj, cls) {
    if (hasClass(obj, cls)) {
        var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
        obj.className = obj.className.replace(reg, ' ');
    }
}

function toggleClass(obj,cls){
    if(hasClass(obj,cls)){
        removeClass(obj, cls);
    }else{
        addClass(obj, cls);
    }
}