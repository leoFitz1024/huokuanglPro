/**
 * Created by chenw on 2018/7/30.
 */
tmqtApp.controller('rangeSizeListController', ['$scope', '$http',function($scope, $http) {

    layui.use(['jquery', 'layer','table'], function() {
        var $ = layui.$ //重点处
            ,
            layer = layui.layer;
        var table = layui.table;

        //方法级渲染
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
        table.render({
            elem: '#LAY_table_rangeSize',
            url: '/getRangesizeList',
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
                    field: 'rangesize',
                    title: '码段',
                    width: 250,
                    sort: true,
                    align: 'center'
                }, {
                    field: 'remarks',
                    title: '备注',
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
            id: 'rangeSizeTable',
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
            done: function(){
                layer.close(tableLoading);
            }
        });
        // var $ = layui.$, active = {
        //     search: function(){
        //         var factoryname = $('#factoryname').val();
        //         var searchLoading = layer.load(1,{shade: [0.1,'#000']});
        //         // 执行重载
        //         table.reload('factoryTable', {
        //             url: '/findFactory', //设置异步接口
        //             page: false,
        //             where: {
        //                 factoryname:factoryname
        //             },
        //             done: function(){
        //                 layer.close(searchLoading);
        //             }
        //         });
        //     },
        //     return: function(){
        //         // 执行重载
        //         var searchLoading = layer.load(1,{shade: [0.1,'#000']});
        //         table.reload('rangeSizeTable', {
        //             url: '/getRangesizeList', //设置异步接口
        //             page: true,
        //             done: function(){
        //                 layer.close(searchLoading);
        //             }
        //         });
        //     }
        // };

        // $('.reloadBtn').on('click', function() {
        //     var type = $(this).data('type');
        //     active[type] ? active[type].call(this) : '';
        // });

        //监听工具条
        table.on('tool(rangeSize)', function(obj) {
            var data = obj.data;
            if(obj.event === 'del') {
                layer.confirm('确认删该码段？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delRangesize',
                        data: {id:data.id},
                        success: function (res) {
                            if(res >0){
                                obj.del();
                                layer.close(delLoading);
                                layer.msg('删除成功', {icon: 1,time:1000});
                            }else{
                                layer.close(delLoading);
                                layer.msg('删除失败', {icon: 2,time:1000});
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
                    title: "修改码段",
                    closeBtn: false,
                    area: '400px;',
                    shade: 0.8,
                    id: 'LAY_rangesizeUpdate', //设定一个id，防止重复弹出
                    btn: ['提交修改', '放弃修改'],
                    btnAlign: 'c',
                    moveType: 1, //拖拽模式，0或者1
                    content: '<div class="layui-field-box"><form class="layui-form" action="">' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">码段：</label><div class="layui-input-inline"><input type="text" class="layui-input" value=' + data.rangesize + ' id="updateRangeSizeStr" placeholder="码段"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备注：</label><div class="layui-input-inline"><textarea placeholder="备注"  class="layui-textarea" id="updateRangeSizeRemarks">' + data.remarks + '</textarea></div></div></div></form></div>',
                    success: function(layero) {

                    },
                    yes: function(index, layero) {
                        var updateLoading = layer.load(1,{shade: [0.1,'#000']});
                        var rangeSizeStr = $('#updateRangeSizeStr').val();
                        var remarks = $('#updateRangeSizeRemarks').val();
                        $http({
                            url: '/updateRangesize',
                            method: 'POST',
                            data: {
                                "id":data.id,
                                "rangesize": rangeSizeStr,
                                "remarks": remarks
                            }
                        }).then(function successCallback(res) {
                            // 请求成功执行代码
                            layer.close(index);
                            layer.close(updateLoading);
                            table.reload('rangeSizeTable', {});
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

        //后面就跟你平时使用jQuery一样
        $('#addRangeSizeBtn').on('click', function() {
            layer.open({
                type: 1,
                title: "添加码段",
                closeBtn: false,
                area: '400px;',
                shade: 0.8,
                id: 'LAY_rangesizeAdd', //设定一个id，防止重复弹出
                btn: ['确认添加', '取消操作'],
                btnAlign: 'c',
                moveType: 1, //拖拽模式，0或者1
                content: '<div class="layui-field-box"><form class="layui-form" action="">' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">码段：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addRangesizeStr" placeholder="码段"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备注：</label><div class="layui-input-inline"><textarea placeholder="备注" class="layui-textarea" id="addRangesizeRemaeks"></textarea></div></div></div></form></div>',
                success: function(layero) {},
                yes: function(index, layero) {
                    var addLoading = layer.load(1,{shade: [0.1,'#000']});
                    var rangesizeStr = $('#addRangesizeStr').val();
                    var remarks = $('#addRangesizeRemaeks').val();

                    if(rangesizeStr != "") {
                        $http({
                            url: '/addRangesize',
                            method: 'POST',
                            data: {
                                "rangesize": rangesizeStr,
                                "remarks": remarks
                            }
                        }).then(function successCallback(response) {
                            // 请求成功执行代码
                            layer.close(index);
                            layer.close(addLoading);
                            table.reload('rangeSizeTable', {});
                            layer.msg('添加成功', {icon: 1,time:1000});
                        }, function errorCallback(response) {
                            // 请求失败执行代码
                            layer.close(index);
                            layer.close(addLoading);
                            layer.msg('添加失败', {icon: 2,time:1500});
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

