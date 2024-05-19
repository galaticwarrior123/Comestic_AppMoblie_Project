package vn.appCosmetic.Controller.Admin.ManageOrder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.appCosmetic.Model.Order;
import vn.appCosmetic.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private List<Order> orderList=new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_manager_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtOrderId.setText("Order ID: " + order.getId());
        holder.txtCartName.setText("Cart Name: " + order.getCart().getName());

        String localDateTime = order.getOrderDate().toString();
        // định dạng lại ngày tháng năm
        String[] dateTime = localDateTime.split("T");
        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");
        String orderDate = date[2] + "/" + date[1] + "/" + date[0] + " " + time[0] + ":" + time[1];

        // convert orderDate to a more readable format
        holder.txtOrderDate.setText("Order Date: " + orderDate);

        if(order.isStatus()){
            holder.txtOrderStatus.setText("Status: Paid");
        }
        else {
            holder.txtOrderStatus.setText("Status: Unpaid");
        }

        holder.txtViewSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderSeeDetailActivity.class);
                intent.putExtra("orderID", order.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId,txtCartName, txtOrderDate, txtOrderStatus, txtViewSeeDetail;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderID);
            txtOrderDate = itemView.findViewById(R.id.txtViewOrderDate);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtCartName = itemView.findViewById(R.id.textViewNameCart);
            txtViewSeeDetail = itemView.findViewById(R.id.txtViewLinkSeeDetails);
        }
    }
}
