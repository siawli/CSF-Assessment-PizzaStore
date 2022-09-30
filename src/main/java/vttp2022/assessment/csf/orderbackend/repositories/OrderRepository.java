package vttp2022.assessment.csf.orderbackend.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate template;

    private final String SQL_SAVE_ORDER = 
        "insert into orders (name, email, pizza_size, thick_crust, sauce, toppings, comments) values (?, ?, ?, ?, ?, ?, ?)";

    private final String SQL_GET_ORDERS = 
        "select * from orders where email = ?";

    public void saveOrders(Order order) {
        String ordersToppings = order.getToppings().toString();
        template.update(SQL_SAVE_ORDER,
            order.getName(), order.getEmail(), order.getSize(),
            order.isThickCrust(), order.getSauce(), ordersToppings.substring(1, ordersToppings.length()-1),
            order.getComments());
    }

    public Optional<List<Order>> getOrders(String email) {
        // try {
        //     Thread.sleep(3000);
        // } catch (Exception ex) {
        //     ex.printStackTrace();
        // }

        SqlRowSet result = template.queryForRowSet(SQL_GET_ORDERS, email);
        List<Order> orderList = new LinkedList<>();

        if (!result.next()) {
            return Optional.empty();
        }
        result.first();
        while (result.next()) {
            Order o = new Order();
            o.setName(result.getString("name"));
            o.setEmail(result.getString("email"));
            o.setOrderId(result.getInt("order_id"));

            String toppings = result.getString("toppings");
            for (String topping: toppings.split(",")) {
                o.addTopping(topping);
            }
            o.setSauce(result.getString("sauce"));
            o.setSize(result.getInt("pizza_size"));
            o.setThickCrust(result.getBoolean("thick_crust"));

            //System.out.println("id: " + o.getOrderId());
            orderList.add(o);
        }

        return Optional.of(orderList);
    }
    
}
