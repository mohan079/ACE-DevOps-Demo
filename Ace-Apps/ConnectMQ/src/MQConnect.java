import java.io.IOException;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

/**
 * Java class to connect to MQ. Post and Retreive messages.
 *
 */
public class MQConnect {

    String qMngrStr = "QM1";
    //String user = "admin";
    //String password = "passw0rd";
    String queueName = "DEV.QUEUE.1";
    String hostName = "ec2-54-92-136-52.compute-1.amazonaws.com";
    int port = 30877;
    String channel = "DEV.APP.SVRCONN";
    //message to put on MQ.
    String msg = "Hello World, WelCome to MQ.";
    //Create a default local queue.
    MQQueue defaultLocalQueue;
    MQQueueManager qManager;
    
    /**
     * Initialize the MQ
     *
     */
    public void init(){
        
        //Set MQ connection credentials to MQ Envorinment.
         MQEnvironment.hostname = hostName;
         MQEnvironment.channel = channel;
         MQEnvironment.port = port;
        // MQEnvironment.userID = user;
         //MQEnvironment.password = password;
         //set transport properties.
         MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
         
         try {
             //initialize MQ manager.
            qManager = new MQQueueManager(qMngrStr);
        } catch (MQException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Method to put message to MQ.
     *
     */
    public void putAndGetMessage(){
        
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT; 
        try {
            defaultLocalQueue = qManager.accessQueue(queueName, openOptions);
            
            MQMessage putMessage = new MQMessage();
            putMessage.writeUTF(msg);
            
            //specify the message options...
            MQPutMessageOptions pmo = new MQPutMessageOptions(); 
            // accept 
            // put the message on the queue
            defaultLocalQueue.put(putMessage, pmo);
            
            System.out.println("Message is put on MQ.");
            
            //get message from MQ.
            MQMessage getMessages = new MQMessage();
            //assign message id to get message.
            getMessages.messageId = putMessage.messageId;
            
            //get message options.
           // MQGetMessageOptions gmo = new MQGetMessageOptions();
           // defaultLocalQueue.get(getMessages, gmo);
            
           // String retreivedMsg = getMessages.readUTF();
          //  System.out.println("Message got from MQ: "+retreivedMsg);
            
        } catch (MQException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
        System.out.println("Processing Main...");
        
        MQConnect clientTest = new MQConnect();
        
        //initialize MQ.
        clientTest.init();
        
        //put and retreive message from MQ.
        clientTest.putAndGetMessage();
        
        System.out.println("Done!");
    }
    
}
