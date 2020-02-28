/**
 * Created by chenw on 2018/6/27.
 */
tmqtApp.controller('productPlanListController', ['$scope', '$http', function($scope, $http) {

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
            elem: '#relTime',
        });
        //预拍日期
        laydate.render({
            elem: '#prePhotoTime',
        });
        //方法级渲染
        table.render({
            elem: '#LAY_table_productPlan',
            url: '/getProductPlanList',
            cols: [
                [{
                    field: 'id',
                    title: 'ID',
                    width: 60,
                    sort: true,
                    align: 'center'
                }, {
                    field: 'code',
                    title: '货号',
                    width: 90,
                    align: 'center'
                }, {
                    field: 'image',
                    title: '图片',
                    width: 110,
                    align: 'center',
                    templet:'<div><img src="{{d.image}}"></div>'
                }, {
                    field: 'material',
                    title: '主材质/类型',
                    width: 95,
                    align: 'center',
                }, {
                    field: 'insidefabric',
                    title: '里布',
                    width: 60,
                    align: 'center',
                }, {
                    field: 'bottommaterial',
                    title: '鞋底材质',
                    width: 60,
                    align: 'center',
                }, {
                    field: 'type',
                    title: '类型',
                    width: 95,
                    align: 'center',
                }, {
                    field: 'color',
                    title: '颜色',
                    width: 100,
                    align: 'center',
                }, {
                    field: 'factory',
                    title: '厂家/供货商',
                    width: 95,
                    align: 'center',
                }, {
                    field: 'cost',
                    title: '成本',
                    width: 110,
                    align: 'center',
                    style: 'height:auto;padding:0;',
                    templet:'#costTpl'
                }, {
                    field: 'price',
                    title: '售价',
                    width: 60,
                    align: 'center',
                }, {
                    field: 'titlestate',
                    title: '标题状态',
                    width: 60,
                    align: 'center',
                    templet:'#titlestateTpl'
                }, {
                    field: 'brand',
                    title: '品牌',
                    width: 95,
                    align: 'center',
                }, {
                    field: 'takelength',
                    title: '测量内长',
                    width: 60,
                    align: 'center',
                    templet:'#takelengthTpl'
                },{
                    field: 'prephototime',
                    title: '预拍时间',
                    width: 65,
                    align: 'center',
                    templet:'#prephototimeTpl'
                }, {
                    field: 'dctime',
                    title: '大C时间',
                    width: 65,
                    align: 'center',
                    templet:'#dcTimeTpl'
                }, {
                    field: 'altime',
                    title: '阿里时间',
                    width: 65,
                    align: 'center',
                    templet:'#alTimeTpl'
                }, {
                    field: 'kdtime',
                    title: 'KD时间',
                    width: 65,
                    align: 'center',
                    templet:'#kdTimeTpl'
                }, {
                    field: 'tmtime',
                    title: '天猫时间',
                    width: 65,
                    align: 'center',
                    templet:'#tmTimeTpl'
                }, {
                    field: 'remarks',
                    title: '备注',
                    width: 100,
                    align: 'center'
                },{
                    width:150,
                    align: 'center',
                    toolbar: '#toolBar'
                }]
            ],
            id: 'productPlanTable',
            page: true,
            height: 'full-300',
            cellMinWidth: 60,
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
            done: function () {
                layer.close(tableLoading);
                $('.layui-table-cell').attr("style","height:auto;padding:0;");
            }
        });
        var active = {
            search: function(){
                var code = $('#code').val();
                var relTime = ($('#relTime').val() !== ""?new Date($('#relTime').val()).getTime()/1000:"");
                var prePhotoTime = ($('#prePhotoTime').val() !== ""?new Date($('#prePhotoTime').val()).getTime()/1000:"");
                var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                // 执行重载
                table.reload('productPlanTable', {
                    url: '/findProductPlan', //设置异步接口
                    page: false,
                    method: 'POST',
                    where: {
                        code:code,
                        relTime:relTime,
                        prePhotoTime:prePhotoTime
                    },
                    done:function () {
                        layer.close(searchLoading);
                        $('.layui-table-cell').attr("style","height:auto;padding:0;");
                    }
                });
            },
            return: function(){
                // 执行重载
                var searchLoading = layer.load(1,{shade: [0.1,'#000']});
                table.reload('productPlanTable', {
                    url: '/getProductPlanList', //设置异步接口
                    page: true,
                    method: 'GET',
                    done:function(){
                        layer.close(searchLoading);
                        $('.layui-table-cell').attr("style","height:auto;padding:0;");
                    }
                });
            }
        };

        $('.reloadBtn').on('click', function() {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        //监听工具条
        table.on('tool(productPlan)', function(obj) {
            var data = obj.data;
            if(obj.event === 'del') {
                layer.confirm('确认删除本条记录？', function(index) {
                    var delLoading = layer.load(1,{shade: [0.1,'#000']});
                    $.ajax({
                        type: "post",
                        url: '/delProductPlan',
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
                var prePhotoTimeStr = toFormatDate(data.prephototime,2);
                var dcTimeStr = toFormatDate(data.dctime,2);
                var alTimeStr = toFormatDate(data.altime,2);
                var kdTimeStr = toFormatDate(data.kdtime,2);
                var tmTimeStr = toFormatDate(data.tmtime,2);
                layer.open({
                    type: 1,
                    title: "修改安排",
                    closeBtn: false,
                    area: '590px;',
                    shade: 0.8,
                    id: 'LAY_productPlanUpdate', //设定一个id，防止重复弹出
                    btn: ['提交修改', '取消操作'],
                    btnAlign: 'c',
                    moveType: 1, //拖拽模式，0或者1
                    content: '<div class="layui-field-box"><form class="layui-form" action="">' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">货&#12288&#12288号：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateCode" placeholder="货号" value="'+ data.code +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel" style="line-height: 55px;">图&#12288&#12288片：</label><div class="layui-input-inline productPlanInput" style="width:auto;"><input type="file" id="updateImage" accept="image/jpg, image/png, image/jpeg, image/gif" style="left:-9999px;position:absolute;display: none"/><div style="border: 1px solid #DFDFDF"><img id="updateImgPreview" src="'+ data.image +'" style="width:100px;height:71px;margin:0 auto;display:block;" /></div></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">主材质/类型：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateMaterial" placeholder="主材质/类型" value="'+ data.material +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">里&#12288&#12288布：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateInsideFabric" placeholder="里布" value="'+ data.insidefabric +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">鞋底材质：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateBottomMaterial" placeholder="鞋底材质" value="'+ data.bottommaterial +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">鞋子类型：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateType" placeholder="鞋子类型" value="'+ data.type +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">颜&#12288&#12288色：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateColor" placeholder="颜色" value="'+ data.color +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">厂家/供货商：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateFactory" placeholder="厂家/供货商" value="'+ data.factory +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">品&#12288&#12288牌：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateBrand" placeholder="品牌" value="'+ data.brand +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">大C时间：</label>' +
                    '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                    '<input type="text" class="layui-input" id="updateDcTime"  placeholder="大C时间" value="'+ dcTimeStr +'"></div>' +
                    '<input type="checkbox" lay-filter="updateDcPre" id="updateDcPre" title="预" value="0" lay-skin="primary"></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">成&#12288&#12288本：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updateCost" placeholder="不同码段用#隔开" value="'+ data.cost +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">阿里时间：</label>' +
                    '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                    '<input type="text" class="layui-input" id="updateAlTime"  placeholder="阿里时间" value="'+ alTimeStr +'"></div>' +
                    '<input type="checkbox" lay-filter="updateAlPre" id="updateAlPre" title="预" value="0" lay-skin="primary"></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">售&#12288&#12288价：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updatePrice" placeholder="售价" value="'+ data.price +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">KD时间：</label>' +
                    '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                    '<input type="text" class="layui-input" id="updateKdTime"  placeholder="KD时间" value="'+ kdTimeStr +'"></div>' +
                    '<input type="checkbox" lay-filter="updateKdPre" id="updateKdPre" title="预" value="0" lay-skin="primary"></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">预拍时间：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="updatePrePhotoTime" placeholder="预拍时间" value="'+ prePhotoTimeStr +'"></div></div></div>' +
                    '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">天猫时间：</label>' +
                    '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                    '<input type="text" class="layui-input" id="updateTmTime"  placeholder="天猫时间" value="'+ tmTimeStr +'"></div>' +
                    '<input type="checkbox" lay-filter="updateTmPre" id="updateTmPre" title="预" value="0" lay-skin="primary"></div></div>' +
                    '<div class="layui-form-item productPlanItem">' +
                    '<div class="layui-inline"><label class="layui-form-label productPlanLabel">标题状态：</label><div class="layui-input-inline productPlanInput"><input type="checkbox" lay-filter="updateTitleState" name="updateTitleState" id="updateTitleState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div>' +
                    '<div class="layui-inline"><label class="layui-form-label productPlanLabel">测量内长：</label><div class="layui-input-inline productPlanInput"><input type="checkbox" lay-filter="updateTakeLength" name="updateTakeLength" id="updateTakeLength" lay-skin="switch" lay-text="YES|NO" value="0"></div></div>' +
                    '</div>' +
                    '<div class="layui-form-item layui-form-text"><label class="layui-form-label">备&#12288&#12288注：</label><div class="layui-input-block"><textarea id="updateRemarks" placeholder="请输入备注" class="layui-textarea">'+ data.remarks +'</textarea></div></div>' +
                    '</form></div>',

                    success: function(layero) {
                        layui.use(['form', 'laydate'], function() {
                            var form = layui.form;
                            form.render();
                            if(data.titlestate == 1){
                                $('#updateTitleState').prop('checked', true); form.render();
                                $('#updateTitleState').val(1);
                            }
                            if(data.takelength == 1){
                                $('#updateTakeLength').prop('checked', true); form.render();
                                $('#updateTakeLength').val(1);
                            }
                            if(data.dcpre == 1){
                                $('#updateDcPre').prop('checked', true); form.render();
                                $('#updateDcPre').val(1);
                            }
                            if(data.alpre == 1){
                                $('#updateAlPre').prop('checked', true); form.render();
                                $('#updateAlPre').val(1);
                            }
                            if(data.kdpre == 1){
                                $('#updateKdPre').prop('checked', true); form.render();
                                $('#updateKdPre').val(1);
                            }
                            if(data.tmpre == 1){
                                $('#updateTmPre').prop('checked', true); form.render();
                                $('#updateTmPre').val(1);
                            }
                            form.on('switch(updateTitleState)', function(data){
                                data.elem.checked?$('#updateTitleState').val(1):$('#updateTitleState').val(0);
                            });
                            form.on('switch(updateTakeLength)', function(data){
                                data.elem.checked?$('#updateTakeLength').val(1):$('#updateTakeLength').val(0);
                            });
                            form.on('checkbox(updateDcPre)', function(data){
                                data.elem.checked?$('#updateDcPre').val(1):$('#updateDcPre').val(0);
                            });
                            form.on('checkbox(updateAlPre)', function(data){
                                data.elem.checked?$('#updateAlPre').val(1):$('#updateAlPre').val(0);
                            });
                            form.on('checkbox(updateKdPre)', function(data){
                                data.elem.checked?$('#updateKdPre').val(1):$('#updateKdPre').val(0);
                            });
                            form.on('checkbox(updateTmPre)', function(data){
                                data.elem.checked?$('#updateTmPre').val(1):$('#updateTmPre').val(0);
                            });

                            var laydate = layui.laydate;
                            //具体日期

                            laydate.render({
                                elem: '#updateDcTime'
                            });
                            laydate.render({
                                elem: '#updateAlTime'
                            });
                            laydate.render({
                                elem: '#updateKdTime'
                            });
                            laydate.render({
                                elem: '#updateTmTime'
                            });
                            laydate.render({
                                elem: '#updatePrePhotoTime'
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
                        });
                    },
                    yes: function(index, layero) {
                        var updateLoading = layer.load(1,{shade: [0.1,'#000']});
                        var code = $('#updateCode').val();
                        var imgFile = $("#updateImage").get(0).files[0];//获取文件流
                        var material = $('#updateMaterial').val();
                        var insideFabric = $('#updateInsideFabric').val();
                        var bottomMaterial = $('#updateBottomMaterial').val();
                        var type = $('#updateType').val();
                        var color = $('#updateColor').val();
                        var factory = $('#updateFactory').val();
                        var cost = $('#updateCost').val();
                        var price = $('#updatePrice').val();
                        var titleState = $('#updateTitleState').val();
                        var brand = $('#updateBrand').val();
                        var takeLength = $('#updateTakeLength').val();
                        var prePhotoTime = ($('#updatePrePhotoTime').val() !== ""?new Date($('#updatePrePhotoTime').val()):"");
                        var dcTime = ($('#updateDcTime').val() !== ""?new Date($('#updateDcTime').val()):"");
                        var alTime = ($('#updateAlTime').val() !== ""?new Date($('#updateAlTime').val()):"");
                        var kdTime = ($('#updateKdTime').val() !== ""?new Date($('#updateKdTime').val()):"");
                        var tmTime = ($('#updateTmTime').val() !== ""?new Date($('#updateTmTime').val()):"");
                        var dcPre = $('#updateDcPre').val();
                        var alPre = $('#updateAlPre').val();
                        var kdPre = $('#updateKdPre').val();
                        var tmPre = $('#updateTmPre').val();
                        var remarks = $('#updateRemarks').val();


                        if(code != "") {
                            var updateFormData = new FormData();
                            updateFormData.append("imgFile",imgFile);
                            updateFormData.append("id",data.id);
                            $http({
                                url: '/updateProductPlan',
                                method: 'POST',
                                data: {
                                    "id":data.id,
                                    "code": code,
                                    "material": material,
                                    "insidefabric": insideFabric,
                                    "bottommaterial": bottomMaterial,
                                    "type": type,
                                    "color": color,
                                    "factory": factory,
                                    "cost": cost,
                                    "price": price,
                                    "titlestate": titleState,
                                    "brand": brand,
                                    "takelength": takeLength,
                                    "prephototime":prePhotoTime,
                                    "dctime": dcTime,
                                    "altime": alTime,
                                    "kdtime": kdTime,
                                    "tmtime": tmTime,
                                    "dcpre": dcPre,
                                    "alpre": alPre,
                                    "kdpre": kdPre,
                                    "tmpre": tmPre,
                                    "remarks":remarks
                                }
                            }).then(function successCallback(res) {
                                // 请求成功执行代码
                                console.log(res);
                                if(res.data == 1){
                                    if(imgFile != undefined){
                                        //需要修改图片
                                        $.ajax({
                                            type: "post",
                                            url: '/updateProductPlanImage',
                                            data: updateFormData,
                                            processData: false,  // 不处理数据
                                            contentType: false,   // 不设置内容类型
                                            success: function (res) {
                                                if(res === 1){
                                                    layer.close(index);
                                                    layer.close(updateLoading);
                                                    table.reload('productPlanTable', {});
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
                                        table.reload('productPlanTable', {});
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
                                        layer.msg('删除失败', {icon: 2,time:1000});
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
                                time: 1500
                            })
                        }
                    }
                });
            }
        });

        $('#addProductPlanBtn').on('click', function() {
            layer.open({
                type: 1,
                title: "添加安排",
                closeBtn: false,
                area: '590px',
                shade: 0.8,
                id: 'LAY_productPlanAdd', //设定一个id，防止重复弹出
                btn: ['确认添加', '取消操作'],
                btnAlign: 'c',
                moveType: 1, //拖拽模式，0或者1
                content: '<div class="layui-field-box"><form class="layui-form" action="">' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">货&#12288&#12288号：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addCode" placeholder="货号"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel" style="line-height: 55px;">图&#12288&#12288片：</label><div class="layui-input-inline productPlanInput" style="width:auto;"><input type="file" id="addImage" accept="image/jpg, image/png, image/jpeg, image/gif" style="left:-9999px;position:absolute;display: none"/><div style="border: 1px solid #DFDFDF"><img id="addImgPreview" src="../../img/add_img.png" style="width:100px;height:71px;margin:0 auto;display:block;" /></div></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">主材质/类型：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addMaterial" placeholder="主材质/类型"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">里&#12288&#12288布：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addInsideFabric" placeholder="里布"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">鞋底材质：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addBottomMaterial" placeholder="鞋底材质"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">鞋子类型：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addType" placeholder="鞋子类型"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">颜&#12288&#12288色：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addColor" placeholder="颜色"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">厂家/供货商：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addFactory" placeholder="厂家/供货商"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">品&#12288&#12288牌：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addBrand" placeholder="品牌"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">大C时间：</label>' +
                '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                '<input type="text" class="layui-input" id="addDcTime"  placeholder="大C时间"></div>' +
                '<input type="checkbox" lay-filter="addDcPre" id="addDcPre" title="预" value="0" lay-skin="primary"></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">成&#12288&#12288本：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addCost" placeholder="不同码段用#隔开"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">阿里时间：</label>' +
                '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                '<input type="text" class="layui-input" id="addAlTime"  placeholder="阿里时间"></div>' +
                '<input type="checkbox" lay-filter="addAlPre" id="addAlPre" title="预" value="0" lay-skin="primary"></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">售&#12288&#12288价：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addPrice" placeholder="售价"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">KD时间：</label>' +
                '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                '<input type="text" class="layui-input" id="addKdTime"  placeholder="KD时间"></div>' +
                '<input type="checkbox" lay-filter="addKdPre" id="addKdPre" title="预" value="0" lay-skin="primary"></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">预拍时间：</label><div class="layui-input-inline productPlanInput"><input type="text" class="layui-input" id="addPrePhotoTime" placeholder="预拍时间"></div></div></div>' +
                '<div class="layui-form-item productPlanItem"><div class="layui-inline"><label class="layui-form-label productPlanLabel">天猫时间：</label>' +
                '<div class="layui-input-inline productPlanInput" style="width:87px;" >' +
                '<input type="text" class="layui-input" id="addTmTime"  placeholder="天猫时间"></div>' +
                '<input type="checkbox" lay-filter="addTmPre" id="addTmPre" title="预" value="0" lay-skin="primary"></div></div>' +
                '<div class="layui-form-item productPlanItem">' +
                '<div class="layui-inline"><label class="layui-form-label productPlanLabel">标题状态：</label><div class="layui-input-inline productPlanInput"><input type="checkbox" lay-filter="addTitleState" name="addTitleState" id="addTitleState" lay-skin="switch" lay-text="YES|NO" value="0"></div></div>' +
                '<div class="layui-inline"><label class="layui-form-label productPlanLabel">测量内长：</label><div class="layui-input-inline productPlanInput"><input type="checkbox" lay-filter="addTakeLength" name="addTakeLength" id="addTakeLength" lay-skin="switch" lay-text="YES|NO" value="0"></div></div>' +
                '</div>' +
                '<div class="layui-form-item layui-form-text"><label class="layui-form-label">备&#12288&#12288注：</label><div class="layui-input-block"><textarea id="addRemarks" placeholder="请输入备注" class="layui-textarea"></textarea></div></div>' +
                '</form></div>',
                success: function(layero) {
                    layui.use(['form', 'laydate'], function() {
                        var form = layui.form;
                        form.render();
                        form.on('switch(addTitleState)', function(data){
                            data.elem.checked?$('#addTitleState').val(1):$('addTitleState').val(0);
                        });
                        form.on('switch(addTakeLength)', function(data){
                            data.elem.checked?$('#addTakeLength').val(1):$('addTakeLength').val(0);
                        });
                        form.on('checkbox(addDcPre)', function(data){
                            console.log(data.elem.checked);
                            data.elem.checked?$('#addDcPre').val(1):$('#addDcPre').val(0);
                        });
                        form.on('checkbox(addAlPre)', function(data){
                            data.elem.checked?$('#addAlPre').val(1):$('#addAlPre').val(0);
                        });
                        form.on('checkbox(addKdPre)', function(data){
                            data.elem.checked?$('#addKdPre').val(1):$('#addKdPre').val(0);
                        });
                        form.on('checkbox(addTmPre)', function(data){
                            data.elem.checked?$('#addTmPre').val(1):$('#addTmPre').val(0);
                        });

                        var laydate = layui.laydate;
                        //具体日期

                        laydate.render({
                            elem: '#addDcTime'
                        });
                        laydate.render({
                            elem: '#addAlTime'
                        });
                        laydate.render({
                            elem: '#addKdTime'
                        });
                        laydate.render({
                            elem: '#addTmTime'
                        });
                        laydate.render({
                            elem: '#addPrePhotoTime'
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
                    var material = $('#addMaterial').val();
                    var insideFabric = $('#addInsideFabric').val();
                    var bottomMaterial = $('#addBottomMaterial').val();
                    var type = $('#addType').val();
                    var color = $('#addColor').val();
                    var factory = $('#addFactory').val();
                    var cost = $('#addCost').val();
                    var price = $('#addPrice').val();
                    var titleState = $('#addTitleState').val();
                    var brand = $('#addBrand').val();
                    var takeLength = $('#addTakeLength').val();
                    var addPrePhotoTimeStr = $('#addPrePhotoTime').val();
                    var prePhotoTime = new Date(addPrePhotoTimeStr);
                    var addDcTimeStr = $('#addDcTime').val();
                    var dcTime = new Date(addDcTimeStr);
                    var addAlTimeStr = $('#addAlTime').val();
                    var alTime = new Date(addAlTimeStr);
                    var addKdTimeStr = $('#addKdTime').val();
                    var kdTime = new Date(addKdTimeStr);
                    var addTmTimeStr = $('#addTmTime').val();
                    var tmTime = new Date(addTmTimeStr);
                    var dcPre = $('#addDcPre').val();
                    var alPre = $('#addAlPre').val();
                    var kdPre = $('#addKdPre').val();
                    var tmPre = $('#addTmPre').val();
                    var remarks = $('#addRemarks').val();


                    if(code != "" && imgFile != undefined) {
                        var addFormData = new FormData();
                        addFormData.append("code",code);
                        addFormData.append("imgFile",imgFile);
                        addFormData.append("material",material);
                        addFormData.append("insideFabric",insideFabric);
                        addFormData.append("bottomMaterial",bottomMaterial);
                        addFormData.append("type",type);
                        addFormData.append("color",color);
                        addFormData.append("factory",factory);
                        addFormData.append("cost",cost);
                        addFormData.append("price",price);
                        addFormData.append("titleState",titleState);
                        addFormData.append("brand",brand);
                        addFormData.append("takeLength",takeLength);
                        if(addPrePhotoTimeStr != ""){
                            addFormData.append("prePhotoTime",prePhotoTime);
                        }
                        if(addDcTimeStr != ""){
                            addFormData.append("dcTime",dcTime);
                        }
                        if(addAlTimeStr != ""){
                            addFormData.append("alTime",alTime);
                        }
                        if(addKdTimeStr != ""){
                            addFormData.append("kdTime",kdTime);
                        }
                        if(addTmTimeStr != ""){
                            addFormData.append("tmTime",tmTime);
                        }
                        addFormData.append("dcPre",dcPre);
                        addFormData.append("alPre",alPre);
                        addFormData.append("kdPre",kdPre);
                        addFormData.append("tmPre",tmPre);
                        addFormData.append("remarks",remarks);
                        $.ajax({
                            type: "post",
                            url: '/addProductPlan',
                            data: addFormData,
                            processData: false,  // 不处理数据
                            contentType: false,   // 不设置内容类型
                            success: function (res) {
                                console.log(res);
                                if(res.code == 1){
                                    layer.close(index);
                                    layer.close(addLoading);
                                    table.reload('productPlanTable', {});
                                    layer.msg(res.msg, {icon: 1,time:1000});
                                }else{
                                    layer.close(addLoading);
                                    if(res === ""){
                                        layer.confirm('请登录以后在进行操作', {
                                            btn: ['登录','取消'] //按钮
                                        }, function(){
                                            layer.closeAll();
                                            window.location.href='/#/login';
                                        });
                                    }else{
                                        layer.msg('添加失败'+res.msg, {icon: 2,time:1000});
                                    }
                                }
                            },
                            error:function () {
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
