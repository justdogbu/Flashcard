package com.example.flashcard;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flashcard.utils.OnDialogConfirmListener;
import com.example.flashcard.utils.OnDrawerNavigationPressedListener;
import com.example.flashcard.viewmodel.HomeDataViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private HomeDataViewModel homeDataViewModel;


    private SharedPreferences sharedPref;
    private static final int UPDATE_USER_REQUEST = 555;
    private OnDrawerNavigationPressedListener onDrawerNavigationPressedListener;
    private OnDialogConfirmListener onDialogConfirmListener;
    private TextView profileUsername;
    private ShapeableImageView profileImage;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        homeDataViewModel = new ViewModelProvider(requireActivity()).get(HomeDataViewModel.class);
        sharedPref = requireActivity().getSharedPreferences("SHAREDPREFKEY", Context.MODE_PRIVATE);

        profileUsername = (TextView) view.findViewById(R.id.profileUsername);
        profileImage = (ShapeableImageView) view.findViewById(R.id.profileImage);

        homeDataViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                profileUsername.setText(user.getUsername());
                Picasso.get().load(Uri.parse(user.getAvatar())).into(profileImage);
            }
        });
            return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDrawerNavigationPressedListener && context instanceof OnDialogConfirmListener) {
            onDrawerNavigationPressedListener = (OnDrawerNavigationPressedListener) context;
            onDialogConfirmListener = (OnDialogConfirmListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDrawerNavigationPressedListener = null;
        onDialogConfirmListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getUserVM() {
        homeDataViewModel = new ViewModelProvider(requireActivity()).get(HomeDataViewModel.class);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUserVM();
        }
    }
}