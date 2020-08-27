package com.haeseong.izobonga_custom.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haeseong.izobonga_custom.R;
import com.haeseong.izobonga_custom.models.Customer;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.MyViewHolder> {
    private ArrayList<Customer> customers;
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;
    private Context context;
    public CallAdapter(Context context, ArrayList<Customer> customers) {
        this.customers = customers;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClickCall(View v, int position);
        void onItemClickDelete(View v, int position);
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate item_layout
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, null);
        MyViewHolder vh = new MyViewHolder(itemLayoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvTicket.setText(String.valueOf(customers.get(position).getTicket()));
        holder.tvPhone.setText(customers.get(position).getPhone());
        holder.tvPersonnel.setText(String.valueOf(customers.get(position).getPersonnel()));
        holder.tvChild.setText(String.valueOf(customers.get(position).getChild()));
        holder.tvIndex.setText(String.valueOf(position+1));
        if (!customers.get(position).isTable6()){
            holder.tvTable.setText(context.getString(R.string.table4));
        }else{
            holder.tvTable.setText(context.getString(R.string.table6));
            holder.tvTable.setTextColor(context.getColor(R.color.colorBlue));
        }

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClickDelete(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (customers != null)
            return customers.size();
        else
            return 0;
    }

    public void addItem(Customer customer) {
        customers.add(customer);
    }

    public void removeItem(int position) {
        customers.remove(position);
    }

    // inner static class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTicket;
        TextView tvPhone;
        TextView tvPersonnel;
        TextView tvChild;
        TextView tvIndex;
        Button btCall;
        Button btDelete;
        TextView tvTable;
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvTicket = itemLayoutView.findViewById(R.id.item_customer_ticket);
            tvPhone = itemLayoutView.findViewById(R.id.item_customer_phone);
            tvPersonnel = itemLayoutView.findViewById(R.id.item_customer_personnel);
            tvChild = itemLayoutView.findViewById(R.id.item_customer_child);
            btCall = itemLayoutView.findViewById(R.id.item_customer_call_button);
            btDelete = itemLayoutView.findViewById(R.id.item_customer_delete_button);
            tvTable = itemLayoutView.findViewById(R.id.item_customer_table);
            tvIndex = itemLayoutView.findViewById(R.id.item_customer_index);
            btCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClickCall(view, pos);
                        }
                    }
                }
            });


        }
    }
}


