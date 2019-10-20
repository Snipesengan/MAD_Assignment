package curtin.edu.au.mad_assignment.controller;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Settings;
import curtin.edu.au.mad_assignment.model.Structure;

public class MapFragment extends Fragment implements Serializable {

    private MapAdapter mapAdapter;
    private GameData gameData;
    private MapElement selectedMapElement;
    private int prevSelectedPosition;
    private int selectedPosition;

    private MapFragment.OnMapElementSelectedListener callback;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    public MapElement getSelectedMapElement(){
        return selectedMapElement;
    }

    public void deselectMapElement(){
        selectedMapElement = null;
        mapAdapter.notifyItemChanged(selectedPosition);
        selectedPosition = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameData = GameData.getInstance(getActivity());
        selectedMapElement = null;
        prevSelectedPosition = -1;
        selectedPosition = -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        RecyclerView rv = view.findViewById(R.id.mapRecyclerView);


        // Specifying how RV should be laid out
        Settings settings = gameData.getSettings();
        rv.setLayoutManager(new GridLayoutManager(
                getActivity(),
                settings.getMapHeight(),
                GridLayoutManager.HORIZONTAL,
                false));

        // mapAdapter requires MapElements[][] to inflate each grid cell with structureImgLayer/terrain img.
        mapAdapter = new MapAdapter();
        rv.setAdapter(mapAdapter);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapElementSelectedListener) {
            callback = (MapFragment.OnMapElementSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapElementSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface OnMapElementSelectedListener {

        void onMapElementSelected(MapElement mapElement, int xPos, int yPos);
    }

    private class MapAdapter extends RecyclerView.Adapter<GridCellVH> implements Serializable
    {
        @Override
        public GridCellVH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new GridCellVH(li,parent);
        }

        @Override
        public void onBindViewHolder(GridCellVH vh, int position) {
            int height = gameData.getSettings().getMapHeight();
            int row = position % height;
            int col = position / height;
            vh.bind(gameData.getMapElement(col,row), position);
        }

        @Override
        public int getItemCount() {
            return gameData.getSettings().getMapHeight() * gameData.getSettings().getMapWidth();
        }
    }


    private class GridCellVH extends RecyclerView.ViewHolder implements Serializable
    {
        private ImageView structureImgLayer;
        private ImageView topLeft;
        private ImageView botLeft;
        private ImageView topRight;
        private ImageView botRight;

        private MapElement bindMapElement;
        private int vhPosition; //Position of this VH on the map
        private int xPos, yPos; //xPosition and yPosition of this VH on the map

        public GridCellVH(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.grid_cell, parent, false));
            int size = parent.getMeasuredHeight() / gameData.getSettings().getMapHeight() + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            topLeft = itemView.findViewById(R.id.imageView);
            topRight = itemView.findViewById(R.id.imageView2);
            botLeft = itemView.findViewById(R.id.imageView3);
            botRight = itemView.findViewById(R.id.imageView4);
            structureImgLayer = itemView.findViewById(R.id.imageView5);



            structureImgLayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prevSelectedPosition = selectedPosition;
                    selectedPosition = vhPosition;

                    flipSelected();
                    mapAdapter.notifyItemChanged(selectedPosition);
                    if(prevSelectedPosition >= 0)
                    {
                        mapAdapter.notifyItemChanged(prevSelectedPosition);
                    }

                    if(selectedMapElement != null)
                    {
                        callback.onMapElementSelected(selectedMapElement,xPos,yPos);
                        mapAdapter.notifyItemChanged(selectedPosition);
                    }
                }
            });
        }

        public void bind(MapElement mapElement,int position) {

            topLeft.setImageResource(mapElement.getNorthWest());
            topRight.setImageResource(mapElement.getNorthEast());
            botLeft.setImageResource(mapElement.getSouthWest());
            botRight.setImageResource(mapElement.getSouthEast());

            // Remember references to mapElement and vhPosition
            this.vhPosition = position;
            bindMapElement = mapElement;
            yPos = position % gameData.getSettings().getMapHeight();
            xPos = position / gameData.getSettings().getMapHeight();

            // Set image resources for structure layer
            if (mapElement.getStructure() != null) {
                structureImgLayer.setImageResource(mapElement.getStructure().getDrawableId());
                structureImgLayer.setAlpha(255);
            }
            else
            {
                // Grids that gets recycled still possibly contained images of a structure
                // therefore they must be set to transparent if a structure do not exists at
                // this vhPosition.
                structureImgLayer.setAlpha(0);
            }

            // Make any item that is not selected transparent
            if(position == selectedPosition && selectedMapElement != null)
            {
                topLeft.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY );
                topRight.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY );
                botLeft.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY );
                botRight.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY );
                structureImgLayer.setColorFilter(0x76ffffff, PorterDuff.Mode.MULTIPLY );
            }
            else
            {
                topLeft.clearColorFilter();
                topRight.clearColorFilter();
                botLeft.clearColorFilter();
                botRight.clearColorFilter();
                structureImgLayer.clearColorFilter();
            }
        }


        /**
         *  Toggle selected MapElement if the user taps on the same item twice
         */
        private void flipSelected()
        {
            // LOGIC FOR SELECTING/DESELECTING A MAP ELEMENT
            if(selectedPosition == prevSelectedPosition)
            {
                selectedMapElement = (selectedMapElement == null) ? bindMapElement : null;
            }
            else
            {
                selectedMapElement = bindMapElement;
            }
        }

    }

}
