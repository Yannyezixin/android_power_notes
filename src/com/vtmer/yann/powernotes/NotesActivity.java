package com.vtmer.yann.powernotes;

import android.support.v4.app.Fragment;

public class NotesActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		//启动NotesListFragment,托管Fragment
		return new NotesListFragment();
	}
	
}
