package com.vtmer.yann.powernotes;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class NoteFragment extends Fragment{
	private static final String TAG = "NoteFragment";
	private static final String DIALOG_DATE = "date";
	private static final int REQUEST_DATE = 0;
	
	public static final String EXTRA_NOTE_ID = "com.vtmer.noteFragment.note_id";
	public static final String EXTRA_ADDNOTE = "com.vtmer.noteFragment.add_note";
	
	private Note mNote;
	private TextView mNoteDate;
	private CheckBox mSolvedCheckBox;
	private EditText mContent;
	private EditText mNoteTitle;
	private boolean mAddNote;

	public static NoteFragment newInstance(UUID noteId, boolean addNote) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_NOTE_ID, noteId);
		args.putSerializable(EXTRA_ADDNOTE, addNote);
		
		NoteFragment fragment = new NoteFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mAddNote = (Boolean) getArguments().getSerializable(EXTRA_ADDNOTE);
		if (mAddNote) {
			mNote = new Note();
		} else {
			UUID noteId = (UUID) getArguments().getSerializable(EXTRA_NOTE_ID);
			mNote = NoteLab.get(getActivity()).getNote(noteId);
		}
	}
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_note_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUpFromSameTask();
				return true;
			case R.id.menu_save_item_note:
				if (mAddNote) NoteLab.get(getActivity()).addNote(mNote);
				saveNote();
				NavUpFromSameTask();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void NavUpFromSameTask() {
		if (NavUtils.getParentActivityIntent(getActivity()) != null) {
			NavUtils.navigateUpFromSameTask(getActivity());
		}
	}
	
	private void saveNote() {
		mNote.setTitle(mNoteTitle.getText().toString());
		mNote.setContent(mContent.getText().toString());
		mNote.setSolved(mSolvedCheckBox.isChecked());
		NoteLab.get(getActivity()).saveNotes();
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_note, parent, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityIntent(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		//初始化
		getActivity().getActionBar().setTitle(null);
		mNoteDate = (TextView) v.findViewById(R.id.date_note);
		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.solved_note);
		mContent = (EditText) v.findViewById(R.id.content_note);
		mNoteTitle = (EditText) v.findViewById(R.id.title_note);
		
		updateDate();
		mSolvedCheckBox.setChecked(mNote.isSolved());
		mContent.setText(mNote.getContent());
		mNoteTitle.setText(mNote.getTitle());
		
		mNoteDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mNote.getDate());
				dialog.setTargetFragment(NoteFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		return v;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mNote.setDate(date);
			updateDate();
		}
	}

	private void updateDate() {
		mNoteDate.setText(mNote.dateFormat(mNote.getDate()));
	}
}
