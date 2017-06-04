/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InventoryManager.Bean;

import InventoryManager.Entity.Item;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Possum
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/ItemMessage"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ItemMessageBean implements MessageListener {
    
    @Resource
    private MessageDrivenContext mdc;
    @PersistenceContext(unitName="com.pos_InventoryManager-ejb")
    private EntityManager em;
    
    public ItemMessageBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        ObjectMessage msg = null;
        try{
            if(message instanceof ObjectMessage){
                msg = (ObjectMessage) message;
                Item newItem = (Item) msg.getObject();
                save(newItem);
            }
        } catch(JMSException ex){
            ex.printStackTrace();
            mdc.setRollbackOnly();
        } catch(Throwable throwable){
            throwable.printStackTrace();
        }
    }
    
    private void save(Object object){
        em.persist(object);
    }
}
