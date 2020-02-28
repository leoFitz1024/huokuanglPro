/**
 * Created by chenw on 2018/12/18.
 */
tmqtApp.controller('updateInbillController', ['$scope', '$rootScope', '$http', '$filter', '$timeout', '$stateParams', function ($rootScope, $scope, $http, $filter, $timeout, $stateParams) {

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
            url: '/getInbillDetail?id=' + billID,
            method: 'GET',
        }).then(function successCallback(res) {
            $scope.inbillData = res.data.data.inbill;
            $scope.purchaseList = res.data.data.purchaseList;
            //具体日期
            laydate.render({
                elem: '#updateInbillTime',
                value: toFormatDate(res.data.data.inbill.time, 2)
            });
        }, function errorCallback(res) {
            // 请求失败执行代码
            console.log(res);
            layer.msg('获取页面数据失败', {icon: 2, time: 1500});
        });

        form.render();
        $scope.dataList = [1];//用来控制单据有几条数据

        $('#addInbillItemBtn').on('click', function () {
            console.log($scope.purchaseList);
            var purchaseItem = {
                "code": "",
                "rangesize": "",
                "color": "",
                "price": "",
                "number": ""
            }
            $scope.purchaseList.push(purchaseItem);
            $scope.$apply();
            form.render();
        });
        $('#delInbillItemBtn').on('click', function () {
            console.log($scope.purchaseList);
            if ($scope.purchaseList.length > 1) {
                $scope.purchaseList.pop();
                $scope.$apply();
            } else {
                layer.msg('至少有一条数据', {time: 1600});
            }
        });

        $('#updateInbillBtn').on('click', function () {
            var time = $('#updateInbillTime').val();
            var factoryidStr = $('#updateInbillfactoryid').val();
            var factoryid = parseInt(factoryidStr);
            var remarks = $('#updateInbillRemarks').val();
            var allnumber = 0;
            var allprice = 0;
            var purchaseList = new Array();
            if (time == "" || factoryidStr == "") {
                layer.msg('请填写完整的日期和厂家。', {time: 1800});
            } else {
                for (var i = 0; i < $scope.purchaseList.length; i++) {
                    var code = $('#updatePurchaseCode' + i).val();
                    var color = $('#updatePurchaseColor' + i).val();
                    var rangsize = $('#updatePurchaseSize' + i).val();
                    var priceStr = $('#updatePurchasePrice' + i).val();
                    var price = parseFloat(priceStr);
                    var numberStr = $('#updatePurchaseNumber' + i).val();
                    var number = parseInt(numberStr);
                    if (code == "" || color == "" || rangsize == "" || priceStr == "" || numberStr == "") {
                        layer.msg('请确保每一条单据条目信息都是完整的。', {time: 1800});
                        return;
                    } else {
                        var purchase = {
                            code: code,
                            color: color,
                            rangesize: rangsize,
                            price: price,
                            number: number
                        };
                        allnumber += number;
                        allprice += (number * price);
                        purchaseList.push(purchase);
                    }
                }
                var updateInbillData = {
                    inbillid:billID,
                    factoryid: factoryid,
                    time: time,
                    remarks: remarks,
                    allnumber: allnumber,
                    allprice: allprice,
                    purchases: purchaseList

                };

                console.log(updateInbillData);
                $http({
                    url: '/updateInbill',
                    method: 'POST',
                    data: updateInbillData
                }).then(function successCallback(res) {
                    if (res.data > 0) {
                        layer.msg('修改成功', {icon: 1, time: 1500});
                        $timeout(function () {
                            window.location.href = '/#/index/inbilllist';
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