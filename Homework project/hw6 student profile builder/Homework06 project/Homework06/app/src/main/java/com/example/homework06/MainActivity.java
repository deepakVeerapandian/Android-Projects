package com.example.homework06;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements MyProfile.OnFragmentInteractionListener,SelectAvatar.OnFragmentInteractionListener,DisplayMyProfile.OnFragmentInteractionListener{

    ImageView img_avatar;
    static public int setImageId = 0;
    static public boolean isEdited = false;
    static public int changedImageId = 0;
//    MyProfile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Profile");

        img_avatar = findViewById(R.id.imgMyAvatar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainActivityContainer, new MyProfile(), "TAG_MyProfile")
                .addToBackStack("TAG_MyProfile")
                .commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void editProfile(ProfileDetails profileDetails) {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.mainActivityContainer, new MyProfile(), "TAG_MyProfile")
//                .addToBackStack("TAG_MyProfile")
//                .commit();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = MyProfile.createEditFragment(profileDetails);
        ft.replace(R.id.mainActivityContainer, fragment);
        ft.commit();
        setTitle("My Profile");
    }

    @Override
    public void goBackToMyProfile(int id) {
        MyProfile myProfile = (MyProfile) getSupportFragmentManager().findFragmentByTag("TAG_MyProfile");
        myProfile.setAvatarImage(id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityContainer, new MyProfile(), "TAG_MyProfile")
                .addToBackStack("TAG_MyProfile")
                .commit();
        setTitle("My Profile");
    }

    @Override
    public void chooseAvatar(ProfileDetails profileDetails) {
        if(isEdited){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = SelectAvatar.selectAvatarOnEdit(profileDetails);
            ft.replace(R.id.mainActivityContainer, fragment);
            ft.commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainActivityContainer, new SelectAvatar(), "TAG_SelectAvatar")
                    .addToBackStack("TAG_SelectAvatar")
                    .commit();
        }

        setTitle("Select Avatar");
    }

    @Override
    public void showSaveDetails(ProfileDetails profileDetails) {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(profileDetails);
        prefsEditor.putString("MySharedPrefObject", json);
        prefsEditor.commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityContainer, new DisplayMyProfile(), "TAG_DisplayMyProfile")
                .addToBackStack("TAG_DisplayMyProfile")
                .commit();
        setTitle("Display My Profile");
    }
}
