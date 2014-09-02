package com.vtmer.yann.powernotes;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class NotesListFragment extends ListFragment {
	
	private static final String TAG = "NotesListFragment";

	private ListView lv;
	private ArrayList<Note> mNotes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_notes_list, container, false);
		
		//适配器
		mNotes = NoteLab.get(getActivity()).getNotes();
		NoteAdapter adapter = new NoteAdapter(mNotes);
		lv = (ListView) v.findViewById(android.R.id.list);
		setListAdapter(adapter);
		
		//上下文操作监听
		setContextChoiceMode();
		
		//设置空视图
		lv.setEmptyView(v.findViewById(android.R.id.empty));
		
		return v;
	}
	
	@TargetApi(11)
	private void setContextChoiceMode() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			//使用上下文浮动菜单操作
			//登记视图
			registerForContextMenu(lv);
		} else {
			//使用上下文操作栏菜单
			//多选模式
			lv.setChoiceMode(lv.CHOICE_MODE_MULTIPLE_MODAL);
			lv.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.note_list_item_delete, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					switch (item.getItemId()) {
						case R.id.menu_delete_list_note:
							NoteAdapter adapter = (NoteAdapter)getListAdapter();
							NoteLab noteLab = NoteLab.get(getActivity());
							//删除note
							for (int i = adapter.getCount()-1; i >=0; i--) {
								if (getListView().isItemChecked(i)) {
									noteLab.deleteNote(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default:
							return false;
					}
				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}
				@Override
				public void onDestroyActionMode(ActionMode mode) {
				}
				@Override
				public void onItemCheckedStateChanged(ActionMode mode,
						int position, long id, boolean checked) {
				}
			});
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();
		((NoteAdapter) getListAdapter()).notifyDataSetInvalidated();
	}
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_note_list_menu, menu);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.note_list_item_delete, menu);
	}
	
	//MenuItem 有一个资源ID可用于识别选中的菜单项 item.getItemId()
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//获取要删除的Note对象
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		NoteAdapter adapter = (NoteAdapter)getListAdapter();
		Note note = adapter.getItem(position);
		
		switch (item.getItemId()) {
			case R.id.menu_delete_list_note:
				NoteLab.get(getActivity()).deleteNote(note);
				adapter.notifyDataSetChanged();
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_new_item_menu:
				Intent i = new Intent(getActivity(), NotePagerActivity.class);
				i.putExtra(NoteFragment.EXTRA_ADDNOTE, true);
				startActivityForResult(i, 0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//获取点击的Item
		Note n = ((NoteAdapter)getListAdapter()).getItem(position);
		
		//启动NotePagerActivity
		Intent i = new Intent(getActivity(), NotePagerActivity.class);
		i.putExtra(NoteFragment.EXTRA_NOTE_ID, n.getId());
		i.putExtra(NoteFragment.EXTRA_ADDNOTE, false);
		startActivity(i);
	}
	
	private class NoteAdapter extends ArrayAdapter<Note> {

		public NoteAdapter(ArrayList<Note> notes) {
			//调用超类的构造方法来绑定Crime对象的数组列表
			//没有使用预定义布局，传入参数0作为作为布局ID参数
			super(getActivity(), 0, notes);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// 检查传入视图是否为复用对象，不是则从定制布局中产生新的视图对象
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_notes_list_item, null);
			}
			Log.d(TAG, "get the view of" + position);
			Note n = getItem(position);
			
			TextView titleTextView = (TextView) convertView.findViewById(R.id.notes_list_title_textView);
			 		 titleTextView.setText(n.getTitle());
			TextView dateTextView = (TextView) convertView.findViewById(R.id.notes_list_date_textView);
			 		  dateTextView.setText(n.dateFormat(n.getDate()));
			CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.notes_list_solved_checkBox);
		     		  solvedCheckBox.setChecked(n.isSolved());
			
			return convertView;
		}	
	}
}
