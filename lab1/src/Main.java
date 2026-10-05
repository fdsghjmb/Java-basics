package shop;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        // ---------- Тестові дані ----------
        Customer ivan = new Customer(1, "Іван Петренко", "ivan@example.com");
        Customer olena = new Customer(2, "Олена Коваль", "olena@example.com");
        Customer taras = new Customer(3, "Тарас Шевчук", "taras@example.com");
        Customer maria = new Customer(4, "Марія Бондар", "maria@example.com"); // без замовлень

        Product laptop = new Product(101, "Ноутбук", ProductCategory.ELECTRONICS, 32000.00);
        Product phone = new Product(102, "Смартфон", ProductCategory.ELECTRONICS, 18500.50);
        Product jacket = new Product(103, "Куртка", ProductCategory.CLOTHING, 2400.00);
        Product novel = new Product(104, "Роман «Тигролови»", ProductCategory.BOOKS, 250.00);
        Product lamp = new Product(105, "Настільна лампа", ProductCategory.HOME, 780.00);
        Product gift = new Product(106, "Подарункова карта", ProductCategory.OTHER, 500.00);

        OrderService service = new OrderService();

        // ---------- 1. addOrder ----------
        printHeader("1. addOrder — додавання замовлень");
        service.addOrder(new Order(1, ivan, Arrays.asList(laptop, novel), OrderStatus.NEW));
        service.addOrder(new Order(2, olena, Arrays.asList(phone, jacket, gift), OrderStatus.PROCESSING));
        service.addOrder(new Order(3, ivan, Arrays.asList(lamp, novel), OrderStatus.SHIPPED));
        service.addOrder(new Order(4, taras, Arrays.asList(jacket), OrderStatus.DELIVERED));
        service.addOrder(new Order(5, olena, Arrays.asList(laptop, phone), OrderStatus.NEW));
        service.addOrder(new Order(6, taras, Arrays.asList(gift, lamp), OrderStatus.CANCELLED));
        System.out.println("Додано 6 замовлень.");

        try {
            service.addOrder(new Order(1, taras, Arrays.asList(novel), OrderStatus.NEW));
        } catch (IllegalArgumentException e) {
            System.out.println("Очікувана помилка при дублюванні id: " + e.getMessage());
        }

        // ---------- 2. findOrderById ----------
        printHeader("2. findOrderById");
        System.out.println("Замовлення #3: " + service.findOrderById(3));
        try {
            service.findOrderById(999);
        } catch (NoSuchElementException e) {
            System.out.println("Очікувана помилка: " + e.getMessage());
        }

        // ---------- 3. findOrdersByCustomer ----------
        printHeader("3. findOrdersByCustomer");
        printList("Замовлення клієнта id=1 (" + ivan.getName() + "):", service.findOrdersByCustomer(1));
        printList("Замовлення клієнта id=4 (" + maria.getName() + "):", service.findOrdersByCustomer(4));

        // ---------- 4. findOrdersByStatus ----------
        printHeader("4. findOrdersByStatus");
        printList("Замовлення зі статусом NEW:", service.findOrdersByStatus(OrderStatus.NEW));
        printList("Замовлення зі статусом SHIPPED:", service.findOrdersByStatus(OrderStatus.SHIPPED));

        // ---------- 5. findOrdersContainingProduct ----------
        printHeader("5. findOrdersContainingProduct");
        printList("Замовлення, що містять «" + novel.getName() + "»:",
                service.findOrdersContainingProduct(novel.getId()));
        printList("Замовлення, що містять «" + jacket.getName() + "»:",
                service.findOrdersContainingProduct(jacket.getId()));

        // ---------- 6. calculateOrderTotal ----------
        printHeader("6. calculateOrderTotal");
        for (long id = 1; id <= 6; id++) {
            System.out.printf("Order #%d: %.2f грн%n", id, service.calculateOrderTotal(id));
        }

        // ---------- 7. findCustomersWithOrders ----------
        printHeader("7. findCustomersWithOrders");
        System.out.println("Клієнти, які мають замовлення:");
        service.findCustomersWithOrders().forEach(c -> System.out.println("  " + c));
        System.out.println("(клієнт без замовлень «" + maria.getName() + "» у списку відсутній)");

        // ---------- 8. sortOrdersByTotalPrice ----------
        printHeader("8. sortOrdersByTotalPrice (за зростанням вартості)");
        for (Order o : service.sortOrdersByTotalPrice()) {
            System.out.printf("  %10.2f грн  ->  %s%n", service.calculateOrderTotal(o.getId()), o);
        }

        // ---------- 9. countOrdersByStatus ----------
        printHeader("9. countOrdersByStatus");
        printStatusCounts(service.countOrdersByStatus());

        // ---------- 10. removeOrder ----------
        printHeader("10. removeOrder");
        service.removeOrder(6);
        System.out.println("Замовлення #6 видалено.");
        try {
            service.removeOrder(6);
        } catch (NoSuchElementException e) {
            System.out.println("Очікувана помилка при повторному видаленні: " + e.getMessage());
        }
        System.out.println("Статистика після видалення:");
        printStatusCounts(service.countOrdersByStatus());

        // Бонус: зміна статусу впливає на статистику
        printHeader("Бонус: зміна статусу замовлення #1 на PROCESSING");
        service.findOrderById(1).setStatus(OrderStatus.PROCESSING);
        printStatusCounts(service.countOrdersByStatus());
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    private static void printList(String caption, List<Order> orders) {
        System.out.println(caption);
        if (orders.isEmpty()) {
            System.out.println("  (нічого не знайдено)");
        } else {
            orders.forEach(o -> System.out.println("  " + o));
        }
    }

    private static void printStatusCounts(Map<OrderStatus, Long> counts) {
        counts.forEach((status, count) -> System.out.printf("  %-11s : %d%n", status, count));
    }
}
