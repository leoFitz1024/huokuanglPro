/**
 * Created by chenw on 2018/12/15.
 */
tmqtApp.controller('inbillListController', ['$scope', '$rootScope', '$http', '$filter','$timeout', function($rootScope, $scope, $http, $filter,$timeout) {
    console.log('inbillListController');
    $scope.username = $rootScope.username;
    layui.use(['laydate','form','table','element','jquery', 'layer'], function() {
        
        //用于查找筛选的全局参数 为了导出数据与展示数据一致
        var excelPurchasefactoryid = "";
        var excelTime = "";
        var excelStartTime = "";
        var excelEndTime = "";
        var _curr_page = 1;
        var _limit = 10;

        
        var table = layui.table,
            laydate = layui.laydate,
            form = layui.form,
            $ = layui.$,
            layer = layui.layer,
            element = layui.element;
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
        //具体日期
        laydate.render({
            elem: '#inbillAccurateTime',
        });

        //范围选择
        //日期范围
        laydate.render({
            elem: '#inbillRangeTime',
            range: true
        });

        //获取厂家列表
        $http({
            url: '/getAllFactory',
            method: 'GET',
        }).then(function successCallback(res) {
            // 请求成功执行代码
            $scope.factorys = res.data.data;
            $scope.$watch('$viewContentLoaded', function() {
                $timeout(function () {
                    form.render();
                }, 1000);
            });
        }, function errorCallback(res) {
            // 请求失败执行代码
            layer.msg('获取厂家列表失败', {icon: 2,time:1500});
        });
        form.render();
        //方法级渲染
        var dataTable = table.render({
            elem: '#LAY_table_inbill',
            url: '/getInbillList',
            cols: [
                [{
                    checkbox: true,
                    fixed: true
                }, {
                    field: 'id',
                    title: 'ID',
                    width: 60,
                    sort: true,
                    fixed: true,
                    align: 'center'
                }, {
                    field: 'time',
                    title: '进货日期',
                    width: 180,
                    sort: true,
                    align: 'center',
                    templet:'#dateTpl'
                }, {
                    field: 'factory',
                    title: '厂家',
                    sort: true,
                    width: 200,
                    align: 'center'
                },{
                    field: 'allnumber',
                    title: '总数',
                    sort: true,
                    width: 120,
                    align: 'center'
                }, {
                    field: 'allprice',
                    title: '总金额',
                    sort: true,
                    width: 120,
                    align: 'center'
                }, {
                    field: 'remarks',
                    title: '备注',
                    sort: true,
                    width: 380,
                    align: 'center'
                }, {
                    field: 'verifystate',
                    title: '校验状态',
                    width: 90,
                    align: 'center',
                    templet:'#verifystateTpl'
                },{
                    field: 'addtime',
                    title: '记录时间',
                    sort: true,
                    width: 180,
                    align: 'center',
                    templet:'#addtimeTpl'
                }, {
                    align: 'center',
                    toolbar: '#toolBar'
                }, ]
            ],
            id: 'inbillTable',
            page: true,
            limit:10,
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
            done: function(res,curr){
                _curr_page = curr;
                layer.close(tableLoading);
                $scope.data = res.data;
                $scope.$apply();
            }
        });

        var $ = layui.$,
            active = {
                search: function() {
                    var purchasefactoryid = "";
                    var remarksKey = "";
                    var time = "";
                    var startTime = "";
                    var endTime = "";

                    var rangeTime = $('#inbillRangeTime').val();
                    if($('#inbillAccurateTime').val() !="") {
                        time = new Date($('#inbillAccurateTime').val()).getTime()/1000;
                    }
                    if(rangeTime != ""){
                        startTime = rangeTime.split(" - ")[0];
                        endTime = rangeTime.split(" - ")[1];
                    }

                    purchasefactoryid = $('#inbillfactoryid').val();
                    remarksKey = $('#inbillRemarks').val();
                    // 执行重载
                    if(time == "" || rangeTime == ""){
                        var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                        table.reload('inbillTable', {
                            url: '/findInbill', //设置异步接口
                            page: false,
                            method: 'post',
                            where: {
                                time:time,
                                startTime:startTime,
                                endTime:endTime,
                                factoryid:purchasefactoryid,
                                remarks:remarksKey
                            },
                            done: function(res,curr){
                                excelPurchasefactoryid = purchasefactoryid;
                                excelTime = time;
                                excelStartTime = startTime;
                                excelEndTime = endTime;
                                _curr_page = curr;
                                _limit = 0;
                                console.log(curr);
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
                    $('#inbillForm')[0].reset();
                    table.reload('inbillTable', {
                        url: '/getInbillList', //设置异步接口
                        page: true,
                        method: 'GET',
                        done: function(res,curr){
                            excelPurchasefactoryid = "";
                            excelTime = "";
                            excelStartTime = "";
                            excelEndTime = "";
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
        table.on('tool(inbill)', function(obj) {
            var data = obj.data;
            if(obj.event === 'del') {
                layer.confirm('确认删除本条单据记录？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delInbill',
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
            }else if(obj.event === 'detail'){
                //查看详情

                $http({
                    url: '/getInbillDetail?id=' + data.id,
                    method: 'GET',
                }).then(function successCallback(res) {
                    var inbillData = res.data.data.inbill;
                    var purchaseList = res.data.data.purchaseList;
                    inbillData.time = toFormatDate(inbillData.time, 2);
                    inbillData.factory = "";
                    for(var i=0;i<$scope.factorys.length;i++){
                        if($scope.factorys[i].id == inbillData.factoryid){
                            inbillData.factory = $scope.factorys[i].factoryname;
                        }
                    }
                    var purchasesStr = "";
                    for(var x = 0;x<purchaseList.length;x++){
                        var index = x+1;
                        purchasesStr = purchasesStr + '<div class="layui-form-item">' +
                            '<div class="layui-form-item" style="float: left;border-bottom: 1px solid #e6e6e6">' +
                            '<div class="layui-inline left-inline"><label class="layui-form-label" style="width:auto;padding-left: 5px;">'+index+'、货号：</label>' +
                            '<div class="layui-input-inline" style="width: 100px"><input readonly="readonly" type="text" class="layui-input" value="'+purchaseList[x].code+'"></div></div>' +
                            '<div class="layui-inline left-inline"><label class="layui-form-label" style="width:auto;padding-left: 5px;">码段：</label>' +
                            '<div class="layui-input-inline short"><input readonly="readonly" type="text" class="layui-input" value="'+purchaseList[x].rangesize+'"/></div></div>' +
                            '<div class="layui-inline left-inline"><label class="layui-form-label" style="width:auto;padding-left: 5px;">颜色：</label>' +
                            '<div class="layui-input-inline short"><input readonly="readonly" type="text" class="layui-input" value="'+purchaseList[x].color+'"></div></div>' +
                            '<div class="layui-inline left-inline"><label class="layui-form-label" style="width:auto;padding-left: 5px;">价格：</label>' +
                            '<div class="layui-input-inline short"><input readonly="readonly" type="text" class="layui-input" value="'+purchaseList[x].price+'"></div></div>' +
                            '<div class="layui-inline left-inline"><label class="layui-form-label" style="width:auto;padding-left: 5px;">数量：</label>' +
                            '<div class="layui-input-inline short"><input readonly="readonly" type="text" class="layui-input" value="'+purchaseList[x].number+'"></div></div>' +
                            '</div></div>';
                    }

                    layer.open({
                        type: 1,
                        title: "订单详情",
                        closeBtn: false,
                        area: '880px;',
                        shade: 0.8,
                        id: 'LAY_inbillDetail', //设定一个id，防止重复弹出
                        btn: ['修改', '关闭'],
                        btnAlign: 'c',
                        moveType: 1, //拖拽模式，0或者1
                        content: '<div class="layui-field-box" style="">' +
                        '<form class="layui-form" id="inbillAddForm" action="" style="display: inline-block;text-align: -webkit-center;">' +
                        '<div class="layui-form-item" style="width: 600px;text-align: center;">' +
                        '<div class="layui-inline"><label class="layui-form-label ifLabel">进货日期：</label>' +
                        '<div class="layui-input-inline ifInput"><input readonly="readonly" type="text" class="layui-input" value="'+inbillData.time+'"></div>' +
                        '</div><div class="layui-inline"><label class="layui-form-label ifLabel">厂家：</label>' +
                        '<div class="layui-input-inline ifInput"><input readonly="readonly" type="text" class="layui-input" value="'+inbillData.factory+'" /></div>' +
                        '</div></div>' +
                        '<div class="layui-form-item" style="width: 600px;text-align: center;">' +
                        '<div class="layui-inline"><label class="layui-form-label ifLabel">总&#12288&#12288数：</label>' +
                        '<div class="layui-input-inline ifInput"><input readonly="readonly" type="text" class="layui-input" value="'+inbillData.allnumber+'"></div>' +
                        '</div><div class="layui-inline"><label class="layui-form-label ifLabel">总金额：</label>' +
                        '<div class="layui-input-inline ifInput"><input readonly="readonly" type="text" class="layui-input" value="'+inbillData.allprice+'" /></div>' +
                        '</div></div>' +
                        '<div class="layui-form-item layui-form-text" style="width: 585px;text-align: center;">' +
                        '<label class="layui-form-label">备&#12288&#12288注：</label>' +
                        '<div class="layui-input-block"><textarea style="width:97.5%;resize:none;" readonly="readonly" class="layui-textarea">'+inbillData.remarks+'</textarea></div>' +
                        '</div><div style="margin-top: 40px;">'+purchasesStr+'</div></form></div>',
                        success: function(layero) {
                            form.render();
                        },
                        btn1: function(index, layero) {
                            layer.closeAll();
                            window.location.href='/#/index/updateinbill?id='+data.id;
                        }
                    });

                }, function errorCallback(res) {
                    // 请求失败执行代码
                    console.log(res);
                    layer.msg('获取数据详情失败', {icon: 2, time: 1500});
                });
            }else if(obj.event === 'update'){
                window.location.href='/#/index/updateinbill?id='+data.id;
            }
        });

        //导出excel
        $scope.exportToExcel = function(){
            window.open("/exportInbillList?time="+excelTime + "&startTime="+excelStartTime+"&endTime="+excelEndTime+"&factoryid="+excelPurchasefactoryid+"&page="+_curr_page+"&limit="+_limit,"_blank");
        }
    });

}]);
