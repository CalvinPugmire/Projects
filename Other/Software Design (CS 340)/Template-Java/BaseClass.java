import java.io.*;
import java.util.regex.*;

public abstract class BaseClass {
    protected String _directory;
    protected String _pattern;
    protected boolean _recurse;
    protected Matcher _matcher;

    public BaseClass(String directory, String pattern, boolean recurse) {
        _directory = directory;
        _pattern = pattern;
        _recurse = recurse;
        _matcher = Pattern.compile(_pattern).matcher("");
    }

    protected abstract void run();

    protected void performDirectory(File dir) {
        if (!dir.isDirectory()) {
            System.out.println(dir + " is not a directory");
            return;
        }

        if (!dir.canRead()) {
            System.out.println("Directory " + dir + " is unreadable");
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                if (file.canRead()) {
                    performFile(file);
                }
                else {
                    System.out.println("File " + file + " is unreadable");
                }
            }
        }

        if (_recurse) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    performDirectory(file);
                }
            }
        }
    }

    protected void performFile(File file) {
        String fileName = "";

        try {
            fileName = file.getCanonicalPath();
        }
        catch (IOException e) {
        }

        _matcher.reset(fileName);
        if (_matcher.find()) {
            try {
                innerFunction(file);
            }
            catch (IOException e) {
                System.out.println("File " + file + " is unreadable");
            }
        }
    }

    protected abstract void innerFunction(File file) throws IOException;

}
