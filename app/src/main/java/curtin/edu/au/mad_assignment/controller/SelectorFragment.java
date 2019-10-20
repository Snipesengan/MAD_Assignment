package curtin.edu.au.mad_assignment.controller;

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
import curtin.edu.au.mad_assignment.model.Structure;
import curtin.edu.au.mad_assignment.model.StructureData;

public class SelectorFragment extends Fragment {

    private SelectorAdapter selectorAdapter;
    private Structure selectedStructure;
    private int prevSelectedPosition;
    private int selectedPosition;

    public SelectorFragment() {
        // Required empty public constructor
    }


    public static SelectorFragment newInstance() {
        SelectorFragment fragment = new SelectorFragment();
        return fragment;
    }

    public Structure getSelectedStructure()
    {
        return selectedStructure;
    }

    public void deselectStructure(){
        selectedStructure = null;
        selectorAdapter.notifyItemChanged(selectedPosition);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // TODO: Setup event handlers

        return view;
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
        private TextView structureText;
        private Structure bindStructure;
        private int vhPosition;


        public StructureVH(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.list_selection, parent, false));
            structureImg = itemView.findViewById(R.id.structureImg);
            structureText = itemView.findViewById(R.id.structureTxt);

            /** SELECTOR CELL EVENTS
             *
             *  1. When user tap an item in the list, stores the reference to the structure
             *    contained in the list. Retrieve it by call getSelectedMapElement()
             *
             *  Interactions with other fragments
             *      * StructureDetailsFragment
             *          This fragment contains information about the structure
             *
             */
            structureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Selecting/deselecting a structure
                    prevSelectedPosition = selectedPosition;
                    selectedPosition = vhPosition;

                    if(selectedPosition == prevSelectedPosition)
                    {
                        selectedStructure = (selectedStructure == null) ? bindStructure : null;
                    }
                    else
                    {
                        selectedStructure = bindStructure;
                    }

                    selectorAdapter.notifyItemChanged(selectedPosition);
                    if(prevSelectedPosition >= 0)
                    {
                        selectorAdapter.notifyItemChanged(prevSelectedPosition);
                    }
                }
            });
        }

        public void bind(Structure structure, int position) {
            structureImg.setImageResource(structure.getDrawableId());
            structureText.setText(structure.getLabel());

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
    }


}
