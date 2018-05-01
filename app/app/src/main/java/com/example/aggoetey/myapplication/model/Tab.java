package com.example.aggoetey.myapplication.model;

import com.example.aggoetey.myapplication.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 * <p>
 * Een tab is een lijst met orders die nog betaald moeten worden door een bepaalde tafel.
 */

public class Tab extends Model implements Serializable {

    private static final Tab ourInstance = new Tab();

    private List<Order> payedOrders = new ArrayList<>();
    private List<Order> orderedOrders = new ArrayList<>();
    private List<Order> receivedOrders = new ArrayList<>();
    private int amountOfOrders = 0;

    public List<Order> getPayedOrders() {
        return payedOrders;
    }

    public List<Order> getOrderedOrders() {
        return orderedOrders;
    }

    public List<Order> getReceivedOrders() { return receivedOrders; }

    private Tab() {
    }

    public static Tab getInstance() {
        return ourInstance;
    }

    /**
     * Begin een nieuw order, dit cancelt de vorige order indien nog niet afgerond
     */
    public Order newOrder() {
        return new Order();
    }


    /**
     * Hiermee wordt de order effectief geplaatst
     */
    public void commitOrder(Order order) {
        this.orderedOrders.add(order);
        order.setOrderNumber(amountOfOrders);
        amountOfOrders++;
    }

    public void payOrder(Order order) {
        orderedOrders.remove(order);
        receivedOrders.remove(order);
        payedOrders.add(order);
        fireInvalidationEvent();
    }

    public void receiveOrder(Order order) {
        orderedOrders.remove(order);
        receivedOrders.add(order);
        fireInvalidationEvent();
    }

    public static class Order extends Model implements Serializable {

        private List<OrderItem> orderItems = new ArrayList<>();
        private int orderNumber;

        private Order() {
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        private void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        /**
         * Voeg een orderItem toe aan de huidige order
         */
        public Order addOrderItem(String note, MenuItem menuItem) {
            this.orderItems.add(new OrderItem(note, menuItem));
            fireInvalidationEvent();
            return this;
        }

        public Order addOrderItem(OrderItem orderItem) {
            this.orderItems.add(orderItem);
            fireInvalidationEvent();
            return this;
        }

        /**
         * Verwijder een orderItem aan de hand van een OrderItem
         */
        public Order removeOrderItem(OrderItem orderItem) {
            this.orderItems.remove(orderItem);
            fireInvalidationEvent();
            return this;
        }

        /**
         * Lukt niet in 1 lijntje omdat we geen lambda's kunnen gebruiken...
         * <p>
         * Verwijder een orderitem aan de hand van een OrderItem
         */
        public void removeOrderItem(MenuItem menuItem) {
//            orderItems.removeIf(orderItem -> orderItem.getMenuItem().equals(menuItem));
            OrderItem toDelete = null;

            // Pass 1 - collect delete candidates
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getMenuItem().equals(menuItem)) {
                    toDelete = orderItem;
                    break;
                }
            }
            if (toDelete != null) {
                orderItems.remove(toDelete);
                fireInvalidationEvent();
            }
        }

        public double getPrice() {
            double prijs = 0;
            for (OrderItem orderItem : orderItems) {
                prijs += orderItem.getMenuItem().price;
            }
            return prijs;
        }

        public static class OrderItem implements Serializable {
            private String note;
            private MenuItem menuItem;

            private OrderItem(String note, MenuItem menuItem) {
                this.note = note;
                this.menuItem = menuItem;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public MenuItem getMenuItem() {
                return menuItem;
            }

            public void setMenuItem(MenuItem menuItem) {
                this.menuItem = menuItem;
            }
        }


    }
}
