package com.dfc.moneymartus.data;

public class ModelData {
	private static final ModelData model = new ModelData();
	public boolean isEnabled;
	
	/**
	 * get Modeldata instance
	 * @return
	 */
	public static ModelData getInstance(){
		return model;
		
	}

}
