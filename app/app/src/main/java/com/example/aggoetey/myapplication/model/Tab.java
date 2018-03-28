package com.example.aggoetey.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by aggoetey on 3/20/18.
 * <p>
 * Een tab is een lijst met orders die nog betaald moeten worden door een bepaalde tafel.
 */

public class Tab extends Observable {

    private static final Tab ourInstance = new Tab();

    private List<Order> payedOrders = new ArrayList<>();
    private List<Order> orderedOrders = new ArrayList<>();
    private Order currentOrder;
    private int amountOfOrders = 0;

    public List<Order> getPayedOrders() {
        return payedOrders;
    }
    public List<Order> getOrderedOrders() {
        return orderedOrders;
    }

    private Tab() {
    }

    public static Tab getInstance() {
        return ourInstance;
    }

    /**
     * Begin een nieuw order, dit cancelt de vorige order indien nog niet afgerond
     */
    public Tab beginOrder(){
        currentOrder = new Order(amountOfOrders);
        return this;
    }

    /**
     * Voeg een orderItem toe aan de huidige order
     */
    public Tab addOrderItem(String note, MenuItem menuItem){
        currentOrder.addOrderItem(note, menuItem);
        return this;
    }

    /**
     * Hiermee wordt de order effectief geplaatst
     */
    public Tab commitOrder(){
        this.orderedOrders.add(currentOrder);
        currentOrder = null;
        amountOfOrders ++;
        return this;
    }

    public void payOrder(Order order) {
        orderedOrders.remove(order);
        payedOrders.add(order);
        this.setChanged();
        this.notifyObservers();
    }

    public static class Order implements Serializable{

        private List<OrderItem> orderItems = new ArrayList<>();
        private int orderNumber;

        private Order(int orderNumber){
            this.orderNumber = orderNumber;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public void addOrderItem (String note, MenuItem menuItem){
            this.orderItems.add(new OrderItem(note, menuItem));
        }

        public void removeOrderItem(OrderItem orderItem){
            this.orderItems.remove(orderItem);
        }

        public int getPrice() {
            int prijs = 0;
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
