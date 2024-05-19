package Corte3.weatherviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import  java.io.InputStream;
import  java.net.HttpURLConnection;
import java.net.MalformedURLException;
import  java.net.URL;
import  java.util.HashMap;
import  java.util.List;
import  java.util.Map;

import androidx.annotation.NonNull;

public class WeatherArrayAdapter  extends ArrayAdapter <Weather> {
    public WeatherArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    private static class ViewHolder{
        ImageView condicionImageView;
        TextView dayTextView;
        TextView lowTextVIew;
        TextView hiTextView;
        TextView humidityTextView;
    }
    private Map<String, Bitmap> bitmaps = new HashMap<>();

    public WeatherArrayAdapter(Context context, List<Weather> forecast){
        super(context, -1, forecast);
    }

    @SuppressLint("StringFormatInvalid")
    public View getView(int position, View convertView, ViewGroup parent){
        Weather day = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView=
                    inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.condicionImageView=
                    (ImageView) convertView.findViewById(R.id.condicionImageView);
            viewHolder.dayTextView=
                    (TextView) convertView.findViewById(R.id.dayTextView);
            viewHolder.lowTextVIew=
                    (TextView) convertView.findViewById(R.id.lowTextView);
            viewHolder.hiTextView=
                    (TextView) convertView.findViewById(R.id.hiTextView);
            viewHolder.humidityTextView=
                    (TextView) convertView.findViewById(R.id.humidityTextView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if ( bitmaps.containsKey(day.iconURL)){
            viewHolder.condicionImageView.setImageBitmap(
                    bitmaps.get(day.iconURL));
        }
        else {
            new LoadImageTask(viewHolder.condicionImageView).execute(day.iconURL);
        }

        Context context= getContext();
        viewHolder.dayTextView.setText(context.getString(
                R.string.Descripcion_dia, day.dayOfWeek, day.description));
        viewHolder.lowTextVIew.setText(
                context.getString(R.string.baja_temp, day.minTemp));
        viewHolder.lowTextVIew.setText(
                context.getString(R.string.alta_temp, day.maxTemp));
        viewHolder.lowTextVIew.setText(
                context.getString(R.string.Humedad, day.humidity));

        return convertView;
    }
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap>{

        private ImageView imageView;
        public LoadImageTask(ImageView condicionImageView) {
           this.imageView = condicionImageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                try ( InputStream inputStream = connection.getInputStream()){
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0], bitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
               e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return bitmap;
        }

        protected void onpostExcute(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
        }
    }
}
