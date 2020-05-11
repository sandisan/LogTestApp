import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import java.text.SimpleDateFormat;  
import java.util.Date;  


public class LogTest_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			String integrationServer = getExecutionGroup().getName();
			String serviceName = getMessageFlow().getName();
			String severity = "INFO"; 
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");  
			Date date = new Date();  
			String logMessage = "Testing CP4I ACE Logging - "+ formatter.format(date);
	
			String log = "{\"IntegrationServer\":\""+integrationServer+
					"\""+",\"ServiceName\":\""+serviceName+"\"},\"Severity\":\""+severity+"\""+"Message\":\""+logMessage+"\"}";
			System.out.println(log);
			MbElement outRoot = outMessage.getRootElement();
			MbElement outJsonRoot = outRoot.createElementAsLastChild(MbJSON.PARSER_NAME);
			MbElement outJsonData = outJsonRoot.createElementAsLastChild(MbElement.TYPE_NAME, MbJSON.DATA_ELEMENT_NAME, null);
			MbElement outJsonTest1 = outJsonData.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "IntegrationServer", integrationServer);
			MbElement outJsonTest2 = outJsonData.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "ServiceName", serviceName);
			MbElement outJsonTest3 = outJsonData.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "Severity", severity);
			MbElement outJsonTest4 = outJsonData.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "Message", logMessage);

			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

}
