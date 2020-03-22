package com.request.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
/**
 * Settings
 */
public class Settings {

    private Properties core;

    private int read_timeout;

    private int connection_timeout;

    private String appliaction_name;

    private int child_threads_max;

    public Settings(){
		setProps();
		setup();
	}

    private void setProps(){
		File file = new File(System.getProperty("user.dir")+"/configs/application.properties");
		core = new Properties();
		try {
			core.load(new FileInputStream(file));	
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}	
    }
    
    private void setup() {
        this.connection_timeout = Integer.parseInt(core.getProperty("request.connection.timeout"));
        this.read_timeout = Integer.parseInt(core.getProperty("request.read.timeout"));
        this.appliaction_name = core.getProperty("application.name");
        this.child_threads_max= Integer.parseInt(core.getProperty("application.child.threads.max"));
    }


    /**
     * @return the connection_timeout
     */
    public int getConnection_timeout() {
        return connection_timeout;
    }


    /**
     * @return the read_timeout
     */
    public int getRead_timeout() {
        return read_timeout;
    }

    /**
     * @return the appliaction_name
     */
    public String getAppliaction_name() {
        return appliaction_name;
    }

    /**
     * @return the child_threads_max
     */
    public int getChild_threads_max() {
        return child_threads_max;
    }
}