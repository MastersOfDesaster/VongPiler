package vong.piler.her.enums;

import org.apache.commons.lang.StringEscapeUtils;

public enum TokenTypeEnum {
    // whitespace
    COMMENT("((:X|:zipper_mouth_face:).*?(\n)).*"), WHITESPACE("( |\t).*"), NEWLINE("(\n).*"),
    
    // program
    START("(was ist das für 1 code\\?).*", "was ist das für 1 code?"), // start
    END("(1 nicer!!!|1 n:icecream:r!!!).*", "1 nicer!!!|1 n:icecream:r!!!"), // end
    
    // function
    CMD("(was ist das für 1).*", "was ist das für 1"), // call function
    PSTART("(vong).*", "vong"), // start of parameters
    PNEXT("((,|\\?|\\+){1}).*", ",|?|+"), // next parameter
    PEND("(her\\?).*", "her?"), // end of parameter
    
    // print
    PRINT("(gieb).*", "gieb"),
    AAL("(halo i bims!!!).*", "halo i bims!!!"),
    
    // if
    IFSTART("(bist du).*", "bist du"),
    IFEND("(real rap).*", "real rap"),
    
    // jump
    HASHTAG("#\\b([a-zA-Z]{1}[0-9a-zA-Z_]{0,31})\\b.*", "#"),
    GOTOSTART("(g zu).*", "g zu"),
    GOTOEND("(du larry!!!).*", "du larry!!!"),
    
    // declare variable
    VSTART("(i bims 1).*", "i bims 1"), // declare variable
    ASSI("(gönn dir).*", "gönn dir"), // assign value
    VEND("(!!!).*", "!!!"), // end of variable declaration

    // types
    TYPE("(zal\\h|word\\h|isso\\h).*", "zal|word|isso"),
    
    // input
    INPUT("(1gabe).*", "1gabe"), // screen input

    // constants
    CONST_ZAL("([-+]?[0-9]*\\.?[0-9]+).*", "const_zal"), // constant of type zal (number)
    CONST_ISSO("(yup|nope).*", "const_isso"), // constant of type isso (boolean)
    CONST_WORD("\\\"(.*?)\\\".*", "const_word"), // constant of type word (string)

    // name / identifier
    NAME("((:{1}(\\w|\\+|\\|){2,}:{1})+|(\\b([a-zA-Z]{1}[0-9a-zäöüßA-Z_ÄÖÜ]{0,31})\\b)).*", "name"),
	
	FNAME("","fname");
    
    private String regEx;
    private String label;

    TokenTypeEnum(String regEx) {
        this.regEx = regEx;
    }
    
    TokenTypeEnum(String regEx, String label) {
        this.regEx = regEx;
        this.label = label;
    }

    public String getRegEx() {
        return this.regEx;
    }
    
    public String getLabel() {
        return this.label;
    }

    public static String toMarkdown() {
        String ebnf = "";
        ebnf += "# Lexer Grammar\n\n";
        ebnf += "|Token|Regular Expression|\n";
        ebnf += "|-----|------------------|\n";
        for (TokenTypeEnum tokenType : TokenTypeEnum.values()) {
            String regex = StringEscapeUtils.escapeJava(tokenType.getRegEx());
            regex = regex.replaceAll("\\|", "\\\\|");
            ebnf += "|" + tokenType.name() + "|" + regex + "|\n";
        }
        return ebnf;
    }
}
