package com.eazydineapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazydineapp.R;
import com.eazydineapp.adapter.HistoryAdapter;
import com.eazydineapp.adapter.OrdersAdapter;
import com.eazydineapp.backend.service.api.OrderService;
import com.eazydineapp.backend.service.impl.OrderServiceImpl;
import com.eazydineapp.backend.ui.api.UIOrderService;
import com.eazydineapp.backend.util.AndroidStoragePrefUtil;
import com.eazydineapp.backend.vo.Order;
import com.eazydineapp.backend.vo.OrderStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Harini on 30-03-2019.
 */

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerOrders;
    private HistoryAdapter historyAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        historyAdapter = new HistoryAdapter(getContext());
        loadUserOrderHistory();
        return view;
    }

    private void loadUserOrderHistory() {
        String arg = null;
        if(null != getArguments()){
            arg = getArguments().getString("orderTrackerRestaurantId");
        }

        final String restaurantId = arg;
        AndroidStoragePrefUtil storagePrefUtil = new AndroidStoragePrefUtil();
        String userId = storagePrefUtil.getRegisteredUser(this);
        OrderService orderService = new OrderServiceImpl();
        orderService.getOrderByUser(userId, new UIOrderService() {
            @Override
            public void displayAllOrders(List<Order> orders) {
                if(restaurantId == null || restaurantId.isEmpty()) {
                    historyAdapter.setOrders(orders);
                }else {
                    List<Order> dbOrders = new ArrayList<>();
                    for(Order order : orders) {
                        if(order.getRestaurantId().equals(restaurantId)) {
                            dbOrders.add(order);
                        }
                        historyAdapter.setOrders(dbOrders);
                    }
                }
            }

            @Override
            public void displayOrder(Order order) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupOrdersRecycler();
    }

    private void setupOrdersRecycler() {
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOrders.setAdapter(historyAdapter);
    }
}
