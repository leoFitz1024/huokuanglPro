tmqtApp.controller('paymentListController', ['$scope','$rootScope', '$http','$timeout', '$filter', function($rootScope,$scope, $http, $timeout, $filter) {
    $scope.username = $rootScope.username;
	layui.use(['jquery','element','form','table','laydate','layer'], function() {

        var excelMonth = "";
        var excelStartMonth = "";
        var excelEndMonth = "";
        var excelStartPaytime = "";
        var excelEndPaytime = "";
        var excelFactoryid = "";
        var _curr_page = 1;
        var _limit = 10;

		var table = layui.table;
            element = layui.element,
            form = layui.form,
            laydate = layui.laydate,
            layer = layui.layer,
            $ = layui.$;
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
        //日期插件
        form.render();
        //具体日期
        laydate.render({
            elem: '#paymentAccurateMonth',
            type: 'month'
        });

        //范围选择
        //日期范围
        laydate.render({
            elem: '#paymentRangeMonth',
            range: true,
            type: 'month'
        });
        laydate.render({
            elem: '#paymentRangePaytime',
            range: true,
        });
		//方法级渲染
		table.render({
			elem: '#LAY_table_payment',
			url: '/getPaymentList',
			cols: [
				[{
					checkbox: true,
					fixed: true
				}, {
					field: 'id',
					title: 'ID',
					width: 100,
					sort: true,
					fixed: true,
					align: 'center'
				}, {
					field: 'month',
					title: '结算月份',
					width: 150,
					sort: true,
					align: 'center',
                    templet:'#paymentMonth'
				}, {
					field: 'factory',
					title: '厂家',
					sort: true,
					width: 250,
					align: 'center'
				}, {
					field: 'money',
					title: '金额',
					width: 120,
					sort: true,
					align: 'center'
				}, {
					field: 'paytime',
					title: '付款时间',
					sort: true,
					width: 180,
					align: 'center',
                    templet:'#paymentPaytime'
				}, {
					field: 'remarks',
					title: '备注',
					sort: true,
					width: 480,
					align: 'center'
				}, {
					field: 'addtime',
					title: '记录时间',
					sort: true,
					width: 200,
					align: 'center',
                    templet:'#addtimeTpl'
				}, {
					//					width:150,
					align: 'center',
					toolbar: '#toolBar'
				}, ]
			],
			id: 'paymentTable',
			page: true,
			height: 'full-300',
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
            done:function (res,curr) {
                _curr_page = curr;
                $scope.data = res.data;
                layer.close(tableLoading);
            }
		});

        var  active = {
                search: function() {
                    var month = "";
                    var rangeMonth = $('#paymentRangeMonth').val();
                    var rangePaytime = $('#paymentRangePaytime').val();
                    var startMonth = "";
                    var endMonth = "";
                    var startPaytime = "";
                    var endPaytime = "";
                    if($('#paymentAccurateMonth').val() !="") {
                        month = new Date($('#paymentAccurateMonth').val()).getTime()/1000;
                    }
                    if(rangeMonth != ""){
                        startMonth = rangeMonth.split(" - ")[0];
                        endMonth = rangeMonth.split(" - ")[1];
                    }
                    if(rangePaytime != ""){
                        startPaytime = rangePaytime.split(" - ")[0];
                        endPaytime = rangePaytime.split(" - ")[1];
                    }

                    var paymentfactoryid = $('#paymentfactoryid').val();
                    // 执行重载
                    if(month == "" || rangeMonth == ""){



                        var searchLoading = layer.load(1,{shade: [0.1,'#000']});

                        table.reload('paymentTable', {
                            url: '/findPayment', //设置异步接口
                            page: false,
                            method: 'post',
                            where: {
                                month:month,
                                factoryid:paymentfactoryid,
                                startMonth:startMonth,
                                endMonth:endMonth,
                                startPaytime:startPaytime,
                                endPaytime:endPaytime
                            },
                            done:function (res,curr) {
                                excelMonth = month;
                                excelStartMonth = startMonth;
                                excelEndMonth = endMonth;
                                excelStartPaytime = startPaytime;
                                excelEndPaytime = endPaytime;
                                excelFactoryid = paymentfactoryid;
                                _curr_page = curr;
                                _limit = 0;
                                $scope.data = res.data;
                                layer.close(searchLoading);
                            }
                        });
                    }else {
                        layer.close(searchLoading);
                        layer.msg('具体月份和月份范围只能选取其中一个！',{time:1800});
                    }
                },
                return: function(){
                    // 执行重载
                    var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                    $('#paymentForm')[0].reset();
                    table.reload('paymentTable', {
                        url: '/getPaymentList', //设置异步接口
                        page: true,
                        method: 'GET',
                        done:function (res,curr) {
                            excelMonth = "";
                            excelStartMonth = "";
                            excelEndMonth = "";
                            excelStartPaytime = "";
                            excelEndPaytime = "";
                            excelFactoryid = "";
                            _curr_page = curr;
                            _limit = 10;
                            $scope.data = res.data;
                            layer.close(searchLoading);
                        }
                    });
                }
            };

        $('.reloadBtn').on('click', function() {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
		
		//监听工具条
		table.on('tool(payment)', function(obj) {
			var data = obj.data;
			if(obj.event === 'del') {
				layer.confirm('确认删除本条记录？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delPayment',
                        data: {id:data.id},
                        success: function (res) {
                            if(res >0){
                                obj.del();
                                layer.close(delLoading);
                                layer.msg('删除成功', {icon: 1,time:1000});
                            }else{
                                layer.close(delLoading);
                                if(res === ""){
                                    layer.confirm('请登录以后在进行操作', {
                                        btn: ['登录','取消'] //按钮
                                    }, function(){
                                        layer.closeAll();
                                        window.location.href='/#/login';
                                    });
                                }else{
                                    layer.msg('删除失败', {icon: 2,time:1000});
                                }
                            }
                        },
                        error:function () {
                            layer.close(delLoading);
                            layer.msg('删除失败', {icon: 2,time:1000});
                        }
                    });
				});
			}
		});

        $http({
            url: '/getAllFactory',
            method: 'GET',
        }).then(function successCallback(res) {
            // 请求成功执行代码
            $scope.factorys = res.data.data;
            var factoryStr = "";

            for(var index in $scope.factorys){
                factoryStr = factoryStr + "<option value='"+$scope.factorys[index].id + "'>"+$scope.factorys[index].factoryname +"</option>";
            }

            $scope.$watch('$viewContentLoaded', function() {
                $timeout(function () {
                    form.render();
                    console.log('render');
                }, 500);
            });

            //后面就跟你平时使用jQuery一样
            $('#addPaymentBtn').on('click', function() {
                layer.open({
                    type: 1,
                    title: "添加付款记录",
                    closeBtn: false,
                    area: '400px;',
                    shade: 0.8,
                    id: 'LAY_paymentAdd', //设定一个id，防止重复弹出
                    btn: ['确认添加', '取消操作'],
                    btnAlign: 'c',
                    moveType: 1, //拖拽模式，0或者1
                    content: '<div class="layui-field-box" style="">' +
                    '<form class="layui-form" lay-filter="update" action="">' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">厂&#12288&#12288家：</label><div class="layui-input-inline"><select name="addPayfactoryid" id="addPayfactoryid"><option value="" selected>请选择厂家</option>'+ factoryStr +'</select></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">结算月份：</label><div class="layui-input-inline paycen-input"><input type="text" class="layui-input" id="addPayMonth" placeholder="年-月"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">付款日期：</label><div class="layui-input-inline paycen-input"><input type="text" class="layui-input" id="addPayDay" placeholder="年-月-日"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">付款金额：</label><div class="layui-input-inline paycen-input"><input type="text" class="layui-input" id="addPayMoney" placeholder="金额"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备&#12288&#12288注：</label><div class="layui-input-inline paycen-input"><textarea name="addPayRemarks" placeholder="请输入备注" id="addPayRemarks" class="layui-textarea"></textarea></div></div></div>' +
                    '</form></div>',
                    success: function(layero) {
                        form.render();
                        var laydate = layui.laydate;
                        //具体日期
                        laydate.render({
                            elem: '#addPayDay'
                        });
                        laydate.render({
                            elem: '#addPayMonth',
                            type: 'month'
                        });
                    },
                    yes: function(index, layero) {
                        var factory = $('#addPayfactoryid').val();
                        var monthStr = $('#addPayMonth').val();
                        var month = new Date(monthStr);
                        var money = $('#addPayMoney').val()
                        var paytimeStr = $('#addPayDay').val();
                        var paytime = new Date(paytimeStr);
                        var remarks = $('#addPayRemarks').val();
                        if(factory == "" || monthStr == "" || money == "" || paytimeStr == ""){
                            layer.msg('请填写完整的数据', {icon: 1,time:1500});
                        }else{
                            var addLoading = layer.load(1,{shade: [0.1,'#000']});
                            $http({
                                url: '/addPayment',
                                method: 'POST',
                                data: {
                                    "factoryid": factory,
                                    "month": month,
                                    "paytime": paytime,
                                    "money": money,
                                    "remarks":remarks
                                }
                            }).then(function successCallback(res) {
                                // 请求成功执行代码
                                if(res.data>0){
                                    layer.close(index);
                                    layer.close(addLoading);
                                    table.reload('paymentTable', {});
                                    layer.msg('添加成功', {icon: 1,time:1000});
                                }else{
                                    if(res.data == -1){
                                        layer.close(index);
                                        layer.close(addLoading);
                                        layer.msg('该月份并无所选厂家欠款记录', {icon: 1,time:2000});
                                    }else if(res.data == -2){
                                        layer.close(index);
                                        layer.close(addLoading);
                                        layer.msg('付款金额超出该月份该厂家欠款金额', {icon: 1,time:2000});
                                    }else {
                                        layer.close(index);
                                        layer.close(addLoading);
                                        layer.msg('添加失败', {icon: 1,time:1500});
                                    }
                                }
                            }, function errorCallback(res) {
                                // 请求失败执行代码
                                layer.close(addLoading);
                                layer.msg('添加失败', {icon: 1,time:1500});
                            });
                        }
                    }
                });
            });
        }, function errorCallback(res) {
            // 请求失败执行代码
            layer.msg('获取厂家列表失败', {icon: 1,time:1500});
        });

        //导出excel
        $scope.exportToExcel = function(){
            window.open("/exportPaymentList?month="+excelMonth + "&startMonth="+excelStartMonth+"&endMonth="+excelEndMonth+"&startPaytime="+excelStartPaytime+"&endPaytime="+excelEndPaytime+"&factoryid="+excelFactoryid+"&page="+_curr_page+"&limit="+_limit,"_blank");
        }

	});


}]);