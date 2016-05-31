/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeToBreak;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 *
 * @author Artur
 */
public class TimeToBreak {

    /**
     * @param args the command line arguments
     */
    static JFrame frame;
    static TrayIcon trayIcon;
    static JPanel panel;
    static JLabel jlabel;
    static JButton jbutton;
    
    public static void initialize()
    {
        frame = new JFrame();
        frame.setResizable(false);
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(140, 100));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jbutton = new JButton();
        jbutton.setText("Ukryj");
        jbutton.setSize(20, 40);
        jlabel = new JLabel();
        jlabel.setText("");
        panel.add(jlabel); 
        panel.add(jbutton);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    static int timetobreak = 3600;
    
    final static SystemTray systemTray = SystemTray.getSystemTray();
    
    static void setTrayIcon()
    {
        Dimension size = systemTray.getTrayIconSize();
        BufferedImage bufferedimage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedimage.getGraphics();

        graphics.setColor(Color.blue);
        graphics.fillRect(0, 0, size.width, size.height);
        PopupMenu popup = new PopupMenu();
        MenuItem miExit = new MenuItem("Exit");
        miExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              System.exit(0);
            }
        });
        popup.add(miExit);
        trayIcon = new TrayIcon(bufferedimage, timetobreak+"", popup);
    }
    
    static void addMethodToTrayIcon()
    {
            if (SystemTray.isSupported()) {
           
            MouseMotionListener mousemotionlistener = new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                  }

                @Override
                public void mouseMoved(MouseEvent e) {
                  int minutes = timetobreak/60;
                  int seconds = timetobreak%60;
                  String time = minutes + " minut " + seconds + " sekund";
                  trayIcon.displayMessage("Time", time, TrayIcon.MessageType.INFO);   
                }
            };
            
            MouseAdapter md = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    systemTray.remove(trayIcon);
                    frame.setVisible(true);
                }
            };
            
            trayIcon.addMouseMotionListener(mousemotionlistener);
            trayIcon.addMouseListener(md);
            trayIcon.setImageAutoSize(true);
          }
    }
    
    
    static void countDown()
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Timer timer = new Timer();
                
                timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                int min = timetobreak/60;
                int sec = timetobreak%60;
                jlabel.setText("Min: " + min + " sek: " + sec + "");
                timetobreak = timetobreak - 1;
                if(timetobreak == 0)
                {
                    JOptionPane.showMessageDialog(null, "Czas na przerwę.");
                    timetobreak = 65*60;
                }}
              }, 1, 1000);
            }
        });
    }
    
    static void addActionToButton()
    {
        jbutton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
           try {
                systemTray.add(trayIcon);
                frame.setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
         }
     });
    }
    
    public static void main(String[] args) throws AWTException{
        // TODO code application logic here
        initialize();
        setTrayIcon();
        addMethodToTrayIcon();
        countDown();
        addActionToButton();
    }
}