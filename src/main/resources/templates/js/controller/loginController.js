tmqtApp.controller('loginController', ['$scope','$http', function($scope, $http, $timeout) {

    layui.use(['jquery', 'layer','element'], function() {
        var $ = layui.$,
            layer = layui.layer,
            table = layui.table,
            element = layui.element;
        element.render();

        //后面就跟你平时使用jQuery一样
        $('#loginBtn').on('click', function() {
                    var loginLoading = layer.load(1,{shade: [0.1,'#000']});
                    var username = $('#loginUserName').val();
                    var password = $('#loginPassword').val();
                    if(username != ''&& password != ''){
                        $http({
                            url: '/loginApi',
                            method: 'POST',
                            data: {
                                "username": username,
                                "password": password
                            }
                        }).then(function successCallback(res) {
                            // 请求成功执行代码
                            if(res.data.code == 1){
                                layer.close(loginLoading);
                                layer.msg("登录成功", {icon: 1,time:1000});
                                setCookie("username", res.data.data);
                                window.setTimeout("window.location.href='/#/index'",500);
                            }else if(res.data.code == 0){
                                layer.close(loginLoading);
                                layer.msg('密码错误', {icon: 2,time:1500});
                            }else if(res.data.code == -1){
                                layer.close(loginLoading);
                                layer.msg('用户不存在', {time:1500});
                            }
                        }, function errorCallback(res) {
                            // 请求失败执行代码
                            layer.close(loginLoading);
                            layer.msg('登录失败', {icon: 2,time:1500});
                        });
                    }else {
                        layer.close(loginLoading);
                        layer.msg('请输入完整登录信息', {time:1500});
                    }
        });
        $('#loginPassword').bind('keyup', function(event) {
            if (event.keyCode == "13") {
                //回车执行查询
                $('#loginBtn').click();
            }
        });
    });
}]);
