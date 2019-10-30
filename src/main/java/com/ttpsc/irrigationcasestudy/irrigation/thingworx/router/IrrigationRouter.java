package com.ttpsc.irrigationcasestudy.irrigation.thingworx.router;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.annotations.*;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.IntegerPrimitive;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.client.IrrigationClient;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.device.IrrigationDevice;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@ThingworxPropertyDefinitions(properties = {

        @ThingworxPropertyDefinition(name = "ConnetedDevices", description = "the number of conneted devices", baseType = "NUMBER", category = "Status", aspects = {
                "isReadOnly:TRUE", "isPersistent:TRUE" }),
})

public class IrrigationRouter extends VirtualThing {
    private static final Logger LOG = LoggerFactory.getLogger(IrrigationRouter.class);
    /**
     *
     */
    private static final long serialVersionUID = 5738957136321905151L;
    private static final  String CONNECTED_DEVICE = "ConnetedDevices";


    //router properties
    private List<IrrigationDevice> deviceList = new ArrayList<>();
    private static int connetedDevices = 0;


    //for mail function
    Properties props;
    Session session;

    public int getConnetedDevices() {
        return (int) getProperty(CONNECTED_DEVICE).getValue().getValue();
    }

    public void setConnetedDevices() throws Exception {
        setProperty(CONNECTED_DEVICE, new IntegerPrimitive(connetedDevices));
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

        //initialize session for java mail
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("eason771108@gmail.com", "roqpeoxkbtrjphdv");
                    }
                });
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
            this.updateSubscribedProperties(10000);

        } catch (Exception e) {
            // This will occur if we provide an unknown property name. We'll ignore
            // the exception in this case and just log it.
            LOG.error("Exception occurred while updating properties.", e);
        }
    }
    @ThingworxServiceDefinition(name = "addNewDevice", description = "Add a new devices to client")
    @ThingworxServiceResult(name = "result", description = "", baseType = "BOOLEAN")
    public Boolean addNewDevice(
            @ThingworxServiceParameter(name = "name",
                    description = "The first addend of the operation",
                    baseType = "STRING") String name,
            @ThingworxServiceParameter(name = "baseTemplateName",
                    description = "Base template name for the operation",
                    baseType = "STRING") String baseTemplateName)
            throws Exception {

        IrrigationClient client = (IrrigationClient) this.getClient();
        IrrigationDevice device = new IrrigationDevice(name, "", client);

        boolean isSuccessful = this.addNewThingOnThingWorx(baseTemplateName, name);
        if (isSuccessful) {
            client.bindThing(device);
            LOG.info(String.format("Add a device to client : %s", name));
        }

        deviceList.add(device);
        connetedDevices++;
        setProperty(CONNECTED_DEVICE, new IntegerPrimitive(connetedDevices));
        return isSuccessful;
    }

    @ThingworxServiceDefinition(name = "getDeviceList", description = "Add a new devices to client")
    @ThingworxServiceResult(name = "result", description = "",
            baseType = "INFOTABLE")
    public InfoTable getDeviceList()
            throws Exception {

        LOG.info(String.format("List all Devices(%d)", connetedDevices));

        InfoTable it = new InfoTable();

        FieldDefinition DeviceNameField = new FieldDefinition();
        DeviceNameField.setBaseType(BaseTypes.STRING);
        DeviceNameField.setName("DeviceName");
        it.addField(DeviceNameField);

        ValueCollection device;
        for(IrrigationDevice d : deviceList) {
            device = new ValueCollection();
            device.SetValue(DeviceNameField, d.deviceName);
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

    private boolean addNewThingOnThingWorx(String baseTemplate, String deviceName) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(String.format("{\r\n    \"isSystemObject\": false,\r\n    \"thingTemplate\": \"%s\",\r\n    \"name\": \"%s\"\r\n}", baseTemplate, deviceName), mediaType);
        Request request = new Request.Builder()
                .url("http://192.168.10.128:8080/Thingworx/Things")
                .put(body)
                .addHeader("appKey", "35370038-46aa-45ec-b2ff-3d1488ca768c")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "PostmanRuntime/7.19.0")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "a5340639-b1ec-40a7-8561-a793f5284f4e,990901b1-c2c7-4585-af4b-567a37f00956")
                .addHeader("Host", "192.168.10.128:8080")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Length", "99")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        LOG.info("Put thing" + response.toString());
        return response.isSuccessful();
    }
}
