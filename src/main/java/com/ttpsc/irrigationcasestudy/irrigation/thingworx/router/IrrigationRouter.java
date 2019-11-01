package com.ttpsc.irrigationcasestudy.irrigation.thingworx.router;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ttpsc.irrigationcasestudy.irrigation.thingworx.config.HttpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.PropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.IPrimitiveType;
import com.thingworx.types.primitives.IntegerPrimitive;
import com.thingworx.types.primitives.LocationPrimitive;
import com.thingworx.types.primitives.StringPrimitive;
import com.thingworx.types.primitives.structs.Location;
import com.ttpsc.irrigationcasestudy.irrigation.model.IrrigationDeviceProperty;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.client.IrrigationClient;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.device.IrrigationDevice;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ThingworxPropertyDefinitions(properties = {

		@ThingworxPropertyDefinition(name = "ConnetedDevices", description = "the number of conneted devices", baseType = "INTEGER", category = "Status", aspects = {
				"isReadOnly:TRUE", "pushType:VALUE", "isPersistent:TRUE"}),
		@ThingworxPropertyDefinition(name = "MailAccount", description = "the account for mail delivery service", baseType = "STRING", category = "Status", aspects = {
				"isReadOnly:FALSE", "pushType:VALUE", "isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "MailPassword", description = "the password of mail delivery service", baseType = "STRING", category = "Status", aspects = {
				"isReadOnly:FALSE", "pushType:VALUE", "isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "MailSmtpHost", description = "the host address of mail server", baseType = "STRING", category = "Status", aspects = {
				"isReadOnly:FALSE", "pushType:VALUE", "isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "MailSmtpPort", description = "the port of mail server", baseType = "NUMBER", category = "Status", aspects = {
				"isReadOnly:FALSE", "pushType:VALUE", "isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "GeoLocation", description = "Router Location", baseType = "LOCATION", category = "Status", aspects = {
				"isReadOnly:FALSE", "pushType:VALUE", "isPersistent:TRUE" }),
		})

public class IrrigationRouter extends VirtualThing {
	private static final Logger LOG = LoggerFactory.getLogger(IrrigationRouter.class);
	
	//this instance
	private final IrrigationRouter routerObj = this;

	private static final long serialVersionUID = 5738957136321905151L;
	private static final  String CONNECTED_DEVICE = "ConnetedDevices";
	private static final String MAIL_ACCOUNT = "MailAccount";
	private static final String MAIL_PW = "MailPassword";
	private static final String Mail_SMTP_HOST = "MailSmtpHost";
	private static final String Mail_SMTP_PORT = "MailSmtpPort";
	private static final String GEO_LOCATOIN = "GeoLocation";
	
	//router properties
	private Map<String, IrrigationDevice> deviceMap = new HashMap<String, IrrigationDevice>();
	private static int connetedDevices = 0;
	private static String MailLogin = "eason771108@gmail.com";
	private static String MailPassword = "roqpeoxkbtrjphdv";
	private static String MailSmtpHost = "smtp.gmail.com"; 
	private static int MailSmtpPort = 587;
	private static Location location;

    @Autowired
    private HttpConfig httpConfig;

	//for mail function
	Properties props;
	Session session;
	
	public int getConnetedDevices() {
		return (int) getProperty(CONNECTED_DEVICE).getValue().getValue();
	}
	
	public void setConnetedDevices() throws Exception {
		setProperty(CONNECTED_DEVICE, new IntegerPrimitive(connetedDevices));
	}

	public String getMailAccount() {
		return (String) getProperty(MAIL_ACCOUNT).getValue().getValue();
	}
	
	public void setMailAccount() throws Exception {
		setProperty(MAIL_ACCOUNT, new StringPrimitive(MailLogin));
	}
	
	public String getMailPassword() {
		return (String) getProperty(MAIL_PW).getValue().getValue();
	}
	
	public void setMailPassword() throws Exception {
		setProperty(MAIL_PW, new StringPrimitive(MailPassword));
	}
	
	public String getMailSmtpHost() {
		return (String) getProperty(Mail_SMTP_HOST).getValue().getValue();
	}
	
	public void setMailSmtpHost() throws Exception {
		setProperty(Mail_SMTP_HOST, new StringPrimitive(Mail_SMTP_HOST));
	}
	
	public int getMailSmtpPort() {
		return (int) getProperty(Mail_SMTP_PORT).getValue().getValue();
	}
	
	public void setMailSmtpPort() throws Exception {
		setProperty(Mail_SMTP_PORT, new IntegerPrimitive(MailSmtpPort));
	}
	
	public int getGeoLocation() {
		return (int) getProperty(GEO_LOCATOIN).getValue().getValue();
	}
	
	public void setGeoLocation() throws Exception {
		LocationPrimitive loc = new LocationPrimitive(location);
		setProperty(GEO_LOCATOIN, loc);
	}
	/**
     * A custom constructor. We implement this so we can call initializeFromAnnotations, which
     * processes all of the VirtualThing's annotations and applies them to the object.
     * 
     * @param name The name of the thing.
     * @param description A description of the thing.
     * @param client The client that this thing is associated with.
     * @throws Exception
     */
    public IrrigationRouter(String name, String description, ConnectedThingClient client)
            throws Exception {
        super(name, description, client);
        this.initializeFromAnnotations();

        location = new Location(121.613512, 25.059419);
    }

    @ThingworxServiceDefinition(name = "addNewDevice", description = "Add a new devices to client")
    @ThingworxServiceResult(name = "result", description = "",
            baseType = "INTEGER")
    public int addNewDevice(
            @ThingworxServiceParameter(name = "name",
                    description = "The first addend of the operation",
                    baseType = "STRING") String name,             
            @ThingworxServiceParameter(name = "baseTemplateName",
                    description = "Base template name for the operation",
                    baseType = "STRING") String baseTemplateName)
            throws Exception {
    	
    	//Check if already in Map
    	if(deviceMap.containsKey(name)) {
    		LOG.warn(String.format("Device with name(%s) is aready in list", name));
    		return -1;
    	}
    	//crete a thing on TWX platform
    	boolean isSuccessful = this.addNewThingOnThingWorx(baseTemplateName, name);
 
        if (!isSuccessful) {
        	LOG.error("Invoking new thing failed");
        	return -1;
        }

    	IrrigationClient client = (IrrigationClient) this.getClient();
    	
    	IrrigationDevice device = new IrrigationDevice(name, "", this.getName(), client);
        
    	deviceMap.put(name, device);
    	
    	//deviceList.add(device);
    	connetedDevices++;
    	setProperty(CONNECTED_DEVICE, new IntegerPrimitive(connetedDevices));
    	
    	device.bindingAllPropertiesToTWX();
    	device.bindingAllServicesToTWX();
    	
    	return device.getDeviceListeningPort();
    }
    
    @ThingworxServiceDefinition(name = "getDeviceList", description = "Add a new devices to client")
    @ThingworxServiceResult(name = "result", description = "",
            baseType = "INFOTABLE")
    public InfoTable getDeviceList()
            throws Exception {
    	
    	LOG.info(String.format("List all Devices(%d)", getConnetedDevices()));
    	
    	InfoTable it = new InfoTable();
    	
    	FieldDefinition DeviceNameField = new FieldDefinition();
    	DeviceNameField.setBaseType(BaseTypes.STRING);
    	DeviceNameField.setName("DeviceName");
    	it.addField(DeviceNameField);
    	
    	FieldDefinition DeviceLocField = new FieldDefinition();
    	DeviceLocField.setBaseType(BaseTypes.LOCATION);
    	DeviceLocField.setName("GeoLocation");
    	it.addField(DeviceLocField);
    	
    	ValueCollection device;
    	
    	for(Map.Entry<String, IrrigationDevice> entity : deviceMap.entrySet()) {
    		device = new ValueCollection();
    		device.SetValue(DeviceNameField, entity.getValue().getName());
    		device.SetValue(DeviceLocField, entity.getValue().getGeoLocation());
    		it.addRow(device);    		
    	}
    	
    	return it;
    }
    
    @ThingworxServiceDefinition(name = "sendMail", description = "send mail to specific address")
    @ThingworxServiceResult(name = "result", description = "",
            baseType = "STRING")
    public String sendMail(
            @ThingworxServiceParameter(name = "name",
            description = "The first addend of the operation",
            baseType = "STRING") String username ) throws Exception {
		String htmlBody =""
				+ "</h1>"
				+ "<p>&nbsp;</p>"
				+ "<p>&nbsp;</p>"
				+ "<p style=\"text-align: left;\">TEST MAIL</p>";

		String result = "ERROR";
		
		if(username== null || username.length() == 0) {
			return result;
		}
		
        //initialize session for java mail
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", MailSmtpHost);
		props.put("mail.smtp.port", MailSmtpPort);

		session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MailLogin, MailPassword);
			}
		  });
		
		Message message = new MimeMessage(session);
		message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(username));
		message.setSentDate(new Date());
		message.setSubject("Router Alarm");
		
		MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html;charset=UTF-8");
    	Multipart multipart = new MimeMultipart();
    	multipart.addBodyPart(messageBodyPart);
        
    	message.setContent(multipart);
    	
		Transport.send(message);
		
		result = "OK";
		
		return result;
    }
    
	@Override
	public void processPropertyWrite(PropertyDefinition property, @SuppressWarnings("rawtypes") IPrimitiveType value) throws Exception {
		this.setPropertyValue(property.getName(), value);
	}
	
    /**
     * This method provides a common interface amongst VirtualThings for processing periodic
     * requests. It is an opportunity to access data sources, update property values, push new
     * values to the server, and take other actions.
     */
    @Override
    public void processScanRequest() {

        try {

            // This call evaluates all properties and determines if they should be pushed
            // to the server, based on their pushType aspect. A pushType of ALWAYS means the
            // property will always be sent to the server when this method is called. A
            // setting of VALUE means it will be pushed if has changed since the last
            // push. A setting of NEVER means it will never be pushed.
            //
            // Our Temperature property is set to ALWAYS, so its value will be pushed
            // every time processScanRequest is called. This allows the platform to get
            // periodic updates and store the time series data. Humidity is set to
            // VALUE, so it will only be pushed if it changed.
        	
        	setConnetedDevices();
        	setMailAccount();
        	setMailPassword();
        	setMailSmtpHost();
        	setMailSmtpPort();
        	setGeoLocation();
        	
            this.updateSubscribedProperties(1000);

        } catch (Exception e) {
            // This will occur if we provide an unknown property name. We'll ignore
            // the exception in this case and just log it.
            LOG.error("Exception occurred while updating properties.", e);
        }
    }

	public void reportDeviceCurrentProperty(String deviceName,IrrigationDeviceProperty irrigationDeviceProperty) throws Exception {
		if(deviceMap.containsKey(deviceName)) {
			deviceMap.get(deviceName).updateAllProperties(irrigationDeviceProperty.getPumpWaterPressure(),
                    irrigationDeviceProperty.getActualIrrigationPower(),
                    irrigationDeviceProperty.getGeoLocation(),
                    irrigationDeviceProperty.getIrrigationState(),
                    irrigationDeviceProperty.getAlarmState(),
                    irrigationDeviceProperty.getIrrigationPowerLevel());
		}
	}
    
    private boolean addNewThingOnThingWorx(String baseTemplate, String deviceName) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(String.format("{\r\n    \"isSystemObject\": false,\r\n    \"thingTemplate\": \"%s\",\r\n    \"name\": \"%s\"\r\n}", baseTemplate, deviceName), mediaType);
        Request request = new Request.Builder()
                .url(httpConfig.getResourceUrl("/Thingworx/Things"))
                .put(body)
                .addHeader("appKey", httpConfig.getAppKey())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Length", "99")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        LOG.info("Put thing " + response.toString());
        return response.code() == 200 || response.code() == 409;
    }
}
