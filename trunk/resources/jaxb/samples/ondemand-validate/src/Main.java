/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationException;
import javax.xml.bind.Validator;
import javax.xml.bind.util.ValidationEventCollector;

// import java content classes generated by binding compiler
import primer.po.*;

/*
 * $Id: Main.java,v 1.1 2004/06/25 21:12:15 kohsuke Exp $
 *
 * Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Sun Microsystems, Inc.  
 * Use is subject to license terms.
 * 
 */
 
public class Main {
    
    // This sample application demonstrates how to validate a Java content
    // tree at runtime. 
    
    public static void main( String[] args ) {
        try {
            // create a JAXBContext capable of handling classes generated into
            // the primer.po package
            JAXBContext jc = JAXBContext.newInstance( "primer.po" );
            
            // create an Unmarshaller
            Unmarshaller u = jc.createUnmarshaller();

            // in this example, we will allow the Validator's default
            // ValidationEventHandler to receive notification of warnings 
            // and errors which will be sent to System.out.  The default
            // ValidationEventHandler will cause the validateRoot operation
            // to fail with an ValidationException after encountering the
            // first error or fatal error.
            
            // unmarshal a valid po instance document into a tree of Java 
            // content objects composed of classes from the primer.po package.
            PurchaseOrder po = 
                (PurchaseOrder)u.unmarshal( new FileInputStream( "po.xml" ) );
                
            // get a reference to the first item in the po 
            Items items = po.getItems();
            List itemTypeList = items.getItem();
            Items.ItemType item = (Items.ItemType)itemTypeList.get( 0 );
            
            // invalidate it by setting some bogus data
            item.setQuantity( new BigInteger( "-5" ) );
            
            // create a Validator
            Validator v = jc.createValidator();
            
            // validate the content tree
            System.out.println("NOTE: This sample is working correctly if you see validation errors!!");
            boolean valid = v.validateRoot( po );
            System.out.println( valid );

        } catch( ValidationException ue ) {
            System.out.println( "Caught ValidationException" );
        } catch( JAXBException je ) {
            je.printStackTrace();
        } catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }
}
