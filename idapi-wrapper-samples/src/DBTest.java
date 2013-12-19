import com.actuate.aces.idapi.control.ActuateAPILocatorEx;
import com.actuate.schemas.*;

import javax.xml.rpc.Call;
import java.io.FileOutputStream;
import java.net.URL;


public class DBTest {
	String serverURL = "http://localhost:8000";
	String user = "Administrator";
	String password = "";
	String volume = "ActuateOne";
	String reportFile = "/Public/BIRT and BIRT Studio Examples/Sales by Employee.rptdesign";

	public void excuteReport(String outputFile) {


		ActuateAPILocatorEx actuateAPI = new ActuateAPILocatorEx();
		actuateAPI.setConnectionHandle("");
		actuateAPI.setDelayFlush(Boolean.TRUE);
		//actuateAPI.setLocale("");
		actuateAPI.setTargetVolume(volume);

		actuateAPI.setFileType("RPTDOCUMENT");
		try {
			Call call = actuateAPI.createCall();
			call.setTargetEndpointAddress(serverURL);
			ActuateSoapPort proxy = actuateAPI.getActuateSoapPort(new URL(serverURL));

			GetSystemVolumeNames gsvn = new GetSystemVolumeNames();
			gsvn.setOnlineOnly(true);


			Login request = new Login();
			request.setUser(user);
			request.setPassword(password);
			LoginResponse response;
			response = proxy.login(request);
			actuateAPI.setAuthId(response.getAuthId());
			System.out.println("***AUTHID:" + response.getAuthId());

			ExecuteReport executeReport = new ExecuteReport();
			executeReport.setJobName("14761299"); //14761188
			executeReport.setInputFileName(reportFile);
			executeReport.setSaveOutputFile(Boolean.FALSE);
			executeReport.setProgressiveViewing(Boolean.FALSE);
			//TODO: Do Parameter Handling here
			//executeReport.setParameterValues(params.toArrayOfParameterValue());

			ExecuteReportResponse reportResponse = proxy.executeReport(executeReport);
			if (reportResponse.getStatus().equals(ExecuteReportStatus.Done)) {
				actuateAPI.setConnectionHandle(reportResponse.getConnectionHandle());
				ObjectIdentifier objectIdentifier = new ObjectIdentifier();
				objectIdentifier.setId(reportResponse.getObjectId());
				objectIdentifier.setType("rptdocument");
				System.out.println("*****Here2***** " + reportResponse.getObjectId());

				String connectionHandle = actuateAPI.getConnectionHandle();

				/*ComponentType component = new ComponentType();
						component.setId("0"); // fetch the whole file
	*/
				/*ViewParameter viewParam = new ViewParameter();
									viewParam.setFormat("PDF"); //format
									viewParam.setUserAgent("Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; GTB7.4; InfoPath.2; SV1; .NET CLR 3.3.69573; WOW64; en-US)");*/

				/*GetContent contentRequest = new GetContent();
				   contentRequest.setObject(objectIdentifier);
				   contentRequest.setComponent(component);
				   contentRequest.setViewParameter(viewParam);
				   contentRequest.setDownloadEmbedded(Boolean.TRUE);*/

				com.actuate.schemas.SelectJavaReportPage selectJavaReportPage = new com.actuate.schemas.SelectJavaReportPage();
				System.out.println("*****Here3***** ");
				selectJavaReportPage.setObject(objectIdentifier);
				selectJavaReportPage.setOutputFormat("csv");
				selectJavaReportPage.setDownloadEmbedded(Boolean.TRUE);

				SelectJavaReportPageResponse selectJavaReportPageResponse = proxy.selectJavaReportPage(selectJavaReportPage);

				//GetContentResponse contentResponse = proxy.getContent(contentRequest);

				//Attachment attachment = contentResponse.getContentRef();
				Attachment attachment = selectJavaReportPageResponse.getPageRef();
				if (attachment != null) {
					FileOutputStream out = new FileOutputStream(outputFile);
					out.write(attachment.getContentData());
					out.close();
					out.flush();
					out = null;
				} else {
					System.out.println("FAILED TO GET THE REPORT CONTENT");
				}
			} else {
				System.out.println("FAILED TO GENERATE PDF REPORT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		System.out.println("ENTER TESTING");
		//NormalActuateCall client = new NormalActuateCall();
		DBTest client = new DBTest();
		for (int i = 0; i < 1; i++) {
			client.excuteReport("E:/ActuateTest/20131128/test" + i + ".csv"); //format
			try {
				Thread.sleep(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("############## Iteration " + i + " End");
		}
		System.out.println("EXIT TESTING");
	}
}
