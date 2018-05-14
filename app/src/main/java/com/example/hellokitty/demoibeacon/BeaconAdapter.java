package com.example.hellokitty.demoibeacon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.List;



public class BeaconAdapter extends BaseAdapter {
    Context context;
    List<org.altbeacon.beacon.Beacon> l;

    public BeaconAdapter(Context context, List<Beacon> l) {
        this.context = context;
        this.l = l;
    }

    public class ViewHolder {
        TextView khoangcach;
        TextView diachimac;
        TextView uuid;
        TextView textViewten;

    }


    @Override
    public int getCount() {
        return l.size();
    }

    @Override
    public Object getItem(int i) {
        return l.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_beacon, null);
            viewHolder = new ViewHolder();
            viewHolder.khoangcach = view.findViewById(R.id.khoangcach);
            viewHolder.diachimac = view.findViewById(R.id.diachimac);
            viewHolder.uuid = view.findViewById(R.id.uuid);
            viewHolder.textViewten = view.findViewById(R.id.textViewten);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        org.altbeacon.beacon.Beacon beacon = l.get(i);

        viewHolder.khoangcach.setText(String.format("%.2f", beacon.getDistance()) + " m");
        viewHolder.diachimac.setText(beacon.getBluetoothAddress().toString());
        viewHolder.uuid.setText(beacon.getId1().toUuid().toString());
        if (beacon.getId1().toUuid().toString().equals("00000000-4ec9-1001-b000-001c4dcf9430")) {
            viewHolder.textViewten.setText("Hoàng");
        } else if (beacon.getId1().toUuid().toString().equals("57427cda-0000-48df-8ae9-21167167e74d")) {
            viewHolder.textViewten.setText("Cảnh");
        } else {
            viewHolder.textViewten.setText("Chưa đăng kí");
        }
        return view;
    }
}
