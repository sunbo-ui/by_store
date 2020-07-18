package com.mysb.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysb.core.dao.address.AddressDao;
import com.mysb.core.dao.customer.CustomerDao;
import com.mysb.core.pojo.address.Address;
import com.mysb.core.pojo.address.AddressQuery;
import com.mysb.core.pojo.entry.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

    @Autowired
    private CustomerDao customerDao;
    @Override
    public PageResult findByUsername(Integer page,Integer rows,String username) {

        PageHelper.startPage(page,rows);
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.setOrderByClause("is_default desc");
        AddressQuery.Criteria queryCriteria = addressQuery.createCriteria();
        queryCriteria.andUserIdEqualTo(username);

        Page<Address> addresses = (Page<Address>)addressDao.selectByExample(addressQuery);
        PageResult pageResult = new PageResult(addresses.getTotal(), addresses.getResult());
        return pageResult;
    }

    @Override
    public void saveAddress(String username,Address address) {
        //设置默认地址
        address.setIsDefault("0");
        address.setCreateDate(new Date());
        address.setUserId(username);
        addressDao.insertSelective(address);
    }

    @Override
    public void setDefault(String username,Long id) {
        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria queryCriteria = addressQuery.createCriteria();
        queryCriteria.andUserIdEqualTo(username);
        List<Address> addresses = addressDao.selectByExample(addressQuery);

        for (Address address : addresses) {
            if(address.getId() == id){
                address.setIsDefault("1");
            }else{
                address.setIsDefault("0");
            }
            addressDao.updateByPrimaryKeySelective(address);
        }
    }

    @Override
    public void updateAddress(Address address) {
        addressDao.updateByPrimaryKeySelective(address);
    }

    @Override
    public Address findById(Long id) {
        return addressDao.selectByPrimaryKey(id);
    }

    @Override
    public void deleteAddress(Long id) {
        addressDao.deleteByPrimaryKey(id);
    }
}
