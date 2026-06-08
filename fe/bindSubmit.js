define(['jquery', 'layer', 'nprogress', 'ui', 'jquery-form', 'jquery-validate'], function($, layer, nprogress, ui) {
  //编辑窗体js
  var _fnCompleteProcess = function (icon, msg, fnCallback) {
    if (fnCallback) fnCallback();
    $.msg_error(msg, null, function () {
      layer.closeAll();
    });
  };

  $.fn.extend({
    /**
     * @param {function} success           提交成功回调
     * @param {function} beforeSubmit      提交前验证，如果设置该参数， function 必须返回 `true` 才会继续执行 submit 动作
     * @param {boolean}  keepFrameShow     是否阻止弹框关闭，默认 false 既提交后关闭弹出框
     * @param {object}   customAjaxConfig  自定义 ajax 参数。比如需要自定义传入 data 格式或其他特殊 ajax 配置，
     *                                     可按 jQuery.ajax 参数格式传入
     * @param {boolean}  skipLayerMsg      成功返回提示是否忽略内置 Layer 提示，默认 false 不忽略，在父级页面弹出 Layer 提示。
     *                                     可将该参数设为 true 后在 success 内配置自定义的消息反馈提示
     */
    bindSubmit: function () {
      var success, beforeSubmit, keepFrameShow, customAjaxConfig, skipLayerMsg;
      if (typeof arguments[0] === 'object') {
        success = arguments[0].success;
        beforeSubmit = arguments[0].beforeSubmit;
        keepFrameShow = arguments[0].keepFrameShow;
        customAjaxConfig = arguments[0].customAjaxConfig;
        skipLayerMsg = arguments[0].skipLayerMsg;
      } else {
        success = arguments[0];
        beforeSubmit = arguments[1];
        keepFrameShow = arguments[2];
        customAjaxConfig = arguments[3];
        skipLayerMsg = arguments[4];
      }
      var $btn = $(this);
      $btn.click(function () {

        var isValid = true;
        var $form = $btn.parents('form:first');
        if (!$form.valid()) {
            $form.find('.input-validation-error:first').focus();
            $form.find('.error:first').focus();
            isValid = false;
        }

        if (typeof beforeSubmit == "function") {
          if (!beforeSubmit()) {
            isValid = false;
          }
        }

        if (!isValid) {
          return false;
        }

        var oldHtml = $btn.html();
        var newHtml = $btn.html().replace('保 存', '稍后...').replace('fa fa-save fa-fw', 'icon-spinner icon-spin');
        $btn.addClass('disabled ').html(newHtml);

        nprogress.start(); // 开启页面顶部进度条效果

        var ajaxConfig = {
          dataType: 'json',
          timeout: 15000,
          success: function (data) {
            if (data.IsSuccess || data.Succeed) {

              nprogress.done(); // 关闭页面顶部进度条效果

              if (!data.Data) data.Data = '保存成功。';

              if (data.Message && data.Message.length > 0) {
                data.Data = data.Message;
              }

              if (success) {
                success(data);
              }

              if (!skipLayerMsg) {
                if (parent.scbui && parent.scbui.message) {
                  parent.scbui.message.success(data.Data);
                } else {
                  parent.$.msg_success(data.Data, {
                    time: 2000
                  });
                }
              }
              if (!keepFrameShow) {
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
              }
            } else {
              nprogress.done(); // 关闭页面顶部进度条效果
              var _message = (data.ResponseError && data.ResponseError.Message) ? data.ResponseError.Message : '保存失败。';
              _fnCompleteProcess(0, _message, function () {
                $btn.html(oldHtml).removeClass('disabled ');
              });
            }
          },
          error: null,
          complete: function (XMLHttpRequest, status) { //请求完成后最终执行参数
            if (status != 'success') {
              var icon = 8;
              var msg = '脚本错误, 未知数据是否保存成功, 请确认后联系管理员进行BUG修复。';
              //超时,status还有success,error等值的情况
              if (status == 'timeout') {
                msg = '执行超时, 未知数据是否保存成功。如确认数据未保存可重试。';
              }

              nprogress.done(); // 关闭页面顶部进度条效果

              _fnCompleteProcess(icon, msg, function () {
                $btn.html(oldHtml).removeClass('disabled ');
              });
            }
          }
        };

        if (customAjaxConfig) {
          var _config = $.extend({}, ajaxConfig, { url: $form.attr('action') }, customAjaxConfig);
          if (typeof _config.data === 'function') {
            _config.data = _config.data();
          }
          $.ajax(_config);
        } else {
          $form.ajaxSubmit(ajaxConfig);
        }

        return false;
      });
    },
    bindClose: function (beforeSubmit) {
      if (typeof beforeSubmit == "function") {
        if (!beforeSubmit()) {
          return false;
        }
      }
      //关闭确认
      var $btn = $(this);
      var $form = $btn.parents('form:first');
      var oldFormArray = $form.formSerialize();
      var b = true;
      $(this).click(function () {
        //获取当前窗口索引
        var index = parent.layer.getFrameIndex(window.name);

        var formArray = $form.formSerialize();
        if (oldFormArray != formArray) {
          b = false;
          layer.confirm('内容已改变，是否继续关闭？', {
            btn: ['确认关闭', '取消'], icon: 3, title: '提示', shadeClose: true
          }, function () {
            parent.layer.close(index);
          });
        }
        if (b) {
          parent.layer.close(index);
        }
      });
    }
  });

  // form 提示图标
  $(function() {
    $('.lable-tips').each(function() {
      $(this).popover({
        content: $(this).data('tips-content'),
        placement: 'bottomLeft',
        arrowPointAtCenter: true,
      })
    });
  });

});
