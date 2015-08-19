package com.dfc.moneymartus.view.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.dfc.moneymartus.Moneymart;
import com.dfc.moneymartus.R;
import com.dfc.moneymartus.business.CheckByIdHandler;
import com.dfc.moneymartus.business.ChecksHandler;
import com.dfc.moneymartus.business.DataObject;
import com.dfc.moneymartus.business.MyChecksDataObject;
import com.dfc.moneymartus.data.ModelData;
import com.dfc.moneymartus.data.Preferences;
import com.dfc.moneymartus.dto.CheckDataObject;
import com.dfc.moneymartus.dto.ChecksObject;
import com.dfc.moneymartus.dto.GetCheckDataObject;
import com.dfc.moneymartus.gateway.CreateSubmitRequestService;
import com.dfc.moneymartus.gateway.IViewCallbackListner;
import com.dfc.moneymartus.infra.Constants;
import com.dfc.moneymartus.infra.Constants.PreferenceConstants;
import com.dfc.moneymartus.infra.ErrorHandling;
import com.dfc.moneymartus.infra.EventTypes;
import com.dfc.moneymartus.infra.Util;
import com.dfc.moneymartus.view.activity.AgreeToFeeActivity;
import com.dfc.moneymartus.view.activity.BaseActivity;
import com.dfc.moneymartus.view.activity.CashedRejectActivity;
import com.dfc.moneymartus.view.activity.CheckCashingDetailsActivity;
import com.dfc.moneymartus.view.activity.HomeActivity;
import com.dfc.moneymartus.view.activity.UploadingCheckDetailsActivity;
import com.dfc.moneymartus.view.adapter.MyChecksAdapter;
import com.dfc.moneymartus.view.callback.IGetCheckCallback;

@SuppressLint("HandlerLeak")
public class MyChecksFragment extends BaseFragment implements OnItemClickListener, IGetCheckCallback, OnClickListener, IViewCallbackListner {//, IViewCallbackListner{

	private static final int EMPTY_MESSAGE = 0;
	private static final int SHOW_CHK_LIST_DATA = 1;
	@InjectView(R.id.my_checks_list)
	ListView mMyChecksList;
	@InjectView(R.id.btn_all)
	Button mBtnAll;
	@InjectView(R.id.btn_pending)
	Button mBtnPending;
	@InjectView(R.id.btn_cashed)
	Button mBtnCashed;
	@InjectView(R.id.btn_rejected)
	Button mBtnRejected;
	@InjectView(R.id.v_divider_all)
	View mViewDividerAll;
	@InjectView(R.id.v_divider_pending)
	View mViewDividerPending;
	@InjectView(R.id.v_divider_cashed)
	View mViewDividerCashed;
	@InjectView(R.id.v_divider_rejected)
	View mViewDividerRejected; 

	private Activity mContext;
	private Preferences mPreferences;
//	public static ArrayList<MyChecksDataObject> mMyChecksListItems;
	private MyChecksAdapter mMyChecksAdapter;
	private ChecksHandler mCheckHandler;
	private Button mBtnLastSelection;
	ArrayList<ChecksObject> mSelectedTabData = new ArrayList<ChecksObject>();
	//private String TAG = "MyChecksFragment";

	private CheckByIdHandler mChkByIdHandler;
	private int mSelectedListPosition;
	private View mSelectedListView;

	private CreateSubmitCheckReceiver mReceiver;

	private String mFrontCheck;
	private String mBackCheck;
	private double mCheckAmount;
	private String mCheckMadeDate;
	private String mFundingMechanismId;
	private Bundle mBundle;
	private IntentFilter mFilter;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.fragments.BaseFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		mContext = getActivity();
		mPreferences = Preferences.getInstance(mContext);
		setHasOptionsMenu(true);
		setUpActionBar();
		mCheckHandler = new ChecksHandler(this);
		mFilter = new IntentFilter(CreateSubmitCheckReceiver.PROCESS_RESPONSE);
		mFilter.addCategory(Intent.CATEGORY_DEFAULT);
		mReceiver = new CreateSubmitCheckReceiver();
		boolean isStateEnabled  = ModelData.getInstance().isEnabled;
//		mSelectedTabData = Moneymart.getInstance().myChecksList;
		if((isStateEnabled && (getArguments() == null || Constants.fetchListFromServer == true)) && Moneymart.getInstance().myCheckUploadQueue.size() == 0)
		{
			processGetChecks();
		}
	}

	/**
	 * Used to set action bar title
	 */
	private void setUpActionBar() {
		Util.setFragmentActionBarTitle((BaseActivity)getActivity(), getResources().getString(R.string.navigation_menu_my_checks));
	}

	/**
	 * Used to set event type to API call
	 */
	private void processGetChecks() {

		mCheckHandler.setReqEventType(EventTypes.EVENT_GET_CHECKS);
		processRequest(mCheckHandler);
	}

	/**
	 * Used to send API call to server and loading
	 * @param dataObject
	 */
	public void processRequest(DataObject dataObject)
	{
		showLoader(getResources().getString(R.string.pbar_text_loading));//("Loading...");
		mCheckHandler.requestChecks();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstance) {
		// Inflate the layout for this fragment
		mBundle = getArguments();
		if (mBundle != null) {
			mCheckAmount = mBundle.getDouble(Constants.CHECK_AMOUNT);
			mFrontCheck = mBundle.getString(Constants.CHECK_FRONT_IMAGE);
			mBackCheck = mBundle.getString(Constants.CHECK_BACK_IMAGE);
			mCheckMadeDate = mBundle.getString(Constants.CHECK_MADE_DATE);
			mFundingMechanismId = mBundle.getString(Constants.CHECK_FUNDINGTO);
			((HomeActivity)getActivity()).setMyCheckObjectForUpload(null);
		}

		View mRootView = inflater.inflate(R.layout.my_checks,container,false);
		initUI(mRootView);
		return mRootView;

	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(final Bundle savedInstance) {
		super.onActivityCreated(savedInstance);
		setRetainInstance(true);

		getView().setFocusableInTouchMode(true);

		getView().setOnKeyListener( new OnKeyListener()
		{
			@Override
			public boolean onKey( View v, int keyCode, KeyEvent event )
			{
				if( keyCode == KeyEvent.KEYCODE_BACK )
				{
					getActivity().finish();
					return true;
				}
				return false;
			}
		} );
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		mContext = null;
	}

	/**
	 * Used to initialize UI components
	 * @param view Used for UI components
	 */
	private void initUI(View view) {
		ButterKnife.inject(this, view);
		mMyChecksList.setOnItemClickListener(this);

		TextView tvEmptyView = (TextView) view
				.findViewById(R.id.tv_empty_text);
		mMyChecksList.setEmptyView(tvEmptyView);

		showAllChecks();
		final boolean isStateEnabled  = ModelData.getInstance().isEnabled;

		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
		if(!isStateEnabled){
			mSwipeRefreshLayout.setEnabled(false);
		}

		/**
		 * Used to pull refresh where app will check network availability and send then API call to get the checks
		 */
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(Moneymart.getInstance().myCheckUploadQueue.size() == 0){
					if(Util.isNetworkAvailable(getActivity())){
						refreshContent();
					} else{
						refreshContentNetworkNotAvailable();
						showAlertDialog(getResources().getText(R.string.error_network).toString(), MyChecksFragment.this);//("Network not available.", this);
					}
				}
				else{
					refreshContentNetworkNotAvailable();
				}
			}
		});

		if(isStateEnabled){
			mMyChecksList.setOnScrollListener(new AbsListView.OnScrollListener() {  
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					//TODO Auto generated
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					int topRowVerticalPosition = 
							mMyChecksList == null || mMyChecksList.getChildCount() == 0 ? 
									0 : mMyChecksList.getChildAt(0).getTop();

					mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
				}
			});
		}
	}

	/**
	 * On pull to refresh app will show UI of pull to refresh and verify network, if it's available it will send api call to fetch check details
	 */
	private void refreshContent(){
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
				processGetChecks();

			}
		});
	}

	/**
	 * On pull to refresh app will show UI of pull to refresh and verify network if it's not available it will not take any action
	 */
	private void refreshContentNetworkNotAvailable(){
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		System.out.println("onResume : MyChecks");
		getActivity().registerReceiver(mReceiver, mFilter);
		mPreferences.addOrUpdateBoolean(PreferenceConstants.IS_SERVICE_IN_BACKGROUND, false);

		if(mMyChecksAdapter!=null){
			mMyChecksAdapter.notifyDataSetChanged();
		}
		setUpActionBar();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
		mPreferences.addOrUpdateBoolean(PreferenceConstants.IS_SERVICE_IN_BACKGROUND, true);
	}

	/**
	 * On click of all tab all will show all the checks
	 */
	@OnClick(R.id.btn_all)
	public void showAllChecks(){
		mBtnAll.setSelected(true);
		mViewDividerAll.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		restButtonState(mBtnLastSelection);
		resetDividerState(mViewDividerAll, mViewDividerPending, mViewDividerCashed, mViewDividerRejected);
		mBtnLastSelection = mBtnAll;
		/*if(Constants.myCheckServerData != null 
				&& Constants.myCheckServerData.getAlCheckObj() != null 
				&& Constants.myCheckServerData.getAlCheckObj().size() > 0){
			showCheckData(Constants.myCheckServerData);
		}*/

		if(Moneymart.getInstance().myChecksList.size() > 0){
			setAdapter(Moneymart.getInstance().myChecksList);
		}
		
		if(mBundle!=null){
			addQueuedCheck();
			mBundle=null;
		}
	}

	/**
	 *  On click of pending tab all will show only pending checks
	 */
	@OnClick(R.id.btn_pending)
	public void showPendingChecks(){
		mBtnPending.setSelected(true);
		mViewDividerPending.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		restButtonState(mBtnLastSelection);
		resetDividerState(mViewDividerPending, mViewDividerAll, mViewDividerCashed, mViewDividerRejected);
		mBtnLastSelection = mBtnPending;
		showCheckData(Constants.myCheckServerData);
	}

	/**
	 * On click of pending tab all will show only cashed checks
	 */
	@OnClick(R.id.btn_cashed)
	public void showCashedChecks(){
		mBtnCashed.setSelected(true);
		mViewDividerCashed.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		restButtonState(mBtnLastSelection);
		resetDividerState(mViewDividerCashed, mViewDividerAll, mViewDividerPending, mViewDividerRejected);
		mBtnLastSelection = mBtnCashed;
		showCheckData(Constants.myCheckServerData);
	}

	/*
	 * On click of pending tab all will show only rejected checks
	 */
	@OnClick(R.id.btn_rejected)
	public void showRejectedChecks(){
		mBtnRejected.setSelected(true);
		mViewDividerRejected.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		restButtonState(mBtnLastSelection);
		resetDividerState(mViewDividerRejected, mViewDividerAll, mViewDividerPending, mViewDividerCashed);
		mBtnLastSelection = mBtnRejected;
		showCheckData(Constants.myCheckServerData);
	}

	/**
	 * Used to reset tab buton state
	 * @param btn
	 */
	private void restButtonState(Button btn) {
		if(btn!=null)
			btn.setSelected(false);
	}

	/**
	 * Used to set tab bottom line divider color
	 * @param v1 All tab divider color
	 * @param v2 Pending tab divider color
	 * @param v3 Cashed tab divider color
	 * @param v4 Rejected tab divider color
	 */
	private void resetDividerState(View v1, View v2, View v3, View v4) {
		v1.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_action_bar)));
		v2.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_white)));
		v3.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_white)));
		v4.setBackgroundColor(Color.parseColor(getResources().getString(R.color.color_white)));
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mSelectedListPosition = position;
		mSelectedListView = view;
		/**
		 * On click of list item app will check is app is verify check status is not uploading, not upload failed or not queued 
		 * then it will send check details API call and then navigate to details screen.
		 * If check status is uploading,upload failed or queued else it will navigate to details screen without calling api
		 */
		int selectedItemStatusCode = 0;
		if(mBtnAll.isSelected()){
			selectedItemStatusCode = Integer.parseInt(Moneymart.getInstance().myChecksList.get(position).getStatusCode());
		}
		else{
			selectedItemStatusCode = Integer.parseInt(mSelectedTabData.get(position).getStatusCode());
		}
		
		if(selectedItemStatusCode == Constants.CheckStatus.UPLOADING || selectedItemStatusCode == Constants.CheckStatus.UPLOADING_FAILED ||
				selectedItemStatusCode == Constants.CheckStatus.QUEUED){
			navigateToDetailScreen(mSelectedListPosition, view, selectedItemStatusCode);
		}
		else{

			if(!Util.isNetworkAvailable(getActivity())){
				refreshContentNetworkNotAvailable();
				showAlertDialog(getResources().getText(R.string.error_network).toString(), MyChecksFragment.this);//("Network not available.", this);
				return;
			}

			mChkByIdHandler = new CheckByIdHandler(MyChecksFragment.this);
			if(mBtnAll.isSelected()){
				mChkByIdHandler.setRequestCheckID(Moneymart.getInstance().myChecksList.get(position).getChequeId());
			}
			else{
				mChkByIdHandler.setRequestCheckID(mSelectedTabData.get(position).getChequeId());
			}
			processCheckByIDService();
		}
	}

	/**
	 * Method is used to get which action is required on click of list item based on it will navigate to respective screen.
	 * @param position Position of check in the list 
	 * @return Check action
	 */
	private int getActionRequired(int position) {
		int pos = 0;
		
		ChecksObject ck = null;
		
		if(mBtnAll.isSelected()){
			ck = Moneymart.getInstance().myChecksList.get(position);
		}
		else{
			ck = mSelectedTabData.get(position);
		}
		
		if (ck.getActionRequired().size() == 1) {
			if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit cheque image front")){
				pos = 11;
			}else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit cheque image back")){
				pos = 12;
			}else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Submit voided cheque image front")){
				pos = 13;
			}else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit voided cheque image front")){
				pos = 14;
			}else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Contact customer services")){
				pos = 15;
			}else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Agree to terms")){
				pos = 16;
			}else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Agree to fees")){
				pos = 17;
			}
		} else if (ck.getActionRequired().size() == 2){
			if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit cheque image front") 
					&& ck.getActionRequired().get(1).getAction().equalsIgnoreCase("Resubmit cheque image back") ){
				pos = 18;
			} else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Submit voided cheque image front")){
				pos = 13;
			}else if(ck.getActionRequired().get(0).getAction().equalsIgnoreCase("Resubmit voided cheque image front")) {
				pos = 13;	
			}
		}
		return pos;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.IGetCheckCallback#onGetChecksFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onGetChecksFailure(VolleyError error, int eventType) {
		hideLoader();
		int statusCode = ErrorHandling.getStatusCode(error);
		String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
		if(statusCode == 401)
		{
			showAlertDialogUnAuthorised(mContext, errorMessage, this);
		} else {
			showAlertDialog(errorMessage, this);
		}

		if(mMyChecksAdapter != null) {
			mMyChecksAdapter = null;
			mMyChecksList.setAdapter(null);
		}


	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.view.callback.IGetCheckCallback#onGetChecksSuccess(com.dfc.moneymartus.dto.CheckDataObject)
	 */
	@Override
	public void onGetChecksSuccess(CheckDataObject dataObject) {

		if(dataObject != null) {
			hideLoader();
			Message msg = new Message();
			if(dataObject.getAlCheckObj() != null && !dataObject.getAlCheckObj().isEmpty()) {
				Collections.sort(dataObject.getAlCheckObj(), dataObject.dateComparator);
				for(int i = 0; i < dataObject.getAlCheckObj().size(); i++){
					getCheckStatus(dataObject, i);
				}
			} 
			if (dataObject.getUarList() != null && !dataObject.getUarList().isEmpty()) {
				Collections.sort(dataObject.getUarList(), dataObject.dateComparator);
			}
			dataObject.getAlCheckObj().addAll(0, dataObject.getUarList());
			//Constants.myCheckServerData = dataObject;
			Constants.setMyCheckServerData(dataObject);
			msg.what = SHOW_CHK_LIST_DATA;
			msg.obj = dataObject;
			uiHandler.sendMessage(msg);
		}

		hideLoader();
	}

	/**
	 * This method is used to sort data based on status like cashed, rejected and UAR and waitng for review.
	 * Based on status check details will be stored in respective list array
	 * @param dataObject Check data
	 * @param position Check position
	 */
	//	private void getCheckStatus(int statusCode, MMChecksObject mmChecksObject) {
	private void getCheckStatus(CheckDataObject dataObject, int position) {
		int statusCode = Integer.parseInt(dataObject.getAlCheckObj().get(position).getStatusCode());

		switch (statusCode) {
		case Constants.CheckStatus.WAITING_FOR_REVIEW :
			dataObject.alPendingList.add(dataObject.getAlCheckObj().get(position));
			break;

		case Constants.CheckStatus.USER_ACTION_REQUIRED :
			break;

		case Constants.CheckStatus.CASHED :
			dataObject.alCashingList.add(dataObject.getAlCheckObj().get(position));
			break;

		case Constants.CheckStatus.REJECTED :
			dataObject.alRejectedList.add(dataObject.getAlCheckObj().get(position));
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btn_right) {
			mDialog.dismiss();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		boolean isStateEnabled  = ModelData.getInstance().isEnabled;
		inflater.inflate(R.menu.add_check_menu, menu);
		MenuItem item = menu.findItem(R.id.action_add);
		if(!isStateEnabled){
			item.setVisible(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == R.id.action_add) {
			((HomeActivity)getActivity()).goToCheckCashingFragment();
		}
		return true;	
	}

	/**
	 *  If check list is empty app will show empty check message else will load check list
	 */
	private final Handler uiHandler = new Handler() {
		public void handleMessage(Message message) {

			switch (message.what) {
			case EMPTY_MESSAGE:

				hideLoader();
				CheckDataObject emptyMsgDataObject = (CheckDataObject) message.obj;
				showDialog(emptyMsgDataObject);
				break;

			case SHOW_CHK_LIST_DATA:
				hideLoader();
				CheckDataObject dataObject = (CheckDataObject) message.obj;
				showCheckData(dataObject);
				break;
			default:
				break;
			}
		}
	};

	public void showDialog(CheckDataObject dataObject) {
		showAlertDialog(dataObject.getMessage(), this);
	}

	/**
	 * Based on user tab selection app will load check list
	 * @param dataObject Check list data object
	 */
	protected void showCheckData(CheckDataObject dataObject) {

		if (dataObject != null) {
			if(mBtnAll.isSelected())
			{
				if(Moneymart.getInstance().myCheckUploadQueue.size()==0){
					setAdapter(null);
					mSelectedTabData = dataObject.getAlCheckObj();
					Moneymart.getInstance().myChecksList.clear();
					for(TreeMap.Entry<String, ChecksObject> entry : Moneymart.getInstance().failedChecksList.entrySet()) {
						String key= null;
						key = entry.getKey();
						System.out.println("Saving failed at : "+ Integer.parseInt(key));
						Moneymart.getInstance().myChecksList.add(entry.getValue());
					}
					Moneymart.getInstance().myChecksList.addAll(Moneymart.getInstance().myChecksList.size(), dataObject.getAlCheckObj());
					}
				else{
					mSelectedTabData = new ArrayList<ChecksObject>();
					mSelectedTabData.addAll(Moneymart.getInstance().myChecksList);
					
				}
				setAdapter(Moneymart.getInstance().myChecksList);

			} else if(mBtnCashed.isSelected())
			{
				mSelectedTabData = dataObject.getCashingList();
				setAdapter(dataObject.getCashingList());

			} else if(mBtnPending.isSelected())
			{
				mSelectedTabData = dataObject.getPendingList();
				setAdapter(dataObject.getPendingList());

			} else if(mBtnRejected.isSelected())
			{
				mSelectedTabData = dataObject.getRejectedList();
				setAdapter(dataObject.getRejectedList());
			}
		} 
	}

	/**
	 * Used to check list row data and refresh list data
	 * @param listData array of check data
	 */
	private void setAdapter(ArrayList<ChecksObject> listData) {
		if(mMyChecksAdapter != null) {
			mMyChecksAdapter = null;
			mMyChecksList.setAdapter(null);
		}

		//		Mocked the status for 008 user to UAR to check void Check Capture.
		//		listData.get(3).setStatusCode(String.valueOf(MMConstants.CheckStatus.UPLOADING_FAILED));
		if(mContext!=null){
			((HomeActivity)mContext).setMyChecksListData(listData);
		}

		mMyChecksAdapter = new MyChecksAdapter(getActivity(), listData);
		mMyChecksList.setAdapter(mMyChecksAdapter);
		mMyChecksAdapter.notifyDataSetChanged();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle intentBundle = null;
		if(data!=null){
			intentBundle =data.getExtras();
		}
		/**
		 * If check status is resubmit front check image then app will navigate to check details screen
		 */
		if(intentBundle!=null && intentBundle.containsKey(Constants.mActionRequiredStatus))
		{
			Intent i = new Intent(mContext, CheckCashingDetailsActivity.class);

			i.putExtra(Constants.mActionRequiredStatus, Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT);
			i.putExtra("check_id", data.getStringExtra(Constants.CHECK_ID));
			i.putExtra(Constants.CHECK_FUNDINGTO, data.getStringExtra(Constants.CHECK_FUNDINGTO));
			i.putExtra(Constants.CHECK_FUNDINGTO_TYPE, data.getStringExtra(Constants.CHECK_FUNDINGTO_TYPE));
			i.putExtras(intentBundle);

			startActivity(i);

		}

		/**
		 * If check status is submit void check image or/and agree to fees then app will navigate agree to fees screen
		 */
		if(requestCode == Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT || requestCode == Constants.MMActionRequired.AGREE_TO_FEES){
			((HomeActivity) mContext).setMyCheckObjectForUpload(null);
			Constants.setMyCheckServerData(null);
			Constants.setFetchListFromServer(true);
			((HomeActivity) mContext).goToMyChecksFragment();

		}
	}

	/**
	 * Used to add queued check into the list
	 */
	private void addQueuedCheck(){
		ChecksObject queueObj = new ChecksObject();
		queueObj.setStatusCode(String.valueOf(Constants.CheckStatus.QUEUED));
		queueObj.setAmount(mCheckAmount);
		queueObj.setDateMade(mCheckMadeDate);
		queueObj.setFundingMechanism(mFundingMechanismId);

		MyChecksDataObject uploadItem = new MyChecksDataObject();
		uploadItem.setmFrontCheckImage(mFrontCheck);
		uploadItem.setmBackCheckImage(mBackCheck);
		uploadItem.setmCheckAmount(mCheckAmount);
		uploadItem.setmCheckMadeDate(mCheckMadeDate);
		uploadItem.setmFundingMechanismsId(mFundingMechanismId);

		int maxQueueId = 0;
		for(int i = 0; i < Moneymart.getInstance().myChecksList.size(); i++){
			ChecksObject checkObj = Moneymart.getInstance().myChecksList.get(i);
			if(checkObj.getQueuedActionId() > maxQueueId){
				maxQueueId = checkObj.getQueuedActionId();
			}

		}
		queueObj.setQueuedActionId(maxQueueId + 1);

		Moneymart.getInstance().myCheckUploadQueue.put(String.valueOf(queueObj.getQueuedActionId()), uploadItem);
		System.out.println("Queue Id : "+ String.valueOf(queueObj.getQueuedActionId()) + "added to queue");

		GetCheckDataObject.LinksCls linkCLS = new GetCheckDataObject().new LinksCls();
		GetCheckDataObject.CheckImageFrontCls checkImageFrontCls = new GetCheckDataObject().new CheckImageFrontCls();
		checkImageFrontCls.rel = mFrontCheck;
		GetCheckDataObject.CheckImageBackCls checkImageBackCls = new GetCheckDataObject().new CheckImageBackCls();
		checkImageBackCls.rel = mBackCheck;
		linkCLS.chequeImageFront = checkImageFrontCls;
		linkCLS.chequeImageBack = checkImageBackCls;
		queueObj.set_links(linkCLS);
		queueObj.setFundingMechanism(mFundingMechanismId);


		if(mMyChecksAdapter==null){
			Moneymart.getInstance().myChecksList.add(queueObj);
			mMyChecksAdapter = new MyChecksAdapter(getActivity(), Moneymart.getInstance().myChecksList);
		}
		else if(Moneymart.getInstance().myChecksList.size()==0){
			Moneymart.getInstance().myChecksList.add(queueObj);
		}
		else
			Moneymart.getInstance().myChecksList.add(0, queueObj);

		mMyChecksList.setAdapter(mMyChecksAdapter);
		mMyChecksAdapter.notifyDataSetChanged();
		addNextCheckToUpload(queueObj.getQueuedActionId(), 0);
	}

	/**
	 * Generate API request and form data to submit check image
	 * @param position Check position
	 */
	public void submitCapturedCheckImage(int position) {

		try{
			Intent msgIntent = new Intent(mContext, CreateSubmitRequestService.class);
			Bundle b = new Bundle();
			b.putString(CreateSubmitRequestService.JSON_BODY, prepareJson(position).toString());
			b.putString(CreateSubmitRequestService.FRONT_CHECK_BASE64, "");
			b.putString(CreateSubmitRequestService.BACK_CHECK_BASE64, "");
			b.putString(CreateSubmitRequestService.UPLOAD_QUEUE_ID, String.valueOf(position));

			msgIntent.putExtras(b);
			mContext.startService(msgIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prepare json response body and image to send data to server
	 * @param position Check position 
	 * @return json object holding check details
	 */
	public JSONObject prepareJson(int position){
		JSONObject jo = new JSONObject();
		try {
			jo.put("chequeId", null);
			jo.put("chequeMaker", null);
			jo.put("chequeNumber", null);
			jo.put("amount", Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(position)).getmCheckAmount());
			jo.put("amountCurrency", "USD");
			jo.put("dateMade", Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(position)).getmCheckMadeDate());
			jo.put("fees", null);
			jo.put("feesCurrency", null);
			jo.put("fundingMechanism", Moneymart.getInstance().myCheckUploadQueue.get(String.valueOf(position)).getmFundingMechanismsId());
			jo.put("feesAgreed", null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}

	/**
	 * Used to handle queued state. This will add queued check to upload list
	 * @param queueId
	 * @param position
	 */
	public void addNextCheckToUpload(int queueId, int position){
		if(Moneymart.getInstance().myCheckUploadQueue.size() == 1){
			Moneymart.getInstance().myChecksList.get(position).setStatusDescription("Uploading");
			Moneymart.getInstance().myChecksList.get(position).setStatusCode(String.valueOf(Constants.CheckStatus.UPLOADING));
			Moneymart.getInstance().myChecksList.get(position).setProgressUpdateRequired(true);
			mMyChecksList.setAdapter(mMyChecksAdapter);
			mMyChecksAdapter.notifyDataSetChanged();
			submitCapturedCheckImage(queueId);
		}
	}

	public void refreshOrFetchListData(){

		if(mMyChecksAdapter!=null && Moneymart.getInstance().myChecksList.size()>0){
			mMyChecksAdapter.notifyDataSetChanged();
		}

		if(Moneymart.getInstance().myCheckUploadQueue.size() == 0){
			mPreferences.addOrUpdateBoolean(PreferenceConstants.IS_UPLOAD_SERVICE_RUNNING, false);
			showLoader();
			processGetChecks();
			Moneymart.getInstance().myCheckUploadQueue.clear();
		}

	}

	/**
	 * Once check upload it will send notification to app. So that even if is not in same screen it will update the satus of check
	 *
	 */
	public class CreateSubmitCheckReceiver extends BroadcastReceiver{

		public static final String PROCESS_RESPONSE = "com.dfc.moneymart.action.PROCESS_RESPONSE";

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("BroadCast : Inside onReceive");
			refreshOrFetchListData();
		}
	}

	/**
	 * Used to create array of upload failed check list
	 * @param queueId Check id
	 * @return index of check
	 */
	public int findUploadFailedItemInList(int queueId){
		int queueIndex = 0;
		Iterator<ChecksObject> checkIterator  = Moneymart.getInstance().myChecksList.iterator();
		while(checkIterator.hasNext()){
			ChecksObject checkObj = checkIterator.next();
			if(String.valueOf(checkObj.getQueuedActionId()).equals(String.valueOf(queueId))){
				break;
			}
			queueIndex++;
		}
		return queueIndex;
	}

	public String findCheckObjectInFailedList(ChecksObject checkObject){

		String queueIndex = null;
		for(TreeMap.Entry<String, ChecksObject> entry : Moneymart.getInstance().failedChecksList.entrySet()) {
			ChecksObject value= null;
			value = entry.getValue();
			if(value.equals(checkObject)){
				queueIndex = entry.getKey();
				break;
			}
		}
		return queueIndex;
	}

	public void performOnItemClick(int position){
		navigateToDetailScreen(position, null, -1);
	}

	/**
	 * Used to send check by ID api call request
	 */
	private void processCheckByIDService()
	{
		showLoader();
		mChkByIdHandler.setReqEventType(EventTypes.EVENT_CHECK_BY_ID);
		processCheckByIdRequest(mChkByIdHandler);
	}

	public void processCheckByIdRequest(DataObject dataObject)
	{
		dataObject.register(Method.GET);
	}


	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewSuccess()
	 */
	@Override
	public void onViewSuccess() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewFailure(com.android.volley.VolleyError, int)
	 */
	@Override
	public void onViewFailure(VolleyError error, int eventType) {
		hideLoader();
		if(eventType == EventTypes.EVENT_CHECK_BY_ID) {
			int statusCode = ErrorHandling.getStatusCode(error);
			String errorMessage = ErrorHandling.getErrorMessage(mContext, statusCode, eventType);
			if(statusCode == 401)
			{
				showAlertDialogUnAuthorised(mContext, errorMessage, this);
			} else {
				showAlertDialog(errorMessage, this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dfc.moneymartus.gateway.IViewCallbackListner#onViewSuccess(com.dfc.moneymartus.business.DataObject)
	 */
	@Override
	public void onViewSuccess(DataObject dataObject) {
		if(dataObject.getEventType() == EventTypes.EVENT_CHECK_BY_ID) {

			mChkByIdHandler = (CheckByIdHandler) dataObject;
			String mCheckStatusCode = mChkByIdHandler.getCheckObj().getStatusCode();
			
			if(mBtnAll.isSelected()){
				Moneymart.getInstance().myChecksList.remove(mSelectedListPosition);
				Moneymart.getInstance().myChecksList.add(mSelectedListPosition, mChkByIdHandler.getCheckObj());
				Moneymart.getInstance().myChecksList.get(mSelectedListPosition).setStatusCode(mCheckStatusCode);
			}
			else{
				mSelectedTabData.remove(mSelectedListPosition);
				mSelectedTabData.add(mSelectedListPosition, mChkByIdHandler.getCheckObj());
				mSelectedTabData.get(mSelectedListPosition).setStatusCode(mCheckStatusCode);
			}
			
			mMyChecksAdapter.notifyDataSetChanged();
			navigateToDetailScreen(mSelectedListPosition, mSelectedListView, Integer.parseInt(mCheckStatusCode));
		}
		hideLoader();

	}

	/**
	 * Used to navigate detail screen on click of list item
	 * @param position Check position in list
	 * @param view UI component
	 * @param statusCode Check status
	 */
	private void navigateToDetailScreen(int position, View view, int statusCode){
		int iSelectedStatusCode = statusCode;

		String checkStatusCode = null;
		String checkId = null;
		
		if(statusCode == -1){
			iSelectedStatusCode = Integer.parseInt(Moneymart.getInstance().myChecksList.get(position).getStatusCode());
		}

		if(mBtnAll.isSelected()){
			checkStatusCode = Moneymart.getInstance().myChecksList.get(position).getStatusCode();
			checkId = Moneymart.getInstance().myChecksList.get(position).getChequeId(); 
		}
		else{
			checkStatusCode = mSelectedTabData.get(position).getStatusCode();
			checkId = mSelectedTabData.get(position).getChequeId();
		}
		
		switch (iSelectedStatusCode) {
		case Constants.CheckStatus.WAITING_FOR_REVIEW :
			Intent i = new Intent(mContext, CheckCashingDetailsActivity.class);
			i.putExtra(Constants.mActionRequiredStatus, iSelectedStatusCode);
			i.putExtra("check_id", checkId);
			attachCheckDetailsToIntent(i);
			startActivity(i);

			break;
		case Constants.CheckStatus.USER_ACTION_REQUIRED :
			//			Mocked to check Void check capture for 008 user.
			//			int actionStatus = MMConstants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT; //getActionRequired(position);
			int actionStatus = getActionRequired(position);
			switch (actionStatus) {
			case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT:
			case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_BACK:
			case Constants.MMActionRequired.RESUBMIT_CHEQUE_IMAGE_FRONT_BACK:
			case Constants.MMActionRequired.RESUBMIT_VOIDED_CHEQUE_IMAGE_FRONT:
				Intent intnt = new Intent(mContext, CheckCashingDetailsActivity.class);
				intnt.putExtra(Constants.mActionRequiredStatus, actionStatus);
				intnt.putExtra("check_id", checkId);
				attachCheckDetailsToIntent(intnt);
				startActivity(intnt);
				break;

			case Constants.MMActionRequired.SUBMIT_VOIDED_CHEQUE_IMAGE_FRONT:
			case Constants.MMActionRequired.AGREE_TO_FEES:
				Intent intent = new Intent(getActivity().getBaseContext(), AgreeToFeeActivity.class);
				intent.putExtra("check_id", checkId);
				intent.putExtra("action_status", Integer.parseInt(checkStatusCode));
				attachCheckDetailsToIntent(intent);
				startActivityForResult(intent, actionStatus);

				break;

				/* This is required in future	
				 * case Constants.MMActionRequired.CONTACT_CUSTOMER_SERVICES:

				break;
			case Constants.MMActionRequired.AGREE_TO_TERMS:

				break;*/
			default:
				break;
			}

			break;

		case Constants.CheckStatus.CASHED :
		case Constants.CheckStatus.REJECTED :
			Intent intent = new Intent(getActivity().getBaseContext(), CashedRejectActivity.class);
			intent.putExtra("check_id", checkId);
			intent.putExtra("action_status", Integer.parseInt(checkStatusCode));
			attachCheckDetailsToIntent(intent);
			startActivity(intent);
			break;

		case Constants.CheckStatus.UPLOADING_FAILED:

			if(view==null){

				int tryAgainQueueId = Moneymart.getInstance().myChecksList.get(position).getQueuedActionId();
				int uploadFailedIndex = findUploadFailedItemInList(tryAgainQueueId);

				ChecksObject checkListObject = Moneymart.getInstance().myChecksList.get(uploadFailedIndex);
				MyChecksDataObject myChecksDataObject = new MyChecksDataObject();
				myChecksDataObject.setmCheckAmount(checkListObject.getAmount());
				myChecksDataObject.setmFrontCheckImage(checkListObject.get_links().chequeImageFront.rel);
				myChecksDataObject.setmBackCheckImage(checkListObject.get_links().chequeImageBack.rel);
				myChecksDataObject.setmCheckMadeDate(checkListObject.getDateMade());
				myChecksDataObject.setmFundingMechanismsId(checkListObject.getFundingMechanism());
				

				Moneymart.getInstance().myCheckUploadQueue.put(String.valueOf(tryAgainQueueId) , myChecksDataObject);
				System.out.println("Queue Id try again : "+ tryAgainQueueId + "added to queue");

				if(Moneymart.getInstance().myCheckUploadQueue.size() == 1){
					Moneymart.getInstance().myChecksList.get(uploadFailedIndex).setStatusDescription("Uploading");
					Moneymart.getInstance().myChecksList.get(uploadFailedIndex).setStatusCode(String.valueOf(Constants.CheckStatus.UPLOADING));
					Moneymart.getInstance().myChecksList.get(uploadFailedIndex).setProgressUpdateRequired(true);
					mMyChecksAdapter.notifyDataSetChanged();
					submitCapturedCheckImage(tryAgainQueueId);
				}
				else{
					Moneymart.getInstance().myChecksList.get(uploadFailedIndex).setStatusDescription("Queued");
					Moneymart.getInstance().myChecksList.get(uploadFailedIndex).setStatusCode(String.valueOf(Constants.CheckStatus.QUEUED));
					mMyChecksAdapter.notifyDataSetChanged();
				}
			}
			else{
				Moneymart.getInstance().myCheckUploadQueue.remove(String.valueOf(Moneymart.getInstance().myChecksList.get(position).getQueuedActionId()));
				String key = null;
				key = findCheckObjectInFailedList(Moneymart.getInstance().myChecksList.get(position));
				if(key!=null && Moneymart.getInstance().failedChecksList.containsKey(key)){
					Moneymart.getInstance().failedChecksList.remove(key);
				}

				Moneymart.getInstance().myChecksList.remove(position);
				((HomeActivity)getActivity()).setMyCheckObjectForUpload(null);
				((HomeActivity)getActivity()).goToCheckCashingFragment();
			}
			break;

		case Constants.CheckStatus.QUEUED :
			Moneymart.getInstance().myCheckUploadQueue.remove(String.valueOf(Moneymart.getInstance().myChecksList.get(position).getQueuedActionId()));

			String key = null;
			key = findCheckObjectInFailedList(Moneymart.getInstance().myChecksList.get(position));
			if(key!=null && Moneymart.getInstance().failedChecksList.containsKey(key)){
				Moneymart.getInstance().failedChecksList.remove(key);
			}

			Moneymart.getInstance().myChecksList.remove(position);
			mMyChecksAdapter.notifyDataSetChanged();
			((HomeActivity)getActivity()).goToCheckCashingFragment();

			break;
		case Constants.CheckStatus.UPLOADING:

			Intent uploadIntent = new Intent(getActivity().getBaseContext(), UploadingCheckDetailsActivity.class);
			//Base64 is too long too parse throwing exception. Hence added check details in constants instead of put extras 
			Constants.setUPLOADING_STATE_CHECKBYID_OBJECT(Moneymart.getInstance().myChecksList.get(position));
			startActivity(uploadIntent);
			break;

		default:
			break;
		}
	}

	/**
	 * Used to send check details to next intent
	 * @param intent 
	 */
	private void attachCheckDetailsToIntent(Intent intent){
		Bundle iBundleCCDetailActivity = new Bundle();
		iBundleCCDetailActivity.putString(Constants.CHECK_ID, mChkByIdHandler.getRequestCheckID());
		iBundleCCDetailActivity.putString(Constants.CHECK_MAKER_NAME, mChkByIdHandler.getCheckObj().getChequeMaker());
		iBundleCCDetailActivity.putString(Constants.CHECK_MADE_DATE, mChkByIdHandler.getCheckObj().getDateMade());
		iBundleCCDetailActivity.putString(Constants.CHECK_NUMBER, String.valueOf(mChkByIdHandler.getCheckObj().getChequeNumber()));
		iBundleCCDetailActivity.putString(Constants.CHECK_AMOUNT, mChkByIdHandler.getCheckObj().getAmount()+"");
		iBundleCCDetailActivity.putString(Constants.CHECK_FUNDINGTO, mChkByIdHandler.getCheckObj().getFundingMechanism());
		iBundleCCDetailActivity.putString(Constants.CHECK_DATE_SUBMIT, mChkByIdHandler.getCheckObj().getDateSubmitted());
		iBundleCCDetailActivity.putString(Constants.CHECK_DATE_COMPLETE, mChkByIdHandler.getCheckObj().getDateCompleted());
		iBundleCCDetailActivity.putString(Constants.CHECK_FEES, mChkByIdHandler.getCheckObj().getFees()+"");
		iBundleCCDetailActivity.putString(Constants.CHECK_FEES_AGREED, mChkByIdHandler.getCheckObj().getFeesAgreed()); 
		iBundleCCDetailActivity.putString(Constants.CHECK_AMOUNT_CURRENCY ,mChkByIdHandler.getCheckObj().getAmountCurrency());
		iBundleCCDetailActivity.putString(Constants.CHECK_FEES_CURRENCY ,mChkByIdHandler.getCheckObj().getFeesCurrency());
		iBundleCCDetailActivity.putString(Constants.CHECK_FRONT_IMAGE_URL ,mChkByIdHandler.getCheckObj().getFrontImageUrl());
		iBundleCCDetailActivity.putString(Constants.CHECK_BACK_IMAGE_URL ,mChkByIdHandler.getCheckObj().getBackImageUrl());
		iBundleCCDetailActivity.putString(Constants.CHECK_VOID_IMAGE_URL ,mChkByIdHandler.getCheckObj().getVoidImageUrl());
		intent.putExtras(iBundleCCDetailActivity);
	}

}