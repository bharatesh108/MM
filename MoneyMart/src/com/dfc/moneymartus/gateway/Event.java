package com.dfc.moneymartus.gateway;

import com.dfc.moneymartus.infra.EventTypes;

public class Event extends EventTypes{
	private int mIntEventType;
	private Object mEventObj;

	/**
	 * Constructor
	 * @param object : Event Object
	 * @param eventType : Type of the event
	 */
	public Event(Object object, int eventType) {

		this.setEventObj(object);
		this.setEventType(eventType);
	}
	/**
	 * Set event type
	 * @param eventType the eventType to set
	 */
	public void setEventType(int eventType) {

		this.mIntEventType = eventType;
	}

	/**
	 * Used to get event type
	 * @return the eventType
	 */
	public int getEventType() {

		return mIntEventType;
	}

	/**
	 * Used set event object
	 * @param eventObj the eventObj to set
	 */
	public void setEventObj(Object eventObj) {

		this.mEventObj = eventObj;
	}

	/**
	 * Used to get event object
	 * @return the eventObj
	 */
	public Object getEventObj() {

		return mEventObj;
	}
}
