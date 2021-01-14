import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static javax.swing.UIManager.setLookAndFeel;

public class Console {
    public static void main(String[] args) {
        new Console();

    }

    public JFrame frame;
    public JTextPane console;
    public JTextField input;
    public JScrollPane scrollpane;

    public StyledDocument document;

    boolean trace = false;

    ArrayList<String> recent = new ArrayList<String>();
    int recent_in= 0;
    int recent_max= 10;

    boolean floop = false;
    int loop_times= 1;
    int loop_temp = 1;



    public Console(){
        try {
            setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        } catch (Exception e) { }
            frame = new JFrame();
            frame.setTitle("Tolu's Console");
            frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));

            console = new JTextPane();
            console.setEditable(false);
            console.setFont(new Font("Times New Romans", Font.PLAIN, 11));
            console.setOpaque(false);

            document = console.getStyledDocument();

            input = new JTextField();

            input.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = input.getText();

                    if(text.length() > 1)
                    {
                        recent.add(text);
                        recent_in = 0;
                        doCommand(text);
                        scrollBottom();
                        input.selectAll();
                    }


                }
            });

            input.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP)
                    {
                        if ((recent_in < recent_max - 1) && (recent_in < (recent.size()-1)))
                        {
                            recent_in++;
                        }
                        input.setText(recent.get(recent.size()-1 - recent_in));
                    }else if (e.getKeyCode()== KeyEvent.VK_DOWN)
                    {
                        if (recent_in> 0)
                        {
                            recent_in --;
                        }
                        input.setText(recent.get(recent.size()-1 - recent_in));

                    }

                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            scrollpane = new JScrollPane(console);
            scrollpane.setOpaque(false);
            scrollpane.getViewport().setOpaque(false);

            frame.add(input, BorderLayout.SOUTH);
            frame.add(scrollpane, BorderLayout.CENTER);

            frame.getContentPane().setBackground(new Color(0, 0, 0));

            frame.setSize(600, 350);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);


    }

    public void scrollTop()
    {
        console.setCaretPosition(0);

    }
    public void scrollBottom()
    {
        console.setCaretPosition(console.getDocument().getLength());

    }
    public void print (String s, boolean trace)
    {
        print(s, trace, new Color (255, 255,255));
    }

    public void print(String s, boolean trace, Color c)
    {
        Style style =console.addStyle("Style", null);
        StyleConstants.setForeground(style, c);

        if (trace)
        {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            String caller = elements[0].getClassName();

            s = caller +  "-->" + s;
        }
        try
        {
            document.insertString(document.getLength(), s, style );
        }
        catch (Exception ex){}
    }

    public void println (String s, boolean trace)
    {
        println(s, trace, new Color (255, 255, 255));

    }

    public void println(String s, boolean trace, Color c)
    {
        print(s+ "\n", trace,c );

    }
    public void clear()
    {
        try
        {
            document.remove(0 , document.getLength());
        }
        catch (Exception ex) {}
    }


    public void doCommand(String s)
    {
        final String[] commands = s.split(" ");

        for(int j =0; j<loop_times; j++ )
        {
            try
            {
                if (commands[0].equalsIgnoreCase("bye"))
                {
                    clear();
                } else if(commands[0].equalsIgnoreCase("hello"))
                {
                    String message = "";
                    for (int i = 1; i < commands.length; i++){
                        message += commands[i];
                        if (i != commands.length -1)
                        {
                            message += " ";
                        }
                    }
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                }else if (commands[0].equalsIgnoreCase(("start"))){
                    String site = "";
                    for (int i = 1; i < commands.length; i++){
                        site += commands[i];
                        if (i != commands.length -1)
                        {
                            site += " ";
                        }
                    }

                    String cmd = "cmd.exe /c start " + site;
                    Runtime.getRuntime().exec(cmd);


                }else if (commands[0].equalsIgnoreCase("trace"))
                {
                    if (commands[1].equalsIgnoreCase("true"))
                    {
                        trace = true;
                        println("Tracing", trace, new Color (155,155,100));

                    }else if (commands[1].equalsIgnoreCase("false"))
                    {
                        trace = false;
                        println("not Tracing", trace, new Color (155,155,100));
                    }

                }else if (commands[0].equalsIgnoreCase("close"))
                {
                        frame.dispose();
                }
                else{
                    println(s,trace, new Color (255, 255, 255));
                }

            }
            catch (Exception ex)
            {
                println(" Error : " + ex.getMessage(), trace, new Color(255, 155, 233));
            }

        }
        if (floop)
        {
            loop_times = loop_temp;
            loop_temp = 1;
        }else
            {
                loop_times = 1;
                loop_temp = 1;
            }



    }


}






