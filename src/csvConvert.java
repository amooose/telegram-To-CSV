/**
 * Created by Dylan on 2/18/2019.
 */

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class csvConvert {
    private static final String NEW_CSV = "telegramConverted.csv";

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(date);
        } catch (ParseException e) {
            System.out.println("error DATE invalid");
            return null;
        }
    }

    public static String dayStringFormat(long msecs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(msecs);
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dow) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
        }
        return "Unknown";
}

    public static String msToMonth(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        int mMonth = calendar.get(Calendar.MONTH);
        return getMonth(mMonth);
    }

    public static String time(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        int mHour = calendar.get(Calendar.HOUR);
        int amPM =calendar.get(Calendar.AM_PM);
        String amPmS = "AM";
        String padH = "";
        if(amPM ==1){
            amPmS = "PM";
        }
        if(amPM ==1 && mHour!=12){
            mHour+=12;
            amPmS = "PM";
        }
        if(mHour<10){
            padH = "0";
        }
        else{
            padH="";
        }
        return padH+mHour+":"+"00"+":"+"00"+""+amPmS;
    }

    public static String date(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        String pad = "";
        String padD = "";
        if(mMonth+1<10){pad="0";}
        if(mDay<10){padD="0";}
        return pad+(mMonth+1)+"/"+padD+mDay+"/"+mYear;
    }

    public static StringBuilder readInMessage(StringBuilder contentBuilder, String num){
        try {
            BufferedReader in = new BufferedReader(new FileReader("messages"+num+".html"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("ERROR: messages"+num+".html not found");
        }
        return contentBuilder;
    }


    public static void processFile(String name1,String name2, String sMSG_AMOUNT) throws IOException {
        int MSG_AMOUNT = Integer.parseInt(sMSG_AMOUNT);
        int counter = 0;
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NEW_CSV));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("name","month","day","hour","date","mstime","words","length","message")))
        {
            for (int j = 1; j <= MSG_AMOUNT; j++) {
                System.out.println("Processing #"+j);
                String num = Integer.toString(j);
                if(j==1){ num = "";}

                StringBuilder contentBuilder = new StringBuilder();
                contentBuilder=readInMessage(contentBuilder,num);

                String html = contentBuilder.toString();
                Document doc = Jsoup.parse(html);
                Elements links = doc.select("div.body");

                String name = "";
                for (int i = 1; i < links.size(); i++) {
                    if (!links.get(i).className().equals("forwarded body")) {
                        String date = links.get(i).select("div.pull_right").attr("title");
                        String tempName = links.get(i).select("div.from_name").text();
                        if (!tempName.equals(""))
                            name = tempName;

                        String message = links.get(i).select("div.text").text();
                        int msgWordCount = 0;
                        if (!message.equals("")) {
                            if(name.contains(name1)){
                                name = name1;
                                TextIterator readWords = new TextIterator();
                                readWords.readTextString(message);
                                while (readWords.hasNext()) {
                                    msgWordCount++;
                                    readWords.next();
                                }
                            }else{
                                name=name2;
                                TextIterator readWords = new TextIterator();
                                readWords.readTextString(message);
                                while (readWords.hasNext()) {
                                    msgWordCount++;
                                    readWords.next();
                                }
                            }

                            Date msgDate = parseDate(date);
                            message = message.replace(",","");
                            message = message.replace("\"","");
                            csvPrinter.printRecord(name, msToMonth(msgDate.getTime()),
                                    dayStringFormat(msgDate.getTime()),time(msgDate.getTime()),date(msgDate.getTime()),msgDate.getTime(),
                                    msgWordCount,message.length(),message.toLowerCase());
                            counter++;
                        }
                    }
                    csvPrinter.flush();
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("\n"+df.format(counter) + " total messages processed & converted");

    }

    public static void main(String[] args) {
        try{
            if(args.length >2) {
                processFile(args[0], args[1], args[2]);
            }else{
                System.out.println("ERROR: Invalid # of args. (name1, name2, # of messages)");
            }
        }catch(IOException e){}
    }
}
