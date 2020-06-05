package com.vx.dyvide.controller.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vx.dyvide.R;
import com.vx.dyvide.controller.adapters.VehicleAdapter;
import com.vx.dyvide.controller.callbacks.DialogCallback;
import com.vx.dyvide.controller.callbacks.VehicleCallback;
import com.vx.dyvide.controller.dialogs.OptionDialog;
import com.vx.dyvide.model.DB.DB;
import com.vx.dyvide.model.DB.ObjectBox;
import com.vx.dyvide.model.DB.SavedConfig;
import com.vx.dyvide.model.Vehicle;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.ArrayList;

import io.objectbox.android.AndroidObjectBrowser;

public class SettingsActivity extends AppCompatActivity implements VehicleCallback, DialogCallback {

    private ImageButton atras;
    private RecyclerView allCars;
    private ArrayList<Vehicle> vehiculoh;
    private int currentVehicle;
    private LinearLayout newCar;
    private LinearLayout carInfo;
    private OptionDialog dialog;


    //-----------------------------------------NEW CAR---------------------------------------------------
    private Button save;
    private Button cancel; //FALTA FER
    private MultiStateToggleButton fuelType;
    private ImageButton chooseCar;
    private ImageButton chooseMoto;
    private EditText vehicleName;
    private EditText consum;
    private int selectedFuel = -3;
    private TextView type;
    private TextView carNameFuel;
    //---------------------------------------------------------------------------------------------------
    //-----------------------------------------INFO CAR--------------------------------------------------
    private TextView name;
    private EditText editName;
    private EditText editConsu;
    private Button update;
    private Button delete;
    //---------------------------------------------------------------------------------------------------


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fuelType = (MultiStateToggleButton) this.findViewById(R.id.mstb_multi_id);
        fuelType.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                selectedFuel = value;
            }
        });
        fuelType.setColorRes(R.color.color_pressed, R.color.color_released);
        carNameFuel = findViewById(R.id.carNameFuel);
        editName = findViewById(R.id.editCarName);
        editConsu = findViewById(R.id.editCarConsu);
        update = findViewById(R.id.update);
        update.setEnabled(true);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehiculoh.get(currentVehicle).setName(editName.getText().toString());
                vehiculoh.get(currentVehicle).setConsum(Float.parseFloat(editConsu.getText().toString()));
                ObjectBox.get().boxFor(SavedConfig.class).get(1).saveVehicles(vehiculoh);
            }
        });

        cancel = findViewById(R.id.cancel);
        cancel.setEnabled(true);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehiclePressed(DB.getVehicles().get(currentVehicle), currentVehicle);
            }
        });
        delete = findViewById(R.id.delete);
        delete.setEnabled(true);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new OptionDialog(SettingsActivity.this);
                dialog.showConfirmationDialog("Are you sure you want to delete this vehicle?");
            }
        });
        newCar = findViewById(R.id.newCar);
        carInfo = findViewById(R.id.carInfo);
        name = findViewById(R.id.currentName);
        type = findViewById(R.id.type);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (everythingDone()) {
                    DB.addVehicle(new Vehicle(whatType(), vehicleName.getText().toString(), Float.parseFloat(consum.getText().toString()), selectedFuel));
                    currentVehicle = DB.getVehicles().size()-2;
                    reload();
                } else {
                    Toast toast = Toast.makeText(SettingsActivity.this, "Please fill everything", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#7ED31F"), PorterDuff.Mode.SRC_IN);
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.WHITE);
                    text.setTypeface(text.getTypeface(), Typeface.BOLD);
                    toast.show();
                }

            }
        });
        chooseCar = findViewById(R.id.triaCoche);
        chooseCar.setEnabled(true);
        chooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.setText("Car");
            }
        });
        chooseMoto = findViewById(R.id.triaMoto);
        chooseMoto.setEnabled(true);
        chooseMoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.setText("Motorcycle");
            }
        });
        vehicleName = findViewById(R.id.addCarName);
        vehicleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name.setText(vehicleName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        consum = findViewById(R.id.addCarConsu);
        atras = findViewById(R.id.back);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
                c.setSelectedVehicle(currentVehicle);
                ObjectBox.get().boxFor(SavedConfig.class).put(c);
                finish();
            }
        });
        vehiculoh = DB.getVehicles();
        allCars = (RecyclerView) findViewById(R.id.carReycle);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        VehicleAdapter adapter = new VehicleAdapter(this, vehiculoh, this);
        allCars.setLayoutManager(manager2);
        allCars.setAdapter(adapter);

        if (DB.hasCars()) {
            newCar.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);

            carInfo.setVisibility(View.VISIBLE);
            if(ObjectBox.get().boxFor(SavedConfig.class).get(1).getSelectedVehicle() == -3){
                currentVehicle = 0;
                SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
                c.setSelectedVehicle(currentVehicle);
                ObjectBox.get().boxFor(SavedConfig.class).put(c);
            }else{
                currentVehicle =  ObjectBox.get().boxFor(SavedConfig.class).get(1).getSelectedVehicle();
            }
            vehiclePressed(DB.getVehicles().get(currentVehicle), currentVehicle);
        }else{
            cancel.setVisibility(View.GONE);
            newCar.setVisibility(View.VISIBLE);
            carInfo.setVisibility(View.GONE);
            resetNewCar();
        }

    }

    private void resetNewCar(){
        consum.setText("");
        vehicleName.setText("");
        fuelType.setValue(-1);
        type.setText("");
        name.setText("");
    }

    private void reload() {
        vehiculoh = DB.getVehicles();
        VehicleAdapter adapter = new VehicleAdapter(this, vehiculoh, this);
        allCars.setAdapter(adapter);
        if (DB.hasCars()) {
            newCar.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            carInfo.setVisibility(View.VISIBLE);
            vehiclePressed(DB.getVehicles().get(currentVehicle), currentVehicle);
        } else {
            newCar.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
            carInfo.setVisibility(View.GONE);
            resetNewCar();
        }
    }


    private boolean everythingDone() {
        boolean ok = true;
        if (type.getText().toString().equals("")) {
            ok = false;
        } else if (vehicleName.getText().toString().equals("")) {
            ok = false;
        } else if (consum.getText().toString().equals("")) {
            ok = false;
        } else if (selectedFuel == -3) {
            ok = false;
        }
        return ok;
    }

    @Override
    public void createNewVehicle() {
        newCar.setVisibility(View.VISIBLE);
        carInfo.setVisibility(View.GONE);
        resetNewCar();
    }

    private int whatType() {
        if (type.getText().toString().equals("Motorcycle")) {
            return 2;
        } else if (type.getText().toString().equals("Car")) {
            return 1;
        } else {
            return 0;
        }
    }

    private String fuelType(Vehicle v) {
        String s = "";

        if (v.getFuel() == 0) {
            s = "Diesel";
        } else if (v.getFuel() == 1) {
            s = "Gasoline";
        } else if (v.getFuel() == 2) {
            s = "Electric";
        }

        return s;
    }

    private String vehicleType(Vehicle v) {
        String s = "";

        if (v.getType() == 1) {
            s = "Car";
        } else if (v.getType() == 2) {
            s = "Motorcycle";
        }

        return s;
    }

    @Override
    public void vehiclePressed(Vehicle vehicle, int position) {
        currentVehicle = position;
        SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
        c.setSelectedVehicle(currentVehicle);
        ObjectBox.get().boxFor(SavedConfig.class).put(c);
        newCar.setVisibility(View.GONE);
        carInfo.setVisibility(View.VISIBLE);
        carNameFuel.setText(new StringBuilder().append(fuelType(vehicle)).append(" ").append(vehicleType(vehicle)).toString());
        editName.setText(vehicle.getName());
        editConsu.setText(String.format("%s", vehicle.getConsum()));
    }

    @Override
    public void onAccept() {
        dialog.cancelDialog();
        SavedConfig c = ObjectBox.get().boxFor(SavedConfig.class).get(1);
        ArrayList<Vehicle> v = c.retrieveVehicles();
        v.remove(currentVehicle+1);
        c.saveVehicles(v);
        ObjectBox.get().boxFor(SavedConfig.class).put(c);
        currentVehicle=0;
        reload();
    }


    @Override
    public void onCancel() {
        dialog.cancelDialog();
    }
}