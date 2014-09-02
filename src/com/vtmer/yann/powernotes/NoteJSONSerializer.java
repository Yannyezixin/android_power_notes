package com.vtmer.yann.powernotes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class NoteJSONSerializer {
	private static final String TAG = "NoteJsonSerializer";
	
	private Context mContext;
	private String mFileName;
	
	public NoteJSONSerializer (Context c, String f) {
		mContext = c;
		mFileName = f;
	}
	
	public ArrayList<Note> loadNotes() throws JSONException, IOException {
		ArrayList<Note> notes = new ArrayList<Note>();
		BufferedReader reader = null;
		try {
			InputStream in = mContext.openFileInput(mFileName);
			reader  = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for (int i = 0; i < array.length(); i++) {
				notes.add(new Note(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			Log.d(TAG, "file not found:",e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return notes;
	}
	
	public void saveNotes(ArrayList<Note> notes)
			throws JSONException, IOException {
		JSONArray array = new JSONArray();
		for (Note n : notes) {
			array.put(n.toJSON());
		}
		
		Writer writer = null;
		try {
			OutputStream out = mContext
					.openFileOutput(mFileName, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
			Log.d(TAG,"成功保存？");
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
