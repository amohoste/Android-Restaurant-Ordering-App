package com.example.aggoetey.myapplication.model;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.aggoetey.myapplication.Model;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.ServerConnectionFailure;
import com.example.aggoetey.myapplication.menu.fragments.MenuFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aggoetey on 3/20/18.
 * <p>
 * Een tab is een lijst met orders die nog betaald moeten worden door een bepaalde tafel.
 */

public class Tab extends Model implements Serializable {

    private static final Tab ourInstance = new Tab();
    private static final String DEBUG_TAG = "TAB_DEBUG";

    private List<Order> payedOrders = new ArrayList<>();
    private List<Order> orderedOrders = new ArrayList<>();
    private List<Order> receivedOrders = new ArrayList<>();
    private int amountOfOrders = 0;

    private Restaurant restaurant;
    private Table table;

    public void setOrderedOrders(List<Order> orderedOrders) {
        this.orderedOrders = orderedOrders;
    }

    private enum Collection {
        ORDERED("ordered"),
        PAYED("payed"),
        RECEIVED("ordered");

        public final String collection;

        Collection(String collection) {
            this.collection = collection;
        }

    }

    public List<Order> getPayedOrders() {
        return payedOrders;
    }

    public List<Order> getOrderedOrders() {
        return orderedOrders;
    }

    public void loadOrderedOrders() {
        final CollectionReference ordered = getTableCollection(Collection.ORDERED);
        List<Order> orderedOrders = new ArrayList<>();
        ordered.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot fireBaseOrderDocument : queryDocumentSnapshots) {
                    Order order = new Order();
                    List<HashMap<String, String>> firebaseOrder = (List<HashMap<String, String>>) fireBaseOrderDocument.getData().get("orders");
                    for (HashMap<String, String> orderMap : firebaseOrder) {
                        Order.OrderItem orderItem = new Order.OrderItem(orderMap.get("note"), new MenuItem(
                                orderMap.get("item"), Double.parseDouble(orderMap.get("price")), orderMap.get("description"), orderMap.get("category")
                        ));
                        order.addOrderItem(orderItem);
                    }
                    orderedOrders.add(order);
                }
                Tab.getInstance().setOrderedOrders(orderedOrders);
                fireInvalidationEvent();
            }
        });
    }

    public List<Order> getReceivedOrders() {
        return receivedOrders;
    }

    private CollectionReference getTableCollection(Collection c) {
        return FirebaseFirestore.getInstance().collection("places").document(restaurant.getGooglePlaceId())
                .collection("tables").document(table.getTableId()).collection(c.collection);
    }


    private Tab() {
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
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
    public void commitOrder(final Order order, final MenuFragment menuFragment) {
        final MenuInfo menuInfo = menuFragment.getMenuInfo();


        // Disable order button
        menuFragment.getActivity().findViewById(R.id.menu_view_login_order_button).setEnabled(false);

        final Toast try_toast = Toast.makeText(menuFragment.getContext(), menuFragment.getResources()
                .getString(R.string.order_send_try), Toast.LENGTH_LONG);
        try_toast.show();

        final DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("places")
                .document(menuInfo.getRestaurant().getGooglePlaceId()).collection("tables")
                .document(menuInfo.getTableID()).collection("ordered").document();

        mDocRef.set(new HashMap<>());

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve tab array
                ArrayList<Object> currentOrders;
                currentOrders = new ArrayList<>();

                // Create new order entry
                for (Order.OrderItem item : menuInfo.getCurrentOrder().getOrderItems()) {
                    HashMap<String, Object> newEntry = new HashMap<>();
                    newEntry.put("itemID", item.getMenuItem().id);
                    newEntry.put("item", item.getMenuItem().title);
                    newEntry.put("description", item.getMenuItem().description);
                    newEntry.put("price", Double.toString(item.getMenuItem().price));
                    newEntry.put("category", item.getMenuItem().category);
                    newEntry.put("note", item.getNote());
                    newEntry.put("time", System.currentTimeMillis() / 1000);
                    currentOrders.add(newEntry);
                }

                // Upload to FireStore
                mDocRef.update("orders", currentOrders).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        menuFragment.getActivity().findViewById(R.id.menu_view_login_order_button).setEnabled(true);

                        try_toast.cancel();
                        Toast.makeText(menuFragment.getContext(), menuFragment.getResources()
                                .getString(R.string.order_send_success), Toast.LENGTH_SHORT)
                                .show();

                        menuInfo.orderCommitted();

                        menuFragment.getActivity().findViewById(R.id.action_pay).performClick();
                        orderedOrders.add(order);
                        order.setOrderNumber(amountOfOrders);
                        amountOfOrders++;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        menuFragment.getActivity().findViewById(R.id.menu_view_login_order_button).setEnabled(true);

                        try_toast.cancel();
                        Toast.makeText(menuFragment.getContext(), menuFragment.getResources()
                                .getString(R.string.order_send_failure), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }).addOnFailureListener(new ServerConnectionFailure(menuFragment, try_toast));

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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public static class Order extends Model implements Serializable {

        private List<OrderItem> orderItems = new ArrayList<>();
        private int orderNumber;
        private Date time;

        private Order() {
            time = new Date();
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
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
         * Zet een lijst van orderItems om in een gegroepeerde lijst van orders
         *
         * @return
         */
        public static List<List<OrderItem>> groupOrders(Order order) {
            if (order.getOrderItems().size() == 0) return new ArrayList<>();

            List<List<OrderItem>> orderItems = new ArrayList<>();

            Collections.sort(order.orderItems);

            OrderItem last = order.orderItems.get(0);
            List<OrderItem> items = new ArrayList<>();

            for (OrderItem orderItem : order.orderItems) {
                if (last.compareTo(orderItem) != 0) {
                    // nieuw item, nieuwe lijst
                    orderItems.add(items);
                    items = new ArrayList<>();
                }
                items.add(orderItem);
                last = orderItem;
            }
            orderItems.add(items);

            return orderItems;
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

        public static class OrderItem implements Serializable, Comparable<OrderItem> {
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

            @Override
            public int compareTo(@NonNull OrderItem orderItem) {
                return menuItem.title.compareTo(orderItem.menuItem.title);
            }
        }


    }
}
