/**
 * Created by chenw on 2018/6/27.
 */
tmqtApp.controller('newProductListController', ['$scope','$http', function($scope, $http) {
    //表格
    layui.use(['jquery', 'layer','table','laydate','form'], function() {
        var layer = layui.layer,
            table = layui.table,
            laydate = layui.laydate,
            form = layui.form,
            $ = layui.$;
        var tableLoading = layer.load(1,{shade: [0.1,'#000']});
        form.render();
        //具体日期
        laydate.render({
            elem: '#startTime',
        });

        //方法级渲染
        table.render({
            elem: '#LAY_table_newProduct',
            url: '/getNewProductList',
            cols: [
                [{
                    checkbox: true,
                }, {
                    field: 'id',
                    title: 'ID',
                    width: 100,
                    sort: true,
                    align: 'center'
                }, {
                    field : 'code',
                    title: '货号',
                    width: 150,
                    align: 'center'
                }, {
                    field: 'image',
                    title: '图片',
                    width: 200,
                    align: 'center',
                    templet:'<div><img src="{{d.image}}"></div>'
                }, {
                    field: 'starttime',
                    title: '开始时间',
                    width: 200,
                    sort: true,
                    align: 'center',
                    templet:'#newProductStartTimeTpl'
                }, {
                    field: 'videostate',
                    title: '视频状态',
                    width: 120,
                    align: 'center',
                    templet:'#videostateTpl'
                }, {
                    field: 'detailimagestate',
                    title: '第二张主图、详情模特照状态',
                    width: 120,
                    align: 'center',
                    templet:'#detailimagestateTpl'
                }, {
                    field: 'buyershowstate',
                    title: '买家秀状态',
                    width: 120,
                    align: 'center',
                    templet:'#buyershowstateTpl'
                }, {
                    field: 'testclickstate',
                    title: '测点击情况',
                    width: 120,
                    align: 'center',
                    templet:'#testclickstateTpl'
                }, {
                    field: 'stockstate',
                    title: '备货状态',
                    width: 150,
                    align: 'center',
                    templet:'#stockstateTpl'
                }, {
                    field: 'remarks',
                    title: '备注',
                    width: 200,
                    align: 'center'
                }, {
                    width:150,
                    align: 'center',
                    toolbar: '#toolBar'
                }]
            ],
            id: 'newProductTable',
            page: true,
            limit: 6,
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
            done:function(){
                layer.close(tableLoading);
            }
        });
        var active = {
            search: function(){
                var code = $('#code').val();
                var startTime = ($('#startTime').val() !== ""?new Date($('#startTime').val()).getTime()/1000:"");
                var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                // 执行重载
                table.reload('newProductTable', {
                    url: '/findNewProduct', //设置异步接口
                    page: false,
                    method:'POST',
                    where: {
                        code:code,
                        startTime:startTime
                    },
                    done:function(){
                        layer.close(searchLoading);
                    }
                });
            },
            return: function(){
                // 执行重载
                var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                table.reload('newProductTable', {
                    url: '/getNewProductList', //设置异步接口
                    page: true,
                    method:'GET',
                    done:function(){
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
        table.on('tool(newProduct)', function(obj) {
            var data = obj.data;
            if(obj.event === 'del') {
                layer.confirm('确认删除本条记录？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delNewProduct',
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
                var startTimeStr = toFormatDate(data.starttime,2);
                layer.open({
                    type: 1,
                    title: "修改",
                    closeBtn: false,
                    area: '400px',
                    shade: 0.8,
                    id: 'LAY_newProductUpdate', //设定一个id，防止重复弹出
                    btn: ['提交修改', '放弃修改'],
                    btnAlign: 'c',
                    moveType: 1, //拖拽模式，0或者1
                    content: '<div class="layui-field-box"><form class="layui-form" action="">' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">货&#12288&#12288号：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="updateCode" placeholder="货号" value="' + data.code + '"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">图&#12288&#12288片：</label><div class="layui-input-inline" style="width:auto;"><input type="file" id="updateImage" accept="image/jpg, image/png, image/jpeg, image/gif" style="left:-9999px;position:absolute;display: none"/><div style="border: 1px solid #DFDFDF"><img id="updateImgPreview" src="'+ data.image +'" style="width:100px;height:71px;margin:0 auto;display:block;" /></div></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">开始时间：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="updateStartTime" placeholder="开始时间" value="'+ startTimeStr +'"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">视频状态：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="updateVideoState" name="updateVideoState" id="updateVideoState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">主图详情：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="updateDetailImageState" name="updateDetailImageState" id="updateDetailImageState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">买&nbsp;家&nbsp;秀：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="updateBuyerShowState" name="updateBuyerShowState" id="updateBuyerShowState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">测试点击：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="updateTestClickState" name="updateTestClickState" id="updateTestClickState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                    '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备货状态：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="updateStockState" name="updateStockState" id="updateStockState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                    '<div class="layui-form-item layui-form-text"><label class="layui-form-label">备&#12288&#12288注：</label><div class="layui-input-block"><textarea id="updateRemarks" placeholder="请输入备注" class="layui-textarea">'+ data.remarks +'</textarea></div></div></form></div>',
                    success: function(layero) {
                        layui.use(['form', 'laydate'], function() {
                            var form = layui.form;
                            if(data.videostate == 1){
                                $('#updateVideoState').prop('checked', true); form.render();
                                $('#updateVideoState').val(1);
                            }
                            if(data.detailimagestate == 1){
                                $('#updateDetailImageState').prop('checked', true); form.render();
                                $('#updateDetailImageState').val(1);
                            }
                            if(data.buyershowstate == 1){
                                $('#updateBuyerShowState').prop('checked', true); form.render();
                                $('#updateBuyerShowState').val(1);
                            }
                            if(data.testclickstate == 1){
                                $('#updateTestClickState').prop('checked', true); form.render();
                                $('#updateTestClickState').val(1);
                            }
                            if(data.stockstate == 1){
                                $('#updateStockState').prop('checked', true); form.render();
                                $('#updateStockState').val(1);
                            }
                            form.render();
                            form.on('switch(updateVideoState)', function(data){
                                if(data.elem.checked == true){
                                    $('#updateVideoState').val(1);
                                }
                                if(data.elem.checked == false){
                                    $('#updateVideoState').val(0);
                                }
                            });
                            form.on('switch(updateDetailImageState)', function(data){
                                if(data.elem.checked == true){
                                    $('#updateDetailImageState').val(1);
                                }
                                if(data.elem.checked == false){
                                    $('#updateDetailImageState').val(0);
                                }
                            });
                            form.on('switch(updateBuyerShowState)', function(data){
                                if(data.elem.checked == true){
                                    $('#updateBuyerShowState').val(1);
                                }
                                if(data.elem.checked == false){
                                    $('#updateBuyerShowState').val(0);
                                }
                            });
                            form.on('switch(updateTestClickState)', function(data){
                                if(data.elem.checked == true){
                                    $('#updateTestClickState').val(1);
                                }
                                if(data.elem.checked == false){
                                    $('#updateTestClickState').val(0);
                                }
                            });
                            form.on('switch(updateStockState)', function(data){
                                if(data.elem.checked == true){
                                    $('#updateStockState').val(1);
                                }
                                if(data.elem.checked == false){
                                    $('#updateStockState').val(0);
                                }
                            });
                            var laydate = layui.laydate;
                            //具体日期
                            laydate.render({
                                elem: '#updateStartTime'
                            });
                        });
                        $(document).find('#updateImage').on("change", function(event){
                            var imageFile = event.target.files[0]//获取文件流

                            var reader = new FileReader();
                            reader.readAsDataURL(imageFile); // 读出 base64
                            reader.onloadend = function () {
                                // 图片的 base64 格式, 可以直接当成 img 的 src 属性值
                                var dataURL = reader.result;
                                // 下面逻辑处理
                                $('#updateImgPreview').attr("src",dataURL);
                            };
                        });
                        $(document).find('#updateImgPreview').on("click",function(event){
                            $("#updateImage").click();
                            console.log("1")
                        });

                    },
                    yes: function(index, layero) {
                        var updateLoading = layer.load(1,{shade: [0.1,'#000']});
                        var code = $('#updateCode').val();
                        var imgFile = $("#updateImage").get(0).files[0];//获取文件流
                        var startTimeStr = $('#updateStartTime').val();
                        var startTime = new Date(startTimeStr);
                        var videoState = $('#updateVideoState').val();
                        var detailImageState = $('#updateDetailImageState').val();
                        var buyerShowState = $('#updateBuyerShowState').val();
                        var testClickState = $('#updateTestClickState').val();
                        var stockState = $('#updateStockState').val();
                        var remarks = $('#updateRemarks').val();
                        if(code != "" && startTimeStr != "") {

                            var updateFormData = new FormData();
                            updateFormData.append("imgFile",imgFile);
                            updateFormData.append("id",data.id);
                            $http({
                                url: '/updateNewProduct',
                                method: 'POST',
                                data: {
                                    "id":data.id,
                                    "code": code,
                                    "starttime": startTime,
                                    "videostate": videoState,
                                    "starttime": startTime,
                                    "detailimagestate": detailImageState,
                                    "buyershowstate": buyerShowState,
                                    "testclickstate": testClickState,
                                    "stockstate": stockState,
                                    "remarks": remarks
                                }
                            }).then(function successCallback(res) {
                                // 请求成功执行代码
                                console.log(res);
                                if(res.data == 1){
                                    if(imgFile != undefined){
                                        //需要修改图片
                                        $.ajax({
                                            type: "post",
                                            url: '/updateNewProductImage',
                                            data: updateFormData,
                                            processData: false,  // 不处理数据
                                            contentType: false,   // 不设置内容类型
                                            success: function (res) {
                                                if(res === 1){
                                                    layer.close(index);
                                                    layer.close(updateLoading);
                                                    table.reload('newProductTable', {});
                                                    layer.msg('修改成功', {icon: 1,time:1000});
                                                }else{
                                                    layer.close(index);
                                                    layer.close(updateLoading);
                                                    layer.msg('修改图片失败', {icon: 2,time:1000});
                                                }
                                            },
                                            error:function () {
                                                layer.close(index);
                                                layer.close(updateLoading);
                                                layer.msg('修改图片失败', {icon: 2,time:1000});
                                            }
                                        });
                                    }else{
                                        //不需要修改图片
                                        layer.close(index);
                                        layer.close(updateLoading);
                                        table.reload('newProductTable', {});
                                        layer.msg('修改成功', {icon: 1,time:1000});
                                    }
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
                                        layer.msg('修改失败', {icon: 2,time:1000});
                                    }
                                }
                            }, function errorCallback(res) {
                                // 请求失败执行代码
                                layer.close(index);
                                layer.close(updateLoading);
                                layer.msg('修改失败', {icon: 2,time:1500});
                            });
                        } else {
                            layer.close(updateLoading);
                            layer.msg("请输入完整信息", {
                                icon: 2,
                                time: 1500
                            })
                        }
                    }
                });
            }
        });

        $('#addNewProductBtn').on('click', function() {
            layer.open({
                type: 1,
                title: "添加新品",
                closeBtn: false,
                area: '400px',
                shade: 0.8,
                id: 'LAY_newProductAdd', //设定一个id，防止重复弹出
                btn: ['确认添加', '取消操作'],
                btnAlign: 'c',
                moveType: 1, //拖拽模式，0或者1
                content: '<div class="layui-field-box"><form class="layui-form" action="">' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">货&#12288&#12288号：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addCode" placeholder="货号"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">图&#12288&#12288片：</label><div class="layui-input-inline" style="width:auto;"><input type="file" id="addImage" accept="image/jpg, image/png, image/jpeg, image/gif" style="left:-9999px;position:absolute;display: none"/><div style="border: 1px solid #DFDFDF"><img id="addImgPreview" src="../../img/add_img.png" style="width:100px;height:71px;margin:0 auto;display:block;" /></div></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">开始时间：</label><div class="layui-input-inline"><input type="text" class="layui-input" id="addStartTime" placeholder="开始时间"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">视频状态：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="addVideoState" name="addVideoState" id="addVideoState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">主图详情：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="addDetailImageState" name="addDetailImageState" id="addDetailImageState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">买&nbsp;家&nbsp;秀：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="addBuyerShowState" name="addBuyerShowState" id="addBuyerShowState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">测试点击：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="addTestClickState" name="addTestClickState" id="addTestClickState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                '<div class="layui-form-item"><div class="layui-inline"><label class="layui-form-label">备货状态：</label><div class="layui-input-inline"><input type="checkbox" lay-filter="addStockState" name="addStockState" id="addStockState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div></div>' +
                '<div class="layui-form-item layui-form-text"><label class="layui-form-label">备&#12288&#12288注：</label><div class="layui-input-block"><textarea id="addRemarks" placeholder="请输入备注" class="layui-textarea"></textarea></div></div></form></div>',
                success: function(layero) {
                    layui.use(['form', 'laydate'], function() {
                        var form = layui.form;
                        form.render();
                        form.on('switch(addVideoState)', function(data){
                            if(data.elem.checked == true){
                                $('#addVideoState').val(1);
                            }
                            if(data.elem.checked == false){
                                $('#addVideoState').val(0);
                            }
                        });
                        form.on('switch(addDetailImageState)', function(data){
                            if(data.elem.checked == true){
                                $('#addDetailImageState').val(1);
                            }
                            if(data.elem.checked == false){
                                $('#addDetailImageState').val(0);
                            }
                        });
                        form.on('switch(addBuyerShowState)', function(data){
                            if(data.elem.checked == true){
                                $('#addBuyerShowState').val(1);
                            }
                            if(data.elem.checked == false){
                                $('#addBuyerShowState').val(0);
                            }
                        });
                        form.on('switch(addTestClickState)', function(data){
                            if(data.elem.checked == true){
                                $('#addTestClickState').val(1);
                            }
                            if(data.elem.checked == false){
                                $('#addTestClickState').val(0);
                            }
                        });
                        form.on('switch(addStockState)', function(data){
                            if(data.elem.checked == true){
                                $('#addStockState').val(1);
                            }
                            if(data.elem.checked == false){
                                $('#addStockState').val(0);
                            }
                        });
                        var laydate = layui.laydate;
                        //具体日期
                        laydate.render({
                            elem: '#addStartTime'
                        });
                    });
                    $(document).find('#addImage').on("change", function(event){
                        var imageFile = event.target.files[0]//获取文件流
                        var reader = new FileReader();
                        reader.readAsDataURL(imageFile); // 读出 base64
                        reader.onloadend = function () {
                            // 图片的 base64 格式, 可以直接当成 img 的 src 属性值
                            var dataURL = reader.result;
                            // 下面逻辑处理
                            $('#addImgPreview').attr("src",dataURL);
                        };
                    });
                    $(document).find('#addImgPreview').on("click",function(event){
                        $("#addImage").click();
                    });
                },
                yes: function(index, layero) {
                    var addLoading = layer.load(1,{shade: [0.1,'#000']});
                    var code = $('#addCode').val();
                    var imgFile = $("#addImage").get(0).files[0];//获取文件流
                    var startTimeStr = $('#addStartTime').val();
                    var startTime = new Date(startTimeStr);
                    var videoState = $('#addVideoState').val();
                    var detailImageState = $('#addDetailImageState').val();
                    var buyerShowState = $('#addBuyerShowState').val();
                    var testClickState = $('#addTestClickState').val();
                    var stockState = $('#addStockState').val();
                    var remarks = $('#addRemarks').val();


                    if(code != "" && imgFile != undefined) {
                        var addFormData = new FormData();
                        addFormData.append("code",code);
                        addFormData.append("imgFile",imgFile);
                        if(startTimeStr != ""){
                            addFormData.append("startTime",startTime);
                        }
                        addFormData.append("videoState",videoState);
                        addFormData.append("detailImageState",detailImageState);
                        addFormData.append("buyerShowState",buyerShowState);
                        addFormData.append("testClickState",testClickState);
                        addFormData.append("stockState",stockState);
                        addFormData.append("remarks",remarks);
                        $.ajax({
                            type: "post",
                            url: '/addNewProduct',
                            data: addFormData,
                            processData: false,  // 不处理数据
                            contentType: false,   // 不设置内容类型
                            success: function (res, textStatus, request) {
                                console.log(res);
                                if(res.code == 1){
                                    layer.close(index);
                                    layer.close(addLoading);
                                    table.reload('newProductTable', {});
                                    layer.msg(res.msg, {icon: 1,time:1000});
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
                                        layer.msg('添加失败', {icon: 2,time:1000});
                                    }
                                }
                            },
                            error:function () {
                                layer.close(index);
                                layer.close(addLoading);
                                layer.msg('添加失败', {icon: 2,time:1000});
                            }
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