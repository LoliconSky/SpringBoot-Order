package com.bfchengnuo.sell.po;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 对于 @DynamicUpdate 的说明：
 * 如果我们在更新表时,只想更新某个修改了的字段,就加 @DynamicUpdate,通常为了提高更新表时的效率.
 * 它的作用是更新“变化”的字段
 * 反之,如果我们更新某个字段时,更新所有的字段,就可以加上 @DynamicUpdate(false). 默认值是 true，不加注解相当于 false
 * DynamicInsert 同理（插入不为空的列）
 *
 * 本例中，如果增加了 updateTime 字段，则需要使用 DynamicUpdate 注解配合数据设置的
 *      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 *      来由数据库自动更新这个更新时间，因为 updateTime 明确使用 sql 更新的情况下，数据库设置的规则也就无效了
 *
 * 然而，当我们使用dynamic-insert、dynamic-update时, Hibernate都要重新计算一次相应的SQL语句，在Hibernate层面会有性能损耗。
 * 只有表中有庞大的blog列或者有超多的列，dynamic insert、 dynamic update才有意义；否则我不认为它们会带来性能提升。
 *
 * Created by 冰封承諾Andy on 2018/7/17.
 */
@Entity
@DynamicUpdate
@Data
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String categoryName;

    private Integer categoryType;

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;
}
