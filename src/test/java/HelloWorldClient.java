/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */


/**
 * 类HelloServer.java的实现描述：TODO 类实现描述 
 * @author HEBI 2012-8-20 下午05:08:58
 */
import javax.xml.namespace.QName;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

/**
 * <b>function:</b>HelloWorldService 客户端调用代码
 * @author hoojo
 * @createDate 2011-1-7 下午03:55:05
 * @file HelloWorldClient.java
 * @package com.hoo.service
 * @project Axis2WebService
 * @blog http://blog.csdn.net/IBM_hoojo
 * @email hoojo_@126.com
 * @version 1.0
 */
public class HelloWorldClient {

    public static void main(String[] args) throws AxisFault {
        //RPCServiceClient是RPC方式调用
        RPCServiceClient client = new RPCServiceClient();
        Options options = client.getOptions();
        //设置调用WebService的URL
        String address = "http://localhost:8080/mkcenter/services/DataRevicerServer";
        EndpointReference epf = new EndpointReference(address);
        options.setTo(epf);
        
        /**
         * 设置将调用的方法，http://ws.apache.org/axis2是方法
         * 默认（没有package）命名空间，如果有包名
         * 就是http://service.hoo.com 包名倒过来即可
         * sayHello就是方法名称了
         */
        QName qname = new QName("http://ws.apache.org/axis2", "ReceiveData");
        //指定调用的方法和传递参数数据，及设置返回值的类型
        Object[] result = client.invokeBlocking(qname, new Object[] { "jackccccccc" }, new Class[] { String.class });
        System.out.println(result[0]);
        
//        qname = new QName("http://ws.apache.org/axis2", "getAge");
//        result = client.invokeBlocking(qname, new Object[] { new Integer(22) }, new Class[] { int.class });
//        System.out.println(result[0]);
    }
}

