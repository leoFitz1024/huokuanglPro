tmqtApp.controller('paymentCenterController', ['$scope','$rootScope', '$http','$filter','$timeout', function($rootScope,$scope, $http,$filter,$timeout) {
    $scope.allDebt = 0;
    $scope.allMoney = 0;
    $scope.allDiscountMoney = 0;
    $scope.allAmountpaid = 0;
    $scope.data = [];
    $scope.username = $rootScope.username;
    layui.use(['jquery', 'layer','table','laydate','element','form'], function() {

        var excelMonth = "";
        var excelStartMonth = "";
        var excelEndMonth = "";
        var excelFactoryid = "";
        var _curr_page = 1;
        var _limit = 10;

        var table = layui.table,
            element = layui.element,
            form = layui.form,
            $ = layui.$,
            layer = layui.layer,
            laydate = layui.laydate;
        //获取厂家列表
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
        $http({
            url: '/getAllFactory',
            method: 'GET',
        }).then(function successCallback(res) {
            $scope.factorys = res.data.data;
            $scope.$watch('$viewContentLoaded', function() {
                $timeout(function () {
                    form.render();
                    console.log('render');
                }, 1000);
            });

        }, function errorCallback(res) {
            // 请求失败执行代码
            console.log(res);
            layer.msg('获取厂家列表失败', {icon: 2,time:1500});
        });
        //日期插件
        form.render();
        //具体日期
        laydate.render({
            elem: '#paycentAccurateMonth',
            type: 'month'
        });
        laydate.render({
            elem: '#addPayMonth',
            type: 'month'
        });
        laydate.render({
            elem: '#addPayDay',
        });

        //范围选择
        //日期范围
        laydate.render({
            elem: '#paycentRangeMonth',
            range: true,
            type: 'month'
        });

        //方法级渲染
        table.render({
            elem: '#LAY_table_debt',
            url: '/getAllMonthCheck',
            cols: [
                [{
                    checkbox: true,
                    fixed: true
                }, {
                    field: 'id',
                    title: 'ID',
                    width: 120,
                    sort: true,
                    // fixed: true,
                    align: 'center'
                }, {
                    field: 'factory',
                    title: '厂家',
                    sort: true,
                    width: 300,
                    align: 'center'
                }, {
                    field: 'month',
                    title: '月份',
                    width: 250,
                    sort: true,
                    align: 'center',
                    templet:'#paymentMonth'
                }, {
                    field: 'allmoney',
                    title: '总金额',
                    width: 250,
                    sort: true,
                    align: 'center'
                }, {
                    field: 'discountmoney',
                    title: '优惠金额',
                    width: 240,
                    align: 'center',
                    edit:'text'
                }, {
                    field: 'amountpaid',
                    title: '已付金额',
                    sort: true,
                    width: 240,
                    align: 'center'
                }, {
                    field: 'debt',
                    title: '欠款',
                    sort: true,
                    width: 240,
                    align: 'center'
                }]
            ],
            id: 'debtTable',
            page: true,
            height: 'full-350',
            cellMinWidth: 80,
            initSort: {
                field: 'wealth',
                type: 'desc'
            },
            myError: function(res, statusText){
                layer.close(tableLoading);
                layer.closeAll();
                if(statusText == "parsererror"){
                    window.location.href='/#/login';
                }
            },
            done: function(res,curr){
                _curr_page = curr;
                $scope.data = res.data;
                layer.close(tableLoading);
            }
        });


            var active = {
                search: function () {
                    var month = "";
                    if ($('#paycentAccurateMonth').val() != 0) {
                        month = $('#paycentAccurateMonth').val() + "-1";
                    }
                    var rangeMonth = $('#paycentRangeMonth').val();
                    var factoryid = "";
                    var startMonth = "";
                    var endMonth = "";
                    if ($('#paycentfactoryid').val() != "") {
                        factoryid = $('#paycentfactoryid').val();
                    }
                    if (rangeMonth != "") {
                        startMonth = rangeMonth.split(" - ")[0];
                        endMonth = rangeMonth.split(" - ")[1];
                    }

                    if (month == "" || rangeMonth == "") {
                        var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                        table.reload('debtTable', {
                            url: '/findDebtMonthCheck', //设置异步接口
                            page: false,
                            method: 'post',
                            where: {
                                factoryid: factoryid,
                                month: month,
                                startMonth: startMonth,
                                endMonth: endMonth,
                            },
                            done: function(res,curr){
                                excelMonth = month;
                                excelStartMonth = startMonth;
                                excelEndMonth = endMonth;
                                excelFactoryid = factoryid;
                                _curr_page = curr;
                                _limit = 0;
                                layer.close(searchLoading);
                                $scope.data = res.data;
                                var allDebt = 0;
                                var allMoney = 0;
                                var allDiscountMoney = 0;
                                var allAmountpaid = 0;
                                for(index in res.data){
                                    allDebt = allDebt+ res.data[index].debt*100;
                                    allMoney = allMoney+ res.data[index].allmoney*100;
                                    allDiscountMoney = allDiscountMoney+ res.data[index].discountmoney*100;
                                    allAmountpaid = allAmountpaid+ res.data[index].amountpaid*100;

                                }
                                $scope.allDebt = allDebt/100;
                                $scope.allMoney = allMoney/100;
                                $scope.allDiscountMoney = allDiscountMoney/100;
                                $scope.allAmountpaid = allAmountpaid/100;
                                $scope.$apply();
                            }
                        });
                    } else {
                        layer.msg('具体月份和月份范围只能选取其中一个！', {time: 1800});
                    }
                },
                return: function(){
                    // 执行重载
                    $('#paymentCenterForm')[0].reset();
                    var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                    table.reload('debtTable', {
                        url: '/getAllMonthCheck', //设置异步接口
                        page: true,
                        method: 'GET',
                        done: function(res,curr){
                            excelMonth = "";
                            excelStartMonth = "";
                            excelEndMonth = "";
                            excelFactoryid = "";
                            _curr_page = curr;
                            _limit = 10;
                            layer.close(searchLoading);
                            $scope.data = res.data;
                            var allDebt = 0;
                            var allMoney = 0;
                            var allDiscountMoney = 0;
                            var allAmountpaid = 0;
                            for(index in res.data){
                                allDebt = allDebt+ res.data[index].debt;
                                allMoney = allMoney+ res.data[index].allmoney;
                                allDiscountMoney = allDiscountMoney+ res.data[index].discountmoney;
                                allAmountpaid = allAmountpaid+ res.data[index].amountpaid;
                            }
                            $scope.allDebt = allDebt;
                            $scope.allMoney = allMoney;
                            $scope.allDiscountMoney = allDiscountMoney;
                            $scope.allAmountpaid = allAmountpaid;
                            $scope.$apply();
                        }
                    });
                }
            }

        $('.reloadBtn').on('click', function() {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        //监听单元格编辑
        table.on('edit(debt)', function(obj){
            var updateLoading = layer.load(1,{shade: [0.1,'#000']});
            var value = obj.value //得到修改后的值
                ,data = obj.data //得到所在行所有键值
                ,field = obj.field; //得到字段
            $http({
                url: '/updateMonthcheck',
                method: 'POST',
                data: {
                    "id":data.id,
                    "discountmoney": value
                }
            }).then(function successCallback(res) {
                // 请求成功执行代码
                layer.close(updateLoading);
                layer.msg('修改成功', {icon: 1,time:1000});
            }, function errorCallback(res) {
                // 请求失败执行代码
                layer.close(updateLoading);
                layer.msg('修改失败', {icon: 2,time:1500});
            });

        });

        //导出excel
        $scope.exportToExcel = function(){
            window.open("/exportMonthCheckList?month="+excelMonth + "&startMonth="+excelStartMonth+"&endMonth="+excelEndMonth+"&factoryid="+excelFactoryid+"&page="+_curr_page+"&limit="+_limit,"_blank");
        }

    });
	
}]);