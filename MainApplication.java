import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.border.Border;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.JFileChooser;
import java.awt.SystemColor;

public class MainApplication {
//End of import

        static String datatypeholder; //hold the data type for semantic applications.

        public static boolean isAlpha(char c) {
            // 'A' <= c <= 'Z' or 'a' <= c <= 'z'
            return (c >= 'A' && c  <= 'Z') || (c  >='a' && c <= 'z');
        }
        // isNumber checks if character is a number.
        public static boolean isNumber(char c) {
            // '0' <= c <= '9'
            return (c  >= '0' && c  <= '9');
        }

        JLabel resulttext, codetext, finish, filename;
        JButton openfile, btnLexer, btnSyntax, btnSemantic, BtnClear;

        File file;
        static String z;
        static String input;
        private JFrame mainFrame;
        Color bgtext = new Color(250, 255, 184); //setting custom color for text output background

        //initialization of JFrame, design stage of code

        private void initialize(){

            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

            mainFrame = new JFrame();
            mainFrame.getContentPane().setBackground(SystemColor.inactiveCaption);
            mainFrame.setBounds(100, 100, 657, 275);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.getContentPane().setLayout(null);
            mainFrame.setVisible(true);


            openfile = new JButton("Open File");
            openfile.setBounds(10, 20, 150, 35);
            openfile.setBorder(border);
            mainFrame.getContentPane().add(openfile);

            btnLexer = new JButton("Lexical Analysis");
            btnLexer.setBounds(10, 60, 150, 35);
            btnLexer.setBorder(border);
            mainFrame.getContentPane().add(btnLexer);

            btnSyntax = new JButton("Syntax Analysis");
            btnSyntax.setBounds(10, 100, 150, 35);
            btnSyntax.setBorder(border);
            mainFrame.getContentPane().add(btnSyntax);

            btnSemantic = new JButton("Semantic Analysis");
            btnSemantic.setBounds(10, 140, 150, 35);
            btnSemantic.setBorder(border);
            mainFrame.getContentPane().add(btnSemantic);

            BtnClear = new JButton("Clear Text");
            BtnClear.setBounds(10, 180, 150, 35);
            BtnClear.setBorder(border);
            mainFrame.getContentPane().add(BtnClear);

            resulttext = new JLabel();
            resulttext.setBackground(bgtext);
            resulttext.setOpaque(true);
            resulttext.setBounds(175, 23, 450, 30);
            resulttext.setText(" Result: ");
            mainFrame.getContentPane().add(resulttext);

            //Creating new Jpanel for the center panel

            JPanel centerJPanel = new JPanel();
            centerJPanel.setLayout(new GridLayout(0,1));
            centerJPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            centerJPanel.setBackground(bgtext);
            centerJPanel.setBounds(175, 60, 450, 155);
            mainFrame.getContentPane().add(centerJPanel);

            //adding filename, codetext and final message into centerJPanel

            filename = new JLabel();
            filename.setBounds(10, 20, 100, 30);
            filename.setText("Filename: ");
            centerJPanel.add(filename);

            codetext = new JLabel();
            codetext.setBounds(10, 60, 100, 30);
            codetext.setText("Code: ");
            centerJPanel.add(codetext);

            finish = new JLabel();
            finish.setBounds(10, 100, 100, 30);
            finish.setText("Feedback: ");
            centerJPanel.add(finish);

            // end of designing

            //start of button event and button disabling

            openfile.setEnabled(true);
            btnLexer.setEnabled(false);
            btnSyntax.setEnabled(false);
            btnSemantic.setEnabled(false);
            BtnClear.setEnabled(false);

            openfile.addActionListener(new ActionListener(){
                //File opener
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser filechoice = new JFileChooser();

                    int filestatus = filechoice.showOpenDialog(null);

                    if(filestatus == JFileChooser.APPROVE_OPTION){
                        openfile.setEnabled(false);

                        file = new File(filechoice.getSelectedFile().getAbsolutePath());
                        filename.setText(filename.getText() + " " + file.getName());


                        btnLexer.setEnabled(true);
                        BtnClear.setEnabled(true);
                        try{
                            Scanner scan = new Scanner(file);
                            input = scan.nextLine();
                            codetext.setText(codetext.getText() + " " + input);
                            scan.close();
                        }
                        catch(FileNotFoundException e1){
                            System.out.println("File Not Found!");
                        }

                    }
                    else{
                        System.out.println("No File selected. try Again");
                        codetext.setText("");
                    }


                }
            }); // end of openfile action event

            btnLexer.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        lexer();
                    } catch (FileNotFoundException e1) {
                        System.out.println("File not found!");
                    } catch (IndexOutOfBoundsException e2){
                        System.out.println("Lexical Analysis Complete, Proceed.");
                        btnLexer.setEnabled(false);
                        btnSyntax.setEnabled(true);
                        BtnClear.setEnabled(true);
                    }
                    btnLexer.setEnabled(false);
                    btnSyntax.setEnabled(true);
                    BtnClear.setEnabled(true);
                }
            }); //end of lexical analysis action event

            btnSyntax.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        parser();

                        String parseresult = resulttext.getText();


                        if(parseresult.equals("Result: Correct Syntax")){
                            btnSemantic.setEnabled(true);
                            btnSyntax.setEnabled(false);
                            BtnClear.setEnabled(true);
                        }
                        else{
                            btnSyntax.setEnabled(false);
                            openfile.setEnabled(true);
                            BtnClear.setEnabled(true);
                        }

                    }
                    catch(FileNotFoundException e1){
                        System.out.println("File Not Found!");
                    }
                }
            }); // end of syntax analysis action event

            btnSemantic.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        semant();
                    }
                    catch(FileNotFoundException e1){
                        System.out.println("File not found!");
                    }
                }
            }); // end of semantic analysis action event

            BtnClear.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    openfile.setEnabled(true);
                    resulttext.setText("Result: ");
                    codetext.setText("Code: ");
                    filename.setText("Filename: ");
                    finish.setText("Feedback: ");
                    BtnClear.setEnabled(false);
                    btnLexer.setEnabled(false);
                    btnSyntax.setEnabled(false);
                    btnSemantic.setEnabled(false);
                }
            }); //end of btnClear action event

        }  // end of initialize method


        public static void main(String[] args) throws Exception {

            MainApplication myFrame = new MainApplication();
            myFrame.initialize();

        } //end of main class

        // start of different analysis methods.

        // Start of lexical analysis method
        public static String removeDuplicateWords(String multiple)
        {

            // Regex to matching repeated words.
            //remove white spaces
            //include all a-z lower uper
            String regex
                    = "\\b(\\w+)(?:\\W+\\1\\b)+";
            Pattern p
                    = Pattern.compile(
                    regex,
                    Pattern.CASE_INSENSITIVE);

            // Pattern class contains matcher() method
            // to find matching between given sentence
            // and regular expression.
            Matcher m = p.matcher(multiple);

            // Check for subsequences of input
            // that match the compiled pattern
            while (m.find()) {
                multiple
                        = multiple.replaceAll(
                        m.group(),
                        m.group(1));
            }
            return multiple;
        }

        public void lexer() throws FileNotFoundException, IndexOutOfBoundsException{

            Scanner myReader = new Scanner(file);

            String nodot = myReader.nextLine();
            String nodot1 = nodot.replace(".", "");
            input = nodot1;


            boolean checker = false;
            for(int x = 0 ; x < input.length(); x++){


                String temp = "";

                // 2. Is the character a letter or a number?
                if (isAlpha(input.charAt(x)) || isNumber(input.charAt(x))) {
                    // 2.1. Collect all the letters
                    while (isAlpha(input.charAt(x)) || isNumber(input.charAt(x))) {
                        temp += input.charAt(x) + "";
                        x++;
                    }
                    // 2.2. is this a data type?
                    if(temp.equals("int") || temp.equals("String") || temp.equals("char") || temp.equals("double") ) {
                        // 2.2.1. print <data_type>
                        resulttext.setText(resulttext.getText() + "<Data type>");

                        continue;

                    }

                    if (checker) {
                        resulttext.setText(resulttext.getText() +"<Value>");
                        System.out.println("Value tag");


                    } else {
                        // if it's identifier, meaning the next is always value.
                        checker = true;
                        resulttext.setText(resulttext.getText() +"<identifier>" );


                    }
                    // there's no continue.
                }
                // 3. is this a assignment operator?
                if (input.charAt(x) == '=') {
                    resulttext.setText(resulttext.getText() +"<assignment operator>" );


                    continue;
                }

                // 4. is this a delimiter?
                if (input.charAt(x) == ';') {
                    resulttext.setText(resulttext.getText() +"<delimiter>" );


                }

            }
            String holder = resulttext.getText();
            String x = removeDuplicateWords(holder);
            resulttext.setText(x);
            myReader.close();
        } // end of lexical analysis method

        // start of syntax analysis method
        public void parser() throws FileNotFoundException{



            String[] datatype ={"int","double","String", "char"};



            Scanner parseread = new Scanner(file);
            input = parseread.nextLine();

            if(input.contains("=")){ // assignment_operator checker

                String[] split = input.split("=");
                String splitholder = split[1]; //value

                char delimit = splitholder.charAt(splitholder.length()-1);
                String[] splitdtandid = split[0].split(" "); //holds data type and identifier.

                System.out.println("Splitdtandid length " + splitdtandid.length);
                int dtid = splitdtandid.length;

                if(dtid == 2){ //left side checker

                    String idholder = splitdtandid[1];

                    Pattern num = Pattern.compile("[0-9]");
                    Matcher nummatch = num.matcher(splitholder);
                    boolean hasnum = nummatch.find();

                    Pattern alpha = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
                    Matcher alphamatch = alpha.matcher(idholder);
                    Matcher alphamatch2 = alpha.matcher(splitholder);
                    boolean hasalpha2 = alphamatch2.find();
                    boolean hasalpha = alphamatch.find();


                    System.out.println(splitholder + " " + delimit + splitdtandid[0] + " " + idholder);


                    if(Arrays.asList(datatype).contains(splitdtandid[0])){ //data_type checker
                        System.out.print("Data type detected, procceed");
                        if(hasalpha == true){ // identifier checker
                            System.out.print("identifier detected proceed");
                            if(hasalpha2 == true || hasnum == true){ //value checker
                                System.out.println("value detected");
                                if(delimit == ';'){ //delimiter checker
                                    System.out.println("delimit detected.");
                                    resulttext.setText("Result: Correct Syntax");
                                }
                                else{
                                    resulttext.setText("Result: Incorrect Syntax");
                                } //end of delimiter checker
                            }
                            else{
                                resulttext.setText("Result: Incorrect Syntax");
                            } //end of value checker
                        }
                        else{
                            resulttext.setText("Result: Incorrect Syntax");
                        } //end of identifier checker
                    }
                    else{
                        resulttext.setText("Result: Incorrect Syntax");
                    } // end of data_type checker
                }
                else{
                    resulttext.setText("Result: Incorrect Syntax");
                } // ends left side checker
            }
            else{
                resulttext.setText("Result: Incorrect Syntax"); //end of assignment_operator checker
            }

            parseread.close();
        }
// end of syntax analysis method

// Start of Semantic analysis method

        public void semant() throws FileNotFoundException{

            Scanner read = new Scanner(file);

            input = read.nextLine();

            codetext.setText(input);

            boolean checker = false;
            for(int x = 0 ; x < input.length(); x++){

                String temp = "";
                // 2. Is the character a letter or a number?
                if (isAlpha(input.charAt(x)) || isNumber(input.charAt(x))) {
                    // 2.1. Collect all the letters
                    while (isAlpha(input.charAt(x)) || isNumber(input.charAt(x))) {
                        temp += input.charAt(x) + "";
                        x++;
                    }
                    // 2.2. is this a data type?
                    if(temp.equals("int") || temp.equals("String") || temp.equals("char") || temp.equals("double") ) {
                        // 2.2.1. print <data_type>
                        if(temp.equals("int")){
                            z = "int";
                        }
                        if(temp.equals("String")){
                            z = "String";
                        }
                        if(temp.equals("char")){
                            z = "char";
                        }
                        if(temp.equals("double")){
                            z = "double";
                        }

                        continue;

                    }

                    if (checker) {
                        resulttext.setText(resulttext.getText() +"<Value>");


                    } else {
                        // if it's identifier, meaning the next is always value.
                        checker = true;
                        resulttext.setText(resulttext.getText() +"<identifier>" );


                    }
                    // there's no continue.
                }
                // 3. is this a assignment operator?
                if (input.charAt(x) == '=') {
                    resulttext.setText(resulttext.getText() +"<assignment_operator>" );


                    continue;
                }

                // 4. is this a delimiter?
                if (input.charAt(x) == ';') {
                    resulttext.setText(resulttext.getText() +"<delimiter>" );


                }
            }


            String[] spliter = input.split("=");
            String nospace = spliter[1].replaceAll(" ", "");
            String nodelimit = spliter[1].replaceAll(";","");
            String valueholder = nospace;
            String nolimit = nodelimit;
            String dot = ".";

            System.out.println(input); //Debug mode

            //number patterns
            Pattern num = Pattern.compile("[0-9]");
            Matcher nummatch = num.matcher(valueholder);
            boolean hasnum = nummatch.find();

            Pattern alpha = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
            Matcher alphamatch = alpha.matcher(valueholder);
            boolean hasalpha = alphamatch.find();

            System.out.println(z);
            System.out.print(valueholder);

            if(z.equals("int")){

                if(hasnum == true && hasalpha == false &&
                        valueholder.charAt(0) != 34 && nolimit.charAt(nolimit.length()-1) != 34 &&
                        valueholder.charAt(0) != 39 && nolimit.charAt(nolimit.length()-1) != 39 && !valueholder.contains(dot)){

                    resulttext.setText("Result: Semantically correct!");
                    finish.setText("No error found in file");
                    BtnClear.setEnabled(true);
                    btnSemantic.setEnabled(false);

                }
                else{
                    resulttext.setText("Result: Semantically Incorrect!");
                    BtnClear.setEnabled(true);
                    openfile.setEnabled(true);
                    btnSemantic.setEnabled(false);
                }
            }
            //check if string then check if value start and end with double quotes;
            else if(z.equals("String")){
                System.out.println("Has String 1");
                if(valueholder.charAt(0) == 34 && nolimit.charAt(nolimit.length()-1) == 34){

                    resulttext.setText("Result: Semantically correct!");
                    finish.setText("No error found in file");
                    BtnClear.setEnabled(true);
                    btnSemantic.setEnabled(false);

                }
                else{
                    resulttext.setText("Result: Semantically Incorrect!");
                    BtnClear.setEnabled(true);
                    openfile.setEnabled(true);
                    btnSemantic.setEnabled(false);
                }
            }
            // check if char then check if value start and end with single quote
            else if(z.equals("char")){
                System.out.println("Has Char");
                if(valueholder.charAt(0) == 39 && nolimit.charAt(nolimit.length()-1) == 39){

                    resulttext.setText("Result: Semantically correct!");
                    finish.setText("No error found in file");
                    BtnClear.setEnabled(true);
                    btnSemantic.setEnabled(false);

                }
                else{
                    resulttext.setText("Result: Semantically Incorrect!");
                    BtnClear.setEnabled(true);
                    openfile.setEnabled(true);
                    btnSemantic.setEnabled(false);
                }

            }
            //check if double then check if value has number and dot / no double quotes / no single quotes / no letter
            else if(z.equals("double")){
                if(hasnum == true && hasalpha ==false && valueholder.contains(dot) && //check three things if value has number and dot and no letter
                        valueholder.charAt(0) != 34 && nolimit.charAt(nolimit.length()-1) != 34 &&
                        valueholder.charAt(0) != 39 && nolimit.charAt(nolimit.length()-1) != 39){

                    resulttext.setText("Result: Semantically correct!");
                    finish.setText("No error found in file");
                    BtnClear.setEnabled(true);
                    btnSemantic.setEnabled(false);

                }
                else{
                    resulttext.setText("Result: Semantically Incorrect!");
                    BtnClear.setEnabled(true);
                    openfile.setEnabled(true);
                    btnSemantic.setEnabled(false);
                }

            }


            read.close();
        }

        // end of semantic analysis method


    }//end of MainApplication


