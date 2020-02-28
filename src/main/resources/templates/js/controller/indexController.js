/**
 * Created by chenw on 2018/5/29.
 */
tmqtApp.controller('indexController', ['$scope', '$rootScope', '$http', function($rootScope, $scope, $http) {
    var userName = getCookie('username');
    $rootScope.username = userName;
    if(userName !== null){
        $scope.logoStr = $rootScope.username.substr(0,1);
    }
    layui.use(['element','form'], function() {
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        var form = layui.form;
        element.render();
    });

    layui.use(['jquery', 'layer'], function() {
        var $ = layui.$,
            layer = layui.layer,
            table = layui.table;

        //后面就跟你平时使用jQuery一样
        $('#exitBtn').on('click', function() {
            var exitLoading = layer.load(1,{shade: [0.1,'#000']});
            $.ajax({
                type: "post",
                url: '/exitApi',
                success: function (res) {
                    // 请求成功执行代码
                    if(res === 1){
                        layer.close(exitLoading);
                        layer.msg("退出成功", {icon: 1,time:1000});
                        window.location.href='/#/login'
                    }
                },
                error:function () {
                    layer.close(exitLoading);
                    layer.msg('退出失败', {icon: 2,time:1500});
                }
            });
        });
    });

}]);