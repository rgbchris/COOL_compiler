/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

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
%eofval}

%class CoolLexer
%cup
%line
%state COMMENT
%state STRING 
%state NULL 

UPPERCASEALPHA = [A-Z]
ALPHA = [a-zA-Z]
DIGIT = [0-9]
ALPHANUMERIC = [a-zA-Z0-9]
WHITESPACE = [\ \f\t]
STRING_TEXT = (\\\"|\\\n|[^\n\"])*
SINGLE_LINE_COMMENT = (--)[^\n\r]*
TRUE_TEST = t[rR][uU][eE]
FALSE_TEST = f[aA][lL][sS][eE]

CLASS = [cC][lL][aA][sS][sS]
ELSE = [eE][lL][sS][eE]
FI = [fF][iI]
IF = [iI][fF]
IN = [iI][nN]
INHERITS = [iI][nN][hH][eE][rR][iI][tT][sS]
LET = [lL][eE][tT]
LOOP = [lL][oO][oO][pP]
POOL = [pP][oO][oO][lL]
THEN = [tT][hH][eE][nN]
WHILE = [wW][hH][iI][lL][eE]
CASE = [cC][aA][sS][eE]
ESAC = [eE][sS][aA][cC]
NEW = [nN][eE][wW]
OF = [oO][fF]
NOT = [nN][oO][tT]
ISVOID = [iI][sS][vV][oO][iI][dD]

%%

<YYINITIAL,COMMENT> \n { 
  curr_lineno++;
}

<YYINITIAL> "+"  { return new Symbol(TokenConstants.PLUS, yytext()); }
<YYINITIAL> "-"  { return new Symbol(TokenConstants.MINUS, yytext()); }
<YYINITIAL> "*"  { return new Symbol(TokenConstants.MULT, yytext()); }
<YYINITIAL> "/"  { return new Symbol(TokenConstants.DIV, yytext()); }
<YYINITIAL> "~"  { return new Symbol(TokenConstants.NEG, yytext()); }
<YYINITIAL> "<"  { return new Symbol(TokenConstants.LT, yytext()); }
<YYINITIAL> "<=" { return new Symbol(TokenConstants.LE, yytext()); }
<YYINITIAL> "=>" { return new Symbol(TokenConstants.DARROW, yytext()); }
<YYINITIAL> "="  { return new Symbol(TokenConstants.EQ, yytext()); }
<YYINITIAL> ","  { return new Symbol(TokenConstants.COMMA, yytext()); }
<YYINITIAL> "."  { return new Symbol(TokenConstants.DOT, yytext()); }
<YYINITIAL> ":"  { return new Symbol(TokenConstants.COLON, yytext()); }
<YYINITIAL> ";"  { return new Symbol(TokenConstants.SEMI, yytext()); }
<YYINITIAL> "<-" { return new Symbol(TokenConstants.ASSIGN, yytext()); }
<YYINITIAL> "("  { return new Symbol(TokenConstants.LPAREN, yytext()); }
<YYINITIAL> ")"  { return new Symbol(TokenConstants.RPAREN, yytext()); }
<YYINITIAL> "{"  { return new Symbol(TokenConstants.LBRACE, yytext()); }
<YYINITIAL> "}"  { return new Symbol(TokenConstants.RBRACE, yytext()); }
<YYINITIAL> "@"  { return new Symbol(TokenConstants.AT, yytext()); }

<YYINITIAL> {TRUE_TEST}  { return new Symbol(TokenConstants.BOOL_CONST, (Boolean)true); }
<YYINITIAL> {FALSE_TEST} { return new Symbol(TokenConstants.BOOL_CONST, (Boolean)false); }

<YYINITIAL> {CLASS}     { return new Symbol(TokenConstants.CLASS, yytext()); }
<YYINITIAL> {ELSE}      { return new Symbol(TokenConstants.ELSE, yytext()); }
<YYINITIAL> {FI}        { return new Symbol(TokenConstants.FI, yytext()); }
<YYINITIAL> {IF}        { return new Symbol(TokenConstants.IF, yytext()); }
<YYINITIAL> {IN}        { return new Symbol(TokenConstants.IN, yytext()); }
<YYINITIAL> {INHERITS}  { return new Symbol(TokenConstants.INHERITS, yytext()); }
<YYINITIAL> {LET}       { return new Symbol(TokenConstants.LET, yytext()); }
<YYINITIAL> {LOOP}      { return new Symbol(TokenConstants.LOOP, yytext()); }
<YYINITIAL> {POOL}      { return new Symbol(TokenConstants.POOL, yytext()); }
<YYINITIAL> {THEN}      { return new Symbol(TokenConstants.THEN, yytext()); }
<YYINITIAL> {WHILE}     { return new Symbol(TokenConstants.WHILE, yytext()); }
<YYINITIAL> {CASE}      { return new Symbol(TokenConstants.CASE, yytext()); }
<YYINITIAL> {ESAC}      { return new Symbol(TokenConstants.ESAC, yytext()); }
<YYINITIAL> {NEW}       { return new Symbol(TokenConstants.NEW, yytext()); }
<YYINITIAL> {OF}        { return new Symbol(TokenConstants.OF, yytext()); }
<YYINITIAL> {NOT}       { return new Symbol(TokenConstants.NOT, yytext()); }
<YYINITIAL> {ISVOID}    { return new Symbol(TokenConstants.ISVOID, yytext()); }

<YYINITIAL> \" {
  // start string
  string_buf.delete(0, string_buf.length()); //reset buffer
  yybegin(STRING);
}

<STRING> \" {
  // end string
  yybegin(YYINITIAL);
  if (string_buf.toString().length() >= MAX_STR_CONST) {
    return new Symbol(TokenConstants.ERROR, "String constant too long"); 
  }
  return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
}

<STRING> \0 {
  yybegin(NULL);
  return new Symbol(TokenConstants.ERROR, "String contains null character."); 
}

<STRING> \\\x00 {
  yybegin(NULL);
  return new Symbol(TokenConstants.ERROR, "String contains escaped null character."); 
}

<NULL> .*\" {
  yybegin(YYINITIAL);
}

<NULL> .*\n {
  curr_lineno++;
  yybegin(YYINITIAL);
}

<STRING> \\\\ { 
  /* escaped backslash */
  string_buf.append('\\');
}

<STRING> \\\" { 
  /* escaped quote */
  string_buf.append('\"');
}

<STRING> \n {
  curr_lineno++;
  yybegin(YYINITIAL);
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); 
}

<STRING> \\\n { 
  string_buf.append('\n'); 
  curr_lineno++;
}

<STRING> \\n { 
  string_buf.append('\n'); 
}
<STRING> \\n { string_buf.append('\n'); }
<STRING> \\b { string_buf.append('\b'); }
<STRING> \\t|\t { string_buf.append('\t'); }
<STRING> \\f|\f { string_buf.append('\f'); }
<STRING> \013 { string_buf.append('\013'); /*  */ }
<STRING> \015 { string_buf.append('\015'); /* carriage return */ }
<STRING> \022 { string_buf.append('\022'); /* */ }
<STRING> \033 { string_buf.append('\033'); /* escape */  }

<STRING> \\[^btfn\\\"] { 
  string_buf.append(yytext().replace("\\", ""));
}

<STRING> ([a-zA-Z0-9!#$%&@^'=<>()\[\]?+-:_*]|[\ ])* { 
  string_buf.append(yytext());
}

<YYINITIAL> \015 { }
<YYINITIAL> \013 { /* vertical tab */ }

<YYINITIAL> {SINGLE_LINE_COMMENT}  {}

<YYINITIAL> "(*"  { 
  yybegin(COMMENT);
  comment_count++;
}
<COMMENT> "(*"  { 
  comment_count++;
}

<COMMENT> "*)"  { 
  comment_count--;
  if (comment_count == 0) {
    yybegin(YYINITIAL);
  }
}

<COMMENT> [^] { 
  /* [^] - allows for catching binary chars. */  
}

<YYINITIAL> "*)"  { 
  return new Symbol(TokenConstants.ERROR, "Unmatched *)"); 
}

<YYINITIAL> {WHITESPACE}+ { }

<YYINITIAL> {UPPERCASEALPHA}({ALPHA}|{DIGIT}|_)* {
  return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL> {ALPHA}({ALPHA}|{DIGIT}|_)* {
  return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL> {DIGIT}+ {
  return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
}

<YYINITIAL> [\ \n\s] { }

. {
  return new Symbol(TokenConstants.ERROR, yytext()); 
}

