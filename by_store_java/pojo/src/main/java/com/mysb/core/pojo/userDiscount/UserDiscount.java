package com.mysb.core.pojo.userDiscount;

import java.io.Serializable;

public class UserDiscount implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 商户
     */
    private String sellerId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 折扣Id
     */
    private Integer discountId;

    /**
     * 是否过期
     */
    private Integer isSalte;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId == null ? null : sellerId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public Integer getIsSalte() {
        return isSalte;
    }

    public void setIsSalte(Integer isSalte) {
        this.isSalte = isSalte;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sellerId=").append(sellerId);
        sb.append(", userId=").append(userId);
        sb.append(", discountId=").append(discountId);
        sb.append(", isSalte=").append(isSalte);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserDiscount other = (UserDiscount) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSellerId() == null ? other.getSellerId() == null : this.getSellerId().equals(other.getSellerId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getDiscountId() == null ? other.getDiscountId() == null : this.getDiscountId().equals(other.getDiscountId()))
            && (this.getIsSalte() == null ? other.getIsSalte() == null : this.getIsSalte().equals(other.getIsSalte()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSellerId() == null) ? 0 : getSellerId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getDiscountId() == null) ? 0 : getDiscountId().hashCode());
        result = prime * result + ((getIsSalte() == null) ? 0 : getIsSalte().hashCode());
        return result;
    }
}