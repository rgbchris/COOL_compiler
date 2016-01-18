/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
      return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
      filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
      return filename;
    }
    /* Custom */
    private boolean string_escapable = false;
    private int comment_count = 0;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		59,
		53
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"42:9,43,1,42,43,0,42:18,43,42,38,42:5,14,15,4,2,10,3,11,5,40,60:9,12,13,7,8" +
",9,42,18,45,46,47,48,49,28,46,50,51,46:2,52,46,30,53,54,46,55,56,32,57,36,5" +
"8,46:3,42,39,42:2,61,42,24,59,27,37,22,23,59,31,29,59:2,25,59,41,33,34,59,2" +
"0,26,19,21,44,35,59:3,16,42,17,6,42,62:2")[0];

	private int yy_rmap[] = unpackFromString(1,167,
"0,1:2,2,3,1:2,4,5,1:5,6,1:4,7,8,1,9,10,1,11,1:5,12:2,13,12:2,14:2,12:12,1:3" +
",15,1:2,16,1:2,17,18,19,20,14:2,21,14:2,12:2,14:10,22,23,24,25,26,27,28,29," +
"30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54," +
"55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,14,76,77,78," +
"79,80,12,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,1" +
"2,102,103,104,105")[0];

	private int yy_nxt[][] = unpackFromString(106,63,
"-1,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,162:2,163,60,162,128,162" +
",164,20,82,61,162,129,85,165,166,134,162,21,9,22,133,9,23,62,134:2,137,134," +
"139,134,83,141,86,143,134:3,145,162,22,9,24,-1:66,25,-1:74,26,-1:50,27,-1:4" +
",28,-1:63,29,-1:57,30,-1:77,162,136,162:10,138,162:6,-1:2,140,162,-1:2,162:" +
"6,138,162:4,136,162:4,140:2,-1:20,134:10,63,134:8,-1:2,134:2,-1:2,134:7,63," +
"134:10,-1:41,22,-1:19,22,-1:45,23:2,-1:20,25:60,-1:20,162:19,-1:2,140,162,-" +
"1:2,162:16,140:2,-1:20,162:12,156,162:6,-1:2,140,162,-1:2,162:6,156,162:9,1" +
"40:2,-1:20,134:19,-1:2,134:2,-1:2,134:18,-1:2,54,81:36,55,56,81:22,24,-1:40" +
",57,58,-1:22,1,50:2,80,50:9,84,50:47,24,-1:19,162:5,146,162:4,31,162:8,-1:2" +
",140,162,-1:2,162,146,162:5,31,162:8,140:2,-1:20,134:3,88,134:10,90,134:4,-" +
"1:2,134:2,-1:2,134:5,88,134:3,90,134:8,-1:20,162:19,-1:2,140,162,-1,23,62,1" +
"62:15,140:2,-1:20,134:12,116,134:6,-1:2,134:2,-1:2,134:6,116,134:11,-1:16,5" +
"1,-1:49,81:36,-1:2,81:22,-1:20,162:4,32,162:2,150,162,32,162,33,162:7,-1:2," +
"140,33,-1:2,162:12,150,162:3,140:2,-1:20,134:4,64,134:2,98,134,64,134,65,13" +
"4:7,-1:2,134,65,-1:2,134:12,98,134:5,-1:5,52,-1:77,162:4,34,162:4,34,162:9," +
"-1:2,140,162,-1:2,162:16,140:2,-1:20,134:4,66,134:4,66,134:9,-1:2,134:2,-1:" +
"2,134:18,-1:20,35,162:12,35,162:5,-1:2,140,162,-1:2,162:16,140:2,-1:20,134:" +
"16,36,134:2,-1:2,134:2,-1:2,134:14,36,134:3,-1:20,162:16,68,162:2,-1:2,140," +
"162,-1:2,162:14,68,162,140:2,-1:20,37,134:12,37,134:5,-1:2,134:2,-1:2,134:1" +
"8,-1:20,69,162:12,69,162:5,-1:2,140,162,-1:2,162:16,140:2,-1:20,134:3,106,1" +
"34:15,-1:2,134:2,-1:2,134:5,106,134:12,-1:20,162:3,38,162:15,-1:2,140,162,-" +
"1:2,162:5,38,162:10,140:2,-1:20,134:7,108,134:11,-1:2,134:2,-1:2,134:12,108" +
",134:5,-1:20,162:11,39,162:7,-1:2,140,39,-1:2,162:16,140:2,-1:20,134:5,110," +
"134:13,-1:2,134:2,-1:2,134,110,134:16,-1:20,162:3,40,162:15,-1:2,140,162,-1" +
":2,162:5,40,162:10,140:2,-1:20,134:17,135,134,-1:2,134:2,-1:2,135,134:17,-1" +
":20,162:8,41,162:10,-1:2,140,162,-1:2,162:3,41,162:12,140:2,-1:20,67,134:12" +
",67,134:5,-1:2,134:2,-1:2,134:18,-1:20,162:15,42,162:3,-1:2,140,162,-1:2,16" +
"2:10,42,162:5,140:2,-1:20,134:14,117,134:4,-1:2,134:2,-1:2,134:9,117,134:8," +
"-1:20,162:3,43,162:15,-1:2,140,162,-1:2,162:5,43,162:10,140:2,-1:20,134:10," +
"119,134:8,-1:2,134:2,-1:2,134:7,119,134:10,-1:20,162:6,44,162:12,-1:2,140,1" +
"62,-1:2,162:8,44,162:7,140:2,-1:20,134:11,70,134:7,-1:2,134,70,-1:2,134:18," +
"-1:20,162:3,45,162:15,-1:2,140,162,-1:2,162:5,45,162:10,140:2,-1:20,134:3,7" +
"4,134:15,-1:2,134:2,-1:2,134:5,74,134:12,-1:20,162:7,46,162:11,-1:2,140,162" +
",-1:2,162:12,46,162:3,140:2,-1:20,134:7,120,134:11,-1:2,134:2,-1:2,134:12,1" +
"20,134:5,-1:20,162:3,47,162:15,-1:2,140,162,-1:2,162:5,47,162:10,140:2,-1:2" +
"0,134:3,71,134:15,-1:2,134:2,-1:2,134:5,71,134:12,-1:20,162:18,48,-1:2,140," +
"162,-1:2,162:4,48,162:11,140:2,-1:20,134:8,72,134:10,-1:2,134:2,-1:2,134:3," +
"72,134:14,-1:20,162:7,49,162:11,-1:2,140,162,-1:2,162:12,49,162:3,140:2,-1:" +
"20,134:3,122,134:15,-1:2,134:2,-1:2,134:5,122,134:12,-1:20,134:15,73,134:3," +
"-1:2,134:2,-1:2,134:10,73,134:7,-1:20,134:6,75,134:12,-1:2,134:2,-1:2,134:8" +
",75,134:9,-1:20,134:6,123,134:12,-1:2,134:2,-1:2,134:8,123,134:9,-1:20,134:" +
"7,76,134:11,-1:2,134:2,-1:2,134:12,76,134:5,-1:20,134:10,124,134:8,-1:2,134" +
":2,-1:2,134:7,124,134:10,-1:20,134,125,134:17,-1:2,134:2,-1:2,134:11,125,13" +
"4:6,-1:20,134:3,77,134:15,-1:2,134:2,-1:2,134:5,77,134:12,-1:20,134:18,78,-" +
"1:2,134:2,-1:2,134:4,78,134:13,-1:20,134:10,126,134:8,-1:2,134:2,-1:2,134:7" +
",126,134:10,-1:20,127,134:12,127,134:5,-1:2,134:2,-1:2,134:18,-1:20,134:7,7" +
"9,134:11,-1:2,134:2,-1:2,134:12,79,134:5,-1:20,162:3,87,162:10,147,162:4,-1" +
":2,140,162,-1:2,162:5,87,162:3,147,162:6,140:2,-1:20,134:12,92,134:6,-1:2,1" +
"34:2,-1:2,134:6,92,134:11,-1:20,134:7,112,134:11,-1:2,134:2,-1:2,134:12,112" +
",134:5,-1:20,134:5,114,134:13,-1:2,134:2,-1:2,134,114,134:16,-1:20,134:14,1" +
"18,134:4,-1:2,134:2,-1:2,134:9,118,134:8,-1:20,162:3,89,162:10,91,162:4,-1:" +
"2,140,162,-1:2,162:5,89,162:3,91,162:6,140:2,-1:20,134:14,121,134:4,-1:2,13" +
"4:2,-1:2,134:9,121,134:8,-1:20,162:2,93,162:16,-1:2,140,162,-1:2,162:13,93," +
"162:2,140:2,-1:20,134:5,94,96,134:12,-1:2,134:2,-1:2,134,94,134:6,96,134:9," +
"-1:20,162:3,95,162:15,-1:2,140,162,-1:2,162:5,95,162:10,140:2,-1:20,134:6,1" +
"30,131,134:11,-1:2,134:2,-1:2,134:8,130,134:3,131,134:5,-1:20,134:3,100,134" +
":10,102,134:4,-1:2,134:2,-1:2,134:5,100,134:3,102,134:8,-1:20,162:7,97,162:" +
"11,-1:2,140,162,-1:2,162:12,97,162:3,140:2,-1:20,134:14,132,134:4,-1:2,134:" +
"2,-1:2,134:9,132,134:8,-1:20,162:5,99,162:13,-1:2,140,162,-1:2,162,99,162:1" +
"4,140:2,-1:20,134:12,104,134:6,-1:2,134:2,-1:2,134:6,104,134:11,-1:20,162:6" +
",153,162:12,-1:2,140,162,-1:2,162:8,153,162:7,140:2,-1:20,162:14,101,162:4," +
"-1:2,140,162,-1:2,162:9,101,162:6,140:2,-1:20,162:7,103,162:11,-1:2,140,162" +
",-1:2,162:12,103,162:3,140:2,-1:20,162:5,154,162:13,-1:2,140,162,-1:2,162,1" +
"54,162:14,140:2,-1:20,162:17,155,162,-1:2,140,162,-1:2,155,162:15,140:2,-1:" +
"20,162:14,105,162:4,-1:2,140,162,-1:2,162:9,105,162:6,140:2,-1:20,162:10,15" +
"7,162:8,-1:2,140,162,-1:2,162:7,157,162:8,140:2,-1:20,162:7,107,162:11,-1:2" +
",140,162,-1:2,162:12,107,162:3,140:2,-1:20,162:7,109,162:11,-1:2,140,162,-1" +
":2,162:12,109,162:3,140:2,-1:20,162:14,158,162:4,-1:2,140,162,-1:2,162:9,15" +
"8,162:6,140:2,-1:20,162:3,159,162:15,-1:2,140,162,-1:2,162:5,159,162:10,140" +
":2,-1:20,162:6,111,162:12,-1:2,140,162,-1:2,162:8,111,162:7,140:2,-1:20,162" +
":10,113,162:8,-1:2,140,162,-1:2,162:7,113,162:8,140:2,-1:20,162,160,162:17," +
"-1:2,140,162,-1:2,162:11,160,162:4,140:2,-1:20,162:10,161,162:8,-1:2,140,16" +
"2,-1:2,162:7,161,162:8,140:2,-1:20,115,162:12,115,162:5,-1:2,140,162,-1:2,1" +
"62:16,140:2,-1:20,162:6,142,144,162:11,-1:2,140,162,-1:2,162:8,142,162:3,14" +
"4,162:3,140:2,-1:20,162:5,148,149,162:12,-1:2,140,162,-1:2,162,148,162:6,14" +
"9,162:7,140:2,-1:20,162:14,151,162:4,-1:2,140,162,-1:2,162:9,151,162:6,140:" +
"2,-1:20,162:12,152,162:6,-1:2,140,162,-1:2,162:6,152,162:9,140:2,-1");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
      case YYINITIAL:
        /* nothing special to do in the initial state */
        break;
        /* If necessary, add code for other states here, e.g: */
      case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant"); 
      case COMMENT:
        yybegin(YYINITIAL);
        if (comment_count > 0) {
          return new Symbol(TokenConstants.ERROR, "EOF in comment"); 
        }
        return new Symbol(TokenConstants.ERROR, "Unmatched *)"); 
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						{ 
  curr_lineno++;
}
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.PLUS, yytext()); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.MINUS, yytext()); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.MULT, yytext()); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.DIV, yytext()); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.NEG, yytext()); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.LT, yytext()); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.EQ, yytext()); }
					case -9:
						break;
					case 9:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.COMMA, yytext()); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.DOT, yytext()); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.COLON, yytext()); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.SEMI, yytext()); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LPAREN, yytext()); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.RPAREN, yytext()); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.LBRACE, yytext()); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.RBRACE, yytext()); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.AT, yytext()); }
					case -19:
						break;
					case 19:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -20:
						break;
					case 20:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -21:
						break;
					case 21:
						{
  yybegin(STRING);
}
					case -22:
						break;
					case 22:
						{
  return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}
					case -23:
						break;
					case 23:
						{ }
					case -24:
						break;
					case 24:
						
					case -25:
						break;
					case 25:
						{}
					case -26:
						break;
					case 26:
						{ 
  return new Symbol(TokenConstants.ERROR, "Unmatched *)"); 
}
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.ASSIGN, yytext()); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.LE, yytext()); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.DARROW, yytext()); }
					case -30:
						break;
					case 30:
						{ 
  yybegin(COMMENT);
  comment_count++;
}
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.FI, yytext()); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.IF, yytext()); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.IN, yytext()); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.OF, yytext()); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LET, yytext()); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NEW, yytext()); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NOT, yytext()); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.BOOL_CONST, (Boolean)true); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.THEN, yytext()); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ELSE, yytext()); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.ESAC, yytext()); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.LOOP, yytext()); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.CASE, yytext()); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.POOL, yytext()); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.BOOL_CONST, (Boolean)false); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.CLASS, yytext()); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.WHILE, yytext()); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID, yytext()); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS, yytext()); }
					case -50:
						break;
					case 50:
						{ }
					case -51:
						break;
					case 51:
						{ 
  comment_count--;
  if (comment_count == 0) {
    yybegin(YYINITIAL);
  }
}
					case -52:
						break;
					case 52:
						{ 
  comment_count++;
}
					case -53:
						break;
					case 53:
						{
  if (yytext().length() > MAX_STR_CONST) {
    return new Symbol(TokenConstants.ERROR, "String constant too long"); 
  }
  return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext()));
}
					case -54:
						break;
					case 54:
						{
  if (string_escapable != true) {
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); 
  }
}
					case -55:
						break;
					case 55:
						{
  yybegin(YYINITIAL);
}
					case -56:
						break;
					case 56:
						{
  string_escapable = true;
}
					case -57:
						break;
					case 57:
						{
  return new Symbol(TokenConstants.ERROR, "String contains null character"); 
}
					case -58:
						break;
					case 58:
						{ 
  /* escaped newline */ 
}
					case -59:
						break;
					case 60:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -60:
						break;
					case 61:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -61:
						break;
					case 62:
						{ }
					case -62:
						break;
					case 63:
						{ return new Symbol(TokenConstants.FI, yytext()); }
					case -63:
						break;
					case 64:
						{ return new Symbol(TokenConstants.IF, yytext()); }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.IN, yytext()); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.OF, yytext()); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.LET, yytext()); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.NEW, yytext()); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.NOT, yytext()); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.THEN, yytext()); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.ELSE, yytext()); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.ESAC, yytext()); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.LOOP, yytext()); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.CASE, yytext()); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.POOL, yytext()); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.CLASS, yytext()); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.WHILE, yytext()); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.ISVOID, yytext()); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.INHERITS, yytext()); }
					case -79:
						break;
					case 80:
						{ }
					case -80:
						break;
					case 81:
						{
  if (yytext().length() > MAX_STR_CONST) {
    return new Symbol(TokenConstants.ERROR, "String constant too long"); 
  }
  return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(yytext()));
}
					case -81:
						break;
					case 82:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -82:
						break;
					case 83:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -83:
						break;
					case 84:
						{ }
					case -84:
						break;
					case 85:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -85:
						break;
					case 86:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -86:
						break;
					case 87:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -87:
						break;
					case 88:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -88:
						break;
					case 89:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -89:
						break;
					case 90:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -90:
						break;
					case 91:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -91:
						break;
					case 92:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -92:
						break;
					case 93:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -93:
						break;
					case 94:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -94:
						break;
					case 95:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -95:
						break;
					case 96:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -96:
						break;
					case 97:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -97:
						break;
					case 98:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -98:
						break;
					case 99:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -99:
						break;
					case 100:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -100:
						break;
					case 101:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -101:
						break;
					case 102:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -102:
						break;
					case 103:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -103:
						break;
					case 104:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -104:
						break;
					case 105:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -105:
						break;
					case 106:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -106:
						break;
					case 107:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -107:
						break;
					case 108:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -108:
						break;
					case 109:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -109:
						break;
					case 110:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -110:
						break;
					case 111:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -111:
						break;
					case 112:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -112:
						break;
					case 113:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -113:
						break;
					case 114:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -114:
						break;
					case 115:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -115:
						break;
					case 116:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -116:
						break;
					case 117:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -117:
						break;
					case 118:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -118:
						break;
					case 119:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -119:
						break;
					case 120:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -120:
						break;
					case 121:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -121:
						break;
					case 122:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -122:
						break;
					case 123:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -123:
						break;
					case 124:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -124:
						break;
					case 125:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -125:
						break;
					case 126:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -126:
						break;
					case 127:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -127:
						break;
					case 128:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -128:
						break;
					case 129:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -129:
						break;
					case 130:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -130:
						break;
					case 131:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -131:
						break;
					case 132:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -132:
						break;
					case 133:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -133:
						break;
					case 134:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -134:
						break;
					case 135:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -135:
						break;
					case 136:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -136:
						break;
					case 137:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -137:
						break;
					case 138:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -138:
						break;
					case 139:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -139:
						break;
					case 140:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -140:
						break;
					case 141:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -141:
						break;
					case 142:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -142:
						break;
					case 143:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -143:
						break;
					case 144:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -144:
						break;
					case 145:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -145:
						break;
					case 146:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -146:
						break;
					case 147:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -147:
						break;
					case 148:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -148:
						break;
					case 149:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -149:
						break;
					case 150:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -150:
						break;
					case 151:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -151:
						break;
					case 152:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -152:
						break;
					case 153:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -153:
						break;
					case 154:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -154:
						break;
					case 155:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -155:
						break;
					case 156:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -156:
						break;
					case 157:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -157:
						break;
					case 158:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -158:
						break;
					case 159:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -159:
						break;
					case 160:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -160:
						break;
					case 161:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -161:
						break;
					case 162:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -162:
						break;
					case 163:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -163:
						break;
					case 164:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -164:
						break;
					case 165:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -165:
						break;
					case 166:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -166:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
