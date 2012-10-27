package i.safareli.georadio;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Player extends MediaPlayer implements OnPreparedListener,
		OnInfoListener, OnClickListener, OnSeekBarChangeListener,
		OnBufferingUpdateListener,OnErrorListener {

	protected TextView _tvInfo;
	protected Timer _timer;
	protected SeekBar _sbSeek;

	protected View _bPlay, _bPause, _bStop;

	protected final int HOUR = 60 * 60 * 1000;
	protected final int MINUTE = 60 * 1000;
	protected final int SECOND = 1000;
	protected int _currentPosition;

	protected ProgressDialog _buferingDialog;
	protected boolean _isSeeking = false;
	protected boolean _isStoped = false;
	protected boolean _isPrepared;
	protected String _dataSource;
	protected Context _context;
	protected Handler _handler;

	public Player(String url, Context context) throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {
		super();
		_context = context;
		_buferingDialog = new ProgressDialog(_context);
		_buferingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_buferingDialog.setMessage("bufering...");

		setAudioStreamType(AudioManager.STREAM_MUSIC);
		setDataSource(url);
		// prepareAsync(); // might take long! (for buffering, etc)
		setOnPreparedListener(this);
		setOnInfoListener(this);
		setOnBufferingUpdateListener(this);
	}

	public void prepareAsync() {
		_buferingDialog.show();
		super.prepareAsync();
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
		if (_bPlay !=null && v.getId() == _bPlay.getId()) {
			if (isStoped() && !isPrepared()) {
				prepareAsync();
			} else if (!isStoped() && isPrepared()) {
				start();
			} else if (!isStoped() && !isPrepared()) {
				start();
			}
		} else if (_bPause !=null && v.getId() == _bPause.getId()) {
			pause();
		} else if (_bStop !=null && v.getId() == _bStop.getId()) {
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

	public void onPrepared(MediaPlayer mp) {
		isPrepared(true);
		start();
			_buferingDialog.hide();
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
				_buferingDialog.show();
			return true;
		} else if (what == MEDIA_INFO_BUFFERING_END) {
				_buferingDialog.hide();
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
		setSeekBarTimer();
		isPrepared(false);
		isStoped(false);
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
		super.release();
			_buferingDialog.dismiss();
		if (_timer != null)
			_timer.cancel();
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (_sbSeek != null)
			_sbSeek.setSecondaryProgress((getDuration() / 100) * percent);
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}
}
