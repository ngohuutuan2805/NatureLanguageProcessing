package com.example.ngohuutuan.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.security.spec.ECField;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.io.*;
import java.nio.channels.*;

import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;
import java.io.InputStream;


import java.io.IOException;
import android.content.res.AssetManager;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });





        //test();

        //boyerMoore();


        searchString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    /************************************ Nature Language Processing *******************************************/



    public String[] initialConsonants = {"b", "c", "d", "đ", "g", "h", "k", "l", "m", "n", "q", "r", "s", "t", "v", "x", "ch", "gh", "gi", "kh", "ng", "nh", "ph", "qu", "th", "tr", "ngh"};

    public String[] vowels = {"a", "ă", "â", "e", "ê", "i", "o", "ô", "ơ", "u", "ư", "y",
            "ai", "ao", "au", "ay", "âu", "ây", "eo", "êu", "ia", "iu", "iê", "oa", "oă", "oe", "oi", "oo", "ôi", "ơi",
            "ua", "uâ", "ui", "uê", "uô", "uơ", "uy", "ưa", "ưi", "ươ", "ưu", "yê",
            "oai", "oay", "uây", "uôi", "iêu", "uyê", "ươu", "ươi", "uya", "yêu", "uyu"};

    public String[] finalConsonants = {"c", "p", "t", "m", "n", "ch", "ng", "nh"};

    public String accentCharacters   = "àÀảẢãÃáÁạẠ ằẰẳẲẵẴắẮặẶ ầẦẩẨẫẪấẤậẬ èÈẻẺẽẼéÉẹẸ ềỀểỂễỄếẾệỆ ìÌỉỈĩĨíÍịỊ òÒỏỎõÕóÓọỌ ồỒổỔỗỖốỐộỘ ờỜởỞỡỠớỚợỢ ùÙủỦũŨúÚụỤ ừỪửỬữỮứỨựỰ yÝyỲyỶyỴyỶ";
    public String unAccentCharacters = "aAaAaAaAaA ăĂăĂăĂăĂăĂ âÂâÂâÂâÂâÂ eEeEeEeEeE êÊêÊêÊêÊêÊ iIiIiIiIiI oOoOoOoOoO ôÔôÔôÔôÔôÔ ơƠơƠơƠơƠơƠ uUuUuUuUuU ưƯưƯưƯưƯưƯ yYyYyYyYyY";

    public String endingCharacterRegx = "^.+[\\?!.:;,\"\\)\\}\\]>']$";

    public String startingCharacterRegx = "^[\"\\(\\{\\[<'].+";


    public String regxDateTime = "(\\d{1,2}/\\d{1,2}/\\d{4})|(\\d{1,2}:\\d{1,2}:\\d{4})|(\\d{1,2}-\\d{1,2}-\\d{4})";

    public String regxPhoneNumber= "(\\+84|0)(\\s?)\\d{9,10}";

    public String regxEmail = "[a-zA-Z0-9_\\.]+@[a-zA-Z]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)*";

    public String regxPercent = "\\d*\\%";

    public String regxMoneyOrNumber = "\\d*(\\$|vnđ)?";

    public String other = "-";

    //public String regxSpecialChars = "([\\[~`!@#$%^&*()_+-=\\\\|;'/.,\":\\?><\\]{}])*";



    public void test(){

        /**
         *  Nature Language Processing
         *
         *  Một từ TV bắt buộc phải có nguyên âm, do đó tìm nguyên âm và check nguyên âm đó có tồn tại hay k
         *  Khi so sánh nguyên âm, ta phải bỏ dấu trước khi so sánh (tức là check đã thoả mãn về dấu)
         *
         *
         */


        String testString = "Ô kỳ ô Ngày ! ngohutuuan@gmail.com ntuan@@gmail.com@ +841649667039 01234567890 12/12/1029 10% 30$ we$ we3 &g ?s**a ấy con' tôis \"\" chỉ. mới ghé {thăm Huế} troyng [giây lát, thực]! ra chỉ mới quanh quẩn trong Đại Nội " +
                "cùng các lăng tẩm chùa chqiền, và (trên bờ sông) Hương, nơi đầu cầu Trường Tiền. " +
                "Nhưng tôi tin rằng không khí trong trẻo và đường phố Huế thật yên tĩnh vào bủôi sáng ngày mồng ba tết năm ấy, " +
                "những tán lá phừợng? xasnh ở! đầu. cầu Trường Tiền... đã để! lại một? ấn stượng rất đẹp trong lòng của một cậu bé con.";


        // Tách kí tự đặc biệt
        //String s  = "Q?ueen, ă? 12$ %32 of reptiles.";
        //String[] parts = s.split("(?=\\W)|\\s+");
        //Log.d("DEBUG", "String: |" + Arrays.toString(parts) + "|");


        //String[] stringArray = testString.split(" ");









        ArrayList<String> stringArray = new ArrayList<String>(Arrays.asList(testString.split("\\s+")));

        String result = "";

        for(int i = 0; i < stringArray.size(); i++) {

            String string = stringArray.get(i);
            Log.d("DEBUG", "****************************************");
            Log.d("DEBUG", "String: |" + string + "|");
            String mainString = string;

            // Kiểm tra viết hoa chữ cái đầu của string đầu tiên
            if(i ==0 && Character.isUpperCase(string.charAt(0)) == false) {
                Log.d("DEBUG", "Reson: không viết hoa chữ cái đầu");
                result += "|" + string + "|";
                continue;
            }

            //String endChar   = string.substring(string.length() - 1 ,string.length());
            //String startChar = string.substring(0, 1);
            //Log.d("DEBUG", "Stating and Ending: |" + startChar + "|" + endChar + "|");

            // Kiểm tra xem string chứa các kí tự kết thúc hoặc kí tự bắt đầu và loại bỏ chúng khỏi string
            if(string.matches(endingCharacterRegx)) {
                if(string.endsWith("...")) {
                    mainString = string.substring(0, string.length()-3);
                } else {
                    mainString = string.substring(0, string.length()-1);
                }

                Log.d("DEBUG", "Removed ending: " + mainString);

            } else if(string.matches(startingCharacterRegx)) {

                mainString = string.substring(1, string.length());
                Log.d("DEBUG", "Removed starting: " + mainString);
            }


            // Kiểm tra string có hợp lệ hay không sau khi đã loại bỏ kí tự đầu (mở ngoặc) và kí tự cuối (đóng ngoặc, kết thúc câu)
            if(isValidString(mainString.toLowerCase())){

                result += " " + string;

            } else {

                result += " |" + string + "|";

            }

            // Kiểm tra viết hoa chữ cái đầu sau dấu kết thúc câu (? ! . ...)
            if(string.endsWith("...") | string.endsWith(".") | string.endsWith("!") | string.endsWith("?") && i < stringArray.size() - 1){

                String nextString = stringArray.get(i+1);
                Log.d("DEBUG", "Next string: " + nextString);
                if(Character.isUpperCase(nextString.charAt(0)) == false){
                    Log.d("DEBUG", "Reson: không viết hoa chữ cái đầu");
                    result += " |" + nextString + "|";
                    i++;
                }
            }


        }

        Log.d("DEBUG", "****************************************");
        Log.d("DEBUG", "Result: " + result);
        Log.d("DEBUG", "****************************************");
    }

    public boolean isValidString(String string){

        if(string.isEmpty()) return true;

        WordComponent component = getComponents(string);

        String vowel = component.vowel;

        if(vowel.isEmpty()) {
            // Mỗi từ TV đều phải có nguyên âm
            Log.d("DEBUG", "Reason: Không có nguyên âm");
            return false;

        } else{

            Log.d("DEBUG", "Check vowel: |" + vowel + "|");

            /**
             * Chưa xử lý chữ hoa, thường
             *
             */
            if(nhieuDauThanh(vowel)) {
                Log.d("DEBUG", "Reason: Có nhiều dấu thanh");
                return false;

            } else {

                vowel = unAccent(vowel); // Bỏ dấu tiếng Việt
                Log.d("DEBUG", "Un accent: " + vowel);

                // Kiểm tra xem nguyên âm (sau khi bỏ dấu) có trong bộ từ điển hay k ?
                for(int i = 0; i < vowels.length; i++){

                    if(vowels[i].contentEquals(vowel)) {
                        return true;
                    }
                }


                /*
                 *  Kí tự đặc biệt, số đếm, phone number, datetime...
                 */

                if(string.matches(regxDateTime)){
                    Log.d("DEBUG", "Reason: datetime");
                    return true;
                } else if(string.matches(regxEmail)){
                    Log.d("DEBUG", "Reason: email");
                    return true;
                } else if(string.matches(regxPhoneNumber)){
                    Log.d("DEBUG", "Reason: phone number");
                    return true;
                } else if(string.matches(regxPercent)){
                    Log.d("DEBUG", "Reason: percent");
                    return true;
                } else if(string.matches(regxMoneyOrNumber)){
                    Log.d("DEBUG", "Reason: money or numbers");
                    return true;
                } else if(string.matches(other)){
                    Log.d("DEBUG", "Reason: dấu gạch ngang");
                    return true;
                }


                Log.d("DEBUG", "Reason: Không nằm trong từ điển");
                return false;
            }

        }
    }

    public WordComponent getComponents(String string){

        WordComponent component = new WordComponent("", "", "");

        // Find initial consonant
        for(int i = initialConsonants.length - 1; i >= 0 ; i--){

            if(string.startsWith(initialConsonants[i])){
                component.initialConsonant = initialConsonants[i];
                break;
            }
        }

        // Find fianl consonant
        for(int i = finalConsonants.length - 1; i >= 0; i--) {

            if(string.endsWith(finalConsonants[i])) {
                component.finalConsonant = finalConsonants[i];
                break;
            }
        }

        // Find vowel
        component.vowel = string.substring(component.initialConsonant.length(), string.length() - component.finalConsonant.length());

        String message = String.format("Component: |%s|%s|%s|",  component.initialConsonant, component.vowel, component.finalConsonant);
        Log.d("Debug",message);

        return component;
    }

    public boolean nhieuDauThanh(String string) {

        int numberOfAccent = 0;
        for(int i = 0; i < string.length(); i++) {

            if(accentCharacters.indexOf(string.charAt(i)) != -1){
                numberOfAccent++;
                //Log.d("DEBUG", "Accent number: " + numberOfAccent);
                if(numberOfAccent > 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public String unAccent(String string) {

        char[] chars = string.toCharArray();

        for(int i = 0; i < chars.length; i++) {

            int index = accentCharacters.indexOf(chars[i]);
            if(index != -1){

                chars[i] = unAccentCharacters.charAt(index);

            }
        }

        return String.valueOf(chars);

    }


    public boolean testBaiKiemTra(){

        Log.d("DEBUG", "test: |" + "hay ho       hihi ".trim().replaceAll("(\\s)+", " ") + "|");


        String x = "thuyuh";

        if(x.length() <= 1){
            return false;
        }

        String vowels = "ueoai";

        char start = x.charAt(0);
        char end   = x.charAt(x.length() - 1);

        boolean  startWithHIsCorrect = false;
        boolean  endWithHIsCorrect = false;
        boolean  middleHIsCorrect = false;


        if(end == 'h'){

            if(vowels.indexOf('s') != -1){

            }

        }

        return true;
    }


    public void boyerMoore(){

//        String text = "asdasdasd asdsd asdad ad as d asd a sd asdasdasd asdsd asdad ad as d asd a sd asdasdasd asdsd asdad ad as " +
//                "d asd a sd asdasdasd asdsd asdad ad as d asd a sd asdasdasd asdsd asdad ad as d asd a sd ";
//        String string = "asdasdasd";
//
//        int n = text.length();
//        int m = string.length();
//
//        try{
//
//            File file = new File("1.txt");
//            FileInputStream fileInputStream = new FileInputStream(file);
//            FileChannel fileChannel = fileInputStream.getChannel();
//
//        } catch (Exception e){
//            System.err.println(e.getMessage());
//        }

//        File file = new File("1.txt");
//        Pattern pattern = Pattern.compile("Phạm Phi Vân");
//        Scanner scanner = null;
//
//        try {
//            scanner = new Scanner(file);
//
//            while (scanner.findWithinHorizon(pattern, 0) != null){
//
//
//
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            scanner.close();
//        }


        Log.d("DEBUG", "Start reading file line by line using BufferedReader");

        InputStream inputStream = null;
        BufferedReader reader = null;

        String searchString = "Phạm Phi Vân";

        try {


            AssetManager assetManager = getAssets();

            inputStream = assetManager.open("2.txt");

            Log.d("DEBUG", "Start");

            reader = new BufferedReader(new InputStreamReader(inputStream));

            Log.d("DEBUG", "End");

            String line = reader.readLine();
            Log.d("DEBUG", "New line: \n" + line);

            while(line != null){

                Log.d("DEBUG", "New line: \n" + line);

                int index = line.indexOf(searchString);
                if(index != -1){
                    Log.d("DEBUG", "Index: \n" + index);
                }

                line = reader.readLine();
            }


        } catch (FileNotFoundException ex) {

            Log.d("DEBUG", ex.getMessage());

        } catch (IOException ex) {

            Log.d("DEBUG", ex.getMessage());

        }

    }


    public void searchString(){

        Log.d("DEBUG", "Search string");

        FileInputStream fis = null;
        BufferedReader reader = null;

        try {
            Log.d("DEBUG", "Search string");
            fis = new FileInputStream("/2.txt");
            Log.d("DEBUG", "Search string");
            reader = new BufferedReader(new InputStreamReader(fis));
            Log.d("DEBUG", "Reading File line by line using BufferedReader");

            String line = reader.readLine();
            while(line != null){
                //System.out.println(line);
                Log.d("DEBUG", "New Line: \n" + line);
                line = reader.readLine();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ex.getMessage());

        } finally {
            try {
                //reader.close();
                //fis.close();
                if(reader != null)
                    reader.close();
                if(fis != null)
                    fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ex.getMessage());
            }
        }

    }

    /************************************ Nature Language Processing *******************************************/

}










