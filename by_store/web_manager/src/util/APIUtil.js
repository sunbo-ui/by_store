var serverBase='http://localhost:8082'
export default {
    BASE_SERVER_URL: serverBase,
    BRAND_FINDPAGE_URL:serverBase + "/brand/findPage",//查询分页
    BRAND_FINDBYID_URL:serverBase + '/brand/findById',//根据id查询品牌
    BRAND_SAVE_URL:serverBase + '/brand/saveBrand',//保存品牌
    BRAND_UPDATE_URL:serverBase + '/brand/updateBrand',//更新品牌
    BRAND_DELETE_URL:serverBase + "/brand/deleteBrand",//删除品牌
    BRAND_SELECT_URL:serverBase + "/brand/findAll",//下拉框选择品牌

    SPEC_FINDPAGE_URL:serverBase + "/spec/findPage",//查询分页
    SPEC_FINDBYID_URL:serverBase + '/spec/findById',//根据id查询规格
    SPEC_SAVE_URL:serverBase + '/spec/saveSpec',//保存规格
    SPEC_UPDATE_URL:serverBase + '/spec/updateSpec',//更新规格
    SPEC_DELETE_URL:serverBase + "/spec/deleteSpec",//删除规格
    SPEC_SELECT_URL:serverBase + "/spec/findAll",//下拉框选择品牌

    TYPE_FINDPAGE_URL:serverBase + "/type/findPage",//查询分页
    TYPE_FINDBYID_URL:serverBase + '/type/findById',//根据id查询模板
    TYPE_SAVE_URL:serverBase + '/type/saveType',//保存模板
    TYPE_UPDATE_URL:serverBase + '/type/updateType',//更新模板
    TYPE_DELETE_URL:serverBase + "/type/deleteType",//删除模板

    SELLER_FINDPAGE_URL:serverBase + "/seller/findPage",//查询分页
    SELLER_FINDPAGESTATUS_URL:serverBase + "/seller/findPageStatus",//查询分页
    SELLER_FINDBYID_URL:serverBase + '/seller/findById',//根据id查询商家
    SELLER_FINDBYSTATUS_URL:serverBase + '/seller/findByStatus',//根据status查询商家
    SELLER_UPDATESTATUS_URL:serverBase + '/seller/updateStatus',//更新商家

    ITEMCAT_FINDBYPARENTID_URL:serverBase + "/itemCat/findByParentId",
    ITEMCAT_FINDALL_URL:serverBase +"/itemCat/findAll",//查询所有分类

    GOODS_ADD_URL:serverBase + "/goods/add",//添加商品
    GOODS_UPDATE_URL:serverBase + "/goods/update",//更新商品
    GOODS_DELETE_URL:serverBase + "/goods/delete",//删除商品
    GOODS_SEARCH_URL:serverBase + "/goods/search",//查询所有商品
    GOODS_FINDBYID_URL:serverBase + "/goods/findById",//根据id查询商品
    GOODS_FINDGOODSBYID_URL:serverBase + "/goods/findGoodsById",//根据id查询商品
    GOODS_UPDATESTATUS_URL:serverBase + "/goods/updateStatus",//修改状态
    GOODS_FINDBYSTATUS_URL:serverBase + "/goods/findByStatus",//查找状态

    LOGIN_LOGIN_URL:serverBase + '/login',//登录
    LOGIN_LOGOUT_URL:serverBase + '/user/logout',//登出

    CONTENT_CATEGORY_FINDALL_URL:serverBase + '/contentCategory/findAll',//广告分类
    CONTENT_CATEGORY_SAVE_URL:serverBase + '/contentCategory/save',//广告分类保存
    CONTENT_CATEGORY_UPDATE_URL:serverBase + '/contentCategory/update',//广告分类更新
    CONTENT_CATEGORY_FINDBYID_URL:serverBase + '/contentCategory/findById',//广告分类回显
    CONTENT_CATEGORY_DELETE_URL:serverBase + '/contentCategory/delete',//广告分类删除
    UPLOAD_UPLOADFILE_URL:serverBase + "/upload/uploadFile",

    CONTENT_FINDALL_URL:serverBase + '/content/findAll',//广告
    CONTENT_SAVE_URL:serverBase + '/content/save',//广告保存
    CONTENT_UPDATE_URL:serverBase + '/content/update',//广告更新
    CONTENT_FINDBYID_URL:serverBase + '/content/findById',//广告回显
    CONTENT_DELETE_URL:serverBase + '/content/delete',//广告删除
    CONTENT_FINDALLCATEGORY_URL:serverBase + '/content/category',//广告删除

    COMMENTS_FINDPAGE_URL:serverBase+"/comments/findByPage",
    COMMENTS_FINDBYID_URL:serverBase+"/comments/findById",
    COMMENTS_UPDATESTATUS_URL:serverBase+"/comments/updateStatus",
}