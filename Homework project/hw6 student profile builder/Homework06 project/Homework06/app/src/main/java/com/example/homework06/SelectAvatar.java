package com.example.homework06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectAvatar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SelectAvatar extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SelectAvatar() {
        // Required empty public constructor
    }

    public static SelectAvatar selectAvatarOnEdit(ProfileDetails profileDetails){
        SelectAvatar selectAvatar = new SelectAvatar();
        Bundle bundle = new Bundle();
        bundle.putSerializable("edit_profile_avatar", profileDetails);
        selectAvatar.setArguments(bundle);

        return selectAvatar;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int imageID = 0;
        for(int i = 1; i <=6 ; i++){
            switch (i){
                case 1:
                    imageID = R.id.imageView1;
                    break;
                case 2:
                    imageID = R.id.imageView2;
                    break;
                case 3:
                    imageID = R.id.imageView3;
                    break;
                case 4:
                    imageID = R.id.imageView4;
                    break;
                case 5:
                    imageID = R.id.imageView5;
                    break;
                case 6:
                    imageID = R.id.imageView6;
                    break;
            }
            final int finalImageID = imageID;
            final int finalI = i;
            getActivity().findViewById(imageID).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.isEdited)
                        MainActivity.changedImageId = finalImageID;

                    mListener.goBackToMyProfile(finalI);
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_avatar, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
        void goBackToMyProfile(int id);
    }
}
