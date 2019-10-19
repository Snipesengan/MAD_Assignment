package curtin.edu.au.mad_assignment.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.GameData;
import curtin.edu.au.mad_assignment.model.MapData;
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private GameData gameData;
    private StructureData structureData;
    private MapData mapData;

    public SelectorFragment() {
        // Required empty public constructor
    }


    public static SelectorFragment newInstance() {
        SelectorFragment fragment = new SelectorFragment();
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

        View view = inflater.inflate(R.layout.fragment_selector, container, false);


        RecyclerView rv = view.findViewById(R.id.selectorRecyclerView);

        // Specifying how RV should be laid out
        rv.setLayoutManager(new GridLayoutManager(
                getActivity(),
                1,
                GridLayoutManager.HORIZONTAL,
                false));

        // Create the adapter and hook it up
        SelectorAdapter selectorAdapter = new SelectorAdapter();
        rv.setAdapter(selectorAdapter);

        // TODO: Setup event handlers

        return view;
    }

    // TODO: Hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

    private class SelectorAdapter extends RecyclerView.Adapter<StructureVH>
    {
        @NonNull
        @Override
        public StructureVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new StructureVH(li,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull StructureVH holder, int position) {
            holder.bind(structureData.get(position));
        }

        @Override
        public int getItemCount() {
            return structureData.count();
        }
    }

    private class StructureVH extends RecyclerView.ViewHolder
    {
        private ImageView structureImg;
        private TextView structureText;

        public StructureVH(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.list_selection, parent, false));
            structureImg = itemView.findViewById(R.id.structureImg);
            structureText = itemView.findViewById(R.id.structureTxt);
        }

        public void bind(Structure structure) {
            structureImg.setImageResource(structure.getDrawableId());
            structureText.setText(structure.getLabel());
        }
    }


}
