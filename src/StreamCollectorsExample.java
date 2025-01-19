import java.util.*;
import java.util.stream.Collectors;

public class StreamCollectorsExample {

    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

System.out.println("Создайте список заказов с разными продуктами и их стоимостями:");
        List<String> ordersInfo = orders.stream()
                .map(order -> order.product() + " - " + order.cost())
                .toList();
        System.out.println(ordersInfo);

System.out.println("Группируйте заказы по продуктам.:");
        List<String> groupAndSortOrders = orders.stream()
                .collect(Collectors.groupingBy(Order::product))
                .values().stream()
                .flatMap(productOrders -> productOrders.stream()
                        .sorted(Comparator.comparingDouble(Order::cost).reversed()))
                .map(Order::toString)
                .toList();

        groupAndSortOrders.forEach(System.out::println);


System.out.println("Для каждого продукта найдите общую стоимость всех заказов:");
        Map<String,Double> summingOrders = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::product,
                        Collectors.summingDouble(Order::cost)
                ));
        summingOrders.forEach((
                prod,
                cost)
                -> System.out.println(prod+" - "+cost));

System.out.println("Отсортируйте продукты по убыванию общей стоимости desc:");
        LinkedHashMap<String, Double> sortedProducts = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::product,
                        Collectors.summingDouble(Order::cost)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        //в случае дублирования - оставить первое (e1, e2) -> e1
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        sortedProducts.forEach((
                prod,
                cost)
                -> System.out.println(prod+" - "+cost));

System.out.println("Выберите три самых дорогих продукта:");
        orders.stream()
                .sorted(Comparator.comparingDouble(Order::cost).reversed())
                .limit(3)
                .forEach(order -> System.out.println(
                        order.product() + ": " +
                        order.cost()));

System.out.println("Выведите результат: список трех самых дорогих продуктов и их общая стоимость.");
        List<Order> top3Orders = orders.stream()
                .sorted(Comparator.comparingDouble(Order::cost).reversed())
                .limit(3)
                //teeing() - принимает 2 коллектора и функцию marge
                .collect(Collectors.teeing(Collectors.toList(), Collectors.summingDouble(Order::cost),
                        (list, sum) -> {
                            list.forEach(order -> System.out.println(order.product() + " - " + order.cost()));
                            System.out.println("Общая стоимость трех самых дорогих продуктов: " + sum);
                            return list;
                        }
                ));
        System.out.println("result  List<Order> top3Orders is: "+top3Orders);


















    }
}