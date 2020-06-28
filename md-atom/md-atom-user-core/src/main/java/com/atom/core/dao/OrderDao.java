package com.atom.core.dao;

import com.atom.core.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao extends BaseDao {

    public List<Order> query() {
        return DB().select(Order.class).result().all(Order.class);
    }
}
