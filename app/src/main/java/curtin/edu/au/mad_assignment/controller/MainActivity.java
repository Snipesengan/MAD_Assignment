package curtin.edu.au.mad_assignment.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import java.io.Serializable;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.Commercial;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Residential;
import curtin.edu.au.mad_assignment.model.Road;
import curtin.edu.au.mad_assignment.model.Settings;
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameData gameData = GameData.getInstance();
        gameData.setSettings(new Settings());

        // ---- ADDING STRUCTURES MANUALLY TO STRUCTURE DATA ----
        StructureData structureDataInstance = StructureData.getInstance();
        for(int i  = 1; i <= 3; i++){
            String drawableName = "ic_building_residential" + i;
            int id = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            structureDataInstance.addStructure(new Residential(id));
        }

        for(int i  = 1; i <= 5; i++){
            String drawableName = "ic_building_commercial" + i;
            int id = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            structureDataInstance.addStructure(new Commercial(id));
        }

        // ---- INITIALISING MAP ELEMENTS MANUALLY ----
        for(int x = 0; x < gameData.getSettings().getMapWidth(); x++){
            for(int y = 0; y < gameData.getSettings().getMapHeight(); y++){
                Road road = new Road(R.drawable.ic_road_nsew);
                Bitmap img = getBitmapFromVectorDrawable(MainActivity.this,R.drawable.ic_road_nsew);
                MapElement mapElement = new MapElement(road, img,"N/A");
                gameData.addMapElement(x,y,mapElement);
            }
        }

        // For now let's start with the map activity
        startActivity(new Intent(MainActivity.this,MapActivity.class));
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
