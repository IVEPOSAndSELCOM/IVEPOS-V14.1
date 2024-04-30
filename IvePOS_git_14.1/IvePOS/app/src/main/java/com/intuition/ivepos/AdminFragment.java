package com.intuition.ivepos;

/**
 * Created by Rohithkumar on 1/6/2015.
 */

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

/**
 * Created by Rohithkumar on 1/6/2015.
 */
public class AdminFragment extends Fragment {

    Fragment frag;
    FragmentTransaction fragTransaction;
    LoginDataBaseAdapter loginDataBaseAdapter;


    public AdminFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.admin_chg_pwd, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (getActivity() instanceof AppCompatActivity){
            androidx.appcompat.app.ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionbar.setSubtitle(getString(R.string.action_admin));
        }

        RelativeLayout relativeLayout = (RelativeLayout)rootview.findViewById(R.id.masteradmin);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new MasterAdminActivity();
                //displayKeyboard();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        RelativeLayout relativeLayout1 = (RelativeLayout)rootview.findViewById(R.id.localadmin);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new LocalAdminActivity();
                //displayKeyboard();
                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
                fragTransaction.commit();
            }
        });

        return rootview;
    }

    public void displayKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }



//    public void onActivityCreated(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onActivityCreated(savedInstanceState);
//
//        loginDataBaseAdapter=new LoginDataBaseAdapter(getActivity());
//        loginDataBaseAdapter=loginDataBaseAdapter.open();
//
//        ImageView btnadmin = (ImageView)getActivity().findViewById(R.id.btngoadmin);
//
//        btnadmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                final  EditText editTextPassword=(EditText)getActivity().findViewById(R.id.etPass);
//
//                String password=editTextPassword.getText().toString();
//
//                // fetch the Password form database for respective user name
//                String storedPassword=loginDataBaseAdapter.getSinlgeEntrypass(password);
//                String storedpasswordlad = loginDataBaseAdapter.getSinlgeEntryladpass(password);
//                if(password.equals(storedPassword))
//                {
//                    frag = new AdminPassActivity();
//                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                    fragTransaction.commit();
//                }
//                else if(password.equals(storedpasswordlad)){
//                    frag = new AdminPassActivity();
//                    fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                    fragTransaction.commit();
//                }
//
//                else
//                {
//                    Toast.makeText(getActivity(), "User Name or Password does not match", Toast.LENGTH_LONG).show();
//                }
//
//
//
//
//
//            }
//        });
//
//
//    }

}
