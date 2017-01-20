package self.edu.project_client.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by WongFuChuen on 13/5/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentLists;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentLists = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position);
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void addFragment(Fragment fragment) {
        fragmentLists.add(fragment);
    }

}
