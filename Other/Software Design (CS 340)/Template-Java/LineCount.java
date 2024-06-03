import java.io.*;
import java.util.*;

public class LineCount extends BaseClass {
	private int _totalLineCount;
	
	public LineCount(String directory, String pattern, boolean recurse) {
		super(directory, pattern, recurse);
		_totalLineCount = 0;
	}

	@Override
	protected void run() {
		performDirectory(new File(_directory));
		System.out.println("TOTAL: " + _totalLineCount);
	}

	@Override
	protected void innerFunction (File file) throws IOException {
		Reader reader = new BufferedReader(new FileReader(file));
		int curLineCount = 0;
		try {
			curLineCount = 0;
			Scanner input = new Scanner(reader);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				++curLineCount;
				++_totalLineCount;
			}
		}
		finally {
			System.out.println(curLineCount + "  " + file);
			reader.close();
		}
	}

	public static void main(String[] args) {
		String directory = "";
		String pattern = "";
		boolean recurse = false;
		
		if (args.length == 2) {
			recurse = false;
			directory = args[0];
			pattern = args[1];
		}
		else if (args.length == 3 && args[0].equals("-r")) {
			recurse = true;
			directory = args[1];
			pattern = args[2];
		}
		else {
			System.out.println("USAGE: java LineCount {-r} <dir> <file-pattern>");
			return;
		}
		
		LineCount lineCounter = new LineCount(directory, pattern, recurse);
		lineCounter.run();
	}
}
