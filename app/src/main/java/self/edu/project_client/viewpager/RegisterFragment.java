package self.edu.project_client.viewpager;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import self.edu.project_client.R;
import self.edu.project_client.connect.server.AsyncResponsePost;
import self.edu.project_client.connect.server.HttpLink;
import self.edu.project_client.connect.server.MyAsyncTaskPost;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements AsyncResponsePost {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Layout Object
    private TextInputLayout registerEmailLayout;
    private TextInputLayout registerPasswordLayout;
    private TextInputLayout registerRePasswordLayout;
    private TextInputLayout registerFirstNameLayout;
    private TextInputLayout registerLastNameLayout;
    private TextInputLayout registerPhoneNoLayout;

    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private EditText etRegisterRePassowrd;
    private EditText etRegisterFirstName;
    private EditText etRegisterLastName;
    private EditText etRegisterPhoneNo;

    private Button btnSignUp;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        registerEmailLayout = (TextInputLayout) v.findViewById(R.id.registerEmailLayout);
        registerPasswordLayout = (TextInputLayout) v.findViewById(R.id.registerPasswordLayout);
        registerRePasswordLayout = (TextInputLayout) v.findViewById(R.id.registerRePasswordLayout);
        registerFirstNameLayout = (TextInputLayout) v.findViewById(R.id.registerFirstNameLayout);
        registerLastNameLayout = (TextInputLayout) v.findViewById(R.id.registerLastNameLayout);
        registerPhoneNoLayout = (TextInputLayout) v.findViewById(R.id.registerPhoneNoLayout);

        etRegisterEmail = (EditText) v.findViewById(R.id.etRegisterEmail);
        etRegisterPassword = (EditText) v.findViewById(R.id.etRegisterPassword);
        etRegisterRePassowrd = (EditText) v.findViewById(R.id.etRegisterRePassword);
        etRegisterFirstName = (EditText) v.findViewById(R.id.etRegisterFirstName);
        etRegisterLastName = (EditText) v.findViewById(R.id.etRegisterLastName);
        etRegisterPhoneNo = (EditText) v.findViewById(R.id.etRegisterPhoneNo);

        btnSignUp = (Button) v.findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etRegisterEmail.getText().toString();
                String pw = etRegisterPassword.getText().toString();
                String repw = etRegisterRePassowrd.getText().toString();
                String firstname = etRegisterFirstName.getText().toString();
                String lastname = etRegisterLastName.getText().toString();
                String phoneNo = etRegisterPhoneNo.getText().toString();

                registerEmailLayout.setError(null);
                registerPasswordLayout.setError(null);
                registerRePasswordLayout.setError(null);
                registerPhoneNoLayout.setError(null);

                if (TextUtils.isEmpty(email)) {
                    registerEmailLayout.setError(getResources().getString(R.string.email_error_message));
                    return;
                } else if (TextUtils.isEmpty(pw)) {
                    registerPasswordLayout.setError(getResources().getString(R.string.password_error_message));
                    return;
                } else if (TextUtils.isEmpty(repw)) {
                    registerRePasswordLayout.setError(getResources().getString(R.string.password_again_error_message));
                    return;
                } else if (TextUtils.isEmpty(phoneNo)) {
                    registerPhoneNoLayout.setError(getResources().getString(R.string.phone_no_error_message));
                    return;
                }

                if (!isEmailValid(email)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.not_email_format), Toast.LENGTH_LONG).show();
                }

                if (!pw.equals(repw)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.password_not_same), Toast.LENGTH_LONG).show();
                }

                if (pw.equals(repw)&&isEmailValid(email)) {
                    registerAction(email, pw, firstname, lastname, phoneNo);
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

        if (result.contains("Register Failed")) {
            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
        } else {
            MainActivity.changeFragment(1);
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

    public void registerAction(String email, String pw, String firstname, String lastname, String phoneNo) {
        MainActivity.loadingProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        MyAsyncTaskPost.nameValuePairs = new ArrayList<NameValuePair>(1);
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("email", email));
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("password", pw));
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("first_name", firstname));
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("last_name", lastname));
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("phone_number", phoneNo));
        MyAsyncTaskPost.nameValuePairs.add(new BasicNameValuePair("sex", "N"));

        MyAsyncTaskPost.getInstance(this).executeHttpPost(HttpLink.URLLink + "user_register.php");
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
