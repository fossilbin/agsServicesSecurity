package com.esrichina.BP.config;

public abstract class configMngrBase {
	
	public abstract void openConfig(String filename);
	
	public abstract Object getConfigProperty(String prop);
	
	public abstract void setConfigProperty(String prop, String value);
	
	public abstract void closeConfig();
}
