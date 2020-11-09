package curtin.edu.au.mad_assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.MapElement;

public class MapElementFragment extends Fragment {

    /**
     *  This fragment shows information about a particular structure in a grid cell.
     *      - xCord: x coordinate of the structure
     *      - yCord: y coordinate of the structure
     *      - structureType (immutable): {"Residential","Commercial","Road"}
     *      - editableName: name of the structure
     *      - Feature for taking a thumbnail
     *
     *  Callback attached to calling activity
     *      +  void onCloseMapElementDetails()
     *          - When the user hits the close button
     *
     *      +   void onDemolishButtonPressed(MapElement mapElement)
     *          - When the user hits the demolish button
     *
     *      +   void onUpdateMapElementName(MapElement mapElement, String name)
     *          - When the user change the name of the map element
     *
     *      +   void onUpdateThumbnail(MapElement mapElement)
     *          - When user wants the change the thumbnail
     *
     */
    private static final String MAP_ELEMENT = "curtin.edu.au.mad_assignment.controller.MapElementFragment.MAP_ELEMENT";

    private MapElement mapElement;
    private ImageButton closeButton;
    private ImageView structureImg;
    private TextView locationTV, structureTypeTV;
    private EditText ownerNameETV;
    private Button demolishButton;
    private ImageButton acceptButton;

    private OnFragmentInteractionListener callback;

    public MapElementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapElementFragment.
     */
    public static MapElementFragment newInstance(MapElement mapElement) {
        MapElementFragment fragment = new MapElementFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAP_ELEMENT,mapElement);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateMapElement(MapElement me)
    {
        mapElement = me;
        structureImg.setImageResource(mapElement.getStructure().getDrawableId());
        locationTV.setText(mapElement.getLocationString());
        ownerNameETV.setText(mapElement.getOwnerName());
        structureTypeTV.setText(mapElement.getStructure().getLabel());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapElement = (MapElement) getArguments().getSerializable(MAP_ELEMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_element, container, false);
        closeButton = view.findViewById(R.id.closeMapFragmentButton);
        structureImg = view.findViewById(R.id.structureImg);
        locationTV = view.findViewById(R.id.mapElementLocation);
        ownerNameETV = view.findViewById(R.id.ownerName);
        structureTypeTV = view.findViewById(R.id.structureType);
        demolishButton = view.findViewById(R.id.demolishButton);
        acceptButton = view.findViewById(R.id.acceptButton);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onCloseMapElementDetails();
            }
        });

        demolishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onDemolishButtonPressed(mapElement);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onUpdateMapElementName(mapElement,ownerNameETV.getText().toString());
                ownerNameETV.setFocusable(false);
            }
        });

        ownerNameETV.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER)
                {
                    MapActivity.hideSoftKeyboard(getActivity());
                    ownerNameETV.setFocusable(false);
                    return true;
                }
                return false;
            }
        });

        /**
         * Focused when touched
         */
        ownerNameETV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ownerNameETV.setFocusableInTouchMode(true);
                return false;
            }
        });

        /**
         * Functionality to start the camera app when touched...
         */
        structureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onUpdateThumbnail(mapElement);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            callback = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface OnFragmentInteractionListener {

        void onCloseMapElementDetails();

        void onDemolishButtonPressed(MapElement mapElement);

        void onUpdateMapElementName(MapElement mapElement, String name);

        void onUpdateThumbnail(MapElement mapElement);
    }
}
