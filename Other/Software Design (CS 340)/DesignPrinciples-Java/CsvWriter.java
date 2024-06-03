//1. What design principles does this program violate?
	//Single responsibility principle.
//2. Refactor the program to improve its design.

CsvWriter.java

public class CsvWriter {
	public CsvWriter() {
	}

	public String write(String[][] lines) {
		String str = "";
		for (int i = 0; i < lines.length; i++)
			str += writeLine(lines[i]);
		return str;
	}

	private String writeLine(String[] fields) {
		String substr = "";
		if (fields.length == 0)
			return substr;
		else {
			substr += writeField(fields[0]);

			for (int i = 1; i < fields.length; i++) {
				substr += ",";
				substr += writeField(fields[i]);
			}
			return substr;
		}
	}

	private String writeField(String field) {
		String subsubstr = "";
		if (field.indexOf(',') != -1 || field.indexOf('\"') != -1)
			subsubstr += writeQuoted(field);
		else
			return field;
	}

	private String writeQuoted(String field) {
		String subsubsubstr = "";
		subsubsubstr += '\"';
		for (int i = 0; i < field.length(); i++) {
			char c = field.charAt(i);
			if (c == '\"')
				subsubsubstr += "\"\"";
			else
				subsubsubstr += c;
		}
		subsubsubstr += '\"';
		return subsubsubstr;
	}
}
