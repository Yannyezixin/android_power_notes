package com.vtmer.yann.powernotes;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class NoteLab {

	private ArrayList<Note> mNotes;
	private static NoteLab sNoteLab;
	private Context mAppContext;
	
	private NoteLab(Context appContext) {
		mAppContext = appContext;
		mNotes = new ArrayList<Note>();
	}
	
	public void addNote(Note n) {
		mNotes.add(n);
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
	
	public Note getNote(UUID id) {
		for (Note c : mNotes) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}
}
