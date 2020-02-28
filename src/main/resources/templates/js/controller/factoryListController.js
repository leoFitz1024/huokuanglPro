tmqtApp.controller('factoryListController', ['$scope', '$http',function($scope, $http) {
    console.log('factoryListController');

	//表格
	layui.use('table', function() {
		var table = layui.table;

		//方法级渲染
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
		table.render({
			elem: '#LAY_table_factory',
			url: '/getFactoryList',
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
					field: 'factoryname',
					title: '厂家名',
					width: 250,
					sort: true,
					align: 'center'
				}, {
					field: 'address',
					title: '厂家地址',
					sort: true,
					width: 800,
					align: 'center'
				}, {
					field: 'addtime',
					title: '添加时间',
					width: 200,
					sort: true,
					align: 'center',
                    templet:'#addtimeTpl'
				}, {
					//					width:150,
					align: 'center',
					toolbar: '#toolBar'
				}]
			],
			id: 'factoryTable',
			page: true,
			height: 'full-300',
			cellMinWidth: 80,
			initSort: {
				field: 'wealth',
				type: 'desc'
			},
            myError: function(res, statusText){
                layer.close(tableLoading);
            	if(statusText == "parsererror"){
                    window.location.href='/#/login';
				}
            },
            done: function(){
                layer.close(tableLoading);
            }
		});
        var $ = layui.$, active = {
            search: function(){
                var factoryname = $('#factoryname').val();
                var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                // 执行重载
                table.reload('factoryTable', {
                    url: '/findFactory', //设置异步接口
                    page: false,
					where: {
						factoryname:factoryname
                    },
                    done: function(){
                        layer.close(searchLoading);
                    }
                });
            },
            return: function(){
                // 执行重载
                var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                table.reload('factoryTable', {
                    url: '/getFactoryList', //设置异步接口
                    page: true,
					done: function(){
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
		table.on('tool(factory)', function(obj) {
			var data = obj.data;
			if(obj.event === 'del') {
				layer.confirm('确认删除该厂家？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delFactory',
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
			} else if(obj.event === 'edit') {
				layer.open({
					type: 1,
					title: "修改厂家",
					closeBtn: false,
					area: '400px;',
					shade: 0.8,
					id: 'LAY_factoryUpdate', //设定一个id，防止重复弹出
					btn: ['提交修改', '放弃修改'],
					btnAlign: 'c',
					moveType: 1, //拖拽模式，0或者1
					content: '<div class="layui-field-box"><form class="layui-form" action="">' +
					'<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">厂家名称：</label><div class="layui-input-inline"><input type="text" class="layui-input" value=' + data.factoryname + ' id="updateFactoryName" placeholder="厂家名称"></div></div></div>' +
					'<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">地&#12288&#12288址：</label><div class="layui-input-inline"><textarea placeholder="厂家地址"  class="layui-textarea" id="updateFactoryAddress">' + data.address + '</textarea></div></div></div></form></div>',
					success: function(layero) {

					},
					yes: function(index, layero) {
                        var updateLoading = layer.load(1,{shade: [0.1,'#000']});
						var factoryname = $('#updateFactoryName').val();
						var address = $('#updateFactoryAddress').val();
                        $http({
                            url: '/updateFactory',
                            method: 'POST',
                            data: {
                            	"id":data.id,
                                "factoryname": factoryname,
                                "address": address
                            }
                        }).then(function successCallback(res) {
                            // 请求成功执行代码
                            layer.close(index);
                            layer.close(updateLoading);
                            table.reload('factoryTable', {});
                            layer.msg('修改成功', {icon: 1,time:1000});
                        }, function errorCallback(res) {
                            // 请求失败执行代码
                            layer.close(index);
                            layer.close(updateLoading);
                            layer.msg('修改失败', {icon: 2,time:1500});
                        });
					}
				});
			}
		});
	});


	//主动加载jquery模块
layui.use(['jquery', 'layer','table'], function() {
	var $ = layui.$ //重点处
		,
		layer = layui.layer;
	var table = layui.table;

	//后面就跟你平时使用jQuery一样
	$('#addFactoryBtn').on('click', function() {
		layer.open({
			type: 1,
			title: "添加厂家",
			closeBtn: false,
			area: '400px;',
			shade: 0.8,
			id: 'LAY_factoryAdd', //设定一个id，防止重复弹出
			btn: ['确认添加', '取消操作'],
			btnAlign: 'c',
			moveType: 1, //拖拽模式，0或者1
			content: '<div class="layui-field-box"><form class="layui-form" action=""><div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">厂家名称：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addFactoryName" placeholder="厂家名称"></div></div></div><div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">地&#12288&#12288址：</label><div class="layui-input-inline"><textarea placeholder="厂家地址" class="layui-textarea" id="addFactoryAddress"></textarea></div></div></div></form></div>',
			success: function(layero) {},
			yes: function(index, layero) {
                var addLoading = layer.load(1,{shade: [0.1,'#000']});
				var factoryname = $('#addFactoryName').val();
				var address = $('#addFactoryAddress').val();

				if(factoryname != "") {
					$http({
						url: '/addFactory',
						method: 'POST',
						data: {
							"factoryname": factoryname,
							"address": address
						}
					}).then(function successCallback(response) {
						// 请求成功执行代码
                        layer.close(index);
                        layer.close(addLoading);
                        table.reload('factoryTable', {});
                        layer.msg('添加成功', {icon: 1,time:1000});
					}, function errorCallback(response) {
						// 请求失败执行代码
                        layer.close(index);
                        layer.close(addLoading);
                        layer.msg('添加失败', {icon: 1,time:1500});
					});
				} else {
                    layer.close(addLoading);
					layer.msg("请输入完整信息", {
						time: 1500
					})
				}
			}
		});
	});
});

}]);

