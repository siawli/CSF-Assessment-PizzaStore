package vttp2022.assessment.csf.orderbackend.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private PricingService priceSvc;

	@Autowired
	private OrderRepository orderRepo;

	// POST /api/order
	// Create a new order by inserting into orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public void createOrder(Order order) {
		orderRepo.saveOrders(order);
	}

	// GET /api/order/<email>/all
	// Get a list of orders for email from orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public List<OrderSummary> getOrdersByEmail(String email) {
		// Use priceSvc to calculate the total cost of an order
		Optional<List<Order>> ordersOpt = this.orderRepo.getOrders(email);
		if (ordersOpt.isEmpty()) {
			return List.of();
		}

		List<Order> orderList = ordersOpt.get();

		List<OrderSummary> orderSumList = new LinkedList<>();

		for (Order o: orderList) {

			Float sizePrice = priceSvc.size(o.getSize());
			Float crustPrice = 0.0f;
			Float toppingSumPrice = 0.0f;
			Float saucePrice = priceSvc.sauce(o.getSauce());
			
			// System.out.println(">>>> orderSvc isThickCrust?: " + o.isThickCrust());
			if (o.isThickCrust()) {
				crustPrice = priceSvc.thickCrust();
			} else {
				crustPrice = priceSvc.thinCrust();
			}

			List<String> toppings = o.getToppings();
			for (String topping: toppings) {
				toppingSumPrice += priceSvc.topping(topping);
				// System.out.println(">>> topping price: " + topping + " " + priceSvc.topping(topping));
			}

			Float totalSum = sizePrice + crustPrice + toppingSumPrice + saucePrice;
			// /*
			// System.out.println(">>>> sizePrice: " + sizePrice);
			// System.out.println(">>>> orderservice crustPrice: " + crustPrice);
			// System.out.println(">>>> toppingSumPrice: " + toppingSumPrice);
			// System.out.println(">>>> sauce: " + saucePrice);

			// System.out.println(">>>> totalSum: " + totalSum);
			// // */

			OrderSummary orderSum = new OrderSummary();
			orderSum.setName(o.getName());
			orderSum.setEmail(o.getEmail());
			orderSum.setOrderId(o.getOrderId());
			orderSum.setAmount(totalSum);
			
			orderSumList.add(orderSum);
		}

		return orderSumList;
	}
}
