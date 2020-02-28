var tmqtApp = angular.module('tmqtApp',['ui.router']);

tmqtApp.config(function ($stateProvider, $urlRouterProvider, $httpProvider) {
    $urlRouterProvider.otherwise('/index');

	$stateProvider
        .state('index', {
            url: '/index',
            views: {
                '': {
                    templateUrl: './index.html',
                    controller:'indexController'
                },
                'index': {
                    templateUrl: './main.html',
                    controller:'indexController'
                },
                'main@index': {
                    templateUrl: './views/inbillList.html',
                    controller:'inbillListController'
                }
            }
        })
        .state('login', {
            url: '/login',
            views: {
                '': {
                    templateUrl: './index.html',
                    controller:'indexController'
                },
                'index': {
                    templateUrl: './login.html',
                    controller:'loginController'
                },
            }
        })
        .state('index.factoryList', {
            url: '/factorylist',
            views: {
                'main@index': {
                    templateUrl: './views/factoryList.html',
                    controller:'factoryListController'
                }
            }
        })
        .state('index.rangeSizeList', {
            url: '/rangesizelist',
            views: {
                'main@index': {
                    templateUrl: './views/rangeSizeList.html',
                    controller:'rangeSizeListController'
                }
            }
        })
        .state('index.inbillList', {
        url: '/inbilllist',
        views: {
            'main@index':{
                templateUrl: './views/inbillList.html',
                controller:'inbillListController'
            }
        }
    })
        .state('index.addinbill', {
            url: '/addinbill',
            views: {
                'main@index':{
                    templateUrl: './views/addInbill.html',
                    controller:'addInbillController'
                }
            }
        })
        .state('index.updateinbill', {
            url: '/updateinbill?id',
            views: {
                'main@index':{
                    templateUrl: './views/updateInbill.html',
                    controller:'updateInbillController'
                }
            }
        })

        .state('index.outbillList', {
            url: '/outbilllist',
            views: {
                'main@index':{
                    templateUrl: './views/outbillList.html',
                    controller:'outbillListController'
                }
            }
        })
        .state('index.addoutbill', {
            url: '/addoutbill',
            views: {
                'main@index':{
                    templateUrl: './views/addOutbill.html',
                    controller:'addOutbillController'
                }
            }
        })
        .state('index.updateoutbill', {
            url: '/updateoutbill?id',
            views: {
                'main@index':{
                    templateUrl: './views/updateOutbill.html',
                    controller:'updateOutbillController'
                }
            }
        })

        .state('index.returnsList', {
            url: '/returnslist',
            views: {
                'main@index': {
                    templateUrl: './views/returnsList.html',
                    controller:'returnsListController'
                }
            }
        })
        .state('index.paymentList', {
            url: '/paymentlist',
            views: {
                'main@index': {
                    templateUrl: './views/paymentList.html',
                    controller:'paymentListController'
                }
            }
        })
        .state('index.paymentCenter', {
            url: '/paymentcenter',
            views: {
                'main@index': {
                    templateUrl: './views/paymentCenter.html',
                    controller:'paymentCenterController'
                }
            }
        })
        .state('index.productPlan', {
            url: '/productplan',
            views: {
                'main@index': {
                    templateUrl: './views/productPlanList.html',
                    controller:'productPlanListController'
                }
            }
        })
        .state('index.newProduct', {
            url: '/newproduct',
            views: {
                'main@index': {
                    templateUrl: './views/newProductList.html',
                    controller:'newProductListController'
                }
            }
        })
        .state('index.userList', {
            url: '/userlist?type',
            views: {
                'main@index': {
                    templateUrl: './views/userList.html',
                    controller:'userListController'
                }
            }
        })
});




//根据id移除元素
function removeById(arr, id) {
	for(var i = 0; i < arr.length; i++) {
		if(arr[i]["id"] == id) {
			arr.splice(i, 1);
			break;
		}
	}
};



function getAgoDay(num) {
	var day1 = new Date();
	day1.setTime(day1.getTime() - 24 * 60 * 60 * 1000 * num);
	var dayAgo = day1.getFullYear() + "-" + (day1.getMonth() + 1) + "-" + day1.getDate();
	return dayAgo;
}

function setCookie(name,value) {
    var exp = new Date();
    exp.setTime(exp.getTime() + 7 * 24 * 60 * 60 * 1000);
    document.cookie = name  + "=" + value;
}

function getCookie(name) {
    var arr,reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if(arr = document.cookie.match(reg)){
        return arr[2];
    }else{
        return null;
    }
}

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();// 日
    var hour = date.getHours(); // 时
    var minutes = date.getMinutes(); // 分
    var seconds = date.getSeconds() //秒

    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (day >= 0 && day <= 9) {
        day = "0" + day;
    }
    var currentdate = year + seperator1 + month + seperator1 + day + " " + hour + "." + minutes + "." + seconds;
    return currentdate;
}

function toFormatDate(date,type) {

    if(date !== null){
        date = new Date(date);
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        if(type == 1){
            return year+'年'+month+'月'+day+'日';
        }else if(type == 2){
            return year+'-'+month+'-'+day;
        }else if(type == 3){
            return year+'-'+month;
        }

    }else{
        return "";
    }

}