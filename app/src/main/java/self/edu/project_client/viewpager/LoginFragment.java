package self.edu.project_client.viewpager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import self.edu.project_client.R;
import self.edu.project_client.connect.server.AsyncResponseGet;
import self.edu.project_client.connect.server.HttpLink;
import self.edu.project_client.connect.server.MyAsyncTaskGet;
import self.edu.project_client.connect.server.json.HandleJsonMethod;
import self.edu.project_client.connect.server.json.HandleJsonVariable;
import self.edu.project_client.method.sharepreference.CustomerSharePreference;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements AsyncResponseGet {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Layout object
    private TextInputLayout loginEmailLayout;
    private TextInputLayout loginPasswordLayout;

    private EditText etLoginEmail;
    private EditText etLoginPassword;

    private Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        loginEmailLayout = (TextInputLayout) v.findViewById(R.id.loginEmailLayout);
        loginPasswordLayout = (TextInputLayout) v.findViewById(R.id.loginPasswordLayout);

        etLoginEmail = (EditText) v.findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) v.findViewById(R.id.etLoginPassword);

        btnLogin = (Button) v.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString();
                String pw = etLoginPassword.getText().toString();

                loginEmailLayout.setError(null);
                loginPasswordLayout.setError(null);

                if(TextUtils.isEmpty(email)) {
                    loginEmailLayout.setError(getResources().getString(R.string.email_error_message));
                    return;
                } else if(TextUtils.isEmpty(pw)){
                    loginPasswordLayout.setError(getResources().getString(R.string.password_error_message));
                    return;
                } else {
                    loginAction(email, pw);
                }
            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processServerFinish(String result) {

        MainActivity.loadingProgressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (result.contains("Login Failed")) {

            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

        } else {

            String[] varName = { "uid", "first_name", "last_name" };
            List<HandleJsonVariable> resultList = new ArrayList<HandleJsonVariable>();
            resultList = HandleJsonMethod.getInstance().convertJsonToVariable(
                    result, varName);

            CustomerSharePreference.getInstance().setStringPreference(
                    CustomerSharePreference.UID, String.valueOf(resultList.get(0).uid));
            CustomerSharePreference.getInstance().setStringPreference(
                    CustomerSharePreference.Email, etLoginEmail.getText().toString());
            CustomerSharePreference.getInstance().setStringPreference(
                    CustomerSharePreference.First_Name, String.valueOf(resultList.get(0).first_name));
            CustomerSharePreference.getInstance().setStringPreference(
                    CustomerSharePreference.Last_Name, String.valueOf(resultList.get(0).last_name));
            CustomerSharePreference.getInstance().setBooleanPreference(
                    CustomerSharePreference.Login, true);

            String userName = String.valueOf(resultList.get(0).first_name) + " " + String.valueOf(resultList.get(0).last_name);

            MainActivity.setUserInfo("", userName, etLoginEmail.getText().toString());

            GeneralFragment.isLogin = true;

            MainActivity.changeFragment(0);
        }

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

    public void loginAction(String email, String pw) {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        MainActivity.loadingProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        MyAsyncTaskGet.getInstance(this).executeHttpGet(
                HttpLink.URLLink + "check_user_login.php?email=" + email + "&password=" + pw);
    }
}
