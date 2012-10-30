package i.safareli.georadio;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Player extends MediaPlayer implements OnPreparedListener,
		OnInfoListener, OnClickListener, OnSeekBarChangeListener,
		OnBufferingUpdateListener, OnErrorListener, OnCancelListener {

	protected TextView _tvInfo;
	protected Timer _timer;
	protected SeekBar _sbSeek;

	protected View _bPlay, _bPause, _bStop;

	protected final int HOUR = 60 * 60 * 1000;
	protected final int MINUTE = 60 * 1000;
	protected final int SECOND = 1000;
	protected int _currentPosition;
	protected ProgressDialog _bufferingDialog;
	protected boolean _isSeeking = false;
	protected boolean _isStoped = true;
	protected boolean _isPrepared = false;
	protected String _dataSource;
	protected Activity _activity;
	protected Handler _handler;
	protected boolean _isPreparing;

	public Player(String url, Activity activity)
			throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {
		super();
		// TODO change context of dialog
		_activity = activity;
		_bufferingDialog = new ProgressDialog(_activity);
		_bufferingDialog.getContext();
		_bufferingDialog.setOnCancelListener(this);
		_bufferingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_bufferingDialog.setMessage("buffering...");

		setAudioStreamType(AudioManager.STREAM_MUSIC);
		setDataSource(url);
		// prepareAsync(); // might take long! (for buffering, etc)
		setOnPreparedListener(this);
		setOnInfoListener(this);
		setOnBufferingUpdateListener(this);
	}

	public void prepareAsync() {
		isPreparing(true);
		isStoped(false);
		_bufferingDialog.show();
		super.prepareAsync();
	}

	public void dismissBufferingDialog() {
		getBufferingDialog().dismiss();
		_bufferingDialog = null;
	}

	private ProgressDialog getBufferingDialog() {
		if (_bufferingDialog == null) {
			_bufferingDialog = new ProgressDialog(_activity);
			_bufferingDialog.getContext();
			_bufferingDialog.setOnCancelListener(this);
			_bufferingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			_bufferingDialog.setMessage("buffering...");
		}

		return _bufferingDialog;
	}

	@Override
	public void setDataSource(String path) throws IOException,
			IllegalArgumentException, SecurityException, IllegalStateException {
		super.setDataSource(path);
		_dataSource = path;
	}

	public String getDataSource() {
		return _dataSource;
	}

	public String getProgressText(int CurrentPositionInMillis) {
		_currentPosition = CurrentPositionInMillis;
		return getProgressText();
	}

	public String getProgressText() {
		if (!isStoped()) {
			int currentPosition;
			int durationInMillis = getDuration();
			if (_currentPosition != -1) {
				currentPosition = _currentPosition;
			} else {
				currentPosition = getCurrentPosition();
			}

			int durationHour = durationInMillis / HOUR;
			int durationMint = (durationInMillis % HOUR) / MINUTE;
			int durationSec = (durationInMillis % MINUTE) / SECOND;

			int currentHour = currentPosition / HOUR;
			int currentMint = (currentPosition % HOUR) / MINUTE;
			int currentSec = (currentPosition % MINUTE) / SECOND;

			_currentPosition = -1;

			if (durationHour > 0) {
				if (getDuration() < 1000) {
					return String.format("%02d:%02d:%02d", currentHour,
							currentMint, currentSec);
				} else {
					return String.format("%02d:%02d:%02d/%02d:%02d:%02d",
							currentHour, currentMint, currentSec, durationHour,
							durationMint, durationSec);
				}

			} else {
				if (getDuration() < 1000) {
					return String.format("%02d:%02d", currentMint, currentSec);
				} else {
					return String.format("%02d:%02d/%02d:%02d", currentMint,
							currentSec, durationMint, durationSec);
				}
			}
		} else {
			return null;
		}
	}

	public boolean isSeeking() {
		return _isSeeking;
	}

	protected boolean isSeeking(boolean Seeking) {
		_isSeeking = Seeking;
		return _isSeeking;
	}

	public boolean isStoped() {
		return _isStoped;
	}

	protected boolean isStoped(boolean Stoped) {
		_isStoped = Stoped;
		return _isStoped;
	}

	public void setSeekBar(SeekBar seekBar) {
		_sbSeek = seekBar;
		_sbSeek.setOnSeekBarChangeListener(this);
	}

	public void setHandler(Handler handler) {
		_handler = handler;
	}

	public void setPlayButton(View button) {
		_bPlay = button;
		_bPlay.setOnClickListener(this);
	}

	public void setPauseButton(View button) {
		_bPause = button;
		_bPause.setOnClickListener(this);
	}

	public void setStopButton(View button) {
		_bStop = button;
		_bStop.setOnClickListener(this);
	}

	public void setInfoPlace(TextView tvInfo) {
		_tvInfo = tvInfo;
	}

	public void onClick(View v) {
		if (_bPlay != null && v.getId() == _bPlay.getId()) {
			// !isPreparing()

			if (isStoped() && !isPreparing() && !isPrepared()) {
				Log.v("sapara", "if (isStoped())");
				prepareAsync();
			} else if (!isStoped() && !isPreparing() && isPrepared()) {
				start();
			} else if (!isStoped() && !isPreparing() && !isPrepared()) {
				start();
			}
		} else if (_bPause != null && v.getId() == _bPause.getId()) {
			pause();
		} else if (_bStop != null && v.getId() == _bStop.getId()) {
			stop();
			if (_sbSeek != null)
				_sbSeek.setProgress(0);
			if (_tvInfo != null)
				_tvInfo.setText("");
		}
	}

	public boolean isPrepared() {
		return _isPrepared;
	}

	private boolean isPrepared(boolean isPrepared) {
		_isPrepared = isPrepared;
		return _isPrepared;
	}

	public boolean isPreparing() {
		return _isPreparing;
	}

	private boolean isPreparing(boolean isPreparing) {
		_isPreparing = isPreparing;
		return _isPreparing;
	}

	public void onPrepared(MediaPlayer mp) {
		isPreparing(false);
		isPrepared(true);
		start();
		getBufferingDialog().hide();
		if (_sbSeek != null) {
			_sbSeek.setMax(getDuration());
			_sbSeek.setProgress(getCurrentPosition());
		}
	}

	protected void setSeekBarTimer() {
		if (_sbSeek != null || _tvInfo != null) {
			_timer = new Timer();
			_timer.schedule(new TimerTask() {
				public void run() {
					// Update the progress bar

					if (_handler != null)
						_handler.post(new Runnable() {

							public void run() {
								if (!isSeeking() && !isStoped()) {
									if (_sbSeek != null)
										_sbSeek.setProgress(getCurrentPosition());
									if (_tvInfo != null)
										_tvInfo.setText(getProgressText());
								}
							}

						});// end _handler.post

					try {
						if (!isSeeking()) {
							Thread.sleep(900);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.v("sapara", "Thread Exception", e);
					}
				}
			}, 0, 100);
		}
	}

	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		if (what == MEDIA_INFO_BUFFERING_START) {
			getBufferingDialog().show();
			return true;
		} else if (what == MEDIA_INFO_BUFFERING_END) {
			getBufferingDialog().hide();
			return true;
		}
		return false;
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser && !isStoped()) {
			if (!isPlaying()) {
				seekTo(progress);
			}
			if (_tvInfo != null)
				_tvInfo.setText(getProgressText(progress));
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		isSeeking(true);
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		isSeeking(false);
		if (isPlaying() && !isStoped()) {
			seekTo(seekBar.getProgress());
		}
	}

	@Override
	public void pause() throws IllegalStateException {
		super.pause();
		if (_timer != null)
			_timer.cancel();
	}

	@Override
	public void start() throws IllegalStateException {
		super.start();
		isPrepared(false);
		isStoped(false);
		setSeekBarTimer();
	}

	@Override
	public void stop() throws IllegalStateException {
		seekTo(0);
		super.stop();
		isStoped(true);
		isPrepared(false);
		if (_timer != null)
			_timer.cancel();
	}

	@Override
	public void release() {
		// _bufferingDialog.dismiss();
		if (_timer != null)
			_timer.cancel();

		_bPause = null;
		_bPlay = null;
		_bStop = null;
		_tvInfo = null;
		_tvInfo = null;
		_timer = null;
		_sbSeek = null;
		_bPlay = null;
		_bPause = null;
		_bStop = null;
		_currentPosition = -1;
		// TODO
		// _bufferingDialog = null;
		_isSeeking = false;
		_isStoped = false;
		_isPrepared = false;
		_dataSource = null;
		_activity = null;
		_handler = null;
		super.release();
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (_sbSeek != null)
			_sbSeek.setSecondaryProgress((getDuration() / 100) * percent);
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		if (what == MEDIA_ERROR_UNKNOWN) {
			// _bufferingDialog.show();
			return true;
		} else if (what == MEDIA_ERROR_SERVER_DIED) {
			// _bufferingDialog.hide();
			return true;
		}
		return false;
	}

	public void onCancel(DialogInterface dialog) {
		_activity.finish();
		release();
		// _activity.onBackPressed()
	}
}
