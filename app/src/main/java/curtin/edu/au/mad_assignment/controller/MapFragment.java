package curtin.edu.au.mad_assignment.controller;

import android.app.SharedElementCallback;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import curtin.edu.au.mad_assignment.model.MapData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Settings;
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements Serializable {

    private MapAdapter mapAdapter;
    private GameData gameData;
    private StructureData structureData;
    private MapData mapData;

    private SelectorFragment selectorFragment = null;

    public void setSelectorFragment(SelectorFragment sf)
    {
        selectorFragment = sf;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(SelectorFragment sf) {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameData = GameData.getInstance(getActivity());
        structureData = StructureData.getInstance();
        mapData = MapData.getInstance();
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

        // mapAdapter requires MapElements[][] to inflate each grid cell with structureImg/terrain img.
        mapAdapter = new MapAdapter();
        rv.setAdapter(mapAdapter);

        //TODO : Set up event handlers

        return view;

    }

    private class MapAdapter extends RecyclerView.Adapter<GridCellVH> implements Serializable
    {

        ImageView structureImg;

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
        private ImageView structureImg;
        private ImageView topLeft;
        private ImageView botLeft;
        private ImageView topRight;
        private ImageView botRight;

        private MapElement bindMapElement;
        private int position;
        private int xPos, yPos;

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
            structureImg = itemView.findViewById(R.id.imageView5);

            structureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Structure selectedStructure = selectorFragment.getSelectedStructure();
                    // TODO: Implement Game-Logic here
                    if(selectedStructure != null)
                    {
                        /**
                         * Constrains for placing a building
                         *      - Player must be able to afford building
                         *      - Cannot build if there is an existing building
                         *      - Must be built adjacent to a road & buildable terrain
                         */

                        int cost = StructureData.getInstance().getCost(selectedStructure.getLabel(),
                                gameData.getSettings());

                        boolean buildable = gameData.checkBuildable(xPos,yPos);

                        if(gameData.getMoney() > cost && buildable)
                        {
                            bindMapElement.setStructure(selectedStructure);

                            //Update the map, would be more efficient
                            mapAdapter.notifyItemChanged(position);
                        }
                        else
                        {

                        }



                    }
                }
            });
        }

        public void bind(MapElement mapElement,int position) {

            topLeft.setImageResource(mapElement.getNorthWest());
            topRight.setImageResource(mapElement.getNorthEast());
            botLeft.setImageResource(mapElement.getSouthWest());
            botRight.setImageResource(mapElement.getSouthEast());

            // Each ViewHolder needs to store a reference to the mapElement every time it gets bind
            bindMapElement = mapElement;
            this.position = position;
            int height = gameData.getSettings().getMapHeight();
            yPos = position % height;
            xPos = position / height;

            if (mapElement.getStructure() != null) {
                structureImg.setImageResource(mapElement.getStructure().getDrawableId());
                structureImg.setAlpha(255);
            }
            else
            {
                // make it transparent
                structureImg.setAlpha(0);
            }
        }
    }

}
