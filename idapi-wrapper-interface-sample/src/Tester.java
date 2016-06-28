import com.actuate.aces.idapi.control.ActuateException;
import opentext.idapi.samplecode.operations.impl.IdapiUtilImpl;
import opentext.idapi.samplecode.operations.util.IdapiUtil;
import opentext.idapi.samplecode.util.EngineProperties;

import javax.xml.rpc.ServiceException;
import java.io.IOException;

/**
 * Created by Kristopher Clark on 10/5/2015.
 */
public class Tester {
    public static void main(String[] args) throws IOException, ActuateException, ServiceException {
        EngineProperties myProps = new EngineProperties();
        IdapiUtil ihub = new IdapiUtilImpl();

        ihub.listAllFilesAndPermissions(myProps.getHost(),
                                        myProps.getUsername(),
                                        myProps.getPassword(),
                                        myProps.getVolume(),
                                        "/");
    }
}
