/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package es.demosix.conexus.Cards;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import es.demosix.conexus.Modelo.Alarm;
import es.demosix.conexus.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        TextView text_time;
        TextView text_device;
        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            text_time = (TextView) v.findViewById(R.id.text_time);
            text_device = (TextView) v.findViewById(R.id.text_device);

        }
    }

    private List<Alarm> mAlarmas;
    private Context mContext;

    CustomAdapter(Context context, List<Alarm> alarmas){
        mAlarmas = alarmas;
        mContext = context;
    }

    private Context getContext(){
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_list_notification, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alarm alarm = mAlarmas.get(position);

        TextView textView = holder.mTextView;
        textView.setText(alarm.getTitulo());
        TextView time_text = holder.text_time;
        time_text.setText(alarm.getHora());
        TextView device_text = holder.text_device;
        device_text.setText(alarm.getDispositivo());
        /*
        * Corregir aqui implementación de la bateria
        * */
    }

    @Override
    public int getItemCount() {
        return mAlarmas.size();
    }

}
