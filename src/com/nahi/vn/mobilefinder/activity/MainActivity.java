package com.nahi.vn.mobilefinder.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.fragment.MainFragment;

public class MainActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onInit(savedInstanceState);
	}

	/**
	 * On init.
	 */
	private void onInit(Bundle savedInstanceState) {
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
                return;
            }
		}
        // Create an instance of ExampleFragment
		MainFragment firstFragment = new MainFragment();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();
	}

}
