package rationalduos.atulsoori.nofucksgiven;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eftimoff.viewpagertransformers.RotateUpTransformer;

import java.util.ArrayList;

import rationalduos.atulsoori.nofucksgiven.adapters.DynamicPagerAdapter;
import rationalduos.atulsoori.nofucksgiven.cardViews.NothingToShowFragment;
import rationalduos.atulsoori.nofucksgiven.models.CardInfo;
import rationalduos.atulsoori.nofucksgiven.utils.CardTransformer;
import rationalduos.atulsoori.nofucksgiven.utils.HackyViewPager;


public class CardHolderFragment extends Fragment {
    ArrayList<CardInfo> listOfCards;
    DynamicPagerAdapter mDynamicPagerAdapter ;
    public CardHolderFragment() {
        super();
        mDynamicPagerAdapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_holder_fragment_layout, vg, false);
        HackyViewPager pager = (HackyViewPager) view.findViewById(R.id.viewPager);

        try {
            listOfCards = getArguments().getParcelableArrayList("cardsList");
        } catch (Exception e) {
            listOfCards = new ArrayList<>();
            Log.e("NFG", Log.getStackTraceString(e));
        }

        mDynamicPagerAdapter = new DynamicPagerAdapter(getChildFragmentManager());

        if(listOfCards.size() == 0) {
            mDynamicPagerAdapter.addFragment(new NothingToShowFragment() );
        }
        for (int i = 0; (i < listOfCards.size()); ++i) {
            try {
                mDynamicPagerAdapter.addFragment(CardTransformer.cardInfoToFragment(listOfCards.get(i)));
            } catch (Exception e) {
                Log.e("NFG", Log.getStackTraceString(e));
            }
        }

        try {
            pager.setAdapter(mDynamicPagerAdapter);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                pager.setPageTransformer(true, new RotateUpTransformer());
            }

        } catch (Exception e) {
            Log.d("NFG", Log.getStackTraceString(e));
        }
        if(savedInstanceState != null){
            Log.d("NFG","CardHolderFragment has some bundle");
            int activeIndex = savedInstanceState.getInt("activeFragment");
            Log.d("NFG1","Bundle has some active fragment "+activeIndex);
            Fragment activeFragment = mDynamicPagerAdapter.getItem(activeIndex);
            mDynamicPagerAdapter.startUpdate((ViewGroup) pager);
            mDynamicPagerAdapter.setPrimaryItem((ViewGroup)pager,activeIndex,activeFragment);
            mDynamicPagerAdapter.finishUpdate((ViewGroup) pager);

        } else {
            Log.d("NFG1","Empty bundle");
        }
        //Log.d("NFG1", Log.getStackTraceString(new Exception()));
        Log.d("NFG1","Returning view");
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle b){
        int f = mDynamicPagerAdapter.getCurrentFragmentPosition();
        b.putInt("activeFragment",(f));
        Log.d("NFG","CardHolderFragment onSaveInstanceState");
        super.onSaveInstanceState(b);
    }

}
