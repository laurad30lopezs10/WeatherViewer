package Corte3.weatherviewer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.NumberFormat;
import java.util.Locale;

public class Weather {
    public String dayOfWeek;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    public Weather(String dayOfWeek, String minTemp, String maxTemp, String humidity,
                   String description, String iconURL) {
        this.dayOfWeek = dayOfWeek;
        this.minTemp = formatTemperature(minTemp);
        this.maxTemp = formatTemperature(maxTemp);
        this.humidity = formatPercentage(humidity);
        this.description = description;
        this.iconURL = iconURL;


        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dayOfWeek));
            this.dayOfWeek = sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatTemperature(String temp) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(0);
        return nf.format(Double.parseDouble(temp)) + "\u00B0F";
    }

    private String formatPercentage(String percent) {
        double value = Double.parseDouble(percent) / 100.0;
        NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
        return nf.format(value);
    }

    private static String convertTimeStampToDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);
        TimeZone tz = TimeZone.getDefault();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        sdf.setTimeZone(tz);

        String dayName = sdf.format(calendar.getTime());
        return dayName;
    }

}

