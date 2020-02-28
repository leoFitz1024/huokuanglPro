/**
 * Created by chenw on 2018/12/18.
 */
tmqtApp.controller('updateOutbillController', ['$scope', '$rootScope', '$http', '$filter', '$timeout', '$stateParams', function ($rootScope, $scope, $http, $filter, $timeout, $stateParams) {

    $scope.username = $rootScope.username;
    var billID = $stateParams.id;
    layui.use(['laydate', 'form', 'element', 'jquery', 'layer'], function () {
        var laydate = layui.laydate,
            $ = layui.$,
            form = layui.form,
            layer = layui.layer;

        $http({
            url: '/getAllFactory',
            method: 'GET',
        }).then(function successCallback(res) {
            $scope.factorys = res.data.data;
            $scope.$watch('$viewContentLoaded', function () {
                $timeout(function () {
                    form.render();
                }, 1000);
            });

        }, function errorCallback(res) {
            // 请求失败执行代码
            console.log(res);
            layer.msg('获取厂家列表失败', {icon: 2, time: 1500});
        });

        $http({
            url: '/getAllRangesize',
            method: 'GET',
        }).then(function successCallback(res) {
            $scope.sizes = res.data.data;
            $scope.$watch('$viewContentLoaded', function () {
                $timeout(function () {
                    form.render();
                }, 1000);
            });

        }, function errorCallback(res) {
            // 请求失败执行代码
            console.log(res);
            layer.msg('获取码段列表失败', {icon: 2, time: 1500});
        });


        //获取详情页面数据
        $http({
            url: '/getOutbillDetail?id=' + billID,
            method: 'GET',
        }).then(function successCallback(res) {
            $scope.outbillData = res.data.data.outbill;
            $scope.returnsList = res.data.data.returnsList;
            $scope.outbillData.time = toFormatDate($scope.outbillData.time, 2);
            $scope.outbillData.atmonth = toFormatDate($scope.outbillData.atmonth, 3);

            //具体日期
            laydate.render({
                elem: '#updateOutbillTime',
                value: toFormatDate(res.data.data.outbill.time, 2)
            });
            laydate.render({
                elem: '#updateOutbillAtmonth',
                type: 'month',
                value: toFormatDate(res.data.data.outbill.atmonth, 3)
            });
        }, function errorCallback(res) {
            // 请求失败执行代码
            console.log(res);
            layer.msg('获取页面数据失败', {icon: 2, time: 1500});
        });

        form.render();
        $scope.dataList = [1];//用来控制单据有几条数据

        $('#addOutbillItemBtn').on('click', function () {
            console.log($scope.returnsList);
            var returnsItem = {
                "code": "",
                "rangesize": "",
                "color": "",
                "price": "",
                "number": ""
            }
            $scope.returnsList.push(returnsItem);
            $scope.$apply();
            form.render();
        });
        $('#delOutbillItemBtn').on('click', function () {
            console.log($scope.returnsList);
            if ($scope.returnsList.length > 1) {
                $scope.returnsList.pop();
                $scope.$apply();
            } else {
                layer.msg('至少有一条数据', {time: 1600});
            }
        });

        $('#updateOutbillBtn').on('click', function () {
            var time = $('#updateOutbillTime').val();
            var atmonth = $('#updateOutbillAtmonth').val();
            var factoryidStr = $('#updateOutbillfactoryid').val();
            var factoryid = parseInt(factoryidStr);
            var remarks = $('#updateOutbillRemarks').val();
            var allnumber = 0;
            var allprice = 0;
            var returnsList = new Array();
            if (time == "" || factoryidStr == "") {
                layer.msg('请填写完整的日期和厂家。', {time: 1800});
            } else {
                for (var i = 0; i < $scope.returnsList.length; i++) {
                    var code = $('#updateReturnsCode' + i).val();
                    var color = $('#updateReturnsColor' + i).val();
                    var rangsize = $('#updateReturnsSize' + i).val();
                    var priceStr = $('#updateReturnsPrice' + i).val();
                    var price = parseFloat(priceStr);
                    var numberStr = $('#updateReturnsNumber' + i).val();
                    var number = parseInt(numberStr);
                    if (code == "" || color == "" || rangsize == "" || priceStr == "" || numberStr == "") {
                        layer.msg('请确保每一条单据条目信息都是完整的。', {time: 1800});
                        return;
                    } else {
                        var returns = {
                            code: code,
                            color: color,
                            rangesize: rangsize,
                            price: price,
                            number: number
                        };
                        allnumber += number;
                        allprice += (number * price);
                        returnsList.push(returns);
                    }
                }
                var updateOutbillData = {
                    outbillid:billID,
                    factoryid: factoryid,
                    time: time,
                    atmonth:atmonth,
                    remarks: remarks,
                    allnumber: allnumber,
                    allprice: allprice,
                    returnss: returnsList

                };

                console.log(updateOutbillData);
                $http({
                    url: '/updateOutbill',
                    method: 'POST',
                    data: updateOutbillData
                }).then(function successCallback(res) {
                    if (res.data > 0) {
                        layer.msg('修改成功', {icon: 1, time: 1500});
                        $timeout(function () {
                            window.location.href = '/#/index/outbilllist';
                        }, 1600);
                    } else {
                        console.log(res);
                        layer.msg('修改失败', {icon: 2, time: 1500});
                    }
                }, function errorCallback(res) {
                    // 请求失败执行代码
                    console.log(res);
                    layer.msg('修改失败', {icon: 2, time: 1500});
                });
            }
        });

    });

}])