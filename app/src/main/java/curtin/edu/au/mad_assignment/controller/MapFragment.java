package curtin.edu.au.mad_assignment.controller;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.Serializable;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapData;
import curtin.edu.au.mad_assignment.model.MapElement;
import curtin.edu.au.mad_assignment.model.Settings;
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


    private OnFragmentInteractionListener mListener;

    private MapAdapter mapAdapter;
    private GameData gameData;
    private StructureData structureData;
    private MapData mapData;


    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameData = GameData.getInstance();
        structureData = StructureData.getInstance();
        mapData = MapData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        RecyclerView rv = view.findViewById(R.id.mapRecyclerView);


        // Specifying how RV should be laid out
        Settings settings = GameData.getInstance().getSettings();
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

    public void notifyAdapter(){
        mapAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            vh.bind(gameData.getMapElement(col,row));
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

        public GridCellVH(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.grid_cell, parent, false));
            int size = parent.getMeasuredHeight() / GameData.getInstance().getSettings().getMapHeight() + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            topLeft = itemView.findViewById(R.id.imageView);
            topRight = itemView.findViewById(R.id.imageView2);
            botLeft = itemView.findViewById(R.id.imageView3);
            botRight = itemView.findViewById(R.id.imageView4);
            structureImg = itemView.findViewById(R.id.imageView5);
        }

        public void bind(MapElement mapElement) {

            topLeft.setImageResource(mapElement.getNorthWest());
            topRight.setImageResource(mapElement.getNorthEast());
            botLeft.setImageResource(mapElement.getSouthWest());
            botRight.setImageResource(mapElement.getSouthEast());

            if (mapElement.getStructure() != null) {
                structureImg.setImageBitmap(mapElement.getImage());
            }
        }
    }

}
