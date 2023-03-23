package com.example.kalkulator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class HelloController {
    public TextArea dnevnik;
    public TextField prikazovalo;
    public Label status;
    public TextArea akcija;


    @FXML
    private Label welcomeText;
    boolean bool = false;
    boolean prejsna = false;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void dodajCB(ActionEvent actionEvent) {
        String beseda = ((Button) (actionEvent).getSource()).getText();
        System.out.println(beseda);
        if (bool) prikazovalo.setText("");
        String[] A = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Set<String> set1 = new HashSet<String>(Arrays.asList(A));

        if (beseda.equals("=")) {

            System.out.println(prikazovalo.getText());
            double result = evaluate(prikazovalo.getText());
            dnevnik.appendText(prikazovalo.getText() + " = " + result + "\n");
            prikazovalo.setText("");
            System.out.println(result);
            prikazovalo.setText(Double.toString(result));
            bool = true;
        } else {
            if (set1.contains(beseda) && prejsna) {
                prikazovalo.setText(prikazovalo.getText() + " " + beseda);
                prejsna = false;
            } else if (set1.contains(beseda) && !prejsna) {
                prikazovalo.setText(prikazovalo.getText() + beseda);
                prejsna = false;
            } else {
                prikazovalo.setText(prikazovalo.getText() + " " + beseda);
                prejsna = true;
            }
            bool = false;

        }
    }

    public static double evaluate(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            char c = token.charAt(0);
            if (Character.isDigit(c)) {
                operands.push(Double.parseDouble(token));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                    double op2 = operands.pop();
                    double op1 = operands.pop();
                    char op = operators.pop();
                    double result = applyOp(op, op1, op2);
                    operands.push(result);
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            double op2 = operands.pop();
            double op1 = operands.pop();
            char op = operators.pop();
            double result = applyOp(op, op1, op2);
            operands.push(result);
        }

        return operands.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    private static double applyOp(char op, double op1, double op2) {
        switch (op) {
            case '+':
                return op1 + op2;
            case '-':
                return op1 - op2;
            case '*':
                return op1 * op2;
            case '/':
                if (op2 == 0) {
                    throw new ArithmeticException("division by zero");
                }
                return op1 / op2;
            default:
                throw new IllegalArgumentException("invalid operator: " + op);
        }
    }


    public void nazajCB(ActionEvent actionEvent) {
        if (prikazovalo.getLength() > 0) {
            if (prikazovalo.getText().charAt(prikazovalo.getLength() - 1) == ' ')
                prikazovalo.setText(prikazovalo.getText().substring(0, prikazovalo.getText().length() - 2));
            else prikazovalo.setText(prikazovalo.getText().substring(0, prikazovalo.getText().length() - 1));
        }
    }

    public void odpriCB(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if (f != null) {
            akcija.setText(akcija.getText() + "Uspešno odpiranje datoteke: " + f.getName() + ", velikost: " + f.length() + " B");
            String izvajanjeOperacij = "Odpiranje datoteke: " + f.getName() + " " + f.length() + "B\n";
            status.setText(izvajanjeOperacij + "\n");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null)
                    dnevnik.appendText(line + "\n");
                br.close();
            } catch (Exception e) {
                status.setText("Neuspešno odpiranje datoteke");
                akcija.setText(akcija.getText() +"Neuspešno odpiranje datoteke");
            }
        }
    }

    public void shraniCB(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File f = fc.showSaveDialog(null);
        if (f != null) {
            status.setText("Uspešno shranjevanje datoteke");
            akcija.setText(akcija.getText() + "Shranjevanje datoteke" + "\n");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write(dnevnik.getText());
                bw.close();

            } catch (Exception e) {
                status.setText("Neuspešno shranjevanje datoteke");
                akcija.setText(akcija.getText() +"Neuspešno shranjevanje datoteke\n" );
            }
        }

    }

    public void pobrisiCB(ActionEvent actionEvent) {
        status.setText("Status: ");
        dnevnik.setText("");
        prikazovalo.setText("");

    }

    public void zapriCB(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void avtorCB(ActionEvent actionEvent) {
        status.setText("Avtor programa: Žan Pičman");
    }
}