tmqtApp.controller('purchaseListController', ['$scope', '$rootScope', '$http', '$filter','$timeout', function($rootScope, $scope, $http, $filter,$timeout) {
    console.log('purchaseListController');
    $scope.username = $rootScope.username;
	layui.use(['laydate','form','table','element','jquery', 'layer'], function() {
		var table = layui.table,
            laydate = layui.laydate,
            form = layui.form,
            $ = layui.$,
            layer = layui.layer;
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
        form.render();
        //具体日期
        laydate.render({
            elem: '#purchaseAccurateTime',
        });

        //范围选择
        //日期范围
        laydate.render({
            elem: '#purchaseRangeTime',
            range: true
        });
		//方法级渲染
		table.render({
			elem: '#LAY_table_purchase',
			url: '/getPurchaseList',
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
					field: 'time',
					title: '进货日期',
					width: 130,
					sort: true,
					align: 'center',
                    templet:'#dateTpl'
				}, {
					field: 'factory',
					title: '厂家',
					sort: true,
					width: 180,
					align: 'center'
				}, {
					field: 'code',
					title: '货号',
					width: 100,
					sort: true,
					align: 'center'
				}, {
					field: 'rangesize',
					title: '码段',
					width: 80,
					align: 'center'
				}, {
					field: 'color',
					title: '颜色',
					sort: true,
					width: 90,
					align: 'center'
				}, {
					field: 'price',
					title: '单价',
					sort: true,
					width: 80,
					align: 'center'
				}, {
					field: 'number',
					title: '数量',
					sort: true,
					width: 90,
					align: 'center'
				}, {
					field: 'allprice',
					title: '总价',
					sort: true,
					width: 90,
					align: 'center'
				}, {
					field: 'remarks',
					title: '备注',
					sort: true,
					width: 380,
					align: 'center'
				}, {
					field: 'addtime',
					title: '记录时间',
					sort: true,
					width: 180,
					align: 'center',
                    templet:'#addtimeTpl'
				}, {
					//					width:150,
					align: 'center',
					toolbar: '#toolBar'
				}, ]
			],
			id: 'purchaseTable',
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
            done: function(res){
                layer.close(tableLoading);
                $scope.data = res.data;
                $scope.$apply();
            }
		});

		var $ = layui.$,
			active = {
				search: function() {
                    var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                    var time = "";
                    var rangeTime = $('#purchaseRangeTime').val();
                    var startTime = "";
                    var endTime = "";
                    if($('#purchaseAccurateTime').val() !="") {
                        time = new Date($('#purchaseAccurateTime').val()).getTime()/1000;
                    }
                    if(rangeTime != ""){
                        startTime = rangeTime.split(" - ")[0];
                        endTime = rangeTime.split(" - ")[1];
                    }

                    var purchasefactoryid = $('#purchasefactoryid').val();
                    var purchaseColor = $('#purchaseColor').val();
                    var purchaseCode = $('#purchaseCode').val();
                    // 执行重载
					if(time == "" || rangeTime == ""){
                        table.reload('purchaseTable', {
                            url: '/findPurchase', //设置异步接口
                            page: false,
                            method: 'post',
                            where: {
                                time:time,
                                startTime:startTime,
                                endTime:endTime,
                                factoryid:purchasefactoryid,
                                color:purchaseColor,
                                code:purchaseCode
                            },
                            done: function(res){
                                layer.close(searchLoading);
                                $scope.data = res.data;
                                $scope.$apply();
                            },
                        });
					}else {
                        layer.msg('具体日期和日期范围只能选取其中一个！',{time:1800});
					}
				},
                return: function(){
                    // 执行重载
                    var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                    $('#purchaseForm')[0].reset();
                    table.reload('purchaseTable', {
                        url: '/getPurchaseList', //设置异步接口
                        page: true,
                        method: 'GET',
                        done: function(res){
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
		table.on('tool(purchase)', function(obj) {
			var data = obj.data;
			if(obj.event === 'del') {
				layer.confirm('确认删除本条记录？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delPurchase',
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

            $http({
                url: '/getAllRangesize',
                method: 'GET',
            }).then(function successCallback(res) {
                $scope.rangesizes = res.data.data;
                var rangesizeStr = "";
                for (var index in $scope.rangesizes) {
                    rangesizeStr = rangesizeStr + "<option value='" + $scope.rangesizes[index].rangesize + "'>" + $scope.rangesizes[index].rangesize + "</option>";
                }
                $scope.$watch('$viewContentLoaded', function () {
                    $timeout(function () {
                        form.render();
                        console.log('render');
                    }, 500);
                });

                $('#addPurchaseBtn').on('click', function() {
                    layer.open({
                        type: 1,
                        title: "添加记录",
                        closeBtn: false,
                        area: '400px;',
                        shade: 0.8,
                        id: 'LAY_purchaseAdd', //设定一个id，防止重复弹出
                        btn: ['确认添加', '取消操作'],
                        btnAlign: 'c',
                        moveType: 1, //拖拽模式，0或者1
                        content: '<div class="layui-field-box" style="">' +
                        '<form class="layui-form" lay-filter="add" action="">' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">货&#12288&#12288号：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addPurchaseCode" placeholder="货号"></div></div></div>' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">厂&#12288&#12288家：</label><div class="layui-input-inline"><select name="addPurchaseFactory" id="addPurchaseFactory"><option value="" selected>请选择厂家</option>'+ factoryStr +'</select></div></div></div>' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">日&#12288&#12288期：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addPurchaseDate" placeholder="年-月-日"></div></div></div>' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">码&#12288&#12288段：</label><div class="layui-input-inline"><select name="addPurchaseSize" id="addPurchaseSize"><option value="" selected>请选择码段</option>'+ rangesizeStr +'</select></div></div></div>' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">顏&#12288&#12288色：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addPurchaseColor" placeholder="顏色"></div></div></div>' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">价&#12288&#12288格：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addPurchasePrice" placeholder="价格"></div></div></div>' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">数&#12288&#12288量：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addPurchaseNumber" placeholder="数量"></div></div></div>' +
                        '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备&#12288&#12288注：</label><div class="layui-inline"><textarea name="addPurchaseRemarks" placeholder="请输入备注" id="addPurchaseRemarks" class="layui-textarea"></textarea></div></div></div>' +
                        '</form></div>',
                        success: function(layero) {
                            form.render();
                            //具体日期
                            laydate.render({
                                elem: '#addPurchaseDate'
                            });
                        },
                        yes: function(index, layero) {
                            var addLoading = layer.load(1,{shade: [0.1,'#000']});
                            var date = new Date($('#addPurchaseDate').val());
                            var factory = $('#addPurchaseFactory').val();
                            var rangesize = $('#addPurchaseSize').val();
                            var color = $('#addPurchaseColor').val();
                            var code = $('#addPurchaseCode').val();
                            var price = $('#addPurchasePrice').val();
                            var number = $('#addPurchaseNumber').val();
                            var allprice = price*number;
                            var remarks = $('#addPurchaseRemarks').val();
                            $http({
                                url: '/addPurchase',
                                method: 'POST',
                                data: {
                                    "time": date,
                                    "factoryid": factory,
                                    "rangesize":rangesize,
                                    "code":code,
                                    "color":color,
                                    "price":price,
                                    "number":number,
                                    "allprice":allprice,
                                    "remarks":remarks
                                }
                            }).then(function successCallback(res) {
                                // 请求成功执行代码
                                if(res.data>0){
                                    layer.close(index);
                                    layer.close(addLoading);
                                    table.reload('purchaseTable', {});
                                    layer.msg('添加成功', {icon: 1,time:1000});
                                }else{
                                    layer.close(index);
                                    layer.close(addLoading);
                                    if(res === ""){
                                        layer.confirm('请登录以后在进行操作', {
                                            btn: ['登录','取消'] //按钮
                                        }, function(){
                                            layer.closeAll();
                                            window.location.href='/#/login';
                                        });
                                    }else{
                                        layer.msg('添加失败', {icon: 2,time:1500});
                                    }

                                }
                            }, function errorCallback(res) {
                                // 请求失败执行代码
                                layer.close(index);
                                layer.close(addLoading);
                                layer.msg('添加失败', {icon: 2,time:1500});
                            });
                        }
                    });
                });
            }, function errorCallback(res) {
                // 请求失败执行代码
                layer.msg('获取码段列表失败', {icon: 2,time:1500});
            });
        }, function errorCallback(res) {
            // 请求失败执行代码
            layer.msg('获取厂家列表失败', {icon: 2,time:1500});
        });

    });
	//导出excel
    $scope.exportToExcel = function(data){
        var data = angular.copy(data)
        var arr = [];
        angular.forEach(data,function (item) {
            arr.push({
                'ID':item.id,
                '进货日期':$filter('date')(item.time,'yyyy-MM-dd'),
                '厂家':item.factory,
                '货号':item.code,
                '码段':item.rangesize,
                '颜色':item.color,
                '单价':item.price,
                '数量':item.number,
                '总价':item.allprice,
                '备注':item.remarks,
                '记录时间':$filter('date')(item.addtime,'yyyy-MM-dd HH:mm:ss')
            })
        });

        // 导出文件的格式
        var excelStyle = {
            headers:true,
            column: {
                style:{
                Font:{Bold:"1",Size:"11"}
                }
            }
        };
        var excelData = arr;
        if(excelData.length < 1){
            layer.msg('暂无数据，数据导出失败', {icon: 2,time:1500});
        }else{

            alasql.promise('SELECT * INTO XLSXML("进货记录-'+ getNowFormatDate() + "-"+ $scope.username +'.xls",?) FROM ?',[excelStyle,excelData])
                .then(function (data) {
                    if(data == 1){
                        $timeout(function(){
                            layer.msg('数据导出成功', {icon: 1,time:1500});
                        })
                    }else{
                        $timeout(function(){
                            layer.msg('数据导出失败', {icon: 2,time:1500});
                        })
                    }
                })
        }
    }


}]);
