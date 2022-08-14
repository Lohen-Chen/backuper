import java.util.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(final String[] args) throws IOException {
        Timer timer = new Timer ();
        TimerTask dailyTask = new TimerTask () {
            @Override
            public void run () {
                    try {
                        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        int minute = Calendar.getInstance().get(Calendar.MINUTE);
                        final String pathToFolder = "C:/Users/Lohen/Desktop/server";
                        final FileOutputStream fos = new FileOutputStream("C:/Users/Lohen/Desktop/worldbackups" + day + year + minute + ".zip");
                        final ZipOutputStream zipOut = new ZipOutputStream(fos);
                        final File fileToZip = new File(pathToFolder);
                        System.out.println("backing up");
                        zipFile(fileToZip, fileToZip.getName(), zipOut);     
                        System.out.println("backed up");    
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        };
        timer.schedule (dailyTask, 0l, 1000*60*60*24);
    }

    private static void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            final File[] children = fileToZip.listFiles();
            for (final File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        final FileInputStream fis = new FileInputStream(fileToZip);
        final ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        final byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}