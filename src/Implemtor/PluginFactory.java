package Implemtor;


/**
 * Created by IntelliJ IDEA.
 * User: y
 * Date: 17/04/11
 * Time: 09:09
 *
 * this class use to dynamic load of java classes
 */

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class PluginFactory {
    /**
     * this method get a path of dircetory ad return array list of
     * classes for the stategy
     *
     * @param       dicPath   - the path of diconery of plugIn
     * @return      array list of stragy from the plugin
     * @throws      Exception
     */
   public   ArrayList<Implementor>    getClassArr(String dicPath) throws  Exception {

       File dic=new File(dicPath);
       if(!dic.isDirectory()){
           throw new Exception(" not good path ");
       }

       File[]   files= dic.listFiles();
       URL[]  urls=new URL[files.length];

       ClassLoader classLoader = this.getClass().getClassLoader();

       ArrayList<Implementor> arr=new ArrayList<Implementor>();
       
       for(int i=0;i<files.length;i++){
           String str=files[i].getName();
           if(str.endsWith(".jar")){
                 str= str.substring(0,str.indexOf(".jar"));
                 urls=new URL[1];
                 urls[0]=files[i].toURL();
                 classLoader= new URLClassLoader(urls);                
                 Class aClass = classLoader.loadClass("Implemtor."+str);
                 Class[] types=new Class[1]; 
                 types[0]=String.class; 
                 
                 Constructor constructor=aClass.getConstructor(types);
                 
                 //TODO add the relevant string here 
                
                 arr.add((Implementor) constructor.newInstance("relvant String"));
           }
       }
       return arr;

   }


}