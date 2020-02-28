/**
 * Created by chenw on 2018/12/17.
 */
tmqtApp.controller('addOutbillController', ['$scope', '$rootScope', '$http', '$filter','$timeout', function($rootScope, $scope, $http, $filter,$timeout) {
    console.log('addbillController');
    $scope.username = $rootScope.username;
    layui.use(['laydate','form','element','jquery', 'layer'], function() {
        var table = layui.table,
            laydate = layui.laydate,
            $ = layui.$,
            form = layui.form,
            layer = layui.layer;

        $http({
            url: '/getAllFactory',
            method: 'GET',
        }).then(function successCallback(res) {
            $scope.factorys = res.data.data;
            $scope.$watch('$viewContentLoaded', function() {
                $timeout(function () {
                    form.render();
                }, 1000);
            });

        }, function errorCallback(res) {
            // 请求失败执行代码
            console.log(res);
            layer.msg('获取厂家列表失败', {icon: 2,time:1500});
        });

        $http({
            url: '/getAllRangesize',
            method: 'GET',
        }).then(function successCallback(res) {
            $scope.sizes = res.data.data;
            $scope.$watch('$viewContentLoaded', function() {
                $timeout(function () {
                    form.render();
                }, 1000);
            });

        }, function errorCallback(res) {
            // 请求失败执行代码
            console.log(res);
            layer.msg('获取码段列表失败', {icon: 2,time:1500});
        });
        form.render();

        //具体日期
        laydate.render({
            elem: '#addOutbillTime',
        });

        laydate.render({
            elem: '#addOutbillAtmonth',
            type: 'month'
        });
        $scope.dataList = [1];//用来控制单据有几条数据

        $('#addOutbillItemBtn').on('click', function() {
            console.log($scope.dataList);
            $scope.dataList.push($scope.dataList.length+1);
            $scope.$apply();
            form.render();
        });
        $('#delOutbillItemBtn').on('click', function() {
            console.log($scope.dataList);
            if($scope.dataList.length>1){
                $scope.dataList.pop();
                $scope.$apply();
            }else{
                layer.msg('至少有一条数据', {time:1600});
            }
        });

        $('#addOutbillBtn').on('click', function() {
            var time = $('#addOutbillTime').val();
            var atmonth = $('#addOutbillAtmonth').val();
            var factoryidStr = $('#addOutbillfactoryid').val();
            var factoryid =parseInt(factoryidStr);
            var remarks = $('#addOutbillRemarks').val();
            var allnumber = 0;
            var allprice = 0;
            var returnsList = new Array();
            if(time==""||factoryidStr==""){
                layer.msg('请填写完整的日期和厂家。', {time:1800});
            }else{
                for(var i=1;i<=$scope.dataList.length;i++){
                    var code = $('#addReturnsCode'+i).val();
                    var color = $('#addReturnsColor'+i).val();
                    var rangsize = $('#addReturnsSize'+i).val();
                    var priceStr = $('#addReturnsPrice'+i).val();
                    var price = parseFloat(priceStr);
                    var numberStr = $('#addReturnsNumber'+i).val();
                    var number = parseInt(numberStr);
                    if(code==""||color==""||rangsize==""||priceStr==""||numberStr==""){
                        layer.msg('请确保每一条单据条目信息都是完整的。', {time:1800});
                        return;
                    }else{
                        var returns = {
                            code:code,
                            color:color,
                            rangesize:rangsize,
                            price:price,
                            number:number
                        };
                        allnumber += number;
                        allprice += (number*price);
                        returnsList.push(returns);
                    }
                }
                var addOutbillData = {
                    factoryid:factoryid,
                    time:time,
                    atmonth:atmonth,
                    remarks:remarks,
                    allnumber:allnumber,
                    allprice:allprice,
                    returnss:returnsList

                };

                $http({
                    url: '/addOutbill',
                    method: 'POST',
                    data:addOutbillData
                }).then(function successCallback(res) {
                    if(res.data>0){
                        var addLayer = layer.confirm('添加进货订单成功', {
                            btn: ['继续添加','返回列表'] //按钮
                        }, function(){
                            layer.close(addLayer);
                            $('#outbillAddForm')[0].reset();
                        }, function(){
                            window.location.href='/#/index/outbilllist';
                        });
                    }else{
                        console.log(res);
                        layer.msg('添加进货订单失败', {icon: 2,time:1500});
                    }
                }, function errorCallback(res) {
                    // 请求失败执行代码
                    console.log(res);
                    layer.msg('添加进货订单失败', {icon: 2,time:1500});
                });
                console.log(addOutbillData);
            }
        });

    });

}])
