package shop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервіс для керування замовленнями. Усі замовлення зберігаються в пам'яті
 * (ключ — id замовлення, порядок додавання зберігається).
 */
public class OrderService {

    private final Map<Long, Order> orders = new LinkedHashMap<>();

    /** Додає замовлення. Кидає IllegalArgumentException, якщо order == null або id вже існує. */
    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Замовлення не може бути null");
        }
        if (orders.containsKey(order.getId())) {
            throw new IllegalArgumentException("Замовлення з id=" + order.getId() + " вже існує");
        }
        orders.put(order.getId(), order);
    }

    /** Видаляє замовлення. Кидає NoSuchElementException, якщо його не існує. */
    public void removeOrder(long orderId) {
        if (orders.remove(orderId) == null) {
            throw new NoSuchElementException("Замовлення з id=" + orderId + " не знайдено");
        }
    }

    /** Шукає замовлення за id. Кидає NoSuchElementException, якщо не знайдено. */
    public Order findOrderById(long orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new NoSuchElementException("Замовлення з id=" + orderId + " не знайдено");
        }
        return order;
    }

    public List<Order> findOrdersByCustomer(long customerId) {
        return orders.values().stream()
                .filter(o -> o.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orders.values().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersContainingProduct(long productId) {
        return orders.values().stream()
                .filter(o -> o.getProducts().stream().anyMatch(p -> p.getId() == productId))
                .collect(Collectors.toList());
    }

    /** Сума цін усіх товарів у замовленні. */
    public double calculateOrderTotal(long orderId) {
        return total(findOrderById(orderId));
    }

    /** Унікальні клієнти, які мають хоча б одне замовлення. */
    public Set<Customer> findCustomersWithOrders() {
        return orders.values().stream()
                .map(Order::getCustomer)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /** Замовлення, відсортовані за загальною вартістю — від найдешевшого до найдорожчого. */
    public List<Order> sortOrdersByTotalPrice() {
        List<Order> sorted = new ArrayList<>(orders.values());
        sorted.sort(Comparator.comparingDouble(this::total));
        return sorted;
    }

    /** Кількість замовлень за кожним статусом (статуси без замовлень мають значення 0). */
    public Map<OrderStatus, Long> countOrdersByStatus() {
        Map<OrderStatus, Long> result = new EnumMap<>(OrderStatus.class);
        for (OrderStatus status : OrderStatus.values()) {
            result.put(status, 0L);
        }
        for (Order order : orders.values()) {
            result.merge(order.getStatus(), 1L, Long::sum);
        }
        return result;
    }

    private double total(Order order) {
        return order.getProducts().stream().mapToDouble(Product::getPrice).sum();
    }
}
