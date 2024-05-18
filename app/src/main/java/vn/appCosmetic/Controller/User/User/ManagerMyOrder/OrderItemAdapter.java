package vn.appCosmetic.Controller.User.User.ManagerMyOrder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.appCosmetic.Model.Order;
import vn.appCosmetic.R;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{

    private Context context;
    private List<Order> orderList;
    private LayoutInflater layoutInflater;

    public OrderItemAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("Mã đơn hàng: "+ order.getId());

        String localDateTime = order.getOrderDate().toString();
        // định dạng lại ngày tháng năm
        String[] dateTime = localDateTime.split("T");
        String[] date = dateTime[0].split("-");
        String[] time = dateTime[1].split(":");
        String orderDate = date[2] + "/" + date[1] + "/" + date[0] + " " + time[0] + ":" + time[1];

        holder.txtOrderDate.setText("Ngày đặt: "+ orderDate);

        if(order.isStatus()){
            holder.txtStatusOrder.setText("Trạng thái: Đã thanh toán");
        }
        else {
            holder.txtStatusOrder.setText("Trạng thái: Chưa thanh toán");
        }

        holder.txtLinkSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeeDetailOrderMainActivity.class);
                intent.putExtra("orderId", order.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtLinkSeeDetail,txtOrderDate,txtStatusOrder;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLinkSeeDetail = itemView.findViewById(R.id.txtLinkSeeDetails);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtStatusOrder = itemView.findViewById(R.id.txtStatusOrder);
            txtOrderId = itemView.findViewById(R.id.textViewOrderID);
        }
    }

}
