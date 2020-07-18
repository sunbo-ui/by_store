var serverBase='http://localhost:8083';
export default {
    BASE_SERVER_URL: serverBase,

    FINDREDISKEY:serverBase+"/socket/findRedisKeys",

    SELLER_REGISTER_URL:serverBase + '/login/register',//登录注册
    LOGIN_LOGIN_URL:serverBase + '/login',//登录
    LOGIN_LOGOUT_URL:serverBase + '/user/logout',//登出

    SELLER_FINDBYID_URL:serverBase + '/seller/findById',//根据id查询商家
    SELLER_UPDATEBYID_URL:serverBase + '/seller/updateBySellerId',//根据id查询商家

    ITEMCAT_FINDBYPARENTID_URL:serverBase + "/itemCat/findByParentId",//根据父类id查询分类
    ITEMCAT_FINDONE_URL:serverBase +"/itemCat/findOne",//根据id查询分类
    ITEMCAT_FINDALL_URL:serverBase +"/itemCat/findAll",//查询所有分类

    TYPE_FINDBYID_URL:serverBase +"/temp/findById",//查询模板根据id
    TYPE_SPECLIST_URL:serverBase +"/temp/findBySpecList",//查询规格

    UPLOAD_UPLOADIMAGE_URL:serverBase + "/upload/uploadImage",
    UPLOAD_UPLOADFILE_URL:serverBase + "/upload/uploadFile",
    UPLOAD_DELIMG_URL:serverBase + "/upload/delImg",

    GOODS_ADD_URL:serverBase + "/goods/add",//添加商品
    GOODS_UPDATE_URL:serverBase + "/goods/update",//更新商品
    GOODS_DELETE_URL:serverBase + "/goods/delete",//删除商品
    GOODS_SEARCH_URL:serverBase + "/goods/search",//查询所有商品
    GOODS_FINDBYID_URL:serverBase + "/goods/findById",//根据id查询商品
    GOODS_UPDATESTATUS_URL:serverBase + "/goods/updateStatus",//修改状态
    GOODS_FINDBYSELLER_URL:serverBase+"/goods/findBySellerId",//根据SellerID查询所有商品
    ORDER_FINDBYSELLERID_URL:serverBase + "/order/findOrderBySellerId",//订单查询
    ORDER_FINDORDERBYID_URL:serverBase + "/order/findOrderById",//订单查询
    ORDER_UPDATESTATUS_URL:serverBase + "/order/updateStatus",//发货
    ORDERITEM_FINDBYORDERID_URL:serverBase + "/order/findOrderItemByOrderId",//订单详情

    COMMENTS_FINDPAGEBySellerId_URL:serverBase+"/comments/findByPageBySellerId",
    COMMENTS_FINDBYID_URL:serverBase+"/comments/findById",
    GOODS_FINDGOODSBYID_URL:serverBase + "/comments/findGoodsById",//根据id查询商品
    GOODS_UPDATESHOPCOMTENT_URL:serverBase+"/comments/updateShopContent",

    DISCOUNT_FINDBYSELLER_URL:serverBase+"/discount/findBySeller",
    DISCOUNT_SAVEDISCOUNT_URL:serverBase+"/discount/saveDiscount",
    DISCOUNT_DELETEDISCOUNT_URL:serverBase+"/discount/deleteDiscount",
    DISCOUNT_DELETEDISCOUNTBYSELECTION_URL:serverBase+"/discount/deleteDiscountBySelection",
    DISCOUNT_FINDBYID_URL:serverBase+"/discount/findById",
    DISCOUNT_UPDATEDISCOUNT_URL:serverBase+"/discount/updateDiscount",
}