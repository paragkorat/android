package com.example.hp.json2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 27-02-2018.
 */

class CustomAdapter extends BaseAdapter{

    Button btn,  usub;
    Context context;
    ArrayList<Model> arrayList;

    public CustomAdapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Model s= arrayList.get(i);
        final LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.activity_custom_adapter,null);
        TextView no= view.findViewById(R.id.ids);
        no.setText(s.getNo());
        TextView name= view.findViewById(R.id.caname);
        name.setText(s.getName());
        TextView email = view.findViewById(R.id.caemail);
        email.setText(s.getEmail());

        Button delete= (Button) view.findViewById(R.id.delete);
        Button update= (Button) view.findViewById(R.id.update);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DeleteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("no",Integer.parseInt(s.getNo()));
                intent.putExtra("name",s.getName());
                intent.putExtra("email",s.getEmail());
                v.getContext().startActivity(intent);
            }
       });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,UpdateActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("no",Integer.parseInt(s.getNo()));

                v.getContext().startActivity(i);

            }
       });

        return view;
    }
}
