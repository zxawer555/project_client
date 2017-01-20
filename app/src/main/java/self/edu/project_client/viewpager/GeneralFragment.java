package self.edu.project_client.viewpager;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import self.edu.project_client.R;
import self.edu.project_client.connect.server.AsyncResponseGet;
import self.edu.project_client.connect.server.HttpLink;
import self.edu.project_client.connect.server.MyAsyncTaskGet;
import self.edu.project_client.connect.server.json.HandleJsonMethod;
import self.edu.project_client.connect.server.json.HandleJsonVariable;
import self.edu.project_client.method.database.DatabaseHandler;
import self.edu.project_client.method.sharepreference.CustomerSharePreference;
import self.edu.project_client.listviewadapter.Elderly;
import self.edu.project_client.listviewadapter.ElderlyListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeneralFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralFragment extends Fragment implements AsyncResponseGet {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Layout Object
    private Button btnAddElderly;

    private ListView elderlyList;
    private ArrayList<Elderly> elderlyLists;
    private ElderlyListAdapter elderlyListAdapter;

    private Dialog dialog;
    private boolean isAddElderly;
    public static boolean isLogin;

    public GeneralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralFragment newInstance(String param1, String param2) {
        GeneralFragment fragment = new GeneralFragment();
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
        View v = inflater.inflate(R.layout.fragment_general, container, false);

        btnAddElderly = (Button) v.findViewById(R.id.btnAddElderly);
        elderlyList = (ListView) v.findViewById(R.id.elderlyList);

        isAddElderly = false;
        
        btnAddElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.UID);

                isAddElderly = true;

                if (!uid.equals("")) {
                    AddElderlyDialog();
                } else {
                    Toast.makeText(getActivity(), getResources().getText(R.string.login_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        elderlyLists = new ArrayList<>();

        elderlyListAdapter = new ElderlyListAdapter(elderlyLists, getActivity());
        elderlyList.setAdapter(elderlyListAdapter);

        elderlyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Elderly elderly = elderlyListAdapter.getItem(position);

                ElderlyDetailFragment.elderly = elderly;

                MainActivity.viewPager.setCurrentItem(3);
            }
        });

        if (isLogin) {
            String uid = CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.UID);

            MyAsyncTaskGet.getInstance(this).executeHttpGet(
                    HttpLink.URLLink + "get_all_elderly_info.php?uid=" + uid);
        } else  if (DatabaseHandler.getInstance(getActivity()).getContactsCount() > 0) {
            List<Map<String, Object>> list = DatabaseHandler.getInstance(getActivity()).getAllContact();

            for(int i=0;i<list.size();i++) {
                int id = Integer.parseInt(list.get(i).get(DatabaseHandler.ELDERLY_COLUMN_ID).toString());
                String eid = list.get(i).get(DatabaseHandler.ELDERLY_COLUMN_EID).toString();
                String firebase_token = list.get(i).get(DatabaseHandler.ELDERLY_COLUMN_TOKEN).toString();
                String identifier = list.get(i).get(DatabaseHandler.ELDERLY_COLUMN_IDENTIFIER).toString();
                String message = list.get(i).get(DatabaseHandler.ELDERLY_COLUMN_MESSAGE).toString();
                String photo_link = list.get(i).get(DatabaseHandler.ELDERLY_COLUMN_PHOTOLINK).toString();
                String gps_status = list.get(i).get(DatabaseHandler.ELDERLY_COLUMN_GPSStatus).toString();

                Elderly elderly = new Elderly(id, eid, firebase_token, identifier, message, photo_link, gps_status);
                elderlyLists.add(elderly);
            }

            elderlyListAdapter.notifyDataSetChanged();
        }

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

        if (isAddElderly) {
            dialog.dismiss();
            isAddElderly  = false;
        }

        if (result.contains("Elderly identifier incorrect")) {

            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();

        } else {
            String[] varName = {"eid", "firebase_token", "identifier", "message", "photo_link"};
            List<HandleJsonVariable> resultList = new ArrayList<HandleJsonVariable>();
            resultList = HandleJsonMethod.getInstance().convertJsonToVariable(
                    result, varName);

            for (int i=0; i<resultList.size(); i++) {
                String eid = String.valueOf(resultList.get(i).eid);
                String firebase_token = String.valueOf(resultList.get(i).firebase_token);
                String identifier = String.valueOf(resultList.get(i).identifier);
                String message = String.valueOf(resultList.get(i).message);
                String photo_link = String.valueOf(resultList.get(i).photo_link);

                DatabaseHandler.getInstance(getActivity()).addContact(eid, firebase_token, identifier, message, photo_link);

                int id = DatabaseHandler.getInstance(getActivity()).getContactsCount() + 1;

                Elderly elderly = new Elderly(id, eid, firebase_token, identifier, message, photo_link, "off");

                elderlyLists.add(elderly);
            }

            elderlyListAdapter.notifyDataSetChanged();
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

    public void AddElderlyDialog() {
        // Create custom dialog object
        dialog = new Dialog(getActivity(), R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        dialog.setContentView(R.layout.add_elderly_dialog);

        dialog.show();

        final EditText etElderlyIdentifier = (EditText) dialog.findViewById(R.id.etElderlyIdentifier);

        final TextInputLayout elderlyIdentifierLayout = (TextInputLayout) dialog.findViewById(R.id.elderlyIdentifierLayout);

        Button btnAddElderly = (Button) dialog.findViewById(R.id.btnAddElderly);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        // if decline button is clicked, close the custom dialog
        btnAddElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                String identifier = etElderlyIdentifier.getText().toString();

                elderlyIdentifierLayout.setError(null);

                if(TextUtils.isEmpty(identifier)) {
                    elderlyIdentifierLayout.setError(getResources().getString(R.string.identifier_error_message));
                    return;
                } else if (DatabaseHandler.getInstance(getActivity()).checkContact(identifier)) {
                    addElderlyAction(identifier);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.repeat_error_message), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                isAddElderly = false;
            }

        });
    }

    public void addElderlyAction(String identifier) {

        MainActivity.loadingProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String uid = CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.UID);

        MyAsyncTaskGet.getInstance(this).executeHttpGet(
                HttpLink.URLLink + "add_elderly_info.php?uid=" + uid +  "&identifier=" + identifier);
    }

}
