package com.vtmer.yann.powernotes;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SortDialogFragment extends DialogFragment{
	private static final String TAG = "NoteSort";
	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setItems(R.array.sort_array, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int position) {
						//Log.d(TAG, "单击了 " +position);
						ArrayList<Note> mNotes = NoteLab.get(getActivity()).getNotes();
						
						switch (position) {
							case 0:
								//Log.d(TAG, "日期排序");
								Collections.sort(mNotes, Note.NoteDateComparator);
								saveNotes();
								break;
							case 1:
								//Log.d(TAG, "标题排序");
								Collections.sort(mNotes, Note.NoteTitleComparator);
								saveNotes();
								break;
							case 2:
								Collections.sort(mNotes, Note.NoteSolvedComparator);
								saveNotes();
								break;
						}
					}
				})
				.create();
	}
	
	public void saveNotes() {
		NoteLab.get(getActivity()).saveNotes();
		getTargetFragment().onResume();
	}
	
}
