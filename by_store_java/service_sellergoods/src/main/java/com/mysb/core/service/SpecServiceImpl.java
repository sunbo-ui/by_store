package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.specification.SpecificationDao;
import com.mysb.core.dao.specification.SpecificationOptionDao;
import com.mysb.core.pojo.entry.PageResult;
import com.mysb.core.pojo.entry.SpecEntity;
import com.mysb.core.pojo.specification.Specification;
import com.mysb.core.pojo.specification.SpecificationOption;
import com.mysb.core.pojo.specification.SpecificationOptionQuery;
import com.mysb.core.pojo.specification.SpecificationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecificationDao specDao;
    @Autowired
    private SpecificationOptionDao optionDao;

    @Override
    public List<Specification> findAll() {
        return specDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer page, Integer rows, Specification spec) {
        //第一个参数:当前页  第二个参数:每页数据条数
        PageHelper.startPage(page,rows);
        SpecificationQuery query = new SpecificationQuery();
        query.setOrderByClause("id desc");
        if(spec != null){
            SpecificationQuery.Criteria criteria = query.createCriteria();
            if(!"".equals(spec.getSpecName()) && spec.getSpecName() != null){
                criteria.andSpecNameLike("%"+spec.getSpecName()+"%");
            }
        }
        Page<Specification> specs =(Page<Specification>) specDao.selectByExample(query);
        return new PageResult(specs.getTotal(),specs.getResult());
    }

    @Override
    public void saveSpec(SpecEntity spec) {
       if(spec.getSpecification()!=null){
           //保存规格对象
           specDao.insertSelective(spec.getSpecification());
           //保存规格选项
           if(spec.getSpecOptionList() != null){
               for (SpecificationOption option : spec.getSpecOptionList()){
                   //设置规格选项外键
                   option.setSpecId(spec.getSpecification().getId());
                   optionDao.insertSelective(option);
               }
           }
       }
    }

    @Override
    public SpecEntity findById(Integer id) {
        if(id != null){
            //查询规格
            Specification specification = specDao.selectByPrimaryKey(Long.parseLong(String.valueOf(id)));
            Long specId = specification.getId();
            //规格规格id查询规格选项
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecIdEqualTo(specId);
            List<SpecificationOption> options = optionDao.selectByExample(query);
            //赋值
            SpecEntity entity = new SpecEntity();
            entity.setSpecification(specification);
            entity.setSpecOptionList(options);
            return entity;
        }
        return null;
    }

    @Override
    public void updateSpec(SpecEntity spec) {
        if(spec.getSpecification()!=null){
            //更新规格对象
            specDao.updateByPrimaryKeySelective(spec.getSpecification());
            //更新规格选项
            if(spec.getSpecOptionList() != null){
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = query.createCriteria();
                criteria.andSpecIdEqualTo(spec.getSpecification().getId());
                optionDao.deleteByExample(query);

                for (SpecificationOption option : spec.getSpecOptionList()){
                    //设置规格选项外键
                    option.setSpecId(spec.getSpecification().getId());
                    optionDao.insertSelective(option);
                }
            }
        }
    }

    @Override
    public void deleteSpec(Map<String, List<Specification>> map) {
        List<Specification> idList = map.get("idList");
        if(idList!=null){
            List<Long> optionId = new ArrayList<>();
            for (Specification spec:idList) {
                optionId.add(spec.getId());
            }
            //删除规格选项
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecIdIn(optionId);
            optionDao.deleteByExample(query);

            //删除规格对象
            SpecificationQuery specQuery = new SpecificationQuery();
            SpecificationQuery.Criteria specQueryCriteria = specQuery.createCriteria();
            specQueryCriteria.andIdIn(optionId);
            specDao.deleteByExample(specQuery);

        }
    }
}
