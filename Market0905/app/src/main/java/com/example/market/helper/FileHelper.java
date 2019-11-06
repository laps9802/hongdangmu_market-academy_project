package com.example.market.helper;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class FileHelper {
    // 싱글톤 패턴 시작
    private static  FileHelper instane = null;

    public static FileHelper getInstance(){
        if (instane==null) instane = new FileHelper();
        return instane;
    }
    public  static  void freeIntstance(){
        instane = null;
    }
    private FileHelper() {}
    // 싱글톤 패턴 종료

    /**
     * 주어진 경로에 data값을 저장한다.
     * @param filePath : 저장할 파일 경로와 이름
     * @param data     : 저장할 내용을 담은 스트림
     * @return boolean : 저장 성공시 true, 실패시 false
     */
    public boolean write(String filePath, byte[] data){
        boolean result = false;
        /* 파일 저장 절차 시작 */
        // finally 블럭에서 인식시키기 위해서 선언부를 try 블럭 위로 위치시킴
        OutputStream out = null;
        try {
            // 객체 생성을 하면서, 파일을 만듦
            out = new FileOutputStream(filePath);
            // 파일쓰기
            out.write(data);
            // 여기까지 도착하면, 성공했다는 의미
            result  = true;
            Log.d("[INFO]","파일 저장 성공 >>> " +filePath);
        } catch (FileNotFoundException e) {
            Log.d("[ERROR]","지정된 경로를 찾을 수 없음 >>> " +filePath);
            //e.printStackTrace();
        } catch (IOException e) {
            Log.d("[ERROR]","파일 저장 실패 >>> " +filePath);
            //e.printStackTrace();
        } catch (Exception e) {
            Log.d("[ERROR]","알 수 없는 에러 >>> " +filePath);
            //e.printStackTrace();
        } finally{
            try {
                if(out!=null)out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 주어진 경로의 파일을 읽고, 내용을 스트림으로 리턴한다/
     * @param filePath : 읽어야할 파일의 경로 및 이름
     * @return byte[] : 읽어온 내용
     */

    public byte[] read(String filePath) {
        byte[] data = null;
        /* 파일 읽기 */
        InputStream in = null;
        try {
            in = new FileInputStream(filePath);
            // 읽어올 내용을 담기 위한 배열은 파일의 용량만큼 사이즈를 할당한다.
            // in.available() : 열고 있는 파일의 크기
            data = new byte[in.available()];
            // 파일 읽기 : 파라미터로 전달된 배열안에, 파일의 내용을 저장한다.
            in.read(data);
            Log.d("[INFO]","파일 읽기 성공 >>> " +filePath);
        } catch (FileNotFoundException e) {
            Log.d("[ERROR]","지정된 경로를 찾을 수 없음 >>> " +filePath);
            //e.printStackTrace();
        } catch (IOException e) {
            Log.d("[ERROR]","파일 읽기 실패 >>> " +filePath);
            //e.printStackTrace();
        } catch (Exception e) {
            Log.d("[ERROR]","알 수 없는 에러 >>> " +filePath);
            //e.printStackTrace();
        } finally{
            try {
                if(in!=null)in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  data;
    }

    /**
     * 문자열을 byte배열로 변환한 뒤, 파일을 저장한다.
     * @param filePath : 저장할 파일 경로
     * @param content  : 저장할 내용
     * @param encType  : 인코딩 형식
     * @return boolean : 저장 성공시 true, 실패시 false
     */

    public boolean writeString(String filePath, String content, String encType){
        boolean result = false;
        byte[] data = null;

        try {
            data = content.getBytes(encType);   // utf-8인 경우 : 한글은 3byte, 영어 숫자는 1byte
        } catch (UnsupportedEncodingException e) {
            Log.d("[ERROR]","인코딩 지정 에러");
            //e.printStackTrace();
        }
        result = write(filePath, data); // 파일 저장
        return result;
    }

    /**
     * 파일의 내용을 문자열로 리턴한다.
     * @param filePath : 읽어야할 파일의 경로
     * @param encType  : 인코딩 형식
     * @return String  : 읽은 내용의 대한 문자열
     */

    public String readString(String filePath, String encType){
        String content = null;
        byte[] data = read(filePath);   // 파일 읽기
        // byte 배열에 저장된 내용을 문자열로 변환한다.
        try {
            content = new String(data,encType);
        } catch (UnsupportedEncodingException e) {
            Log.d("[ERROR]","인코딩 지정 에러");
            //e.printStackTrace();
        }
        return content;
    }
}
