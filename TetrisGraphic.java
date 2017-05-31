import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TetrisGraphic {
    private JFrame frame;
    private TestPane testPane;

    public TetrisGraphic() {
        this.frame = new JFrame("Tetris Grid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void dispose(){
		frame.dispose();
    }
    
    public void updateGrid(int[][] grid){
        testPane = new TestPane(grid);
        this.frame.remove(testPane);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                frame.add(testPane);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        try{
            Thread.sleep(0);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    public class TestPane extends JPanel {
        public TestPane(int[][] grid) {
            setLayout(new GridBagLayout());
            Random rnd = new Random();
            Color[] colors = new Color[]{Color.GREEN, Color.BLUE};
            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.gridy = 0;
            for(int i = 0; i < grid.length; i++){
                gbc.gridx = 0;
                for(int j = 0; j < grid[0].length; j++){
                    JPanel cell = new JPanel(){
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(15, 15);
                        }
                    };
                    int color = (grid[i][j] == 1) ? 0 : 1;
                    cell.setBackground(colors[color]);
                    add(cell, gbc);
                    gbc.gridx++;
                }
                gbc.gridy++;
            }
        }
    }
}