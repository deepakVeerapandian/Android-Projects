package com.example.homework06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homework06.SelectAvatar.OnFragmentInteractionListener;

import static com.example.homework06.MainActivity.isEdited;
import static com.example.homework06.MainActivity.setImageId;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyProfile extends Fragment  {

    private EditText et_firstName;
    private EditText et_lastName;
    private EditText et_number;
    private RadioGroup rg_dept;
    private TextView txtDept;
    private TextView txtMyAvatar;
    private Button btn_save;
    private ImageView img;

    Boolean error = false;
    String department = "";
    int selectedImage = 0;

    public static MyProfile createEditFragment(ProfileDetails profileDetails){
        MyProfile myProfileFragment = new MyProfile();
        Bundle bundle = new Bundle();
        bundle.putSerializable("edit_profile", profileDetails);
        myProfileFragment.setArguments(bundle);

        return myProfileFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        et_firstName = getActivity().findViewById(R.id.editTextFirst);
        et_lastName = getActivity().findViewById(R.id.editTextLast);
        et_number = getActivity().findViewById(R.id.editTextStdId);
        txtDept = getActivity().findViewById(R.id.txtDept);
        txtMyAvatar = getActivity().findViewById(R.id.txtMyAvatar);
        rg_dept = getActivity().findViewById(R.id.radioGroup);
        img = getActivity().findViewById(R.id.imgMyAvatar);
        btn_save = getActivity().findViewById(R.id.btnSave);
//        img_avatar = getActivity().findViewById(R.id.imgMyAvatar);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDetails profileDetails = new ProfileDetails();
                if(isEdited){
                    profileDetails = (ProfileDetails) getArguments().getSerializable("edit_profile");;
                }
                mListener.chooseAvatar(profileDetails);
            }
        });

        switch (setImageId){
            case 1:
                img.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_f_1));
                selectedImage = R.drawable.avatar_f_1;
                break;
            case 2:
                img.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_m_1));
                selectedImage = R.drawable.avatar_m_1;
                break;
            case 3:
                img.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_f_2));
                selectedImage = R.drawable.avatar_f_2;
                break;
            case 4:
                img.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_m_2));
                selectedImage = R.drawable.avatar_m_2;
                break;
            case 5:
                img.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_f_3));
                selectedImage = R.drawable.avatar_f_3;
                break;
            case 6:
                img.setImageDrawable(getActivity().getDrawable(R.drawable.avatar_m_3));
                selectedImage = R.drawable.avatar_m_3;
                break;
        }


        rg_dept.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton1:
                        department = "CS";
                        break;
                    case R.id.radioButton2:
                        department = "SIS";
                        break;
                    case R.id.radioButton3:
                        department = "BIO";
                        break;
                    case R.id.radioButton4:
                        department = "Others";
                        break;
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error = false;
//                et_firstName = getActivity().findViewById(R.id.editTextFirst);
//                et_lastName = getActivity().findViewById(R.id.editTextLast);
//                et_number = getActivity().findViewById(R.id.editTextStdId);
//                txtDept = getActivity().findViewById(R.id.txtDept);
//                txtMyAvatar = getActivity().findViewById(R.id.txtMyAvatar);

                et_firstName.setError(null);
                et_lastName.setError(null);
                et_number.setError(null);
                txtMyAvatar.setError(null);
                txtDept.setError(null);



                if(et_firstName.getText().toString().equals("")){
                    et_firstName.setError("Enter a first name");
                    error = true;
                }
                if(et_lastName.getText().toString().equals("")){
                    et_lastName.setError("Enter a last name");
                    error = true;
                }
                if(et_number.getText().toString().equals("") || et_number.getText().toString().length() != 9){
                    et_number.setError("Enter a 9 digit student id");
                    error = true;
                }
                if(department.equals("")){
                    txtDept.setError("Choose a department");
                    error = true;
                }
                if(selectedImage == 0){
                    txtMyAvatar.setError("Choose an avatar");
                    error = true;
                    Toast.makeText(getContext(), "Choose an avatar", Toast.LENGTH_SHORT).show();
                }


                if(!error){
                    ProfileDetails profileDetails = new ProfileDetails();
                    profileDetails.firstName = et_firstName.getText().toString();
                    profileDetails.lastName = et_lastName.getText().toString();
                    profileDetails.studentId = et_number.getText().toString();
                    profileDetails.department = department;
                    profileDetails.imageId = selectedImage;

                    mListener.showSaveDetails(profileDetails);
                }
                else{
                    Toast.makeText(getContext(), "Enter correct details", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(MainActivity.isEdited){
            ProfileDetails profileDetails = (ProfileDetails) getArguments().getSerializable("edit_profile");
            if(profileDetails == null)
                profileDetails = (ProfileDetails) getArguments().getSerializable("edit_profile_avatar");
            et_firstName.setText(profileDetails.firstName);
            et_lastName.setText(profileDetails.lastName);
            et_number.setText(profileDetails.studentId);

            String department = profileDetails.department;
            int checkId = 0;
            switch (department){
                case "CS":
                    checkId = R.id.radioButton1;
                    break;
                case "SIS":
                    checkId = R.id.radioButton2;
                    break;
                case "BIO":
                    checkId = R.id.radioButton3;
                    break;
                case "Others":
                    checkId = R.id.radioButton4;
                    break;
            }
            rg_dept.check(checkId);

            img.setImageDrawable(getActivity().getDrawable(profileDetails.imageId));
        }

    }

    public void setAvatarImage(int id){
//        getActivity().findViewById(R.id.imgMyAvatar).setImageDrawable(getActivity().getDrawable(R.drawable.avatar_f_1));
        setImageId = id;
    }


    private OnFragmentInteractionListener mListener;

    public MyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
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
        void chooseAvatar(ProfileDetails profileDetails);
        void showSaveDetails(ProfileDetails profileDetails);
    }
}
