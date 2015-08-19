package com.dfc.moneymartus.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import butterknife.InjectView;

import com.android.volley.VolleyError;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.FaqHandler;
import com.dfc.moneymartus.dto.FaqDataObject.Faq;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.BaseActivity;
import com.dfc.moneymartus.view.activity.FaqAnswerActivity;
import com.dfc.moneymartus.view.adapter.FaqArrayAdapter;
import com.dfc.moneymartus.view.callback.FaqView;

public class FaqMoneyMartFragment extends BaseFragment implements OnItemClickListener,FaqView{

	@InjectView(R.id.my_checks_list)
	ListView mFaqList;


	private Faq[] mFaqArray;
	private FaqHandler mFaqHandler;

	Button mBtnGeneral;
	Button mBtnCC;
	Button mBtnMc;
	View mViewDividerGeneral;
	View mViewDividerCC;
	View mViewDividerMC;

	private Activity mContext;
	private String mTitle;

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mContext = getActivity();

	}
	
	/**
	 * Used to set action bar title 
	 */
	private void setUpActionBar() {
		Util.setFragmentActionBarTitle((BaseActivity)getActivity(), getResources().getString(R.string.navigation_menu_faq_question));
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {
		// Inflate the layout for this fragment
		View mRootView = inflater.inflate(R.layout.faq_list_tab,container,false);
		initUI(mRootView);

		return mRootView;

	}

	/**
	 * Used to initialize UI components
	 * @param view used to create UI components 
	 */
	private void initUI(View view) {
		
		//ButterKnife.inject(this, view);
		mBtnGeneral = (Button)view.findViewById(R.id.btn_general);
		mBtnCC = (Button)view.findViewById(R.id.btn_cc);
		mBtnMc = (Button)view.findViewById(R.id.btn_mc);

		mViewDividerGeneral = (View)view.findViewById(R.id.v_divider_general);
		mViewDividerCC = (View)view.findViewById(R.id.v_divider_cc);
		mViewDividerMC = (View)view.findViewById(R.id.v_divider_mc);
		mFaqList = (ListView)view.findViewById(R.id.my_checks_list);
		
		mBtnGeneral.setOnClickListener(this);
		mBtnCC.setOnClickListener(this);
		mBtnMc.setOnClickListener(this);

		mFaqList.setOnItemClickListener(this);
		mTitle = "General";
		createRequest(EventTypes.FAQ_GENERAL, Constants.URL_FAQ_CORE);

		mViewDividerGeneral.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		mBtnGeneral.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
	
	}

	/**
	 * This method is required in future once FAQ's is available for for all 3 tabs. Currently only one API call is applicable for FAQ.
	 */
	@Override
	public void onClick(View v) {

		if (v.equals(mBtnGeneral)) {
			mTitle = "General";
			mViewDividerGeneral.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
			mBtnGeneral.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
			resetDividerState(mViewDividerCC, mViewDividerMC);
			createRequest(EventTypes.FAQ_GENERAL, Constants.URL_FAQ_CORE);
			resetTextColor(mBtnCC, mBtnMc);//list_text_color

		} else if (v.equals(mBtnCC)) {
			mTitle = "Check Cashing";
			mViewDividerCC.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
			mBtnCC.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
			createRequest(EventTypes.FAQ_CC, Constants.URL_FAQ_CC);
			resetDividerState(mViewDividerGeneral, mViewDividerMC);
			resetTextColor(mBtnGeneral, mBtnMc);

		} else if (v.equals(mBtnMc)) {
			mTitle = "Momentum Card";
			mViewDividerMC.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
			mBtnMc.setTextColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
			createRequest(EventTypes.FAQ_MC, Constants.URL_FAQ_MOMENTUM);
			resetDividerState(mViewDividerGeneral, mViewDividerCC);
			resetTextColor(mBtnCC, mBtnGeneral);
		}	else if (v.getId() == R.id.btn_right) {
			mDialog.dismiss();
		}
	}

	/**
	 * This method is required in future once FAQ's is available for for all 3 tabs. Currently only one API call is applicable for FAQ.
	 */
	private void resetTextColor(Button btn1, Button btn2) {
		btn1.setTextColor(Color.parseColor(getResources().getString(R.color.list_text_color)));
		btn2.setTextColor(Color.parseColor(getResources().getString(R.color.list_text_color)));
	}

	private void createRequest(int eventType, String URL) {
		
		if(Util.isNetworkAvailable(mContext))
		{
			showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
			mFaqHandler = new FaqHandler(this);
			mFaqHandler.setReqEventType(eventType);
			mFaqHandler.setUrl(URL);
			mFaqHandler.requestFaq();
		} else
		{
			showAlertDialog(getResources().getText(R.string.error_network).toString(), this);//("Network not available.", this);
		}

	}

	/**
	 * This method is required in future once FAQ's is available for for all 3 tabs. Currently only one API call is applicable for FAQ.
	 */
	private void resetDividerState(View v1, View v2) {
		v1.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_white)));
		v2.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_white)));
	}


	@Override
	public void onActivityCreated(final Bundle savedInstance) {
		super.onActivityCreated(savedInstance);
		setRetainInstance(true);
	}


	@Override
	public void onResume() {
		super.onResume();
		setUpActionBar();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(),FaqAnswerActivity.class);
		Faq faq = mFaqArray[position];
		intent.putExtra("question",faq.question );
		intent.putExtra("answer", faq.answer);
		intent.putExtra("title", mTitle);
		startActivity(intent);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FaqView#onGetFaqFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetFaqFailure(VolleyError error, int eventType) {
		hideLoader();
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(mContext, errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.FaqView#onGetFaqSuccess(com.dfc.moneymartus.dto.FaqDataObject.Faq[])
	 */
	@Override
	public void onGetFaqSuccess(Faq... faqArray) {
		hideLoader();
		this.mFaqArray = faqArray;
		String [] values =new String[faqArray.length];
		for (int i = 0; i < values.length; i++) {
			values[i]=faqArray[i].question;
			System.out.println("Values********************** "+values[i]);
		}
		if(getActivity()!=null){
			FaqArrayAdapter adapter = new FaqArrayAdapter(getActivity(),
					R.layout.faq_list_item, values);
			mFaqList.setAdapter(adapter);
		}
	}
}

