package edu.binghamton.cs571;

public class UglyRegexpParser {

  Token _lookahead;
  Scanner _scanner;

  UglyRegexpParser(Scanner scanner) {
    _scanner = scanner;
    _lookahead = _scanner.nextToken();
  }


  /** parse a sequence of lines containing ugly-regexp's; for each
   *  ugly regexp print out the corresponding standard regexp.
   *  If there is an error, print diagnostic and continue with
   *  next line.
   */
  public void parse() {
    while (_lookahead.kind != Token.Kind.EOF) {
      try {
        String out = uglyRegexp();
        if (check(Token.Kind.NL)) System.out.println(out);
        match(Token.Kind.NL);
      }
      catch (ParseException e) {
        System.err.println(e.getMessage());
        while (_lookahead.kind != Token.Kind.NL) {
          _lookahead = _scanner.nextToken();
        }
        _lookahead = _scanner.nextToken();
      }
    }
  }

  /** Return standard syntax regexp corresponding to ugly-regexp
   *  read from _scanner.
   */
  //IMPLEMENT THIS FUNCTION and any necessary functions it may call.
  
  
  private String uglyRegexp() {
	  
	String t = term();	 
	String temp = t;
    String temp2 = exprRest(t); //placeholder for compile
	return temp2;
  }
  
 private String exprRest(String valueSoFar)
 {
	 if(_lookahead.kind == Token.Kind.dotOP){
		 match(Token.Kind.dotOP);
		 String t = term();
		 return  exprRest("(" + valueSoFar + t+ ")") ;
	 }
	 else {
		 return valueSoFar;
	 }
 }
  
  private String term() {
	  
	  String f = factor();
	  return termRest(f);
	  }
  
  private String termRest(String valueSoFar){
	 if(_lookahead.kind == Token.Kind.plusOP){
		 match(Token.Kind.plusOP);
		 String f = factor();
		 return "("+ termRest(valueSoFar +"|" + f) + ")";
	 }
	 else{
		 return valueSoFar;
	 }
	  
  }
    
 private String factor() {
	  
	  String s = charWord();
	  return factorRest(s);
	  }
 
 private String factorRest(String valueSoFar){
	 if(_lookahead.kind == Token.Kind.multOP){
		 match(Token.Kind.multOP);
		 String s = charWord();
		 return factorRest(valueSoFar + s);
	 }
	 else{
		 return valueSoFar;
	 }
 }

 private String charWord(){
	 if(_lookahead.kind == Token.Kind.CHARS){
		 match(Token.Kind.CHARS); 
	 }
	 else if(_lookahead.kind == Token.Kind.openingBRACES){
		 match(Token.Kind.openingBRACES);
		 String reparse = uglyRegexp();
		 match(Token.Kind.closingBRACES);
		 return ("(" + reparse + ")");
		 
	 }
	 else if (_lookahead.kind == Token.Kind.multOP){
		 match(Token.Kind.multOP);
		 String parse_agian = charWord();
		 return parse_agian + "*";
	 }
		 
	 String char_Word_Rest = charWordRest();
	 match(Token.Kind.closingBRACES);
	 return char_Word_Rest;
	 	 
 }
 
 private String charWordRest() {
		if(_lookahead.kind == Token.Kind.openingBRACES){
			match(Token.Kind.openingBRACES);
		}
		String char_a = alpha();
		String char_b = alphaRest(char_a);
		return "[" + char_b + "]";
	}
 
 
 
 private String alpha() {
		
	 String alpha = _lookahead.lexeme;
	 if(_lookahead.kind == Token.Kind.CHAR){
	
			match(Token.Kind.CHAR);
			return alpha;
	 }
	 else{
	 
	 if(_lookahead.kind == Token.Kind.CHARS){
		 
		match(Token.Kind.CHARS);
		
	}else    if(_lookahead.kind == Token.Kind.EOF){
	 
		match(Token.Kind.EOF);
		
	}else
    
    if(_lookahead.kind == Token.Kind.NL){
   	 
		match(Token.Kind.NL);
		
	}else
    
    if(_lookahead.kind == Token.Kind.dotOP){
      	 
		match(Token.Kind.dotOP);
		
	}else
    
    if(_lookahead.kind == Token.Kind.plusOP){
      	 
		match(Token.Kind.plusOP);
		
	}else
    
    if(_lookahead.kind == Token.Kind.multOP){
      	 
		match(Token.Kind.multOP);
		
    }else
    
    if(_lookahead.kind == Token.Kind.commaOP){
      	 
		match(Token.Kind.commaOP);
		
	}else
    
    if(_lookahead.kind == Token.Kind.openingBRACES){
      	 
		match(Token.Kind.openingBRACES);
		
	}else
    if(_lookahead.kind == Token.Kind.closingBRACES){
      	 
		match(Token.Kind.closingBRACES);
		}
    
    String alpha_string = "\\"+alpha;
    return alpha_string;
	
	 }
} 

 private String alphaRest(String a) {
		if (_lookahead.kind == Token.Kind.commaOP) {
			match(Token.Kind.commaOP);
			String b = alpha();
			String new_string = alphaRest(a + b);
			return new_string;
		} 
		else{
		    return a;
		}
	}
 
  
  
  
  

  //Utility functions which may be useful for parsing or translation

  /** Return s with first char escaped using a '\' if it is
   * non-alphanumeric.
   */
  private static String quote(String s) {
    return (Character.isLetterOrDigit(s.charAt(0))) ? s : "\\" + s;
  }

  /** Return true iff _lookahead.kind is equal to kind. */
  private boolean check(Token.Kind kind) {
    return check(kind, null);
  }

  /** Return true iff lookahead kind and lexeme are equal to
   *  corresponding args.  Note that if lexeme is null, then it is not
   *  used in the match.
   */
  private boolean check(Token.Kind kind, String lexeme) {
    return (_lookahead.kind == kind &&
            (lexeme == null || _lookahead.lexeme.equals(lexeme)));
  }

  /** If lookahead kind is equal to kind, then set lookahead to next
   *  token; else throw a ParseException.
   */
  private void match(Token.Kind kind) {
    match(kind, null);
  }

  /** If lookahead kind and lexeme are not equal to corresponding
   *  args, then set lookahead to next token; else throw a
   *  ParseException.  Note that if lexeme is null, then it is
   *  not used in the match.
   */
  private void match(Token.Kind kind, String lexeme) {
    if (check(kind, lexeme)) {
      _lookahead = _scanner.nextToken();
    }
    else {
      String expected = (lexeme == null) ? kind.toString() : lexeme;
      String message = String.format("%s: syntax error at '%s', expecting '%s'",
                                     _lookahead.coords, _lookahead.lexeme,
                                     expected);
      throw new ParseException(message);
    }
  }

  private static class ParseException extends RuntimeException {
    ParseException(String message) {
      super(message);
    }
  }


  /** main program: parses and translates ugly-regexp's contained in
   *  the file specified by it's single command-line argument.
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.format("usage: java %s FILENAME\n",
                        UglyRegexpParser.class.getName());
      System.exit(1);
    }
    Scanner scanner =
      ("-".equals(args[0])) ? new Scanner() : new Scanner(args[0]);
    (new UglyRegexpParser(scanner)).parse();
  }


}
