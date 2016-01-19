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
	private final int NULL = 3;
	private final int yy_state_dtrans[] = {
		0,
		74,
		55,
		97
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
		/* 59 */ YY_NO_ANCHOR,
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
		/* 74 */ YY_NOT_ACCEPT,
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
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NOT_ACCEPT,
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
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"39,41:8,44,1,46,45,47,41:4,48,41:8,49,41:4,51,50,38,50:5,14,15,4,2,10,3,11," +
"5,68:10,12,13,7,8,9,50,18,52,53,54,55,56,28,53,57,58,53:2,59,53,30,60,61,53" +
",62,63,32,64,65,66,53:3,50,40,50:2,69,41,24,43,27,37,22,23,67,31,29,67:2,25" +
",67,42,33,34,67,20,26,19,21,36,35,67:3,16,41,17,6,41,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,185,
"0,1:3,2,3,1:2,4,5,1:5,6,1:4,7,8,1,9,1:2,10,11,1:5,12:2,13,12:2,14:2,12:12,1" +
":3,15,1:17,16,17,18,19,20,14:2,21,14:2,12:2,14:10,22,23,24,16,25,26,27,16,2" +
"8,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,5" +
"3,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,14,7" +
"7,78,79,80,81,12,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100," +
"101,102,12,103,104,105,106")[0];

	private int yy_nxt[][] = unpackFromString(107,70,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,180:2,181,76,180,146,180" +
",182,21,99,77,180,147,103,183,184,180:2,22,10:3,151,180,23:2,24,25,10:3,23," +
"152:2,155,152,157,152,100,159,104,161,152:4,163,180,26,10,-1:73,27,-1:81,28" +
",-1:57,29,-1:4,30,-1:70,31,-1:64,32,-1:84,180,154,180:10,156,180:6,-1:4,180" +
":2,-1:8,180:5,156,180:4,154,180:5,158:2,-1:19,152:10,78,152:8,-1:4,152:2,-1" +
":8,152:6,78,152:11,-1:44,23:2,-1:5,23,-1:86,26,-1:3,27:45,-1,27:22,-1:19,18" +
"0:19,-1:4,180:2,-1:8,180:16,158:2,-1:19,180:12,174,180:6,-1:4,180:2,-1:8,18" +
"0:5,174,180:10,158:2,-1:19,152:19,-1:4,152:2,-1:8,152:18,1,56,96:4,10,96:6," +
"10,96:2,10:2,96:20,57,58,75,10,96:2,59,60,61,62,63,64,96:20,-1,72,102:36,73" +
",102:8,-1,102:22,1,2,52:2,95,52:9,101,52:55,-1,65,66:17,59,66:3,60,66:14,67" +
",68,69,66,70,71,66:26,-1:19,180:5,164,180:4,33,180:8,-1:4,180:2,-1:8,164,18" +
"0:5,33,180:9,158:2,-1:19,152:3,106,152:10,108,152:4,-1:4,152:2,-1:8,152:4,1" +
"06,152:3,108,152:9,-1:19,152:12,134,152:6,-1:4,152:2,-1:8,152:5,134,152:12," +
"-1:15,53,-1:56,96:4,-1,96:6,-1,96:2,-1:2,96:20,-1:4,96:2,-1:6,96:20,1,72,98" +
":36,73,98:8,-1,98:22,-1:19,180:4,34,180:2,168,180,34,180,35,180:7,-1:4,35,1" +
"80,-1:8,180:11,168,180:4,158:2,-1:19,152:4,79,152:2,116,152,79,152,80,152:7" +
",-1:4,80,152,-1:8,152:11,116,152:6,-1:4,54,-1:84,180:4,36,180:4,36,180:9,-1" +
":4,180:2,-1:8,180:16,158:2,-1:19,152:4,81,152:4,81,152:9,-1:4,152:2,-1:8,15" +
"2:18,-1:19,37,180:12,37,180:5,-1:4,180:2,-1:8,180:16,158:2,-1:19,152:16,38," +
"152:2,-1:4,152:2,-1:8,152:14,38,152:3,-1:19,180:16,83,180:2,-1:4,180:2,-1:8" +
",180:14,83,180,158:2,-1:19,39,152:12,39,152:5,-1:4,152:2,-1:8,152:18,-1:19," +
"84,180:12,84,180:5,-1:4,180:2,-1:8,180:16,158:2,-1:19,152:3,124,152:15,-1:4" +
",152:2,-1:8,152:4,124,152:13,-1:19,180:3,40,180:15,-1:4,180:2,-1:8,180:4,40" +
",180:11,158:2,-1:19,152:7,126,152:11,-1:4,152:2,-1:8,152:11,126,152:6,-1:19" +
",180:11,41,180:7,-1:4,41,180,-1:8,180:16,158:2,-1:19,152:5,128,152:13,-1:4," +
"152:2,-1:8,128,152:17,-1:19,180:3,42,180:15,-1:4,180:2,-1:8,180:4,42,180:11" +
",158:2,-1:19,152:17,153,152,-1:4,152:2,-1:8,152:13,153,152:4,-1:19,180:8,43" +
",180:10,-1:4,180:2,-1:8,180:2,43,180:13,158:2,-1:19,82,152:12,82,152:5,-1:4" +
",152:2,-1:8,152:18,-1:19,180:15,44,180:3,-1:4,180:2,-1:8,180:9,44,180:6,158" +
":2,-1:19,152:14,135,152:4,-1:4,152:2,-1:8,152:8,135,152:9,-1:19,180:3,45,18" +
"0:15,-1:4,180:2,-1:8,180:4,45,180:11,158:2,-1:19,152:10,137,152:8,-1:4,152:" +
"2,-1:8,152:6,137,152:11,-1:19,180:6,46,180:12,-1:4,180:2,-1:8,180:7,46,180:" +
"8,158:2,-1:19,152:11,85,152:7,-1:4,85,152,-1:8,152:18,-1:19,180:3,47,180:15" +
",-1:4,180:2,-1:8,180:4,47,180:11,158:2,-1:19,152:3,89,152:15,-1:4,152:2,-1:" +
"8,152:4,89,152:13,-1:19,180:7,48,180:11,-1:4,180:2,-1:8,180:11,48,180:4,158" +
":2,-1:19,152:7,138,152:11,-1:4,152:2,-1:8,152:11,138,152:6,-1:19,180:3,49,1" +
"80:15,-1:4,180:2,-1:8,180:4,49,180:11,158:2,-1:19,152:3,86,152:15,-1:4,152:" +
"2,-1:8,152:4,86,152:13,-1:19,180:18,50,-1:4,180:2,-1:8,180:3,50,180:12,158:" +
"2,-1:19,152:8,87,152:10,-1:4,152:2,-1:8,152:2,87,152:15,-1:19,180:7,51,180:" +
"11,-1:4,180:2,-1:8,180:11,51,180:4,158:2,-1:19,152:3,140,152:15,-1:4,152:2," +
"-1:8,152:4,140,152:13,-1:19,152:15,88,152:3,-1:4,152:2,-1:8,152:9,88,152:8," +
"-1:19,152:6,90,152:12,-1:4,152:2,-1:8,152:7,90,152:10,-1:19,152:6,141,152:1" +
"2,-1:4,152:2,-1:8,152:7,141,152:10,-1:19,152:7,91,152:11,-1:4,152:2,-1:8,15" +
"2:11,91,152:6,-1:19,152:10,142,152:8,-1:4,152:2,-1:8,152:6,142,152:11,-1:19" +
",152,143,152:17,-1:4,152:2,-1:8,152:10,143,152:7,-1:19,152:3,92,152:15,-1:4" +
",152:2,-1:8,152:4,92,152:13,-1:19,152:18,93,-1:4,152:2,-1:8,152:3,93,152:14" +
",-1:19,152:10,144,152:8,-1:4,152:2,-1:8,152:6,144,152:11,-1:19,145,152:12,1" +
"45,152:5,-1:4,152:2,-1:8,152:18,-1:19,152:7,94,152:11,-1:4,152:2,-1:8,152:1" +
"1,94,152:6,-1:19,180:3,105,180:10,165,180:4,-1:4,180:2,-1:8,180:4,105,180:3" +
",165,180:7,158:2,-1:19,152:12,110,152:6,-1:4,152:2,-1:8,152:5,110,152:12,-1" +
":19,152:7,130,152:11,-1:4,152:2,-1:8,152:11,130,152:6,-1:19,152:5,132,152:1" +
"3,-1:4,152:2,-1:8,132,152:17,-1:19,152:14,136,152:4,-1:4,152:2,-1:8,152:8,1" +
"36,152:9,-1:19,180:3,107,180:10,109,180:4,-1:4,180:2,-1:8,180:4,107,180:3,1" +
"09,180:7,158:2,-1:19,152:14,139,152:4,-1:4,152:2,-1:8,152:8,139,152:9,-1:19" +
",180:2,111,180:16,-1:4,180:2,-1:8,180:12,111,180:3,158:2,-1:19,152:5,112,11" +
"4,152:12,-1:4,152:2,-1:8,112,152:6,114,152:10,-1:19,180:3,113,180:15,-1:4,1" +
"80:2,-1:8,180:4,113,180:11,158:2,-1:19,152:6,148,149,152:11,-1:4,152:2,-1:8" +
",152:7,148,152:3,149,152:6,-1:19,152:3,118,152:10,120,152:4,-1:4,152:2,-1:8" +
",152:4,118,152:3,120,152:9,-1:19,180:7,115,180:11,-1:4,180:2,-1:8,180:11,11" +
"5,180:4,158:2,-1:19,152:14,150,152:4,-1:4,152:2,-1:8,152:8,150,152:9,-1:19," +
"180:5,117,180:13,-1:4,180:2,-1:8,117,180:15,158:2,-1:19,152:12,122,152:6,-1" +
":4,152:2,-1:8,152:5,122,152:12,-1:19,180:6,171,180:12,-1:4,180:2,-1:8,180:7" +
",171,180:8,158:2,-1:19,180:14,119,180:4,-1:4,180:2,-1:8,180:8,119,180:7,158" +
":2,-1:19,180:7,121,180:11,-1:4,180:2,-1:8,180:11,121,180:4,158:2,-1:19,180:" +
"5,172,180:13,-1:4,180:2,-1:8,172,180:15,158:2,-1:19,180:17,173,180,-1:4,180" +
":2,-1:8,180:13,173,180:2,158:2,-1:19,180:14,123,180:4,-1:4,180:2,-1:8,180:8" +
",123,180:7,158:2,-1:19,180:10,175,180:8,-1:4,180:2,-1:8,180:6,175,180:9,158" +
":2,-1:19,180:7,125,180:11,-1:4,180:2,-1:8,180:11,125,180:4,158:2,-1:19,180:" +
"7,127,180:11,-1:4,180:2,-1:8,180:11,127,180:4,158:2,-1:19,180:14,176,180:4," +
"-1:4,180:2,-1:8,180:8,176,180:7,158:2,-1:19,180:3,177,180:15,-1:4,180:2,-1:" +
"8,180:4,177,180:11,158:2,-1:19,180:6,129,180:12,-1:4,180:2,-1:8,180:7,129,1" +
"80:8,158:2,-1:19,180:10,131,180:8,-1:4,180:2,-1:8,180:6,131,180:9,158:2,-1:" +
"19,180,178,180:17,-1:4,180:2,-1:8,180:10,178,180:5,158:2,-1:19,180:10,179,1" +
"80:8,-1:4,180:2,-1:8,180:6,179,180:9,158:2,-1:19,133,180:12,133,180:5,-1:4," +
"180:2,-1:8,180:16,158:2,-1:19,180:6,160,162,180:11,-1:4,180:2,-1:8,180:7,16" +
"0,180:3,162,180:4,158:2,-1:19,180:5,166,167,180:12,-1:4,180:2,-1:8,166,180:" +
"6,167,180:8,158:2,-1:19,180:14,169,180:4,-1:4,180:2,-1:8,180:8,169,180:7,15" +
"8:2,-1:19,180:12,170,180:6,-1:4,180:2,-1:8,180:5,170,180:10,158:2");

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
						
					case -2:
						break;
					case 2:
						{ 
  curr_lineno++;
}
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.PLUS, yytext()); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.MINUS, yytext()); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.MULT, yytext()); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.DIV, yytext()); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.NEG, yytext()); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.LT, yytext()); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.EQ, yytext()); }
					case -10:
						break;
					case 10:
						{
  return new Symbol(TokenConstants.ERROR, yytext()); 
}
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.COMMA, yytext()); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.DOT, yytext()); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.COLON, yytext()); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.SEMI, yytext()); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.LPAREN, yytext()); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.RPAREN, yytext()); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.LBRACE, yytext()); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.RBRACE, yytext()); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.AT, yytext()); }
					case -20:
						break;
					case 20:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -21:
						break;
					case 21:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -22:
						break;
					case 22:
						{
  // start string
  string_buf.delete(0, string_buf.length()); //reset buffer
  yybegin(STRING);
}
					case -23:
						break;
					case 23:
						{ }
					case -24:
						break;
					case 24:
						{ /* vertical tab */ }
					case -25:
						break;
					case 25:
						{ }
					case -26:
						break;
					case 26:
						{
  return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}
					case -27:
						break;
					case 27:
						{}
					case -28:
						break;
					case 28:
						{ 
  return new Symbol(TokenConstants.ERROR, "Unmatched *)"); 
}
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.ASSIGN, yytext()); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LE, yytext()); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.DARROW, yytext()); }
					case -32:
						break;
					case 32:
						{ 
  yybegin(COMMENT);
  comment_count++;
}
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.FI, yytext()); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.IF, yytext()); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.IN, yytext()); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.OF, yytext()); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.LET, yytext()); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.NEW, yytext()); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.NOT, yytext()); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.BOOL_CONST, (Boolean)true); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.THEN, yytext()); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.ELSE, yytext()); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.ESAC, yytext()); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.LOOP, yytext()); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CASE, yytext()); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.POOL, yytext()); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.BOOL_CONST, (Boolean)false); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.CLASS, yytext()); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.WHILE, yytext()); }
					case -50:
						break;
					case 50:
						{ return new Symbol(TokenConstants.ISVOID, yytext()); }
					case -51:
						break;
					case 51:
						{ return new Symbol(TokenConstants.INHERITS, yytext()); }
					case -52:
						break;
					case 52:
						{ }
					case -53:
						break;
					case 53:
						{ 
  comment_count--;
  if (comment_count == 0) {
    yybegin(YYINITIAL);
  }
}
					case -54:
						break;
					case 54:
						{ 
  comment_count++;
}
					case -55:
						break;
					case 55:
						{ 
  string_buf.append(yytext());
}
					case -56:
						break;
					case 56:
						{
  curr_lineno++;
  yybegin(YYINITIAL);
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); 
}
					case -57:
						break;
					case 57:
						{
  // end string
  yybegin(YYINITIAL);
  if (string_buf.toString().length() >= MAX_STR_CONST) {
    return new Symbol(TokenConstants.ERROR, "String constant too long"); 
  }
  return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
}
					case -58:
						break;
					case 58:
						{
  yybegin(NULL);
  return new Symbol(TokenConstants.ERROR, "String contains null character."); 
}
					case -59:
						break;
					case 59:
						{ string_buf.append('\t'); }
					case -60:
						break;
					case 60:
						{ string_buf.append('\f'); }
					case -61:
						break;
					case 61:
						{ string_buf.append('\013'); /*  */ }
					case -62:
						break;
					case 62:
						{ string_buf.append('\015'); /* carriage return */ }
					case -63:
						break;
					case 63:
						{ string_buf.append('\022'); /* */ }
					case -64:
						break;
					case 64:
						{ string_buf.append('\033'); /* escape */  }
					case -65:
						break;
					case 65:
						{ 
  string_buf.append('\n'); 
  curr_lineno++;
}
					case -66:
						break;
					case 66:
						{ 
  string_buf.append(yytext().replace("\\", ""));
}
					case -67:
						break;
					case 67:
						{ 
  /* escaped quote */
  string_buf.append('\"');
}
					case -68:
						break;
					case 68:
						{
  yybegin(NULL);
  return new Symbol(TokenConstants.ERROR, "String contains escaped null character."); 
}
					case -69:
						break;
					case 69:
						{ 
  /* escaped backslash */
  string_buf.append('\\');
}
					case -70:
						break;
					case 70:
						{ 
  string_buf.append('\n'); 
}
					case -71:
						break;
					case 71:
						{ string_buf.append('\b'); }
					case -72:
						break;
					case 72:
						{
  curr_lineno++;
  yybegin(YYINITIAL);
}
					case -73:
						break;
					case 73:
						{
  yybegin(YYINITIAL);
}
					case -74:
						break;
					case 75:
						{
  return new Symbol(TokenConstants.ERROR, yytext()); 
}
					case -75:
						break;
					case 76:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -76:
						break;
					case 77:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.FI, yytext()); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.IF, yytext()); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.IN, yytext()); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.OF, yytext()); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.LET, yytext()); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.NEW, yytext()); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.NOT, yytext()); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.THEN, yytext()); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.ELSE, yytext()); }
					case -86:
						break;
					case 87:
						{ return new Symbol(TokenConstants.ESAC, yytext()); }
					case -87:
						break;
					case 88:
						{ return new Symbol(TokenConstants.LOOP, yytext()); }
					case -88:
						break;
					case 89:
						{ return new Symbol(TokenConstants.CASE, yytext()); }
					case -89:
						break;
					case 90:
						{ return new Symbol(TokenConstants.POOL, yytext()); }
					case -90:
						break;
					case 91:
						{ return new Symbol(TokenConstants.CLASS, yytext()); }
					case -91:
						break;
					case 92:
						{ return new Symbol(TokenConstants.WHILE, yytext()); }
					case -92:
						break;
					case 93:
						{ return new Symbol(TokenConstants.ISVOID, yytext()); }
					case -93:
						break;
					case 94:
						{ return new Symbol(TokenConstants.INHERITS, yytext()); }
					case -94:
						break;
					case 95:
						{ }
					case -95:
						break;
					case 96:
						{ 
  string_buf.append(yytext());
}
					case -96:
						break;
					case 98:
						{
  return new Symbol(TokenConstants.ERROR, yytext()); 
}
					case -97:
						break;
					case 99:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -98:
						break;
					case 100:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -99:
						break;
					case 101:
						{ }
					case -100:
						break;
					case 103:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -101:
						break;
					case 104:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -102:
						break;
					case 105:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -103:
						break;
					case 106:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -104:
						break;
					case 107:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -105:
						break;
					case 108:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -106:
						break;
					case 109:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -107:
						break;
					case 110:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -108:
						break;
					case 111:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -109:
						break;
					case 112:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -110:
						break;
					case 113:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -111:
						break;
					case 114:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -112:
						break;
					case 115:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -113:
						break;
					case 116:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -114:
						break;
					case 117:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -115:
						break;
					case 118:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -116:
						break;
					case 119:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -117:
						break;
					case 120:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -118:
						break;
					case 121:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -119:
						break;
					case 122:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -120:
						break;
					case 123:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -121:
						break;
					case 124:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -122:
						break;
					case 125:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -123:
						break;
					case 126:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -124:
						break;
					case 127:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -125:
						break;
					case 128:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -126:
						break;
					case 129:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -127:
						break;
					case 130:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -128:
						break;
					case 131:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -129:
						break;
					case 132:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -130:
						break;
					case 133:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -131:
						break;
					case 134:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -132:
						break;
					case 135:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -133:
						break;
					case 136:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -134:
						break;
					case 137:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -135:
						break;
					case 138:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -136:
						break;
					case 139:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -137:
						break;
					case 140:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -138:
						break;
					case 141:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -139:
						break;
					case 142:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -140:
						break;
					case 143:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -141:
						break;
					case 144:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -142:
						break;
					case 145:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -143:
						break;
					case 146:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -144:
						break;
					case 147:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -145:
						break;
					case 148:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -146:
						break;
					case 149:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -147:
						break;
					case 150:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -148:
						break;
					case 151:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -149:
						break;
					case 152:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -150:
						break;
					case 153:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -151:
						break;
					case 154:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -152:
						break;
					case 155:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -153:
						break;
					case 156:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -154:
						break;
					case 157:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -155:
						break;
					case 158:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -156:
						break;
					case 159:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -157:
						break;
					case 160:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -158:
						break;
					case 161:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -159:
						break;
					case 162:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -160:
						break;
					case 163:
						{
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}
					case -161:
						break;
					case 164:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -162:
						break;
					case 165:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -163:
						break;
					case 166:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -164:
						break;
					case 167:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -165:
						break;
					case 168:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -166:
						break;
					case 169:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -167:
						break;
					case 170:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -168:
						break;
					case 171:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -169:
						break;
					case 172:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -170:
						break;
					case 173:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -171:
						break;
					case 174:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -172:
						break;
					case 175:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -173:
						break;
					case 176:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -174:
						break;
					case 177:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -175:
						break;
					case 178:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -176:
						break;
					case 179:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -177:
						break;
					case 180:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -178:
						break;
					case 181:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -179:
						break;
					case 182:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -180:
						break;
					case 183:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -181:
						break;
					case 184:
						{
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}
					case -182:
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
