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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainFragment extends Fragment {
	private TextView countDownView;
	private MyCountDownTimer myCountDownTimer;
	private long settime;
	private ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, 30);
	private ImageView image;
	private long count;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View fragmentView = inflater.inflate(R.layout.fragment_main, container);
		final Button btnDialog = (Button) fragmentView.findViewById(R.id.btn_dialog_open);
		final Button btn_start = (Button) fragmentView.findViewById(R.id.btn_start);
		final Button btn_stop = (Button) fragmentView.findViewById(R.id.btn_stop);
		btn_stop.setEnabled(false);
		image = (ImageView) fragmentView.findViewById(R.id.imageView1);
		imageOutput(2);
		// 時間設定
		btnDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				showTimePickerDialog(fragmentView);
				btn_stop.setEnabled(false);
			}
		});
		// 開始ボタン
		btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				// カウントダウンView
				countDownView = (TextView) fragmentView.findViewById(R.id.label_dialog_text);
				myCountDownTimer = new MyCountDownTimer(settime, 500l);
				myCountDownTimer.start();
				btnDialog.setEnabled(false);
				btn_start.setEnabled(false);
				btn_stop.setEnabled(true);
				imageOutput(2);
				image.setImageResource(R.drawable.img);
			}
		});
		// 停止ボタン
		btn_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				myCountDownTimer.cancel();
				btnDialog.setEnabled(true);
				btn_start.setEnabled(true);
				imageOutput(2);
				toneGenerator.stopTone();
			}
		});
		// EditText
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void imageOutput(int set) {
		if (set == 1) {
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.INVISIBLE);
		}
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
				textView.setText(String.format("%s : %s", hourOfDay, minute));
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
			countDownView.setText(String.format("%s : %s", Long.toString(millisUntilFinished / 1000 / 60),
					Long.toString(millisUntilFinished / 1000 % 60)));
			count = millisUntilFinished / 1000 % 3;
			if (count == 0) {
				imageOutput(1);
			} else {
				imageOutput(2);
			}
		}

		@Override
		public void onFinish() {
			countDownView.setText(getResources().getString(R.string.time_up));
			toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
			image.setImageResource(R.drawable.imgm);
			imageOutput(1);
		}
	}
}
