package com.vx.dyvide.model.DB;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vx.dyvide.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DB {

    public static Toast toast;

    public static boolean hasConfig(){
        List<SavedConfig> list = ObjectBox.get().boxFor(SavedConfig.class).query().equal(SavedConfig_.id, 1).build().find();
        return list.size() ==1;
    }

    public static void addVehicle(Vehicle v){
        SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
        ArrayList<Vehicle> veh = c.retrieveVehicles();
        veh.add(v);
        c.saveVehicles(veh);
        ObjectBox.get().boxFor(SavedConfig.class).put(c);
    }

    public static boolean hasCars(){
        return ObjectBox.get().boxFor(SavedConfig.class).get(1).retrieveVehicles().size()>1;
    }

    public static void makeCustomToast(Context c, String ok){
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(c, ok, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.parseColor("#7ED31F"), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 570);
        toast.show();
    }

    public static boolean noInternet(Context c){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return !(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static Vehicle getCurrentVehicle(){
        ArrayList<Vehicle> cars = getVehicles();
        return cars.get(ObjectBox.get().boxFor(SavedConfig.class).get(1).getSelectedVehicle());
    }

    public static ArrayList<Vehicle> getVehicles(){
        ArrayList<Vehicle> cars = ObjectBox.get().boxFor(SavedConfig.class).get(1).retrieveVehicles();
        Vehicle v = cars.get(0);
        cars.remove(0);
        cars.add(v);
        return cars;
    }

    public static boolean isLocationEnabled(Context context){
        int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF);
        final boolean enabled = (mode != android.provider.Settings.Secure.LOCATION_MODE_OFF);
        return enabled;
    }

    public static void createConfig(){
        SavedConfig c = new SavedConfig();
        c.setId(1);
        c.setOnboard(0);
        c.setLan("EN");
        c.selectedVehicle = -3;
        Vehicle v = new Vehicle(-5, "Add", 0, -3);
        ArrayList<Vehicle> array = new ArrayList<>();
        array.add(v);
        c.saveVehicles(array);
        ObjectBox.get().boxFor(SavedConfig.class).put(c);
    }
    public static void setAppLocale(Context c, String localeCode){
        Resources resources = c.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    public static boolean isOnboard() {
        int on = ObjectBox.get().boxFor(SavedConfig.class).get(1).isOnboard;
        return on==1;
    }

    public static void setLanguage(Context x) {
        String lan = "";
        if(hasConfig()){
            SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
            lan = c.lan;
        }else{
            lan = "en";
        }
        DB.setAppLocale(x, lan);
    }

    public static int getRandomNum(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
