tmqtApp.controller('userListController', ['$scope', '$rootScope', '$http','$stateParams', function($rootScope, $scope, $http,$stateParams) {

    var competence = $stateParams.type;

	layui.use(['element','form','table','jquery', 'layer'], function() {
		var table = layui.table,
            element = layui.element,//导航的hover效果、二级菜单等功能，需要依赖element模块
            form = layui.form,
            $ = layui.$,
            layer = layui.layer;
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
		//方法级渲染
		table.render({
			elem: '#LAY_table_users',
			url: '/getUserList?competence='+competence,
			cols: [
				[{
					checkbox: true,
					fixed: true
				}, {
					field: 'id',
					title: 'ID',
					width: 120,
					sort: true,
					fixed: true,
					align: 'center'
				}, {
					field: 'username',
					title: '用户名',
					width: 300,
					sort: true,
					align: 'center'
				},{
					field: 'remarks',
					title: '备注',
					sort: true,
					width: 500,
					align: 'center'
				},{
					field: 'addtime',
					title: '添加时间',
					sort: true,
					width: 250,
					align: 'center',
                    templet:'#addtimeTpl'
				}, {						
//					width:150,
					align: 'center',
					toolbar: '#toolBar'
				}, ]
			],
			id: 'usersTable',
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
            done:function () {
                layer.close(tableLoading);
            }
		});

        var active = {
                search: function() {
                    var username = $('#searchUserName').val();
                    var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                    // 执行重载
                    if(username != ""){
                        table.reload('usersTable', {
                            url: '/findUsers', //设置异步接口
                            page: false,
                            method: 'post',
                            where: {
                                username:username,
								competence:competence
                            },
                            done:function () {
                                layer.close(searchLoading);
                            }
                        });
                    }else {
                        layer.close(searchLoading);
                        layer.msg('请输入用户名！',{time:1800});
                    }
                },
                return: function(){
                    // 执行重载
                    var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                    table.reload('usersTable', {
                        url: '/getUserList?competence='+competence, //设置异步接口
                        page: true,
                        method: 'GET',
                        done:function () {
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
		table.on('tool(user)', function(obj) {
			var data = obj.data;
			if(obj.event === 'detail') {
				layer.msg('ID：' + data.id + ' 的查看操作');
			} else if(obj.event === 'del') {
				layer.confirm('确认删除本条记录？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delUser',
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
                    title: "修改用户",
                    closeBtn: false,
                    area: '400px;',
                    shade: 0.8,
                    id: 'LAY_userUpdate', //设定一个id，防止重复弹出
                    btn: ['确认修改', '取消操作'],
                    btnAlign: 'c',
                    moveType: 1, //拖拽模式，0或者1
                    content: '<div class="layui-field-box" style=""><form class="layui-form" lay-filter="add" action="">' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">原 密 码：</label>' +
                    '<div class="layui-input-inline"><input type="text" class="layui-input" id="updateUserOldPassword" placeholder="原密码"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">新 密 码：</label>' +
                    '<div class="layui-input-inline"><input type="text" class="layui-input" id="updateUserNewPassword" placeholder="密码"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备&#12288&#12288注：</label>' +
                    '<div class="layui-inline"><textarea name="updateUserRemarks" placeholder="请输入备注" id="updateUserRemarks" class="layui-textarea">'+data.remarks+'</textarea></div></div></div>' +
                    '</form></div>',
                    success: function(layero) {
                        layui.use(['form'], function() {
                            var form = layui.form;
                            form.render();
                        });
                    },
                    yes: function(index, layero) {
                        var updateLoading = layer.load(1,{shade: [0.1,'#000']});
                        var oldPassword = $('#updateUserOldPassword').val();
                        var newPassword = $('#updateUserNewPassword').val();
                        var remarks = $('#updateUserRemarks').val();
                        if(newPassword != '' && oldPassword!= ''){
                            $.ajax({
                                type: "post",
                                url: '/updateUser',
                                data: {
                                    id:data.id,
                                    oldPassWord: oldPassword,
                                    newPassWord: newPassword,
                                    remarks:remarks
                                },
                                success: function (res) {
                                    // 请求成功执行代码
                                    if(res>0){
                                        layer.close(index);
                                        layer.close(updateLoading);
                                        table.reload('usersTable', {});
                                        layer.msg('修改成功', {icon: 1,time:1000});
                                    }else{
                                        if(res === -1){
                                            layer.close(index);
                                            layer.close(updateLoading);
                                            layer.msg('原密码不正确，修改失败', {icon: 2,time:1500});
                                        }else{
                                            layer.close(index);
                                            layer.close(updateLoading);
                                            if(res === ""){
                                                layer.confirm('请登录以后在进行操作', {
                                                    btn: ['登录','取消'] //按钮
                                                }, function(){
                                                    layer.closeAll();
                                                    window.location.href='/#/login';
                                                });
                                            }else{
                                                layer.msg('修改失败', {icon: 2,time:1500});
                                            }

                                        }

                                    }
                                },
                                error:function () {
                                    // 请求失败执行代码
                                    layer.close(index);
                                    layer.close(updateLoading);
                                    layer.msg('修改失败', {icon: 2,time:1500});
                                }
                            });

                        }else {
                            layer.close(updateLoading);
                            layer.msg('请填写完整信息', {time:1500});
                        }
                    }
                });
			}
		});


        $('#addUserBtn').on('click', function() {
            layer.open({
                type: 1,
                title: "添加用户",
                closeBtn: false,
                area: '400px;',
                shade: 0.8,
                id: 'LAY_userAdd', //设定一个id，防止重复弹出
                btn: ['确认添加', '取消操作'],
                btnAlign: 'c',
                moveType: 1, //拖拽模式，0或者1
                content: '<div class="layui-field-box" style="">' +
                '<form class="layui-form" lay-filter="add" action="">' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">用  户  名：</label>' +
                '<div class="layui-input-inline"><input type="text" class="layui-input" id="addUserName" placeholder="用户名"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">密&#12288&#12288码：</label>' +
                '<div class="layui-input-inline"><input type="text" class="layui-input" id="addUserPassword" placeholder="密码"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备&#12288&#12288注：</label>' +
                '<div class="layui-inline"><textarea name="addUserRemarks" placeholder="请输入备注" id="addUserRemarks" class="layui-textarea"></textarea></div></div></div>' +
                '</form></div>',
                success: function(layero) {
                    layui.use(['form'], function() {
                        var form = layui.form;
                        form.render();
                    });
                },
                yes: function(index, layero) {
                    var addLoading = layer.load(1,{shade: [0.1,'#000']});
                    var username = $('#addUserName').val();
                    var password = $('#addUserPassword').val();
                    var remarks = $('#addUserRemarks').val();
                    if(username != ''&& password != ''){
                        $http({
                            url: '/addUser',
                            method: 'POST',
                            data: {
                                "username": username,
                                "password": password,
                                "competence":competence,
                                "remarks":remarks
                            }
                        }).then(function successCallback(res) {
                            // 请求成功执行代码
                            if(res.data>0){
                                layer.close(index);
                                layer.close(addLoading);
                                table.reload('usersTable', {});
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
                                    layer.msg('添加失败', {icon: 1,time:1500});
                                }
                            }
                        }, function errorCallback(res) {
                            // 请求失败执行代码
                            layer.close(index);
                            layer.close(addLoading);
                            layer.msg('添加失败', {icon: 1,time:1500});
                        });
                    }else {
                        layer.close(addLoading);
                        layer.msg('请填写完整信息', {icon: 1,time:1500});
                    }
                }
            });
        });

	});


}]);