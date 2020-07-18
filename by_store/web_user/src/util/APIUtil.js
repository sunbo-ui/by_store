var serverBase = 'http://localhost:8086'
var portalBase ='http://localhost:9002';
export default {
    BASE_SERVER_URL: serverBase,
    BASE_PORTAL_URL: portalBase,
    USER_SMSCODE_URL: serverBase + "/user/sendCode",
    USER_REGISTER_URL: serverBase + "/user/register",
    USER_LOGIN_URL: serverBase + "/user/login",//登录
    LOGOUT: serverBase + "/logout",//登出

    CART_FINDCARTLIST_URL: serverBase + "/cart/findCartList",//查询购物车
    DETELETE_CART_URL: serverBase + "/cart/deleteCartList",//删除购物车

    ADDRESS_FINDBYUSERNAME_URL: serverBase + "/address/findByUsername",//根据用户名查询收货地址
    ADDRESS_SAVE_URL: serverBase + "/address/saveAddress",//保存
    ADDRESS_UPDATE_URL: serverBase + "/address/updateAddress",//更新
    ADDRESS_DELETE_URL: serverBase + "/address/deleteAddress",//删除
    ADDRESS_UPDATEBYID_URL: serverBase + "/address/findById",//更新
    ADDRESS_SETDEFAULT_URL: serverBase + "/address/setDefault",//设置默认地址


    ORDER_UPDATESTATUS_URL: serverBase + "/order/updateStatus",//发货
    ORDER_FINDBYUSERNAME_URL: serverBase + "/order/findByUsername",//根据username查询所有order
    ORDERITEM_DELETE_URL: serverBase + "/order/deleteOrderItemById",//取消订单
    ORDERI_DELETE_URL: serverBase + "/order/deleteOrderById",//删除订单

    ORDER_FINDORDERBYID_URL:serverBase + "/order/findOrderById",//订单查询
    ORDERITEM_FINDBYORDERID_URL:serverBase + "/order/findOrderItemByOrderId",//订单详情
    ORDER_GOTOBYORDERID_URL: serverBase + "/order/gotoByOrderId",//添加订单

    UPLOAD_UPLOADFILE_URL:serverBase + "/upload/uploadFile",
    DELETE_UPLOADURL_URL:serverBase + "/upload/delImg",

    COMMENTS_SAVE_URL:serverBase+"/comments/save",
    COMMENTS_FINDBYUSERID_URL:serverBase+"/comments/findByUserId",
    COMMENTS_FINDORDERITEMBYGOODSID_URL:serverBase+"/comments/findOrderItemByGoodsId",
    COMMENTS_DELETECOMMENTS_URL:serverBase+"/comments/deleteCommentById",
    COMMENTS_FINDBYID_URL:serverBase+"/comments/findById",
    COMMENTS_UPDATECOMMENTS_URL:serverBase+"/comments/updateComment",

    COLLECT_FINDPAGE_URL:serverBase+"/collect/findPage",//查询收藏
    COLLECT_DELETE_URL:serverBase+"/collect/deleteCollect",//删除收藏
    COLLECT_DELETEALL_URL:serverBase+"/collect/deleteAllCollect",//删除收藏
    COLLECT_FINDBYID_URL:serverBase+"/collect/findById",//
    COLLECT_UPDATENOTES_URL:serverBase+"/collect/updateNotes",//更新备注

    TRACH_FINDPAGE_URL:serverBase+"/track/findPage",
    TRACH_SAVE_URL:serverBase+"/track/saveTrack",
    TRACH_DELETE_URL:serverBase+"/track/deleteTrack",
    TRACK_DELETESELECT_URL:serverBase+"/track/deleteSelect",
    TRACK_DELETEALL_URL:serverBase+"/track/deleteAll",

    CUSTOMER_FINDBYUSERNAME_URL:serverBase+"/user/findByUsername",
    CUSTOMER_SAVE_URL:serverBase+"/user/saveCustomer",
    EWALLET_FINDAMOUNTBYUSERNAME_URL:serverBase+"/ewallet/findAmountByUsername",
    EWALLET_SAVE_URL:serverBase+"/ewallet/save",
    EWALLET_INSERTAMOUNT_URL:serverBase+"/ewallet/insertAmount",
    PAY_FINDBYUSER_URL:serverBase+"/pay/findByUsername",

    DISCOUNT_FINDBYUSERID_URL:serverBase+"/userDiscount/findByUsername",
    USERDISCOUNT_FINDBYUSERID_URL:serverBase+"/userDiscount/findUserDiscount",
    USERDISCOUNT_DELETE_URL:serverBase+"/userDiscount/deleteUserDiscount",

    INTEGRAL_FINDBYUSERNAME_URL:serverBase+"/integral/findByUsername",
    INTEGRAL_EXCHANGE_URL:serverBase+"/integral/exchange",
}