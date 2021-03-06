package com.swipeacademy.kissthebaker.BakingInstructions;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swipeacademy.kissthebaker.Main.RecipeResponse;
import com.swipeacademy.kissthebaker.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DirectionsActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.step_number)TextView mStepNum;
    @Nullable @BindView(R.id.prev_step)ImageButton mPrevButton;
    @Nullable @BindView(R.id.next_step)ImageButton mNextButton;

    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static final String STEP_NUMBER = "step_num";

    private DirectionsFragment mFragment;
    private ArrayList<RecipeResponse.StepsBean> sList;
    private int stepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        ButterKnife.bind(this);

        final FragmentManager fm = getSupportFragmentManager();
        mFragment = (DirectionsFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // Retrieve data
        Bundle bundle = getIntent().getExtras();
        sList = bundle.getParcelableArrayList(getString(R.string.stepsList_Key));
        stepPosition = bundle.getInt(getString(R.string.stepPosition));

        if(savedInstanceState == null) {
            stepPosition = bundle.getInt(getString(R.string.stepPosition));
        } else {
            stepPosition = savedInstanceState.getInt(STEP_NUMBER);
        }

        // Set correct position on navi
        updateStepNumberText(stepPosition);

        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stepPosition--;
                    mFragment = DirectionsFragment.newInstance(sList,stepPosition);
                    updateStepNumberText(stepPosition);
                    fm.beginTransaction().replace(R.id.direction_fragment_container,mFragment,TAG_RETAINED_FRAGMENT).commit();
                }
            });
        }

        if (mNextButton != null) {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stepPosition++;
                    mFragment = DirectionsFragment.newInstance(sList,stepPosition);
                    updateStepNumberText(stepPosition);
                    fm.beginTransaction().replace(R.id.direction_fragment_container,mFragment,TAG_RETAINED_FRAGMENT).commit();
                }
            });
        }

        if(mFragment == null){
            mFragment = DirectionsFragment.newInstance(sList,stepPosition);
            fm.beginTransaction().replace(R.id.direction_fragment_container,mFragment,TAG_RETAINED_FRAGMENT).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_NUMBER,stepPosition);
    }

    /**
     * Method to update the current step number in navigation bar
     *
     * @param stepPosition Current position
     */
    private void updateStepNumberText(int stepPosition){

        if(mStepNum != null) {
            mStepNum.setText(Integer.toString(stepPosition));

            // Check position and hide arrow if at 0 or last position,
            // preventing going negative and higher than list size
            if (stepPosition == 0) {
                mPrevButton.setVisibility(View.GONE);
            } else if (stepPosition == sList.size() - 1) {
                mNextButton.setVisibility(View.GONE);
            } else {
                mPrevButton.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
            }
        }
    }


}
