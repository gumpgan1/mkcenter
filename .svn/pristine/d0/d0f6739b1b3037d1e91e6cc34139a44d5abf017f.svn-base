package com.mktech.webservices;


/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
/**
 * 类WordFileService.java的实现描述：TODO 类实现描述 
 * @author HEBI 2012-8-21 下午04:36:20
 */
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.io.FileOutputStream;
    import javax.activation.DataHandler;
public class WordFileService {
       //  使用byte[]类型参数上传二进制文件
        public boolean uploadWithByte(byte[] file, String filename)
        {
             FileOutputStream fos = null;
             try
             {   System.out.println("开始读入文件");              
                 fos = new FileOutputStream("c:\\"+filename+".docx");    
                 fos.write(file);
                 fos.close();
                 System.out.println("文件写完成");     
             }
             catch (Exception e)
             {   e.printStackTrace();
                 return false;
             }
             finally
             {
                 if (fos != null)
                 {
                     try
                     {
                         fos.close();
                     }
                     catch (Exception e)
                     {
                     }
                 }
             }
             return true;
        }
        private void writeInputStreamToFile(InputStream is, OutputStream os) throws Exception
        {
             int n = 0;
             byte[] buffer = new byte[8192];
             while((n = is.read(buffer)) > 0)
             {
                 os.write(buffer, 0, n);
             }
        }
        //  使用DataHandler类型参数上传文件
        public boolean uploadWithDataHandler(DataHandler file, String filename)
        {
            
             FileOutputStream fos = null;
             try
             {   System.out.println("开始读入文件"); 
                 fos = new FileOutputStream("D:\\"+filename);   
                 //  可通过DataHandler类的getInputStream方法读取上传数据
                 writeInputStreamToFile(file.getInputStream(), fos);
                 fos.close();
                 System.out.println("文件写完成");
             }
             catch (Exception e)
             {  e.printStackTrace();
                 return false;
             }
             finally
             {
                 if (fos != null)
                 {
                     try
                     {
                         fos.close();
                     }
                     catch (Exception e)
                     {  e.printStackTrace();
                     }
                 }
             }
             return true;
        }
    }


