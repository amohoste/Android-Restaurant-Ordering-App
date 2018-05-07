package com.example.aggoetey.myapplication.model;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.aggoetey.myapplication.Listener;
import com.example.aggoetey.myapplication.Model;
import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.ServerConnectionFailure;
import com.example.aggoetey.myapplication.menu.fragments.MenuFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    public void commitOrder(final Order order, final MenuFragment menuFragment) {
        final MenuInfo menuInfo = menuFragment.getMenuInfo();

        // Disable order button
        menuFragment.getActivity().findViewById(R.id.menu_view_login_order_button).setEnabled(false);

        final Toast try_toast = Toast.makeText(menuFragment.getContext(), menuFragment.getResources()
                .getString(R.string.order_send_try), Toast.LENGTH_LONG);
        try_toast.show();

        final DocumentReference mDocRef = FirebaseFirestore.getInstance().document("places/"
                .concat(menuInfo.getRestaurant().getGooglePlaceId()).concat("/tables/")
                .concat(menuInfo.getTableID()));

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve tab array
                ArrayList<Object> currentOrders;
                if (documentSnapshot.exists() && documentSnapshot.get("ordered") != null ) {
                    currentOrders = (ArrayList<Object>) documentSnapshot.get("ordered");
                } else {
                    currentOrders = new ArrayList<>();
                }

                // Create new order entry
                for (Tab.Order.OrderItem item: menuInfo.getCurrentOrder().getOrderItems()) {
                    HashMap<String, Object> newEntry = new HashMap<>();
                    newEntry.put("itemID", item.getMenuItem().id);
                    newEntry.put("item", item.getMenuItem().title);
                    newEntry.put("price", Double.toString(item.getMenuItem().price));
                    newEntry.put("category", item.getMenuItem().category);
                    newEntry.put("note", item.getNote());
                    currentOrders.add(newEntry);
                }

                // Upload to FireStore
                mDocRef.update("ordered", currentOrders).addOnSuccessListener(new OnSuccessListener<Void>() {
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
