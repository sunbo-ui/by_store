package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.good.BrandDao;
import com.mysb.core.dao.good.GoodsDao;
import com.mysb.core.dao.good.GoodsDescDao;
import com.mysb.core.dao.item.ItemCatDao;
import com.mysb.core.dao.item.ItemDao;
import com.mysb.core.dao.seller.SellerDao;
import com.mysb.core.pojo.entry.GoodsEntity;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.good.Brand;
import com.mysb.core.pojo.good.Goods;
import com.mysb.core.pojo.good.GoodsDesc;
import com.mysb.core.pojo.good.GoodsQuery;
import com.mysb.core.pojo.item.Item;
import com.mysb.core.pojo.item.ItemCat;
import com.mysb.core.pojo.item.ItemQuery;
import com.mysb.core.pojo.seller.Seller;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao descDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao catDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private JmsTemplate jmsTemplate;
    //为商品上架使用
    @Autowired
    private ActiveMQTopic topicPageAndSolrDestination;
    //为商品下架使用
    @Autowired
    private ActiveMQQueue queueSolrDeleteDestination;


    public void add(GoodsEntity goodsEntity) {
        //1. 保存商品对象
        //刚添加的商品状态默认为0 未申请
        goodsEntity.getGoods().setAuditStatus("0");
        //设置未删除商品
        goodsEntity.getGoods().setIsDelete("0");
        //设置small_pic
        String itemImages = goodsEntity.getGoodsDesc().getItemImages();
        List<Map> maps = JSON.parseArray(itemImages, Map.class);
        if (maps != null && maps.size() > 0) {
            String url = String.valueOf(maps.get(0).get("url"));
            goodsEntity.getGoods().setSmallPic(url);
        }
        //添加商品
        goodsDao.insertSelective(goodsEntity.getGoods());
        //2. 保存商品详情对象
        //商品主键作为商品详情的主键
        goodsEntity.getGoodsDesc().setGoodsId(goodsEntity.getGoods().getId());
        //添加商品详情
        descDao.insertSelective(goodsEntity.getGoodsDesc());

        //3.保存规格,库存
        insertItem(goodsEntity);
    }

    @Override
    public PageResult findPage(Goods goods, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        GoodsQuery query = new GoodsQuery();
        query.setOrderByClause("id desc");
        GoodsQuery.Criteria criteria = query.createCriteria();
        if (goods != null) {
            if (goods.getGoodsName() != null && !"".equals(goods.getGoodsName())) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus())) {
                criteria.andAuditStatusEqualTo(goods.getAuditStatus());
            }
            if (goods.getSellerId() != null && !"".equals(goods.getSellerId())
                    && !"admin".equals(goods.getSellerId())) {
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
        }
        criteria.andIsDeleteNotEqualTo("1");
        Page<Goods> goodsList = (Page<Goods>) goodsDao.selectByExample(query);
        return new PageResult(goodsList.getTotal(), goodsList.getResult());
    }

    /**
     * 保存库存
     *
     * @param goodsEntity
     */
    public void insertItem(GoodsEntity goodsEntity) {
        if ("1".equals(goodsEntity.getGoods().getIsEnableSpec())) {
            if (goodsEntity.getItemList() != null) {
                for (Item item : goodsEntity.getItemList()) {
                    String title = goodsEntity.getGoods().getGoodsName();
                    String spec = item.getSpec();
                    Map map = JSON.parseObject(spec, Map.class);
                    Collection<String> values = map.values();
                    for (String value : values) {
                        System.out.println(value);
                        title = title + " " + value;
                    }
                    //设置标题
                    item.setTitle(title);
                    //设置库存属性
                    Item item2 = setItemValue(goodsEntity, item);
                    itemDao.insertSelective(item2);
                }
            }
        } else {
            Item item = new Item();
            item.setPrice(new BigDecimal("9999"));
            item.setNum(0);
            item.setSpec("{}");
            item.setTitle(goodsEntity.getGoods().getGoodsName());
            Item item1 = setItemValue(goodsEntity, item);
            itemDao.insertSelective(item1);
        }
    }

    public Item setItemValue(GoodsEntity goodsEntity, Item item) {
        //商品id
        item.setGoodsId(goodsEntity.getGoods().getId());
        //创建时间
        item.setCreateTime(new Date());
        //更新时间
        item.setUpdateTime(new Date());
        //商家id
        item.setSellerId(goodsEntity.getGoods().getSellerId());
        //库存状态, 默认为0, 未审核
        item.setStatus("0");
        //分类id, 库存使用商品的第三级分类最为库存分类
        item.setCategoryid(goodsEntity.getGoods().getCategory3Id());
        //分类名称
        ItemCat itemCat = catDao.selectByPrimaryKey(goodsEntity.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //品牌名称
        Brand brand = brandDao.selectByPrimaryKey(goodsEntity.getGoods().getBrandId());
        item.setBrand(brand.getName());
        //卖家名称
        Seller seller = sellerDao.selectByPrimaryKey(goodsEntity.getGoods().getSellerId());
        item.setSeller(seller.getName());
        //示例图片
        String itemImages = goodsEntity.getGoodsDesc().getItemImages();
        List<Map> maps = JSON.parseArray(itemImages, Map.class);
        if (maps != null && maps.size() > 0) {
            String url = String.valueOf(maps.get(0).get("url"));
            item.setImage(url);
        }
        return item;
    }

    @Override
    public GoodsEntity findById(Long id) {
        //根据商品id查询商品对象
        Goods goods = goodsDao.selectByPrimaryKey(id);
        //根据商品id查询商品详情
        GoodsDesc goodsDesc = descDao.selectByPrimaryKey(id);
        //根据商品id查询库存集合对象
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(query);

        //封装城GoodEntity
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoods(goods);
        goodsEntity.setGoodsDesc(goodsDesc);
        goodsEntity.setItemList(items);
        return goodsEntity;
    }

    @Override
    public void update(final GoodsEntity goodsEntity) {
        //重新设置状态
        goodsEntity.getGoods().setAuditStatus("0");
        //删除索引库根据goodsId
        jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(String.valueOf(goodsEntity.getGoods().getId()));
                return textMessage;
            }
        });
        //修改商品对象
        goodsDao.updateByPrimaryKeySelective(goodsEntity.getGoods());
        //修改商品详情
        descDao.updateByPrimaryKeySelective(goodsEntity.getGoodsDesc());
        //根据id删除对应的库存集合
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(goodsEntity.getGoods().getId());
        itemDao.deleteByExample(query);
        //添加库存集合
        insertItem(goodsEntity);
    }

    @Override
    public void delete(Map<String, List<Goods>> map) {
        List<Goods> idList = map.get("idList");
        if (idList != null) {
            for (final Goods goods : idList) {
                goods.setIsDelete("1");
                goodsDao.updateByPrimaryKeySelective(goods);
                //删除索引库根据goodsId
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage = session.createTextMessage(String.valueOf(goods.getId()));
                        return textMessage;
                    }
                });
            }
            this.updateStatus(3l,map);
        }
    }

    @Override
    public void updateStatus(Long id,Map<String,List<Goods>> map) {
        List<Goods> idList = map.get("idList");
        if (idList != null) {
            for (final Goods goods : idList) {
                goods.setAuditStatus(String.valueOf(id));
                /**
                 *  0:"未申请",1:"申请中",2:"审核通过",3:"已驳回"
                 */
                goodsDao.updateByPrimaryKeySelective(goods);
                ItemQuery query = new ItemQuery();
                ItemQuery.Criteria criteria = query.createCriteria();
                criteria.andGoodsIdEqualTo(goods.getId());
                //查出对应库存
                List<Item> items = itemDao.selectByExample(query);
                //遍历每个库存修改状态
                for (Item item : items) {
                    item.setStatus(String.valueOf(id));
                    ItemQuery itemQuery = new ItemQuery();
                    ItemQuery.Criteria queryCriteria = itemQuery.createCriteria();
                    queryCriteria.andIdEqualTo(item.getId());
                    itemDao.updateByExampleSelective(item,itemQuery);
                }
                if(id == 2 || "2".equals(id)){
                    System.out.println("send");
                    jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            TextMessage textMessage = session.createTextMessage(String.valueOf(goods.getId()));
                            return textMessage;
                        }
                    });
                }else{
                    //删除索引库根据goodsId
                    jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            TextMessage textMessage = session.createTextMessage(String.valueOf(goods.getId()));
                            return textMessage;
                        }
                    });
                }
            }

        }
    }
    /**
     * 根据商品id和状态查询库存
     * @return
     */
    public List<Item> findItemByGoodsIdAndStatus(Long id,Map<String,List<Goods>> map){
        ItemQuery query = new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        ArrayList<Long> list = new ArrayList<>();
        List<Goods> idList = map.get("idList");
        for (Goods goods : idList) {
            list.add(goods.getId());
        }
        criteria.andStatusEqualTo(String.valueOf(id));
        criteria.andGoodsIdIn(list);
        return itemDao.selectByExample(query);
    }
    public List<Long> findItemByGoodsId(Map<String,List<Goods>> map){
        List<Long> list = new ArrayList<>();
        List<Goods> idList = map.get("idList");
        for (Goods goods : idList) {
            list.add(goods.getId());
        }
        return list;
    }

    @Override
    public Goods findGoodsById(Long id) {
        return goodsDao.selectByPrimaryKey(id);
    }

    @Override
    public PageResult findByStatus(Integer page, Integer rows, String status) {
        PageHelper.startPage(page,rows);
        GoodsQuery query = new GoodsQuery();
        query.setOrderByClause("id desc");
        GoodsQuery.Criteria criteria = query.createCriteria();
        if(!status.equals("-1")){
            criteria.andAuditStatusEqualTo(status);
        }
        Page<Goods> goods = (Page<Goods>)goodsDao.selectByExample(query);
        return new PageResult(goods.getTotal(),goods.getResult());
    }

    @Override
    public List<Goods> findBySellerId(String username) {
        GoodsQuery query = new GoodsQuery();
        GoodsQuery.Criteria criteria = query.createCriteria();
        criteria.andSellerIdEqualTo(username);
        return goodsDao.selectByExample(query);
    }

}
