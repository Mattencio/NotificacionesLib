package com.pagatodo.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pagatodo.notifications.databinding.LibNotificacionItem2Binding;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorMensajes extends RecyclerView.Adapter<AdaptadorMensajes.MensajesVieHolder> {

    private final List<Notificacion> notificaciones = new ArrayList<>();
    private Context context;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final Comparator DATE_COMPARATOR = new Comparator<Notificacion>() {
        @Override
        public int compare(final Notificacion notificacion, final Notificacion noti) {

            Calendar calNoti = Calendar.getInstance();
            Calendar calNotificacion = Calendar.getInstance();

            try {
                calNoti.setTime(dateFormat.parse(noti.getFechaEnvio()));
                calNotificacion.setTime(dateFormat.parse(notificacion.getFechaEnvio()));

                return calNoti.compareTo(calNotificacion);
            } catch (ParseException e) {
                Log.e("EXCEPTION",e.getMessage());
                return -1;
            }
        }
    };

    @NonNull
    @Override
    public MensajesVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LibNotificacionItem2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.lib_notificacion_item_2, parent, false);
        this.context = parent.getContext();
        return new MensajesVieHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesVieHolder holder, int position) {
        final Notificacion notificacion = notificaciones.get(position);
        holder.binding.tvTitlePromocion.setText(notificacion.getTitulo());
        holder.binding.tvMensaje.setText(notificacion.getMensaje());
        holder.binding.tvDay.setText(obtenDia(notificacion.getFechaEnvio()));
        holder.binding.tvMthYr.setText(formatearFecha(notificacion.getFechaEnvio()));
    }

    private CharSequence formatearFecha(String campo1) {
        Format fmMonth = new SimpleDateFormat("MMM", Locale.getDefault());
        Format fmYear = new SimpleDateFormat("YY", Locale.getDefault());
        String s;
        try {
            s = fmMonth.format(dateFormat.parse(campo1)).toUpperCase() + " " + fmYear.format(dateFormat.parse(campo1));
        } catch (ParseException e) {
            e.printStackTrace();
            s = "";
        }
        return s;
    }

    private String obtenDia(String campo1) {
        Date date;
        try {
            date = dateFormat.parse(campo1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
            return "00";
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public void add(final Notificacion notificacion) {
        if (!notificaciones.contains(notificacion)) {
            if(notificaciones.size()<10){
            notificaciones.add(notificacion);
            }
            sortNotificaciones();
            notifyItemChanged(notificaciones.indexOf(notificacion));
            notifyDataSetChanged();
        }
    }

    public void update(final Notificacion notificacion) {
        if (notificaciones.contains(notificacion)) {
            final int itemIndex = notificaciones.indexOf(notificacion);
            notificaciones.set(itemIndex, notificacion);
            sortNotificaciones();
            notifyItemChanged(notificaciones.indexOf(notificacion));
            notifyDataSetChanged();
            //notifyItemChanged(itemIndex);
        }
    }

    public void remove(final Notificacion notificacion) {
        if (notificaciones.contains(notificacion)) {
            final int itemIndex = notificaciones.indexOf(notificacion);
            notificaciones.remove(itemIndex);
            notifyItemRemoved(itemIndex);
            notifyDataSetChanged();
        }
    }

    private void sortNotificaciones() {
        Collections.sort(notificaciones, DATE_COMPARATOR);
    }

    class MensajesVieHolder extends RecyclerView.ViewHolder {
        LibNotificacionItem2Binding binding;

        public MensajesVieHolder(final LibNotificacionItem2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
