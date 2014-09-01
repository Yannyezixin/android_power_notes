package com.vtmer.yann.powernotes;

import java.util.ArrayList;

import android.content.Context;

public class NoteLab {

	private ArrayList<Note> mNotes;
	private static NoteLab sNoteLab;
	private Context mAppContext;
	
	private NoteLab(Context appContext) {
		mAppContext = appContext;
		mNotes = new ArrayList<Note>();
		for (int i = 0; i < 50; i++) {
			Note n = new Note();
			n.setTitle("Note #" + i);
			n.setContent("this is the content of Note #" + i);
			n.setSolved(i % 3 == 0);
			mNotes.add(n);
		}
	}
	
	// 单例
	public static NoteLab get(Context c) {
		if (sNoteLab == null) {
			sNoteLab = new NoteLab(c.getApplicationContext());
		}
		return sNoteLab;
	}
	
	public ArrayList<Note> getNotes() {
		return mNotes;
	}
}
