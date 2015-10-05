package opentext.idapi.samplecode.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by Kristopher Clark on 10/5/2015.
 */
public class EngineProperties {
    protected Properties props = new Properties();

    public String host;
    public String username;
    public String password;
    public String volume;

    private static final String PROPS_FILE = "/resources/ihub.properties";

    public EngineProperties() throws IOException {
        getProperties();
    }

    protected void saveEngineProperties() {
        try {
            props.setProperty("host",     host);
            props.setProperty("username", username);
            props.setProperty("password", password);
            props.setProperty("volume",   volume);

            props.store(getPropsOutputStream(), "Properties changed on " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    protected void getProperties() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(PROPS_FILE);

        try {
            if(inputStream != null)
                props.load(inputStream);
        }catch(IOException ex){
            throw new FileNotFoundException("iHub Propertie file '" + PROPS_FILE + "' not found in classpath");
        }

        System.out.println(new File(".").getAbsolutePath());
        System.out.println(props.getProperty("host"));

        host = props.getProperty("host");
        username = props.getProperty("username");
        password = props.getProperty("password");
        volume   = props.getProperty("volume");
    }

    protected OutputStream getPropsOutputStream() throws FileNotFoundException {
        return new FileOutputStream(new File(PROPS_FILE));
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVolume() {
        return volume;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
