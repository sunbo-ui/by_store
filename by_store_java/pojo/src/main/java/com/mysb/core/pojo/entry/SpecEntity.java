package com.mysb.core.pojo.entry;

import com.mysb.core.pojo.specification.Specification;
import com.mysb.core.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

public class SpecEntity implements Serializable {
    //规格选项集合
    private List<SpecificationOption> specOptionList;
    //规格对象
    private Specification specification;

    public List<SpecificationOption> getSpecOptionList() {
        return specOptionList;
    }

    public void setSpecOptionList(List<SpecificationOption> specOptionList) {
        this.specOptionList = specOptionList;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    @Override
    public String toString() {
        return "SpecEntity{" +
                "specOptionList=" + specOptionList +
                ", specification=" + specification +
                '}';
    }
}
