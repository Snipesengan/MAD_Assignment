package curtin.edu.au.mad_assignment.controller;

import android.content.Context;
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
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;

public class SelectorFragment extends Fragment {

    private GameData gameData;
    private SelectorAdapter selectorAdapter;
    private Structure selectedStructure;
    private int prevSelectedPosition;
    private int selectedPosition;

    private OnFragmentInteractionListener callback;

    public SelectorFragment() {
        // Required empty public constructor
    }

    public static SelectorFragment newInstance() {
        SelectorFragment fragment = new SelectorFragment();
        return fragment;
    }

    public Structure getSelectedStructure(){
        return selectedStructure;
    }

    public void deselectStructure(){
        selectedStructure = null;
        selectorAdapter.notifyItemChanged(selectedPosition);
        selectedPosition = -1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameData = GameData.getInstance();
        prevSelectedPosition = -1;
        selectedPosition = -1;

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
        selectorAdapter = new SelectorAdapter();
        rv.setAdapter(selectorAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            callback = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStructureSelectedListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface OnFragmentInteractionListener {

        void onStructureSelectedListener(Structure structure);
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
            holder.bind(StructureData.get(position), position);
        }

        @Override
        public int getItemCount() {
            return StructureData.count();
        }
    }

    private class StructureVH extends RecyclerView.ViewHolder
    {
        private ImageView structureImg;
        private TextView structureType;
        private TextView structureCost;
        private Structure bindStructure;
        private int vhPosition;


        public StructureVH(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.list_selection, parent, false));
            structureImg = itemView.findViewById(R.id.structureImg);
            structureType = itemView.findViewById(R.id.structureTxt);
            structureCost = itemView.findViewById(R.id.structureCost);

            structureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Selecting/deselecting a structure
                    prevSelectedPosition = selectedPosition;
                    selectedPosition = vhPosition;

                    flipSelected();
                    selectorAdapter.notifyItemChanged(selectedPosition);
                    if(prevSelectedPosition >= 0)
                    {
                        selectorAdapter.notifyItemChanged(prevSelectedPosition);
                    }

                    if(selectedStructure != null)
                    {
                        callback.onStructureSelectedListener(selectedStructure);
                        selectorAdapter.notifyItemChanged(selectedPosition);
                    }
                }
            });
        }

        public void bind(Structure structure, int position) {
            structureImg.setImageResource(structure.getDrawableId());
            structureType.setText(structure.getLabel());
            structureCost.setText("$ " + gameData.getStructureCost(structure));

            bindStructure = structure;
            vhPosition = position;

            // Make any item that is not selected transparent
            if(position == selectedPosition && selectedStructure != null)
            {
                structureImg.setAlpha(255);
            }
            else
            {
                structureImg.setAlpha(127);
            }
        }

        /**
         *  Toggle selected structure if the user tap on the same cell twice
         */
        private void flipSelected()
        {
            if(selectedPosition == prevSelectedPosition)
            {
                selectedStructure = (selectedStructure == null) ? bindStructure : null;
            }
            else
            {
                selectedStructure = bindStructure;
            }
        }

    }


}
