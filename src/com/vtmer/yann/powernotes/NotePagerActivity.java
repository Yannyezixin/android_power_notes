package com.vtmer.yann.powernotes;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class NotePagerActivity extends FragmentActivity{

	private ViewPager mViewPager;
	private ArrayList<Note> mNotes;
	private boolean mAddNote;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//以代码方式创建内容视图，设置ViewPager为Activity的内容视图
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		//获取数据
		mNotes = NoteLab.get(this).getNotes();
		mAddNote = (Boolean) getIntent().getSerializableExtra(NoteFragment.EXTRA_ADDNOTE);
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int pos) {
				if (mAddNote) {
					return NoteFragment.newInstance(UUID.randomUUID(), mAddNote);
				} else {
					Note note = mNotes.get(pos);
					return NoteFragment.newInstance(note.getId(), false);
				}
			}

			@Override
			public int getCount() {
				if (mAddNote) {
					return 1;
				} else {
					return mNotes.size();
				}
			}
			
		});
		UUID noteId = (UUID) getIntent().getSerializableExtra(NoteFragment.EXTRA_NOTE_ID);
		for (int i = 0; i < mNotes.size(); i ++) {
			if (mNotes.get(i).getId().equals(noteId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
