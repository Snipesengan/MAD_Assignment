package curtin.edu.au.mad_assignment.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import curtin.edu.au.mad_assignment.R;
import curtin.edu.au.mad_assignment.model.MapElement;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapElementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapElementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MapElementFragment extends Fragment {

    /**
     *  This fragment shows information about a particular structure in a grid cell.
     *      - xCord: x coordinate of the structure
     *      - yCord: y coordinate of the structure
     *      - structureType (immutable): {"Residential","Commercial","Road"}
     *      - editableName: name of the structure
     *      - Feature for taking a thumbnail
     *
     */
    private static final String MAP_ELEMENT = "curtin.edu.au.mad_assignment.controller.MapElementFragment.MAP_ELEMENT";

    private MapElement mapElement;

    private OnFragmentInteractionListener mListener;

    public MapElementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapElementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapElementFragment newInstance(MapFragment mapFragment) {
        MapElementFragment fragment = new MapElementFragment();
        Bundle args = new Bundle();
        args.putSerializable(MAP_ELEMENT,mapFragment);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_element, container, false);
    }

    // TODO: Implement feature to take a photo
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
}
