package com.intuition.ivepos;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 17-Apr-18.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<EditModel> editModelArrayList;


    public CustomAdapter(Context ctx, ArrayList<EditModel> editModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.MyViewHolder holder, final int position) {


        holder.editText1.setText(editModelArrayList.get(position).getEditTextValue1());
        holder.editText3.setText(editModelArrayList.get(position).getEditTextValue3());
        Log.d("print","yes");

    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        protected EditText editText1, editText2, editText3;

        public MyViewHolder(View itemView) {
            super(itemView);

            final EditModel editModel = new EditModel();

            editText1 = (EditText) itemView.findViewById(R.id.editText1);
            editText2 = (EditText) itemView.findViewById(R.id.editText2);
            editText3 = (EditText) itemView.findViewById(R.id.editText3);

            editText3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

//                      editModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());

                    if (charSequence.length() == 0 || editText1.getText().toString().equals("") || editText2.getText().toString().equals("") || editText3.getText().toString().equals("")) {
//                        EditModel editModel = new EditModel();
//                    editModel.setEditTextValue("1");
                        editModelArrayList.remove(editModel);
                    } else if (charSequence.length() > 0) {
//                        EditModel editModel = new EditModel();
//                    editModel.setEditTextValue("1");
                        editModelArrayList.add(editModel);
                        editModelArrayList.get(getAdapterPosition()).setEditTextValue1(editText1.getText().toString());
                        editModelArrayList.get(getAdapterPosition()).setEditTextValue3(editText3.getText().toString());
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

    }
}
