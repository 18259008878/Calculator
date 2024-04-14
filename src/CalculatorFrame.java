import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class CalculatorFrame extends JFrame implements ActionListener {
    public CalculatorFrame() {
        setTitle("计算器");
        setSize(1200,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        leftPanel.setPreferredSize(new Dimension(850,800));

        processText.setEditable(false);
        processText.setFont(new Font("黑体", Font.PLAIN, 25));
        processText.setHorizontalAlignment(JTextField.RIGHT);
        processText.setBorder(BorderFactory.createMatteBorder(25, 3, 0, 3, new Color(181, 181, 181)));
        processText.setBackground(new Color(181, 181, 181));
        processText.setPreferredSize(new Dimension(850,50));

        resultText.setEditable(false);
        resultText.setFont(new Font("黑体", Font.BOLD, 55));
        resultText.setHorizontalAlignment(JTextField.RIGHT);
        resultText.setBorder(BorderFactory.createMatteBorder(0, 3, 5, 3, new Color(181, 181, 181)));
        resultText.setBackground(new Color(181, 181, 181));
        resultText.setPreferredSize(new Dimension(850,50));

        for (int i=0;i<20;++i) {
            button[i]=new JButton(buttonExpr[i]);
            button[i].setPreferredSize(new Dimension(95,50));
            if (!buttonExpr[i].equals("0")&&!buttonExpr[i].equals("1")&&!buttonExpr[i].equals("2")
            &&!buttonExpr[i].equals("3")&&!buttonExpr[i].equals("4")&&!buttonExpr[i].equals("5")
            &&!buttonExpr[i].equals("6")&&!buttonExpr[i].equals("7")&&!buttonExpr[i].equals("8")
            &&!buttonExpr[i].equals("9")&&!buttonExpr[i].equals(".")&&!buttonExpr[i].equals("+/-")) {
                button[i].setBackground(new Color(232, 232, 232));
                button[i].setForeground(Color.black);
                button[i].setFont(new Font(Font.SERIF, Font.PLAIN, 30));
            } else {
                button[i].setBackground(Color.white);
                button[i].setFont(new Font("黑体", Font.BOLD, 40));
            }
            if (buttonExpr[i].equals("=")) {
                button[i].setBackground(new Color(126, 192, 238));
            }
            button[i].setBorderPainted(false);

            buttonPanel.add(button[i]);
        }

        for (int i=0;i<buttonExpr.length;++i) {
            button[i].addActionListener(this);
        }

        buttonPanel.setPreferredSize(new Dimension(850,600));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(5, 3, 5, 3, new Color(181, 181, 181)));

        leftPanel.add(processText,BorderLayout.NORTH);
        leftPanel.add(resultText,BorderLayout.CENTER);
        leftPanel.add(buttonPanel,BorderLayout.SOUTH);

        rightPanel.setPreferredSize(new Dimension(350,800));
        rightPanel.setBorder(null);

        logRecord.setEditable(false);
        logRecord.setBackground(new Color(181, 181, 181));
        logRecord.setFont(new Font("黑体", Font.BOLD, 25));
        logRecord.setBorder(null);

        logDelButton.setBackground(new Color(181, 181, 181));
        logDelButton.setFont(new Font("黑体", Font.PLAIN, 25));
        logDelButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logDelButton.setBorder(null);
        logDelButton.addActionListener(this);

        rhtPanel.setViewportView(logRecord);
        rhtPanel.setBorder(null);

        rightPanel.add(rhtPanel,BorderLayout.CENTER);
        rightPanel.add(logDelButton,BorderLayout.SOUTH);

        add(leftPanel,BorderLayout.CENTER);
        add(rightPanel,BorderLayout.EAST);

        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command=e.getActionCommand();
        if (lastOperation.equals("=")) {
            processText.setText("");
        }
        if (command.equals("C")) {
            serveForC();
        } else if (CalculatorUtil.isNumber(command)||command.equals(".")) {
            serveForNumber(command);
        } else if (command.equals("=")) {
            serveForEqualSignal();
        } else if (command.equals("←")) {
            serveForBackSpace();
        } else if (command.equals("+")||command.equals("－")||command.equals("×")
        ||command.equals("÷")||command.equals("%")) {
            serveForDoubleOperation(command);
        } else if (command.equals("清空")){
            logRecord.setText("尚无历史记录");
        } else {
            serveForSinglrOperation(command);
        }
        lastCommand=command;
    }

    private void serveForSinglrOperation(String command) {
        if (command.equals("+/-")) {
            if (!number.equals("0")&&!number.equals("0.")) {
                if (lastCommand.equals("+/-")) {
                    number=number.substring(1);
                } else {
                    number="-"+number;
                }
            }
            resultText.setText(number);
        }
    }

    private void serveForDoubleOperation(String command) {
        if (!lastCommand.equals("+")&&!lastCommand.equals("－")&&!lastCommand.equals("×")
        &&!lastCommand.equals("÷")&&!lastCommand.equals("%")) {
            expr.add(number);
            processText.setText(processText.getText()+resultText.getText()+command);
            if (CalculatorUtil.getPriority(lastOperation)>=CalculatorUtil.getPriority(command)) {
                number=CalculatorUtil.calDAO(expr);
                resultText.setText(number);
                expr.clear();
                expr.add(number);
            }
            expr.add(command);
        } else {
            expr.remove(expr.size()-1);
            expr.add(command);
            if ((processText.getText().contains("+")||processText.getText().contains("－"))
            &&(command.equals("×")||command.equals("÷")||command.equals("%"))) {
                processText.setText('('+processText.getText().substring(0,processText.getText().length()-1)+')'+command);
            } else {
                processText.setText(processText.getText().substring(0,processText.getText().length()-1)+command);
            }
        }
        
        lastOperation=command;
        isFirstNumber=true;
    }

    private void serveForBackSpace() {
       if (number.length()>1) {
            number=number.substring(0, number.length()-1); 
        } else {
            number="0";
            isFirstNumber=true;
        }
        resultText.setText(number);
    }

    private void serveForEqualSignal() {
        expr.add(number);
        processText.setText(processText.getText()+number+"=");
        resultText.setText(CalculatorUtil.calDAO(expr));
        if (logRecord.getText().equals("尚无历史记录")) {
            logRecord.setText("");
        }
        logRecord.append(processText.getText()+'\n'+resultText.getText()+"\n\n");
        number=CalculatorUtil.calDAO(expr);
        expr.clear();
        isFirstNumber=true;
        lastOperation="=";
    }

    private void serveForNumber(String command) {
        if (command.equals(("."))) {
            if (resultText.getText().indexOf(".")<0) {
                number+=".";
                resultText.setText(resultText.getText()+".");
            }
        } else if (command.equals("0")) {
            if (!number.equals("0")) {
                if (isFirstNumber) {
                    number=command;
                    resultText.setText(number);
                } else {
                    number+=command;
                    resultText.setText(resultText.getText()+command);
                }
            }
        } else {
            if (isFirstNumber) {
                number=command;
                resultText.setText(number);
            } else {
                number+=command;
                resultText.setText(resultText.getText()+command);
            }
        }

        isFirstNumber=false;
    }

    private void serveForC() {
        processText.setText("");
        resultText.setText("0");
        number="0";
        expr.clear();
        isFirstNumber=true;
        lastCommand=lastOperation="=";
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new CalculatorFrame();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JPanel leftPanel=new JPanel(new BorderLayout());
    private JTextField processText=new JTextField();
    private JTextField resultText=new JTextField("0");
    private JPanel buttonPanel=new JPanel(new GridLayout(5,4,4,4));
    private String[] buttonExpr = {"%", "÷", "C", "←","7", "8", "9",
            "×", "4", "5", "6", "－", "1", "2", "3", "+", "+/-", "0", ".", "="};
    private JButton button[]=new JButton[buttonExpr.length];

    private JScrollPane rhtPanel=new JScrollPane();
    private JPanel rightPanel=new JPanel(new BorderLayout());
    private JButton logDelButton=new JButton("清空");
    private JTextArea logRecord=new JTextArea("尚无历史记录");

    private List<String> expr=new ArrayList<>();
    private String number="0";
    private boolean isFirstNumber=true;
    private String lastOperation="=";
    private String lastCommand="=";
}