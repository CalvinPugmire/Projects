import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends BaseClass {
	private Matcher _searchMatcher;
	private int _totalMatches;
	
	public FileSearch(String dirName, String filePattern, String searchPattern, boolean recurse) {
		super(dirName, filePattern, recurse);
		_searchMatcher = Pattern.compile(searchPattern).matcher("");
		_totalMatches = 0;
	}

	@Override
	protected void run() {
		performDirectory(new File(_directory));
		System.out.println("");
		System.out.println("TOTAL MATCHES: " + _totalMatches);
	}

	@Override
	protected void innerFunction (File file) throws IOException {
		InputStream data = new BufferedInputStream(new FileInputStream(file));
		int curMatches = 0;
		try {
			Scanner input = new Scanner(data);
			while (input.hasNextLine()) {
				String line = input.nextLine();

				_searchMatcher.reset(line);
				if (_searchMatcher.find()) {
					if (++curMatches == 1) {
						System.out.println("");
						System.out.println("FILE: " + file);
					}

					System.out.println(line);
					++_totalMatches;
				}
			}
		}
		finally {
			if (curMatches > 0) {
				System.out.println("MATCHES: " + curMatches);
			}
			data.close();
		}
	}

	public static void main(String[] args) {
		String directory = "";
		String pattern = "";
		String searchPattern = "";
		boolean recurse = false;
		
		if (args.length == 3) {
			recurse = false;
			directory = args[0];
			pattern = args[1];
			searchPattern = args[2];
		}
		else if (args.length == 4 && args[0].equals("-r")) {
			recurse = true;
			directory = args[1];
			pattern = args[2];
			searchPattern = args[3];
		}
		else {
			System.out.println("USAGE: java LineCount {-r} <dir> <file-pattern>");
			return;
		}

		FileSearch fileSearcher = new FileSearch(directory, pattern, searchPattern, recurse);
		fileSearcher.run();
	}
}
