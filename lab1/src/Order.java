package shop;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final long id;
    private final Customer customer;
    private final List<Product> products;
    private OrderStatus status;

    public Order(long id, Customer customer, List<Product> products, OrderStatus status) {
        this.id = id;
        this.customer = customer;
        this.products = new ArrayList<>(products); // захисна копія
        this.status = status;
    }

    public long getId() { return id; }
    public Customer getCustomer() { return customer; }

    /** Повертає копію списку, щоб зовні не можна було змінити склад замовлення. */
    public List<Product> getProducts() { return new ArrayList<>(products); }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(id)
          .append(" [").append(status).append("]")
          .append(" клієнт: ").append(customer.getName())
          .append(", товари: ");
        for (int i = 0; i < products.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(products.get(i).getName());
        }
        return sb.toString();
    }
}
