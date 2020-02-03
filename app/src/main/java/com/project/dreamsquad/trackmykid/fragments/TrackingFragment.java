package com.project.dreamsquad.trackmykid.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.project.dreamsquad.trackmykid.R;
import com.project.dreamsquad.trackmykid.activity.MainActivityOld;
import com.project.dreamsquad.trackmykid.models.UserProfile;
import com.project.dreamsquad.trackmykid.others.CircleTransform;
import com.project.dreamsquad.trackmykid.others.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by this pc on 29-05-17.
 */

public class TrackingFragment extends Fragment {

    private View view;
    String image_address = "https://images.pexels.com/photos/208134/pexels-photo-208134.jpeg?auto=compress&cs=tinysrgb&h=650&w=940";
//    String image_address = "http://media.gettyimages.com/photos/male-high-school-student-portrait-picture-id98680202?s=170667a";
    ImageView student_image;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView viewInMap, name, class_sec, status;
    TextView student_name, school;
    SupportMapFragment map;
    PrefManager pref;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.student_card_layout, container, false);
        student_image = (ImageView) view.findViewById(R.id.student_image);
        viewInMap = (TextView) view.findViewById(R.id.view_in_map);
        name = (TextView) view.findViewById(R.id.student_name);
        class_sec = (TextView) view.findViewById(R.id.school);
        status = (TextView) view.findViewById(R.id.status);
        student_name = (TextView) view.findViewById(R.id.student_name);
        school = (TextView) view.findViewById(R.id.school);

        pref = new PrefManager(getActivity());

        UserProfile userProfile = UserProfile.getInstance();
        student_name.setText(userProfile.getKidName());
        school.setText(userProfile.getKidSchool());

        viewInMap.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                System.out.println("A");
                Intent intent = new Intent(getActivity(), MainActivityOld.class);
                System.out.println("B");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//                    View student_image  =  view.findViewById(R.id.student_image);
//                    View status =  view.findViewById(R.id.status);
//                    View name= view.findViewById(R.id.student_name);
////                    View class_sec= view.findViewById(R.id.class_section);
//                    View tab_host= view.findViewById(R.id.tab_host);
//
//                    Pair<View, String> pair1 = Pair.create(student_image, student_image.getTransitionName());
//                    Pair<View, String> pair2 = Pair.create(name, name.getTransitionName());
//                    Pair<View, String> pair3 = Pair.create(status, status.getTransitionName());
//                    Pair<View, String> pair4 = Pair.create(class_sec, class_sec.getTransitionName());
//                    Pair<View, String> pair5 = Pair.create(tab_host, tab_host.getTransitionName());
//
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation(getActivity(), pair1, pair2, pair3,pair4,pair5);
//                    startActivity(intent, options.toBundle());
//                }
//                else {
                startActivity(intent);

            }
        });

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        createViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_host);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                if (tabLayout.getSelectedTabPosition() == 0) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_orange_24dp);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_grey_24dp);
//                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_grey_24dp);
                }
                if (tabLayout.getSelectedTabPosition() == 1) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_grey_24dp);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_orange_24dp);
//                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_grey_24dp);
                }
//                if (tabLayout.getSelectedTabPosition() == 2) {
//                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_grey_24dp);
//                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_grey_24dp);
//                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_orange_24dp);
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Glide.with(getActivity()).load(image_address)
                .apply(new RequestOptions()
                        .centerCrop()
                        .bitmapTransform(new CircleTransform(getActivity()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .thumbnail(0.5f)

                .into(student_image);

        return view;
    }

    private void createViewPager(ViewPager viewPager) {
        System.out.println("Inside create view pager");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        System.out.println("After adaper created");
        adapter.addFrag(new BusTrackFragment(), "Tab 1");
        System.out.println("Bus tracker frag");
        adapter.addFrag(new ContactsFragment(), "Tab 2");
//        adapter.addFrag(new Calender_Fragment(), "Tab 3");
        viewPager.setAdapter(adapter);
        System.out.println("Adapted addeed");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_orange_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_phone_grey_24dp);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_date_range_grey_24dp);
    }
}
