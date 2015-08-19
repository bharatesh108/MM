package com.dfc.moneymartus.view.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.dfc.moneymartus.R;

@SuppressLint("InflateParams")
public class FaqAnswerActivity extends BaseActivity {

	@InjectView(R.id.tv_que)
	TextView mTextQuestion;
	@InjectView(R.id.tv_ans)
	TextView mTextAnswer;

	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_answer_faq);
		initUI();
		
		
		
	}

	/**
	 * this is used to initialize the UI component 
	 */
	private void initUI() {
		ButterKnife.inject(this);
		Bundle bundle = getIntent().getExtras();
		String queStr = bundle.getString("question");
		String ansStr = bundle.getString("answer");
		String tabInfo = bundle.getString("title");
		mTextQuestion.setText(queStr);
		mTextAnswer.setText(ansStr);
		setUpActionBar(tabInfo);
		}

	

	/**
	 * this is used to initialize and set up the action bar
	 * @param title
	 */
	private void setUpActionBar(String title) {
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.view_actionbarview_new, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		mTitleTextView.setText(title);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
	}

	

	
	public void homeBtnClick(View v) {
		finish();
	}

	
}
