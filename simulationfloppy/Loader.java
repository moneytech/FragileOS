import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Loader {
    private Floppy floppyDisk = new Floppy();
    private int MAX_SECTOR_NUM = 18;

    public Loader(String pathName) {
        writeFileToFloppy(pathName, true, 0, 1);
    }

    public void makeFloppy() {
        writeFileToFloppy("../kernel/kernel.bat", false, 1, 1);

        //TODO 7 cylinders are only temporary values
        FAT12System fileSys = new FAT12System(floppyDisk, 7, 1);
    	FileHeader header = new FileHeader();
    	header.setFileName("alienwar");
    	header.setFileExt("exe");
    	byte[] date = new byte[2];
    	date[0] = 0x11;
    	date[1] = 0x12;
    	header.setFileTime(date);
    	header.setFileDate(date);
   	
    	byte[] bbuf = new byte[4600];
    	File file = new File("../app/alienwars.bat");
    	InputStream in = null;
    	try {
    		in = new FileInputStream(file);
    		long len = file.length();
    		
    		int count = 0;
    		while (count < file.length()) {
    			bbuf[count] = (byte) in.read();
    			count++;
    		}
    		
    		in.close();
    	}catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    	
    	header.setFileContent(bbuf);
    	fileSys.addHeader(header);

		header = new FileHeader();
    	header.setFileName("circle");
    	header.setFileExt("exe");
    	file = new File("../app/kaleidoscope.bat");
    	in = null;
    	try {
    		in = new FileInputStream(file);
    		long len = file.length();
    		
    		int count = 0;
    		while (count < file.length()) {
    			bbuf[count] = (byte) in.read();
    			count++;
    		}
    		
    		in.close();
    	}catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    	header.setFileContent(bbuf);
    	fileSys.addHeader(header);

    	header = new FileHeader();
    	header.setFileName("crack");
    	header.setFileExt("exe");
    	file = new File("../api/crack.bat");
    	in = null;
    	try {
    		in = new FileInputStream(file);
    		long len = file.length();
    		
    		int count = 0;
    		while (count < file.length()) {
    			bbuf[count] = (byte) in.read();
    			count++;
    		}
    		
    		in.close();
    	}catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    	header.setFileContent(bbuf);
    	fileSys.addHeader(header);
    	
    	header = new FileHeader();
    	header.setFileName("text");
    	header.setFileExt("txt");
    	String content = "this is a text file with name text.txt";
    	header.setFileContent(content.getBytes());
    	fileSys.addHeader(header);
    	
    	fileSys.flashFileHeaders();
    	    	
    	floppyDisk.makeFloppy("../FragileOS.img");
    }

    private void writeFileToFloppy(String pathName, boolean bootable, int cylinder, int beginSec) {
        File file = new File(pathName);
        InputStream in;

        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[512];
            if (bootable) {
                buf[510] = 0x55;
                buf[511] = (byte) 0xaa;
            }

            while (in.read(buf) > 0) {
                floppyDisk.writeFloppy(Floppy.MagneticHead.MagneticHead0, cylinder, beginSec, buf);

                System.out.println("Load file " + pathName.substring(3) + " to floppy with cylinder: " + cylinder + " and sector:" + beginSec);
                if (beginSec >= MAX_SECTOR_NUM) {
                    beginSec = 0;
                    cylinder++;
                }

                beginSec++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Loader loader = new Loader("../boot/boot.bat");
        loader.makeFloppy();
    }
}
