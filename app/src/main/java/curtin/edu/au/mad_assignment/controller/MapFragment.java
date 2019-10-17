package curtin.edu.au.mad_assignment.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapElement;
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

    private static final String ARG_GAME_DATA = "curtin.edu.au.mad_assignment.model.game_data";
    private static final String ARG_STRUCTURE_DATA = "curtin.edu.au.mad_assignment.model.structure_data";

    private GameData gameData;
    private StructureData structureData;
    private OnFragmentInteractionListener mListener;

    private MapAdapter mapAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //TODO : Set up event handlers
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.mapRecyclerView);

        // Specifying how RV should be laid out
        rv.setLayoutManager(new GridLayoutManager(
                getActivity(),
                gameData.getSettings().getMapHeight(),
                GridLayoutManager.HORIZONTAL,
                false));

        // Create the adapter and hook it up
        mapAdapter = new MapAdapter(gameData, structureData);
        rv.setAdapter(mapAdapter);


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

    public class MapAdapter extends RecyclerView.Adapter<MyMapViewHolder> implements Serializable
    {
        private GameData gameData;
        private StructureData structureData;

        public MapAdapter(GameData gameData, StructureData structureData)
        {
            this.gameData = gameData;
            this.structureData = structureData;
        }

        @Override
        public MyMapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new MyMapViewHolder(li,parent);
        }

        @Override
        public void onBindViewHolder(MyMapViewHolder vh, int position) {
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


    private class MyMapViewHolder extends RecyclerView.ViewHolder implements Serializable
    {
        private ImageView backgroundI;
        private ImageView structureI;

        public MyMapViewHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.grid_cell, parent, false));
            int size = parent.getMeasuredHeight() / gameData.getSettings().getMapHeight() + 2;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            //Layers
            //backgroundI = (ImageView) itemView.findViewById(R.id.bgImg);
            structureI = (ImageView) itemView.findViewById(R.id.strImg);
        }

        public void bind(MapElement mapElement){
            Bitmap img = mapElement.getImage();
            structureI.setImageBitmap(img);
        }
    }

}
