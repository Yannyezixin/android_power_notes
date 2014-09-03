package com.vtmer.yann.powernotes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;

public class DatePickerFragment extends DialogFragment {
	
	private static final String TAG = "NoteDatePickerFragment";
	public static final String EXTRA_DATE = 
			"com.vtmer.yann.powernote.date";
	
	private Date mDate;
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		getTargetFragment()
			.onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	private void initTimePicker(boolean Is24Hour, int hour, int minute) {
		mTimePicker.setIs24HourView(Is24Hour);
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(minute);
	}
	
	public static DatePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState) {
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_date, null);
		
		mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth,
						mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute())
						.getTime();
				Log.d(TAG, mDate.toString());
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		
		mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
		initTimePicker(true, hourOfDay, minute);
		mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				mDate = new GregorianCalendar(mDatePicker.getYear(), mDatePicker.getMonth(),
						mDatePicker.getDayOfMonth(), hourOfDay, minute).getTime();
				Log.d(TAG, mDate.toString());
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		
		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_picker_title)
				.setPositiveButton(R.string.date_picker_ok, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								sendResult(Activity.RESULT_OK);
							}
						})
				.setNegativeButton(R.string.date_picker_cancel, null)
				.create();
	}	
}
