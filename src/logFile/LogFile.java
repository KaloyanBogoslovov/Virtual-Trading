package logfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogFile {
  private static File customDir;

  public static void initPathLocationAndCreateFile() {
    try {
      String path = System.getProperty("user.home") + File.separator;
      path += File.separator + "Virtual_Trading_1.1.0_Update_LogFile.txt";
      customDir = new File(path);
      customDir.createNewFile();
    } catch (IOException o) {
      System.out.println("initPathLocationAndCreateFile throws exception");
    }
  }


  public static void saveDataToLogFile(String data) {
    try {
      FileWriter fileWriter = new FileWriter(customDir, true);
      BufferedWriter br = new BufferedWriter(fileWriter);
      PrintWriter save = new PrintWriter(br);
      String timeStamp = getTimeStamp().toString();
      save.write(timeStamp + " " + data + "\n");
      save.close();
    } catch (IOException e) {
      System.out.println("saveDataToLogFile throws exception");
    }
  }

  private static StringBuilder getTimeStamp() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    StringBuilder date = new StringBuilder(dateFormat.format(cal.getTime()));
    return date;
  }


}
