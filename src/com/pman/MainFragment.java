package com.pman;

import android.app.TimePickerDialog;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainFragment extends Fragment {
	private TextView countDownView;
	private MyCountDownTimer myCountDownTimer;
	private long settime;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View fragmentView = inflater.inflate(R.layout.fragment_main, container);
		// 時間設定
		final Button btnDialog = (Button) fragmentView.findViewById(R.id.btn_dialog_open);
		btnDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				showTimePickerDialog(fragmentView);
			}
		});
		// 開始ボタン
		final Button btn_start = (Button) fragmentView.findViewById(R.id.btn_start);
		btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				// カウントダウンView
				countDownView = (TextView) fragmentView.findViewById(R.id.label_dialog_text);
				// long settime = R.id.label_dialog_text / 60;
				myCountDownTimer = new MyCountDownTimer(settime, 1000l);
				myCountDownTimer.start();
				btn_start.setEnabled(false);
			}
		});
		// 停止ボタン
		final Button btn_stop = (Button) fragmentView.findViewById(R.id.btn_stop);
		btn_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				btn_start.setEnabled(true);
			}
		});
		// EditText
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * タイムピッカーダイアログ表示
	 */
	private void showTimePickerDialog(final View fragmentView) {
		// 時間設定
		final int hour = 0;
		final int minute = 0;
		// 時間選択ダイアログの生成
		final TimePickerDialog timepick = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
				// 設定 ボタンクリック時の処理
				final TextView textView = (TextView) fragmentView.findViewById(R.id.label_dialog_text);
				textView.setText(String.format("%02d : %02d", hourOfDay, minute));
				settime = (hourOfDay * 60 + minute) * 1000;
			}
		}, hour, minute, true);
		// 表示
		timepick.show();
	}

	/**
	 * カウントダウンタイマー
	 */
	private class MyCountDownTimer extends CountDownTimer {
		MyCountDownTimer(final long millisInFuture, final long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(final long millisUntilFinished) {
			countDownView.setText(String.format("%s:%s", Long.toString(millisUntilFinished / 1000 / 60),
					Long.toString(millisUntilFinished / 1000 % 60)));
		}

		@Override
		public void onFinish() {
			countDownView.setText(getResources().getString(R.string.time_up));
			ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
			toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
			toneGenerator.stopTone();
		}
	}
}
