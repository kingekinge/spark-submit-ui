/*!
 * zeroModal.js
 * http://git.oschina.net/cylansad/zeroModal
 *
 * Copyright 2016, Sad
 */
(function(obj) {

    if (typeof module !== 'undefined' && typeof exports === 'object' && define.cmd) {
        module.exports = obj;
    } else if (typeof define === 'function' && define.amd) {
        define(function() {
            return obj;
        });
    } else {
        window.zeroModal = obj;
    }

}((function($) {

    var zeroModal = {};

    // 临时变量,是否已显示
    var _tmp_variate_ishow = false;
    // 临时变量,最后的zindex值
    var _tmp_last_zindex = 1000;

    /**
     * 显示模态框
     * @param  {[type]} opt [description]
     * @return {[type]}     [description]
     */
    zeroModal.show = function(opt) {
        // 初始化
        var params = _initParams(opt);

        // 渲染
        _render(params);

        // 重新定位
        _tmp_variate_ishow = true;
        $(window).resize(function() {
            if (_tmp_variate_ishow) {
                _resize(params);
            }
        });

        return params.unique;
    };

    /**
     * 关闭指定模态框(不会触发onCleanup、onClosed方法)
     * @param  {[type]} unique [description]
     * @return {[type]}        [description]
     */
    zeroModal.close = function(unique) {
        _close({ unique: unique });
        _tmp_variate_ishow = false;
    };

    /**
     * 关闭全部模态框(不会触发onCleanup、onClosed方法)
     * @return {[type]} [description]
     */
    zeroModal.closeAll = function() {
        $('.zeromodal-overlay').remove();
        $('.zeromodal-container').remove();
        _tmp_variate_ishow = false;
    };


    /**
     * 显示等待框
     * @return {[type]} [description]
     */
    zeroModal.loading = function(type) {
        var loadClass = 'zeromodal-loading1';
        switch (type) {
            case 1:
                loadClass = 'zeromodal-loading1';
                break;
            case 2:
                loadClass = 'zeromodal-loading2';
                break;
        }
        var params = _initParams();
        _buildOverlay(params);

        // 重新定位top值
        var _top = $(window).scrollTop() + Math.ceil($(window).height() / 3);

        $('body').append('<div zero-unique-loading="' + params.unique + '" class="' + loadClass + '" style="top:' + _top + 'px;"></div>');
        return params.unique;
    };

    /**
     * 显示alert框
     * @param  {[type]} content [description]
     * @return {[type]}         [description]
     */
    zeroModal.alert = function(content) {
        var _opt = {
            iconClass: 'zeromodal-icon-info',
            iconText: '!'
        };

        var params = {};
        $.extend(params, _opt);

        if (typeof content === 'object') {
            $.extend(params, content);
        } else {
            params.content = content;
        }
        _buildAlertInfo(params);
    };

    /**
     * 显示错误alert框
     * @param  {[type]} content [description]
     * @return {[type]}         [description]
     */
    zeroModal.error = function(content) {
        var params = {
            iconDisplay: '<div class="zeromodal-icon zeromodal-error"><span class="x-mark"><span class="line left"></span><span class="line right"></span></span></div>'
        };

        if (typeof content === 'object') {
            $.extend(params, content);
        } else {
            params.content = content;
        }
        _buildAlertInfo(params);
    };

    /**
     * 显示正确alert框
     * @param  {[type]} content [description]
     * @return {[type]}         [description]
     */
    zeroModal.success = function(content) {
        var params = {
            iconDisplay: '<div class="zeromodal-icon zeromodal-success"><span class="line tip"></span><span class="line long"></span><div class="placeholder"></div></div>'
        };

        if (typeof content === 'object') {
            $.extend(params, content);
        } else {
            params.content = content;
        }
        _buildAlertInfo(params);
    };

    /**
     * 显示confirm框
     * @param  {[type]} content [description]
     * @param  {[type]} okFn    [description]
     * @return {[type]}         [description]
     */
    zeroModal.confirm = function(content, okFn) {
        var _opt = {
            iconClass: 'zeromodal-icon-question',
            iconText: '?',
        };

        var params = {};
        $.extend(params, _opt);
        if (typeof okFn === 'function') {
            params.okFn = okFn;
        }
        params.cancel = true;

        if (typeof content === 'object') {
            $.extend(params, content);
        } else {
            params.content = content;
        }
        _buildAlertInfo(params);
    };

    /**
     * 默认的参数
     * @type {Object}
     */
    var defaultOpt = {
        unique: '', // 唯一值
        title: '',
        content: '',
        url: false,
        iframe: false,
        width: '500px',
        height: '300px',
        transition: false,
        opacity: 0.2,
        overlay: true,
        overlayClose: false,
        ok: false,
        okTitle: '确定',
        okFn: false,
        cancel: false,
        cancelTitle: '关闭',
        cancelFn: true,
        buttonTopLine: true,
        esc: false,
        /* callbacks */
        onOpen: false,
        onLoad: false,
        onComplete: false,
        onCleanup: false,
        onClosed: false
    };

    // 初始化
    var _initParams = function(opt) {
        var params = {};
        $.extend(params, defaultOpt);
        $.extend(params, opt);
        if (typeof params.unique === 'undefined' || params.unique === '') {
            params.unique = _getUuid();
        }
        return params;
    };

    var _render = function(opt) {
        if (typeof opt.onOpen === 'function') { opt.onOpen(); }

        _buildOverlay(opt);
        _buildModal(opt);
    };

    var _close = function(opt) {
        if (typeof opt === 'object') {
            if (typeof opt.onCleanup === 'function') {
                opt.onCleanup();
            }

            $('[zero-unique-overlay="' + opt.unique + '"]').remove();
            $('[zero-unique-container="' + opt.unique + '"]').remove();

            if (typeof opt.onClosed === 'function') {
                opt.onClosed();
            }
        }
    };

    /**
     * 构建遮罩层
     * @param  {[type]} opt [description]
     * @return {[type]}     [description]
     */
    var _buildOverlay = function(opt) {
        _tmp_last_zindex++;

        var _width = $(document).width();
        var _height = $(document).height();

        // 是否需要显示遮罩层
        if (opt.overlay) {
            var _overlay = $('<div zero-unique-overlay="' + opt.unique + '" class="zeromodal-overlay" style="z-index:' + _tmp_last_zindex + ';width:' + _width + 'px;height:' + _height + 'px"></div>');
            $('body').append(_overlay);

            // 是否允许点击遮罩层关闭modal
            if (opt.overlayClose) {
                _overlay.css('cursor', 'pointer');
                _overlay.click(function() {
                    _close(opt);
                });
            } else {
                _overlay.click(function() {
                    _shock($('[zero-unique-container="' + opt.unique + '"]'));
                });
            }
        }
    };

    /**
     * 构建模态框
     * @param  {[type]} opt [description]
     * @return {[type]}     [description]
     */
    var _buildModal = function(opt) {
        _tmp_last_zindex++;

        //// 获取modal的宽度和高度
        var _width = opt.width.replace('px', '');
        var _height = opt.height.replace('px', '');
        var _wwidth = $(window).width();
        var _wheight = $(window).height();
        // 如果width为%值，则计算具体的width值
        if (_width.indexOf('%') !== -1) {
            _width = (_wwidth * parseInt(_width.replace('%', '')) / 100);
        }
        // 如果height为%值，则计算具体的height值
        if (_height.indexOf('%') !== -1) {
            _height = (_wheight * parseInt(_height.replace('%', '')) / 100);
        }
        if (typeof _width === 'string') _width = parseInt(_width);
        if (typeof _height === 'string') _height = parseInt(_height);

        //// 获取modal的位置
        var _left = (_wwidth - _width) / 2;
        var _top = $(window).scrollTop() + Math.ceil(($(window).height() - _height) / 3);

        //// 构建容器
        var _container = $('<div zero-unique-container="' + opt.unique + '" class="zeromodal-container" style="z-index:' + _tmp_last_zindex + ';width:' + _width + 'px;height:' + _height + 'px;left:' + _left + 'px;top:' + (opt.transition ? _top - 50 : _top) + 'px"></div>');

        //// 构建头部
        var _header = $('<div class="zeromodal-header"><div class="zeromodal-close">×</div><span class="modal-title">' + opt.title + '</span></div>');
        _container.append(_header);
        $('body').append(_container);
        $('.zeromodal-close').click(function() { _close(opt); });

        // 出场动画
        if (opt.transition) {
            $('.zeromodal-container').animate({ top: _top }, 300);
        }

        //// 构建内容区
        var _bodyHeight = $('[zero-unique-container="' + opt.unique + '"]').height() - $(_header).height() - 10 - ((opt.ok || opt.cancel) ? 60 : 0);
        var _body = $('<div zero-unique-body="' + opt.unique + '" class="zeromodal-body" style="height:' + _bodyHeight + 'px;"></div>');
        _container.append(_body);

        if (typeof opt.onLoad === 'function') { opt.onLoad(); }
        // 如果url为空，则直接显示content的内容
        if (!opt.url) {
            // 如果是div方式，则设置overflow-y属性，同时通过ajax获取内容
            $('[zero-unique-body="' + opt.unique + '"]').addClass('zeromodal-overflow-y');

            _body.html(opt.content);
            if (typeof opt.onComplete === 'function') { opt.onComplete(); }
        } else {
            _body.html('<div class="zeromodal-loading1"></div>');
            // 如果iframe为true，则通过iframe的方式加载需要显示的内容
            if (opt.iframe) {
                var _iframe = $('<iframe src="' + opt.url + '" class="zeromodal-frame"></iframe>');
                _body.append(_iframe);
                _iframe.load(function() {
                    $('.zeromodal-loading1').remove();
                    if (typeof opt.onComplete === 'function') { opt.onComplete(); }
                });
            } else {
                // 如果是div方式，则设置overflow-y属性，同时通过ajax获取内容
                $('[zero-unique-body="' + opt.unique + '"]').addClass('zeromodal-overflow-y');
                $.ajax({
                    url: opt.url,
                    dataType: "html",
                    type: "get",
                    success: function(data) {
                        _body.append(data);
                        $('.zeromodal-loading1').remove();
                        if (typeof opt.onComplete === 'function') { opt.onComplete(); }
                    }
                });
            }
        }

        //// 构建尾部区
        _buildFooter(opt, _container);

        if (opt.esc) {
            $('body').one("keyup", function(e) {
                if (e.keyCode === 27) {
                    _close(opt);
                }
            });
        }
    };


    /**
     * 构建尾部
     * @param  {[type]} opt [description]
     * @param  {[type]} _container [description]
     * @return {[type]}     [description]
     */
    var _buildFooter = function(opt, _container) {
        if (opt.ok || opt.cancel) {
            var _footer = '<div class="zeromodal-footer">';
            _footer += opt.buttonTopLine ? '<div class="zeromodal-line"></div>' : '';
            _footer += '        <div zeromodal-btn-container="' + opt.unique + '" class="zeromodal-btn-container"></div>';
            _footer += '   </div>';
            _container.append(_footer);

            if (opt.ok) {
                var _ok = $('<button zeromodal-btn-ok="' + opt.unique + '" class="zeromodal-btn zeromodal-btn-primary">' + opt.okTitle + '</button>');
                $('[zeromodal-btn-container="' + opt.unique + '"]').append(_ok);
                _ok.click(function() {
                    if (typeof opt.okFn === 'function') {
                        var _r = opt.okFn();
                        if (typeof _r === 'undefined' || _r)
                            _close(opt);
                    } else
                        _close(opt);
                });
            }
            if (opt.cancel) {
                var _cancel = $('<button zeromodal-btn-cancel="' + opt.unique + '" class="zeromodal-btn zeromodal-btn-default">' + opt.cancelTitle + '</button>');
                $('[zeromodal-btn-container="' + opt.unique + '"]').append(_cancel);
                _cancel.click(function() {
                    if (typeof opt.cancelFn === 'function') {
                        var _r = opt.cancelFn();
                        if (typeof _r === 'undefined' || _r)
                            _close(opt);
                    } else
                        _close(opt);
                });
            }
        }
    };

    /**
     * 构建提示框、确认框等内容
     */
    var _buildAlertInfo = function(opt) {
        // 初始化
        if (typeof opt === 'undefined' || typeof opt.cancelTitle === 'undefined') {
            opt.cancelTitle = '取消';
        }

        var params = _initParams(opt);
        params.width = '360px';
        params.height = '300px';
        params.esc = true;
        params.ok = true;
        params.buttonTopLine = false;
        if (typeof _okFn !== 'undefined') {
            params.okFn = _okFn;
        }
        if (typeof cancelFn !== 'undefined') {
            params.cancelFn = cancelFn;
        }

        var _content = params.content || '';
        var _contentDetail = params.contentDetail || '';
        params.content = '';

        // 渲染
        _render(params);

        // 渲染内容
        var icon = undefined;
        if (typeof params.iconDisplay !== 'undefined') {
            icon = $(params.iconDisplay);
        } else {
            icon = $('<div class="zeromodal-icon ' + params.iconClass + '">' + params.iconText + '</div>');
        }
        var text = $('<div class="zeromodal-title1">' + _content + '</div><div class="zeromodal-title2">' + _contentDetail + '</div>');
        $('[zero-unique-body="' + params.unique + '"]').append(icon);
        $('[zero-unique-body="' + params.unique + '"]').append(text);
        $('[zero-unique-body="' + params.unique + '"]').removeClass('zeromodal-overflow-y');

        // 给按钮添加focus
        $('[zeromodal-btn-ok="' + params.unique + '"]').focus();

        // 重新定位
        _tmp_variate_ishow = true;
        $(window).resize(function() {
            if (_tmp_variate_ishow) {
                _resize(params);
            }
        });
    };

    /**
     * 重新定位
     * @param  {[type]} opt [description]
     * @return {[type]}     [description]
     */
    var _resize = function(opt) {
        // 遮罩层
        $('[zero-unique-overlay="' + opt.unique + '"]').css("width", $(document).width() + 'px').css("height", $(document).height() + 'px');

        // 弹出层
        var _wwidth = $(window).width();
        var _wheight = $(window).height();
        var _width = opt.width.replace('px', '');
        var _height = opt.height.replace('px', '');

        if (_width.indexOf('%') !== -1) {
            _width = (_wwidth * parseInt(_width.replace('%', '')) / 100);
        }
        if (_height.indexOf('%') !== -1) {
            _height = (_wheight * parseInt(_height.replace('%', '')) / 100);
        }
        if (typeof _width === 'string') _width = parseInt(_width);
        if (typeof _height === 'string') _height = parseInt(_height);
        var _left = (_wwidth - _width) / 2;
        var _top = $(window).scrollTop() + Math.ceil(($(window).height() - _height) / 3);
        $('[zero-unique-container="' + opt.unique + '"]').css('width', _width + 'px').css('height', _height + 'px').css('left', _left + 'px').css('top', _top + 'px');
    };

    /**
     * 元素左右晃动
     * @param  {[type]} jobj [description]
     * @return {[type]}      [description]
     */
    var _shock = function(jobj) {
        if (jobj.length === 0) {
            return;
        }
        var left = jobj.position().left
        for (var i = 0; i < 2; i++) {
            jobj.animate({ left: left - 2 }, 50);
            jobj.animate({ left: left }, 50);
            jobj.animate({ left: left + 2 }, 50);
        }
        jobj.animate({ left: left }, 50);
    };

    /**
     * 获取uuid
     * @returns {string}
     */
    var _getUuid = function() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4";
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
        s[8] = s[13] = s[18] = s[23] = "";
        var uuid = s.join("");
        return uuid;
    };

    return zeroModal;

}(jQuery))));
