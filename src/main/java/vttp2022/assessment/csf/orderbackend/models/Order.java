package vttp2022.assessment.csf.orderbackend.models;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

// IMPORTANT: You can add to this class, but you cannot delete its original content

public class Order {

	private Integer orderId;
	private String name;
	private String email;
	private Integer size;
	private String sauce;
	private Boolean thickCrust;
	private List<String> toppings = new LinkedList<>();
	private String comments;

	public void setOrderId(Integer orderId) { this.orderId = orderId; }
	public Integer getOrderId() { return this.orderId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	public void setSize(Integer size) { this.size = size; }
	public Integer getSize() { return this.size; }

	public void setSauce(String sauce) { this.sauce = sauce; }
	public String getSauce() { return this.sauce; }

	public void setThickCrust(Boolean thickCrust) { this.thickCrust = thickCrust; }
	public Boolean isThickCrust() { return this.thickCrust; }

	public void setToppings(List<String> toppings) { this.toppings = toppings; }
	public List<String> getToppings() { return this.toppings; }
	public void addTopping(String topping) { this.toppings.add(topping); }

	public void setComments(String comments) { this.comments = comments; }
	public String getComments() { return this.comments; }

	public static Order createOrder(String payload) {

		JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject data = reader.readObject();

		Order o = new Order();
		//o.setName(payload.getString);
		o.setName(data.getString("name"));
		o.setEmail(data.getString("email"));
		o.setSize(data.getInt("size"));
		o.setSauce(data.getString("sauce"));
		o.setComments(data.getString("comments"));

		JsonArray toppings = data.getJsonArray("toppings");
		for (JsonValue ingred: toppings) {
			String ingredS = ingred.toString();
			//System.out.println("jsonVal toppings: " + ingred.toString().substring(1, ingredS.length()-1));
			o.toppings.add(ingredS.substring(1, ingredS.length()-1));
		}
		// System.out.println(">>>> toppings: " + o.getToppings().toString());
		// System.out.println(">>> model base: " + data.getString("base"));
		if (data.getString("base").contains("thin")) {
			o.setThickCrust(false);
		} else {
			o.setThickCrust(true);
		}

		// // System.out.println("model: isThickCrust?: " + o.isThickCrust());
		return o;

	}
}
