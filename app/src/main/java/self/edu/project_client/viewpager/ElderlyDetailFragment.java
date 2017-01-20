package self.edu.project_client.viewpager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import self.edu.project_client.R;
import self.edu.project_client.connect.server.AsyncResponseGet;
import self.edu.project_client.connect.server.HttpLink;
import self.edu.project_client.connect.server.MyAsyncTaskGet;
import self.edu.project_client.listviewadapter.Elderly;
import self.edu.project_client.listviewadapter.ElderlyLocation;
import self.edu.project_client.method.database.DatabaseHandler;
import self.edu.project_client.method.service.ElderlyLocationService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ElderlyDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ElderlyDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElderlyDetailFragment extends Fragment implements AsyncResponseGet {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Layout Object
    private ImageView imgEldelyOriginalIcon;
    private TextView tvElderlyDetailIdentifier;
    private Switch gpsSwtich;
    private TextView tvElderlyDetailLat;
    private TextView tvElderlyDetailLong;

    public static Elderly elderly;
    private Intent elderlyLocationServiceIntent;
    private String  gps_status;

    public ElderlyDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ElderlyDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ElderlyDetailFragment newInstance(String param1, String param2) {
        ElderlyDetailFragment fragment = new ElderlyDetailFragment();
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
        View v = inflater.inflate(R.layout.fragment_elderly_detail, container, false);

        imgEldelyOriginalIcon = (ImageView) v.findViewById(R.id.imgElderlyOriginalIcon);
        tvElderlyDetailIdentifier = (TextView) v.findViewById(R.id.tvElderlyDetailIdentifier);
        gpsSwtich = (Switch) v.findViewById(R.id.gpsSwitch);
        tvElderlyDetailLat = (TextView) v.findViewById(R.id.tvElderlyDetailLat);
        tvElderlyDetailLong = (TextView) v.findViewById(R.id.tvElderlyDetailLong);

        ElderlyLocationService.mContext = getActivity();

        elderlyLocationServiceIntent = new Intent(getActivity(), ElderlyLocationService.class);

        if (elderly!=null) {
            tvElderlyDetailIdentifier.setText(elderly.getIdentifier());

            gpsSwtich.setChecked(elderly.getGPSStatus());

            gpsSwtich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String content;
                    if (elderly.getGPSStatus()) {
                        gps_status = "off";
                        content = "GPSOff";
                    } else {
                        gps_status = "on";
                        content = "GPSOn";
                    }

                    elderly.setGPSStatus(gps_status);

                    DatabaseHandler.getInstance(getActivity()).updateContact(elderly.getId(), gps_status);

                    connectServer(content);

                }
            });
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        MainActivity.viewPager.setCurrentItem(0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processServerFinish(String result) {

        if (gps_status.equals("on")) {

            Log.d("result", "Start");

            if (ElderlyLocationService.elderlyLocationCount == 0) {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
                        getActivity().startService(elderlyLocationServiceIntent);

                        getActivity().registerReceiver(broadcastReceiver,
                                new IntentFilter(ElderlyLocationService.BROADCAST_ACTION));
//                    }
//                }, 3000);
            }

            ElderlyLocationService.elderlyLocationCount += 1;

        } else {
            Log.d("result", "Stop");

            ElderlyLocationService.elderlyLocationCount -= 1;

            if (ElderlyLocationService.elderlyLocationCount ==0 ) {
                getActivity().stopService(elderlyLocationServiceIntent);
            }

        }
    }

    public void connectServer(String content) {
        MyAsyncTaskGet.getInstance(this).executeHttpGet(
                HttpLink.URLLink + "firebase_message.php?token=" + elderly.getFirebase_token() + "&content=" + content);
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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            updateUI(intent);
        }

    };

    private void updateUI(Intent intent) {

        if (intent.getSerializableExtra("location")!=null) {
            ArrayList<ElderlyLocation> arrayList = (ArrayList<ElderlyLocation>) intent.getSerializableExtra("location");
            Log.d("result", String.valueOf(arrayList));

            for (int i=0;i<arrayList.size();i++) {
                ElderlyLocation location = arrayList.get(i);

                if (location.getEid().equals(elderly.getEid())) {
                    tvElderlyDetailLat.setText(location.getLatitude());
                    tvElderlyDetailLong.setText(location.getLongitude());
                }
            }
        } else {
            tvElderlyDetailLat.setText(getActivity().getResources().getString(R.string.elderly_message));
        }
    }
}
