var serverBase='http://localhost:8084';
var userServer = 'http://localhost:9003';
export default {
    BASE_SERVER_URL: serverBase,
    BASE_USER_SERVER:userServer,
    CONTENT_FINDBYCATEGORYID_URL:serverBase + "/content/findByCategoryId",//获取轮播图
    ITEMSEARCH_SEARCH_URL:serverBase + "/itemSearch/search",//获取搜索对象

    GOODS_FINDBYID_URL:serverBase + "/goods/findById",//根据id查询商品
    ITEMCAT_FINDONE_URL:serverBase +"/itemCat/findOne",//根据id查询分类

    CART_ADDGOODSTOCARTLIST_URL:serverBase + "/cart/addGoodsToCartList",//添加购物车
    CART_FINDCARTLIST_URL:serverBase + "/cart/findCartList",//查询购物车
    DETELETE_CART_URL:serverBase + "/cart/deleteCartList",//删除购物车
    DETELETE_ONE_CART_URL:serverBase + "/cart/deleteOneCartList",//删除购物车
    UPDATEBYNUM_CART_URL:serverBase + "/cart/updateByNumCartList",//更新数量购物车
    CLEARSHOP_CART_URL:serverBase + "/cart/clearShop",//更新数量购物车
    LOGOUT:serverBase + "/logout",

    ADDRESS_FINDBYUSERNAME_URL: serverBase + "/address/findByUsername",//根据用户名查询收货地址
    ADDRESS_SETDEFAULT_URL: serverBase + "/address/setDefault",//设置默认地址
    ADDRESS_UPDATEBYID_URL: serverBase + "/address/findById",//更新
    ORDER_ADD_URL: serverBase + "/order/add",//添加订单
    PAY_CREATENATIVE_URL:serverBase+"/pay/createNative",//支付
    PAY_QUERYPAYSTATUS_URL:serverBase+"/pay/queryPayStatus",//支付
    PAY_QUERYSTATUS_URL:serverBase+"/pay/queryStatus",//支付

    COMMENTS_FINDBYGOODSID_URL: serverBase+"/comments/findByGoodsId",//根据商品Id查询所有评价

    COLLECT_SAVE_URL: serverBase+"/collect/saveCollect",//加入收藏
    COLLECT_SECLECT_URL: serverBase+"/collect/saveCollectBySelect",//加入收藏

    TRACH_SAVE_URL:serverBase+"/track/saveTrack",

    CUSTOMER_FINDBYUSERNAME_URL:serverBase+"/user/findByUsername",

    EWALLET_FINDAMOUNTBYUSERNAME_URL:serverBase+"/ewallet/findAmountByUsername",
    EWALLET_SAVE_URL:serverBase+"/ewallet/save",

    DISCOUNT_FINDBYSELLER_USER_URL:serverBase+"/discount/findBySellerIdShow",//根据sellerId

    USERDISCOUNT_SAVE_URL:serverBase+"/userDiscount/saveUserDiscount",
    USERDISCOUNT_FINDBYDISCOUNTID_URL:serverBase+"/userDiscount/findDiscountId",
    USERDISCOUNT_FINDBYSELLERANDUSERNAME:serverBase+"/userDiscount/findBySellerAndUserName",
}