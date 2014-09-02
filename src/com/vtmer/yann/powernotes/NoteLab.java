package com.vtmer.yann.powernotes;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class NoteLab {
	private static final String TAG = "NoteLab";
	private static final String FILENAME = "notes.json";

	private NoteJSONSerializer mSerializer;
	private ArrayList<Note> mNotes;
	private static NoteLab sNoteLab;
	private Context mAppContext;
	
	private NoteLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new NoteJSONSerializer(mAppContext, FILENAME);
		try {
			mNotes = mSerializer.loadNotes();
			Log.d(TAG, "load date from file");
		} catch (Exception e) {
			mNotes = new ArrayList<Note>();
			Log.d(TAG, "第一次启动应用?");
		}
	}
	
	public void addNote(Note n) {
		mNotes.add(n);
	}
	
	public void deleteNote(Note n) {
		mNotes.remove(n);
	}
	
	public boolean saveNotes() {
		try {
			mSerializer.saveNotes(mNotes);
			Log.d(TAG, "saved to file");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	// 单例
	public static NoteLab get(Context c) {
		if (sNoteLab == null) {
			Log.d(TAG, "单例");
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
