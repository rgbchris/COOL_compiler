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
WHITESPACE = [\ \f\t]
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

<YYINITIAL> "+"  { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL> "-"  { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL> "*"  { return new Symbol(TokenConstants.MULT); }
<YYINITIAL> "/"  { return new Symbol(TokenConstants.DIV); }
<YYINITIAL> "~"  { return new Symbol(TokenConstants.NEG); }
<YYINITIAL> "<"  { return new Symbol(TokenConstants.LT); }
<YYINITIAL> "<=" { return new Symbol(TokenConstants.LE); }
<YYINITIAL> "=>" { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL> "="  { return new Symbol(TokenConstants.EQ); }
<YYINITIAL> ","  { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL> "."  { return new Symbol(TokenConstants.DOT); }
<YYINITIAL> ":"  { return new Symbol(TokenConstants.COLON); }
<YYINITIAL> ";"  { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL> "<-" { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL> "("  { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL> ")"  { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL> "{"  { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL> "}"  { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL> "@"  { return new Symbol(TokenConstants.AT); }

<YYINITIAL> {TRUE_TEST}  { return new Symbol(TokenConstants.BOOL_CONST, (Boolean)true); }
<YYINITIAL> {FALSE_TEST} { return new Symbol(TokenConstants.BOOL_CONST, (Boolean)false); }
<YYINITIAL> {CLASS}     { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL> {ELSE}      { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL> {FI}        { return new Symbol(TokenConstants.FI); }
<YYINITIAL> {IF}        { return new Symbol(TokenConstants.IF); }
<YYINITIAL> {IN}        { return new Symbol(TokenConstants.IN); }
<YYINITIAL> {INHERITS}  { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL> {LET}       { return new Symbol(TokenConstants.LET); }
<YYINITIAL> {LOOP}      { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL> {POOL}      { return new Symbol(TokenConstants.POOL); }
<YYINITIAL> {THEN}      { return new Symbol(TokenConstants.THEN); }
<YYINITIAL> {WHILE}     { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL> {CASE}      { return new Symbol(TokenConstants.CASE); }
<YYINITIAL> {ESAC}      { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL> {NEW}       { return new Symbol(TokenConstants.NEW); }
<YYINITIAL> {OF}        { return new Symbol(TokenConstants.OF); }
<YYINITIAL> {NOT}       { return new Symbol(TokenConstants.NOT); }
<YYINITIAL> {ISVOID}    { return new Symbol(TokenConstants.ISVOID); }

<YYINITIAL> \" {
  /* start string */
  string_buf.delete(0, string_buf.length()); //reset buffer
  yybegin(STRING);
}

<STRING> \" {
  /* end string */
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
<STRING> \\n { string_buf.append('\n'); /* newline */ }
<STRING> \\b { string_buf.append('\b'); /* backspace */ }
<STRING> \\t|\t { string_buf.append('\t'); /* tab */ }
<STRING> \\f|\f { string_buf.append('\f'); /* formfeed */ }
<STRING> \013 { string_buf.append('\013'); /* vertical tab */ }
<STRING> \015 { string_buf.append('\015'); /* carriage return */ }
<STRING> \022 { string_buf.append('\022'); /* ^R device control 2 */ }
<STRING> \033 { string_buf.append('\033'); /* escape */  }

<STRING> \\[^btfn\\\"] { 
  string_buf.append(yytext().replace("\\", ""));
}

<STRING> ([a-zA-Z0-9!#$%&@^'=<>()\[\]?+-:_*]|[\ ])* { 
  string_buf.append(yytext());
}

<YYINITIAL> \015 { /* carriage return */ }
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

